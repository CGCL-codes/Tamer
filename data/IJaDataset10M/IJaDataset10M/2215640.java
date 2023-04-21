package editor;

import java.awt.*;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.*;
import javax.imageio.*;

public final class MyCanvas extends java.awt.Canvas {

    private BufferedImage image;

    private boolean imageExists = false;

    /** Creates a new instance of MyCanvas */
    public MyCanvas() {
        super();
    }

    /** sets image for canvas to display */
    public void setImage(BufferedImage img) {
        image = img;
        if (image != null) {
            imageExists = true;
            setSize(image.getWidth(), image.getHeight());
        } else {
            imageExists = false;
            setSize(0, 0);
        }
    }

    /** returns image or null, if none exists*/
    public BufferedImage getImage() {
        if (imageExists) return image; else return null;
    }

    /** removes the image, canvas displays nothing */
    public void invalidateImage() {
        imageExists = false;
        image = null;
        setSize(0, 0);
    }

    /** repaint canvas */
    public void paint(Graphics g) {
        Graphics2D gr = (Graphics2D) g;
        if (imageExists) {
            gr.drawImage(image, 0, 0, null);
        }
    }

    public void update(Graphics g) {
        paint(g);
    }
}
