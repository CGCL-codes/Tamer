package com.gempukku.gempballs.view;

import com.gempukku.animator.Animated;
import com.gempukku.animator.DisplayInfo;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

public class SparkleShape implements Animated {

    private Shape _shape;

    private Color _color;

    private long _interval = 10000;

    private long _length = 750;

    public SparkleShape(Shape shape, Color color) {
        _shape = shape;
        _color = color;
    }

    public void paintAnimated(Graphics2D gr, DisplayInfo displayInfo, long time, AnimatedCallback callback) {
        Rectangle bounds = _shape.getBounds();
        int maxSize = bounds.width + bounds.height;
        long curentTime = time % _interval;
        if (curentTime < _length) {
            gr.setClip(_shape);
            gr.setColor(_color);
            gr.setStroke(new BasicStroke(2));
            gr.drawLine((int) (bounds.x + (curentTime / (1d * _length)) * maxSize), bounds.y, bounds.x, (int) (bounds.y + (curentTime / (1d * _length)) * maxSize));
        }
    }

    public Rectangle getBounds(DisplayInfo displayInfo, long time) {
        return _shape.getBounds();
    }

    public Object getState(DisplayInfo displayInfo, long time) {
        return null;
    }
}
