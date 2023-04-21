package net.sf.jagg.test.model;

import net.sf.jagg.Aggregator;

/**
 * Just to test the custom <code>Aggregator</code> feature.
 *
 * @author Randy Gettman
 * @since 0.3.0
 */
public class ComplexSumAggregator extends Aggregator {

    private Complex mySum = null;

    /**
    * Constructs an <code>ComplexSumAggregator</code> that operates on the specified
    * property.
    * @param property Add up all this property's values.
    */
    public ComplexSumAggregator(String property) {
        setProperty(property);
    }

    /**
    * Returns an uninitialized copy of this <code>Aggregator</code> object,
    * with the same property(ies) to analyze.
    * @return An uninitialized copy of this <code>Aggregator</code> object.
    */
    public ComplexSumAggregator replicate() {
        return new ComplexSumAggregator(getProperty());
    }

    /**
    * Initialize the sum to 0 (+ 0i).
    */
    public void init() {
        if (mySum == null) mySum = new Complex(); else mySum.reset();
    }

    /**
    * Add the property value to the sum.
    *
    * @param value The value to aggregate.
    */
    public void iterate(Object value) {
        if (value != null) {
            String property = getProperty();
            try {
                Complex obj = (Complex) getValueFromProperty(value, property);
                if (obj != null) {
                    mySum = mySum.add(obj);
                }
            } catch (ClassCastException e) {
                throw new UnsupportedOperationException("Property \"" + property + "\" must be a Complex.", e);
            }
        }
    }

    /**
    * Merge the given <code>Aggregator</code> into this one by adding the
    * respective sums.
    *
    * @param agg The <code>Aggregator</code> to merge into this one.
    */
    public void merge(Aggregator agg) {
        if (agg != null && agg instanceof ComplexSumAggregator) {
            ComplexSumAggregator otherAgg = (ComplexSumAggregator) agg;
            mySum = mySum.add(otherAgg.mySum);
        }
    }

    /**
    * Return the sum.
    *
    * @return The sum as a <code>Complex</code>.
    */
    public Complex terminate() {
        return mySum;
    }
}
