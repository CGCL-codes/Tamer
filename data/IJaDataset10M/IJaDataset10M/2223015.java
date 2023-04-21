package exam11;

import java.util.Scanner;

/**
 * ��ӡ��ħ���󡱡���νħ������ָ����ķ������ÿһ�С�ÿһ�к�
�Խ���֮�;���ȡ����磬���ħ����Ϊ��
8 1 6
3 5 7
4 9 2
Ҫ���ӡ����1��n*n����Ȼ��ɵ�ħ����
 * @author Fantasy
 *
 */
public class Problem11 {

    private boolean[][] isFilled;

    private int[][] matrix;

    public Problem11(int n_matrix) {
        isFilled = new boolean[n_matrix][n_matrix];
        matrix = new int[n_matrix][n_matrix];
        for (int i = 0; i < n_matrix; i++) {
            for (int j = 0; j < n_matrix; j++) {
                isFilled[i][j] = true;
            }
        }
    }

    public boolean checkValid() {
        int sumRow = 0;
        int sumCol = 0;
        int sumDiagonalLeft = 0;
        int sumDiagonalRight = 0;
        for (int i = 0; i < matrix.length - 1; i++) {
            for (int j = 0; j < matrix.length - 1; j++) {
            }
        }
        return false;
    }

    public void input(int n) {
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < matrix.length; j++) {
                for (int k = 0; k < matrix.length; k++) {
                    if (isFilled[j][k] && n > 0) {
                        matrix[j][k] = i;
                        isFilled[j][k] = false;
                        input(n - 1);
                    }
                }
            }
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
        }
    }

    public static void main(String[] args) {
        int n_matrix = new Scanner(System.in).nextInt();
        int n = n_matrix * n_matrix;
        Problem11 app = new Problem11(n_matrix);
        app.input(n);
    }
}
