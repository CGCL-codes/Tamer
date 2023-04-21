package de.cnc.expression.prepostfixoperators;

import java.math.BigDecimal;
import de.cnc.expression.AbstractRuntimeEnvironment;
import de.cnc.expression.AbstractToken;
import de.cnc.expression.exceptions.ExpressionEvaluationException;
import de.cnc.expression.exceptions.ExpressionParseException;

public class MinusPrefixOperator extends AbstractPrePostfixOperator {

    public static MinusPrefixOperator parse(String paStr, int paIntLine, int paIntCol, String paStrOriginalSource) throws ExpressionParseException {
        if ("-".equals(paStr) || (paStr.length() > 1 && paStr.startsWith("-") && paStr.charAt(1) != '-')) {
            MinusPrefixOperator minusOp = new MinusPrefixOperator(paStrOriginalSource, paIntLine, paIntCol);
            minusOp.strSource = "-";
            minusOp.iSrcLength = 1;
            return minusOp;
        }
        return null;
    }

    /**
   * constructor
   */
    public MinusPrefixOperator(String paStrOriginalSource, int paIntLine, int paIntCol) {
        super(paStrOriginalSource, paIntLine, paIntCol);
    }

    /**
   * process the operator
   */
    public final Object eval(final AbstractRuntimeEnvironment pRunEnv, AbstractToken paTok) throws ExpressionEvaluationException {
        return eval(paTok.eval(pRunEnv));
    }

    /**
   * process the operator
   */
    protected Object eval(Object paObjValue) throws ExpressionEvaluationException {
        if (paObjValue == null) {
            throw new ExpressionEvaluationException(this, "operand missed");
        }
        if (!(paObjValue instanceof Number)) {
            throw new ExpressionEvaluationException(this, "operand is " + paObjValue + ":" + paObjValue.getClass().getName() + " ; numeric expected");
        }
        return new BigDecimal("" + paObjValue).negate();
    }
}
