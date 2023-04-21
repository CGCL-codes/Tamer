package jgrit.gui.img;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * Simple component that displays an image
 * 
 * @author Sam Hartsfield
 */
public class ImageCanvas extends JComponent {

    /** Arbitrary version identifier */
    private static final long serialVersionUID = 7023368072703619796L;

    /**
	 * The image that will be displayed in the component
	 */
    private BufferedImage img = null;

    /**
	 * @return the img
	 */
    public BufferedImage getImg() {
        return img;
    }

    /**
	 * @param img the new BufferedImage to draw
	 */
    public void setImage(BufferedImage img) {
        this.img = img;
        revalidate();
        repaint();
    }

    /**
	 * Creates an empty canvas
	 */
    public ImageCanvas() {
    }

    public ImageCanvas(File imageFile) {
        loadImage(imageFile);
    }

    public ImageCanvas(BufferedImage img) {
        setImage(img);
    }

    /**
	 * Load an image
	 */
    public void loadImage(File imageFile) {
        try {
            setImage(ImageIO.read(imageFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
    }

    @Override
    public Dimension getPreferredSize() {
        if (img == null) {
            return new Dimension(100, 100);
        } else {
            return new Dimension(img.getWidth(), img.getHeight());
        }
    }

    /**
	 * @param args ignored
	 */
    public static void main(String[] args) {
        ImageCanvas iv = new ImageCanvas(new File("E:/ComputerScience/SrDesign/resources/grit/grit/RacoonMarioKick.png"));
        JFrame frame = new JFrame("JGrit Image Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(iv);
        frame.pack();
        frame.setVisible(true);
    }
}
