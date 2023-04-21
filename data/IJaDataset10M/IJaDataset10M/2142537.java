package phex.gui.common;

import java.awt.*;
import javax.swing.*;

public class SplashWindow extends JWindow {

    public static final String SPLASH_IMAGE_NAME = "/phex/resources/splash.jpg";

    private Image image;

    public SplashWindow() {
        ImageIcon icon = new ImageIcon(SplashWindow.class.getResource(SPLASH_IMAGE_NAME));
        image = icon.getImage();
        setSize(image.getWidth(null) + 4, image.getHeight(null) + 4);
        BaseFrame.centerWindow(this, new Point(0, 0));
    }

    public void showSplash() {
        setVisible(true);
        repaint();
    }

    public void paint(Graphics g) {
        g.drawImage(image, 2, 2, null);
        g.setColor(Color.white);
        g.drawLine(0, 0, 0, image.getHeight(null) + 3);
        g.drawLine(0, 0, image.getWidth(null) + 3, 0);
        g.setColor(Color.lightGray);
        g.drawLine(1, 1, 1, image.getHeight(null) + 2);
        g.drawLine(1, 1, image.getWidth(null) + 2, 1);
        g.setColor(Color.black);
        g.drawLine(0, image.getHeight(null) + 3, image.getWidth(null) + 3, image.getHeight(null) + 3);
        g.drawLine(image.getWidth(null) + 3, 0, image.getWidth(null) + 3, image.getHeight(null) + 3);
        g.setColor(Color.darkGray);
        g.drawLine(1, image.getHeight(null) + 2, image.getWidth(null) + 2, image.getHeight(null) + 2);
        g.drawLine(image.getWidth(null) + 2, 1, image.getWidth(null) + 2, image.getHeight(null) + 2);
    }
}
