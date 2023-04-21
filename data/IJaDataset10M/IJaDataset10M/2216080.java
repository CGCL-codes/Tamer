package rollmadness.util;

import java.awt.geom.Rectangle2D;

public class JMERectangle {

    private final Rectangle2D rect;

    public JMERectangle(Rectangle2D rect) {
        this.rect = rect;
    }

    public JMERectangle(double x, double y, double w, double h) {
        this(new Rectangle2D.Double(x, y, w, h));
    }

    public com.jme3.font.Rectangle getFontRectangle() {
        return new com.jme3.font.Rectangle((float) rect.getX(), (float) rect.getY(), (float) rect.getWidth(), (float) rect.getHeight());
    }
}
