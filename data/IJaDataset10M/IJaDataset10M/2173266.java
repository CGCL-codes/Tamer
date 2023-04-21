package org.happy.commons.sort.algorithms;

import static org.junit.Assert.*;
import java.util.Arrays;
import org.happy.commons.sort.algorithms.InsertionSort_1x0;
import org.happy.commons.util.ArraysHelper;
import org.happy.commons.util.Arrays_1x0;
import org.happy.commons.util.Random_1x0;
import org.happy.commons.util.comparators.Comparator_1x0;
import org.junit.Test;

public class InsertionSortTest {

    @Test
    public void testSortRange_e2() {
        for (int t = 0; t < (int) 1e5; t++) {
            int arrayLength = (int) (20 * Math.random()) + 5;
            Integer[] a = ArraysHelper.createRandomArray(arrayLength, 0, arrayLength);
            Integer[] a_copy = a.clone();
            Comparator_1x0<Integer> comparator = new Comparator_1x0<Integer>();
            int fromIndex = -1;
            int toIndex = -1;
            while (fromIndex == toIndex) {
                fromIndex = Random_1x0.randomInt(arrayLength - 1);
                toIndex = Random_1x0.randomInt(arrayLength - 1);
                if (toIndex < fromIndex) {
                    int tmp = toIndex;
                    toIndex = fromIndex;
                    fromIndex = tmp;
                }
            }
            InsertionSort_1x0.sort(a, fromIndex, toIndex, comparator);
            int errorIndex = Arrays_1x0.isAscending(a, fromIndex, toIndex, comparator);
            if (errorIndex != -1) {
                System.out.println("fromIndex=" + fromIndex);
                System.out.println("toIndex=" + toIndex);
                System.out.println(Arrays_1x0.toString(a_copy));
                System.out.println(Arrays_1x0.toString(a));
            }
            assertEquals(-1, errorIndex);
        }
    }

    @Test
    public void testSort01() {
        Integer[] a = new Integer[] { 2 };
        Comparator_1x0<Integer> comparator = new Comparator_1x0<Integer>();
        InsertionSort_1x0.sort(a, comparator);
        assertEquals(new Integer(2), a[0]);
    }

    @Test
    public void testSort02() {
        Integer[] a = new Integer[] { 2, 1 };
        Comparator_1x0<Integer> comparator = new Comparator_1x0<Integer>();
        InsertionSort_1x0.sort(a, 0, 1, comparator);
        assertEquals(-1, Arrays_1x0.isAscending(a, comparator));
    }

    @Test
    public void testSort03() {
        for (int t = 0; t < (int) 1e5; t++) {
            Integer[] a = new Integer[] { (int) (Math.random() * 2), (int) (Math.random() * 2) };
            Comparator_1x0<Integer> comparator = new Comparator_1x0<Integer>();
            InsertionSort_1x0.sort(a, comparator);
            assertEquals(-1, Arrays_1x0.isAscending(a, comparator));
        }
    }

    @Test
    public void testSort04() {
        for (int t = 0; t < (int) 1e5; t++) {
            Integer[] a = new Integer[] { (int) (Math.random() * 10), (int) (Math.random() * 10) };
            Comparator_1x0<Integer> comparator = new Comparator_1x0<Integer>();
            InsertionSort_1x0.sort(a, comparator);
            assertEquals(-1, Arrays_1x0.isAscending(a, comparator));
        }
    }

    @Test
    public void testSort05() {
        for (int t = 0; t < (int) 1e5; t++) {
            Integer[] a = new Integer[] { (int) (Math.random() * 10), (int) (Math.random() * 10), (int) (Math.random() * 10) };
            Comparator_1x0<Integer> comparator = new Comparator_1x0<Integer>();
            InsertionSort_1x0.sort(a, comparator);
            assertEquals(-1, Arrays_1x0.isAscending(a, comparator));
        }
    }

    @Test
    public void testSort06() {
        for (int t = 0; t < (int) 1e6; t++) {
            int arrayLength = (int) (100 * Math.random()) + 10;
            Integer[] a = ArraysHelper.createRandomArray(arrayLength, 0, arrayLength);
            Comparator_1x0<Integer> comparator = new Comparator_1x0<Integer>();
            int fromIndex = (int) (Math.random() * a.length) / 2;
            int toIndex = (int) (Math.random() * a.length);
            if (toIndex < fromIndex) {
                int tmp = toIndex;
                toIndex = fromIndex;
                fromIndex = tmp;
            } else if (toIndex == fromIndex) {
                toIndex += 1;
            }
            InsertionSort_1x0.sort(a, fromIndex, toIndex, comparator);
            int errorIndex = Arrays_1x0.isAscending(a, fromIndex, toIndex - 1, comparator);
            assertEquals(-1, errorIndex);
        }
    }

