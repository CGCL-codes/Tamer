package common.model.map;

import static common.constant.ErrConstant.*;
import common.model.Direction;
import common.model.map.event.MapEvent;
import common.model.map.event.MapEventListener;
import common.model.map.event.MapPropertyChangeEvent;
import common.model.map.event.TileEventListener;
import common.model.map.interfaces.*;
import org.apache.log4j.Logger;
import javax.swing.event.EventListenerList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A Tiled Map
 * Data is stored as a 2-dimensional array containing references to Tile objects
 * It is responsible for:
 * Validating Tiles
 * Looping through Tiles in various ways
 *
 * @author Stefan
 * @since 2.0
 */
public abstract class TileMap2D implements TileMap {

    protected static final Logger logger = Logger.getLogger(TileMap2D.class);

    private static int TILE_SIZE = 32;

    private int cols, rows;

    private Tile tileMap[][];

    private EventListenerList listenerList = new EventListenerList();

    /**
   * Create a Map with all tiles set to null.
   * Tiles should be added by calling one of the setTile methods
   *
   * @param cols The amount of columns in the mapArray
   * @param rows The amount of rows in the mapArray
   */
    public TileMap2D(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        this.tileMap = new Tile[cols][rows];
    }

    /**
   * Copy a map
   *
   * @param origMap the map to be copied
   */
    public TileMap2D(TileMap2D origMap) {
        this.cols = origMap.cols;
        this.rows = origMap.rows;
        this.tileMap = origMap.tileMap;
    }

