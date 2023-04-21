package gumbo.tech.math;

import gumbo.core.update.annotation.Mutates;
import gumbo.core.update.annotation.Observes;

/**
 * A specialized Tuple2 typically used as the basis for 2D spatial
 * quantities (min/max, inside/outside, etc.). The maximum value is always
 * greater than or equal to the minimum value. Depending on usage, the range
 * limits can define inside or outside, and can be inclusive or exclusive.
 * Object equality is based on identity. Use Quantitative methods for
 * state-based equality.
 * <P>
 * Derived from gumbo3.graphic.math.Range.
 * @author jonb
 */
public interface Range extends Tuple2 {

    /**
	 * Sets the minimum value of this range.
	 * @param val Temp input value.  Never null.
	 * @return Reference to this object. Never null.
	 * @throws IllegalStateException if the new minimum is greater than the
	 * maximum.
	 */
    @Mutates(props = BEAN_RANGE)
    public Range setMin(double val);

    /**
	 * Sets the maximum value of this range.
	 * @param val Temp input value.  Never null.
	 * @return Reference to this object. Never null.
	 * @throws IllegalStateException if the new maximum is less than the
	 * minimum.
	 */
    @Mutates(props = BEAN_RANGE)
    public Range setMax(double val);

    /**
	 * Sets this range to the specified value.
	 * @param min Temp input minimum value. Never null.
	 * @param max Temp input maximum value. Never null.
	 * @return Reference to this object. Never null.
	 * @throws IllegalStateException if minimum is greater than the maximum.
	 */
    @Mutates(props = BEAN_RANGE)
    @Override
    public Range set(double min, double max);

    /**
	 * Sets this range to the specified extent. The range values will be
	 * reordered as needed.
	 * @param valA Temp input value. Never null.
	 * @param valB Temp input value. Never null.
	 * @return Reference to this object. Never null.
	 */
    @Mutates(props = BEAN_RANGE)
    public Range setExtent(double valA, double valB);

    /**
	 * Gets the minimum value of this range.
	 * @return Temp output value.  Never null.
	 */
    @Observes(props = BEAN_RANGE)
    public double getMin();

    /**
	 * Gets the maximum value of this range.
	 * @return Temp output value.  Never null.
	 */
    @Observes(props = BEAN_RANGE)
    public double getMax();

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range setX(double val);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range setY(double val);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range set(Tuple2 val);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range set(double val);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range clear();

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range random();

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range noise();

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range neg();

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range neg(Tuple2 tuple);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range abs();

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range abs(Tuple2 tuple);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range round();

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range round(Tuple2 tuple);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range add(double val);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range add(Tuple2 tuple, double val);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range add(Tuple2 tuple);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range add(Tuple2 tupleA, Tuple2 tupleB);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range sub(double val);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range sub(Tuple2 tuple, double val);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range sub(Tuple2 tuple);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range sub(Tuple2 tupleA, Tuple2 tupleB);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range mult(double val);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range mult(Tuple2 tuple, double val);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range mult(Tuple2 tuple);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range mult(Tuple2 tupleA, Tuple2 tupleB);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range div(double val);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range divInv(double val);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range div(Tuple2 tuple, double val);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range div(double val, Tuple2 tuple);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range div(Tuple2 tuple);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range divInv(Tuple2 tuple);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range div(Tuple2 tupleA, Tuple2 tupleB);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range min(double val);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range min(Tuple2 tuple, double val);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range min(Tuple2 tuple);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range min(Tuple2 tupleA, Tuple2 tupleB);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range max(double val);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range max(Tuple2 tuple, double val);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range max(Tuple2 tuple);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range max(Tuple2 tupleA, Tuple2 tupleB);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range clampMin(double min);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range clampMin(Tuple2 tuple, double min);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range clampMin(Tuple2 min);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range clampMin(Tuple2 tuple, Tuple2 min);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range clampMax(double max);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range clampMax(Tuple2 tuple, double max);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range clampMax(Tuple2 max);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range clampMax(Tuple2 tuple, Tuple2 max);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range clamp(double min, double max);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range clamp(Tuple2 tuple, double min, double max);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range clamp(Tuple2 min, Tuple2 max);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range clamp(Tuple2 tuple, Tuple2 min, Tuple2 max);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range set(int dim, double val);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range set(double[] vals);

    @Mutates(props = BEAN_RANGE)
    @Override
    public Range dupe();

    /**
	 * Bean property ID for updates to Range state. Unless otherwise noted, the
	 * before and after event type is UpdateEvent.
	 */
    public static final String BEAN_RANGE = "Range";
}
