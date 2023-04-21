package jshomeorg.simplytrain.service.trackObjects.objects.graphics;

import java.awt.*;

/**
 *
 * @author js
 */
public class hvsignal_main extends hvsignal_base {

    public static final int ANIMSTEPS = 5;

    static Polygon border;

    static {
        border = new Polygon();
        border.addPoint(scale2, 0);
        border.addPoint(0, scale);
        border.addPoint(0, scale * 5);
        border.addPoint(scale2, scale * 6);
        border.addPoint(scale * 4, scale * 6);
        border.addPoint(scale * 4 + scale2, scale * 5);
        border.addPoint(scale * 4 + scale2, scale);
        border.addPoint(scale * 4, 0);
    }

    public static final int T_GREEN = 0;

    public static final int T_GREENYELLOW = 1;

    public static final int T_GREENYELLOWGREEN = 2;

    private boolean tored = true;

    private boolean slow = false;

    private boolean Cy1 = false;

    private boolean Cr1 = false;

    private boolean Cr2 = false;

    private boolean Cg1 = false;

    /** Creates a new instance of hvsignal_main */
    public hvsignal_main(int _type) {
        super(_type, scale * 5, scale * 6);
    }

    public void setRed(boolean b) {
        tored = b;
    }

    public void setSlow(boolean b) {
        slow = b;
    }

    public boolean getRed() {
        return tored;
    }

    public boolean getSlow() {
        return slow;
    }

    private void paintRed1(Graphics2D g, int a) {
        g.setColor(col_redanim[a]);
        g.fillOval(scale * 1 - scale2, scale * 4 - scale2, scale + scale2, scale + scale2);
    }

    private void paintRed2(Graphics2D g, int a) {
        g.setColor(col_redanim[a]);
        g.fillOval(scale * 1 - scale2, scale * 2 - scale2, scale + scale2, scale + scale2);
    }

    private void paintGreen1(Graphics2D g, int a) {
        g.setColor(col_gruenanim[a]);
        g.fillOval(scale * 3 - scale2, scale * 4 - scale2, scale + scale2, scale + scale2);
    }

    private void paintGreen2(Graphics2D g, int a) {
        g.setColor(col_gruenanim[a]);
        g.fillOval(scale * 3 - scale2, scale * 2 - scale2, scale + scale2, scale + scale2);
    }

    private void paintYellow(Graphics2D g, int a) {
        g.setColor(col_yellowanim[a]);
        g.fillOval(scale * 3 - scale2, scale * 4 - scale2, scale + scale2, scale + scale2);
    }

    public void paint(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.draw(border);
        g.fill(border);
        paintAnim(g, 0);
    }

    public void paintAnim(Graphics2D g2, int count) {
        if (g2 != null) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            boolean Ly1 = Cy1;
            boolean Lr1 = Cr1;
            boolean Lr2 = Cr2;
            boolean Lg1 = Cg1;
            if (tored) {
                Ly1 = false;
                Lg1 = false;
                Lr1 = true;
                Lr2 = false;
            } else {
                switch(type) {
                    case T_GREEN:
                        Lr1 = false;
                        Ly1 = false;
                        Lr2 = false;
                        Lg1 = true;
                        break;
                    case T_GREENYELLOW:
                        Lr1 = false;
                        Ly1 = true;
                        Lr2 = false;
                        Lg1 = true;
                        break;
                    case T_GREENYELLOWGREEN:
                        Lr1 = false;
                        Ly1 = slow;
                        Lr2 = false;
                        Lg1 = true;
                        break;
                }
            }
            paintRed1(g2, calcIndex(Cr1, Lr1, count));
            if (type == T_GREEN) paintGreen1(g2, calcIndex(Cg1, Lg1, count)); else {
                paintGreen2(g2, calcIndex(Cg1, Lg1, count));
                paintYellow(g2, calcIndex(Cy1, Ly1, count));
            }
            if (count == 0) {
                Cr1 = Lr1;
                Cr2 = Lr2;
                Cy1 = Ly1;
                Cg1 = Lg1;
            }
        }
    }

    public void setState(int s) {
        if (s == S_RED) tored = true; else if (s == S_GREEN) {
            tored = false;
            slow = false;
        } else if (s == S_YELLOW) {
            tored = false;
            slow = true;
        }
    }
}
