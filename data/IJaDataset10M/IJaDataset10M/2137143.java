package net.sourceforge.j_snake;

import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Ignat Alexeyenko
 * Date: Aug 21, 2008
 * Time: 11:27:47 PM
 */
public final class CellPool {

    Logger log = Logger.getLogger(CellPool.class);

    private static CellPool INSTANCE;

    Map<Dot, EmptyCell> emptyCells;

    Map<Dot, WallCell> wallCells;

    Map<Dot, FoodCell> foodCells;

    Map<Dot, SnakeCell> snakeCells;

    Map<Dot, BonusCell> bonusCells;

    /**
     * Creates a cell pool caches
     */
    private CellPool() {
        emptyCells = new HashMap<Dot, EmptyCell>();
        wallCells = new HashMap<Dot, WallCell>();
        foodCells = new HashMap<Dot, FoodCell>();
        snakeCells = new HashMap<Dot, SnakeCell>();
        bonusCells = new HashMap<Dot, BonusCell>();
    }

    /**
     * Gives access to CellPool instance
     * @return CellPool instance
     */
    public static CellPool getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CellPool();
        }
        return INSTANCE;
    }

    /**
     * Creates a EmptyCell object for specified coordinates
     * Uses cache to store created objects
     * @param x x-axis coord
     * @param y y-axis coord
     * @return EmptyCell object
     */
    public EmptyCell getEmptyCell(int x, int y) {
        Dot keyDot = DotPool.getInstance().getDot(x, y);
        EmptyCell cached = emptyCells.get(keyDot);
        if (cached == null) {
            cached = new EmptyCell(keyDot);
            emptyCells.put(keyDot, cached);
            log.debug("Missing EmptyCell cache for dot " + keyDot);
        } else {
            log.debug("Returning cached EmptyCell for dot " + keyDot);
        }
        return cached;
    }

    /**
     * Creates a WallCell object for specified coordinates
     * Uses cache to store created objects 
     * @param x x-axis coord
     * @param y y-axis coord
     * @return WallCell object
     */
    public WallCell getWallCell(int x, int y) {
        Dot keyDot = DotPool.getInstance().getDot(x, y);
        WallCell cached = wallCells.get(keyDot);
        if (cached == null) {
            cached = new WallCell(keyDot);
            wallCells.put(keyDot, cached);
            log.debug("Missing WallCell cache for dot " + keyDot);
        } else {
            log.debug("Returning cached WallCell for dot " + keyDot);
        }
        return cached;
    }

    /**
     * Creates a FoodCell object for specified coordinates
     * Uses cache to store created objects
     * @param x x-axis coord
     * @param y y-axis coord
     * @return FoodCell object
     */
    public FoodCell getFoodCell(int x, int y) {
        Dot keyDot = DotPool.getInstance().getDot(x, y);
        FoodCell cached = foodCells.get(keyDot);
        if (cached == null) {
            cached = new FoodCell(keyDot);
            foodCells.put(keyDot, cached);
            log.debug("Missing FoodCell cache for dot " + keyDot);
        } else {
            log.debug("Returning cached FoodCell for dot " + keyDot);
        }
        return cached;
    }

    /**
     * Creates a SnakeCell object for specified coordinates
     * Uses cache to store created objects
     * @param x x-axis coord
     * @param y y-axis coord
     * @return SnakeCell object
     */
    public SnakeCell getSnakeCell(int x, int y) {
        Dot keyDot = DotPool.getInstance().getDot(x, y);
        SnakeCell cached = snakeCells.get(keyDot);
        if (cached == null) {
            cached = new SnakeCell(keyDot);
            snakeCells.put(keyDot, cached);
            log.debug("Missing SnakeCell cache for dot " + keyDot);
        } else {
            log.debug("Returning cached SnakeCell for dot " + keyDot);
        }
        return cached;
    }

    /**
     * Creates a BonusCell object for specified coordinates
     * Uses cache to store created objects
     * @param x x-axis coord
     * @param y y-axis coord
     * @return BonusCell object
     */
    public BonusCell getBonusCell(int x, int y) {
        Dot keyDot = DotPool.getInstance().getDot(x, y);
        BonusCell cached = bonusCells.get(keyDot);
        if (cached == null) {
            cached = new BonusCell(keyDot);
            bonusCells.put(keyDot, cached);
            log.debug("Missing BonusCell cache for dot " + keyDot);
        } else {
            log.debug("Returning cached BonusCell for dot " + keyDot);
        }
        return cached;
    }
}
