package de.jdufner.sudoku.common.board;

import java.util.List;

/**
 * Die Nebendiagonale läuft von unten links nach oben rechts.
 * 
 * @author <a href="mailto:jdufner@users.sf.net">Jürgen Dufner</a>
 * @since 2010-01-15
 * @version $Revision: 120 $
 */
public final class SecondaryDiagonal extends House {

    public SecondaryDiagonal(final SudokuSize sudokuSize, final int index, final List<Cell> cells) {
        super(sudokuSize, index, cells);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof SecondaryDiagonal) {
            return super.equals(other);
        }
        return false;
    }

    @Override
    public String toString() {
        return "SecondaryDiagonal " + super.toString();
    }
}
