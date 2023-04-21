package net.sf.mustang.types;

import java.math.BigDecimal;
import java.util.StringTokenizer;
import net.sf.mustang.K;

public class XNumber {

    private static final int PRECISION = 32;

    private static final String ZERO = "0";

    private static final String I = "i";

    private static final String F = "f";

    private static final String X = "x";

    private static final char CI = 'i';

    private static final char CF = 'f';

    private static final char CX = 'x';

    private static final String SEP = ".-";

    private BigDecimal value;

    public XNumber() {
        value = new BigDecimal(ZERO).setScale(PRECISION, BigDecimal.ROUND_HALF_EVEN);
    }

    public XNumber(String s) {
        value = new BigDecimal(s).setScale(PRECISION, BigDecimal.ROUND_HALF_EVEN);
    }

    public XNumber(XNumber kn) {
        value = new BigDecimal(kn.toString()).setScale(PRECISION, BigDecimal.ROUND_HALF_EVEN);
    }

    public XNumber(double d) {
        value = new BigDecimal(K.EMPTY + d).setScale(PRECISION, BigDecimal.ROUND_HALF_EVEN);
    }

    public String toString() {
        BigDecimal bd = value;
        try {
            while (bd.scale() > 0) {
                bd = bd.setScale(bd.scale() - 1);
            }
        } catch (ArithmeticException e) {
        }
        return bd.toString();
    }

    public double toDouble() {
        return value.doubleValue();
    }

    public float toFloat() {
        return value.floatValue();
    }

    public long toLong() {
        return value.longValue();
    }

    public int toInt() {
        return value.intValue();
    }

    public Integer toInteger() {
        return new Integer(value.intValue());
    }

    public String get(String theFormat) {
        return format(theFormat);
    }

    public String format(String theFormat) {
        StringBuffer retVal = new StringBuffer();
        String intPart = K.EMPTY;
        String decPart = K.EMPTY;
        String grp = K.EMPTY;
        int nDec = 0;
        int nInt = 0;
        int nGrp = 0;
        for (int i = 0; i < theFormat.length(); i++) {
            if (theFormat.charAt(i) == CI) nInt++; else if (theFormat.charAt(i) == CF) nDec++;
        }
        BigDecimal bd = value.setScale(nDec, BigDecimal.ROUND_HALF_UP);
        String theSValue = bd.toString();
        boolean isNeg = (value.signum() < 0);
        StringTokenizer st = new StringTokenizer(theSValue, SEP);
        intPart = st.nextToken();
        if (st.hasMoreTokens()) decPart = st.nextToken();
        String temp = K.EMPTY;
        for (int i = 0; i < theFormat.length(); ) {
            temp = theFormat.substring(i);
            if (temp.startsWith(I) || temp.startsWith(X)) {
                int e = temp.lastIndexOf(CX);
                if (temp.lastIndexOf(CI) > e) e = temp.lastIndexOf(CI);
                i += e + 1;
                for (int j = e; j >= 0; j--) {
                    if (temp.charAt(j) != CI && temp.charAt(j) != CX) {
                        if (nGrp == 0) nGrp = e - j;
                        grp = K.EMPTY + temp.charAt(j);
                        j = -1;
                    }
                }
                for (int j = intPart.length(); j < nInt; j++) intPart = ZERO + intPart;
                for (int j = intPart.length() - nGrp; nGrp > 0 && j > 0; j -= nGrp) intPart = intPart.substring(0, j) + grp + intPart.substring(j);
                retVal.append(intPart);
            } else if (temp.startsWith(F)) {
                i += decPart.length();
                retVal.append(decPart);
            } else {
                retVal.append(temp.charAt(0));
                i += 1;
            }
        }
        if (isNeg) retVal.insert(0, K.MINUS);
        return retVal.toString();
    }

    public XNumber set(String s) {
        value = new BigDecimal(s).setScale(PRECISION, BigDecimal.ROUND_HALF_EVEN);
        return this;
    }

    public XNumber set(XNumber n) {
        return set(n.toString());
    }

    public XNumber set(java.lang.Number n) {
        return set(n.toString());
    }

    public XNumber set(double d) {
        return set(K.EMPTY + d);
    }

    public XNumber set(int i) {
        return set(K.EMPTY + i);
    }

    public boolean gt(String s) {
        return (value.compareTo(new BigDecimal(s)) > 0);
    }

    public boolean gt(XNumber n) {
        return gt(n.toString());
    }

    public boolean gt(java.lang.Number n) {
        return gt(n.toString());
    }

    public boolean gt(double d) {
        return gt(K.EMPTY + d);
    }

    public boolean gt(int i) {
        return gt(K.EMPTY + i);
    }

    public boolean gtEq(String s) {
        return (value.compareTo(new BigDecimal(s)) >= 0);
    }

