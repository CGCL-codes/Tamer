package example;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class SwingFrame extends JFrame {

    private static final long serialVersionUID = 5833257921262775575L;

    private JLabel image;

    public SwingFrame() {
        createGUI();
    }

    private void createGUI() {
        image = new JLabel();
        this.getContentPane().add(image);
        this.pack();
    }

    public void setImage(BufferedImage bufImage) {
        image.setIcon(new ImageIcon(bufImage));
        image.setPreferredSize(new Dimension(bufImage.getWidth(), bufImage.getHeight()));
        this.pack();
    }
}
