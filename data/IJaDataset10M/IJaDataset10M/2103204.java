package jp.seraph.sample.etc;

public class Problem3 {

    public static void main(String[] args) {
        int[] a = { 1, -2, 3, -4, 5 };
        int[] b = negativeToZero(a);
        int[] c = positiveToZero(a);
        for (int t : b) System.out.print(t + ", ");
    }

    static int[] negativeToZero(int[] a) {
        int[] b = (int[]) a.clone();
        for (int i = 0; i < b.length; i++) {
            if (b[i] < 0) b[i] = 0;
        }
        return b;
    }

    static int[] positiveToZero(int[] a) {
        int[] b = (int[]) a.clone();
        for (int i = 0; i < b.length; i++) {
            if (b[i] > 0) b[i] = 0;
        }
        return b;
    }
}
