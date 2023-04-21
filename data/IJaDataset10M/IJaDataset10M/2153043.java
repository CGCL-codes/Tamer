package com.tensegrity.palobrowser.shapes;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

/**
 * <code>SequenceLayouter</code>
 * 
 * @author Stepan Rutz
 * @version $ID$
 */
public class SequenceLayouter implements Layouter {

    public static final String PROPERTYKEY_IGNORE_LAYOUT = "com.tensegrity.palobrowser.shapes.SequenceLayouter.IgnoreLayout";

    private boolean horizontal;

    public SequenceLayouter(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public Point measure(ContainerShape container, GC gc) {
        return new Point(1, 1);
    }

    public void layout(ContainerShape container) {
        Shape shapes[] = container.getShapes();
        int gap = horizontal ? 20 : 20;
        int x = 10;
        int y = 10;
        for (int i = 0, n = shapes.length; i < n; ++i) {
            Shape shape = shapes[i];
            if (shape.getProperty(PROPERTYKEY_IGNORE_LAYOUT) != null) continue; else if (!shape.isVisible()) continue;
            if (horizontal) {
                shape.setPosition(x, y);
                x += shape.getWidth() + gap;
            } else {
                shape.setPosition(x, y);
                y += shape.getHeight() + gap;
            }
        }
    }
}
