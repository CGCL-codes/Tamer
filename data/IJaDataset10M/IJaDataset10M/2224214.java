package org.yajul.empire;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class World {

    private Set units = new HashSet();

    /**
     * Index of units, by coordinate.  Since many units can occupy the same space.
     */
    private Cell[][] grid;

    private int nextUnitId = 1;

    private int xsize;

    private int ysize;

    private int minHeight;

    private int maxHeight;

    public World(int xsize, int ysize) {
        this.xsize = xsize;
        this.ysize = ysize;
        grid = new Cell[xsize][ysize];
        createTerrain();
    }

    public void createTerrain() {
        int delta = 0;
        int radius = 1;
        Random random = new Random();
        int deltaMax = 1000;
        int radiusMax = xsize * ysize / 4;
        int halfDeltaMax = deltaMax / 2;
        int halfRadiusMax = radiusMax / 2;
        Cell cell;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int x = 0; x < xsize; x++) {
            for (int y = 0; y < ysize; y++) {
                delta = random.nextInt(deltaMax) - halfDeltaMax;
                radius = random.nextInt(halfRadiusMax);
                cell = getCell(x, y);
                cell.setHeight(cell.getHeight() + delta);
                if (cell.getHeight() > max) max = cell.getHeight();
                if (cell.getHeight() < min) min = cell.getHeight();
            }
        }
        minHeight = min;
        maxHeight = max;
    }

    public int getXsize() {
        return xsize;
    }

    public int getYsize() {
        return ysize;
    }

    int nextUnitId() {
        return nextUnitId++;
    }

    public Cell getCell(int wx, int wy) {
        Cell cell = grid[wx][wy];
        if (cell == null) {
            cell = new Cell(this, wx, wy);
            grid[wx][wy] = cell;
        }
        return cell;
    }

    public List getUnitList(int wx, int wy) {
        Cell cell = grid[wx][wy];
        if (cell == null) return null; else return cell.getUnitList();
    }

    public Unit getTopUnit(int wx, int wy) {
        List list = getUnitList(wx, wy);
        if (list == null) return null; else return (Unit) list.get(0);
    }

    void addUnit(Unit u) {
        units.add(u);
    }

    public int getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }
}
