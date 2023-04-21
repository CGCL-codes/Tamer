package chromolite.game;

/**
 * The board where stones are thrown and the reserves from which the player chooses.
 *
 * @author pvs
 */
public class Board {

    public static final int RESERVE_SIZE = 2;

    public static final int WIDTH = 9;

    public static final int HEIGHT = 9;

    public static final int TOP = 0;

    public static final int RIGHT = 1;

    public static final int BOTTOM = 2;

    public static final int LEFT = 3;

    public static final int[] RESERVE_POSITIONS = { TOP, RIGHT, BOTTOM, LEFT };

    private Engine engine;

    private Cell[][] cells;

    private Stone[][][] stoneReserves;

    public Board(Engine engine) {
        this.engine = engine;
        createCells();
        createStoneReserves();
    }

    private void createCells() {
        cells = new Cell[WIDTH][HEIGHT];
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells.length; y++) {
                cells[x][y] = new Cell(new BoardPos(this, x, y));
            }
        }
    }

    public void clearArena() {
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells.length; y++) {
                cells[x][y].setStone(null);
            }
        }
    }

    private void createStoneReserves() {
        stoneReserves = new Stone[4][][];
        stoneReserves[TOP] = new Stone[WIDTH][RESERVE_SIZE];
        stoneReserves[LEFT] = new Stone[HEIGHT][RESERVE_SIZE];
        stoneReserves[BOTTOM] = new Stone[WIDTH][RESERVE_SIZE];
        stoneReserves[RIGHT] = new Stone[HEIGHT][RESERVE_SIZE];
    }

    public Stone[][] getStoneReserve(int reservePos) {
        return stoneReserves[reservePos];
    }

    public void dealReserveStones() {
        for (int i = 0; i < stoneReserves.length; i++) {
            for (int j = 0; j < stoneReserves[i].length; j++) {
                for (int k = 0; k < stoneReserves[i][j].length; k++) {
                    stoneReserves[i][j][k] = engine.createRandomReserveStone();
                }
            }
        }
    }

    public void dealArenaStones() {
        for (int x = cells.length - 1; x >= 0; x--) {
            for (int y = cells[x].length - 1; y >= 0; y--) {
                cells[x][y].setStone(null);
            }
        }
        for (int i = 0; i < engine.getInitialStoneNb(); i++) {
            Cell cell = engine.getRandomCell(this);
            cell.setStone(engine.createRandomBoardStone());
        }
    }

    public int getStoneCount() {
        int count = 0;
        for (int x = cells.length - 1; x >= 0; x--) {
            for (int y = cells[x].length - 1; y >= 0; y--) {
                if (cells[x][y].getStone() != null) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean isEmpty() {
        return getStoneCount() == 0;
    }

    public boolean isFull() {
        return getStoneCount() >= (WIDTH * HEIGHT);
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    /**
	 * Returns the position (TOP, RIGHT, BOTTOM or LEFT) corresponding to the reserve at
	 * the pseudo coordinates specified. Returns -1 if the coordinates don't correspond to
	 * any reserve.
	 */
    public int getReservePosition(int x, int y) {
        if (0 <= x && x < WIDTH) {
            if (y < 0) {
                return TOP;
            } else if (y >= HEIGHT) {
                return BOTTOM;
            }
        } else if (0 <= y && y < HEIGHT) {
            if (x < 0) {
                return LEFT;
            } else {
                return RIGHT;
            }
        }
        return -1;
    }

    public int getReserveLength(int position) {
        return stoneReserves[position].length;
    }

    public Stone getReserveStone(int position, int l, int depth) {
        return stoneReserves[position][l][depth];
    }

    public void setReserveStone(int position, int l, int depth, Stone stone) {
        stoneReserves[position][l][depth] = stone;
    }

    public void fillReserve(int position, int l) {
        Stone[] reserveLane = stoneReserves[position][l];
        while (reserveLane[0] == null) {
            for (int depth = 1; depth < reserveLane.length; depth++) {
                reserveLane[depth - 1] = reserveLane[depth];
                reserveLane[depth] = null;
            }
        }
        for (int depth = 0; depth < reserveLane.length; depth++) {
            if (reserveLane[depth] == null) {
                reserveLane[depth] = engine.createRandomReserveStone();
            }
        }
    }

    /**
	 * Indicates whether the specified coordinates are on the board (as opposite to
	 * on the lateral reserves).
	 */
    public boolean isCell(int x, int y) {
        return 0 <= x && x < WIDTH && 0 <= y && y < HEIGHT;
    }
}
