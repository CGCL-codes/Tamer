package org.apache.myfaces.trinidadinternal.ui.data.bind;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.myfaces.trinidad.logging.TrinidadLogger;
import org.apache.myfaces.trinidadinternal.ui.UIXRenderingContext;
import org.apache.myfaces.trinidadinternal.ui.data.BoundValue;

/**
 * Booolean BoundValue that compares either two BoundValues or a
 * BoundValues and an Object with a comparison operator and
 * returns the Boolean result.
 * <p>
 * <STRONG>
 * Only BoundValues that return <CODE>java.lang.Numbers</CODE> or
 * <CODE>java.lang.Numbers</CODE> can be used with comparisons other than
 * <CODE>COMPARISON_EQUALS</CODE> and <CODE>COMPARISON_NOT_EQUALS</CODE>
 * </STRONG>
 * <p>
 *@version $Name:  $ ($Revision: 245 $) $Date: 2008-11-25 19:05:42 -0500 (Tue, 25 Nov 2008) $
 * @deprecated This class comes from the old Java 1.2 UIX codebase and should not be used anymore.
 */
@Deprecated
public class ComparisonBoundValue implements BoundValue {

    /**
   * True if the left and right sides are equivalent.
   */
    public static final int COMPARISON_EQUALS = 1;

    /**
   * True if the left and right sides are not equivalent.
   */
    public static final int COMPARISON_NOT_EQUALS = ~COMPARISON_EQUALS;

    /**
   * True if the left side is greater than the right side.
   * <P>
   * <STRONG>This requires that both sides be or return
   * <CODE>java.lang.Number</CODE>s.
   */
    public static final int COMPARISON_GREATER_THAN = 2;

    /**
   * True if the left side is greater than or equal to the right side.
   * <P>
   * <STRONG>This requires that both sides be or return
   * <CODE>java.lang.Number</CODE>s.
   */
    public static final int COMPARISON_GREATER_THAN_OR_EQUALS = COMPARISON_GREATER_THAN + COMPARISON_EQUALS;

    /**
   * True if the left side is less than the right side.
   * <P>
   * <STRONG>This requires that both sides be or return
   * <CODE>java.lang.Number</CODE>s.
   */
    public static final int COMPARISON_LESS_THAN = ~COMPARISON_GREATER_THAN_OR_EQUALS;

    /**
   * True if the left side is less than or equal to the right side.
   * <P>
   * <STRONG>This requires that both sides be or return
   * <CODE>java.lang.Number</CODE>s.
   */
    public static final int COMPARISON_LESS_THAN_OR_EQUALS = ~COMPARISON_GREATER_THAN;

    public ComparisonBoundValue(int comparison, BoundValue leftSideValue, BoundValue rightSideValue) {
        if (leftSideValue == null) throw new IllegalArgumentException(_LOG.getMessage("NULL_LEFTSIDEVALUE"));
        if (rightSideValue == null) throw new IllegalArgumentException(_LOG.getMessage("NULL_RIGHTSIDEVALUE"));
        if ((comparison < COMPARISON_LESS_THAN) || (comparison > COMPARISON_GREATER_THAN_OR_EQUALS)) throw new IllegalArgumentException(_LOG.getMessage("UNKNOWN_COMPARISON"));
        _comparison = comparison;
        _leftSideValue = leftSideValue;
        _rightSideValue = rightSideValue;
    }

    public ComparisonBoundValue(int comparison, BoundValue leftSideValue, Object rightSide) {
        this(comparison, leftSideValue, new FixedBoundValue(rightSide));
    }

    public static ComparisonBoundValue createExistsValue(BoundValue existenceValue) {
        return new ComparisonBoundValue(COMPARISON_NOT_EQUALS, existenceValue, FixedBoundValue.NULL_VALUE);
    }

