package mn.more.foundation.convert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Entry point to all conversions for array-type instances. This class will make
 * best attempts to convert arrays of all POJOs and primitive types. However,
 * for type-specific conversion, other converter classes might serve as better
 * choice.<p>
 * <p/>
 * All conversion, unless otherwise stated, will maintain the same sequence.
 *
 * @author $Author: mikeliucc $
 * @version $Id: ArrayConverter.java 5 2008-09-01 12:08:42Z mikeliucc $
 */
public final class ArrayConverter {

    /** singleton constr. */
    private ArrayConverter() {
    }

    /**
	 * Returns a <code>java.util.List</code> representation of a given array.
	 *
	 * @param array array of primitive values
	 * @return java.util.List representation of <code>array</code>
	 */
    public static List<Boolean> toList(boolean[] array) {
        if (array == null) {
            return null;
        }
        int size = array.length;
        List<Boolean> list = new ArrayList<Boolean>(size);
        for (int i = 0; i < size; i++) {
            list.add(array[i]);
        }
        return list;
    }

    /**
	 * Returns a <code>java.util.List</code> representation of a given array.
	 *
	 * @param array array of primitive values
	 * @return java.util.List representation of <code>array</code>
	 */
    public static List<Byte> toList(byte[] array) {
        if (array == null) {
            return null;
        }
        int size = array.length;
        List<Byte> list = new ArrayList<Byte>(size);
        for (int i = 0; i < size; i++) {
            list.add(array[i]);
        }
        return list;
    }

    /**
	 * Returns a <code>java.util.List</code> representation of a given array.
	 *
	 * @param array array of primitive values
	 * @return java.util.List representation of <code>array</code>
	 */
    public static List<Integer> toList(int[] array) {
        if (array == null) {
            return null;
        }
        int size = array.length;
        List<Integer> list = new ArrayList<Integer>(size);
        for (int i = 0; i < size; i++) {
            list.add(array[i]);
        }
        return list;
    }

    /**
	 * Returns a <code>java.util.List</code> representation of a given array.
	 *
	 * @param array array of primitive values
	 * @return java.util.List representation of <code>array</code>
	 */
    public static List<Short> toList(short[] array) {
        if (array == null) {
            return null;
        }
        int size = array.length;
        List<Short> list = new ArrayList<Short>(size);
        for (int i = 0; i < size; i++) {
            list.add(array[i]);
        }
        return list;
    }

    /**
	 * Returns a <code>java.util.List</code> representation of a given array.
	 *
	 * @param array array of primitive values
	 * @return java.util.List representation of <code>array</code>
	 */
    public static List<Long> toList(long[] array) {
        if (array == null) {
            return null;
        }
        int size = array.length;
        List<Long> list = new ArrayList<Long>(size);
        for (int i = 0; i < size; i++) {
            list.add(array[i]);
        }
        return list;
    }

    /**
	 * Returns a <code>java.util.List</code> representation of a given array.
	 *
	 * @param array array of primitive values
	 * @return java.util.List representation of <code>array</code>
	 */
    public static List<Float> toList(float[] array) {
        if (array == null) {
            return null;
        }
        int size = array.length;
        List<Float> list = new ArrayList<Float>(size);
        for (int i = 0; i < size; i++) {
            list.add(array[i]);
        }
        return list;
    }

    /**
	 * Returns a <code>java.util.List</code> representation of a given array.
	 *
	 * @param array array of primitive values
	 * @return java.util.List representation of <code>array</code>
	 */
    public static List<Double> toList(double[] array) {
        if (array == null) {
            return null;
        }
        int size = array.length;
        List<Double> list = new ArrayList<Double>(size);
        for (int i = 0; i < size; i++) {
            list.add(array[i]);
        }
        return list;
    }

    /**
	 * convert <code>Object[]</code> into a <code>java.util.List</code> object.
	 *
	 * @param array array of Objects
	 * @return list of the elements in <code>array</code>
	 */
    public static List<Object> toList(Object[] array) {
        if (array == null) {
            return null;
        }
        int size = array.length;
        List<Object> list = new ArrayList<Object>(size);
        list.addAll(Arrays.asList(array));
        return list;
    }

    /**
	 * Converts an array of primitve values into a String array, with each item in
	 * the <code>array</code> as literal value.
	 *
	 * @return String representation of an array of primitive
	 */
    public static String[] toStringArray(boolean[] array) {
        if (array == null) {
            return null;
        }
        int size = array.length;
        String[] strings = new String[size];
        for (int i = 0; i < size; i++) {
            strings[i] = String.valueOf(array[i]);
        }
        return strings;
    }

