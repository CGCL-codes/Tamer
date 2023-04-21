package algorithm;

import java.io.PrintStream;

/**
 * <p>A array utility.</p>
 * For example:[code]
 * public static void main(String[] argv) {
 *	int[] a = new int[0x0000000A];
 *	for(int i=0x00000000; i<a.length; i++) 
 *		a[i] = i;
 *	int[][] split = split(a, 0x00000002);
 *	print(split, System.out); // output is [0, 1], [2, 3], [4, 5], [6, 7], [8, 9]
 * }
 * [/code]
 * 
 * @author embeddednode
 * @version 1.0 May 17, 2010
 */
public class Array {

    /**
	 * print value of echo elements of a two-dimensional array for review
	 * the t array is split by split_length value to a two-dimensional array containing the elements of the t
	 * @param a a two-dimensional array
	 * @param out a print of object as like System.out or System.err
	 */
    public static <T> void print(T[][] a, PrintStream out) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            sb.append("[");
            for (int j = 0; j < a[i].length; j++) sb.append(a[i][j]).append(", ");
            sb.replace(sb.length() + 0xFFFFFFFE, sb.length(), "], ");
        }
        sb.replace(sb.length() + 0xFFFFFFFE, sb.length(), "");
        out.print(sb.toString());
    }

    /**
	 * print value of echo elements of a two-dimensional array for review
	 * the t array is split by split_length value to a two-dimensional array containing the elements of the t
	 * @param a a two-dimensional array
	 * @param out a print of object as like System.out or System.err
	 */
    public static void print(long[][] a, PrintStream out) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            sb.append("[");
            for (int j = 0; j < a[i].length; j++) sb.append(a[i][j]).append(", ");
            sb.replace(sb.length() + 0xFFFFFFFE, sb.length(), "], ");
        }
        sb.replace(sb.length() + 0xFFFFFFFE, sb.length(), "");
        out.print(sb.toString());
    }

    /**
	 * print value of echo elements of a two-dimensional array for review
	 * the t array is split by split_length value to a two-dimensional array containing the elements of the t
	 * @param a a two-dimensional array
	 * @param out a print of object as like System.out or System.err
	 */
    public static void print(int[][] a, PrintStream out) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            sb.append("[");
            for (int j = 0; j < a[i].length; j++) sb.append(a[i][j]).append(", ");
            sb.replace(sb.length() + 0xFFFFFFFE, sb.length(), "], ");
        }
        sb.replace(sb.length() + 0xFFFFFFFE, sb.length(), "");
        out.print(sb.toString());
    }

    /**
	 * print value of echo elements of a two-dimensional array for review
	 * the t array is split by split_length value to a two-dimensional array containing the elements of the t
	 * @param a a two-dimensional array
	 * @param out a print of object as like System.out or System.err
	 */
    public static void print(short[][] a, PrintStream out) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            sb.append("[");
            for (int j = 0; j < a[i].length; j++) sb.append(a[i][j]).append(", ");
            sb.replace(sb.length() + 0xFFFFFFFE, sb.length(), "], ");
        }
        sb.replace(sb.length() + 0xFFFFFFFE, sb.length(), "");
        out.print(sb.toString());
    }

    /**
	 * print value of echo elements of a two-dimensional array for review
	 * the t array is split by split_length value to a two-dimensional array containing the elements of the t
	 * @param a a two-dimensional array
	 * @param out a print of object as like System.out or System.err
	 */
    public static void print(byte[][] a, PrintStream out) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            sb.append("[");
            for (int j = 0; j < a[i].length; j++) sb.append(a[i][j]).append(", ");
            sb.replace(sb.length() + 0xFFFFFFFE, sb.length(), "], ");
        }
        sb.replace(sb.length() + 0xFFFFFFFE, sb.length(), "");
        out.print(sb.toString());
    }

    /**
	 * print value of echo elements of a two-dimensional array for review
	 * the t array is split by split_length value to a two-dimensional array containing the elements of the t
	 * @param a a two-dimensional array
	 * @param out a print of object as like System.out or System.err
	 */
    public static void print(float[][] a, PrintStream out) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            sb.append("[");
            for (int j = 0; j < a[i].length; j++) sb.append(a[i][j]).append(", ");
            sb.replace(sb.length() + 0xFFFFFFFE, sb.length(), "], ");
        }
        sb.replace(sb.length() + 0xFFFFFFFE, sb.length(), "");
        out.print(sb.toString());
    }

    /**
	 * print value of echo elements of a two-dimensional array for review
	 * the t array is split by split_length value to a two-dimensional array containing the elements of the t
	 * @param a a two-dimensional array
	 * @param out a print of object as like System.out or System.err
	 */
    public static void print(double[][] a, PrintStream out) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            sb.append("[");
            for (int j = 0; j < a[i].length; j++) sb.append(a[i][j]).append(", ");
            sb.replace(sb.length() + 0xFFFFFFFE, sb.length(), "], ");
        }
        sb.replace(sb.length() + 0xFFFFFFFE, sb.length(), "");
        out.print(sb.toString());
    }

    /**
	 * the t array is split by split_length value to a two-dimensional array containing the elements of the t
	 * @param <T> left variable of declaration class name
	 * @param t a one-dimensional array
	 * @param split_length the one-dimensional array of split length
	 * @return an two-dimensional array containing the elements of the t
	 */
    @SuppressWarnings("unchecked")
    public static <T> T[][] split(T[] t, int split_length) {
        int size = (int) Math.ceil((double) t.length / split_length);
        T[][] result;
        if (size == 0x00000001) result = (T[][]) java.lang.reflect.Array.newInstance(t.getClass().getComponentType(), size, t.length); else {
            result = (T[][]) java.lang.reflect.Array.newInstance(t.getClass().getComponentType(), size, split_length);
            result[size + 0xFFFFFFFF] = (T[]) java.lang.reflect.Array.newInstance(t.getClass().getComponentType(), t.length - split_length * (size + 0xFFFFFFFF));
        }
        for (int i = 0x00000000; i < t.length; i++) result[i / split_length][i % split_length] = t[i];
        return result;
    }

    /**
	 * the t array is split by split_length value to a two-dimensional array containing the elements of the t
	 * @param a a one-dimensional array
	 * @param split_length the one-dimensional array of split length
	 * @return an two-dimensional array containing the elements of the t
	 */
    public static long[][] split(long[] a, int split_length) {
        int size = (int) Math.ceil((double) a.length / split_length);
        long[][] result;
        if (size == 0x00000001) result = new long[size][a.length]; else {
            result = new long[size][];
            for (int i = 0x00000000; i < size + 0xFFFFFFFF; i++) result[i] = new long[split_length];
            result[size + 0xFFFFFFFF] = new long[a.length - split_length * (size + 0xFFFFFFFF)];
        }
        for (int i = 0x00000000; i < a.length; i++) result[i / split_length][i % split_length] = a[i];
        return result;
    }

    /**
	 * the t array is split by split_length value to a two-dimensional array containing the elements of the t
	 * @param a a one-dimensional array
	 * @param split_length the one-dimensional array of split length
	 * @return an two-dimensional array containing the elements of the t
	 */
    public static int[][] split(int[] a, int split_length) {
        int size = (int) Math.ceil((double) a.length / split_length);
        int[][] result;
        if (size == 0x00000001) result = new int[size][a.length]; else {
            result = new int[size][];
            for (int i = 0x00000000; i < size + 0xFFFFFFFF; i++) result[i] = new int[split_length];
            result[size + 0xFFFFFFFF] = new int[a.length - split_length * (size + 0xFFFFFFFF)];
        }
        for (int i = 0x00000000; i < a.length; i++) result[i / split_length][i % split_length] = a[i];
        return result;
    }

    /**
	 * the t array is split by split_length value to a two-dimensional array containing the elements of the t
	 * @param a a one-dimensional array
	 * @param split_length the one-dimensional array of split length
	 * @return an two-dimensional array containing the elements of the t
	 */
    public static short[][] split(short[] a, int split_length) {
        int size = (int) Math.ceil((double) a.length / split_length);
        short[][] result;
        if (size == 0x00000001) result = new short[size][a.length]; else {
            result = new short[size][];
            for (int i = 0x00000000; i < size + 0xFFFFFFFF; i++) result[i] = new short[split_length];
            result[size + 0xFFFFFFFF] = new short[a.length - split_length * (size + 0xFFFFFFFF)];
        }
        for (int i = 0x00000000; i < a.length; i++) result[i / split_length][i % split_length] = a[i];
        return result;
    }

    /**
	 * the t array is split by split_length value to a two-dimensional array containing the elements of the t
	 * @param a a one-dimensional array
	 * @param split_length the one-dimensional array of split length
	 * @return an two-dimensional array containing the elements of the t
	 */
    public static byte[][] split(byte[] a, int split_length) {
        int size = (int) Math.ceil((double) a.length / split_length);
        byte[][] result;
        if (size == 0x00000001) result = new byte[size][a.length]; else {
            result = new byte[size][];
            for (int i = 0x00000000; i < size + 0xFFFFFFFF; i++) result[i] = new byte[split_length];
            result[size + 0xFFFFFFFF] = new byte[a.length - split_length * (size + 0xFFFFFFFF)];
        }
        for (int i = 0x00000000; i < a.length; i++) result[i / split_length][i % split_length] = a[i];
        return result;
    }

    /**
	 * the t array is split by split_length value to a two-dimensional array containing the elements of the t
	 * @param a a one-dimensional array
	 * @param split_length the one-dimensional array of split length
	 * @return an two-dimensional array containing the elements of the t
	 */
    public static float[][] split(float[] a, int split_length) {
        int size = (int) Math.ceil((double) a.length / split_length);
        float[][] result;
        if (size == 0x00000001) result = new float[size][a.length]; else {
            result = new float[size][];
            for (int i = 0x00000000; i < size + 0xFFFFFFFF; i++) result[i] = new float[split_length];
            result[size + 0xFFFFFFFF] = new float[a.length - split_length * (size + 0xFFFFFFFF)];
        }
        for (int i = 0x00000000; i < a.length; i++) result[i / split_length][i % split_length] = a[i];
        return result;
    }

    /**
	 * the t array is split by split_length value to a two-dimensional array containing the elements of the t
	 * @param a a one-dimensional array
	 * @param split_length the one-dimensional array of split length
	 * @return an two-dimensional array containing the elements of the t
	 */
    public static double[][] split(double[] a, int split_length) {
        int size = (int) Math.ceil((double) a.length / split_length);
        double[][] result;
        if (size == 0x00000001) result = new double[size][a.length]; else {
            result = new double[size][];
            for (int i = 0x00000000; i < size + 0xFFFFFFFF; i++) result[i] = new double[split_length];
            result[size + 0xFFFFFFFF] = new double[a.length - split_length * (size + 0xFFFFFFFF)];
        }
        for (int i = 0x00000000; i < a.length; i++) result[i / split_length][i % split_length] = a[i];
        return result;
    }
}
