package ac.hiu.j314.elmve.ui;

import ac.hiu.j314.elmve.*;
import java.io.*;
import java.awt.*;

public class EPoint2DUI extends Elm2DUIBase {

    public static final int CIRCLE = 0;

    public static final int SQUARE = 1;

    protected int type = CIRCLE;

    protected Dimension size;

    protected Color color;

    public EPoint2DUI() {
        size = new Dimension(10, 10);
        color = new Color(0.0f, 0.0f, 0.0f);
    }

    public void init(Serializable data) {
        Serializable s[] = (Serializable[]) data;
        size = (Dimension) s[0];
        type = W.p((Integer) s[1]);
        color = (Color) s[2];
    }

    public void update(Serializable data) {
        color = (Color) data;
        repaint();
    }

    public Dimension getPreferredSize() {
        return size;
    }

    public void paint(Graphics g) {
        g.setColor(color);
        if (type == CIRCLE) g.fillOval(0, 0, size.width, size.height); else if (type == SQUARE) g.fillRect(0, 0, size.width, size.height);
    }
}
