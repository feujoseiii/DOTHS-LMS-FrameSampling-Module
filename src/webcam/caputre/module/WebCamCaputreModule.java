
package webcam.caputre.module;


import static org.bytedeco.javacpp.opencv_core.cvFlip;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_core.IplImage;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.VideoInputFrameGrabber;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javafx.event.ActionEvent;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class WebCamCaputreModule implements Runnable {
    
    private static JButton startBtn;
    private static JButton stopBtn;
    private static JPanel footerPanel;
    private static JLabel imgLabel;
    private static Boolean hasStarted = true;
    
    private static CanvasFrame canvas = new CanvasFrame("Frame Capture Demo");
    private static FrameGrabber grabber = new VideoInputFrameGrabber(0); 
    private static OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
    
    public WebCamCaputreModule() {
        createInterface();
    }
    
    public void createInterface(){
        canvas.setSize(620, 480);
        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        canvas.setLocationRelativeTo(null);
        canvas.setLayout(new BorderLayout());
        
        imgLabel = new JLabel();
        imgLabel.setPreferredSize(new Dimension(canvas.getWidth(),canvas.getHeight()));
        canvas.add(imgLabel);
        
        // Buttons para dagdag pogi points
        footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        startBtn = new JButton("Start");
        startBtn.setPreferredSize(new Dimension(canvas.getSize().width/2,20));
        footerPanel.add(startBtn);
        
        stopBtn = new JButton("Stop");
        stopBtn.setPreferredSize(new Dimension(canvas.getSize().width/2,20));
        footerPanel.add(stopBtn);
        
        canvas.add(footerPanel,BorderLayout.SOUTH);
        // Buttons para dagdag pogi points
        
        canvas.pack();
    }
    
    @Override
    public void run(){
        if(hasStarted){
            int i=0;
            try {
                grabber.start();
                grabber.setAspectRatio(CV_PI);
                while(true){
                    IplImage frame = converter.convert(grabber.grab());
                    if(frame != null){
                        String filename = "frame-" + i++ + ".jpg";
                        cvSaveImage(filename, frame);
                        System.out.println("Log: Image saved " + filename);
                    }
                    canvas.showImage(grabber.grab());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    

    public static void main(String[] args) {
        WebCamCaputreModule capture = new WebCamCaputreModule();
        capture.run();
    }
    
}


