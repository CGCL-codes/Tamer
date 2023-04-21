package org.vizzini.game.boardgame.chess.action;

import junit.framework.TestSuite;
import org.vizzini.game.IntegerPosition;
import org.vizzini.game.boardgame.action.AbstractGridBoardActionTest;
import org.vizzini.game.boardgame.chess.IChessAgent;
import org.vizzini.game.boardgame.chess.IChessEnvironment;
import org.vizzini.game.boardgame.chess.TestData;
import org.vizzini.util.TestFinder;

/**
 * Provides unit tests for the <code>EnPassantCaptureAction</code> class.
 *
 * <p>By default, all test methods (methods names beginning with <code>
 * test</code>) are run. Run individual tests from the command line using the
 * <code>main()</code> method. Specify individual test methods to run using the
 * <code>suite()</code> method.</p>
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @see      TestFinder
 * @since    v0.3
 */
public class EnPassantCaptureActionTest extends AbstractGridBoardActionTest {

    /**
     * Construct this object with the given parameter.
     *
     * @param  method  Method to run.
     *
     * @since  v0.3
     */
    public EnPassantCaptureActionTest(String method) {
        super(method);
    }

    /**
     * Application method.
     *
     * @param  args  Application arguments.
     *
     * @since  v0.3
     */
    public static void main(String[] args) {
        TestFinder.getInstance().run(EnPassantCaptureActionTest.class, args);
    }

    /**
     * @return  a suite of tests to run.
     *
     * @since   v0.3
     */
    public static TestSuite suite() {
        TestSuite suite = new TestSuite(EnPassantCaptureActionTest.class);
        return suite;
    }

    /**
     * Test the <code>get()</code> method.
     *
     * @throws  IllegalAccessException  if there is an illegal access.
     * @throws  InstantiationException  if there is an instantiation problem.
     *
     * @since   v0.3
     */
    public void testGet() throws InstantiationException, IllegalAccessException {
        IChessEnvironment board = TestData.createBoard(isBoardWritten());
        IChessAgent agent = (IChessAgent) board.getAgentCollection().findByName("white");
        IntegerPosition from = IntegerPosition.get(0, 0, 0);
        IntegerPosition capture = IntegerPosition.get(1, 1, 1);
        IntegerPosition to = IntegerPosition.get(2, 2, 2);
        EnPassantCaptureAction action = EnPassantCaptureAction.get(board, agent, from, capture, to);
        assertNotNull(action);
        assertEquals(board, action.getEnvironment());
        assertEquals(agent, action.getAgent());
        assertEquals(from, action.getFromPosition());
        assertEquals(capture, action.getCapturePosition());
        assertEquals(to, action.getToPosition());
        try {
            EnPassantCaptureAction.get(null, agent, from, capture, to);
            fail("Should have thrown an exception");
        } catch (Exception e) {
            assertTrue(true);
        }
        try {
            EnPassantCaptureAction.get(board, null, from, capture, to);
            fail("Should have thrown an exception");
        } catch (Exception e) {
            assertTrue(true);
        }
        try {
            EnPassantCaptureAction.get(board, agent, null, capture, to);
            fail("Should have thrown an exception");
        } catch (Exception e) {
            assertTrue(true);
        }
        try {
            EnPassantCaptureAction.get(board, agent, from, null, to);
            fail("Should have thrown an exception");
        } catch (Exception e) {
            assertTrue(true);
        }
        try {
            EnPassantCaptureAction.get(board, agent, from, capture, null);
            fail("Should have thrown an exception");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    /**
     * @return  a flag indicating if the board should be written upon creation.
     *
     * @since   v0.3
     */
    protected boolean isBoardWritten() {
        return false;
    }
}
