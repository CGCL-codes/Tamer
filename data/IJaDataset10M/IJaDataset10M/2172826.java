package org.gvsig.fmap.drivers.gpe.model;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import org.gvsig.gpe.parser.ICoordinateIterator;

/**
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class GPEBBox {

    private String id = null;

    private double[] x;

    private double[] y;

    private double[] z;

    private String srs;

    public GPEBBox() {
    }

    public GPEBBox(String id, ICoordinateIterator coords, String srs) {
        super();
        this.id = id;
        double[] buffer = new double[coords.getDimension()];
        this.x = new double[2];
        this.y = new double[2];
        this.z = new double[2];
        try {
            if (coords.hasNext()) {
                coords.next(buffer);
                x[0] = buffer[0];
                y[0] = buffer[1];
                if (buffer.length > 2) {
                    z[0] = buffer[2];
                }
                if (coords.hasNext()) {
                    coords.next(buffer);
                    x[1] = buffer[0];
                    y[1] = buffer[1];
                    if (buffer.length > 2) {
                        z[1] = buffer[2];
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.srs = srs;
    }

    /**
	 * @return a rectangle in 2D
	 */
    public Rectangle2D getBbox2D() {
        return new Rectangle2D.Double(x[0], y[0], x[1], y[1]);
    }

    /**
	 * @return the id
	 */
    public String getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * @return the srs
	 */
    public String getSrs() {
        return srs;
    }

    /**
	 * @param srs the srs to set
	 */
    public void setSrs(String srs) {
        this.srs = srs;
    }

    /**
	 * @return the x
	 */
    public double[] getX() {
        return x;
    }

    /**
	 * @param x the x to set
	 */
    public void setX(double[] x) {
        this.x = x;
    }

    /**
	 * @return the y
	 */
    public double[] getY() {
        return y;
    }

    /**
	 * @param y the y to set
	 */
    public void setY(double[] y) {
        this.y = y;
    }

    /**
	 * @return the z
	 */
    public double[] getZ() {
        return z;
    }

    /**
	 * @param z the z to set
	 */
    public void setZ(double[] z) {
        this.z = z;
    }
}