    public boolean gtEq(XNumber n) {
        return gtEq(n.toString());
    }

    public boolean gtEq(java.lang.Number n) {
        return gtEq(n.toString());
    }

    public boolean gtEq(double d) {
        return gtEq(K.EMPTY + d);
    }

    public boolean gtEq(int i) {
        return gtEq(K.EMPTY + i);
    }

    public boolean lt(String s) {
        return (value.compareTo(new BigDecimal(s)) < 0);
    }

    public boolean lt(XNumber n) {
        return lt(n.toString());
    }

    public boolean lt(java.lang.Number n) {
        return lt(n.toString());
    }

    public boolean lt(double d) {
        return lt(K.EMPTY + d);
    }

    public boolean lt(int i) {
        return lt(K.EMPTY + i);
    }

    public boolean ltEq(String s) {
        return (value.compareTo(new BigDecimal(s)) <= 0);
    }

    public boolean ltEq(XNumber n) {
        return ltEq(n.toString());
    }

    public boolean ltEq(java.lang.Number n) {
        return ltEq(n.toString());
    }

    public boolean ltEq(double d) {
        return ltEq(K.EMPTY + d);
    }

    public boolean ltEq(int i) {
        return ltEq(K.EMPTY + i);
    }

    public boolean eq(String s) {
        return (value.compareTo(new BigDecimal(s)) == 0);
    }

    public boolean eq(XNumber n) {
        return eq(n.toString());
    }

    public boolean eq(java.lang.Number n) {
        return eq(n.toString());
    }

    public boolean eq(double d) {
        return eq(K.EMPTY + d);
    }

    public boolean eq(int i) {
        return eq(K.EMPTY + i);
    }

    public XNumber mul(String s) {
        value = value.multiply(new BigDecimal(s));
        return this;
    }

    public XNumber mul(XNumber n) {
        return mul(n.toString());
    }

    public XNumber mul(java.lang.Number n) {
        return mul(n.toString());
    }

    public XNumber mul(double d) {
        return mul(K.EMPTY + d);
    }

    public XNumber mul(int i) {
        return mul(K.EMPTY + i);
    }

    public XNumber mod(String s) {
        value = value.remainder(new BigDecimal(s));
        return this;
    }

    public XNumber mod(XNumber n) {
        return mod(n.toString());
    }

    public XNumber mod(java.lang.Number n) {
        return mod(n.toString());
    }

    public XNumber mod(double d) {
        return mod(K.EMPTY + d);
    }

    public XNumber mod(int i) {
        return mod(K.EMPTY + i);
    }

    public XNumber div(String s) {
        value = value.divide(new BigDecimal(s), BigDecimal.ROUND_HALF_EVEN);
        return this;
    }

    public XNumber div(XNumber n) {
        return div(n.toString());
    }

    public XNumber div(java.lang.Number n) {
        return div(n.toString());
    }

    public XNumber div(double d) {
        return div(K.EMPTY + d);
    }

    public XNumber div(int i) {
        return div(K.EMPTY + i);
    }

    public XNumber add(String s) {
        value = value.add(new BigDecimal(s));
        return this;
    }

    public XNumber add(XNumber n) {
        return add(n.toString());
    }

    public XNumber add(java.lang.Number n) {
        return add(n.toString());
    }

    public XNumber add(double d) {
        return add(K.EMPTY + d);
    }

    public XNumber add(int i) {
        return add(K.EMPTY + i);
    }

    public XNumber sub(String s) {
        value = value.subtract(new BigDecimal(s));
        return this;
    }

    public XNumber sub(XNumber n) {
        return sub(n.toString());
    }

    public XNumber sub(java.lang.Number n) {
        return sub(n.toString());
    }

    public XNumber sub(double d) {
        return sub(K.EMPTY + d);
    }

    public XNumber sub(int i) {
        return sub(K.EMPTY + i);
    }

    public XNumber abs() {
        value = value.abs();
        return this;
    }

    public static double String2Double(Object s) {
        double retVal = 0;
        try {
            retVal = Double.parseDouble((String) s);
        } catch (Exception e) {
        }
        return retVal;
    }

    public static float String2Float(Object s) {
        float retVal = 0;
        try {
            retVal = Float.parseFloat((String) s);
        } catch (Exception e) {
        }
        return retVal;
    }

    public static long String2Long(Object s) {
        long retVal = 0;
        try {
            retVal = Long.parseLong((String) s);
        } catch (Exception e) {
        }
        return retVal;
    }

    public static int String2Int(Object s) {
        int retVal = 0;
        try {
            retVal = Integer.parseInt((String) s);
        } catch (Exception e) {
        }
        return retVal;
    }

    public static boolean isNumber(Object s) {
        boolean retVal = true;
        try {
            Double.parseDouble((String) s);
        } catch (Exception e) {
            retVal = false;
        }
        return retVal;
    }
}
