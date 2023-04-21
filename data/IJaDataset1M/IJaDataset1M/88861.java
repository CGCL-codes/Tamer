package sudoku.solvers.difficult;

import sudoku.*;
import sudoku.actions.*;
import java.util.Vector;

/**
 *
 * @author doug
 */
public class Guardians extends Difficult {

    /**
     * Creates a Guardians solver without a tail.
     *
     * @param text The Text object to write the description to.
     *
     * @param view The view object to display the board.
     *
     * @param sud The Sudoku board.
     */
    public Guardians(Text text, View view, Sudoku sud) {
        super(text, view, sud);
    }

    /**
     * Gives the name of the helper.
     *
     * @return The name of the helper.
     */
    @Override
    public String getHeader() {
        return "Guardians";
    }
}
