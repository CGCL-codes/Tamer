package uk.ac.rdg.resc.edal.geometry.impl;

import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * An immutable one-dimensional envelope
 * @author Jon
 */
public final class OneDEnvelope extends AbstractEnvelope {

    private final double min;

    private final double max;

    public OneDEnvelope(double min, double max, CoordinateReferenceSystem crs) {
        super(crs);
        if (crs != null && crs.getCoordinateSystem().getDimension() != 1) {
            throw new IllegalArgumentException("CRS must be one-dimensional");
        }
        this.min = min;
        this.max = max;
    }

    /** Creates a one-dimensional envelope with a null coordinate reference system */
    public OneDEnvelope(double min, double max) {
        this(min, max, null);
    }

    /** returns 1 */
    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public final double getMinimum(int i) {
        if (i == 0) return this.min;
        throw new IndexOutOfBoundsException();
    }

    @Override
    public final double getMaximum(int i) {
        if (i == 0) return this.max;
        throw new IndexOutOfBoundsException();
    }

    @Override
    public DirectPosition getLowerCorner() {
        return new DirectPositionImpl(this.getCoordinateReferenceSystem(), this.min);
    }

    @Override
    public DirectPosition getUpperCorner() {
        return new DirectPositionImpl(this.getCoordinateReferenceSystem(), this.max);
    }
}
