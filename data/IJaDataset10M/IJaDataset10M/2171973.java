package org.mandarax.util.resultsetfilters.aggregationfunctions;

import org.mandarax.kernel.LogicFactory;
import org.mandarax.kernel.ResultSet;
import org.mandarax.kernel.VariableTerm;
import org.mandarax.util.resultsetfilters.AggregationException;
import org.mandarax.util.resultsetfilters.AggregationFunction;

/**
 * COUNT function. Counts records within a group.
 * @author <A href="http://www-ist.massey.ac.nz/JBDietrich" target="_top">Jens Dietrich</A>
 * @version 3.4 <7 March 05>
 * @since 3.0
 */
public class COUNT implements AggregationFunction {

    private static Class[] supportedTypes = { Object.class };

    private int counter = 0;

    /**
	 * Constructor.
	 * @param var the variable
	 */
    public COUNT() throws AggregationException {
        super();
    }

    /**
	 * Copy this function (get a newly initialized instance of this class).
	 * @return a function
	 */
    public AggregationFunction copy() throws AggregationException {
        return new COUNT();
    }

    /**
	 * Get the name of this function.
	 * @return a name
	 */
    public String getName() {
        return "COUNT";
    }

    /**
	 * Get an array of supported types.
	 * @return an array of types
	 */
    protected Class[] getSupportedTypes() {
        return supportedTypes;
    }

    /**
	 * Add a value. May throw an IllegalArgumentException
	 * indicating that this type of value cannot be aggregated.
	 * @param rs a result set
	 */
    public void add(ResultSet rs) throws AggregationException {
        counter++;
    }

    /**
	 * Get the aggregated value.
	 * @return a value
	 */
    public Object getAggregatedValue() throws AggregationException {
        return new Integer(counter);
    }

    /**
	 * Get the result type. This type might differ from the input type (e.g. when counting records).
	 * @return the result type
	 */
    public Class getResultType() {
        return Integer.class;
    }

    /**
	 * Set the type.
	 * @param clazz a type.
	 */
    public void setType(Class clazz) throws AggregationException {
    }

    /**
	 * Get the variable that can be used to retrieve the value from
	 * the filtered result set.
	 * @return a variable name
	 */
    public VariableTerm getVariableInResult() {
        return LogicFactory.getDefaultFactory().createVariableTerm("COUNT(*)", getResultType());
    }
}
