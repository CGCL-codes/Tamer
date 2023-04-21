package org.dlib.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

    private ImageIcon image = new ImageIcon();

    private int margin = 0;

    public ImagePanel() {
    }

    public ImagePanel(String imageFile) {
        setImage(imageFile);
    }

    public void setImage(String imageFile) {
        image = new ImageIcon(imageFile);
        updatePrefSize();
    }

    public void setMargin(int m) {
        margin = m;
        updatePrefSize();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image.getImage() != null) g.drawImage(image.getImage(), margin, margin, this);
    }

    private void updatePrefSize() {
        int iw = image.getIconWidth();
        int ih = image.getIconHeight();
        setPreferredSize(new Dimension(iw + margin * 2, ih + margin * 2));
    }
}
