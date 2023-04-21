package synthlabgui.presentation.impl;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class AboutView extends JDialog {

    AboutView() {
        Icon image = new ImageIcon("about.jpg");
        JLabel label = new JLabel(image);
        add(label);
        setTitle("About us");
        setSize(image.getIconWidth(), image.getIconHeight());
        System.out.println(image.getIconHeight() + "  " + image.getIconWidth());
        setModal(true);
        setVisible(true);
    }

    public static void main(String args[]) {
        new AboutView();
    }
}
