package scratchcomp;

import java.awt.*;

public class Ruler extends UComponent {

    public static final int HORIZONTAL = 0;

    public static final int VERTICAL = 1;

    public static final int MM = 10;

    public static final int CM = 100;

    private int mode;

    private int pos, spos, zoom = 1;

    private int lastsize;

    private FontMetrics fm;

    public Ruler(int m) {
        mode = m;
    }

    public void updatePosition(int i) {
        pos = i;
        repaint();
    }

    public void setStartPos(int i) {
        spos = i;
        repaint();
    }

    public void setZoom(int i) {
        zoom = i;
        repaint();
    }

    public void zoomIn() {
        zoom++;
        repaint();
    }

    public void zoomOut() {
        if (zoom > 1) {
            zoom--;
            repaint();
        }
    }

    public void paint(Graphics g) {
        fm = g.getFontMetrics();
        String num;
        switch(mode) {
            case HORIZONTAL:
                for (int i = spos, j = 0; i < this.getWidth() * (2 * zoom); i++) {
                    if (i % CM == 0) {
                        num = "" + (j++);
                        g.drawLine((i / 2) * zoom, this.getHeight() / 4, (i / 2) * zoom, this.getHeight());
                        if (i > 0) g.drawString(num, (i / 2) * zoom - fm.stringWidth(num), this.getHeight() / 4 + fm.getHeight() / 2);
                    } else if (i % MM == 0) {
                        g.drawLine((i / 2) * zoom, this.getHeight() / 2, (i / 2) * zoom, this.getHeight());
                    }
                    if (i == pos) {
                        g.setColor(Color.blue);
                        g.drawLine(i, 0, i, this.getHeight());
                        g.setColor(Color.black);
                    }
                }
                break;
            case VERTICAL:
                for (int i = 0, j = 0; i < this.getHeight() * (2 * zoom); i++) {
                    if (i % CM == 0) {
                        num = "" + (j++);
                        g.drawLine(this.getWidth() / 4, (i / 2) * zoom, this.getWidth(), (i / 2) * zoom);
                        if (i > 0) g.drawString(num, this.getWidth() / 4 - fm.stringWidth(num) / 2, (i / 2) * zoom - fm.getHeight() / 4);
                    } else if (i % MM == 0) {
                        g.drawLine(this.getWidth() / 2, (i / 2) * zoom, this.getWidth(), (i / 2) * zoom);
                    }
                    if (i == pos) {
                        g.setColor(Color.blue);
                        g.drawLine(0, i, this.getWidth(), i);
                        g.setColor(Color.black);
                    }
                }
                break;
        }
    }
}
