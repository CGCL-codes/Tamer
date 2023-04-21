package tjacobs.ui.drag;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import tjacobs.ui.util.PaintUtils;
import tjacobs.ui.util.WindowClosingActions;

/**
 * See the public static method addAMouseDragOutliner
 */
public class MouseDragOutliner extends MouseAdapter implements MouseMotionListener {

    public static final BasicStroke DASH_STROKE = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 10.0f, new float[] { 8, 8 }, 0);

    public static final int RECTANGLE = 0;

    public static final int OVAL = 1;

    public static final int LINE = 2;

    private boolean mUseMove = false;

    private Point mStart;

    private Point mEnd;

    private int mShape = RECTANGLE;

    private Shape mCustomShape;

    private Component mComponent;

    private MyRunnable mRunner = new MyRunnable();

    private List<OutlineListener> mListeners = new ArrayList<OutlineListener>(1);

    public MouseDragOutliner() {
        super();
    }

    public MouseDragOutliner(boolean useMove) {
        this();
        mUseMove = useMove;
    }

    public void setShape(int s) {
        mShape = s;
    }

    public int getShape() {
        return mShape;
    }

    public void setCustomShape(Shape s) {
        mCustomShape = s;
    }

    public Shape getCustomShape() {
        return mCustomShape;
    }

    public void mouseDragged(MouseEvent me) {
        doMouseDragged(me);
    }

    public void mousePressed(MouseEvent me) {
        mStart = me.getPoint();
    }

    public void mouseEntered(MouseEvent me) {
        mStart = me.getPoint();
    }

    public void mouseReleased(MouseEvent me) {
        Iterator<OutlineListener> i = mListeners.iterator();
        Point end = me.getPoint();
        while (i.hasNext()) {
            i.next().mouseDragEnded(mStart, end);
        }
    }

    public void mouseMoved(MouseEvent me) {
        if (mUseMove) {
            doMouseDragged(me);
        }
    }

    public void addOutlineListener(OutlineListener ol) {
        mListeners.add(ol);
    }

    public void removeOutlineListener(OutlineListener ol) {
        mListeners.remove(ol);
    }

    private class MyRunnable implements Runnable {

        public void run() {
            Graphics g = mComponent.getGraphics();
            if (g == null) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g;
            Stroke s = g2.getStroke();
            g2.setStroke(DASH_STROKE);
            int x = Math.min(mStart.x, mEnd.x);
            int y = Math.min(mStart.y, mEnd.y);
            int w = Math.abs(mEnd.x - mStart.x);
            int h = Math.abs(mEnd.y - mStart.y);
            if (w == 0 || h == 0) return;
            g2.setXORMode(Color.WHITE);
            if (mCustomShape != null) {
                Rectangle r = mCustomShape.getBounds();
                AffineTransform scale = AffineTransform.getScaleInstance(w / (double) r.width, h / (double) r.height);
                AffineTransform trans = AffineTransform.getTranslateInstance(x - r.x, y - r.y);
                g2.transform(trans);
                g2.transform(scale);
                g2.draw(mCustomShape);
            } else {
                if (mShape == RECTANGLE) g2.drawRect(x, y, w, h); else if (mShape == OVAL) g2.drawOval(x, y, w, h); else if (mShape == LINE) g2.drawLine(mStart.x, mStart.y, mEnd.x, mEnd.y);
            }
            g2.setStroke(s);
        }
    }

    public void doMouseDragged(MouseEvent me) {
        mEnd = me.getPoint();
        if (mStart != null) {
            mComponent = me.getComponent();
            mComponent.repaint();
            SwingUtilities.invokeLater(mRunner);
        }
    }

    public static MouseDragOutliner addAMouseDragOutliner(Component c) {
        MouseDragOutliner mdo = new MouseDragOutliner();
        c.addMouseListener(mdo);
        c.addMouseMotionListener(mdo);
        return mdo;
    }

    public static interface OutlineListener {

        public void mouseDragEnded(Point start, Point finish);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("MouseDragOutliner Test");
        Container c = f.getContentPane();
        JPanel p = new JPanel();
        c.add(p);
        MouseDragOutliner outliner = addAMouseDragOutliner(p);
        outliner.setShape(LINE);
        Shape a = PaintUtils.createStandardStar(100, 100, 5, .3, 0);
        outliner.setCustomShape(a);
        f.setBounds(200, 200, 400, 400);
        f.addWindowListener(new WindowClosingActions.Exit());
        f.setVisible(true);
    }
}
