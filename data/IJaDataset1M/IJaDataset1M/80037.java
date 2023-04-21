package net.sf.jagg;

/**
 * This abstract class represents variance-like aggregator calculations over
 * numeric values.
 *
 * @author Randy Gettman
 * @since 0.3.0
 */
public abstract class AbstractVarianceAggregator extends Aggregator {

    /**
    * A running count of items processed so far for the given property.
    */
    protected long myCount;

    /**
    * A running total of items processed so far for the given property.
    */
    protected DoubleDouble mySum = new DoubleDouble();

    /**
    * A running total of the variance, before it is divided by
    * the denominator in the variance calculation.
    */
    protected DoubleDouble myVarNumerator = new DoubleDouble();

    /**
    * Constructs an <code>VarianceAggregator</code> that operates on the specified
    * property.
    * @param property Calculate the variance of this property's values.
    */
    public AbstractVarianceAggregator(String property) {
        setProperty(property);
    }

    /**
    * Initialize the sum and count to zero.
    */
    public void init() {
        myCount = 0;
        mySum.reset();
        myVarNumerator.reset();
    }

    /**
    * If the property is non-null, then count it and add the property value to
    * the sum.  Update the variance numerator.
    *
    * @param value The value to aggregate.
    */
    public void iterate(Object value) {
        if (value != null) {
            String property = getProperty();
            try {
                Number obj = (Number) getValueFromProperty(value, property);
                if (obj != null) {
                    long oldCount = myCount;
                    myCount++;
                    double dVal = obj.doubleValue();
                    mySum.addToSelf(dVal);
                    if (myCount == 1) myVarNumerator.reset(); else {
                        DoubleDouble temp = new DoubleDouble(dVal);
                        temp.multiplySelfBy(myCount);
                        temp.subtractFromSelf(mySum);
                        temp.squareSelf();
                        temp.divideSelfBy(myCount);
                        temp.divideSelfBy(oldCount);
                        myVarNumerator.addToSelf(temp);
                    }
                }
            } catch (ClassCastException e) {
                throw new UnsupportedOperationException("Property \"" + property + "\" must represent a Number.", e);
            }
        }
    }

    /**
    * Merge the given <code>Aggregator</code> into this one.  Add the
    * respective sums and counts together.  Update the variance numerator.
    *
    * @param agg The <code>Aggregator</code> to merge into this one.
    */
    public void merge(Aggregator agg) {
        if (agg != null && agg instanceof AbstractVarianceAggregator) {
            AbstractVarianceAggregator otherAgg = (AbstractVarianceAggregator) agg;
            DoubleDouble temp = new DoubleDouble(otherAgg.myCount);
            temp.divideSelfBy(myCount);
            temp.multiplySelfBy(mySum);
            temp.subtractFromSelf(otherAgg.mySum);
            temp.squareSelf();
            DoubleDouble temp3 = new DoubleDouble(myCount);
            temp3.divideSelfBy(otherAgg.myCount * (myCount + otherAgg.myCount));
            temp3.multiplySelfBy(temp);
            myVarNumerator.addToSelf(otherAgg.myVarNumerator);
            myVarNumerator.addToSelf(temp3);
            mySum.addToSelf(otherAgg.mySum);
            myCount += otherAgg.myCount;
        }
    }

    /**
    * Return the result as a <code>DoubleDouble</code>.  This is used mainly
    * when other <code>Aggregators</code> that use this result must maintain a
    * high precision.
    * @return A <code>DoubleDouble</code> representing the result of the
    *    aggregation.
    */
    public abstract DoubleDouble terminateDoubleDouble();
}
