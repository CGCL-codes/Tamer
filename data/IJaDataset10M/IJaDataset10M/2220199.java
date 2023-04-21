package org.berlin.chem.orig;

import java.awt.Graphics;
import java.util.Enumeration;
import java.util.Vector;

/**
 * The SquirmGrid class manages a 2D grid of SquirmCellSlots and a list of
 * SquirmCells. By storing both structures we make great speed savings at the
 * expense of using extra memory.
 * 
 * Can query for a cell slot being empty using cell_grid[x][y].queryEmpty()
 * 
 * Can retrieve its cell (if not empty) using cell_grid[x][y].getOccupant()
 * 
 */
class SquirmGrid {

    /** array of SquirmCellSlots that may or may not have SquirmCells in */
    protected SquirmCellSlot cell_grid[][];

    /** the x and y size of the cell_grid array */
    protected int n_x, n_y;

    /** list of the SquirmCells that exist in the grid */
    protected Vector<SquirmCell> cell_list;

    /** a count of the time steps elapsed */
    private int count = 0;

    /** which side should the cataclysm affect (alternates) */
    private boolean on_right = true;

    private static final int N_CELLS = 500;

    private int FLOOD_PERIOD = 10000;

    private boolean DO_FLOOD = false;

    public void setFloodOnOff(boolean on) {
        DO_FLOOD = on;
    }

    public void setFloodPeriod(int period) {
        FLOOD_PERIOD = period;
    }

    public int getCount() {
        return count;
    }

    public String getContents(final int x, final int y) {
        if (x < 0 || x >= n_x || y < 0 || y >= n_y) return "";
        if (cell_grid[x][y].queryEmpty()) return "";
        String msg = "";
        SquirmCell cell = cell_grid[x][y].getOccupant();
        msg += cell.getStringType();
        msg += cell.getState();
        return msg;
    }

    /**
     * Public constructor initializes size of grid and creates a simple world.
     */
    SquirmGrid(int x, int y) {
        n_x = x;
        n_y = y;
        cell_grid = new SquirmCellSlot[n_x][n_y];
        int i, j;
        for (i = 0; i < n_x; i++) for (j = 0; j < n_y; j++) cell_grid[i][j] = new SquirmCellSlot();
        cell_list = new Vector<SquirmCell>();
        initSimple();
    }

    /** 
     * Render the grid and its contents.
     */
    public void draw(final Graphics g, float scale, boolean fast) {
        for (final Enumeration<SquirmCell> e = cell_list.elements(); e.hasMoreElements(); ) {
            ((SquirmCell) e.nextElement()).draw(g, scale, cell_grid, fast);
        }
        g.drawString(String.valueOf(count), 10, 10);
    }

    /** 
     * Initialize some simple creatures.
     */
    public void initSimple() {
        {
            SquirmCell e = new SquirmCell(10, n_y / 2 + 0, 0, 8, cell_list, cell_grid);
            SquirmCell a = new SquirmCell(10, n_y / 2 + 1, 2, 1, cell_list, cell_grid);
            SquirmCell b = new SquirmCell(10, n_y / 2 + 2, 3, 1, cell_list, cell_grid);
            SquirmCell c = new SquirmCell(10, n_y / 2 + 3, 4, 1, cell_list, cell_grid);
            SquirmCell f = new SquirmCell(10, n_y / 2 + 4, 1, 1, cell_list, cell_grid);
            e.makeBondWith(a);
            a.makeBondWith(b);
            b.makeBondWith(c);
            c.makeBondWith(f);
        }
        int px, py;
        for (int i = 0; i < N_CELLS; i++) {
            px = (int) Math.floor(Math.random() * (float) n_x);
            py = (int) Math.floor(Math.random() * (float) n_y);
            if (cell_grid[px][py].queryEmpty()) {
                new SquirmCell(px, py, SquirmCellProperties.getRandomType(), 0, cell_list, cell_grid);
            }
        }
    }

    /** 
     * Give each cell a chance to move, in strict order.
     */
    public void doTimeStep(final SquirmChemistry chemistry) {
        SquirmCell cell;
        for (final Enumeration<SquirmCell> e = cell_list.elements(); e.hasMoreElements(); ) {
            cell = (SquirmCell) e.nextElement();
            cell.makeReactions(chemistry, n_x, n_y, cell_grid);
            cell.makeMove(n_x, n_y, cell_grid);
            cell.ageSelf();
        }
        if (count++ % FLOOD_PERIOD == 0 && DO_FLOOD) {
            doCataclysm();
        }
    }

    /** 
     * Delete all cells in the right-hand half of the area and refresh.
     */
    protected void doCataclysm() {
        int x, y;
        for (x = (on_right ? n_x / 2 : 0); x < (on_right ? n_x : n_x / 2); x++) {
            for (y = 0; y < n_y; y++) {
                if (!cell_grid[x][y].queryEmpty()) cell_grid[x][y].getOccupant().killSelf(cell_list, cell_grid);
            }
        }
        int px, py;
        for (int i = 0; i < N_CELLS / 2; i++) {
            px = (int) Math.floor(Math.random() * (float) n_x / 2) + (on_right ? n_x / 2 : 0);
            py = (int) Math.floor(Math.random() * (float) n_y);
            if (cell_grid[px][py].queryEmpty()) {
                new SquirmCell(px, py, SquirmCellProperties.getRandomType(), 0, cell_list, cell_grid);
            }
        }
        on_right = !on_right;
    }
}
