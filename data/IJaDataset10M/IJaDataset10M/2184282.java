package xutools.spatialoperations;

import java.util.LinkedList;
import xutools.helpers.Coordinates2D;

/**
 * Performs buffering operation on a 2D map. The map must have only nonnegative
 * coordinates.
 * 
 * @author Tobias Weigel
 * @date 29.01.2009
 * 
 */
public class SpatialBuffer<T extends DiscreteCoordinateProvider> implements DiscreteMapProvider<T> {

    public static final int MAX_SIZE = 200;

    /**
	 * The buffer working map. If a coordinate does not have any content, its
	 * array entry will be null.
	 */
    private Field<T>[][] map;

    private int sizeX, sizeY;

    /**
	 * With every growth step, this index will be increased by one. This index
	 * equals the index the fields have that were created during the previous
	 * growth step.
	 */
    private int growthIndex = 0;

    /**
	 * Creates a new buffer from a map provider.
	 * 
	 * @param mapProvider
	 *            the map provider which will be used to initialize the map
	 */
    @SuppressWarnings("unchecked")
    public SpatialBuffer(DiscreteMapProvider<T> mapProvider) {
        this.sizeX = mapProvider.getMapSizeX();
        this.sizeY = mapProvider.getMapSizeY();
        map = (Field<T>[][]) new Field[sizeX][sizeY];
        try {
            for (int x = 0; x < sizeX; x++) {
                for (int y = 0; y < sizeY; y++) {
                    T element = mapProvider.getMapElementAt(x, y);
                    if (element == null) continue;
                    Coordinates2D coords = new Coordinates2D(x, y);
                    map[x][y] = new Field<T>(element, coords);
                }
            }
        } catch (IllegalCoordinateException exc) {
            throw new IllegalArgumentException("Corrupted source map!", exc);
        }
    }

    @SuppressWarnings("unchecked")
    public void addElement(T element) {
        Coordinates2D coords = element.getCoordinates();
        if ((coords.getX() < 0) || (coords.getY() < 0)) throw new IllegalArgumentException("Cannot fill a buffer with elements that have negative coordinates! (" + element + ")");
        if ((coords.getX() > MAX_SIZE) || (coords.getY() > MAX_SIZE)) throw new IllegalArgumentException("Cannot create a spatial buffer with a side length larger than " + MAX_SIZE + "! (requested size was: x=" + coords.getX() + "; y=" + coords.getY() + ")");
        if ((coords.getX() > sizeX) || (coords.getY() > sizeY)) {
            int newSizeX = Math.max(sizeX, coords.getX());
            int newSizeY = Math.max(sizeY, coords.getY());
            Field<T>[][] newMap = (Field<T>[][]) new Field[newSizeX][newSizeY];
            for (int i = 0; i < newSizeX; i++) {
                System.arraycopy(map[i], 0, newMap[i], 0, map[i].length);
            }
            map = newMap;
        }
    }

    private void growToField(int x, int y, int nbx, int nby, LinkedList<Field<T>> workList) {
        if ((nbx < 0) || (nby < 0) || (nbx >= sizeX) || (nby >= sizeY)) return;
        if (map[nbx][nby] != null) return;
        T content = null;
        Field<T> field = new Field<T>(content, new Coordinates2D(nbx, nby), growthIndex + 1, map[x][y].getGrowthDeltaX() + nbx - x, map[x][y].getGrowthDeltaY() + nby - y);
        map[nbx][nby] = field;
        workList.add(field);
    }

    /**
	 * Grows a buffer around the content in the map. If at least one new field
	 * is created, the growthIndex is increased by one. Note that fields created
	 * by this process will not have any content (i.e. their content is null).
	 * 
	 * @return LinkedList containing the new fields. If this list is empty, the
	 *         growthIndex has not been changes.
	 */
    public LinkedList<Field<T>> grow() {
        LinkedList<Field<T>> list = new LinkedList<Field<T>>();
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if (map[x][y] == null) continue;
                if (map[x][y].getGrowthIndex() != growthIndex) continue;
                growToField(x, y, x - 1, y, list);
                growToField(x, y, x + 1, y, list);
                growToField(x, y, x, y - 1, list);
                growToField(x, y, x, y + 1, list);
            }
        }
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if (map[x][y] == null) continue;
                if (map[x][y].getGrowthIndex() != growthIndex) continue;
                growToField(x, y, x - 1, y - 1, list);
                growToField(x, y, x + 1, y - 1, list);
                growToField(x, y, x - 1, y + 1, list);
                growToField(x, y, x + 1, y + 1, list);
            }
        }
        if (!list.isEmpty()) growthIndex++;
        return list;
    }

    /**
	 * @return the growthIndex
	 */
    public int getGrowthIndex() {
        return growthIndex;
    }

    @Override
    public T getMapElementAt(int x, int y) throws IllegalCoordinateException {
        return map[x][y].getContent();
    }

    @Override
    public int getMapSizeX() {
        return sizeX;
    }

    @Override
    public int getMapSizeY() {
        return sizeY;
    }

    /**
	 * Returns the field information for given coordinate.
	 * 
	 * @param x
	 * @param y
	 * @return Field<T>
	 * @throws IllegalCoordinateException
	 */
    public Field<T> getField(int x, int y) throws IllegalCoordinateException {
        if ((x < 0) || (y < 0) || (x >= sizeX) || (y >= sizeY)) throw new IllegalCoordinateException(x, y);
        return map[x][y];
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                if (map[x][y] == null) str.append(". "); else if (map[x][y].getGrowthIndex() > 9) str.append("X "); else str.append(map[x][y].getGrowthIndex() + " ");
            }
            str.append("\n");
        }
        return str.toString();
    }
}
