package org.mandarax.util.resultsetfilters.aggregationfunctions;

import java.math.*;
import org.mandarax.kernel.VariableTerm;
import org.mandarax.util.resultsetfilters.AggregationException;
import org.mandarax.util.resultsetfilters.AggregationFunction;

/**
 * SUM function.
 * @author <A href="http://www-ist.massey.ac.nz/JBDietrich" target="_top">Jens Dietrich</A>
 * @version 3.4 <7 March 05>
 * @since 3.0
 */
public class SUM extends AbstractAggregationFunction {

    private static Class[] supportedTypes = { Integer.class, Double.class, Float.class, Long.class, Short.class, BigInteger.class, BigDecimal.class, String.class };

    /**
	 * Constructor.
	 * @param var the variable
	 */
    public SUM(VariableTerm var) throws AggregationException {
        super(var);
    }

    /**
	 * Get the name of this function.
	 * @return a name
	 */
    public String getName() {
        return "SUM";
    }

    /**
	 * Get an array of supported types.
	 * @return an array of types
	 */
    protected Class[] getSupportedTypes() {
        return supportedTypes;
    }

    /**
	 * Compute the aggregated value.
	 * @return the computed object
	 */
    protected Object compute() throws AggregationException {
        try {
            if (type == Integer.class) {
                int sum = 0;
                for (int i = 0; i < getNumberOfValues(); i++) sum = sum + getValueAsInt(i);
                return new Integer(sum);
            }
            if (type == Double.class) {
                double sum = 0;
                for (int i = 0; i < getNumberOfValues(); i++) sum = sum + getValueAsDouble(i);
                return new Double(sum);
            }
            if (type == Float.class) {
                float sum = 0;
                for (int i = 0; i < getNumberOfValues(); i++) sum = sum + getValueAsFloat(i);
                return new Float(sum);
            }
            if (type == Short.class) {
                int sum = 0;
                for (int i = 0; i < getNumberOfValues(); i++) sum = sum + this.getValueAsShort(i);
                return new Integer(sum);
            }
            if (type == Long.class) {
                long sum = 0;
                for (int i = 0; i < getNumberOfValues(); i++) sum = sum + this.getValueAsLong(i);
                return new Long(sum);
            }
            if (type == BigInteger.class) {
                BigInteger sum = new BigInteger("0");
                for (int i = 0; i < getNumberOfValues(); i++) sum = sum.add(getValueAsBigInteger(i));
                return sum;
            }
            if (type == BigDecimal.class) {
                BigDecimal sum = new BigDecimal("0");
                for (int i = 0; i < getNumberOfValues(); i++) sum = sum.add(getValueAsBigDecimal(i));
                return sum;
            }
            if (type == String.class) {
                StringBuffer buf = new StringBuffer();
                for (int i = 0; i < getNumberOfValues(); i++) buf.append(getValueAsString(i));
                return buf.toString();
            }
        } catch (Throwable x) {
            throw new AggregationException("Error computing aggregated value", x);
        }
        throw new AggregationException("Cannot compute value - unsupported type");
    }

    /**
	 * Copy this function (get a newly initialized instance of this class).
	 * @return a function
	 */
    public AggregationFunction copy() throws AggregationException {
        return new SUM(var);
    }
}
