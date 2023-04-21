package com.peterhi.runtime;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import com.peterhi.runtime.Util;
import com.peterhi.runtime.Util.Struct;

public class TestStructuredObject {

    public static void main(String[] args) {
        TestObject user = createUser();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Util.writeStruct(baos, user);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        System.out.println(baos.size());
        Struct so = Util.readStruct(bais);
        System.out.println(so);
        System.out.println(bais.available());
    }

    private static TestObject createUser() {
        TestObject user = Util.New(TestObject.class);
        user.setBoolean(true);
        user.setByte(Byte.MAX_VALUE);
        user.setChar(Character.MAX_VALUE);
        user.setShort(Short.MAX_VALUE);
        user.setInt(Integer.MAX_VALUE);
        user.setFloat(Float.MAX_VALUE);
        user.setLong(Long.MAX_VALUE);
        user.setDouble(Double.MAX_VALUE);
        user.setString("HAHA!!!");
        user.setTestObject(user);
        user.setObject("OBJECT!!!");
        user.setBooleanArray(new boolean[] { true, false });
        user.setByteArray(new byte[] { Byte.MIN_VALUE, Byte.MAX_VALUE });
        user.setCharArray(new char[] { Character.MIN_VALUE, Character.MAX_VALUE });
        user.setShortArray(new short[] { Short.MIN_VALUE, Short.MAX_VALUE });
        user.setIntArray(new int[] { Integer.MIN_VALUE, Integer.MAX_VALUE });
        user.setFloatArray(new float[] { Float.MIN_VALUE, Float.MAX_VALUE });
        user.setLongArray(new long[] { Long.MIN_VALUE, Long.MAX_VALUE });
        user.setDoubleArray(new double[] { Double.MIN_VALUE, Double.MAX_VALUE });
        user.setStringArray(new String[] { "HAHA!!!", "HOHO!!!" });
        user.setTestObjectArray(new TestObject[] { user });
        user.setObjectArray(new Object[] { true, Byte.MAX_VALUE, Character.MAX_VALUE, Short.MAX_VALUE, Integer.MAX_VALUE, Float.MAX_VALUE, Long.MAX_VALUE, Double.MAX_VALUE, "HAHA!!!", "OBJECT!!!" });
        user.setBooleanArrays(new boolean[][] { new boolean[] { true, false }, new boolean[] { false, true } });
        user.setByteArrays(new byte[][] { new byte[] { Byte.MIN_VALUE, Byte.MAX_VALUE }, new byte[] { Byte.MAX_VALUE, Byte.MIN_VALUE } });
        user.setCharArrays(new char[][] { new char[] { Character.MIN_VALUE, Character.MAX_VALUE }, new char[] { Character.MAX_VALUE, Character.MIN_VALUE } });
        user.setShortArrays(new short[][] { new short[] { Short.MIN_VALUE, Short.MAX_VALUE }, new short[] { Short.MAX_VALUE, Short.MIN_VALUE } });
        user.setIntArrays(new int[][] { new int[] { Integer.MIN_VALUE, Integer.MAX_VALUE }, new int[] { Integer.MAX_VALUE, Integer.MIN_VALUE } });
        user.setFloatArrays(new float[][] { new float[] { Float.MIN_VALUE, Float.MAX_VALUE }, new float[] { Float.MAX_VALUE, Float.MIN_VALUE } });
        user.setLongArrays(new long[][] { new long[] { Long.MIN_VALUE, Long.MAX_VALUE }, new long[] { Long.MAX_VALUE, Long.MIN_VALUE } });
        user.setDoubleArrays(new double[][] { new double[] { Double.MIN_VALUE, Double.MAX_VALUE }, new double[] { Double.MAX_VALUE, Double.MIN_VALUE } });
        user.setStringArrays(new String[][] { new String[] { "HAHA!!!", "HOHO!!!" }, new String[] { "HOHO!!!", "HAHA!!!" } });
        user.setTestObjectArrays(new TestObject[][] { new TestObject[] { user }, new TestObject[] { user }, new TestObject[] { user } });
        user.setObjectArrays(new Object[][] { new Object[] { true, Byte.MAX_VALUE, Character.MAX_VALUE }, new Object[] { Short.MAX_VALUE, Integer.MAX_VALUE, Long.MAX_VALUE }, new Object[] { Float.MAX_VALUE, Double.MAX_VALUE }, new Object[] { "HAHA!!!", "OBJECT!!!" } });
        return user;
    }
}
