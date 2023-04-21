package javaapplication1;

import java.awt.*;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author seismicstuff
 */
public class SSHeaderPanel extends JPanel {

    private Image myImage = null;

    SSSettings mysettings;

    public SSHeaderPanel(SSSettings s) {
        mysettings = s;
        init();
    }

    private void init() {
        File file = new File("images/aamiadheader.gif");
        System.out.println("file.exists(): " + file.exists());
        ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath());
        myImage = imageIcon.getImage();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(myImage, 0, 0, super.getWidth(), super.getHeight(), this);
    }
}
