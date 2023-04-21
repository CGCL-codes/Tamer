package net.rptools.chartool.model.property.function;

import java.math.BigDecimal;
import java.util.List;
import net.rptools.parser.Parser;
import net.rptools.parser.function.AbstractFunction;
import net.rptools.parser.function.EvaluationException;
import net.rptools.parser.function.ParameterException;

/**
 * Function that returns the index of one string w/in another or -1 if the string isn't in the other.
 * 
 * @author jgorrell
 * @version $Revision$ $Date$ $Author$
 */
public class IndexOfFunction extends AbstractFunction {

    /**
   * Describe the function to the parser.
   */
    public IndexOfFunction() {
        super(2, 2, "indexOf");
    }

    /**
   * @see net.rptools.parser.function.AbstractFunction#childEvaluate(net.rptools.parser.Parser, java.lang.String, java.util.List)
   */
    @Override
    public Object childEvaluate(Parser aParser, String functionName, List<Object> parameters) throws EvaluationException, ParameterException {
        return BigDecimal.valueOf(((String) parameters.get(0)).indexOf((String) parameters.get(1)));
    }

    /**
   * @see net.rptools.parser.function.AbstractFunction#checkParameters(java.util.List)
   */
    @Override
    public void checkParameters(List<Object> aParameters) throws ParameterException {
        super.checkParameters(aParameters);
        if (!(aParameters.get(0) instanceof String)) throw new ParameterException("First parameter must be a string but a " + (aParameters.get(0) == null ? "null" : aParameters.get(0).getClass().getName() + " was passed"));
        if (!(aParameters.get(1) instanceof String)) throw new ParameterException("Second parameter must be a string but a " + (aParameters.get(1) == null ? "null" : aParameters.get(1).getClass().getName() + " was passed"));
    }
}
