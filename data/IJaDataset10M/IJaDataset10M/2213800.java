package de.jdufner.sudoku.solver.ddt.sudoku_org_uk_daily;

import org.apache.log4j.Logger;
import de.jdufner.sudoku.common.board.Grid;
import de.jdufner.sudoku.common.factory.SudokuFactory;
import de.jdufner.sudoku.solver.ddt.AbstractSolverExcelTestCase;
import de.jdufner.sudoku.solver.service.Solver;

/**
 * @author <a href="mailto:jdufner@users.sf.net">Jürgen Dufner</a>
 * @since 0.1
 * @version $Revision: 120 $
 */
public final class SudokuOrgUkDailyTest extends AbstractSolverExcelTestCase {

    private static final Logger LOG = Logger.getLogger(SudokuOrgUkDailyTest.class);

    private String sudokuAsString;

    private boolean solveable;

    public void testStrategySolver() {
        Grid sudoku = SudokuFactory.INSTANCE.buildSudoku(sudokuAsString);
        Solver solver = getStrategySolver();
        Grid result = solver.solve(sudoku);
        if (LOG.isDebugEnabled()) {
            LOG.debug(result.toString());
        }
        assertEquals(solveable, result.isSolved());
    }

    public String getSudokuAsString() {
        return sudokuAsString;
    }

    public void setSudokuAsString(String sudokuAsString) {
        this.sudokuAsString = sudokuAsString;
    }

    public boolean isSolveable() {
        return solveable;
    }

    public void setSolveable(boolean solveable) {
        this.solveable = solveable;
    }
}
