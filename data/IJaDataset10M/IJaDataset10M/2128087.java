package bonneville.tests14.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.SortedMap;
import java.util.TreeMap;
import bonneville.util.Globals;
import bonneville.util.Timer;

/**
 * Test arithmetic using ints, longs, doubles, BigDecimals, and BigIntegers.
 */
public class TestArithmetic {

    public static void main(String[] args) {
        final String JVM_MODE = args[0];
        final String TEST_NAME = args[1].substring(args[1].lastIndexOf(".") + 1);
        final long OUTER_LOOPS = Long.parseLong(args[2]);
        final int INT_MAX = Integer.parseInt(args[3]);
        final long LONG_MAX = Long.parseLong(args[4]);
        final float DOUBLE_MAX = Float.parseFloat(args[5]);
        final BigDecimal BIGDECIMAL_MAX = new BigDecimal(args[6]);
        final BigInteger BIGINTEGER_MAX = new BigInteger(args[7]);
        SortedMap testParms = new TreeMap();
        testParms.put(Globals.JVM_MODE_KEY, JVM_MODE);
        testParms.put(Globals.TEST_NAME_KEY, TEST_NAME);
        testParms.put(Globals.OUTER_LOOPS_KEY, new Long(OUTER_LOOPS).toString());
        testParms.put("intMax", new Integer(INT_MAX).toString());
        testParms.put("longMax", new Long(LONG_MAX).toString());
        testParms.put("doubleMax", new Double(DOUBLE_MAX).toString());
        testParms.put("bigdecimalMax", BIGDECIMAL_MAX.toString());
        testParms.put("bigintegerMax", BIGINTEGER_MAX.toString());
        Timer totalTimer = new Timer(testParms);
        for (long i = 0; i < OUTER_LOOPS; i++) {
            totalTimer.start();
            doIntMath(INT_MAX);
            doLongMath(LONG_MAX);
            doDoubleMath(DOUBLE_MAX);
            doBigDecimalMath(BIGDECIMAL_MAX);
            doBigIntegerMath(BIGINTEGER_MAX);
            totalTimer.stopAndPrintResult();
            System.out.println();
        }
        totalTimer.printAverageResults();
    }

    private static void doIntMath(int intMax) {
        Timer intTimer = new Timer();
        intTimer.start();
        int intAddSubtractResult = 0;
        for (int j = 0; j < intMax; j++) {
            intAddSubtractResult += j;
            intAddSubtractResult -= (j - 1);
        }
        int intMultiplyDivideResult = 1;
        for (int j = 2; j < intMax; j++) {
            intMultiplyDivideResult *= j;
            intMultiplyDivideResult /= j - 1;
        }
        intTimer.stop();
        System.out.println(Globals.SPACER + "int results: " + intTimer.getResult() + ", " + "add/sub = " + intAddSubtractResult + ", " + "mult/div = " + intMultiplyDivideResult);
    }

    private static void doLongMath(long longMax) {
        Timer longTimer = new Timer();
        longTimer.start();
        long longAddSubtractResult = 0;
        for (long j = 0; j < longMax; j++) {
            longAddSubtractResult += j;
            longAddSubtractResult -= (j - 1);
        }
        int longMultiplyDivideResult = 1;
        for (int j = 2; j < longMax; j++) {
            longMultiplyDivideResult *= j;
            longMultiplyDivideResult /= (j - 1);
        }
        longTimer.stop();
        System.out.println(Globals.SPACER + "long results: " + longTimer.getResult() + ", " + "add/sub = " + longAddSubtractResult + ", " + "mult/div = " + longMultiplyDivideResult);
    }

    private static void doDoubleMath(double doubleMax) {
        Timer doubleTimer = new Timer();
        doubleTimer.start();
        double doubleAddSubtractResult = 0.0;
        for (double j = 0.0; j < doubleMax; j++) {
            doubleAddSubtractResult += j;
            doubleAddSubtractResult -= (j - 1.0);
        }
        double doubleMultiplyDivideResult = 1.0;
        for (double j = 2.0; j < doubleMax; j++) {
            doubleMultiplyDivideResult *= j;
            doubleMultiplyDivideResult /= (j - 1.0);
        }
        doubleTimer.stop();
        System.out.println(Globals.SPACER + "double results: " + doubleTimer.getResult() + ", " + "add/sub = " + doubleAddSubtractResult + ", " + "mult/div = " + doubleMultiplyDivideResult);
    }

    private static void doBigDecimalMath(BigDecimal bigDecimalMax) {
        Timer bigDecimalTimer = new Timer();
        bigDecimalTimer.start();
        final BigDecimal BIG_DECIMAL_ONE = new BigDecimal("1");
        BigDecimal bigDecimalAddSubtractResult = new BigDecimal(0.0);
        for (BigDecimal j = new BigDecimal(0.0); j.compareTo(bigDecimalMax) < 0; j = j.add(BIG_DECIMAL_ONE)) {
            bigDecimalAddSubtractResult = bigDecimalAddSubtractResult.add(j);
            bigDecimalAddSubtractResult = bigDecimalAddSubtractResult.subtract(j.subtract(BIG_DECIMAL_ONE));
        }
        BigDecimal bigDecimalMultiplyDivideResult = new BigDecimal(1.0);
        for (BigDecimal j = new BigDecimal(2.0); j.compareTo(bigDecimalMax) < 0; j = j.add(BIG_DECIMAL_ONE)) {
            bigDecimalMultiplyDivideResult = bigDecimalMultiplyDivideResult.multiply(j);
            bigDecimalMultiplyDivideResult = bigDecimalMultiplyDivideResult.divide(j.subtract(BIG_DECIMAL_ONE), BigDecimal.ROUND_UNNECESSARY);
        }
        bigDecimalTimer.stop();
        System.out.println(Globals.SPACER + "BigDecimal results: " + bigDecimalTimer.getResult() + ", " + "add/sub = " + bigDecimalAddSubtractResult + ", " + "mult/div = " + bigDecimalMultiplyDivideResult);
    }

    private static void doBigIntegerMath(BigInteger bigIntegerMax) {
        Timer bigIntegerTimer = new Timer();
        bigIntegerTimer.start();
        final BigInteger BIG_INTEGER_ONE = new BigInteger("1");
        BigInteger bigIntegerAddSubtractResult = BigInteger.valueOf(0);
        for (BigInteger j = BigInteger.valueOf(0); j.compareTo(bigIntegerMax) < 0; j = j.add(BIG_INTEGER_ONE)) {
            bigIntegerAddSubtractResult = bigIntegerAddSubtractResult.add(j);
            bigIntegerAddSubtractResult = bigIntegerAddSubtractResult.subtract(j.subtract(BIG_INTEGER_ONE));
        }
        BigInteger bigIntegerMultiplyDivideResult = BigInteger.valueOf(1);
        for (BigInteger j = BigInteger.valueOf(2); j.compareTo(bigIntegerMax) < 0; j = j.add(BIG_INTEGER_ONE)) {
            bigIntegerMultiplyDivideResult = bigIntegerMultiplyDivideResult.multiply(j);
            bigIntegerMultiplyDivideResult = bigIntegerMultiplyDivideResult.divide(j.subtract(BIG_INTEGER_ONE));
        }
        bigIntegerTimer.stop();
        System.out.println(Globals.SPACER + "BigInteger results: " + bigIntegerTimer.getResult() + ", " + "add/sub = " + bigIntegerAddSubtractResult + "mult/div = " + bigIntegerMultiplyDivideResult);
    }
}
