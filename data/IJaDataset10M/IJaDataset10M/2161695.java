package energyPuzzle;

import junit.framework.Assert;
import org.junit.Test;
import resources.Resources;

public class PuzzleTests {

    @Test
    public void newPuzzle() {
        final Puzzle puzzle = new Puzzle();
        Assert.assertEquals(Resources.getClearedPuzzle(), puzzle.toString());
    }

    @Test
    public void loadPuzzleFromString() {
        final Puzzle puzzle = new Puzzle(Resources.getNormalPuzzle());
        Assert.assertEquals(Resources.getNormalPuzzle(), puzzle.toString());
    }

    @Test
    public void movePuzzleUp() {
        final Puzzle puzzleUp = new Puzzle(Resources.getNormalPuzzle());
        puzzleUp.up();
        Assert.assertEquals(Resources.getNormalUpPuzzle(), puzzleUp.toString());
    }

    @Test
    public void movePuzzleDown() {
        final Puzzle puzzleDown = new Puzzle(Resources.getNormalPuzzle());
        puzzleDown.down();
        Assert.assertEquals(Resources.getNormalDownPuzzle(), puzzleDown.toString());
    }

    @Test
    public void movePuzzleLeft() {
        final Puzzle puzzleLeft = new Puzzle(Resources.getNormalPuzzle());
        puzzleLeft.left();
        Assert.assertEquals(Resources.getNormalLeftPuzzle(), puzzleLeft.toString());
    }

    @Test
    public void movePuzzleRight() {
        final Puzzle puzzleRight = new Puzzle(Resources.getNormalPuzzle());
        puzzleRight.right();
        Assert.assertEquals(Resources.getNormalRightPuzzle(), puzzleRight.toString());
    }

    @Test
    public void solve3PiecesPuzzle() {
        final Puzzle puzzle3Pieces = new Puzzle(Resources.getPuzzleToSolve3Pieces());
        Assert.assertTrue(puzzle3Pieces.isSolvable());
        puzzle3Pieces.down();
        puzzle3Pieces.clean();
        Assert.assertTrue(puzzle3Pieces.isSolved());
    }

    @Test
    public void solve4PiecesPuzzle() {
        final Puzzle puzzle4Pieces = new Puzzle(Resources.getPuzzleToSolv4Pieces());
        Assert.assertTrue(puzzle4Pieces.isSolvable());
        puzzle4Pieces.right();
        puzzle4Pieces.clean();
        Assert.assertTrue(puzzle4Pieces.isSolved());
    }

    @Test
    public void solve5PiecesPuzzle() {
        final Puzzle puzzle5Pieces = new Puzzle(Resources.getPuzzleToSolv5Pieces());
        Assert.assertTrue(puzzle5Pieces.isSolvable());
        puzzle5Pieces.up();
        puzzle5Pieces.clean();
        Assert.assertTrue(puzzle5Pieces.isSolved());
    }

    @Test
    public void testImpossiblePuzzle() {
        final Puzzle impossiblePuzzle = new Puzzle(Resources.getPuzzleImpossible());
        impossiblePuzzle.clean();
        Assert.assertFalse(impossiblePuzzle.isSolved());
        Assert.assertFalse(impossiblePuzzle.isSolvable());
    }

    @Test
    public void functionalTest() {
        final Puzzle simplePuzzle = new Puzzle(Resources.getSimplePuzzle());
        simplePuzzle.play("ddruul");
        Assert.assertTrue(simplePuzzle.isSolved());
    }
}