    /**
	 * Converts an array of primitve values into a String array, with each item in
	 * the <code>array</code> as literal value.
	 *
	 * @return String representation of an array of primitive
	 */
    public static String[] toStringArray(byte[] array) {
        if (array == null) {
            return null;
        }
        int size = array.length;
        String[] strings = new String[size];
        for (int i = 0; i < size; i++) {
            strings[i] = String.valueOf(array[i]);
        }
        return strings;
    }

    /**
	 * Converts an array of primitve values into a String array, with each item in
	 * the <code>array</code> as literal value.
	 *
	 * @return String representation of an array of primitive
	 */
    public static String[] toStringArray(int[] array) {
        if (array == null) {
            return null;
        }
        int size = array.length;
        String[] strings = new String[size];
        for (int i = 0; i < size; i++) {
            strings[i] = String.valueOf(array[i]);
        }
        return strings;
    }

    /**
	 * Converts an array of primitve values into a String array, with each item in
	 * the <code>array</code> as literal value.
	 *
	 * @return String representation of an array of primitive
	 */
    public static String[] toStringArray(short[] array) {
        if (array == null) {
            return null;
        }
        int size = array.length;
        String[] strings = new String[size];
        for (int i = 0; i < size; i++) {
            strings[i] = String.valueOf(array[i]);
        }
        return strings;
    }

    /**
	 * Converts an array of primitve values into a String array, with each item in
	 * the <code>array</code> as literal value.
	 *
	 * @return String representation of an array of primitive
	 */
    public static String[] toStringArray(long[] array) {
        if (array == null) {
            return null;
        }
        int size = array.length;
        String[] strings = new String[size];
        for (int i = 0; i < size; i++) {
            strings[i] = String.valueOf(array[i]);
        }
        return strings;
    }

    /**
	 * Converts an array of primitve values into a String array, with each item in
	 * the <code>array</code> as literal value.
	 *
	 * @return String representation of an array of primitive
	 */
    public static String[] toStringArray(float[] array) {
        if (array == null) {
            return null;
        }
        int size = array.length;
        String[] strings = new String[size];
        for (int i = 0; i < size; i++) {
            strings[i] = String.valueOf(array[i]);
        }
        return strings;
    }

    /**
	 * Converts an array of primitve values into a String array, with each item in
	 * the <code>array</code> as literal value.
	 *
	 * @return String representation of an array of primitive
	 */
    public static String[] toStringArray(double[] array) {
        if (array == null) {
            return null;
        }
        int size = array.length;
        String[] strings = new String[size];
        for (int i = 0; i < size; i++) {
            strings[i] = String.valueOf(array[i]);
        }
        return strings;
    }

    /**
	 * converts an object array into a string array, strictly using the
	 * <code>toString()</code> method of each object item in the array.
	 *
	 * @param array array of Objects
	 * @return array of the String representation of elements in
	 *         <code>array</code>
	 */
    public static String[] toStringArray(Object[] array) {
        if (array == null) {
            return null;
        }
        int size = array.length;
        String[] stringArray = new String[size];
        for (int i = 0; i < size; i++) {
            stringArray[i] = (array[i] == null ? null : array[i].toString());
        }
        return stringArray;
    }

    /**
	 * return the <code>java.lang.String</code> representation of
	 * <code>array</code>. <code>delimiter</code> is used to seperate the slots in
	 * the <code>array</code>.
	 *
	 * @param array array of byte values
	 * @return String representation of the array of primitive values.
	 */
    public static String toString(boolean[] array, String delimiter) {
        if (array == null) {
            return null;
        }
        if (array.length < 1) {
            return "";
        }
        int arrayLength = array.length - 1;
        StringBuilder sbuffer = new StringBuilder(arrayLength * 4);
        for (int i = 0; i < arrayLength; i++) {
            sbuffer.append(array[i] ? "true" : "false");
            sbuffer.append(delimiter).append("");
        }
        sbuffer.append(array[arrayLength] ? "true" : "false");
        return sbuffer.toString();
    }

    /**
	 * return the <code>java.lang.String</code> representation of
	 * <code>array</code>. <code>delimiter</code> is used to seperate the slots in
	 * the <code>array</code>.
	 *
	 * @param array array of byte values
	 * @return String representation of the array of primitive values.
	 */
    public static String toString(byte[] array, String delimiter) {
        if (array == null) {
            return null;
        }
        if (array.length < 1) {
            return "";
        }
        int arrayLength = array.length - 1;
        StringBuilder sbuffer = new StringBuilder(arrayLength * 3);
        for (int i = 0; i < arrayLength; i++) {
            sbuffer.append(array[i]);
            sbuffer.append(delimiter).append("");
        }
        sbuffer.append(array[arrayLength]);
        return sbuffer.toString();
    }

