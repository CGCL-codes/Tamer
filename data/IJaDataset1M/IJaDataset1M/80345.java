package de.grogra.grammar;

public final class LongLiteral extends NumberLiteral {

    private long value;

    private int radix;

    public LongLiteral(String value, int radix) {
        super(LONG_LITERAL, value);
        this.radix = -radix;
    }

    public LongLiteral(long value) {
        super(LONG_LITERAL, null);
        this.value = value;
        radix = 10;
    }

    public boolean isDecimal() {
        return (radix == 10) || (radix == -10);
    }

    public static long parse(CharSequence s, int begin, int end, int radix, boolean allowOverflowOnce) throws NumberFormatException {
        if ((s == null) || (begin < 0) || (end > s.length()) || (end <= begin)) {
            throw new NumberFormatException("Empty string");
        }
        if (radix < Character.MIN_RADIX) {
            throw new NumberFormatException("radix " + radix + " less than Character.MIN_RADIX");
        } else if (radix > Character.MAX_RADIX) {
            throw new NumberFormatException("radix " + radix + " greater than Character.MAX_RADIX");
        }
        int i = begin;
        boolean negative;
        long limit;
        if (s.charAt(i) == '-') {
            negative = true;
            i++;
            if (i == end) {
                throw new NumberFormatException(s.subSequence(begin, end).toString());
            }
            limit = Long.MIN_VALUE;
        } else {
            negative = false;
            limit = -Long.MAX_VALUE;
        }
        long limitBefore = limit / radix, result = 0;
        while (i < end) {
            int digit = Character.digit(s.charAt(i++), radix);
            if (digit < 0) {
                throw new NumberFormatException(s.subSequence(begin, end).toString());
            }
            boolean overflow = result < limitBefore;
            result *= radix;
            if (overflow || (result < limit + digit)) {
                if (allowOverflowOnce) {
                    allowOverflowOnce = false;
                } else {
                    throw new NumberFormatException(s.subSequence(begin, end).toString());
                }
            }
            result -= digit;
        }
        return negative ? result : -result;
    }

    @Override
    public long longValue() {
        if (radix < 0) {
            String s = super.getText();
            return value = parse(s, 0, s.length(), radix = -radix, (radix == 8) || (radix == 16));
        }
        return value;
    }

    @Override
    public int intValue() {
        return (int) longValue();
    }

    @Override
    public float floatValue() {
        return longValue();
    }

    @Override
    public double doubleValue() {
        return longValue();
    }

    @Override
    public String getText() {
        String s = super.getText();
        if (s == null) {
            s = String.valueOf(value);
            setText(s);
            radix = 10;
        }
        return s;
    }
}
