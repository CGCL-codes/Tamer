package org.zkforge.apache.commons.el;

import org.zkoss.xel.XelException;

/**
 *
 * <p>The implementation of the integer divide operator
 * 
 * @author Nathan Abramson - Art Technology Group
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: luehe $
 **/
public class IntegerDivideOperator extends BinaryOperator {

    public static final IntegerDivideOperator SINGLETON = new IntegerDivideOperator();

    /**
   *
   * Constructor
   **/
    public IntegerDivideOperator() {
    }

    /**
   *
   * Returns the symbol representing the operator
   **/
    public String getOperatorSymbol() {
        return "idiv";
    }

    /**
   *
   * Applies the operator to the given value
   **/
    public Object apply(Object pLeft, Object pRight, Logger pLogger) throws XelException {
        if (pLeft == null && pRight == null) {
            if (pLogger.isLoggingWarning()) {
                pLogger.logWarning(Constants.ARITH_OP_NULL, getOperatorSymbol());
            }
            return PrimitiveObjects.getInteger(0);
        }
        long left = Coercions.coerceToPrimitiveNumber(pLeft, Long.class, pLogger).longValue();
        long right = Coercions.coerceToPrimitiveNumber(pRight, Long.class, pLogger).longValue();
        try {
            return PrimitiveObjects.getLong(left / right);
        } catch (Exception exc) {
            if (pLogger.isLoggingError()) {
                pLogger.logError(Constants.ARITH_ERROR, getOperatorSymbol(), "" + left, "" + right);
            }
            return PrimitiveObjects.getInteger(0);
        }
    }
}
