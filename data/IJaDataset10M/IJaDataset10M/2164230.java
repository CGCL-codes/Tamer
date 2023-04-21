package org.proteomecommons.MSExpedite.Graph;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Color;

public class MultiRowToolTip implements IDrawable {

    String text[];

    int hFontgap = 2;

    Rectangle bounds = new Rectangle(0, 0, 10, 10);

    boolean isVisible = true;

    boolean withinBounds = true;

    public MultiRowToolTip() {
    }

    public void setVisible(boolean b) {
        isVisible = b;
    }

    public void setText(String[] s) {
        text = s;
    }

    public void setText(String s) {
        text = new String[] { s };
    }

    public void setBounds(Rectangle r) {
        bounds = r;
    }

    public void setBounds(int x, int y, int width, int height) {
        bounds = new Rectangle(x, y, width, height);
    }

    public boolean isVisible() {
        return isVisible && withinBounds;
    }

    public Dimension getPreferredSize() {
        return new Dimension(bounds.width, bounds.height);
    }

    public void draw(Graphics g) {
        if (!isVisible()) return;
        Color savedColor = g.getColor();
        g.setColor(Color.black);
        int xOffset = bounds.x;
        int yOffset = bounds.y;
        int maxTextHeight = 0;
        int maxTextLength = 0;
        if (text == null) return;
        for (int i = 0; i < text.length; i++) {
            if (text[i] == null) continue;
            g.drawString(text[i], xOffset, yOffset + maxTextHeight + hFontgap);
            maxTextHeight += g.getFontMetrics().getHeight();
            maxTextLength = (g.getFontMetrics().stringWidth(text[i]) > maxTextLength) ? g.getFontMetrics().stringWidth(text[i]) : maxTextLength;
        }
        g.setColor(savedColor);
    }
}
