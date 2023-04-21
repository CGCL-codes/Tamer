package org.designerator.media.image.util.test;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import java.awt.geom.*;
import com.sun.image.codec.jpeg.*;

public class EdgeDetector {

    public static void main(String[] args) {
        EdgeDetector ip = new EdgeDetector();
        if (args.length < 1) {
            System.out.println("Enter a valid JPEG file name");
            System.exit(0);
        }
        ip.loadAndDisplay(args[0]);
    }

    public void loadAndDisplay(String filename) {
        BufferedImage img = readAsBufferedImage(filename);
        ConvolveOp sobelOp = getSobelVertOp();
        BufferedImage destImage = createEdgeImage(img, sobelOp);
        displayImage(destImage);
    }

    public static void displayImage(BufferedImage img) {
        JFrame fr = new JFrame();
        ImagePanel pan = new ImagePanel(img);
        pan.setSize(256, 256);
        fr.getContentPane().add(pan);
        fr.pack();
        fr.setSize(256, 256);
        fr.show();
    }

    static class ImagePanel extends JComponent {

        protected BufferedImage image;

        public ImagePanel() {
        }

        public ImagePanel(BufferedImage img) {
            image = img;
        }

        public void setImage(BufferedImage img) {
            image = img;
        }

        public void paintComponent(Graphics g) {
            Rectangle rect = this.getBounds();
            if (image != null) {
                g.drawImage(image, 0, 0, rect.width, rect.height, this);
            }
        }
    }

    public static BufferedImage readAsBufferedImage(String filename) {
        try {
            FileInputStream fis = new FileInputStream(filename);
            JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(fis);
            BufferedImage bi = decoder.decodeAsBufferedImage();
            return bi;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static BufferedImage createEdgeImage(BufferedImage srcImage, BufferedImageOp op) {
        BufferedImage destImage = op.createCompatibleDestImage(srcImage, srcImage.getColorModel());
        destImage = op.filter(srcImage, destImage);
        return destImage;
    }

    public static ConvolveOp getSobelVertOp() {
        float sbvMatrix[] = { -1.0f, -2.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f };
        Kernel kernel = new Kernel(3, 3, sbvMatrix);
        return getConvolveOp(kernel);
    }

    public static ConvolveOp getSobelHorizOp() {
        float sbhMatrix[] = { 1.0f, -0.0f, -1.0f, 2.0f, 0.0f, -2.0f, 1.0f, 0.0f, -1.0f };
        Kernel kernel = new Kernel(3, 3, sbhMatrix);
        return getConvolveOp(kernel);
    }

    public static ConvolveOp getConvolveOp(Kernel kernel) {
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, hints);
        return op;
    }
}
