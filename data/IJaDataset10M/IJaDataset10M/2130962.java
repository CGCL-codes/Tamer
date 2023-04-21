package ch.ethz.mxquery.util;

import ch.ethz.mxquery.exceptions.DynamicException;
import ch.ethz.mxquery.exceptions.ErrorCodes;
import ch.ethz.mxquery.exceptions.MXQueryException;

/**
 * Double implementation on an integer. <br />
 * NaN, Inf, and Exponential representation (e.g. 2e-4) are not supported
 * 
 * @author David Alexander Graf
 * 
 */
public class LongDouble {

    private long value;

    private long zero;

    public static final int maxDecimalPlaces = 18;

    private int decimalPlaces;

    private long longMax;

    /**
     * Returns the last digit of the passed value.
     * 
     * @param value
     * @return the last digit
     */
    public static int getLastDigit(long value) {
        long cur = (value / 10) * 10;
        return (int) (value - cur);
    }

    /**
     * Computes n * 10^i
     * 
     * @param n
     * @param i
     * @return n * 10^i
     */
    private long addPowTen(long n, int i) {
        int value = 1;
        for (int j = 0; j < i; j++) {
            value *= 10;
        }
        return value * n;
    }

    /**
     * Sets up all constants
     * 
     * @param decimalPlaces
     *                The thing that the user must define
     */
    private void setUp(int decimalPlaces) {
        if (decimalPlaces > IntDouble.maxDecimalPlaces) {
            throw new NumberFormatException("Too many decimal places!");
        } else if (decimalPlaces < 0) {
            throw new NumberFormatException("No negativ decimal places!");
        }
        this.decimalPlaces = decimalPlaces;
        this.zero = this.addPowTen(1, this.decimalPlaces);
        this.longMax = Long.MAX_VALUE / this.zero - 1;
    }

    /**
     * Constructor
     * 
     * @param decimalPlaces
     */
    public LongDouble(int decimalPlaces) {
        this.setUp(decimalPlaces);
        this.value = 0;
    }

    /**
     * Constructor
     * 
     * @param str
     *                tries to parse this string
     * @param decimalPlaces
     */
    public LongDouble(String str, int decimalPlaces) {
        this.setUp(decimalPlaces);
        long intValue = 0;
        long commaValue = 0;
        boolean negative = false;
        if (str.startsWith("-")) {
            negative = true;
            str = str.substring(1);
        } else if (str.startsWith("+")) {
            str = str.substring(1);
        }
        if (str.indexOf(".") >= 0) {
            if (str.startsWith(".")) {
                str = "0" + str;
            }
            int dotIndex = str.indexOf(".");
            String strInt = str.substring(0, dotIndex);
            String strComma = str.substring(dotIndex + 1, str.length());
            strComma = IntDouble.deleteEndZeros(strComma);
            if (strComma.length() > this.decimalPlaces) {
                throw new NumberFormatException("Overflow");
            }
            commaValue = Integer.parseInt(strComma);
            if (commaValue != 0) {
                int commaZeros = IntDouble.countStartZeros(strComma);
                commaValue = this.addPowTen(commaValue, this.decimalPlaces - String.valueOf(commaValue).length() - commaZeros);
            }
            intValue = Integer.parseInt(strInt);
        } else {
            intValue = Integer.parseInt(str);
        }
        if (intValue > this.longMax) {
            throw new NumberFormatException("Overflow");
        }
        this.value = intValue * this.zero + commaValue;
        if (negative) {
            this.value = -this.value;
        }
    }

    /**
     * Constructor
     * 
     * @param init
     *                initial value (only integer)
     * @param decimalPlaces
     */
    public LongDouble(long init, int decimalPlaces) {
        this.setUp(decimalPlaces);
        this.value = init * this.zero;
    }

    private LongDouble(long init, int decimalPlaces, boolean origValue) {
        this.setUp(decimalPlaces);
        if (origValue) {
            this.value = init;
        } else {
            this.value = init * this.zero;
        }
    }

    public boolean equalsZero() {
        return this.value == 0;
    }

    public LongDouble add(LongDouble ld) {
        long value = this.value + ld.value;
        return new LongDouble(value, this.decimalPlaces, true);
    }

    public LongDouble add(int i) {
        long value = this.value + i * this.zero;
        return new LongDouble(value, this.decimalPlaces, true);
    }

    public LongDouble subtract(LongDouble ld) {
        long value = this.value - ld.value;
        return new LongDouble(value, this.decimalPlaces, true);
    }

    public LongDouble subtract(int i) {
        long value = this.value - i * this.zero;
        return new LongDouble(value, this.decimalPlaces, true);
    }

