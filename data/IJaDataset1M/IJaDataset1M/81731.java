package cxcore.geometry;

import java.awt.geom.*;

/**
 *
 * @author sean
 */
public class JcvArc extends Arc2D.Double {

    public JcvArc() {
        super();
    }

    public JcvArc(double x, double y, double w, double h, double start, double extent, int type) {
        super(x, y, w, h, start, extent, type);
    }

    public JcvArc(float x, float y, float w, float h, double start, double extent, int type) {
        super(x, y, w, h, start, extent, type);
    }

    public JcvArc(int x, int y, int w, int h, double start, double extent, int type) {
        super(x, y, w, h, start, extent, type);
    }

    public JcvArc(int type) {
        super(type);
    }

    public JcvArc(Rectangle2D ellipseBounds, double start, double extent, int type) {
        super(ellipseBounds, start, extent, type);
    }

    /**
     *
     * @return
     */
    public int getIntX() {
        return (int) x;
    }

    /**
     *
     * @return
     */
    public int getIntY() {
        return (int) y;
    }

    /**
     *
     */
    public float getFloatX() {
        return (float) x;
    }

    /**
     *
     * @return
     */
    public float getFloatY() {
        return (float) y;
    }

    /**
     *
     * @return
     */
    public int getIntWidth() {
        return (int) width;
    }

    /**
     *
     * @return
     */
    public int getIntHeight() {
        return (int) height;
    }

    /**
     *
     * @return
     */
    public float getFloatWidth() {
        return (float) width;
    }

    /**
     *
     * @return
     */
    public float getFloatHeight() {
        return (float) height;
    }
}
