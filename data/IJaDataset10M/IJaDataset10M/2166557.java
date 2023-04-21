package cn.evilelf.sort;

import static cn.evilelf.sort.SortUtil.reverse;
import static cn.evilelf.sort.SortUtil.swap;
import java.util.Random;

public class Quick {

    private static final Random RND = new Random();

    private static int partition(int[] array, int begin, int end) {
        int index = begin + RND.nextInt(end - begin + 1);
        int pivot = array[index];
        swap(array, index, end);
        for (int i = index = begin; i < end; ++i) {
            if (array[i] <= pivot) {
                swap(array, index++, i);
            }
        }
        swap(array, index, end);
        return index;
    }

    private static int[] qsort(int[] array, int begin, int end) {
        if (end > begin) {
            int index = partition(array, begin, end);
            qsort(array, begin, index - 1);
            qsort(array, index + 1, end);
        }
        return array;
    }

    public static int[] sort(int[] array) {
        return qsort(array, 0, array.length - 1);
    }

    private static int partition(long[] array, int begin, int end) {
        int index = begin + RND.nextInt(end - begin + 1);
        long pivot = array[index];
        swap(array, index, end);
        for (int i = index = begin; i < end; ++i) {
            if (array[i] <= pivot) {
                swap(array, index++, i);
            }
        }
        swap(array, index, end);
        return index;
    }

    private static long[] qsort(long[] array, int begin, int end) {
        if (end > begin) {
            int index = partition(array, begin, end);
            qsort(array, begin, index - 1);
            qsort(array, index + 1, end);
        }
        return array;
    }

    public static long[] sort(long[] array) {
        return qsort(array, 0, array.length - 1);
    }

    private static int partition(float[] array, int begin, int end) {
        int index = begin + RND.nextInt(end - begin + 1);
        float pivot = array[index];
        swap(array, index, end);
        for (int i = index = begin; i < end; ++i) {
            if (array[i] <= pivot) {
                swap(array, index++, i);
            }
        }
        swap(array, index, end);
        return index;
    }

    private static float[] qsort(float[] array, int begin, int end) {
        if (end > begin) {
            int index = partition(array, begin, end);
            qsort(array, begin, index - 1);
            qsort(array, index + 1, end);
        }
        return array;
    }

    public static float[] sort(float[] array) {
        return qsort(array, 0, array.length - 1);
    }

    private static int partition(double[] array, int begin, int end) {
        int index = begin + RND.nextInt(end - begin + 1);
        double pivot = array[index];
        swap(array, index, end);
        for (int i = index = begin; i < end; ++i) {
            if (array[i] <= pivot) {
                swap(array, index++, i);
            }
        }
        swap(array, index, end);
        return index;
    }

    private static double[] qsort(double[] array, int begin, int end) {
        if (end > begin) {
            int index = partition(array, begin, end);
            qsort(array, begin, index - 1);
            qsort(array, index + 1, end);
        }
        return array;
    }

    public static double[] sort(double[] array) {
        return qsort(array, 0, array.length - 1);
    }

    @SuppressWarnings("unchecked")
    private static int partition(Comparable[] array, int begin, int end) {
        int index = begin + RND.nextInt(end - begin + 1);
        Comparable pivot = array[index];
        swap(array, index, end);
        for (int i = index = begin; i < end; ++i) {
            if (array[i].compareTo(pivot) <= 0) {
                swap(array, index++, i);
            }
        }
        swap(array, index, end);
        return index;
    }

    @SuppressWarnings("unchecked")
    private static Comparable[] qsort(Comparable[] array, int begin, int end) {
        if (end > begin) {
            int index = partition(array, begin, end);
            qsort(array, begin, index - 1);
            qsort(array, index + 1, end);
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    public static Comparable[] sort(Comparable[] array) {
        return qsort(array, 0, array.length - 1);
    }

    public static void main(String[] args) {
        String[] array = { "bad", "mum", "fuck", "love", "chuanshiji", "from" };
        reverse(sort(array));
        for (int i = 0; i < array.length; ++i) {
            System.out.println(array[i]);
        }
    }
}
