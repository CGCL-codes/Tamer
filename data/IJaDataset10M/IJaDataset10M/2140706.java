package xxl.core.math.statistics.parametric.aggregates;

import xxl.core.functions.AbstractFunction;
import xxl.core.functions.Function;
import xxl.core.math.functions.StatelessAggregationFunction;

/**
 * Provides the same functionality as {@link StatefulStandardDeviation} but
 * keeps the state information. Hence, the incrementally
 * computed aggregate consists of an Object array whose
 * first component is the current standard deviation and the following
 * components are the state.
 * 
 * @see StatefulStandardDeviation
 */
public class StatelessStandardDeviation extends StatelessAggregationFunction<Number, Object[], Number> {

    /**
     * The aggregate mapping function.
     */
    public static final Function<Object[], Number> AGGREGATE_MAPPING = new AbstractFunction<Object[], Number>() {

        @Override
        public Number invoke(Object[] state) {
            return (Number) state[0];
        }
    };

    /** internally used Function for recursive computing of internally used variance */
    protected StatelessVariance var;

    /**
	 * Creates a new stateless standard deviation aggregation function.
	 */
    public StatelessStandardDeviation() {
        var = new StatelessVariance();
    }

    /** 
     * Function call for incremental aggregation.
     * The first argument corresponds to the old aggregate,
     * whereas the second argument corresponds to the new
     * incoming value. <br>
     * Depending on these two arguments the new aggregate, i.e. 
     * average, has to be computed and returned.
	 * 
	 * @param state result of the aggregation function in the previous computation step
	 * @param next next object used for computation
	 * @return an Object array that contains the new aggregation value of type Double,
	 * and a counter of type Integer that reveals how often this function has
	 * been invoked.
	 */
    @Override
    public Object[] invoke(Object[] state, Number next) {
        if (next == null) return state;
        if (state == null) return new Object[] { 0d, var.invoke(null, next) };
        Number[] v = var.invoke((Number[]) state[1], next);
        return new Object[] { Math.sqrt(var.getAggregateMapping().invoke(v).doubleValue()), v };
    }

    @Override
    public Function<Object[], Number> getAggregateMapping() {
        return AGGREGATE_MAPPING;
    }
}
