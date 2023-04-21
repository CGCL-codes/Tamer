package org.expasy.jpl.commons.base.cond.operator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.expasy.jpl.commons.base.cond.operator.api.Operator;
import org.expasy.jpl.commons.base.cond.operator.impl.OperatorAnd;
import org.expasy.jpl.commons.base.cond.operator.impl.OperatorApproxEquals;
import org.expasy.jpl.commons.base.cond.operator.impl.OperatorBelongs;
import org.expasy.jpl.commons.base.cond.operator.impl.OperatorBelongsToDoD;
import org.expasy.jpl.commons.base.cond.operator.impl.OperatorContains;
import org.expasy.jpl.commons.base.cond.operator.impl.OperatorEquals;
import org.expasy.jpl.commons.base.cond.operator.impl.OperatorGreaterThan;
import org.expasy.jpl.commons.base.cond.operator.impl.OperatorInter;
import org.expasy.jpl.commons.base.cond.operator.impl.OperatorLowerThan;
import org.expasy.jpl.commons.base.cond.operator.impl.OperatorMulti;
import org.expasy.jpl.commons.base.cond.operator.impl.OperatorOr;

/**
 * This manager stores all {@code Operator}s and provides an access by name.
 * 
 * @author nikitin
 * 
 * @version 1.0
 * 
 */
@SuppressWarnings("unchecked")
public final class OperatorManager {

    public static final OperatorMulti<Number> GREATER_EQUALS;

    public static final OperatorMulti<Number> LESS_EQUALS;

    private static final OperatorManager INSTANCE;

    private Map<String, Operator> operators;

    static {
        OperatorGreaterThan<Number> gt = OperatorGreaterThan.newInstance();
        OperatorLowerThan<Number> lt = OperatorLowerThan.newInstance();
        OperatorEquals<Number> eq = OperatorEquals.newInstance();
        GREATER_EQUALS = OperatorMulti.newInstance(Arrays.asList(gt, eq));
        LESS_EQUALS = OperatorMulti.newInstance(Arrays.asList(lt, eq));
        INSTANCE = new OperatorManager();
    }

    public OperatorManager() {
        Operator op;
        operators = new HashMap<String, Operator>();
        op = OperatorAnd.getInstance();
        operators.put(op.getToken(), op);
        op = OperatorOr.getInstance();
        operators.put(op.getToken(), op);
        op = OperatorEquals.newInstance();
        operators.put(op.getToken(), op);
        op = OperatorApproxEquals.newInstance();
        operators.put(op.getToken(), op);
        op = OperatorContains.newInstance();
        operators.put(op.getToken(), op);
        op = OperatorBelongs.newInstance();
        operators.put(op.getToken(), op);
        op = OperatorBelongsToDoD.newInstance();
        operators.put(op.getToken(), op);
        op = OperatorInter.newInstance();
        operators.put(op.getToken(), op);
        op = OperatorGreaterThan.newInstance();
        operators.put(op.getToken(), op);
        op = OperatorLowerThan.newInstance();
        operators.put(op.getToken(), op);
        op = OperatorLowerThan.newInstance();
        operators.put(op.getToken(), op);
        operators.put(GREATER_EQUALS.getToken(), GREATER_EQUALS);
        operators.put(LESS_EQUALS.getToken(), LESS_EQUALS);
    }

    public static OperatorManager getInstance() {
        return INSTANCE;
    }

    public Operator getOperator(String token) throws InvalidOperatorException {
        if (!operators.containsKey(token)) {
            throw new InvalidOperatorException(token);
        }
        return operators.get(token);
    }

    public boolean isConditionContainsOperator(String condition) {
        return getOperatorPosInCondition(condition).length != 0;
    }

    public int[] getOperatorPosInCondition(String condition) {
        for (String token : operators.keySet()) {
            Pattern pat = Pattern.compile("(\\" + token + ")");
            Matcher matcher = pat.matcher(condition);
            if (matcher.find()) {
                return new int[] { matcher.start(), matcher.end() };
            }
        }
        return new int[] {};
    }

    /**
	 * 
	 * @param condition
	 * @return
	 * @throws InvalidOperatorException
	 * 
	 * @throws OperatorNotFoundRTException if operator not found
	 */
    public String[] getRvalueOpLvalue(String condition) throws InvalidOperatorException {
        int[] opPositions = getOperatorPosInCondition(condition);
        if (opPositions.length == 2) {
            String[] rOpv = new String[3];
            rOpv[1] = condition.substring(opPositions[0], opPositions[1]);
            rOpv[2] = condition.substring(opPositions[1]).trim();
            rOpv[0] = condition.substring(0, opPositions[0]).trim();
            return rOpv;
        } else {
            throw new InvalidOperatorException(condition + ": cannot find operator " + " - badly formatted condition (choose a valid " + "operator from " + toString());
        }
    }

    public String toString() {
        return operators.keySet().toString();
    }

    public class InvalidOperatorException extends Exception {

        private static final long serialVersionUID = 2619370166950396830L;

        public InvalidOperatorException(String operator) {
            super(operator + ": not a valid operator token " + operators.keySet());
        }
    }

    /**
	 * Exception thrown when {@code AbstractOperator} is not well configured or
	 * adapted to the operand types.
	 * 
	 * @author nikitin
	 * 
	 * @version 1.0.0
	 * 
	 */
    public static class InvalidOperatorRTException extends RuntimeException {

        private static final long serialVersionUID = 7505270167210601346L;

        public InvalidOperatorRTException(final String message) {
            super(message + ": please choose a valid operator OR configure it correctly" + " with compatible" + " sets with the help of method 'setCompatibleOperands'" + "called from the constructor OR" + " override the method 'isCompatibleOperands'.");
        }
    }
}
