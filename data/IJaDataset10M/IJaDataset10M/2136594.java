package elliott803.view.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Scrollable;

public class DisplayPlot extends JPanel implements Scrollable, ComponentListener {

    private static final long serialVersionUID = 1L;

    int minY, maxY;

    AffineTransform transform;

    List<Segment> segments;

    Point p1, p2;

    Rectangle r1;

    public DisplayPlot() {
        setBackground(Color.WHITE);
        addComponentListener(this);
        segments = Collections.synchronizedList(new ArrayList<Segment>());
        plotClear();
    }

    public void plotDraw(int x, int y) {
        plotDraw(x, y, 0);
    }

    public void plotMove(int x, int y) {
        plotMove(x, y, 0);
    }

    public void plotDraw(int x, int y, int dir) {
        Segment lastSeg = segments.get(segments.size() - 1);
        if (lastSeg.draw && lastSeg.dir != 0 && lastSeg.dir == dir) {
            lastSeg.x = x;
            lastSeg.y = y;
            mapToPoint(lastSeg, false);
        } else {
            segments.add(mapToPoint(new Segment(true, x, y, dir), true));
        }
        if (y < minY || y > maxY) {
            minY = Math.min(y - 10, minY);
            maxY = Math.max(y + 10, maxY);
            setTransform();
            revalidate();
            repaint();
        } else {
            repaint(r1);
        }
        scrollRectToVisible(r1);
    }

    public void plotMove(int x, int y, int dir) {
        Segment lastSeg = segments.get(segments.size() - 1);
        if (!lastSeg.draw) {
            lastSeg.x = x;
            lastSeg.y = y;
            mapToPoint(lastSeg, false);
        } else {
            segments.add(mapToPoint(new Segment(false, x, y, dir), true));
        }
    }

    public void plotClear() {
        segments.clear();
        segments.add(new Segment(false, 0, 0, 0));
        p1 = new Point();
        p2 = new Point();
        r1 = new Rectangle();
        minY = 9999;
        maxY = -9999;
        setTransform();
        revalidate();
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        p2.setLocation(0, 0);
        mapToPoint(segments.get(0), true);
        synchronized (segments) {
            for (int i = 1; i < segments.size(); i++) {
                if (mapToPoint(segments.get(i), true).draw) {
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }
    }

    private void setTransform() {
        double scale = getWidth() / 1100.0;
        transform = new AffineTransform();
        transform.translate(0, (getHeight() + (maxY + minY) * scale) / 2);
        transform.scale(scale, -scale);
    }

    private Segment mapToPoint(Segment seg, boolean add) {
        if (add) {
            Point p = p2;
            p2 = p1;
            p1 = p;
        }
        p2.x = seg.x;
        p2.y = seg.y;
        transform.transform(p2, p2);
        r1.setBounds(p1.x, p1.y, 0, 0);
        r1.add(p2);
        r1.grow(1, 1);
        return seg;
    }

    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), (int) ((maxY - minY) * transform.getScaleX()));
    }

    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return visibleRect.height / 10;
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return visibleRect.height;
    }

    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    public boolean getScrollableTracksViewportHeight() {
        return (getParent().getHeight() > getPreferredSize().height);
    }

    public void componentResized(ComponentEvent e) {
        setTransform();
        repaint();
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }

    private static class Segment {

        boolean draw;

        int x, y, dir;

        Segment(boolean draw, int x, int y, int dir) {
            this.draw = draw;
            this.x = x;
            this.y = y;
            this.dir = dir;
        }

        public String toString() {
            return draw + ": [" + x + "," + y + "] " + dir;
        }
    }
}
