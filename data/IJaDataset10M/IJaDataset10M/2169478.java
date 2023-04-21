package com.gantzgulch.life;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.util.List;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import com.gantzgulch.life.model.Board;
import com.gantzgulch.life.util.CoordinateUtil;
import com.gantzgulch.life.util.PatternLoader;
import com.gantzgulch.life.util.TroveUtils;

public abstract class AbstractTest {

    private PatternLoader patternLoader;

    @Before
    public void abstractSetUp() {
        patternLoader = new PatternLoader();
    }

    protected List<Long> cells(Board board) {
        return TroveUtils.toList(board.getCoordinateIterator());
    }

    protected long cell(int xCoordinate, int yCoordinate) {
        return CoordinateUtil.computeCoordinate(xCoordinate, yCoordinate);
    }

    protected void dumpBoard(Board board) {
        System.out.println("Board:");
        for (int yCoordinate = -5; yCoordinate <= 5; yCoordinate++) {
            for (int xCoordinate = -5; xCoordinate <= 5; xCoordinate++) {
                System.out.print(board.isAlive(xCoordinate, yCoordinate) ? "*" : ".");
            }
            System.out.println();
        }
    }

    protected void loadPattern(String patternResource, Board board) {
        patternLoader.loadPattern(patternResource, board);
    }

    protected void assertBoardExtents(Board board, int xCoordinateMin, int xCoordinateMax, int yCoordinateMin, int yCoordinateMax) {
        assertThat(board.getXCoordinateMin(), is(xCoordinateMin));
        assertThat(board.getXCoordinateMax(), is(xCoordinateMax));
        assertThat(board.getYCoordinateMin(), is(yCoordinateMin));
        assertThat(board.getYCoordinateMax(), is(yCoordinateMax));
    }

    protected Matcher<Board> hasCellCountOf(long cellCount) {
        return new HasCellCountOf(cellCount);
    }

    public static class HasCellCountOf extends BaseMatcher<Board> {

        private long cellCount;

        private HasCellCountOf(long cellCount) {
            this.cellCount = cellCount;
        }

        @Override
        public boolean matches(Object item) {
            Board board = (Board) item;
            return board.getCellCount() == cellCount;
        }

        @Override
        public void describeTo(Description description) {
            description.appendValue(cellCount);
        }
    }

    protected Matcher<Board> cellIsAlive(int xCoordinate, int yCoordinate) {
        return new CellIsAlive(xCoordinate, yCoordinate);
    }

    public static class CellIsAlive extends BaseMatcher<Board> {

        private int xCoordinate;

        private int yCoordinate;

        private CellIsAlive(int xCoordinate, int yCoordinate) {
            this.xCoordinate = xCoordinate;
            this.yCoordinate = yCoordinate;
        }

        @Override
        public boolean matches(Object item) {
            Board board = (Board) item;
            return board.isAlive(xCoordinate, yCoordinate);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("Cell at (" + xCoordinate + "," + yCoordinate + ") to be alive.");
        }
    }
}
