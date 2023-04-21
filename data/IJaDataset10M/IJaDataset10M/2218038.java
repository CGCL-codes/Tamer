package net.jankenpoi.sudokuki.solver;

import static org.junit.Assert.*;
import net.jankenpoi.sudokuki.model.GridModel;
import org.junit.Test;

public class BruteForceGridSolverTest {

    public static final String strProblemGrid1 = "" + "402006000" + "013002070" + "000000000" + "006090020" + "007005000" + "000020504" + "590080000" + "004070009" + "600300002";

    public static final String strSolutionGrid1 = "" + "482716395" + "913542678" + "765938241" + "856493127" + "247165983" + "139827564" + "591284736" + "324671859" + "678359412";

    public static final String strProblemGridDifficult = "" + "002009050" + "400060000" + "003100700" + "051700000" + "209000000" + "000080004" + "010350000" + "000000307" + "004000020";

    public static final String strSolutionDifficult = "" + "172439658" + "498567132" + "563128749" + "851794263" + "249613875" + "736285914" + "917352486" + "625841397" + "384976521";

    public static final String strProblemGridDiabolic = "" + "900001002" + "080605070" + "000008000" + "453000090" + "000070000" + "070000521" + "000900000" + "020107050" + "300200006";

    public static final String strSolutionGridDiabolic = "" + "935741862" + "184625379" + "762398145" + "453812697" + "216579438" + "879463521" + "547986213" + "628137954" + "391254786";

    GridModel problemGrid1 = new GridModel(strProblemGrid1);

    GridModel solutionGrid1 = new GridModel(strSolutionGrid1);

    GridModel problemGridDifficult = new GridModel(strProblemGridDifficult);

    GridModel solutionGridDifficult = new GridModel(strSolutionDifficult);

    GridModel problemGridDiabolic = new GridModel(strProblemGridDiabolic);

    GridModel solutionGridDiabolic = new GridModel(strSolutionGridDiabolic);

    @Test
    public void testCopyCurrentFlagsToNextPosition() {
        BruteForceGridSolver solver = new BruteForceGridSolver(problemGrid1);
        solver.copyCurrentFlagsToNextPosition();
        solver.forwardToNextPosition();
        int currentIndex = solver.getCurrentIndex();
        int[] cellShadowMemory = solver.getCellShadowMemory();
        assertTrue(currentIndex - GridSolver.GRID_LENGTH >= 0);
        for (int i = currentIndex - GridSolver.GRID_LENGTH; i < currentIndex; i++) {
            assertEquals(cellShadowMemory[i], cellShadowMemory[i + GridSolver.GRID_LENGTH]);
        }
    }

    @Test
    public void testResolveGrid1() {
        GridSolver solver = new BruteForceGridSolver(problemGrid1);
        GridSolution solution = solver.resolve();
        assertTrue(solution.isSolved());
        GridModel solGrid = solution.getSolutionGrid();
        for (int li = 0; li < 9; li++) {
            for (int co = 0; co < 9; co++) {
                System.out.print(solGrid.getValueAt(li, co));
            }
        }
        System.out.println();
        assertTrue(areAllValuesEqual(solution.getSolutionGrid(), solutionGrid1));
    }

    @Test
    public void testResolveDifficultGrid() {
        GridSolver solver = new BruteForceGridSolver(problemGridDifficult);
        GridSolution solution = solver.resolve();
        assertTrue(solution.isSolved());
        GridModel solGrid = solution.getSolutionGrid();
        for (int li = 0; li < 9; li++) {
            for (int co = 0; co < 9; co++) {
                System.out.print(solGrid.getValueAt(li, co));
            }
        }
        System.out.println();
        assertTrue(areAllValuesEqual(solution.getSolutionGrid(), solutionGridDifficult));
    }

    @Test
    public void testResolveDiabolicGrid() {
        GridSolver solver = new BruteForceGridSolver(problemGridDiabolic);
        System.out.println();
        System.out.println("BruteForceGridSolverTest.testResolveDifficultGrid() *** start test ***");
        System.out.println();
        GridSolution solution = solver.resolve();
        assertTrue(solution.isSolved());
        GridModel solGrid = solution.getSolutionGrid();
        for (int li = 0; li < 9; li++) {
            for (int co = 0; co < 9; co++) {
                System.out.print(solGrid.getValueAt(li, co));
            }
        }
        System.out.println();
        assertTrue(areAllValuesEqual(solution.getSolutionGrid(), solutionGridDiabolic));
    }

    @Test
    public void testResolve10RandomGrids() {
        for (int i = 0; i < 10; i++) {
            GridSolver solver = new BruteForceGridSolver(new GridModel());
            GridSolution solution = solver.resolve();
            assertTrue(solution.isSolved());
        }
    }

    private boolean areAllValuesEqual(GridModel solutionGrid, GridModel otherGrid) {
        int[] solCells = solutionGrid.cloneCellInfosAsInts();
        int[] otherCells = otherGrid.cloneCellInfosAsInts();
        boolean allEqual = true;
        for (int li = 0; li < 9; li++) {
            for (int co = 0; co < 9; co++) {
                allEqual &= (solCells[9 * li + co] == otherCells[9 * li + co]);
            }
        }
        return allEqual;
    }
}
