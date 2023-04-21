package org.sudoku.solver;

import java.text.MessageFormat;

/**
 * Defines common constants and utilities.
 * 
 * @author shivkumar cm
 * 
 */
public class Sudoku {

    private Sudoku() {
    }

    public static final int SUDOKU_BASE = 1;

    public static final int SUDOKU_MAX = 9;

    public static final int SUDOKU_SIDE = SUDOKU_MAX - SUDOKU_BASE + 1;

    public static final int GRID_SIZE = SUDOKU_MAX + 1;

    public static final int CELL_COUNT = SUDOKU_MAX * SUDOKU_MAX;

    public static final int CUBE_SIZE = SUDOKU_MAX / 3;

    /**
	 * Unknown cells are treated as a bit string. If the ith least significant
	 * big is set then it implies that the cell value could be i, for 1 &lt;= i
	 * &lt;= 9. Therefore, to begin with the blank value has all the 9 bits (as
	 * well as the 0th least significant bit for simplicity) is set to 1, i.e.,
	 * it is a bit string with ten 1's.
	 */
    public static final int BLANK = 0x3FF;

    public static final int UNKNOWN = 0;

    public static final char CHAR_BASE = '1';

    public static final char CHAR_MAX = '9';

    public static final char CHAR_BLANK = '0';

    /**
	 * If a cell value is successfully inferred, then it will have only one of
	 * the 9 bits (as well as the 0th least significant bit) set to 1. This
	 * array has in its ith position the numerical value of the bit string when
	 * a cell value is inferred to be i, for 1 &lt;= i &lt;= 9. The 0th position
	 * is unused.
	 */
    public static final int INFERRED[] = new int[] { 0, 3, 5, 9, 17, 33, 65, 129, 257, 513 };

    /**
	 * @return The index of the base of the corresponding cube for the given row
	 *         or cell index.
	 */
    public static int cubeBase(int rowCol) {
        return ((rowCol - 1) / 3) * 3 + 1;
    }

    public static final String MSG_WRONG_NUM_STRINGS = MessageFormat.format("Problem should have exactly {0} strings - one for each row.", new Object[] { SUDOKU_SIDE });

    public static final String MSG_WRONG_STRING_LENGTH = MessageFormat.format("Each row of the problem should of length {0} characters.", new Object[] { SUDOKU_SIDE });

    public static final String MSG_ILLEGAL_CHARACTER = MessageFormat.format("Only characters from ''{1}'' through ''{2}'' or the blank character ''{3}'' are allowed.", new Object[] { CHAR_BASE, CHAR_MAX, CHAR_BLANK });

    /**
	 * The index of the row element in the inferred array.
	 */
    public static final int ROW = 0;

    /**
	 * The index of the col element in the inferred array.
	 */
    public static final int COL = 1;

    /**
	 * The index of the digit element in the inferred array.
	 */
    public static final int DIGIT = 2;
}
