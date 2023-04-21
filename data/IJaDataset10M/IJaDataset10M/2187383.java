package xxl.core.spatial.points;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import xxl.core.functions.AbstractFunction;
import xxl.core.functions.Function;
import xxl.core.io.Convertable;

/**
 * A Wrapper for double[]-points that provides useful methods on points
 * like e.g. a conversion mechanism.
 *
 * @see xxl.core.io.Convertable
 * @see xxl.core.spatial.points.Point
 * @see xxl.core.spatial.points.Points
 * @see xxl.core.spatial.points.AbstractPoint
 * @see xxl.core.spatial.points.FloatPoint
 *
 *
 */
public class DoublePoint extends AbstractPoint implements Convertable, Cloneable {

    /** A factory for DoublePoints.
	 */
    public static final Function FACTORY = new AbstractFunction() {

        public Object invoke(Object point) {
            return new DoublePoint((double[]) point);
        }

        public Object invoke(Object object, Object dim) {
            return new DoublePoint(((Integer) dim).intValue());
        }
    };

    /** The primitive double-point to be wrapped.
	 */
    protected double[] point;

    /** Creates a new DoublePoint.
	 *
	 *	@param point the primitive double-point to be wrapped.
	 */
    public DoublePoint(double[] point) {
        this.point = point;
    }

    /** Creates a new DoublePoint.
	 *  (with coordinates (0,...,0)).
	 *
	 *	@param dim dimensionality of the point
	 */
    public DoublePoint(int dim) {
        this(new double[dim]);
    }

    /** Creates a new DoublePoint from a NumberPoint<Double>.
	 *
	 *	@param point the NumberPoint<Double> to be wrapped.
	 */
    public DoublePoint(NumberPoint<Double> np) {
        this(np.dimensions());
        Double[] arr = (Double[]) np.getPoint();
        for (int i = 0; i < point.length; i++) point[i] = arr[i];
    }

    /** Returns a physical copy of this DoublePoint.
	 * @return returns a physical copy of this DoublePoint
	 */
    @Override
    public Object clone() {
        return new DoublePoint((double[]) point.clone());
    }

    /** Returns (gets) the primitive double-point wrapped by this DoublePoint.
	 * @return returns the primitive double-point wrapped by this DoublePoint
	 */
    @Override
    public Object getPoint() {
        return point;
    }

    /** Returns the dimensionality of this DoublePoint.
	 * @return returns the dimensionality of this DoublePoint
	 */
    @Override
    public int dimensions() {
        return point.length;
    }

    /**
	 * Reads the state (the attributes) for an object of this class from
	 * the specified data input and restores the calling object. The state
	 * of the object before calling <tt>read</tt> will be lost.<br>
	 * The <tt>read</tt> method must read the values in the same sequence
	 * and with the same types as were written by <tt>write</tt>.
	 *
	 * @param dataInput the stream to read data from in order to restore
	 *        the object.
	 * @throws IOException if I/O errors occur.
	 */
    @Override
    public void read(DataInput dataInput) throws IOException {
        for (int i = 0; i < point.length; i++) point[i] = dataInput.readDouble();
    }

    /**
	 * Writes the state (the attributes) of the calling object to the
	 * specified data output. This method should serialize the state of
	 * this object without calling another <tt>write</tt> method in order
	 * to prevent recursions.
	 *
	 * @param dataOutput the stream to write the state (the attributes) of
	 *        the object to.
	 * @throws IOException includes any I/O exceptions that may occur.
	 */
    @Override
    public void write(DataOutput dataOutput) throws IOException {
        for (int i = 0; i < point.length; i++) dataOutput.writeDouble(point[i]);
    }

    /** Returns the coordinate of this DoublePoint in a given dimension <dim>.
	 * 	
	 * @param dim dimension to get coordinate
	 * @return returns the coordinate in given dimension
	 */
    @Override
    public double getValue(int dim) {
        return point[dim];
    }

    /** Checks whether the point is equal to another object given.
	 * 
	 * @param object object to be tested.
	 * @return <tt>true</tt> if the point is equal to given object.
	 * 
	 * @see java.lang.Object#equals()
	 */
    @Override
    public boolean equals(Object o) {
        DoublePoint p = (DoublePoint) o;
        return Arrays.equals(point, p.point);
    }

    /** Checks whether the point is equal to another object given within a given epsilon.
	 * 
	 * @param object object to be tested.
	 * @param epsilon the tolerance of the comparison
	 * @return <tt>true</tt> if the point is equal to given object within a given epsilon.
	 */
    public boolean equalsEpsilon(DoublePoint p, double epsilon) {
        for (int i = 0; i < this.dimensions(); i++) {
            if (Math.abs(this.point[i] - p.point[i]) > epsilon) return false;
        }
        return true;
    }

    public int hashCode() {
        double c = 0;
        for (int i = 0; i < point.length; i++) c += point[i];
        return (int) c % 1117;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < point.length; i++) {
            sb.append(point[i] + "\t");
        }
        return sb.toString();
    }
}
