package ictk.boardgame;

/** this is thrown when you try to execute or add an illegal move to the
 *  game.
 */
public class IllegalMoveException extends MoveException {

    String moveString;

    public IllegalMoveException() {
        super();
    }

    public IllegalMoveException(String s) {
        super(s);
    }

    public IllegalMoveException(String s, Move _m) {
        super(s, _m);
    }

    /** if the move wasn't processed, or doesn't yet have sensible data
    *  the moveString might have been set.  This is basically a way for
    *  bad input values to be returned by parsers.
    */
    public void setMoveString(String m) {
        moveString = m;
    }

    /** returns a value that failed to be parsed into a legal move.
    */
    public String getMoveString() {
        return moveString;
    }

    public String toString() {
        if (moveString == null) return getMessage(); else return getMessage() + ": " + moveString;
    }
}
