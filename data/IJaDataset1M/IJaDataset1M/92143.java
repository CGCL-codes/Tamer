package toolkit.levelEditor.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import toolkit.shared.util.Util;

public class PPolygon extends PSelectablePath {

    private final Paint defaultPaint = new Color(1, 1, 1, .4f);

    private final Paint hoverPaint = new Color(1, 1, .5f, .6f);

    private final Paint selectedPaint = new Color(1, 1, .5f, 1f);

    private boolean closed;

    private final List<Point2D> points = new ArrayList<Point2D>();

    private final List<PPointHandle> pointHandles = new ArrayList<PPointHandle>();

    public PPolygon() {
        setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        setStrokePaint(defaultPaint);
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(final boolean closed) {
        if (this.closed != closed) {
            this.closed = closed;
            if (closed) {
                super.closePath();
            } else {
                rebuildPath();
            }
        }
    }

    public List<Point2D> getPoints() {
        return points;
    }

    public PPointHandle pointMoveTo(final float x, final float y) {
        moveTo(x, y);
        return addPoint(x, y);
    }

    public PPointHandle pointLineTo(final float x, final float y) {
        lineTo(x, y);
        return addPoint(x, y);
    }

    private PPointHandle addPoint(final float x, final float y, final int index) {
        final Point2D point = new Point2D.Float(x, y);
        points.add(index, point);
        final PPointHandle p = new PPointHandle(index, this);
        p.addPropertyChangeListener("transform", new PropertyChangeListener() {

            public void propertyChange(final PropertyChangeEvent evt) {
                points.get(p.getIndex()).setLocation(p.getTransform().getTranslateX(), p.getTransform().getTranslateY());
                rebuildPath();
            }
        });
        p.translate(x, y);
        pointHandles.add(index, p);
        addChild(p);
        reIndexPoints();
        rebuildPath();
        return p;
    }

    public void addPoint(final PPointHandle handle) {
        addPoint((float) handle.getTransform().getTranslateX(), (float) handle.getTransform().getTranslateY(), handle.getIndex());
    }

    public PPointHandle addPoint(final float x, final float y) {
        return addPoint(x, y, points.size());
    }

    public PPointHandle addPointNear(final Point2D point) {
        double dist = Double.MAX_VALUE;
        Point2D p1 = null;
        Point2D p2 = null;
        int index = -1;
        for (int i = 0; i <= points.size(); i++) {
            if (i < points.size()) {
                p1 = points.get(i);
            } else {
                p1 = points.get(0);
            }
            if (i > 0) {
                final double d = Util.distanceToLine(p1, p2, point);
                if (d < dist) {
                    dist = d;
                    index = i;
                }
            }
            p2 = p1;
        }
        return addPoint((float) point.getX(), (float) point.getY(), index);
    }

    public void removePoint(final PPointHandle point) {
        removeChild(point);
        points.remove(point.getIndex());
        pointHandles.remove(point.getIndex());
        reIndexPoints();
        if (points.size() > 1) {
            rebuildPath();
        }
        if (points.size() == 2) {
            setClosed(false);
        }
    }

    private void reIndexPoints() {
        for (int i = 0; i < points.size(); i++) {
            pointHandles.get(i).setIndex(i);
        }
    }

    private void rebuildPath() {
        setPath(new GeneralPath());
        Point2D p = points.get(0);
        super.moveTo((float) p.getX(), (float) p.getY());
        if (points.size() > 1) {
            for (int i = 1; i < points.size(); i++) {
                p = points.get(i);
                super.lineTo((float) p.getX(), (float) p.getY());
            }
        }
        if (closed) {
            super.closePath();
        }
    }

    @Override
    public void stateChanged(final PNodeState state) {
        switch(state) {
            case HIGHLIGHT:
                setStrokePaint(hoverPaint);
                break;
            case DESELECTED:
                setStrokePaint(defaultPaint);
                break;
            case SELECTED:
                setStrokePaint(selectedPaint);
                break;
            default:
                break;
        }
    }

    public void translate(final float dx, final float dy) {
    }

    @Override
    public String toString() {
        return "PPolygon (" + getPoints().size() + " nodes)";
    }
}
