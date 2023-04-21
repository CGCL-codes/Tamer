package net.sf.ezmorph.test;

import junit.framework.Assert;

/**
 * Provides assertions on arrays (primitive and objects).<br>
 * All methods support multiple dimensional arrays.
 *
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class ArrayAssertions extends Assert {

    /**
    * Asserts that two boolean[] are equal.<br>
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(boolean[] expecteds, boolean[] actuals) {
        assertEquals(null, expecteds, actuals);
    }

    /**
    * Asserts that a boolean[] is equal to and Object[] (presumably an
    * Boolean[])
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(boolean[] expecteds, Object[] actuals) {
        assertEquals(null, expecteds, actuals);
    }

    /**
    * Asserts that two byte[] are equal.<br>
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(byte[] expecteds, byte[] actuals) {
        assertEquals(null, expecteds, actuals);
    }

    /**
    * Asserts that a byte[] is equal to and Object[] (presumably an Byte[])
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(byte[] expecteds, Object[] actuals) {
        assertEquals(null, expecteds, actuals);
    }

    /**
    * Asserts that two char[] are equal.<br>
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(char[] expecteds, char[] actuals) {
        assertEquals(null, expecteds, actuals);
    }

    /**
    * Asserts that a char[] is equal to and Object[] (presumably an Character[])
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(char[] expecteds, Object[] actuals) {
        assertEquals(null, expecteds, actuals);
    }

    /**
    * Asserts that two double[] are equal.<br>
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(double[] expecteds, double[] actuals) {
        assertEquals(null, expecteds, actuals);
    }

    /**
    * Asserts that a double[] is equal to and Object[] (presumably an Double[])
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(double[] expecteds, Object[] actuals) {
        assertEquals(null, expecteds, actuals);
    }

    /**
    * Asserts that two float[] are equal.<br>
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(float[] expecteds, float[] actuals) {
        assertEquals(null, expecteds, actuals);
    }

    /**
    * Asserts that a float[] is equal to and Object[] (presumably an Float[])
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(float[] expecteds, Object[] actuals) {
        assertEquals(null, expecteds, actuals);
    }

    /**
    * Asserts that two int[] are equal.<br>
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(int[] expecteds, int[] actuals) {
        assertEquals(null, expecteds, actuals);
    }

    /**
    * Asserts that a int[] is equal to and Object[] (presumably an Integer[])
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(int[] expecteds, Object[] actuals) {
        assertEquals(null, expecteds, actuals);
    }

    /**
    * Asserts that two long[] are equal.<br>
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(long[] expecteds, long[] actuals) {
        assertEquals(null, expecteds, actuals);
    }

    /**
    * Asserts that a long[] is equal to and Object[] (presumably an Long[])
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(long[] expecteds, Object[] actuals) {
        assertEquals(null, expecteds, actuals);
    }

    /**
    * Asserts that Object[] (presumably an Boolean[]) is equal to a boolean[].
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(Object[] expecteds, boolean[] actuals) {
        assertEquals(null, expecteds, actuals);
    }

    /**
    * Asserts that Object[] (presumably an Byte[]) is equal to a byte[].
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(Object[] expecteds, byte[] actuals) {
        assertEquals(null, expecteds, actuals);
    }

    /**
    * Asserts that Object[] (presumably an Character[]) is equal to a char[].
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(Object[] expecteds, char[] actuals) {
        assertEquals(null, expecteds, actuals);
    }

    /**
    * Asserts that Object[] (presumably an Double[]) is equal to a double[].
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(Object[] expecteds, double[] actuals) {
        assertEquals(null, expecteds, actuals);
    }

    /**
    * Asserts that Object[] (presumably an Float[]) is equal to a float[].
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(Object[] expecteds, float[] actuals) {
        assertEquals(null, expecteds, actuals);
    }

    /**
    * Asserts that Object[] (presumably an Integer[]) is equal to a int[].
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(Object[] expecteds, int[] actuals) {
        assertEquals(null, expecteds, actuals);
    }

    /**
    * Asserts that Object[] (presumably an Long[]) is equal to a long[].
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(Object[] expecteds, long[] actuals) {
        assertEquals(null, expecteds, actuals);
    }

    /**
    * Asserts that two Object[] are equal.<br>
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(Object[] expecteds, Object[] actuals) {
        assertEquals(null, expecteds, actuals);
    }

    /**
    * Asserts that Object[] (presumably an Short[]) is equal to a short[].
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(Object[] expecteds, short[] actuals) {
        assertEquals(null, expecteds, actuals);
    }

    /**
    * Asserts that a short[] is equal to and Object[] (presumably an Short[])
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(short[] expecteds, Object[] actuals) {
        assertEquals(null, expecteds, actuals);
    }

    /**
    * Asserts that two short[] are equal.<br>
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(short[] expecteds, short[] actuals) {
        assertEquals(null, expecteds, actuals);
    }

    /**
    * Asserts that two boolean[] are equal.<br>
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(String message, boolean[] expecteds, boolean[] actuals) {
        if (expecteds == actuals) {
            return;
        }
        String header = message == null ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected array was null");
        }
        if (actuals == null) {
            fail(header + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            fail(header + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals(header + "arrays first differed at element [" + i + "];", expecteds[i], actuals[i]);
        }
    }

    /**
    * Asserts that a boolean[] is equal to and Object[] (presumably an
    * Boolean[])
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(String message, boolean[] expecteds, Object[] actuals) {
        String header = message == null ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected array was null");
        }
        if (actuals == null) {
            fail(header + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            fail(header + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals(header + "arrays first differed at element [" + i + "];", new Boolean(expecteds[i]), actuals[i]);
        }
    }

    /**
    * Asserts that two byte[] are equal.<br>
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(String message, byte[] expecteds, byte[] actuals) {
        if (expecteds == actuals) {
            return;
        }
        String header = message == null ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected array was null");
        }
        if (actuals == null) {
            fail(header + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            fail(header + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals(header + "arrays first differed at element [" + i + "];", expecteds[i], actuals[i]);
        }
    }

    /**
    * Asserts that a byte[] is equal to and Object[] (presumably an Byte[])
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(String message, byte[] expecteds, Object[] actuals) {
        String header = message == null ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected array was null");
        }
        if (actuals == null) {
            fail(header + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            fail(header + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals(header + "arrays first differed at element [" + i + "];", new Byte(expecteds[i]), actuals[i]);
        }
    }

    /**
    * Asserts that two char[] are equal.<br>
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(String message, char[] expecteds, char[] actuals) {
        if (expecteds == actuals) {
            return;
        }
        String header = message == null ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected array was null");
        }
        if (actuals == null) {
            fail(header + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            fail(header + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals(header + "arrays first differed at element [" + i + "];", expecteds[i], actuals[i]);
        }
    }

    /**
    * Asserts that a char[] is equal to and Object[] (presumably an Character[])
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(String message, char[] expecteds, Object[] actuals) {
        String header = message == null ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected array was null");
        }
        if (actuals == null) {
            fail(header + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            fail(header + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals(header + "arrays first differed at element [" + i + "];", new Character(expecteds[i]), actuals[i]);
        }
    }

    /**
    * Asserts that two double[] are equal.<br>
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(String message, double[] expecteds, double[] actuals) {
        if (expecteds == actuals) {
            return;
        }
        String header = message == null ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected array was null");
        }
        if (actuals == null) {
            fail(header + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            fail(header + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals(header + "arrays first differed at element [" + i + "];", expecteds[i], actuals[i], 0d);
        }
    }

    /**
    * Asserts that a double[] is equal to and Object[] (presumably an Double[])
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(String message, double[] expecteds, Object[] actuals) {
        String header = message == null ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected array was null");
        }
        if (actuals == null) {
            fail(header + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            fail(header + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals(header + "arrays first differed at element [" + i + "];", new Double(expecteds[i]), actuals[i]);
        }
    }

    /**
    * Asserts that two float[] are equal.<br>
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(String message, float[] expecteds, float[] actuals) {
        if (expecteds == actuals) {
            return;
        }
        String header = message == null ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected array was null");
        }
        if (actuals == null) {
            fail(header + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            fail(header + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals(header + "arrays first differed at element [" + i + "];", expecteds[i], actuals[i], 0f);
        }
    }

    /**
    * Asserts that a float[] is equal to and Object[] (presumably an Float[])
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(String message, float[] expecteds, Object[] actuals) {
        String header = message == null ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected array was null");
        }
        if (actuals == null) {
            fail(header + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            fail(header + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals(header + "arrays first differed at element [" + i + "];", new Float(expecteds[i]), actuals[i]);
        }
    }

    /**
    * Asserts that two int[] are equal.<br>
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(String message, int[] expecteds, int[] actuals) {
        if (expecteds == actuals) {
            return;
        }
        String header = message == null ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected array was null");
        }
        if (actuals == null) {
            fail(header + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            fail(header + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals(header + "arrays first differed at element [" + i + "];", expecteds[i], actuals[i]);
        }
    }

    /**
    * Asserts that a int[] is equal to and Object[] (presumably an Integer[])
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(String message, int[] expecteds, Object[] actuals) {
        String header = message == null ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected array was null");
        }
        if (actuals == null) {
            fail(header + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            fail(header + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals(header + "arrays first differed at element [" + i + "];", new Integer(expecteds[i]), actuals[i]);
        }
    }

    /**
    * Asserts that two long[] are equal.<br>
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(String message, long[] expecteds, long[] actuals) {
        if (expecteds == actuals) {
            return;
        }
        String header = message == null ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected array was null");
        }
        if (actuals == null) {
            fail(header + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            fail(header + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals(header + "arrays first differed at element [" + i + "];", expecteds[i], actuals[i]);
        }
    }

    /**
    * Asserts that a long[] is equal to and Object[] (presumably an Long[])
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(String message, long[] expecteds, Object[] actuals) {
        String header = message == null ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected array was null");
        }
        if (actuals == null) {
            fail(header + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            fail(header + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals(header + "arrays first differed at element [" + i + "];", new Long(expecteds[i]), actuals[i]);
        }
    }

    /**
    * Asserts that Object[] (presumably an Boolean[]) is equal to a boolean[].
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(String message, Object[] expecteds, boolean[] actuals) {
        String header = message == null ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected array was null");
        }
        if (actuals == null) {
            fail(header + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            fail(header + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals(header + "arrays first differed at element [" + i + "];", expecteds[i], new Boolean(actuals[i]));
        }
    }

    /**
    * Asserts that Object[] (presumably an Byte[]) is equal to a byte[].
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(String message, Object[] expecteds, byte[] actuals) {
        String header = message == null ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected array was null");
        }
        if (actuals == null) {
            fail(header + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            fail(header + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals(header + "arrays first differed at element [" + i + "];", expecteds[i], new Byte(actuals[i]));
        }
    }

    /**
    * Asserts that Object[] (presumably an Character[]) is equal to a char[].
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(String message, Object[] expecteds, char[] actuals) {
        String header = message == null ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected array was null");
        }
        if (actuals == null) {
            fail(header + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            fail(header + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals(header + "arrays first differed at element [" + i + "];", expecteds[i], new Character(actuals[i]));
        }
    }

    /**
    * Asserts that Object[] (presumably an Double[]) is equal to a double[].
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(String message, Object[] expecteds, double[] actuals) {
        String header = message == null ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected array was null");
        }
        if (actuals == null) {
            fail(header + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            fail(header + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals(header + "arrays first differed at element [" + i + "];", expecteds[i], new Double(actuals[i]));
        }
    }

    /**
    * Asserts that Object[] (presumably an Float[]) is equal to a float[].
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(String message, Object[] expecteds, float[] actuals) {
        String header = message == null ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected array was null");
        }
        if (actuals == null) {
            fail(header + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            fail(header + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals(header + "arrays first differed at element [" + i + "];", expecteds[i], new Float(actuals[i]));
        }
    }

    /**
    * Asserts that Object[] (presumably an Integer[]) is equal to a int[].
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(String message, Object[] expecteds, int[] actuals) {
        String header = message == null ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected array was null");
        }
        if (actuals == null) {
            fail(header + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            fail(header + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals(header + "arrays first differed at element [" + i + "];", expecteds[i], new Integer(actuals[i]));
        }
    }

    /**
    * Asserts that Object[] (presumably an Long[]) is equal to a long[].
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(String message, Object[] expecteds, long[] actuals) {
        String header = message == null ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected array was null");
        }
        if (actuals == null) {
            fail(header + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            fail(header + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals(header + "arrays first differed at element [" + i + "];", expecteds[i], new Long(actuals[i]));
        }
    }

    /**
    * Asserts that two Object[] are equal.<br>
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(String message, Object[] expecteds, Object[] actuals) {
        if (expecteds == actuals) {
            return;
        }
        String header = message == null ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected array was null");
        }
        if (actuals == null) {
            fail(header + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            fail(header + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; i++) {
            Object o1 = expecteds[i];
            Object o2 = actuals[i];
            if (o1 == null) {
                if (o2 == null) {
                    return;
                } else {
                    fail(header + "arrays first differed at element [" + i + "];");
                }
            } else {
                if (o2 == null) {
                    fail(header + "arrays first differed at element [" + i + "];");
                }
            }
            if (o1.getClass().isArray() && o2.getClass().isArray()) {
                Class type1 = o1.getClass().getComponentType();
                Class type2 = o2.getClass().getComponentType();
                if (type1.isPrimitive()) {
                    if (type1 == Boolean.TYPE) {
                        if (type2 == Boolean.TYPE) {
                            assertEquals(header + "arrays first differed at element " + i + ";", (boolean[]) o1, (boolean[]) o2);
                        } else {
                            assertEquals(header + "arrays first differed at element " + i + ";", (boolean[]) o1, (Object[]) o2);
                        }
                    } else if (type1 == Byte.TYPE) {
                        if (type2 == Byte.TYPE) {
                            assertEquals(header + "arrays first differed at element " + i + ";", (byte[]) o1, (byte[]) o2);
                        } else {
                            assertEquals(header + "arrays first differed at element " + i + ";", (byte[]) o1, (Object[]) o2);
                        }
                    } else if (type1 == Short.TYPE) {
                        if (type2 == Short.TYPE) {
                            assertEquals(header + "arrays first differed at element " + i + ";", (short[]) o1, (short[]) o2);
                        } else {
                            assertEquals(header + "arrays first differed at element " + i + ";", (short[]) o1, (Object[]) o2);
                        }
                    } else if (type1 == Integer.TYPE) {
                        if (type2 == Integer.TYPE) {
                            assertEquals(header + "arrays first differed at element " + i + ";", (int[]) o1, (int[]) o2);
                        } else {
                            assertEquals(header + "arrays first differed at element " + i + ";", (int[]) o1, (Object[]) o2);
                        }
                    } else if (type1 == Long.TYPE) {
                        if (type2 == Long.TYPE) {
                            assertEquals(header + "arrays first differed at element " + i + ";", (long[]) o1, (long[]) o2);
                        } else {
                            assertEquals(header + "arrays first differed at element " + i + ";", (long[]) o1, (Object[]) o2);
                        }
                    } else if (type1 == Float.TYPE) {
                        if (type2 == Float.TYPE) {
                            assertEquals(header + "arrays first differed at element " + i + ";", (float[]) o1, (float[]) o2);
                        } else {
                            assertEquals(header + "arrays first differed at element " + i + ";", (float[]) o1, (Object[]) o2);
                        }
                    } else if (type1 == Double.TYPE) {
                        if (type2 == Double.TYPE) {
                            assertEquals(header + "arrays first differed at element " + i + ";", (double[]) o1, (double[]) o2);
                        } else {
                            assertEquals(header + "arrays first differed at element " + i + ";", (double[]) o1, (Object[]) o2);
                        }
                    } else if (type1 == Character.TYPE) {
                        if (type2 == Character.TYPE) {
                            assertEquals(header + "arrays first differed at element " + i + ";", (char[]) o1, (char[]) o2);
                        } else {
                            assertEquals(header + "arrays first differed at element " + i + ";", (char[]) o1, (Object[]) o2);
                        }
                    }
                } else if (type2.isPrimitive()) {
                    if (type2 == Boolean.TYPE) {
                        assertEquals(header + "arrays first differed at element " + i + ";", (Object[]) o1, (boolean[]) o2);
                    } else if (type2 == Byte.TYPE) {
                        assertEquals(header + "arrays first differed at element " + i + ";", (Object[]) o1, (byte[]) o2);
                    } else if (type2 == Short.TYPE) {
                        assertEquals(header + "arrays first differed at element " + i + ";", (Object[]) o1, (short[]) o2);
                    } else if (type2 == Integer.TYPE) {
                        assertEquals(header + "arrays first differed at element " + i + ";", (Object[]) o1, (int[]) o2);
                    } else if (type2 == Long.TYPE) {
                        assertEquals(header + "arrays first differed at element " + i + ";", (Object[]) o1, (long[]) o2);
                    } else if (type2 == Float.TYPE) {
                        assertEquals(header + "arrays first differed at element " + i + ";", (Object[]) o1, (float[]) o2);
                    } else if (type2 == Double.TYPE) {
                        assertEquals(header + "arrays first differed at element " + i + ";", (Object[]) o1, (double[]) o2);
                    } else if (type2 == Character.TYPE) {
                        assertEquals(header + "arrays first differed at element " + i + ";", (Object[]) o1, (char[]) o2);
                    }
                } else {
                    Object[] expected = (Object[]) o1;
                    Object[] actual = (Object[]) o2;
                    assertEquals(header + "arrays first differed at element " + i + ";", expected, actual);
                }
            } else {
                assertEquals(header + "arrays first differed at element [" + i + "];", o1, o2);
            }
        }
    }

    /**
    * Asserts that Object[] (presumably an Short[]) is equal to a short[].
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(String message, Object[] expecteds, short[] actuals) {
        String header = message == null ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected array was null");
        }
        if (actuals == null) {
            fail(header + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            fail(header + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals(header + "arrays first differed at element [" + i + "];", expecteds[i], new Short(actuals[i]));
        }
    }

    /**
    * Asserts that a short[] is equal to and Object[] (presumably an Short[])
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(String message, short[] expecteds, Object[] actuals) {
        String header = message == null ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected array was null");
        }
        if (actuals == null) {
            fail(header + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            fail(header + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals(header + "arrays first differed at element [" + i + "];", new Short(expecteds[i]), actuals[i]);
        }
    }

    /**
    * Asserts that two short[] are equal.<br>
    *
    * @param expecteds
    * @param actuals
    */
    public static void assertEquals(String message, short[] expecteds, short[] actuals) {
        if (expecteds == actuals) {
            return;
        }
        String header = message == null ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected array was null");
        }
        if (actuals == null) {
            fail(header + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            fail(header + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals(header + "arrays first differed at element [" + i + "];", expecteds[i], actuals[i]);
        }
    }

    private ArrayAssertions() {
    }
}
