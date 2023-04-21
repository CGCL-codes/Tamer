package arrays;

public class ArrayAllocation01 {

    public static void main() {
        int[] A = new int[5], B[] = new int[3][5];
        for (int i = 0; i < 5; ++i) {
            A[i] = A[i] + i + 1;
        }
        runningSum(A);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 5; ++j) {
                B[i][j] = i * j;
            }
        }
        for (int i = 0; i < 3; ++i) {
            runningSum(B[i]);
        }
    }

    static void runningSum(int[] X) {
        for (int i = 0; i < 4; ++i) {
            X[i + 1] = X[i + 1] + X[i];
        }
        System.out.println(X[4]);
    }
}
