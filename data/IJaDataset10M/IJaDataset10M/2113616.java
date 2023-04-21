package ru.ifmo.fcdesigner.flowchart.view;

import ru.ifmo.fcdesigner.flowchart.view.visitors.SimpleViewVisitor;
import ru.ifmo.fcdesigner.flowchart.view.visitors.ViewVisitor;
import java.awt.*;
import java.util.List;

/**
 * FlowchartElementView connector.
 *
 * @author Dmitry Paraschenko
 */
public class ConnectorElement extends AbstractElement<ru.ifmo.fcdesigner.flowchart.model.AbstractElement, PointElementView> {

    /** Source element. */
    private AbstractBoundedElementView source;

    /** Destination element or connector. */
    private AbstractBoundedElementView target;

    public ConnectorElement(AbstractBoundedElementView src, AbstractBoundedElementView dest, List<Point> points, FlowchartElementView flowchart) {
        super(flowchart);
        if (flowchart != null) {
            flowchart.addConnector(this);
            this.addObserver(flowchart);
        }
        source = src;
        target = dest;
        System.out.println(source + " --> " + target);
        for (Point p : points) {
            System.out.println(p);
            this.points.add(new PointElementView(p, null, this));
        }
    }

    /**
     * Returns source.
     *
     * @return source.
     */
    public AbstractBoundedElementView getSource() {
        return source;
    }

    /**
     * Sets source.
     * 
     * @param source new value.
     */
    public void setSource(AbstractBoundedElementView source) {
        this.source = source;
    }

    /**
     * Returns target.
     *
     * @return target.
     */
    public AbstractBoundedElementView getTarget() {
        return target;
    }

    /**
     * Sets target.
     *
     * @param target new value.
     */
    public void setTarget(AbstractBoundedElementView target) {
        this.target = target;
    }

    public Point getLocation() {
        return new Point(0, 0);
    }

    public void setLocation(Point location) {
    }

    public void translate(int dx, int dy) {
        for (PointElementView point : points) {
            point.translate(dx, dy);
        }
    }

    /**
     * Returns point with index i.
     *
     * @param i point index.
     * @return point with index i.
     */
    public Point getPoint(int i) {
        return getPoints().get(i).getLocation();
    }

    public void setPoints(List<Point> points) {
        this.points.clear();
        for (Point point : points) {
            this.points.add(new PointElementView(point, getFlowchart(), this));
        }
    }

    public <R> R accept(SimpleViewVisitor<R> visitor) {
        return visitor.visitConnectorElement(this);
    }

    public <R, A> R accept(ViewVisitor<R, A> visitor, A arg) {
        return visitor.visitConnectorElement(this, arg);
    }

    public boolean isSelected() {
        return false;
    }

    public void setSelected(boolean selected) {
    }

    public static double getDistance(Point p1, Point p2) {
        return Math.hypot(p1.getX() - p2.getX(), p1.getY() - p2.getY());
    }

    public void remove() {
        getFlowchart().removeConnector(this);
        updated();
    }
}
