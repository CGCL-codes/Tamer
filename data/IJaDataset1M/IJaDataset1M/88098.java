package sorting;

/**
 * An insertion sort demonstration algorithm
 * SortAlgorithm.java, Thu Oct 27 10:32:35 1994
 *
 * @author Jason Harrison@cs.ubc.ca
 * @version 	1.0, 23 Jun 1995
 *
 */
class InsertionSort {

    private static int length = 4000;

    private static byte[] array = new byte[length];

    private static int time = 0;

    void sort(byte a[]) throws Exception {
        for (int i = 1; i < a.length; i++) {
            int j = i;
            byte B = a[i];
            while ((j > 0) && (a[j - 1] > B)) {
                a[j] = a[j - 1];
                j--;
            }
            a[j] = B;
        }
    }

    public static void main(String[] args) {
        execute();
    }

    public static void execute() {
        for (int loop = 0; loop < array.length; loop++) {
            array[loop] = (byte) ((byte) array.length - loop);
        }
        time = (int) System.currentTimeMillis();
        try {
            new InsertionSort().sort(array);
        } catch (Exception e) {
            System.out.println(e);
        }
        time = (int) System.currentTimeMillis() - time;
    }

    private static void generateResults() {
        takatuka.vm.VM.printFreeMemory();
        System.out.print("Total time taken to quicksort ");
        System.out.print(time);
        System.out.print("ms, length=");
        System.out.print(length);
    }
}