    @Test
    public void testSort07() {
        for (int t = 0; t < (int) 1e6; t++) {
            int arrayLength = (int) (100 * Math.random()) + 1;
            Integer[] a = ArraysHelper.createRandomArray(arrayLength, 0, arrayLength);
            Comparator_1x0<Integer> comparator = new Comparator_1x0<Integer>();
            InsertionSort_1x0.sort(a, comparator);
            assertEquals(-1, Arrays_1x0.isAscending(a, comparator));
        }
    }

    @Test
    public void testSortIntArray() {
        for (int t = 0; t < 10000; t++) {
            int arrayLength = (int) 1e3;
            Integer[] aTemp = ArraysHelper.createRandomArray(arrayLength, 0, arrayLength);
            int[] a = new int[aTemp.length];
            for (int i = 0; i < aTemp.length; i++) {
                a[i] = aTemp[i];
            }
            int pLength = (int) (Math.random() * 30 + 1);
            int[] p = new int[pLength];
            for (int i = 0; i < pLength; i++) {
                p[i] = (int) (Math.random() * (arrayLength - 1));
            }
            Arrays.sort(p);
            InsertionSort_1x0.sort(a);
            Integer[] a_validate = new Integer[pLength];
            for (int i = 0; i < pLength; i++) {
                a_validate[i] = a[p[i]];
            }
            assertEquals(-1, Arrays_1x0.isAscending(a_validate, new Comparator_1x0<Integer>()));
        }
    }

    /**
	 * test primitive data types
	 */
    @Test
    public void testSortArrayByte() {
        for (int t = 0; t < 10000; t++) {
            int arrayLength = (int) 1e3;
            byte[] a = new byte[arrayLength];
            Random_1x0.randomBytes(a);
            InsertionSort_1x0.sort(a);
            Byte[] a_validate = new Byte[a.length];
            for (int i = 0; i < a_validate.length; i++) {
                a_validate[i] = a[i];
            }
            assertEquals(-1, Arrays_1x0.isAscending(a_validate));
        }
    }

    @Test
    public void testSortArrayByteRange_e2() {
        for (int t = 0; t < (int) 1e5; t++) {
            int arrayLength = (int) (1e2 * Math.random()) + 5;
            byte[] a = new byte[arrayLength];
            Random_1x0.randomBytes(a);
            int fromIndex = -1;
            int toIndex = -1;
            while (fromIndex == toIndex) {
                fromIndex = Random_1x0.randomInt(arrayLength - 1);
                toIndex = Random_1x0.randomInt(arrayLength - 1);
                if (toIndex < fromIndex) {
                    int tmp = toIndex;
                    toIndex = fromIndex;
                    fromIndex = tmp;
                }
            }
            InsertionSort_1x0.sort(a, fromIndex, toIndex);
            Byte[] a_validate = new Byte[a.length];
            for (int i = 0; i < a_validate.length; i++) {
                a_validate[i] = a[i];
            }
            assertEquals(-1, Arrays_1x0.isAscending(a_validate, fromIndex, toIndex));
        }
    }

    @Test
    public void testSortArrayShort() {
        for (int t = 0; t < 10000; t++) {
            int arrayLength = (int) 1e3;
            short[] a = new short[arrayLength];
            for (int i = 0; i < a.length; i++) a[i] = (short) Random_1x0.randomInt(Short.MAX_VALUE);
            InsertionSort_1x0.sort(a);
            Short[] a_validate = new Short[a.length];
            for (int i = 0; i < a_validate.length; i++) {
                a_validate[i] = a[i];
            }
            assertEquals(-1, Arrays_1x0.isAscending(a_validate));
        }
    }

