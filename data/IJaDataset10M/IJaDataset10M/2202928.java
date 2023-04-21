package jdvi;

import java.awt.*;
import java.util.*;
import java.io.*;

public class JDviText implements Serializable, JDviScribbable {

    int scale = 0;

    Color color = null;

    String text = "";

    Point position;

    public JDviText(int dpi, Color c, Point p, String s) {
        color = c;
        scale = dpi;
        text = s;
        position = p;
    }

    /**
     * set the text
     */
    public void setText(String s) {
        text = s;
    }

    /**
     * get the text
     */
    public String getText() {
        return text;
    }

    /**
     * set the position
     */
    public void setPosition(Point p) {
        position = p;
    }

    /**
     * get the position
     */
    public Point getPosition() {
        return position;
    }

    /**
     * tests wether the upper left of the text hits the given rectangle
     */
    public boolean hits(Rectangle r, int dpi) {
        double factor = (double) dpi / ((double) scale);
        return (r.contains((int) (position.x * factor), (int) (position.y * factor))) ? true : false;
    }

    /**
     * draw this text scaled to fit the current resolution
     */
    public void draw(Graphics g, int dpi) {
        double factor = (double) dpi / ((double) scale);
        int x = (int) (position.x * factor);
        int y = (int) (position.y * factor);
        int h = g.getFontMetrics().getHeight();
        g.setColor(color);
        String t;
        StringTokenizer st = new StringTokenizer(text, "\n");
        while (st.hasMoreTokens()) {
            t = st.nextToken();
            g.drawString(t, x, y);
            y += h;
        }
    }
}
