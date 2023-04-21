package es.iiia.shapeeditor.controllers;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import es.iiia.shapeeditor.ViewUtils;
import es.iiia.shapegrammar.model.GeometryModel;

public abstract class ControllerBase {

    private static final String PROPERTY_LAYOUT = "Layout";

    public static int controlPointSize = 3;

    private PropertyChangeSupport listeners;

    private ArrayList<ControllerBase> children;

    protected Point[] controlPoints;

    protected Point[] snapPoints;

    protected boolean fillShape = false;

    public abstract GeometryModel getModel();

    public abstract void setModel(GeometryModel model);

    public abstract void append(GeneralPath path);

    public abstract Shape getShape();

    public abstract Point2D[] getEditPoints();

    public abstract Shape transformShape(AffineTransform transform);

    public abstract double getDistance(Point point);

    protected ControllerBase() {
        this.listeners = new PropertyChangeSupport(this);
        this.children = null;
    }

    public boolean isFillShape() {
        return this.fillShape;
    }

    public void addChild(ControllerBase child) {
        if (this.children == null) {
            this.children = new ArrayList<ControllerBase>();
        }
        this.children.add(child);
    }

    public boolean hasChildren() {
        return this.children != null && this.children.size() > 0;
    }

    public ArrayList<ControllerBase> getChildren() {
        return this.children;
    }

    public Point[] getSnapPoints() {
        if (snapPoints == null) {
            snapPoints = new Point[1];
            snapPoints[0] = new Point(this.getShape().getBounds().x, this.getShape().getBounds().y);
        }
        return snapPoints;
    }

    public Point2D[] getControlPoints() {
        if (controlPoints == null) {
            controlPoints = create8ControlPointsFromBounds();
        }
        return controlPoints;
    }

    public int getPosition(Point pt) {
        return getPosition(getControlPoints(), pt);
    }

    ;

    protected int getPosition(Point2D[] controlPoints, Point2D position) {
        int i = 0;
        for (Point2D pt : controlPoints) {
            if (Math.abs(position.getX() - pt.getX()) <= controlPointSize && Math.abs(position.getY() - pt.getY()) <= controlPointSize) {
                return i;
            }
            i++;
        }
        return -1;
    }

    protected Point[] create8ControlPointsFromBounds() {
        return ViewUtils.create8ControlPointsFromBounds(getShape().getBounds());
    }

    protected void reset() {
        controlPoints = null;
        snapPoints = null;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(listener);
    }

    protected PropertyChangeSupport getListeners() {
        return listeners;
    }

    protected void fireChangeListeners(Object oldValue, Object newValue) {
        fireChangeListeners(null, oldValue, newValue);
    }

    protected void fireChangeListeners(PropertyChangeEvent evt, Object oldValue, Object newValue) {
        this.getListeners().firePropertyChange(evt == null ? PROPERTY_LAYOUT : evt.getPropertyName(), oldValue, newValue);
        reset();
    }
}
