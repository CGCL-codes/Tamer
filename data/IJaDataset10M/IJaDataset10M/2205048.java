package net.rptools.parser.function.impl;

import java.math.BigDecimal;
import java.util.List;
import net.rptools.parser.Parser;
import net.rptools.parser.function.AbstractNumberFunction;
import net.rptools.parser.function.EvaluationException;
import net.rptools.parser.function.ParameterException;

public class Min extends AbstractNumberFunction {

    public Min() {
        super(1, -1, "min");
    }

    @Override
    public Object childEvaluate(Parser parser, String functionName, List<Object> parameters) throws EvaluationException, ParameterException {
        BigDecimal result = null;
        boolean first = true;
        for (Object param : parameters) {
            BigDecimal n = (BigDecimal) param;
            if (first) {
                result = n;
                first = false;
            } else {
                result = result.min(n);
            }
        }
        return result;
    }
}
