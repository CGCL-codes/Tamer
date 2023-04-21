package freefret;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class RightArrowImage extends Container {

    java.net.URL imgURL = getClass().getResource("/img/icons/RightArrow.png");

    private ImageIcon arrowImgIco = new ImageIcon(imgURL);

    private JLabel arrowLabel = new JLabel(arrowImgIco);

    public RightArrowImage() {
        this.setPreferredSize(new Dimension(arrowImgIco.getIconHeight(), arrowImgIco.getIconWidth()));
        this.add(arrowLabel);
        arrowLabel.setBounds(0, 0, arrowImgIco.getIconWidth(), arrowImgIco.getIconHeight());
    }
}
