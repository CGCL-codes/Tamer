package net.sf.jagg;

/**
 * This class represents the "sample standard deviation" aggregator over
 * numeric values.
 *
 * @author Randy Gettman
 * @since 0.1.0
 */
public class StdDevAggregator extends AbstractVarianceAggregator {

    /**
    * Constructs an <code>StdDevAggregator</code> that operates on the specified
    * property.
    * @param property Calculate the standard deviation of this property's values.
    */
    public StdDevAggregator(String property) {
        super(property);
    }

    /**
    * Returns an uninitialized copy of this <code>Aggregator</code> object,
    * with the same property(ies) to analyze.
    * @return An uninitialized copy of this <code>Aggregator</code> object.
    */
    public StdDevAggregator replicate() {
        return new StdDevAggregator(getProperty());
    }

    /**
    * Return the sample standard deviation by taking the square root of the
    * sample variance.
    *
    * @return The sample standard deviation as a <code>Double</code>.  Returns
    *    0 if only 0 or 1 items have been processed.
    */
    public Double terminate() {
        return terminateDoubleDouble().doubleValue();
    }

    /**
    * Return the result as a <code>DoubleDouble</code>.  This is used mainly
    * when other <code>Aggregators</code> that use this result must maintain a
    * high precision.
    * @return The sample standard deviation as a <code>DoubleDouble</code>.
    * @since 0.4.0
    */
    public DoubleDouble terminateDoubleDouble() {
        if (myCount <= 1) return new DoubleDouble();
        DoubleDouble result = new DoubleDouble(myVarNumerator);
        result.divideSelfBy(myCount - 1);
        result.sqrtSelf();
        return result;
    }
}
