package org.carabiner.example;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SquareMouseController extends MouseAdapter {

    private Rectangle rect;

    private ShapeModel model;

    public SquareMouseController(ShapeModel shapeModel) {
        model = shapeModel;
    }

    public void mousePressed(MouseEvent e) {
        rect = new Rectangle(e.getPoint());
    }

    public void mouseReleased(MouseEvent e) {
        rect.setSize(e.getX() - rect.x, e.getY() - rect.y);
        model.addShape(rect, Color.RED);
        Component c = (Component) e.getSource();
        c.removeMouseListener(this);
    }

    Rectangle getRect() {
        return rect;
    }
}
