package csc485.pg4.backgammon.gameFramework;

public class NoException extends MoveException {

    private Board theBoard;

    public NoException(Board board) {
        super.message = "No exceptions found";
        this.theBoard = board;
    }

    @Override
    public String toString() {
        return theBoard.toString();
    }
}
