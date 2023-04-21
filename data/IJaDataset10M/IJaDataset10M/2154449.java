package s15;

import java.util.Arrays;

interface QueenBoard {

    public int boardSize();

    public void putQueen(int row, int col);

    public void removeQueen(int row, int col);

    public boolean isSquareAttacked(int row, int col);
}

public class Queens {

    public static void solve(int n) {
        QueenBoard b = new QueenBoardGUI(n);
        if (isSolvable(b)) System.out.println("Found !\n" + b); else System.out.println("Not found !");
    }

    public static boolean isSolvable(QueenBoard b) {
        return isSolvableFromColumn(0, b);
    }

    public static boolean isSolvableFromColumn(int col, QueenBoard b) {
        System.out.println(b.boardSize());
        int n = b.boardSize();
        if (n == col) {
            b.toString();
            return true;
        } else {
            for (int i = 0; i < n; i++) {
                if (!b.isSquareAttacked(i, col)) {
                    b.putQueen(i, col);
                    if (!Queens.isSolvableFromColumn(col + 1, b)) {
                        b.removeQueen(i, col);
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        int n = 6;
        if (args.length != 0) n = Integer.parseInt(args[0]);
        solve(n);
    }
}

class QueenBoardBasic implements QueenBoard {

    int[] queensRow;

    boolean[] isRowUsed;

    boolean[] isDiagonal1Used;

    boolean[] isDiagonal2Used;

    int n;

    public QueenBoardBasic(int dim) {
        n = dim;
        queensRow = new int[n];
        Arrays.fill(queensRow, -1);
        isRowUsed = new boolean[n];
        isDiagonal1Used = new boolean[2 * n - 1];
        isDiagonal2Used = new boolean[2 * n - 1];
    }

    public int boardSize() {
        return n;
    }

    public void putQueen(int row, int col) {
        queensRow[col] = row;
        isRowUsed[row] = true;
        isDiagonal1Used[row + col] = true;
        isDiagonal2Used[row - col + n - 1] = true;
    }

    public void removeQueen(int row, int col) {
        isRowUsed[row] = false;
        isDiagonal1Used[row + col] = false;
        isDiagonal2Used[row - col + n - 1] = false;
    }

    public boolean isSquareAttacked(int row, int col) {
        System.out.println(row + "," + col);
        return isRowUsed[row] || isDiagonal1Used[row + col] || isDiagonal2Used[row - col + n - 1];
    }

    public String toString() {
        String res = "";
        for (int i = 0; i < n; i++, res += "\n") for (int j = 0; j < n; j++) res += (i == queensRow[j]) ? "X" : "-";
        return res;
    }
}
