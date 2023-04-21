package net.rptools.parser.function.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import net.rptools.parser.Parser;
import net.rptools.parser.function.AbstractNumberFunction;
import net.rptools.parser.function.EvaluationException;
import net.rptools.parser.function.ParameterException;

public class BitwiseAnd extends AbstractNumberFunction {

    public BitwiseAnd() {
        super(1, -1, "bitwiseand", "band");
    }

    @Override
    public Object childEvaluate(Parser parser, String functionName, List<Object> parameters) throws EvaluationException, ParameterException {
        BigInteger value = null;
        for (Object param : parameters) {
            BigDecimal n = (BigDecimal) param;
            if (value == null) {
                value = n.toBigInteger();
            } else {
                value = value.and(n.toBigInteger());
            }
        }
        return new BigDecimal(value);
    }
}
