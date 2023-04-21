package net.infonode.gui.componentpainter;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import net.infonode.util.*;

/**
 * An abstract base class for {@link ComponentPainter}'s. Default implementations for both paint methods are provided,
 * but becuase they call each other a sub class must override one or both methods.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.7 $
 * @since IDW 1.2.0
 */
public abstract class AbstractComponentPainter implements ComponentPainter, Serializable {

    private static final long serialVersionUID = 1;

    protected AbstractComponentPainter() {
    }

    private void paint(Component component, Graphics g, int x, int y, int width, int height) {
        paint(component, g, x, y, width, height, Direction.RIGHT, false, false);
    }

    public void paint(Component component, Graphics g, int x, int y, int width, int height, Direction direction, boolean horizontalFlip, boolean verticalFlip) {
        if (direction != Direction.RIGHT || horizontalFlip || verticalFlip) {
            Graphics2D g2 = (Graphics2D) g;
            AffineTransform t = g2.getTransform();
            try {
                int w = direction.isHorizontal() ? width : height;
                int h = direction.isHorizontal() ? height : width;
                AffineTransform nt = ImageUtils.createTransform(direction, horizontalFlip, verticalFlip, w, h);
                g2.translate(x, y);
                g2.transform(nt);
                paint(component, g, 0, 0, w, h);
            } finally {
                g2.setTransform(t);
            }
        } else {
            paint(component, g, x, y, width, height);
        }
    }

    public boolean isOpaque(Component component) {
        return true;
    }
}
