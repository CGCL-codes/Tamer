package org.formaria.awt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

/**
 * A graphical button that is drawn with rounded edges.
 * <p> Copyright (c) Formaria Ltd., 2008, This software is licensed under
 * the GNU Public License (GPL), please see license.txt for more details. If
 * you make commercial use of this software you must purchase a commercial
 * license from Formaria.</p>
 * $Revision: 1.3 $ not attributable
 */
public class GraphicButton extends Label {

    /**
   * Rounded edges, but no fill
   */
    public static final int LINE_STYLE = 0;

    /**
   * Rounded edges and filled
   */
    public static final int SOLID_STYLE = 1;

    /**
   * Rounded corner on the bottom right corner
   */
    public static final int BOTTOM_RIGHT_STYLE = 2;

    /**
   * Rounded corner on the bottom left corner
   */
    public static final int BOTTOM_LEFT_STYLE = 3;

    private int drawStyle;

    private boolean drawArrow = true;

    private boolean center = false;

    public GraphicButton() {
    }

    public GraphicButton(String title) {
        this(title, LINE_STYLE);
    }

    public GraphicButton(String title, int style) {
        super();
        drawStyle = style;
        setText(title);
    }

    /**
   * Set the button style
   * @param style "0"=LINE_STYLE, "1"=SOLID_STYLE, "2"=BOTTOM_RIGHT_STYLE, "3"=BOTTOM_LEFT_STYLE
   */
    public void setDrawStyle(String style) {
        drawStyle = Integer.parseInt(style);
    }

    /**
   * Set the button style
   * @param style LINE_STYLE, SOLID_STYLE, BOTTOM_RIGHT_STYLE, BOTTOM_LEFT_STYLE
   */
    public void setDrawStyle(int style) {
        drawStyle = style;
    }

    /**
   * Set the button caption
   * @param title the text to display
   */
    public void setTitle(String title) {
        setText(title);
    }

    public void paint(Graphics g) {
        switch(drawStyle) {
            case LINE_STYLE:
                drawLineStyle(g);
                break;
            case SOLID_STYLE:
                drawSolidStyle(g);
                drawRightArrow(g);
                break;
            case BOTTOM_RIGHT_STYLE:
                drawSolidStyle(g);
                completeBotRightStyle(g);
                drawRightArrow(g);
                break;
            case BOTTOM_LEFT_STYLE:
                drawSolidStyle(g);
                completeBotLeftStyle(g);
                drawLeftArrow(g);
                break;
        }
        drawText(g);
    }

    private void drawText(Graphics g) {
        Font f = new Font("Arial", Font.BOLD, 12);
        g.setFont(f);
        if (drawStyle == BOTTOM_LEFT_STYLE) {
            FontMetrics fm = g.getFontMetrics(f);
            Rectangle2D rect = fm.getStringBounds(getText(), g);
            g.drawString(getText(), 34, 14);
        } else if (center) {
            FontMetrics fm = g.getFontMetrics(f);
            Rectangle2D rect = fm.getStringBounds(getText(), g);
            int mid = getSize().width / 2;
            mid = mid - (rect.getBounds().width / 2);
            g.drawString(getText(), mid, 14);
        } else {
            g.drawString(getText(), 10, 14);
        }
    }

    private void drawLineStyle(Graphics g) {
        Dimension size = getSize();
        int w = size.width;
        g.setColor(Color.red);
        g.drawRoundRect(0, 0, w - 1, size.height - 1, 16, 16);
        drawRightArrow(g);
        g.setColor(Color.red);
    }

    private void drawSolidStyle(Graphics g) {
        Dimension size = getSize();
        int w = size.width;
        g.setColor(Color.red);
        g.fillRoundRect(0, 0, w, size.height - 1, 16, 16);
        g.setColor(Color.white);
    }

    private void completeBotRightStyle(Graphics g) {
        g.setColor(Color.red);
        int w = getSize().width;
        g.fillRect(0, 0, w, 10);
        g.setColor(Color.white);
        g.fillRect(w - 27, 0, 3, getSize().height);
    }

    private void completeBotLeftStyle(Graphics g) {
        g.setColor(Color.red);
        int w = getSize().width;
        g.fillRect(0, 0, w, 10);
        g.setColor(Color.white);
        g.fillRect(27, 0, 3, getSize().height);
    }

    private void drawRightArrow(Graphics g) {
        if (!drawArrow) return;
        int w = getSize().width;
        g.fillOval(w - 8, 9, 2, 2);
        g.fillOval(w - 11, 9, 2, 2);
        g.fillOval(w - 14, 9, 2, 2);
        g.fillOval(w - 17, 9, 2, 2);
        g.fillOval(w - 20, 9, 2, 2);
        g.fillOval(w - 12, 5, 2, 2);
        g.fillOval(w - 10, 7, 2, 2);
        g.fillOval(w - 10, 11, 2, 2);
        g.fillOval(w - 12, 13, 2, 2);
    }

    private void drawLeftArrow(Graphics g) {
        if (!drawArrow) return;
        int w = getSize().width;
        g.fillOval(8, 9, 2, 2);
        g.fillOval(11, 9, 2, 2);
        g.fillOval(14, 9, 2, 2);
        g.fillOval(17, 9, 2, 2);
        g.fillOval(20, 9, 2, 2);
        g.fillOval(12, 5, 2, 2);
        g.fillOval(10, 7, 2, 2);
        g.fillOval(10, 11, 2, 2);
        g.fillOval(12, 13, 2, 2);
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void doLayout() {
        super.doLayout();
        repaint();
    }

    /**
   * Center the text
   * @param c true to center the text
   */
    public void setCentered(boolean c) {
        center = c;
    }

    /**
   * Turn on drawing of the arrow
   * @param draw true to draw the arrow
   */
    public void setDrawArrow(boolean draw) {
        drawArrow = draw;
    }
}
