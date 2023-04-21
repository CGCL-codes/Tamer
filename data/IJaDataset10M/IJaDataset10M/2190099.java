package praktikumid.kaug3;

public class Kolmikud {

    static int[][] board = { { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 }, { 0, 1, 1, 1, 0 }, { 0, 0, 1, 0, 0 }, { 0, 0, 1, 1, 1 } };

    public static void main(String[] args) {
        int n = hKolmikud(board);
        System.out.println("kolmikuid: " + n);
    }

    static int hKolmikud(int[][] b) {
        return horisontaalneOtsing(b, 3, 0, 1);
    }

    static int horisontaalneOtsing(int[][] b, int n, int dr, int dc) {
        int result = 0;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (b[row][col] == 1) {
                    int counter = n - 1;
                    for (int x = col + 1; x < board[0].length; x++) {
                        if (b[row][x] == 1) {
                            counter--;
                        }
                    }
                    if (counter == 0 && col + counter + 1 < board[0].length && b[row][col + counter + 1] != 1) {
                        result++;
                    }
                }
            }
        }
        return result;
    }
}
