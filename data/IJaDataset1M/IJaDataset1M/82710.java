package org.vizzini.example.tictactoe;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.vizzini.game.boardgame.IGridBoard;
import org.vizzini.ui.ApplicationSupport;
import org.vizzini.ui.game.boardgame.GridBoardUIText;

/**
 * Provides tests for the <code>GridBoardKey</code> class.
 */
public class GridBoardKeyTest {

    /** Test data. */
    private final TestData _testData = new TestData();

    /** Flag indicating whether to provide verbose output. */
    private final boolean _isVerbose = false;

    /**
     * Test the <code>GridBoardKey()</code> method.
     */
    @Test
    public void constructor2() {
        final IGridBoard gridBoard = _testData.createGridBoard2();
        final GridBoardKey gbk = new GridBoardKey(gridBoard);
        assertNotNull(gbk);
        assertThat(gbk.getGridBoardString(), is("      XO "));
        assertThat(gbk.getRotation(), is(Rotation.Z_90));
    }

    /**
     * Test the <code>GridBoardKey()</code> method.
     */
    @Test
    public void constructor4() {
        final IGridBoard gridBoard = _testData.createGridBoard4();
        final GridBoardKey gbk = new GridBoardKey(gridBoard);
        assertNotNull(gbk);
        assertThat(gbk.getGridBoardString(), is("   O  XOX"));
        assertThat(gbk.getRotation(), is(Rotation.Z_90));
    }

    /**
     * Test the <code>GridBoardKey()</code> method.
     */
    @Test
    public void constructor6() {
        final IGridBoard gridBoard = _testData.createGridBoard6();
        final GridBoardKey gbk = new GridBoardKey(gridBoard);
        assertNotNull(gbk);
        assertThat(gbk.getGridBoardString(), is("   OXOXOX"));
        assertThat(gbk.getRotation(), is(Rotation.Z_90));
    }

    /**
     * Test the <code>GridBoardKey()</code> method.
     */
    @Test
    public void constructor8() {
        final IGridBoard gridBoard = _testData.createGridBoard8();
        final GridBoardKey gbk = new GridBoardKey(gridBoard);
        assertNotNull(gbk);
        assertThat(gbk.getGridBoardString(), is(" OXXXOOOX"));
        assertThat(gbk.getRotation(), is(Rotation.Z_180));
    }

    /**
     * Test the <code>GridBoardKey()</code> method.
     */
    @Test
    public void constructor8All() {
        final String gridBoardString = " OXXXOOOX";
        final IGridBoard gridBoard8 = _testData.createGridBoard8();
        printGridBoard(gridBoard8);
        final GridBoardKey gridBoardKey8 = new GridBoardKey(gridBoard8);
        assertNotNull(gridBoardKey8);
        assertThat(gridBoardKey8.getGridBoardString(), is(gridBoardString));
        assertThat(gridBoardKey8.getRotation(), is(Rotation.Z_180));
        final IGridBoard gridBoard8Left = _testData.createGridBoard8RotateLeft();
        printGridBoard(gridBoard8Left);
        final GridBoardKey gridBoardKey8Left = new GridBoardKey(gridBoard8Left);
        assertNotNull(gridBoardKey8Left);
        assertThat(gridBoardKey8Left.getGridBoardString(), is(gridBoardString));
        assertThat(gridBoardKey8Left.getRotation(), is(Rotation.X_180));
        final IGridBoard gridBoard8Right = _testData.createGridBoard8RotateRight();
        printGridBoard(gridBoard8Right);
        final GridBoardKey gridBoardKey8Right = new GridBoardKey(gridBoard8Right);
        assertNotNull(gridBoardKey8Right);
        assertThat(gridBoardKey8Right.getGridBoardString(), is(gridBoardString));
        assertThat(gridBoardKey8Right.getRotation(), is(Rotation.Z_270));
        assertThat(gridBoard8.getTokenCollection().size(), is(8));
        assertThat(gridBoard8Left.getTokenCollection().size(), is(8));
        assertThat(gridBoard8Right.getTokenCollection().size(), is(8));
        assertTrue(gridBoardKey8.equals(gridBoardKey8Left));
        assertTrue(gridBoardKey8.equals(gridBoardKey8Right));
        assertTrue(gridBoardKey8Left.equals(gridBoardKey8Right));
    }

    /**
     * Test the <code>GridBoardKey()</code> method.
     */
    @Test
    public void constructor8Rotate() {
        final IGridBoard gridBoard = _testData.createGridBoard8RotateLeft();
        final GridBoardKey gbk = new GridBoardKey(gridBoard);
        assertNotNull(gbk);
        assertThat(gbk.getGridBoardString(), is(" OXXXOOOX"));
        assertThat(gbk.getRotation(), is(Rotation.X_180));
    }

    /**
     * Test the <code>GridBoardKey()</code> method.
     */
    @Test
    public void constructorEmpty() {
        final IGridBoard gridBoard = _testData.createGridBoardEmpty();
        final GridBoardKey gbk = new GridBoardKey(gridBoard);
        assertNotNull(gbk);
        assertThat(gbk.getGridBoardString(), is("         "));
        assertThat(gbk.getRotation(), is(Rotation.NONE));
    }

    /** Set up the test. */
    @Before
    public void setUp() {
        ApplicationSupport.setStringBundleName("org.vizzini.example.tictactoe.ui.resources");
        ApplicationSupport.setConfigBundleName("org.vizzini.example.tictactoe.ui.config");
    }

    /**
     * Test the <code>equals()</code> method.
     */
    @Test
    public void testEquals() {
        final IGridBoard gridBoard0 = _testData.createGridBoard2();
        final GridBoardKey gridBoardKey0 = new GridBoardKey(gridBoard0);
        final IGridBoard gridBoard1 = _testData.createGridBoard2();
        final GridBoardKey gridBoardKey1 = new GridBoardKey(gridBoard1);
        assertTrue(gridBoardKey0.equals(gridBoardKey1));
        assertThat(gridBoardKey0, is(gridBoardKey1));
    }

    /**
     * Print the given parameter.
     * 
     * @param gridBoard Grid board.
     */
    private void printGridBoard(final IGridBoard gridBoard) {
        if (_isVerbose) {
            final GridBoardUIText writer = new GridBoardUIText(null, gridBoard);
            writer.writeBoard(gridBoard, System.out);
        }
    }
}
