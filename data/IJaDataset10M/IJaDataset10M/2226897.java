package closestPoints;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class PaintPanel extends JPanel {

    public PaintPanel() {
        this.setSize(1000, 700);
        points = new ArrayList<PaintPoint>(100);
        listener = new MyListeners();
        this.addMouseListener(listener);
        locked = false;
        showCoordinate = true;
    }

    public boolean isShowCoordinate() {
        return showCoordinate;
    }

    public void setShowCoordinate(boolean showCoordinate) {
        this.showCoordinate = showCoordinate;
        this.repaint();
    }

    public void clear() {
        this.points.clear();
        this.repaint();
        this.locked = false;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void paint(Graphics g) {
        g.setPaintMode();
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        this.paintPoints((Graphics2D) g);
    }

    public void showResult(int x1, int y1, int x2, int y2) {
        PaintPoint p = null;
        Iterator<PaintPoint> itor = points.iterator();
        while (itor.hasNext()) {
            p = itor.next();
            if ((p.x == x1 && p.y == y1) || (p.x == x2 && p.y == y2)) {
                p.color = Color.RED;
                p.radius = 10;
            }
        }
        this.repaint();
    }

    public ArrayList<PaintPoint> getPoints() {
        return points;
    }

    private void paintPoints(Graphics2D g2d) {
        PaintPoint p = null;
        Iterator<PaintPoint> itor = points.iterator();
        while (itor.hasNext()) {
            p = itor.next();
            this.drawPoint(p, g2d);
        }
    }

    private void drawPoint(PaintPoint p, Graphics2D g2d) {
        int w, h;
        w = h = p.radius;
        g2d.setColor(p.color);
        g2d.fillOval(p.x - w / 2, p.y - h / 2, w, h);
        if (this.showCoordinate) {
            g2d.setColor(Color.BLACK);
            g2d.drawString(p.toString(), p.x + w, p.y + h);
        }
    }

    private ArrayList<PaintPoint> points = null;

    private MouseListener listener = null;

    private boolean locked;

    private boolean showCoordinate;

    /**
	 * 监听鼠标点击事件。
	 * @author hcy
	 *
	 */
    private class MyListeners implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (locked) {
                return;
            }
            PaintPoint p = new PaintPoint();
            p.x = e.getX();
            p.y = e.getY();
            points.add(p);
            repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}