    /**
	 * return the <code>java.lang.String</code> representation of
	 * <code>array</code>. <code>delimiter</code> is used to seperate the slots in
	 * the <code>array</code>.
	 *
	 * @param array array of byte values
	 * @return String representation of the array of primitive values.
	 */
    public static String toString(int[] array, String delimiter) {
        if (array == null) {
            return null;
        }
        if (array.length < 1) {
            return "";
        }
        int arrayLength = array.length - 1;
        StringBuilder sbuffer = new StringBuilder(arrayLength * 5);
        for (int i = 0; i < arrayLength; i++) {
            sbuffer.append(array[i]);
            sbuffer.append(delimiter).append("");
        }
        sbuffer.append(array[arrayLength]);
        return sbuffer.toString();
    }

    /**
	 * return the <code>java.lang.String</code> representation of
	 * <code>array</code>. <code>delimiter</code> is used to seperate the slots in
	 * the <code>array</code>.
	 *
	 * @param array array of byte values
	 * @return String representation of the array of primitive values.
	 */
    public static String toString(short[] array, String delimiter) {
        if (array == null) {
            return null;
        }
        if (array.length < 1) {
            return "";
        }
        int arrayLength = array.length - 1;
        StringBuilder sbuffer = new StringBuilder(arrayLength * 5);
        for (int i = 0; i < arrayLength; i++) {
            sbuffer.append(array[i]);
            sbuffer.append(delimiter).append("");
        }
        sbuffer.append(array[arrayLength]);
        return sbuffer.toString();
    }

    /**
	 * return the <code>java.lang.String</code> representation of
	 * <code>array</code>. <code>delimiter</code> is used to seperate the slots in
	 * the <code>array</code>.
	 *
	 * @param array array of byte values
	 * @return String representation of the array of primitive values.
	 */
    public static String toString(long[] array, String delimiter) {
        if (array == null) {
            return null;
        }
        if (array.length < 1) {
            return "";
        }
        int arrayLength = array.length - 1;
        StringBuilder sbuffer = new StringBuilder(arrayLength * 5);
        for (int i = 0; i < arrayLength; i++) {
            sbuffer.append(array[i]);
            sbuffer.append(delimiter).append("");
        }
        sbuffer.append(array[arrayLength]);
        return sbuffer.toString();
    }

    /**
	 * return the <code>java.lang.String</code> representation of
	 * <code>array</code>. <code>delimiter</code> is used to seperate the slots in
	 * the <code>array</code>.
	 *
	 * @param array array of byte values
	 * @return String representation of the array of primitive values.
	 */
    public static String toString(float[] array, String delimiter) {
        if (array == null) {
            return null;
        }
        if (array.length < 1) {
            return "";
        }
        int arrayLength = array.length - 1;
        StringBuilder sbuffer = new StringBuilder(arrayLength * 6);
        for (int i = 0; i < arrayLength; i++) {
            sbuffer.append(array[i]);
            sbuffer.append(delimiter).append("");
        }
        sbuffer.append(array[arrayLength]);
        return sbuffer.toString();
    }

    /**
	 * return the <code>java.lang.String</code> representation of
	 * <code>array</code>. <code>delimiter</code> is used to seperate the slots in
	 * the <code>array</code>.
	 *
	 * @param array array of byte values
	 * @return String representation of the array of primitive values.
	 */
    public static String toString(double[] array, String delimiter) {
        if (array == null) {
            return null;
        }
        if (array.length < 1) {
            return "";
        }
        int arrayLength = array.length - 1;
        StringBuilder sbuffer = new StringBuilder(arrayLength * 6);
        for (int i = 0; i < arrayLength; i++) {
            sbuffer.append(array[i]);
            sbuffer.append(delimiter).append("");
        }
        sbuffer.append(array[arrayLength]);
        return sbuffer.toString();
    }

    /**
	 * Convert String array into a <code>String</code> object, separating each
	 * indices with the <code>delimiter</code> specified.
	 *
	 * @param array array of Objects
	 * @return java.lang.String
	 */
    public static String toString(Object[] array, String delimiter) {
        if (array == null) {
            return null;
        }
        if (array.length < 1) {
            return "";
        }
        int arrayLength = array.length - 1;
        StringBuilder sbuffer = new StringBuilder(arrayLength * 15);
        for (int i = 0; i < arrayLength; i++) {
            sbuffer.append(array[i]);
            sbuffer.append(delimiter).append("");
        }
        sbuffer.append(array[arrayLength]);
        return sbuffer.toString();
    }

    /**
	 * returns the <code>java.lang.String</code> representation of the
	 * <code>index</code>-th position of the <code>array</code> array.
	 *
	 * @param array java.lang.Object[]
	 * @param index int
	 * @return java.lang.String
	 */
    public static String toString(Object[] array, int index) {
        if (array == null) {
            return null;
        }
        if (array.length <= index) {
            return null;
        }
        Object obj = array[index];
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }
}
