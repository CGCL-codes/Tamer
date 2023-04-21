package com.vividsolutions.jts.geom.impl;

import java.io.Serializable;
import com.vividsolutions.jts.geom.*;

/**
 * Creates {@link CoordinateSequence}s represented as an array of {@link Coordinate}s.
 *
 * @version 1.7
 */
public final class CoordinateArraySequenceFactory implements CoordinateSequenceFactory, Serializable {

    private static final long serialVersionUID = -4099577099607551657L;

    private static CoordinateArraySequenceFactory instanceObject = new CoordinateArraySequenceFactory();

    private CoordinateArraySequenceFactory() {
    }

    private Object readResolve() {
        return CoordinateArraySequenceFactory.instance();
    }

    /**
   * Returns the singleton instance of {@link CoordinateArraySequenceFactory}
   */
    public static CoordinateArraySequenceFactory instance() {
        return instanceObject;
    }

    /**
   * Returns a {@link CoordinateArraySequence} based on the given array (the array is
   * not copied).
   *
   * @param coordinates
   *            the coordinates, which may not be null nor contain null
   *            elements
   */
    public CoordinateSequence create(Coordinate[] coordinates) {
        return new CoordinateArraySequence(coordinates);
    }

    /**
   * @see com.vividsolutions.jts.geom.CoordinateSequenceFactory#create(com.vividsolutions.jts.geom.CoordinateSequence)
   */
    public CoordinateSequence create(CoordinateSequence coordSeq) {
        return new CoordinateArraySequence(coordSeq);
    }

    /**
   * @see com.vividsolutions.jts.geom.CoordinateSequenceFactory#create(int, int)
   *
   * @throws IllegalArgumentException if the dimension is > 3
   */
    public CoordinateSequence create(int size, int dimension) {
        if (dimension > 3) throw new IllegalArgumentException("dimension must be <= 3");
        return new CoordinateArraySequence(size);
    }
}
