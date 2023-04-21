package uk.co.rubox;

/**
* Provides functions to manipulate the cube based on integer commands.
*/
public class gamelogic {

    private static final String[] desc = { "nothing", "set", "unset", "move", "switch", "rotate+", "rotate-" };

    public gamelogic() {
        return;
    }

    /**
	*  Provides the caller with a list of string descriptions for available operations.
	* @return Simple string descriptions for the moves possible.
	* @author M. Wilkes
	*/
    public String[] getDescriptors() {
        return desc;
    }

    /**
	*  Returns the int ID if you know the string description of a move.
	* @param descript The string as returned by getDescriptors
	* @return The index that identifies specified move
	* @author M. Wilkes	
	*/
    public int identifyMove(String descript) throws NonexistantMoveException {
        for (int i = 0; i < desc.length; i++) {
            if (desc[i].equals(descript)) {
                return i;
            }
        }
        throw new NonexistantMoveException();
    }

    public transitionalpiece[][][] executeMoveT(transitionalpiece[][][] resultant, int movetype, int x1, int y1, int z1, int x2, int y2, int z2, piecestatus newstate) throws ImpossibleMoveException {
        if (movetype == 0) {
            System.err.println("I'm not listening!");
            return resultant;
        }
        System.err.println("Ok, ok, you're the boss!");
        if (movetype == 1) {
            resultant[x1][y1][z1].newStatus(newstate);
        }
        if (movetype == 2) {
            resultant[x1][y1][z1].newStatus(uk.co.rubox.piecestatus.EMPTY);
        }
        if (movetype == 3) {
            resultant[x2][y2][z2].newStatus(resultant[x1][y1][z1].getOldStatus());
            resultant[x1][y1][z1].newStatus(uk.co.rubox.piecestatus.EMPTY);
        }
        if (movetype == 4) {
            resultant[x2][y2][z2].newStatus(resultant[x1][y1][z1].getOldStatus());
            resultant[x1][y1][z1].newStatus(resultant[x2][y2][z2].getOldStatus());
        }
        if (movetype == 5) {
            int face = 0;
            int index = 0;
            if (x1 == x2) {
                face = 0;
                index = x1;
                for (int n = resultant[index][0].length; n >= 0; n--) {
                    int o = resultant[index][0].length - n;
                    for (int m = -0; m < resultant[0].length; m++) {
                        resultant[index][m][o].newStatus(resultant[index][m][n].getOldStatus());
                    }
                }
            } else if (y1 == y2) {
                face = 1;
                index = y1;
                for (int n = resultant[0][index].length; n >= 0; n--) {
                    int o = resultant[0][index].length - n;
                    for (int m = -0; m < resultant[0].length; m++) {
                        resultant[o][index][m].newStatus(resultant[n][index][m].getOldStatus());
                    }
                }
            } else if (z1 == z2) {
                face = 2;
                index = z1;
                for (int n = resultant[index][0].length; n >= 0; n--) {
                    int o = resultant[index][0].length - n;
                    for (int m = -0; m < resultant[0].length; m++) {
                        resultant[o][m][index].newStatus(resultant[n][m][index].getOldStatus());
                    }
                }
            } else {
                throw new ImpossibleMoveException();
            }
        }
        if (movetype == 6) {
            int face = 0;
            int index = 0;
            if (x1 == x2) {
                face = 0;
                index = x1;
                for (int n = resultant[index][0].length; n >= 0; n--) {
                    int o = resultant[index][0].length - n;
                    for (int m = -0; m < resultant[0].length; m++) {
                        resultant[index][m][n].newStatus(resultant[index][m][o].getOldStatus());
                    }
                }
            } else if (y1 == y2) {
                face = 1;
                index = y1;
                for (int n = resultant[0][index].length; n >= 0; n--) {
                    int o = resultant[0][index].length - n;
                    for (int m = -0; m < resultant[0].length; m++) {
                        resultant[n][index][m].newStatus(resultant[o][index][m].getOldStatus());
                    }
                }
            } else if (z1 == z2) {
                face = 2;
                index = z1;
                for (int n = resultant[index][0].length; n >= 0; n--) {
                    int o = resultant[index][0].length - n;
                    for (int m = -0; m < resultant[0].length; m++) {
                        resultant[n][m][index].newStatus(resultant[o][m][index].getOldStatus());
                    }
                }
            } else {
                throw new ImpossibleMoveException();
            }
        }
        return resultant;
    }

    /**
	*  Runs a move known to the system on a grid, and returns the transitional form.
	* This is the real workhorse of the game logic module, it will be called every time there is a move made.
	* @param gameboard The current state of the board
	* @param movetype The integer ID of the move to be made
	* @param x1 The x index of the first piece involved in the move.
	* @param y1 The y index of the first piece involved in the move.
	* @param z1 The z index of the first piece involved in the move.	
	* @param x2 The x index of the second piece involved in the move.
	* @param y2 The y index of the second piece involved in the move.
	* @param z2 The z index of the second piece involved in the move.	
	* @param newstate The state for the piece being set to become
	* @return The transitional form of the board after the move is made.
	* @author M. Wilkes	
	*/
    public transitionalpiece[][][] executeMove(piece[][][] gameboard, int movetype, int x1, int y1, int z1, int x2, int y2, int z2, piecestatus newstate) throws ImpossibleMoveException {
        transitionalpiece[][][] resultant = new transitionalpiece[gameboard.length][gameboard[0].length][gameboard[0][0].length];
        for (int i = 0; i < gameboard.length; i++) {
            for (int j = 0; j < gameboard.length; j++) {
                for (int k = 0; k < gameboard[0][0].length; k++) {
                    resultant[i][j][k] = new transitionalpiece(gameboard[i][j][k]);
                }
            }
        }
        return this.executeMoveT(resultant, movetype, x1, y1, z1, x2, y2, z2, newstate);
    }

    public class NonexistantMoveException extends Exception {
    }

    public static class ImpossibleMoveException extends Exception {
    }
}
