package visad.georef;

import visad.RealTupleType;
import visad.VisADException;
import java.awt.geom.Rectangle2D;

/**
 * A trivial implementation for a MapProjection which provides an
 * identity coordinate system with a default bounding box.  This is
 * useful for defining an area in LatLon or LonLat space with a 
 * bounding box.  What goes in, comes right back out.
 */
public class TrivialMapProjection extends MapProjection {

    private float x, y, width, height;

    /**
     * Create a MapProjection that just returns the input tuple.
     * Default Map area is set to be from (-180,-90) to (180, 90)
     *
     * @throws VisADException  reference does not contain Latitude/Longitude
     *                         or couldn't create the necessary VisAD object
     */
    public TrivialMapProjection() throws VisADException {
        this(RealTupleType.SpatialEarth2DTuple, new Rectangle2D.Float(-180, -90, 360, 180));
    }

    /**
     * Create a MapProjection that just returns the input tuple.
     *
     * @param  reference  reference RealTupleType
     *
     * @throws VisADException  reference does not contain Latitude/Longitude
     *                         or couldn't create the necessary VisAD object
     */
    public TrivialMapProjection(RealTupleType reference) throws VisADException {
        this(reference, new Rectangle2D.Float(-180, -90, 360, 180));
    }

    /**
     * Create a MapProjection that just returns the input tuple.
     *
     * @param  reference  reference RealTupleType
     *
     * @throws VisADException  reference does not contain Latitude/Longitude
     *                         or couldn't create the necessary VisAD object
     */
    public TrivialMapProjection(RealTupleType type, Rectangle2D bounds) throws VisADException {
        super(type, type.getDefaultUnits());
        x = (float) bounds.getX();
        y = (float) bounds.getY();
        width = (float) bounds.getWidth();
        height = (float) bounds.getHeight();
    }

    /** 
     * Transform to the reference coordinates
     *
     * @param  tuple  array of values
     * @return  input array
     *
     * @throws VisADException  tuple is null or wrong dimension
     */
    public double[][] toReference(double[][] tuple) throws VisADException {
        if (tuple == null || getDimension() != tuple.length) throw new VisADException("Values are null or wrong dimension");
        return tuple;
    }

    /** 
     * Transform from the reference coordinates
     *
     * @param  refTuple  array of values
     * @return  input array
     *
     * @throws VisADException  tuple is null or wrong dimension
     */
    public double[][] fromReference(double[][] refTuple) throws VisADException {
        if (refTuple == null || getDimension() != refTuple.length) throw new VisADException("Values are null or wrong dimension");
        return refTuple;
    }

    /** 
     * Transform to the reference coordinates
     *
     * @param  tuple  array of values
     * @return  input array
     *
     * @throws VisADException  tuple is null or wrong dimension
     */
    public float[][] toReference(float[][] tuple) throws VisADException {
        if (tuple == null || getDimension() != tuple.length) throw new VisADException("Values are null or wrong dimension");
        return tuple;
    }

    /** 
     * Transform from the reference coordinates
     *
     * @param  refTuple  array of values
     * @return  input array
     *
     * @throws VisADException  tuple is null or wrong dimension
     */
    public float[][] fromReference(float[][] refTuple) throws VisADException {
        if (refTuple == null || getDimension() != refTuple.length) throw new VisADException("Values are null or wrong dimension");
        return refTuple;
    }

    /**
     * See if the object in question is equal to this CoordinateSystem.
     * The two objects are equal if they are the same object or if they
     * are both TrivialMapProjection and have the same dimension.
     *
     * @param  cs  Object in question
     * @return  true if they are considered equal, otherwise false.
     */
    public boolean equals(Object o) {
        if (!(o instanceof TrivialMapProjection)) return false;
        TrivialMapProjection that = (TrivialMapProjection) o;
        return (this == that) || (that.getReference().equals(this.getReference()) && Double.doubleToLongBits(that.x) == Double.doubleToLongBits(this.x) && Double.doubleToLongBits(that.y) == Double.doubleToLongBits(this.y) && Double.doubleToLongBits(that.width) == Double.doubleToLongBits(this.width) && Double.doubleToLongBits(that.height) == Double.doubleToLongBits(this.height));
    }

    /**
     * Get a reasonable bounding box in this coordinate system. MapProjections 
     * are typically specific to an area of the world; there's no bounding 
     * box that works for all projections so each subclass must implement
     * this method. For example, the bounding box for a satellite image 
     * MapProjection might have an upper left corner of (0,0) and the width 
     * and height of the Rectangle2D would be the number of elements and 
     * lines, respectively.
     *
     * @return the bounding box of the MapProjection
     *
     */
    public Rectangle2D getDefaultMapArea() {
        return new Rectangle2D.Float(x, y, width, height);
    }
}