    public LongDouble multiply(LongDouble ld) {
        long val = 0;
        long cur = this.value;
        long foreignValue = ld.value;
        int multiplier = 1;
        boolean negativ = false;
        if (cur < 0) {
            cur = -cur;
            negativ = !negativ;
        }
        if (foreignValue < 0) {
            foreignValue = -foreignValue;
            negativ = !negativ;
        }
        for (int i = 0; i < 19; i++) {
            if (cur == 0) {
                break;
            }
            int digit = LongDouble.getLastDigit(cur);
            if (i < this.decimalPlaces) {
                val += foreignValue * digit;
                val /= 10;
            } else {
                val += multiplier * foreignValue * digit;
                multiplier *= 10;
            }
            cur = cur / 10;
        }
        if (negativ) {
            val = -val;
        }
        return new LongDouble(val, this.decimalPlaces, true);
    }

    public LongDouble multiply(int i) {
        long value = this.value * i;
        return new LongDouble(value, this.decimalPlaces, true);
    }

    public LongDouble divide(LongDouble ld) throws MXQueryException {
        long index = 1000000000;
        index *= index;
        long restValue = this.value;
        long rest = 0;
        long result = 0;
        long foreignValue = ld.value;
        boolean negativ = false;
        if (restValue < 0) {
            restValue = -restValue;
            negativ = !negativ;
        }
        if (foreignValue < 0) {
            foreignValue = -foreignValue;
            negativ = !negativ;
        }
        for (int i = 0; i < this.decimalPlaces; i++) {
            long cur = restValue / index;
            restValue -= cur * index;
            cur += 10 * rest;
            if (cur / foreignValue > 0) {
                throw new DynamicException(ErrorCodes.F0003_OVERFLOW_UNDERFLOW_NUMERIC, "overflow", null);
            }
            rest = cur % foreignValue;
            index /= 10;
        }
        long i2 = 1000000000;
        i2 *= i2;
        for (int i = 0; i < 19; i++) {
            long cur = 0;
            if (index > 0) {
                cur = restValue / index;
                restValue -= cur * index;
            }
            cur += 10 * rest;
            long res = cur / foreignValue;
            if (i == 0) {
                if (res > 9) {
                    throw new DynamicException(ErrorCodes.F0003_OVERFLOW_UNDERFLOW_NUMERIC, "overflow", null);
                }
            }
            result += i2 * res;
            rest = cur % foreignValue;
            index /= 10;
            i2 /= 10;
        }
        if (negativ) {
            result = -result;
        }
        return new LongDouble(result, this.decimalPlaces, true);
    }

    public LongDouble divide(int i) {
        long value = this.value / i;
        return new LongDouble(value, this.decimalPlaces, true);
    }

    public LongDouble mod(LongDouble ld) {
        long value = this.value % ld.value;
        return new LongDouble(value, this.decimalPlaces, true);
    }

    public LongDouble mod(int i) {
        long value = this.value % (i * this.zero);
        return new LongDouble(value, this.decimalPlaces, true);
    }

    public int idiv(LongDouble ld) throws MXQueryException {
        LongDouble newd = this.divide(ld);
        return newd.getIntValue();
    }

    public int idiv(int i) {
        LongDouble d = this.divide(i);
        return d.getIntValue();
    }

    public LongDouble negate() {
        long value = -this.value;
        return new LongDouble(value, this.decimalPlaces, true);
    }

    public int hashCode() {
        return (int) (value ^ zero);
    }

    public boolean equals(Object o) {
        if (o instanceof LongDouble) {
            return equals((LongDouble) o);
        }
        return false;
    }

    public boolean equals(LongDouble ld) {
        return this.value == ld.value;
    }

    public boolean unequals(LongDouble ld) {
        return this.value != ld.value;
    }

    public int compareTo(LongDouble ld) {
        if (this.value > ld.value) {
            return 1;
        } else if (this.value < ld.value) {
            return -1;
        } else {
            return 0;
        }
    }

    public int compareTo(int i) {
        return this.compareTo(new LongDouble(i, this.decimalPlaces));
    }

    private long getCommaValue() {
        long commaVal = this.value % this.zero;
        if (commaVal < 0) {
            return -commaVal;
        } else {
            return commaVal;
        }
    }

    public int getIntValue() {
        return (int) (this.value / this.zero);
    }

    public String toString() {
        String str = "";
        long intValue = this.getIntValue();
        if (intValue == 0 && this.value < 0) {
            str += "-";
        }
        return str + String.valueOf(intValue) + "." + IntDouble.deleteEndZeros(IntDouble.addStartZerosToComma(String.valueOf(this.getCommaValue()), this.decimalPlaces));
    }
}
