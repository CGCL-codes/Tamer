package com.fj.torkel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

public class CLabel extends Component {

    private static final long serialVersionUID = 3258416114332807730L;

    private String text;

    public CLabel(String text) {
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
        repaint();
    }

    public String getText() {
        return text;
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawString(text, 20, 0);
    }

    public Dimension getMinimumSize() {
        Image image = QuestApplet.paneltexture;
        if (image == null) {
            return new Dimension(16, 16);
        }
        return new Dimension(image.getWidth(null), image.getHeight(null));
    }

    public Dimension getPreferredSize() {
        return getMinimumSize();
    }
}