    /**
   * Check the vars set for this map
   *
   * @param validateTiles if all the tiles in the map should be checked
   * @throws IllegalStateException Thrown when the map is not valid
   */
    void validateMapState(boolean validateTiles) throws IllegalStateException {
        if (cols <= 0 || rows <= 0) {
            throw new IllegalStateException("Map cols:" + cols + ", rows:" + rows + " " + ERR_NOT_VALID);
        }
        if (TILE_SIZE <= 0) {
            throw new IllegalStateException("TileSize: " + TILE_SIZE + " is " + ERR_SMALER_THEN_ZERO + " or " + ERR_NOT_SET);
        }
        if (validateTiles) {
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    Tile t = tileMap[col][row];
                    if (t == null) {
                        throw new IllegalStateException("Map Tile @ col:" + col + ", Row:" + row + " " + ERR_NULL);
                    }
                    if (t.getTerrain() == null) {
                        throw new IllegalStateException("Map Tile @ col:" + col + ", Row:" + row + " has no terrain.");
                    }
                }
            }
        }
    }

    public void teleport(int fromCol, int fromRow, int toCol, int toRow) {
        Tile from = getTile(fromCol, fromRow);
        Tile to = getTile(toCol, toRow);
        teleport(from, to);
    }

    /**
   * Teleports a TileMover from TileLocation 'from' to TileLocation 'to'
   * Preconditions:
   * from and to parameters cannot be null
   * from contains a TileMover
   * if a precondition was not met an IllegalArgumentException is thrown
   */
    public void teleport(TileLocation from, TileLocation to) {
        checkTeleport(from, to);
        TileMover mover = from.getTileMover();
        from.removeTileMover();
        to.add(mover);
    }

    /**
   * Check the parameters for the teleport function
   */
    private void checkTeleport(TileLocation from, TileLocation to) {
        if (from == null) {
            throw new IllegalArgumentException("Cannot teleport, from Tile " + ERR_NULL);
        }
        TileMover mover = from.getTileMover();
        if (mover == null) {
            throw new IllegalArgumentException("Cannot teleport, from Tile (" + from.getCol() + "," + from.getRow() + ") contains no TileMover.");
        }
        if (to == null) {
            throw new IllegalArgumentException("Cannot teleport, to Tile " + ERR_NULL);
        }
    }

    public Iterable<Tile> getAllTiles() {
        Iterable<Tile> it;
        it = new Iterable<Tile>() {

            public Iterator<Tile> iterator() {
                final WholeMapIterator m = new WholeMapIterator();
                return new Iterator<Tile>() {

                    public boolean hasNext() {
                        return m.hasNext();
                    }

                    public Tile next() {
                        return m.next();
                    }

                    public void remove() {
                        m.remove();
                    }
                };
            }
        };
        return it;
    }

    /**
   * Returns all the tiles surrounding the given tile within the
   * given range. The center tile itself is not included.
   *
   * @param t        The tile that lies on the center of the tiles to return.
   * @param minRange How far away do we need to start from the center tile.
   * @param maxRange How far away do we need to stop from the center tile.
   * @return The tiles surrounding the given tile.
   */
    public Iterable<Tile> getSurroundingTiles(Tile t, int minRange, int maxRange) {
        if (minRange <= 0 || maxRange <= 0) {
            throw new IllegalArgumentException("min-max range should be a positive number");
        }
        return (maxRange == 1) ? getAdjacentTiles(t) : getCircleIterator(t, minRange, maxRange);
    }

    private Iterable<Tile> getAdjacentTiles(final Tile center) {
        return new Iterable<Tile>() {

            public Iterator<Tile> iterator() {
                final AdjacentIterator adjIterator = new AdjacentIterator(center);
                return new Iterator<Tile>() {

                    public boolean hasNext() {
                        return adjIterator.hasNext();
                    }

                    public Tile next() {
                        return adjIterator.next();
                    }

                    public void remove() {
                        adjIterator.remove();
                    }
                };
            }
        };
    }

    private Iterable<Tile> getCircleIterator(final Tile center, final int minRange, final int maxRange) {
        return new Iterable<Tile>() {

            public Iterator<Tile> iterator() {
                final CircleIterator circleIterator = new CircleIterator(center, minRange, maxRange);
                return new Iterator<Tile>() {

                    public boolean hasNext() {
                        return circleIterator.hasNext();
                    }

                    public Tile next() {
                        return circleIterator.next();
                    }

                    public void remove() {
                        circleIterator.remove();
                    }
                };
            }
        };
    }

    public Iterable<Tile> getSquareIterator(final Tile center, final int range) {
        return new Iterable<Tile>() {

            public Iterator<Tile> iterator() {
                final SquareIterator squareIterator = new SquareIterator(center, range);
                return new Iterator<Tile>() {

                    public boolean hasNext() {
                        return squareIterator.hasNext();
                    }

                    public Tile next() {
                        return squareIterator.next();
                    }

                    public void remove() {
                        squareIterator.remove();
                    }
                };
            }
        };
    }

    /**
   * Base class for internal iterators.
   */
    private abstract class MapIterator implements Iterator<Tile> {

        /**
     * Get the next Tile as a Tile rather as an object.
     *
     * @return Tile.
     * @throws java.util.NoSuchElementException
     *          if iterator is exhausted.
     */
        public abstract Tile nextTile() throws NoSuchElementException;

        /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration.
     * @throws NoSuchElementException iteration has no more elements.
     */
        public Tile next() {
            return nextTile();
        }

        /**
     * Removes from the underlying collection the last element returned by
     * the iterator (optional operation).
     *
     * @throws UnsupportedOperationException no matter what.
     */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
   * Loop through all columns for every row
   */
    private final class WholeMapIterator extends MapIterator {

        private int col, row;

        public WholeMapIterator() {
            col = 0;
            row = 0;
        }

        /**
     * Determine if the iterator has another position in it.
     *
     * @return True of there is another position
     */
        public boolean hasNext() {
            return row < rows;
        }

        /**
     * Obtain the next Tile to iterate over.
     *
     * @return Next position
     * @throws java.util.NoSuchElementException
     *          if last position already returned
     */
        @Override
        public Tile nextTile() throws NoSuchElementException {
            if (row < rows) {
                Tile t = tileMap[col][row];
                col++;
                if (col == cols) {
                    col = 0;
                    row++;
                }
                return t;
            }
            throw new NoSuchElementException("Iterator exhausted");
        }
    }

    /**
   * Loop through Tiles around the Center Tile
   * This will only return tiles at the 4 compass Directions
   * relative to the baseTile.
   * if a tile is found to be out of the map bounds it is skipped
   * and the next compass direction gets examined.
   */
    private final class AdjacentIterator extends MapIterator {

        private final int MAX_SURROUNDING_TILE_COUNT = 4;

        private final Tile center;

        private int index;

        Tile nextTile;

        /**
     * @param baseTile The Tile around which to iterate
     */
        public AdjacentIterator(Tile baseTile) {
            this.center = baseTile;
        }

        /**
     * Determine if the iterator has another position in it.
     *
     * @return True of there is another position
     */
        public boolean hasNext() {
            if (index == MAX_SURROUNDING_TILE_COUNT) {
                return false;
            }
            while (index < MAX_SURROUNDING_TILE_COUNT) {
                Direction dir = Direction.values()[index];
                nextTile = getAdjacent(center, dir);
                if (nextTile == null) {
                    index++;
                } else {
                    return true;
                }
            }
            return false;
        }

        /**
     * Obtain the next position to iterate over.
     *
     * @return Next position
     * @throws NoSuchElementException if last position already returned
     */
        @Override
        public Tile nextTile() throws NoSuchElementException {
            index += 1;
            if (isWithinMapBounds(nextTile)) return nextTile; else {
                throw new NoSuchElementException("Iterator exhausted");
            }
        }
    }

    /**
   * An interator returning Tiles in the form of a square around the center tile within a given range.
   * The center tile is included, and all returned positions are valid.
   */
    private final class SquareIterator extends MapIterator {

        private final Tile startTile;

        private final int totalSquareRows, totalSquareCols;

        private int row, col;

        /**
     * @param center The center to Iterate around
     * @param range  The amount of tiles to move away from the center
     */
        public SquareIterator(Tile center, int range) {
            if (center == null) {
                throw new IllegalArgumentException("Center Tile cannot be null");
            }
            Tile t = center;
            int rowOffset;
            for (rowOffset = 0; rowOffset < range; rowOffset++) {
                t = getAdjacent(t, Direction.NORTH);
                if (t == null) {
                    break;
                }
            }
            t = center;
            int colOffset;
            for (colOffset = 0; colOffset < range; colOffset++) {
                t = getAdjacent(t, Direction.WEST);
                if (t == null) {
                    break;
                }
            }
            int startRow = center.getRow() - rowOffset;
            int startCol = center.getCol() - colOffset;
            startTile = getTile(startCol, startRow);
            totalSquareCols = startTile.getCol() + range - (range - colOffset) + range;
            totalSquareRows = startTile.getRow() + range - (range - rowOffset) + range;
            col = startTile.getCol();
            row = startTile.getRow();
        }

        /**
     * Determine if the iterator has another Tile Within
     * Square bounds and map bounds.
     *
     * @return <code>true</code> if there is another position and
     *         <code>false</code> otherwise.
     */
        public boolean hasNext() {
            return row <= totalSquareRows && row < rows;
        }

        /**
     * Obtains the next Tile.
     *
     * @return The next Tile. This Tile is guaratied to be valid(not null, within map bounds)
     */
        public Tile nextTile() {
            Tile t = null;
            if (row <= totalSquareRows) {
                t = tileMap[col][row];
                col++;
                if (col > totalSquareCols || col >= cols) {
                    col = startTile.getCol();
                    row++;
                }
            }
            return t;
        }
    }

    /**
   * An interator returning positions in a spiral starting at a given center
   * tile. The center tile is never included in the positions returned, and
   * all returned positions are valid.
   */
    private final class CircleIterator extends MapIterator {

        private final int minRange;

        private final int maxRange;

        private final Tile center;

        private final List<Tile> circleTileList = new ArrayList<Tile>();

        private int index;

        private Tile nextTile;

        /**
     * @param center   The center of the circle
     * @param minRange min Radius of the circle
     * @param maxRange max radius of the circle
     */
        public CircleIterator(Tile center, int minRange, int maxRange) {
            this.minRange = minRange;
            this.maxRange = maxRange;
            this.center = center;
            if (center == null) {
                throw new IllegalArgumentException("Center Tile cannot be null.");
            }
            for (Tile t : getSquareIterator(center, maxRange)) {
                if (isValid(t) && inRange(t)) {
                    circleTileList.add(t);
                }
            }
            nextTile = circleTileList.get(0);
        }

        /**
     * Determine if the iterator has another Tile in it.
     *
     * @return <code>true</code> if there is another Tile and
     *         <code>false</code> otherwise.
     */
        public boolean hasNext() {
            return isValid(nextTile) && index != circleTileList.size();
        }

        /**
     * Obtains the next Tile.
     *
     * @return The next Tile. This Tile is guaratied to be valid(not null, within map bounds)
     */
        public Tile nextTile() {
            nextTile = circleTileList.get(index++);
            return nextTile;
        }

        public boolean inRange(Tile tileToCheck) {
            int valid = Math.abs(tileToCheck.getRow() - center.getRow()) + Math.abs(tileToCheck.getCol() - center.getCol());
            return valid >= minRange && valid <= maxRange;
        }
    }

    /**
   * A Tile is a location, so use the location of the Tile
   * to set itself in the map.
   */
    public void setTile(Tile t) {
        setTile(t, t);
    }

    /**
   * Set the Tile on the location
   */
    public void setTile(Location location, Tile t) {
        int col = location.getCol();
        int row = location.getRow();
        setTile(col, row, t);
    }

    public void setTile(int col, int row, Tile t) {
        Tile oldVal = tileMap[col][row];
        tileMap[col][row] = t;
        propertyChange("tile", oldVal, t);
    }

    public int getTileSize() {
        return TILE_SIZE;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public int countTiles() {
        return rows * cols;
    }

    public Tile getTile(int col, int row) {
        Tile t;
        if (isWithinMapBounds(col, row)) {
            t = tileMap[col][row];
        } else {
            t = null;
        }
        return t;
    }

    public Tile getTile(Location loc) {
        return getTile(loc.getCol(), loc.getRow());
    }

    public Tile getRandomTile() {
        int randCol = (int) (Math.random() * cols);
        int randRow = (int) (Math.random() * rows);
        return getTile(randCol, randRow);
    }

    /**
   * Gets the tile adjacent to the baseTile, in a given direction.
   * Direction.STILL returns the same Tile
   *
   * @param location  The basetile
   * @param direction One of the compass Direction @see Direction
   * @return Adjacent position or same tile
   */
    public Tile getAdjacent(Location location, Direction direction) {
        int row = location.getRow();
        int col = location.getCol();
        switch(direction) {
            case NORTH:
                row = location.getRow() - 1;
                break;
            case EAST:
                col = location.getCol() + 1;
                break;
            case SOUTH:
                row = location.getRow() + 1;
                break;
            case WEST:
                col = location.getCol() - 1;
                break;
            case STILL:
                break;
        }
        return getTile(col, row);
    }

    /**
   * What direction do we have to move to
   * if we start at the <code>baseTile</code> and move to the <code>to</code> tile
   * if <code>to</code> is not adjacent or null Direction.STILL is returned.
   * So this method always returns.
   *
   * @param baseLocation start Tile
   * @param to           The tile to search for
   * @return The Direction to move to or STILL if not found.
   */
    public Direction getDirectionTo(Location baseLocation, Location to) {
        Direction direction;
        if (getAdjacent(baseLocation, Direction.NORTH) == to) {
            direction = Direction.NORTH;
        } else if (getAdjacent(baseLocation, Direction.EAST) == to) {
            direction = Direction.EAST;
        } else if (getAdjacent(baseLocation, Direction.SOUTH) == to) {
            direction = Direction.SOUTH;
        } else if (getAdjacent(baseLocation, Direction.WEST) == to) {
            direction = Direction.WEST;
        } else {
            direction = Direction.STILL;
        }
        return direction;
    }

    /**
   * @return True if the two Tiles are next to each other
   */
    public boolean isAdjacent(Location a, Location b) {
        if (a == b) return false;
        int deltaR = Math.abs(a.getRow() - b.getRow());
        int deltaC = Math.abs(a.getCol() - b.getCol());
        return (deltaR <= 1) && (deltaC <= 1) && (deltaR != deltaC);
    }

    public boolean isValid(Location location) {
        return isWithinMapBounds(location);
    }

    /**
   * @param location Location to be checked
   * @return true if the Location is within map Bounds
   */
    public boolean isWithinMapBounds(Location location) {
        if (location != null) {
            if (isWithinMapBounds(location.getCol(), location.getRow())) {
                return true;
            }
        }
        return false;
    }

    private boolean isWithinMapBounds(int col, int row) {
        return col >= 0 && col < cols && row >= 0 && row < rows;
    }

    protected void propertyChange(String property, Object oldVal, Object newVal) {
        MapPropertyChangeEvent ev = new MapPropertyChangeEvent(this, property, oldVal, newVal);
        fireEvent(ev);
    }

    public void addGameEventListener(MapEventListener listener) {
        listenerList.add(MapEventListener.class, listener);
    }

    public void removeGameEventListener(MapEventListener listener) {
        listenerList.remove(MapEventListener.class, listener);
    }

    protected void fireEvent(MapEvent evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == TileEventListener.class) {
                ((MapEventListener) listeners[i + 1]).mapChange(evt);
            }
        }
    }
}
