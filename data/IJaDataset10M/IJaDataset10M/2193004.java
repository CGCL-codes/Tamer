package uk.co.rubox;

/**
* Validates moves for noughts and crosses.
*/
public class gameoxo implements game {

    gamelogic logic = new gamelogic();

    boolean last = false;

    public gameoxo() {
    }

    public boolean lastOK() {
        return last;
    }

    public String name() {
        return "Noughts and Crosses";
    }

    public String version() {
        return "0.0.0.1";
    }

    public String author() {
        return "Matthew Wilkes";
    }

    public int[][] availableSizes() {
        int[] opt1 = { 4, 4, 4 };
        int[] opt2 = { 5, 5, 5 };
        int[] opt3 = { 6, 6, 6 };
        int[][] opts = { opt1, opt2, opt2 };
        return opts;
    }

    public piece[][][] initial(int x, int y, int z) throws InvalidCubeSizeException {
        if (x == y && x == z) {
            if (x == 4 || x == 5 || x == 6) {
            } else {
                throw new InvalidCubeSizeException();
            }
        } else {
            throw new InvalidCubeSizeException();
        }
        piece[][][] cube = new piece[x][y][z];
        for (int i = 0; i < cube.length; i++) {
            for (int j = 0; j < cube[0].length; j++) {
                for (int k = 0; k < cube[0].length; k++) {
                    System.err.println("Creating (" + i + "," + j + "," + k + ")");
                    cube[i][j][k] = new piece();
                }
            }
        }
        return cube;
    }

    public transitionalpiece[][][] executeMove(piece[][][] gameboard, int movetype, int x1, int y1, int z1, int x2, int y2, int z2, piecestatus newstate) throws gamelogic.ImpossibleMoveException {
        transitionalpiece[][][] output;
        if (gameboard[x1][y1][z1].getStatus().getStatus() != uk.co.rubox.piecestatus.EMPTY) {
            System.err.println("NO WAY, JOSE!");
            output = logic.executeMove(gameboard, 0, x1, y1, z1, x2, y2, z2, newstate);
            last = false;
            return output;
        } else {
            if (movetype == 1) {
                try {
                    output = logic.executeMove(gameboard, movetype, x1, y1, z1, x2, y2, z2, newstate);
                    last = true;
                } catch (Exception e) {
                    output = logic.executeMove(gameboard, 0, x1, y1, z1, x2, y2, z2, newstate);
                    last = false;
                }
            } else {
                output = logic.executeMove(gameboard, 0, x1, y1, z1, x2, y2, z2, newstate);
                last = false;
            }
        }
        return output;
    }
}
