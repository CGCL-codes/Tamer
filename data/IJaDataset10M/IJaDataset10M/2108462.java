package org.jmlspecs.unfinished;

public class JMLLong implements JMLComparable {

    private long longValue;

    public JMLLong() {
        longValue = 0;
    }

    public JMLLong(long inLong) {
        longValue = inLong;
    }

    public JMLLong(int inInt) {
        longValue = (long) inInt;
    }

    public JMLLong(Long inLong) {
        longValue = inLong.longValue();
    }

    public JMLLong(String s) throws JMLTypeException {
        try {
            longValue = Long.valueOf(s).longValue();
        } catch (RuntimeException re) {
            throw new JMLTypeException("Bad format string passed to " + "JMLLong(String): \"" + s + "\"");
        }
    }

    /** The JMLLong that represents zero.
     */
    public static final JMLLong ZERO = new JMLLong();

    /** The JMLLong that represents one.
     */
    public static final JMLLong ONE = new JMLLong(1);

    public Object clone() {
        return this;
    }

    public int compareTo(Object op2) throws ClassCastException {
        if (op2 instanceof JMLLong) {
            long lv2 = ((JMLLong) op2).longValue;
            if (longValue < lv2) {
                return -1;
            } else if (longValue == lv2) {
                return 0;
            } else {
                return +1;
            }
        } else {
            throw new ClassCastException();
        }
    }

    public boolean equals(Object op2) {
        return op2 != null && op2 instanceof JMLLong && longValue == ((JMLLong) op2).longValue;
    }

    /** Return a hash code for this object.
     */
    public int hashCode() {
        return (int) longValue;
    }

    public long longValue() {
        return longValue;
    }

    public Long getLong() {
        return new Long(longValue);
    }

    public JMLLong negated() {
        return new JMLLong(-longValue);
    }

    public JMLLong plus(JMLLong i2) {
        return new JMLLong(longValue + i2.longValue);
    }

    public JMLLong minus(JMLLong i2) {
        return new JMLLong(longValue - i2.longValue);
    }

    public JMLLong times(JMLLong i2) {
        return new JMLLong(longValue * i2.longValue);
    }

    public JMLLong dividedBy(JMLLong i2) {
        return new JMLLong(longValue / i2.longValue);
    }

    public JMLLong remainderBy(JMLLong i2) {
        return new JMLLong(longValue % i2.longValue);
    }

    public boolean greaterThan(JMLLong i2) {
        return longValue > i2.longValue;
    }

    public boolean lessThan(JMLLong i2) {
        return longValue < i2.longValue;
    }

    public boolean greaterThanOrEqualTo(JMLLong i2) {
        return longValue >= i2.longValue;
    }

    public boolean lessThanOrEqualTo(JMLLong i2) {
        return longValue <= i2.longValue;
    }

    public String toString() {
        return String.valueOf(longValue);
    }
}
