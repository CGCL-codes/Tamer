package hotgammon.view.helper;

import hotgammon.domain.*;
import java.util.*;

/** A test stub game instance that hardwires some basic behaviour -
 * enough to make it useful for testing.
 * 
 * Author Henrik B�rbak Christensen
 */
class FixedSetupGame implements Game {

    Location loneRiderHere1, loneRiderHere2;

    public FixedSetupGame() {
        loneRiderHere1 = Location.B12;
        loneRiderHere2 = Location.R1;
        newGame();
    }

    int movesLeft;

    public void newGame() {
        movesLeft = 2;
        turn = 0;
        fireDieRollChange();
        fireBoardChange();
    }

    public Color getColor(Location location) {
        if (location == loneRiderHere1) return Color.BLACK;
        if (location == loneRiderHere2) return Color.RED;
        return Color.NONE;
    }

    public int getCount(Location location) {
        int sum = 0;
        if (location == loneRiderHere1) sum++;
        if (location == loneRiderHere2) sum++;
        return sum;
    }

    public boolean move(Location from, Location to) {
        if (to != Location.B3 && to != Location.R3) {
            if (from == loneRiderHere1) {
                loneRiderHere1 = to;
            } else if (from == loneRiderHere2) {
                loneRiderHere2 = to;
            }
            System.out.println("GAME: moving from " + from + " to " + to);
        } else {
            System.out.println("GAME: Moving to B3/R3 is illegal (testing purposes)");
            return false;
        }
        movesLeft--;
        fireBoardChange();
        return true;
    }

    boolean tictac = true;

    public Color getPlayerInTurn() {
        if (movesLeft == 0) return Color.NONE;
        if (tictac) return Color.RED;
        return Color.BLACK;
    }

    public int getNumberOfMovesLeft() {
        return movesLeft;
    }

    private GameListener gameListener = null;

    public void addGameListener(GameListener gl) {
        gameListener = gl;
    }

    int turn;

    public void nextTurn() {
        turn++;
        movesLeft = 2;
        tictac = !tictac;
        System.out.println("nextTurn: " + turn);
        fireDieRollChange();
        fireBoardChange();
    }

    void fireDieRollChange() {
        if (gameListener != null) gameListener.diceRolled();
    }

    void fireBoardChange() {
        if (gameListener != null) gameListener.boardChange();
    }

    public int[] diceThrown() {
        switch(turn % 5) {
            case 1:
                return new int[] { 2, 3 };
            case 2:
                return new int[] { 4, 4 };
            case 3:
                return new int[] { 3, 6 };
            case 4:
                return new int[] { 6, 1 };
            case 0:
                return new int[] { 5, 3 };
            default:
                return new int[] { 2, 2 };
        }
    }

    /** return game end status. Testing game never ends.
   */
    public Color winner() {
        if (getCount(Location.B_BEAR_OFF) > 0) return Color.BLACK; else if (getCount(Location.R_BEAR_OFF) > 0) return Color.RED; else return Color.NONE;
    }

    public int[] diceValuesLeft() {
        int[] v = { 1 };
        return v;
    }

    private Location[] allLocations = { Location.B1, Location.B2, Location.B3, Location.B4, Location.B5, Location.B6, Location.B7, Location.B8, Location.B9, Location.B10, Location.B11, Location.B12, Location.R1, Location.R2, Location.R3, Location.R4, Location.R5, Location.R6, Location.R7, Location.R8, Location.R9, Location.R10, Location.R11, Location.R12, Location.R_BEAR_OFF, Location.B_BEAR_OFF, Location.R_BAR, Location.B_BAR };

    class LocationIterator implements Iterator<Location> {

        private int index;

        Location[] whichToIterate;

        LocationIterator(Location[] which) {
            whichToIterate = which;
            index = 0;
        }

        public boolean hasNext() {
            return index < whichToIterate.length;
        }

        public Location next() {
            return whichToIterate[index++];
        }

        public void remove() {
            throw new UnsupportedOperationException("Locations cannot be removed.");
        }
    }

    public Iterator<Location> boardIterator() {
        return new LocationIterator(allLocations);
    }
}
