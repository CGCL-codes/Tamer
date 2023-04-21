package snake.core.exceptions;

public class OutOfBoardBoundsException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public OutOfBoardBoundsException() {
        super("Snake goes out of board");
    }
}