    @Test
    public void testSortArrayShortRange_e2() {
        for (int t = 0; t < (int) 1e5; t++) {
            int arrayLength = (int) (1e2 * Math.random()) + 5;
            short[] a = new short[arrayLength];
            for (int i = 0; i < a.length; i++) a[i] = (short) Random_1x0.randomInt(Short.MAX_VALUE);
            int fromIndex = -1;
            int toIndex = -1;
            while (fromIndex == toIndex) {
                fromIndex = Random_1x0.randomInt(arrayLength - 1);
                toIndex = Random_1x0.randomInt(arrayLength - 1);
                if (toIndex < fromIndex) {
                    int tmp = toIndex;
                    toIndex = fromIndex;
                    fromIndex = tmp;
                }
            }
            InsertionSort_1x0.sort(a, fromIndex, toIndex);
            Short[] a_validate = new Short[a.length];
            for (int i = 0; i < a_validate.length; i++) {
                a_validate[i] = a[i];
            }
            assertEquals(-1, Arrays_1x0.isAscending(a_validate, fromIndex, toIndex));
        }
    }

    @Test
    public void testSortArrayInt() {
        for (int t = 0; t < 10000; t++) {
            int arrayLength = (int) 1e3;
            int[] a = new int[arrayLength];
            for (int i = 0; i < a.length; i++) a[i] = Random_1x0.randomInt(Integer.MAX_VALUE);
            InsertionSort_1x0.sort(a);
            Integer[] a_validate = new Integer[a.length];
            for (int i = 0; i < a_validate.length; i++) {
                a_validate[i] = a[i];
            }
            assertEquals(-1, Arrays_1x0.isAscending(a_validate));
        }
    }

    @Test
    public void testSortArrayIntegerRange_e2() {
        for (int t = 0; t < (int) 1e5; t++) {
            int arrayLength = (int) (1e2 * Math.random()) + 5;
            int[] a = new int[arrayLength];
            for (int i = 0; i < a.length; i++) a[i] = Random_1x0.randomInt(Integer.MAX_VALUE);
            int fromIndex = -1;
            int toIndex = -1;
            while (fromIndex == toIndex) {
                fromIndex = Random_1x0.randomInt(arrayLength - 1);
                toIndex = Random_1x0.randomInt(arrayLength - 1);
                if (toIndex < fromIndex) {
                    int tmp = toIndex;
                    toIndex = fromIndex;
                    fromIndex = tmp;
                }
            }
            InsertionSort_1x0.sort(a, fromIndex, toIndex);
            Integer[] a_validate = new Integer[a.length];
            for (int i = 0; i < a_validate.length; i++) {
                a_validate[i] = a[i];
            }
            assertEquals(-1, Arrays_1x0.isAscending(a_validate, fromIndex, toIndex));
        }
    }

    @Test
    public void testSortArrayLong() {
        for (int t = 0; t < 10000; t++) {
            int arrayLength = (int) 1e3;
            long[] a = new long[arrayLength];
            for (int i = 0; i < a.length; i++) a[i] = Random_1x0.randomLong();
            InsertionSort_1x0.sort(a);
            Long[] a_validate = new Long[a.length];
            for (int i = 0; i < a_validate.length; i++) {
                a_validate[i] = a[i];
            }
            assertEquals(-1, Arrays_1x0.isAscending(a_validate));
        }
    }

    @Test
    public void testSortArrayLongRange_e2() {
        for (int t = 0; t < (int) 1e5; t++) {
            int arrayLength = (int) (1e2 * Math.random()) + 5;
            long[] a = new long[arrayLength];
            for (int i = 0; i < a.length; i++) a[i] = Random_1x0.randomLong();
            int fromIndex = -1;
            int toIndex = -1;
            while (fromIndex == toIndex) {
                fromIndex = Random_1x0.randomInt(arrayLength - 1);
                toIndex = Random_1x0.randomInt(arrayLength - 1);
                if (toIndex < fromIndex) {
                    int tmp = toIndex;
                    toIndex = fromIndex;
                    fromIndex = tmp;
                }
            }
            InsertionSort_1x0.sort(a, fromIndex, toIndex);
            Long[] a_validate = new Long[a.length];
            for (int i = 0; i < a_validate.length; i++) {
                a_validate[i] = a[i];
            }
            assertEquals(-1, Arrays_1x0.isAscending(a_validate, fromIndex, toIndex));
        }
    }

    @Test
    public void testSortArrayFloat() {
        for (int t = 0; t < 10000; t++) {
            int arrayLength = (int) 1e3;
            float[] a = new float[arrayLength];
            for (int i = 0; i < a.length; i++) a[i] = Random_1x0.randomFloat();
            InsertionSort_1x0.sort(a);
            Float[] a_validate = new Float[a.length];
            for (int i = 0; i < a_validate.length; i++) {
                a_validate[i] = a[i];
            }
            assertEquals(-1, Arrays_1x0.isAscending(a_validate));
        }
    }

