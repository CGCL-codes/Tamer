package org.zxframework.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Gavin King
 */
public final class ArrayUtil {

    /** An empty string array. */
    public static final String[] EMPTY_STRING_ARRAY = {};

    /** An empty int array. */
    public static final int[] EMPTY_INT_ARRAY = {};

    /** An empty boolean array. */
    public static final boolean[] EMPTY_BOOLEAN_ARRAY = {};

    /** An empty Class array. */
    public static final Class[] EMPTY_CLASS_ARRAY = {};

    /** An empty Object array. */
    public static final Object[] EMPTY_OBJECT_ARRAY = {};

    /** 
     * Hide the default constructor.
     */
    private ArrayUtil() {
        super();
    }

    /**
     * Check if the array contains a specific object.
     * 
	 * @param array The array to seek in.
	 * @param object The object to look for.
	 * @return Returns true if the Array contains the object.
	 */
    public static boolean contains(Object[] array, Object object) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(object)) return true;
        }
        return false;
    }

    /**
     * Find the position of an object in a array.
     * 
	 * @param array The array to seek in.
	 * @param object The object to look for.
	 * @return Returns the postion of the first matach in the array, otherwise -1 if no matches.
	 */
    public static int indexOf(Object[] array, Object object) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(object)) return i;
        }
        return -1;
    }

    /**
     * Create a shallow clone of an array.
     * 
	 * @param elementClass The type of array to return.
	 * @param array The array to clone.
	 * @return Returns a cloned array.
	 */
    public static Object[] clone(Class elementClass, Object[] array) {
        Object[] result = (Object[]) Array.newInstance(elementClass, array.length);
        System.arraycopy(array, 0, result, 0, array.length);
        return result;
    }

    /**
     * Get a String array of the toString of an Array.
     * 
	 * @param objects The array to print the toString of.
	 * @return Returns a String array.
	 */
    public static String[] toStringArray(Object[] objects) {
        int length = objects.length;
        String[] result = new String[length];
        for (int i = 0; i < length; i++) {
            result[i] = objects[i].toString();
        }
        return result;
    }

    /**
     * @param componentType The type of array we want to populate.
     * @param value The default value to use.
     * @param length The size of the array.
     * @return Retun an Object[] with the specified default value.
     */
    public static Object[] fillArray(Class componentType, Object value, int length) {
        Object[] result = (Object[]) Array.newInstance(componentType, length);
        Arrays.fill(result, value);
        return result;
    }

    /**
     * Populate a String array with a specified default value.
     * 
	 * @param value The value to fill the array with.
	 * @param length The size of the array.
	 * @return Returns an array with the default value set to the value specified.
	 */
    public static String[] fillArray(String value, int length) {
        String[] result = new String[length];
        Arrays.fill(result, value);
        return result;
    }

    /**
	 * @param value The value to fill the array with.
	 * @param length The size of the array.
	 * @return Returns an int Array with the default value set to "value".
	 */
    public static int[] fillArray(int value, int length) {
        int[] result = new int[length];
        Arrays.fill(result, value);
        return result;
    }

    /**
	 * @param coll The collection. (ALL elements must be Strings)
	 * @return Returns an String[] of the elements in the colleciton.
	 */
    public static String[] toStringArray(Collection coll) {
        return (String[]) coll.toArray(EMPTY_STRING_ARRAY);
    }

    /**
	 * @param coll The collection. (ALL elements must be Strings)
	 * @return Returns an String[][] of the elements in the collection.
	 */
    public static String[][] to2DStringArray(Collection coll) {
        return (String[][]) coll.toArray(new String[coll.size()][]);
    }

    /**
	 * @param coll The collection. (ALL elements must be int)
	 * @return Returns a int[][] of the elements in the collection.
	 */
    public static int[][] to2DIntArray(Collection coll) {
        return (int[][]) coll.toArray(new int[coll.size()][]);
    }

    /**
	 * @param coll The collection. (ALL elements must be int)
	 * @return Returns a int[] of the elements in the collection.
	 */
    public static int[] toIntArray(Collection coll) {
        Iterator iter = coll.iterator();
        int[] arr = new int[coll.size()];
        int i = 0;
        while (iter.hasNext()) {
            arr[i++] = ((Integer) iter.next()).intValue();
        }
        return arr;
    }

    /**
	 * @param coll The collection. (ALL elements must be boolean)
	 * @return Returns a boolean[] of the elements in the collection.
	 */
    public static boolean[] toBooleanArray(Collection coll) {
        Iterator iter = coll.iterator();
        boolean[] arr = new boolean[coll.size()];
        int i = 0;
        while (iter.hasNext()) {
            arr[i++] = ((Boolean) iter.next()).booleanValue();
        }
        return arr;
    }

    /**
	 * @param array The Object array to cast.
	 * @param to The array to cast to.
	 * @return Returns the resultant cast.
	 */
    public static Object[] typecast(Object[] array, Object[] to) {
        return java.util.Arrays.asList(array).toArray(to);
    }

    /**
	 * @param array The array to convery.
	 * @return Returns a list version of an array.
	 */
    public static List toList(Object array) {
        if (array instanceof Object[]) return Arrays.asList((Object[]) array);
        int size = Array.getLength(array);
        ArrayList list = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            list.add(Array.get(array, i));
        }
        return list;
    }

    /**
	 * @param strings The array to slice.
	 * @param begin The starting position. Note, arrays are 0 based.
	 * @param length The length of the array to returns. Note, begin+lengh 
     *               should be less than the total size of the array
	 * @return Returns the specified section of the array.
	 */
    public static String[] slice(String[] strings, int begin, int length) {
        String[] result = new String[length];
        for (int i = 0; i < length; i++) {
            result[i] = strings[begin + i];
        }
        return result;
    }

    /**
     * @param objects The array to slice.
     * @param begin The starting position. Note, arrays are 0 based.
     * @param length The length of the array to returns. Note, begin+lengh 
     *               should be less than the total size of the array
     * @return Returns the specified section of the array.
     */
    public static Object[] slice(Object[] objects, int begin, int length) {
        Object[] result = new Object[length];
        for (int i = 0; i < length; i++) {
            result[i] = objects[begin + i];
        }
        return result;
    }

    /**
	 * @param iter The itertor to get a list of.
	 * @return Returns a list version of an Iterator.
	 */
    public static List toList(Iterator iter) {
        List list = new ArrayList();
        while (iter.hasNext()) {
            list.add(iter.next());
        }
        return list;
    }

    /**
	 * @param x The array to join to.
	 * @param y The array to join
	 * @return Returns the joined array. Start at x and ending at y.
	 */
    public static String[] join(String[] x, String[] y) {
        String[] result = new String[x.length + y.length];
        for (int i = 0; i < x.length; i++) result[i] = x[i];
        for (int i = 0; i < y.length; i++) result[i + x.length] = y[i];
        return result;
    }

    /**
     * @param x The array to join to.
     * @param y The array to join
     * @return Returns the joined array. Start at x and ending at y.
     */
    public static int[] join(int[] x, int[] y) {
        int[] result = new int[x.length + y.length];
        for (int i = 0; i < x.length; i++) result[i] = x[i];
        for (int i = 0; i < y.length; i++) result[i + x.length] = y[i];
        return result;
    }

    /**
	 * @param array The array to get the toString of.
	 * @return Returns the toString of an array.
	 */
    public static String toString(Object[] array) {
        StringBuffer sb = new StringBuffer();
        sb.append('[');
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) sb.append(',');
        }
        sb.append(']');
        return sb.toString();
    }

    /**
     * Checks whether all of the values in an array are negative.
     * 
     * @param array The int array.
     * @return Returns whether all of the elements in a boolean array are negative.
     */
    public static boolean isAllNegative(int[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] >= 0) return false;
        }
        return true;
    }

    /**
     * Checks whether all of the values in an array are true.
     * 
     * @param array The boolean array.
     * @return Returns whether all of the elements in a boolean array are true.
     */
    public static boolean isAllTrue(boolean[] array) {
        for (int i = 0; i < array.length; i++) {
            if (!array[i]) return false;
        }
        return true;
    }

    /**
	 * @param array The boolean array.
	 * @return Returns the number of true boolean values in a boolean array.
	 */
    public static int countTrue(boolean[] array) {
        int result = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i]) result++;
        }
        return result;
    }

    /**
     * Checks whether all of the values in an array is false.
     * 
	 * @param array The boolean array.
	 * @return Returns whether all of the elements in a boolean array is false.
	 */
    public static boolean isAllFalse(boolean[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i]) return false;
        }
        return true;
    }

    /**
     * Copy all of the values in the array into the Collection object.
     * 
	 * @param collection The collection to add the elements to.
	 * @param array The array of values to copy.
	 */
    public static void addAll(Collection collection, Object[] array) {
        for (int i = 0; i < array.length; i++) {
            collection.add(array[i]);
        }
    }

    private static int SEED = 23;

    private static int PRIME_NUMER = 37;

    /**
	 * Calculate the array hash (only the first level).
     * 
     * @param array The array to get the hash
     * @return Returns the hash of the array. 
	 */
    public static int hash(Object[] array) {
        int length = array.length;
        int seed = SEED;
        for (int index = 0; index < length; index++) {
            seed = hash(seed, array[index] == null ? 0 : array[index].hashCode());
        }
        return seed;
    }

    /**
	 * Calculate the array hash (only the first level),
     * 
     * @param array The array to get the hash
     * @return Returns the hash of the array. 
	 */
    public static int hash(char[] array) {
        int length = array.length;
        int seed = SEED;
        for (int index = 0; index < length; index++) {
            seed = hash(seed, array[index]);
        }
        return seed;
    }

    /**
	 * Calculate the array hash (only the first level).
     * 
	 * @param bytes The array to get the hash
	 * @return Returns the hash of the array.
	 */
    public static int hash(byte[] bytes) {
        int length = bytes.length;
        int seed = SEED;
        for (int index = 0; index < length; index++) {
            seed = hash(seed, bytes[index]);
        }
        return seed;
    }

    private static int hash(int seed, int i) {
        return PRIME_NUMER * seed + i;
    }

    /**
	 * Compare 2 arrays only at the first level.
     * 
	 * @param o1 Array one.
	 * @param o2 Array two.
	 * @return Returns whether the 2 arrays are equal.
	 */
    public static boolean isEquals(Object[] o1, Object[] o2) {
        if (o1 == o2) return true;
        if (o1 == null || o2 == null) return false;
        int length = o1.length;
        if (length != o2.length) return false;
        for (int index = 0; index < length; index++) {
            if (!o1[index].equals(o2[index])) return false;
        }
        return true;
    }

    /**
	 * Compare 2 arrays only at the first level.
     * 
	 * @param o1 Array one.
	 * @param o2 Array two.
	 * @return Returns whether to 2 arrays are equal.
	 */
    public static boolean isEquals(char[] o1, char[] o2) {
        if (o1 == o2) return true;
        if (o1 == null || o2 == null) return false;
        int length = o1.length;
        if (length != o2.length) return false;
        for (int index = 0; index < length; index++) {
            if (!(o1[index] == o2[index])) return false;
        }
        return true;
    }

    /**
	 * Compare 2 arrays only at the first level.
     * 
	 * @param b1 Array one
	 * @param b2 Array two.
	 * @return Returns whether the 2 arrays are equal.
	 */
    public static boolean isEquals(byte[] b1, byte[] b2) {
        if (b1 == b2) return true;
        if (b1 == null || b2 == null) return false;
        int length = b1.length;
        if (length != b2.length) return false;
        for (int index = 0; index < length; index++) {
            if (!(b1[index] == b2[index])) return false;
        }
        return true;
    }
}
