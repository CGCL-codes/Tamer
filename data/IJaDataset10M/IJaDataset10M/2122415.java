package pl.wcislo.sbql4j.java.utils;

import java.util.ArrayList;
import java.util.List;

public class ArrayUtils {

    public static <T> List<T> toList(T[] array) {
        if (array == null) {
            return new ArrayList<T>();
        }
        List<T> res = new ArrayList<T>(array.length);
        for (T el : array) {
            res.add(el);
        }
        return res;
    }

    public static List<Integer> toList(int[] array) {
        if (array == null) {
            return new ArrayList<Integer>();
        }
        List<Integer> res = new ArrayList<Integer>(array.length);
        for (int el : array) {
            res.add(el);
        }
        return res;
    }

    public static List<Byte> toList(byte[] array) {
        if (array == null) {
            return new ArrayList<Byte>();
        }
        List<Byte> res = new ArrayList<Byte>(array.length);
        for (byte el : array) {
            res.add(el);
        }
        return res;
    }

    public static List<Short> toList(short[] array) {
        if (array == null) {
            return new ArrayList<Short>();
        }
        List<Short> res = new ArrayList<Short>(array.length);
        for (short el : array) {
            res.add(el);
        }
        return res;
    }

    public static List<Long> toList(long[] array) {
        if (array == null) {
            return new ArrayList<Long>();
        }
        List<Long> res = new ArrayList<Long>(array.length);
        for (long el : array) {
            res.add(el);
        }
        return res;
    }

    public static List<Double> toList(double[] array) {
        if (array == null) {
            return new ArrayList<Double>();
        }
        List<Double> res = new ArrayList<Double>(array.length);
        for (double el : array) {
            res.add(el);
        }
        return res;
    }

    public static List<Float> toList(float[] array) {
        if (array == null) {
            return new ArrayList<Float>();
        }
        List<Float> res = new ArrayList<Float>(array.length);
        for (float el : array) {
            res.add(el);
        }
        return res;
    }
}
