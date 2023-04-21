package alice.tuprolog;

/**
 *
 * Int class represents the integer prolog data type
 *
 *
 *
 */
public class Int extends Number {

    private int value;

    public Int(int v) {
        value = v;
    }

    /**
     *  Returns the value of the Integer as int
     *
     */
    public final int intValue() {
        return value;
    }

    /**
     *  Returns the value of the Integer as float
     *
     */
    public final float floatValue() {
        return (float) value;
    }

    /**
     *  Returns the value of the Integer as double
     *
     */
    public final double doubleValue() {
        return (double) value;
    }

    /**
     *  Returns the value of the Integer as long
     *
     */
    public final long longValue() {
        return value;
    }

    /** is this term a prolog integer term? */
    public final boolean isInteger() {
        return true;
    }

    /** is this term a prolog real term? */
    public final boolean isReal() {
        return false;
    }

    /** is an int Integer number? */
    public final boolean isTypeInt() {
        return true;
    }

    /** is a float Real number? */
    public final boolean isTypeFloat() {
        return false;
    }

    /** is a double Real number? */
    public final boolean isTypeDouble() {
        return false;
    }

    /** is a long Integer number? */
    public final boolean isTypeLong() {
        return false;
    }

    public boolean isObject() {
        return false;
    }

    /**
     * Returns true if this integer term is grater that the term provided.
     * For number term argument, the int value is considered.
     */
    public boolean isGreater(Term t) {
        t = t.getTerm();
        if (t.isNumber()) {
            return value > ((Number) t).intValue();
        } else if (t.isStruct()) {
            return false;
        } else if (t.isVar()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if this integer term is equal that the term provided.
     * For number term argument, the int value is considered.
     */
    public boolean isEqual(Term t) {
        t = t.getTerm();
        if (t.isNumber()) {
            return value == ((Number) t).intValue();
        } else {
            return false;
        }
    }

    /**
     * Tries to unify a term with the provided term argument.
     * This service is to be used in demonstration context.
     */
    boolean unify(Term t, int m) {
        t = t.getTerm();
        if (t.isVar()) {
            return t.unify(this, m);
        } else if (t.isNumber()) {
            return value == ((Number) t).intValue();
        } else {
            return false;
        }
    }

    public String toString() {
        return Integer.toString(value);
    }
}
