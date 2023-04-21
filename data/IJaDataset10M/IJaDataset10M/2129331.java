package de.jdufner.sudoku.common.board;

import java.util.List;

/**
 * A block represents a unit of m x n cells. It spans about several columns and
 * rown and contains each literal one time.
 * 
 * @author <a href="mailto:jdufner@users.sf.net">Jürgen Dufner</a>
 * @since 0.1
 * @version $Revision: 120 $
 * @see <a
 *      href="http://sudopedia.org/wiki/Box">http://sudopedia.org/wiki/Box</a>
 */
public final class Box extends House {

    /**
   * @param sudoku
   *          Die Größe des Sudokus.
   * @param index
   *          Der Index des Blocks.
   * @param cells
   *          Eine Liste aller Zellen, die in diesem Block enthalten sind.
   */
    public Box(final SudokuSize sudokuSize, final int index, final List<Cell> cells) {
        super(sudokuSize, index, cells);
    }

    /**
   * @return
   */
    public int[] getColumnIndexes() {
        final int startIndex = (index % sudokuSize.getBoxHeight()) + sudokuSize.getBoxWidth();
        int[] columnIndexes = new int[sudokuSize.getBoxWidth()];
        for (int i = 0; i < sudokuSize.getBoxWidth(); i++) {
            columnIndexes[i] = startIndex + i;
        }
        return columnIndexes;
    }

    /**
   * @return
   */
    public int[] getRowIndexes() {
        final int startIndex = (index / sudokuSize.getBoxHeight()) * sudokuSize.getBoxHeight();
        int[] rowIndexes = new int[sudokuSize.getBoxHeight()];
        for (int i = 0; i < sudokuSize.getBoxHeight(); i++) {
            rowIndexes[i] = startIndex + i;
        }
        return rowIndexes;
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof Box) {
            return super.equals(other);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Block " + super.toString();
    }
}
