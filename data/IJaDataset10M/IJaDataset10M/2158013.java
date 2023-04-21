package au.gov.qld.dnr.dss.v1.ranking;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import javax.swing.*;

class ImageComponent extends JPanel {

    Image image;

    ImageComponent(Image image) {
        this.image = image;
    }

    public Dimension getPreferredSize() {
        int height = image.getHeight(null);
        int width = image.getWidth(null);
        Dimension size = new Dimension(width, height);
        return size;
    }

    public void paintComponent(Graphics g) {
        Dimension size = getSize();
        g.drawImage(image, 0, 0, null);
    }
}
