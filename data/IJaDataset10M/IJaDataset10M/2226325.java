package cummingsm;

import cummingsm.LivableLocations.GridSpace;
import cummingsm.interfaces.MapMod;

public class Map {

    private LivingCollection[][] _map;

    public static int mapSizeX = 2;

    public static int mapSizeY = 2;

    private Map() {
        System.out.println("MapCreated");
        _map = new LivingCollection[mapSizeX][mapSizeY];
        BuildingGenerator mg = BuildingGenerator.getInstance();
        for (int x = 0; x < mapSizeX; x++) {
            for (int y = 0; y < mapSizeY; y++) {
                _map[x][y] = new LivingCollection(x + 1, y + 1);
                _map[x][y].addLivableLocation(new GridSpace(_map[x][y]));
            }
        }
    }

    public static void forceNewMap() {
        LazyHolder.uniqueINstance = new Map();
    }

    public void mapMod(MapMod mm) {
        mm.applyMapMod(_map);
    }

    private static class LazyHolder {

        private static Map uniqueINstance = new Map();
    }

    /**
	 * Locks resize map 
	 */
    private static boolean instantuated = false;

    /**
	 * Changes the default map grid size.  This will only work if called before
	 * 	the first call of Map.getInstence.
	 * @author Matthew Cummings
	 * @date Jul 16, 2009
	 * @param sizeX
	 * @param sizeY
	 * @return
	 */
    public static void resizeMap(int sizeX, int sizeY) {
        System.out.println("Warning, your resizing the map");
        mapSizeX = sizeX;
        mapSizeY = sizeY;
    }

    public static Map getInstance() {
        instantuated = true;
        return LazyHolder.uniqueINstance;
    }

    /**
	 * Once based array
	 * @author Matthew Cummings
	 * @date Jul 8, 2009
	 * @param x
	 * @param y
	 * @return
	 */
    public LivingCollection getTile(int x, int y) {
        if (x < 1 || x > mapSizeX) {
            return null;
        }
        if (y < 1 || y > mapSizeY) {
            return null;
        }
        x--;
        y--;
        return _map[x][y];
    }
}