    @Test
    public void testSortArrayFloatRange_e2() {
        for (int t = 0; t < (int) 1e5; t++) {
            int arrayLength = (int) (1e2 * Math.random()) + 5;
            float[] a = new float[arrayLength];
            for (int i = 0; i < a.length; i++) a[i] = Random_1x0.randomFloat();
            int fromIndex = -1;
            int toIndex = -1;
            while (fromIndex == toIndex) {
                fromIndex = Random_1x0.randomInt(arrayLength - 1);
                toIndex = Random_1x0.randomInt(arrayLength - 1);
                if (toIndex < fromIndex) {
                    int tmp = toIndex;
                    toIndex = fromIndex;
                    fromIndex = tmp;
                }
            }
            InsertionSort_1x0.sort(a, fromIndex, toIndex);
            Float[] a_validate = new Float[a.length];
            for (int i = 0; i < a_validate.length; i++) {
                a_validate[i] = a[i];
            }
            assertEquals(-1, Arrays_1x0.isAscending(a_validate, fromIndex, toIndex));
        }
    }

    @Test
    public void testSortArrayDouble() {
        for (int t = 0; t < 10000; t++) {
            int arrayLength = (int) 1e3;
            double[] a = new double[arrayLength];
            for (int i = 0; i < a.length; i++) a[i] = Random_1x0.randomDouble();
            InsertionSort_1x0.sort(a);
            Double[] a_validate = new Double[a.length];
            for (int i = 0; i < a_validate.length; i++) {
                a_validate[i] = a[i];
            }
            assertEquals(-1, Arrays_1x0.isAscending(a_validate));
        }
    }

    @Test
    public void testSortArrayDoubleRange_e2() {
        for (int t = 0; t < (int) 1e5; t++) {
            int arrayLength = (int) (1e2 * Math.random()) + 5;
            double[] a = new double[arrayLength];
            for (int i = 0; i < a.length; i++) a[i] = Random_1x0.randomDouble();
            int fromIndex = -1;
            int toIndex = -1;
            while (fromIndex == toIndex) {
                fromIndex = Random_1x0.randomInt(arrayLength - 1);
                toIndex = Random_1x0.randomInt(arrayLength - 1);
                if (toIndex < fromIndex) {
                    int tmp = toIndex;
                    toIndex = fromIndex;
                    fromIndex = tmp;
                }
            }
            InsertionSort_1x0.sort(a, fromIndex, toIndex);
            Double[] a_validate = new Double[a.length];
            for (int i = 0; i < a_validate.length; i++) {
                a_validate[i] = a[i];
            }
            assertEquals(-1, Arrays_1x0.isAscending(a_validate, fromIndex, toIndex));
        }
    }

    @Test
    public void testSortArrayChar() {
        for (int t = 0; t < 10000; t++) {
            int arrayLength = (int) 1e3;
            char[] a = new char[arrayLength];
            for (int i = 0; i < a.length; i++) a[i] = (char) Random_1x0.randomInt(Character.MAX_VALUE);
            InsertionSort_1x0.sort(a);
            Character[] a_validate = new Character[a.length];
            for (int i = 0; i < a_validate.length; i++) {
                a_validate[i] = a[i];
            }
            assertEquals(-1, Arrays_1x0.isAscending(a_validate));
        }
    }

    @Test
    public void testSortArrayCharRange_e2() {
        for (int t = 0; t < (int) 1e2; t++) {
            int arrayLength = (int) (20 * Math.random()) + 5;
            char[] a = new char[arrayLength];
            for (int i = 0; i < a.length; i++) a[i] = (char) Random_1x0.randomInt(Character.MAX_VALUE);
            int fromIndex = -1;
            int toIndex = -1;
            while (fromIndex == toIndex) {
                fromIndex = Random_1x0.randomInt(arrayLength - 1);
                toIndex = Random_1x0.randomInt(arrayLength - 1);
                if (toIndex < fromIndex) {
                    int tmp = toIndex;
                    toIndex = fromIndex;
                    fromIndex = tmp;
                }
            }
            InsertionSort_1x0.sort(a, fromIndex, toIndex);
            Character[] a_validate = new Character[a.length];
            for (int i = 0; i < a_validate.length; i++) {
                a_validate[i] = a[i];
            }
            assertEquals(-1, Arrays_1x0.isAscending(a_validate, fromIndex, toIndex));
        }
    }
}
