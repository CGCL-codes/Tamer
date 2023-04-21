package net.nexttext;

import java.awt.Rectangle;
import java.awt.Shape;
import processing.core.PVector;

public class RandomLocation implements Locatable {

    Shape shape;

    public RandomLocation(Shape shape) {
        this.shape = shape;
    }

    public PVector getLocation() {
        float x, y;
        do {
            Rectangle bounds = shape.getBounds();
            x = (float) (bounds.x + Math.random() * bounds.width);
            y = (float) (bounds.y + Math.random() * bounds.height);
        } while (!shape.contains(x, y));
        return new PVector(x, y);
    }
}
