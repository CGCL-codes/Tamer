package sudoku.solvers.trial;

import sudoku.*;
import sudoku.actions.*;
import java.util.Vector;

/**
 *
 * @author doug
 */
public class BowmanBingo extends TrialAndError {

    /**
     * Creates a Bowman Bingo solver without a tail.
     *
     * @param text The Text object to write the description to.
     *
     * @param view The view object to display the board.
     *
     * @param sud The Sudoku board.
     */
    public BowmanBingo(Text text, View view, Sudoku sud) {
        super(text, view, sud);
    }

    /**
     * Gives the name of the helper.
     *
     * @return The name of the helper.
     */
    @Override
    public String getHeader() {
        return "Bowman Bingo";
    }
}
