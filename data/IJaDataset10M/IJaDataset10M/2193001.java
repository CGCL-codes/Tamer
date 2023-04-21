package com.w20e.socrates.expression;

import com.w20e.socrates.expression.Expression;

/**
 * String implementation for expression. The XString is basically a wrapper
 * class for the java.lang.String, that implements the Expression interface.
 */
public class XString extends XObjectImpl {

    /**
	 * UID.
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Wrapped string for the XString object.
	 */
    private final String str;

    /**
	 * Constructor taking a String object as argument. The XString class wraps
	 * the argument string with Expression methods.
	 * 
	 * @param string
	 *            the string to wrap.
	 */
    public XString(final String string) {
        this.str = string;
    }

    /**
	 * String representation of this object.
	 * 
	 * @return the <code>java.lang.String</code> object wrapped by this class.
	 */
    public final String toString() {
        return this.str;
    }

    /**
	 * Get the Number obect wrapped by this XString. If the value cannot be
	 * represented as a number, a Float representating NaN will be returned.
	 * 
	 * @return the wrapped <code>Number</code> object
	 */
    public final Number toNumber() {
        try {
            return Double.valueOf(this.str.replaceAll(" ", ""));
        } catch (Exception e) {
            return Float.valueOf(Float.NaN);
        }
    }

    /**
	 * The signature for this method is that it should return 0 if the
	 * expressions are equal, a value less than zero if the expression provided
	 * as an argument is greater than the expression, and a value bigger than
	 * zero if the expression provided as argument is less than the expression.
	 * If the argument is not an instance of Expression, a ClassCastException is
	 * thrown.
	 * 
	 * @param expr
	 *            The expression to use for comparison.
	 * @return integer value indicating result of comparison
	 */
    public final int compareTo(final Expression expr) {
        if (expr instanceof Undef) {
            if (this.str == null) {
                return 0;
            }
            return 1;
        }
        if (this.str == null) {
            return -1;
        }
        return this.str.compareTo(expr.toString());
    }

    /**
	 * Compares this XObject to the specified object. If the argument is null,
	 * and the XObject is Undef, true will be returned. If either the XObject
	 * itself or the argument is an XBoolean, both will converted to boolean.
	 * Otherwise if one is an XNumber, both will be converted to an XNumber.
	 * Otherwise the comparison will be string wise.
	 * 
	 * @param obj
	 *            The expression to use in equality check.
	 * @return the boolean representation of this object.
	 */
    public final boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Expression)) {
            return false;
        }
        Expression expr = (Expression) obj;
        if (expr.eval() instanceof Undef) {
            return false;
        } else if (expr.eval() instanceof XBoolean) {
            return expr.eval().toBoolean() == toBoolean();
        } else if (expr.eval() instanceof XNumber) {
            return expr.eval().toNumber().equals(toNumber());
        }
        return toString().equals(expr.eval().toString());
    }

    /**
	 * Returns true if and only if the String wrapped by this class is not null,
	 * and is not an empty string.
	 * 
	 * @return boolean representation of this object.
	 */
    public final boolean toBoolean() {
        if ("".equals(this.str) || this.str == null) {
            return false;
        }
        return true;
    }

    /**
	 * Return underlying object. This is always a String.
	 * 
	 * @return the wrapped String object
	 */
    public final Object toObject() {
        return this.str;
    }

    /**
	 * Override HashCode.
	 * 
	 * @see java.lang.Object#hashCode()
	 * @return hash
	 */
    public final int hashCode() {
        return this.str.hashCode();
    }
}
