package uebung01.ml.aufgabe04;

import static java.lang.System.out;

public class Sequence {

    public static void main(String[] args) {
        for (int n = 1; n <= 5; n++) {
            out.println("n = " + n);
            out.println("  recursive: " + recursive(n));
            out.println("  iterative: " + iterative(n));
            out.println("  explicit:  " + explicit(n));
        }
    }

    static int recursive(int n) {
        if (n == 1) return 5; else return recursive(n - 1) + 8;
    }

    static int iterative(int n) {
        int a1 = 5;
        int sum = 0;
        for (int i = 2; i <= n; i++) {
            sum += 8;
        }
        return a1 + sum;
    }

    static int explicit(int n) {
        return 8 * n - 3;
    }
}
