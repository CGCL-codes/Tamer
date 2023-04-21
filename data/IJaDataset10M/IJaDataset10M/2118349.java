package ch.unisi.inf.pfii.teamred.pacman.test;

import java.util.ArrayList;
import junit.framework.TestCase;
import ch.unisi.inf.pfii.teamred.pacman.model.Block;
import ch.unisi.inf.pfii.teamred.pacman.model.Board;
import ch.unisi.inf.pfii.teamred.pacman.model.Creature;
import ch.unisi.inf.pfii.teamred.pacman.model.Direction;
import ch.unisi.inf.pfii.teamred.pacman.model.Floor;
import ch.unisi.inf.pfii.teamred.pacman.model.Pacman;
import ch.unisi.inf.pfii.teamred.pacman.model.Pill;
import ch.unisi.inf.pfii.teamred.pacman.model.Position;

/**
 * @author luca.vignola@lu.unisi.ch
 * 
 */
public final class BoardTest extends TestCase {

    private Board board;

    private Block[][] blocks;

    private ArrayList<Creature> creatures;

    private Pacman pacman;

    protected final void setUp() throws Exception {
        super.setUp();
        int length = 1;
        blocks = new Block[length][length];
        blocks[0][0] = new Floor();
        creatures = new ArrayList<Creature>();
        Position pacmanPosition = new Position(0, 0);
        pacman = new Pacman(pacmanPosition, Direction.STOP);
        creatures.add(pacman);
        board = new Board(blocks, creatures);
    }

    public final void testBoard() {
        assertSame(blocks, board.getBlocks());
        assertSame(creatures, board.getCreatures());
    }

    public final void testGetHeight() {
        assertEquals(blocks.length, board.getWidth());
    }

    public final void testGetWidth() {
        assertEquals(blocks[0].length, board.getHeight());
    }

    public final void testCollisionHappened() {
        assertFalse(board.collisionHappened());
    }

    public final void testGetPacman() {
        assertSame(pacman, board.getPacman());
        creatures.remove(pacman);
        assertNull(board.getPacman());
    }

    public final void testToString() {
        assertEquals("P\n", board.toString());
    }

    public final void testNoMoreItems() {
        assertTrue(board.noMoreItemsOnBoard());
        board.getBlocks()[0][0] = new Floor(new Pill());
        assertFalse(board.noMoreItemsOnBoard());
    }
}
