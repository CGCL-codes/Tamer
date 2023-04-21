package org.carabiner.example;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultShapeModel implements ShapeModel {

    private PropertyChangeSupport pcs;

    private List shapes;

    public DefaultShapeModel() {
        shapes = new ArrayList();
        pcs = new PropertyChangeSupport(this);
    }

    public void render(Graphics2D g) {
        for (Iterator i = shapes.iterator(); i.hasNext(); ) {
            ColoredShape cShape = (ColoredShape) i.next();
            g.setColor(cShape.color);
            g.draw(cShape.shape);
        }
    }

    public void addShape(Shape shape, Color color) {
        shapes.add(new ColoredShape(shape, color));
        pcs.firePropertyChange("shapes", null, shape);
    }

    private class ColoredShape {

        public ColoredShape(Shape newShape, Color newColor) {
            shape = newShape;
            color = newColor;
        }

        private Color color;

        private Shape shape;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public int getShapeCount() {
        return shapes.size();
    }
}
