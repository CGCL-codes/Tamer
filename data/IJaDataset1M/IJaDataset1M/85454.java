package net.sourceforge.pseudoq.model;

import junit.framework.*;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import net.sourceforge.pseudoq.solver.Solution;

/**
 * JUnit test class for {@link Puzzle}.
 * @author <a href="http://sourceforge.net/users/stevensa">Andrew Stevens</a>
 */
public class PuzzleTest extends TestCase {

    public PuzzleTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(PuzzleTest.class);
        return suite;
    }

    /**
     * Test of getGrid method, of class net.sourceforge.pseudoq.model.Puzzle.
     */
    public void testGetGrid() {
        System.out.println("testGetGrid");
        Puzzle puzzle = new PuzzleImpl();
        Grid grid = new Grid(1);
        ((PuzzleImpl) puzzle).setGrid(grid);
        assertEquals(grid, puzzle.getGrid());
    }

    /**
     * Test of getGivens method, of class net.sourceforge.pseudoq.model.Puzzle.
     */
    public void testGetGivens() {
        System.out.println("testGetGivens");
        Puzzle puzzle = new PuzzleImpl();
        Set<Coordinate> givens = new HashSet<Coordinate>();
        ((PuzzleImpl) puzzle).setGivens(givens);
        assertEquals(givens, puzzle.getGivens());
    }

    /**
     * Test of solution property, of class net.sourceforge.pseudoq.model.Puzzle.
     */
    public void testPropertySolution() {
        System.out.println("testPropertySolution");
        Puzzle puzzle = new PuzzleImpl();
        Solution solution = new Solution();
        puzzle.setSolution(solution);
        assertEquals(solution, puzzle.getSolution());
    }

    /**
     * Test of filename property, of class net.sourceforge.pseudoq.model.Puzzle.
     */
    public void testPropertyFilename() {
        System.out.println("testPropertyFilename");
        Puzzle puzzle = new PuzzleImpl();
        puzzle.setFilename("someFile.pdq");
        assertEquals("someFile.pdq", puzzle.getFilename());
    }

    /**
     * Test of isComplete method, of class net.sourceforge.pseudoq.model.Puzzle.
     */
    public void testIsComplete() {
        System.out.println("testIsComplete");
        Puzzle puzzle = new PuzzleImpl();
        Grid grid = new Grid(2);
        grid.put(new Coordinate(0, 1), Integer.valueOf(0));
        grid.put(new Coordinate(1, 0), Integer.valueOf(1));
        grid.put(new Coordinate(1, 1), Integer.valueOf(2));
        ((PuzzleImpl) puzzle).setGrid(grid);
        assertFalse(puzzle.isComplete());
        grid.put(new Coordinate(0, 1), Integer.valueOf(3));
        assertTrue(puzzle.isComplete());
    }

    /**
     * Test of isSolved method, of class net.sourceforge.pseudoq.model.Puzzle.
     */
    public void testIsSolved() {
        System.out.println("testIsSolved");
        Puzzle puzzle = new PuzzleImpl();
        assertFalse(puzzle.isSolved());
        puzzle.setSolution(new Solution());
        assertTrue(puzzle.isSolved());
    }

    /**
     * Test of isPlayed method, of class net.sourceforge.pseudoq.model.Puzzle.
     */
    public void testIsPlayed() {
        System.out.println("testIsPlayed");
        Puzzle puzzle = new PuzzleImpl();
        Grid grid = new Grid(2);
        grid.put(new Coordinate(0, 1), Integer.valueOf(0));
        grid.put(new Coordinate(1, 0), Integer.valueOf(0));
        grid.put(new Coordinate(1, 1), Integer.valueOf(1));
        Set<Coordinate> givens = new HashSet<Coordinate>();
        givens.add(new Coordinate(1, 1));
        ((PuzzleImpl) puzzle).setGrid(grid);
        ((PuzzleImpl) puzzle).setGivens(givens);
        assertFalse(puzzle.isPlayed());
        grid.put(new Coordinate(0, 1), Integer.valueOf(2));
        assertTrue(puzzle.isPlayed());
        grid.put(new Coordinate(1, 0), Integer.valueOf(3));
        assertTrue(puzzle.isPlayed());
    }

    /**
     * Test of reset method, of class net.sourceforge.pseudoq.model.Puzzle.
     */
    public void testReset() {
        System.out.println("testReset");
        Puzzle puzzle = new PuzzleImpl();
        Grid grid = new Grid(2);
        grid.put(new Coordinate(0, 1), 0);
        grid.put(new Coordinate(1, 0), 1);
        grid.put(new Coordinate(1, 1), 2);
        ((PuzzleImpl) puzzle).setGrid(grid);
        puzzle.reset();
        assertNull(puzzle.getGrid().get(new Coordinate(0, 0)));
        assertEquals(Integer.valueOf(0), puzzle.getGrid().get(new Coordinate(0, 1)));
        assertEquals(Integer.valueOf(0), puzzle.getGrid().get(new Coordinate(1, 0)));
        assertEquals(Integer.valueOf(0), puzzle.getGrid().get(new Coordinate(1, 1)));
    }

    /**
     * Test of resetToGivens method, of class net.sourceforge.pseudoq.model.Puzzle.
     */
    public void testResetToGivens() {
        System.out.println("testResetToGivens");
        Puzzle puzzle = new PuzzleImpl();
        Grid grid = new Grid(2);
        grid.put(new Coordinate(0, 1), 0);
        grid.put(new Coordinate(1, 0), 1);
        grid.put(new Coordinate(1, 1), 2);
        ((PuzzleImpl) puzzle).setGrid(grid);
        Set<Coordinate> givens = new HashSet<Coordinate>();
        givens.add(new Coordinate(1, 1));
        ((PuzzleImpl) puzzle).setGivens(givens);
        puzzle.resetToGivens();
        assertNull(puzzle.getGrid().get(new Coordinate(0, 0)));
        assertEquals(Integer.valueOf(0), puzzle.getGrid().get(new Coordinate(0, 1)));
        assertEquals(Integer.valueOf(0), puzzle.getGrid().get(new Coordinate(1, 0)));
        assertEquals(Integer.valueOf(2), puzzle.getGrid().get(new Coordinate(1, 1)));
    }

    /**
     * Dummy implementation of abstract class net.sourceforge.pseudoq.model.Puzzle.
     */
    private class PuzzleImpl extends Puzzle {

        public PuzzleTypeEnum getType() {
            return null;
        }

        public int getMaxInt() {
            return 0;
        }

        public int getSize() {
            return 0;
        }

        public Map<String, Region> getRegions() {
            return null;
        }

        public void setGrid(Grid grid) {
            this.grid = grid;
        }

        public void setGivens(Set<Coordinate> givens) {
            this.givens = givens;
        }
    }
}
