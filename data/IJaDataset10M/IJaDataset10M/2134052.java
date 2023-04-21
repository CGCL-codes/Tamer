package bitext2tmx.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;
import bitext2tmx.util.Utilities;

public class SplashScreen extends JWindow {

    public SplashScreen() {
        ImageIcon image = null;
        JLabel label;
        try {
            label = new JLabel(Utilities.getIcon("b2t-splash.png", this));
        } catch (Exception ex) {
            label = new JLabel("Error: unable to load image!");
        }
        getContentPane().add(label);
        pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (dim.getWidth() - getWidth()) / 2;
        int y = (int) (dim.getHeight() - getHeight()) / 2;
        setLocation(x, y);
    }

    public void display() {
        pack();
        setVisible(true);
    }

    public void remove() {
        setVisible(false);
        dispose();
    }
}
