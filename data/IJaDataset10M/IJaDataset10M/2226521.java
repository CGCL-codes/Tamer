package ise.calculator;

import java.lang.reflect.Method;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Provides access to java.lang.Math and java.lang.StrictMath for Ant. Provides
 * add, subtract, multiply, divide and mod methods as well as access to all methods
 * java.lang.Math and java.lang.StrictMath via reflection.
 * @author Dale Anson, danson@grafidog.com
 */
public class Math {

    private boolean strict = false;

    public static Class BIGDECIMAL_TYPE;

    public static Class BIGINT_TYPE;

    static {
        try {
            BIGDECIMAL_TYPE = Class.forName("java.math.BigDecimal");
        } catch (ClassNotFoundException e) {
            BIGDECIMAL_TYPE = null;
        }
        try {
            BIGINT_TYPE = Class.forName("java.math.BigInteger");
        } catch (Exception e) {
            BIGINT_TYPE = null;
        }
    }

    public Math() {
    }

    public Math(boolean strict) {
        this.strict = strict;
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    public boolean isStrict() {
        return strict;
    }

    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return a.add(b);
    }

    public static BigInteger add(BigInteger a, BigInteger b) {
        return a.add(b);
    }

    public static BigInteger and(BigInteger a, BigInteger b) {
        return a.and(b);
    }

    public static int and(int a, int b) {
        return a & b;
    }

    public static BigInteger or(BigInteger a, BigInteger b) {
        return a.or(b);
    }

    public static int or(int a, int b) {
        return a | b;
    }

    public static BigInteger not(BigInteger a) {
        return a.not();
    }

    public static int not(int a) {
        return ~a;
    }

    public static BigInteger xor(BigInteger a, BigInteger b) {
        return a.xor(b);
    }

    public static int xor(int a, int b) {
        return a ^ b;
    }

    public static double add(double a, double b) {
        return a + b;
    }

    public static float add(float a, float b) {
        return a + b;
    }

    public static long add(long a, long b) {
        return a + b;
    }

    public static int add(int a, int b) {
        return a + b;
    }