    /**
   * Calculates the current state of the model.
   */
    public Object getValue(UIXRenderingContext context) {
        Object leftSide = _leftSideValue.getValue(context);
        Object rightSide = _rightSideValue.getValue(context);
        boolean isNot = false;
        int comparison = _comparison;
        if ((comparison & COMPARISON_EQUALS) == 0) {
            comparison = ~comparison;
            isNot = true;
        }
        boolean areBothNumbers = (leftSide instanceof Number) && (rightSide instanceof Number);
        boolean newResult;
        if (leftSide == rightSide) {
            newResult = true;
        } else if (leftSide != null) {
            if (areBothNumbers) newResult = _equalsForNumbers((Number) leftSide, (Number) rightSide); else newResult = leftSide.equals(rightSide);
        } else {
            newResult = false;
        }
        if (!newResult && (comparison != COMPARISON_EQUALS)) {
            if ((leftSide == null) || (rightSide == null)) return Boolean.FALSE;
            if (!areBothNumbers) {
                if (_LOG.isSevere()) _LOG.severe(new IllegalArgumentException("Numeric comparisons only allowed on numbers"));
                return Boolean.FALSE;
            }
            Number leftNumber = (Number) leftSide;
            Number rightNumber = (Number) rightSide;
            if (comparison == COMPARISON_LESS_THAN_OR_EQUALS) {
                isNot = !isNot;
            }
            if (leftNumber instanceof Long) {
                newResult = (leftNumber.longValue() > rightNumber.longValue());
            } else {
                newResult = (leftNumber.doubleValue() > rightNumber.doubleValue());
            }
        }
        if (isNot) newResult = !newResult;
        return (newResult) ? Boolean.TRUE : Boolean.FALSE;
    }

    private static boolean _equalsForNumbers(Number a, Number b) {
        if ((a == null) || (b == null)) return (a == b);
        Class<?> ac = a.getClass();
        Class<?> bc = b.getClass();
        if (ac == bc) return a.equals(b);
        if ((ac == Long.class) || (ac == Integer.class) || (ac == Short.class) || (ac == Byte.class)) return _equalsForLong(a.longValue(), b, bc);
        if ((ac == Double.class) || (ac == Float.class)) return _equalsForDouble(a.doubleValue(), b, bc);
        if (ac == BigInteger.class) return _equalsForBigInteger((BigInteger) a, b, bc);
        if (ac == BigDecimal.class) return _equalsForBigDecimal((BigDecimal) a, b, bc);
        return a.equals(b);
    }

    private static boolean _equalsForLong(long a, Number b, Class<?> bc) {
        if ((bc == Double.class) || (bc == Float.class)) {
            return (b.doubleValue() == a);
        }
        if (bc == BigDecimal.class) {
            return BigDecimal.valueOf(a).equals(b);
        }
        if (bc == BigInteger.class) {
            return BigInteger.valueOf(a).equals(b);
        }
        return a == b.longValue();
    }

    private static boolean _equalsForDouble(double a, Number b, Class<?> bc) {
        if (bc == BigDecimal.class) {
            return new BigDecimal(a).equals(b);
        }
        if (bc == BigInteger.class) {
            return new BigDecimal(a).equals(new BigDecimal((BigInteger) b));
        }
        return a == b.doubleValue();
    }

    private static boolean _equalsForBigInteger(BigInteger a, Number b, Class<?> bc) {
        if (bc == BigDecimal.class) {
            return new BigDecimal(a).equals(b);
        }
        if ((bc == Double.class) || (bc == Float.class)) {
            return new BigDecimal(a).equals(new BigDecimal(b.doubleValue()));
        }
        return BigInteger.valueOf(b.longValue()).equals(a);
    }

    private static boolean _equalsForBigDecimal(BigDecimal a, Number b, Class<?> bc) {
        if (bc == BigInteger.class) {
            return a.equals(new BigDecimal((BigInteger) b));
        }
        if ((bc == Double.class) || (bc == Float.class)) {
            return a.equals(new BigDecimal(b.doubleValue()));
        }
        return a.equals(BigDecimal.valueOf(b.longValue()));
    }

    private BoundValue _leftSideValue;

    private BoundValue _rightSideValue;

    private int _comparison;

    private static final TrinidadLogger _LOG = TrinidadLogger.createTrinidadLogger(ComparisonBoundValue.class);
}