    public static BigDecimal add(BigDecimal[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        BigDecimal b = a[0];
        for (int i = 1; i < a.length; i++) {
            b = b.add(a[i]);
        }
        return b;
    }

    public static BigInteger add(BigInteger[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        BigInteger b = a[0];
        for (int i = 1; i < a.length; i++) {
            b = b.add(a[i]);
        }
        return b;
    }

    public static double add(double[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        double b = a[0];
        for (int i = 1; i < a.length; i++) {
            b += a[i];
        }
        return b;
    }

    public static float add(float[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        float b = a[0];
        for (int i = 1; i < a.length; i++) {
            b += a[i];
        }
        return b;
    }

    public static long add(long[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        long b = a[0];
        for (int i = 1; i < a.length; i++) {
            b += a[i];
        }
        return b;
    }

    public static int add(int[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        int b = a[0];
        for (int i = 1; i < a.length; i++) {
            b += a[i];
        }
        return b;
    }

    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return a.subtract(b);
    }

    public static BigInteger subtract(BigInteger a, BigInteger b) {
        return a.subtract(b);
    }

    public static double subtract(double a, double b) {
        return a - b;
    }

    public static float subtract(float a, float b) {
        return a - b;
    }

    public static long subtract(long a, long b) {
        return a - b;
    }

    public static int subtract(int a, int b) {
        return a - b;
    }

    public static BigDecimal subtract(BigDecimal[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        BigDecimal b = a[0];
        for (int i = 1; i < a.length; i++) {
            b = b.subtract(a[i]);
        }
        return b;
    }

    public static BigInteger subtract(BigInteger[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        BigInteger b = a[0];
        for (int i = 1; i < a.length; i++) {
            b = b.subtract(a[i]);
        }
        return b;
    }

    public static double subtract(double[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        double b = a[0];
        for (int i = 1; i < a.length; i++) {
            b -= a[i];
        }
        return b;
    }

    public static float subtract(float[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        float b = a[0];
        for (int i = 1; i < a.length; i++) {
            b -= a[i];
        }
        return b;
    }

    public static long subtract(long[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        long b = a[0];
        for (int i = 1; i < a.length; i++) {
            b -= a[i];
        }
        return b;
    }

    public static int subtract(int[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        int b = a[0];
        for (int i = 1; i < a.length; i++) {
            b -= a[i];
        }
        return b;
    }

    public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
        return a.multiply(b);
    }

    public static BigInteger multiply(BigInteger a, BigInteger b) {
        return a.multiply(b);
    }

    public static double multiply(double a, double b) {
        return a * b;
    }

    public static float multiply(float a, float b) {
        return a * b;
    }

    public static long multiply(long a, long b) {
        return a * b;
    }

    public static int multiply(int a, int b) {
        return a * b;
    }

    public static BigDecimal multiply(BigDecimal[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        BigDecimal b = a[0];
        for (int i = 1; i < a.length; i++) {
            b = b.multiply(a[i]);
        }
        return b;
    }

    public static BigInteger multiply(BigInteger[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        BigInteger b = a[0];
        for (int i = 1; i < a.length; i++) {
            b = b.multiply(a[i]);
        }
        return b;
    }

    public static double multiply(double[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        double b = a[0];
        for (int i = 1; i < a.length; i++) {
            b *= a[i];
        }
        return b;
    }

    public static float multiply(float[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        float b = a[0];
        for (int i = 1; i < a.length; i++) {
            b *= a[i];
        }
        return b;
    }

    public static long multiply(long[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        long b = a[0];
        for (int i = 1; i < a.length; i++) {
            b *= a[i];
        }
        return b;
    }

    public static int multiply(int[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        int b = a[0];
        for (int i = 1; i < a.length; i++) {
            b *= a[i];
        }
        return b;
    }

    public static BigDecimal divide(BigDecimal a, BigDecimal b) {
        return a.divide(b, BigDecimal.ROUND_HALF_EVEN);
    }

    public static BigInteger divide(BigInteger a, BigInteger b) {
        return a.divide(b);
    }

    public static double divide(double a, double b) {
        return a / b;
    }

    public static float divide(float a, float b) {
        return a / b;
    }

    public static long divide(long a, long b) {
        return a / b;
    }

    public static int divide(int a, int b) {
        return a / b;
    }

    public static BigDecimal divide(BigDecimal[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        BigDecimal b = a[0];
        for (int i = 1; i < a.length; i++) {
            b = b.divide(a[i], BigDecimal.ROUND_HALF_EVEN);
        }
        return b;
    }

    public static BigInteger divide(BigInteger[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        BigInteger b = a[0];
        for (int i = 1; i < a.length; i++) {
            b = b.divide(a[i]);
        }
        return b;
    }

    public static double divide(double[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        double b = a[0];
        for (int i = 1; i < a.length; i++) {
            b /= a[i];
        }
        return b;
    }

    public static float divide(float[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        float b = a[0];
        for (int i = 1; i < a.length; i++) {
            b /= a[i];
        }
        return b;
    }

    public static long divide(long[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        long b = a[0];
        for (int i = 1; i < a.length; i++) {
            b /= a[i];
        }
        return b;
    }

    public static int divide(int[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        int b = a[0];
        for (int i = 1; i < a.length; i++) {
            b /= a[i];
        }
        return b;
    }

    public static BigInteger mod(BigInteger a, BigInteger b) {
        return a.mod(b);
    }

    public static double mod(double a, double b) {
        return a % b;
    }

    public static float mod(float a, float b) {
        return a % b;
    }

    public static long mod(long a, long b) {
        return a % b;
    }

    public static int mod(int a, int b) {
        return a % b;
    }

    public static BigInteger mod(BigInteger[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        BigInteger b = a[0];
        for (int i = 1; i < a.length; i++) {
            b = b.mod(a[i]);
        }
        return b;
    }

    public static double mod(double[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        double b = a[0];
        for (int i = 1; i < a.length; i++) {
            b %= a[i];
        }
        return b;
    }

    public static float mod(float[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        float b = a[0];
        for (int i = 1; i < a.length; i++) {
            b %= a[i];
        }
        return b;
    }

    public static long mod(long[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        long b = a[0];
        for (int i = 1; i < a.length; i++) {
            b %= a[i];
        }
        return b;
    }

    public static int mod(int[] a) {
        if (a.length == 0) {
            throw new IllegalArgumentException();
        }
        if (a.length == 1) {
            return a[0];
        }
        int b = a[0];
        for (int i = 1; i < a.length; i++) {
            b %= a[i];
        }
        return b;
    }

    public static BigInteger factorial(BigInteger x) {
        if (x.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("number must be greater than 0");
        }
        BigInteger y = x;
        for (x = x.subtract(BigInteger.ONE); x.toString().compareTo("1") > 0; x = x.subtract(BigInteger.ONE)) {
            y = y.multiply(x);
        }
        return y;
    }

    public static int factorial(double x) {
        return factorial((int) x);
    }

    public static int factorial(float x) {
        return factorial((int) x);
    }

    public static int factorial(int x) {
        if (x < 0) {
            throw new IllegalArgumentException("number must be greater than 0");
        }
        int y = x;
        for (x -= 1; x > 1; x--) {
            y *= x;
        }
        return y;
    }

    public static BigDecimal min(BigDecimal a, BigDecimal b) {
        return a.min(b);
    }

    public static BigInteger min(BigInteger a, BigInteger b) {
        return a.min(b);
    }

    public static BigDecimal max(BigDecimal a, BigDecimal b) {
        return a.max(b);
    }

    public static BigInteger max(BigInteger a, BigInteger b) {
        return a.max(b);
    }

    /**
     * y raised to the x power
     */
    public static BigInteger pow(BigInteger y, BigInteger x) {
        int exp = x.intValue();
        if (exp < 1) {
            throw new IllegalArgumentException("Exponent must be greater than 0");
        }
        return y.pow(x.intValue());
    }

    /**
     * y raised to the x power
     */
    public static BigDecimal pow(BigDecimal y, BigDecimal x) {
        if (x.compareTo(BigDecimal.ONE) <= 0) {
            throw new ArithmeticException("Powers of BigDecimals must be integers greater than 1");
        }
        String exp = x.toString();
        if (exp.indexOf('.') > 0) {
            exp = exp.substring(0, exp.indexOf('.'));
        }
        BigInteger e = new BigInteger(exp);
        BigDecimal z = new BigDecimal(y.toString());
        for (; e.compareTo(BigInteger.ONE) > 0; e = e.subtract(BigInteger.ONE)) {
            y = y.multiply(z);
        }
        return y;
    }

    /**
     * Do a mathematical calculation. The allowed operations are all
     * operations supported by java.lang.Math and this class. Assumes data
     * type is "double".
     * @param op the name of a mathematical operation to perform
     * @param the operands for the operation, these strings must parse to numbers.
     */
    public Number calculate(String op, String[] operands) {
        return calculate(op, "double", operands);
    }

    /**
     * Do a mathematical calculation. The allowed operations are all
     * operations supported by java.lang.Math.
     * @param op the name of a mathematical operation to perform
     * @param type the data type of the operands
     * @param the operands for the operation
     */
    public Number calculate(String op, String type, String[] operands) {
        try {
            if (operands.length >= 2 && (op.equals("add") || op.equals("subtract") || op.equals("multiply") || op.equals("divide") || op.equals("mod"))) {
                return calculateArray(op, type, operands);
            }
            if (operands.length > 2) {
                throw new IllegalArgumentException("too many operands");
            }
            Class c;
            Method m;
            if (strict) {
                c = Class.forName("java.lang.StrictMath");
            } else {
                c = Class.forName("java.lang.Math");
            }
            if (op.equals("random")) {
                m = c.getDeclaredMethod(op, new Class[] {});
                Object result = m.invoke(c, null);
                return (Number) result;
            }
            ArrayList<Candidate> candidates = new ArrayList<Candidate>();
            Method[] methods = c.getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                String name = methods[i].getName();
                if (name.equals(op)) {
                    candidates.add(new Candidate(c, methods[i]));
                }
            }
            c = this.getClass();
            methods = c.getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                String name = methods[i].getName();
                if (name.equals(op)) {
                    candidates.add(new Candidate(c, methods[i]));
                }
            }
            if (candidates.size() == 0) {
                throw new ArithmeticException("Unknown operation: " + op);
            }
            Class wantTypeClass = getDataType(type);
            int paramCount = -1;
            try {
                for (Candidate candidate : candidates) {
                    Method method = candidate.getCandidateMethod();
                    paramCount = method.getParameterTypes().length;
                    if (paramCount == operands.length) {
                        break;
                    }
                }
            } catch (Exception e) {
                throw new ArithmeticException("Wrong number of arguments, have " + operands.length + ", but can't find corresponding method.");
            }
            Class typeClass = null;
            if (candidates.size() == 1) {
                Candidate candidate = (Candidate) candidates.get(0);
                c = candidate.getCandidateClass();
                m = candidate.getCandidateMethod();
                typeClass = m.getParameterTypes()[0];
            } else {
                for (Candidate candidate : candidates) {
                    c = candidate.getCandidateClass();
                    m = candidate.getCandidateMethod();
                    if (m.getParameterTypes()[0].equals(wantTypeClass)) {
                        typeClass = wantTypeClass;
                        break;
                    }
                }
                if (typeClass == null) {
                    throw new ArithmeticException("Can't find a method with parameters of type " + type);
                }
            }
            Class[] paramTypes = new Class[paramCount];
            for (int i = 0; i < paramCount; i++) {
                paramTypes[i] = typeClass;
            }
            m = c.getDeclaredMethod(op, paramTypes);
            System.out.println("Math.calculate, invoking: " + m.toString());
            Object[] params = getParams(typeClass, operands);
            try {
                System.out.println("Math.calculateArray, invoking: " + m.toString());
                Object result = m.invoke(c, params);
                return (Number) result;
            } catch (InvocationTargetException ite) {
                Throwable t = ite.getCause();
                if (t != null && t instanceof ArithmeticException) {
                    throw (ArithmeticException) t;
                } else {
                    throw ite;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
        }
        return null;
    }

    /**
     * Performs a calculation on an array of numbers. The mathematical methods
     * in this class will accept an array of numbers, so
     * <code>add(new int[]{1, 2, 3})</code>
     * is equivalent to
     * <code>add(add(1, 2), 3)</code>
     * which is equivalent to 1 + 2 + 3.
     * @param op the operation to perform
     * @type the data type of the operands. All operands will be cast to the same
     * data type
     * @param operands these strings must parse to numbers.
     */
    private Number calculateArray(String op, String type, String[] operands) {
        try {
            Class c = this.getClass();
            ArrayList<Method> candidates = new ArrayList<Method>();
            Method[] methods = c.getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                String name = methods[i].getName();
                if (name.equals(op)) {
                    if (methods[i].getParameterTypes().length == 1) {
                        if (methods[i].getParameterTypes()[0].isArray()) {
                            candidates.add(methods[i]);
                        }
                    }
                }
            }
            if (candidates.size() == 0) {
                throw new ArithmeticException("Unknown operation: " + op);
            }
            Object wantTypeClass = getDataTypeArray(type, operands.length);
            Class typeClass = null;
            for (Method m : candidates) {
                if (m.getParameterTypes()[0].equals(wantTypeClass.getClass())) {
                    typeClass = getDataType(type);
                    Object[] params = getParamsArray(typeClass, operands);
                    try {
                        Object result = m.invoke(c, params);
                        return (Number) result;
                    } catch (InvocationTargetException ite) {
                        Throwable t = ite.getCause();
                        if (t != null && t instanceof ArithmeticException) {
                            throw (ArithmeticException) t;
                        } else {
                            throw ite;
                        }
                    }
                }
            }
        } catch (Exception e) {
            if (e instanceof ArithmeticException) {
                throw (ArithmeticException) e;
            }
        }
        return null;
    }

    /**
     * Converts a string representing a data type into the actual type.
     * @param type one of "int", "long", "float", or "double"
     * @return one of Integer.TYPE, Long.TYPE, Float.TYPE, or Double.TYPE. If the
     * given type is null or not one of the allowed types, Double.TYPE will be
     * returned.
     */
    private Class getDataType(String type) {
        if (type == null) {
            return Double.TYPE;
        }
        if (type.equals("int")) {
            return Integer.TYPE;
        } else if (type.equals("long")) {
            return Long.TYPE;
        } else if (type.equals("float")) {
            return Float.TYPE;
        } else if (type.equals("bigint")) {
            try {
                return Class.forName("java.math.BigInteger");
            } catch (Exception e) {
            }
        } else if (type.equals("bigdecimal")) {
            try {
                return Class.forName("java.math.BigDecimal");
            } catch (Exception e) {
            }
        }
        return Double.TYPE;
    }

    /**
     * Converts a string representing a data type into an Array.
     * @param type one of "int", "long", "float", or "double"
     * @param length how long to make the array
     * @return an Array representing the data type
     */
    private Object getDataTypeArray(String type, int length) {
        if (type == null) {
            return Array.newInstance(Double.TYPE, length);
        }
        if (type.equals("int")) {
            return Array.newInstance(Integer.TYPE, length);
        } else if (type.equals("long")) {
            return Array.newInstance(Long.TYPE, length);
        } else if (type.equals("float")) {
            return Array.newInstance(Float.TYPE, length);
        } else if (type.equals("bigdecimal")) {
            return Array.newInstance(BIGDECIMAL_TYPE, length);
        } else if (type.equals("bigint")) {
            return Array.newInstance(BIGINT_TYPE, length);
        } else {
            return Array.newInstance(Double.TYPE, length);
        }
    }

    /**
     * @returns the given operands as an array of the given type.
     */
    private Object[] getParams(Class typeClass, String[] operands) {
        int paramCount = operands.length;
        Object[] params = new Object[paramCount];
        if (typeClass == BIGDECIMAL_TYPE) {
            for (int i = 0; i < paramCount; i++) {
                params[i] = new BigDecimal(operands[i]);
            }
        } else if (typeClass == BIGINT_TYPE) {
            for (int i = 0; i < paramCount; i++) {
                params[i] = new BigInteger(operands[i]);
            }
        } else if (typeClass == Double.TYPE) {
            for (int i = 0; i < paramCount; i++) {
                params[i] = new Double(operands[i]);
            }
        } else if (typeClass == Long.TYPE) {
            for (int i = 0; i < paramCount; i++) {
                params[i] = new Long(operands[i]);
            }
        } else if (typeClass == Float.TYPE) {
            for (int i = 0; i < paramCount; i++) {
                params[i] = new Float(operands[i]);
            }
        } else {
            for (int i = 0; i < paramCount; i++) {
                params[i] = new Integer(operands[i]);
            }
        }
        if (paramCount > 2) {
            params = new Object[] { params };
        }
        return params;
    }

    /**
     * Converts the given operands into an array of the given type.
     */
    private Object[] getParamsArray(Class typeClass, String[] operands) {
        int paramCount = operands.length;
        if (typeClass == BIGDECIMAL_TYPE) {
            BigDecimal[] array = (BigDecimal[]) Array.newInstance(typeClass, operands.length);
            for (int i = 0; i < paramCount; i++) {
                array[i] = new BigDecimal(operands[i]);
            }
            return new Object[] { array };
        } else if (typeClass == BIGINT_TYPE) {
            BigInteger[] array = (BigInteger[]) Array.newInstance(typeClass, operands.length);
            for (int i = 0; i < paramCount; i++) {
                array[i] = new BigInteger(operands[i]);
            }
            return new Object[] { array };
        } else if (typeClass == Double.TYPE) {
            double[] array = (double[]) Array.newInstance(typeClass, operands.length);
            for (int i = 0; i < paramCount; i++) {
                Array.setDouble(array, i, new Double(operands[i]).doubleValue());
            }
            return new Object[] { array };
        } else if (typeClass == Long.TYPE) {
            long[] array = (long[]) Array.newInstance(typeClass, operands.length);
            for (int i = 0; i < paramCount; i++) {
                Array.setLong(array, i, new Long(operands[i]).longValue());
            }
            return new Object[] { array };
        } else if (typeClass == Float.TYPE) {
            float[] array = (float[]) Array.newInstance(typeClass, operands.length);
            for (int i = 0; i < paramCount; i++) {
                Array.setFloat(array, i, new Float(operands[i]).floatValue());
            }
            return new Object[] { array };
        } else {
            Object array = Array.newInstance(typeClass, operands.length);
            for (int i = 0; i < paramCount; i++) {
                Array.setInt(array, i, new Integer(operands[i]).intValue());
            }
            return new Object[] { array };
        }
    }

    public class Candidate {

        private Class c;

        private Method m;

        public Candidate(Class c, Method m) {
            this.c = c;
            this.m = m;
        }

        public Class getCandidateClass() {
            return c;
        }

        public Method getCandidateMethod() {
            return m;
        }
    }

    public static void main(String[] args) {
        Math math = new Math();
        System.out.println(math.calculate("add", new String[] { "6", "5", "4" }));
    }
}
