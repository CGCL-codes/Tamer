package net.sourceforge.huntforgold.model.map;

import net.sourceforge.huntforgold.graphics.GameScreen;
import net.sourceforge.huntforgold.graphics.LandTile;
import net.sourceforge.huntforgold.graphics.LandTileSet;
import net.sourceforge.huntforgold.graphics.TileManager;
import net.sourceforge.huntforgold.model.Position;
import net.sourceforge.huntforgold.util.CoordinateTransform;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * A facade for the map
 */
public class MapManager {

    /** The logger */
    private static Logger log = Logger.getLogger(MapManager.class);

    /** The singleton instance */
    private static MapManager instance = null;

    /** The game map key */
    public static final int GAME_MAP = 0;

    /** The sea map key */
    public static final int SEA_MAP = 1;

    /** The sea land map key */
    public static final int SEA_LAND_MAP = 2;

    /** The game maps */
    private Map maps;

    /** The current data map */
    private DataMap map;

    /** Land tile set */
    private LandTileSet landTileSet;

    /** Pixels per tile */
    private int pixelsPerTile;

    /** The x offset */
    private int x1;

    /** The y offset */
    private int y1;

    /** Game screen width */
    private int gameScreenWidth;

    /** Game screen height */
    private int gameScreenHeight;

    /**
   * Creates a new MapManager.
   */
    protected MapManager() {
        maps = new HashMap();
        maps.put(new Integer(GAME_MAP), new DataMap());
        maps.put(new Integer(SEA_MAP), new DataMap());
        maps.put(new Integer(SEA_LAND_MAP), new DataMap());
        map = (DataMap) maps.get(new Integer(GAME_MAP));
        landTileSet = (LandTileSet) TileManager.getTileManager().getTileSet(TileManager.LAND_TILES);
        pixelsPerTile = landTileSet.getTileSize();
        gameScreenWidth = GameScreen.getGameScreen().getWidth();
        gameScreenHeight = GameScreen.getGameScreen().getHeight();
    }

    /**
   * Get the instance of the MapManager
   *
   * @return The facade
   */
    public static synchronized MapManager getMapManager() {
        if (instance == null) {
            instance = new MapManager();
        }
        return instance;
    }

    /**
   * Set the active map
   * @param key The active map key
   */
    public void setActiveMap(int key) {
        if (key >= GAME_MAP && key <= SEA_LAND_MAP) {
            map = (DataMap) maps.get(new Integer(key));
        } else {
            log.error("Unknown map key: " + key);
        }
    }

    /**
   * Load a map into the facade
   *
   * @param is A map input stream
   * @exception IOException Thrown if an I/O error occurs
   */
    public synchronized void loadMap(InputStream is) throws IOException {
        map.load(is);
    }

    /**
   * Initialize the sea map
   * @param pos The center position
   */
    public void initializeSeaMap(Position pos) {
        DataMap gameMap = (DataMap) maps.get(new Integer(GAME_MAP));
        DataMap seaMap = (DataMap) maps.get(new Integer(SEA_MAP));
        int numberOfRows = gameMap.getNumberOfRows();
        int numberOfColumns = gameMap.getNumberOfColumns();
        long[][] m = new long[numberOfRows][numberOfColumns];
        int tilesPerDegree = gameMap.getTilesPerDegree();
        double diffX = gameMap.getTopleftX() - gameMap.getBottomrightX();
        double diffY = gameMap.getTopleftY() - gameMap.getBottomrightY();
        double topleftX = pos.getX() + (diffX / 2);
        double topleftY = pos.getY() + (diffY / 2);
        double bottomrightX = pos.getX() - (diffX / 2);
        double bottomrightY = pos.getY() - (diffY / 2);
        seaMap.initialize(numberOfRows, numberOfColumns, m, tilesPerDegree, topleftX, topleftY, bottomrightX, bottomrightY);
        int x = (int) (gameMap.getTopleftX() - pos.getX()) * tilesPerDegree;
        int y = (int) (gameMap.getTopleftY() - pos.getY()) * tilesPerDegree;
        byte wind = gameMap.getWind(y, x);
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                seaMap.setTile(i, j, (byte) 24);
                seaMap.setWind(i, j, wind);
            }
        }
    }

    /**
   * Initialize the sea / land map
   * @param pos The center position of the map
   * @param direction The direction of the land
   * @return The fort position
   */
    public Position initializeSeaLandMap(Position pos, double direction) {
        DataMap gameMap = (DataMap) maps.get(new Integer(GAME_MAP));
        DataMap seaLandMap = (DataMap) maps.get(new Integer(SEA_LAND_MAP));
        int numberOfRows = (gameScreenHeight / pixelsPerTile) + 1;
        int numberOfColumns = (gameScreenWidth / pixelsPerTile) + 1;
        long[][] m = new long[numberOfRows][numberOfColumns];
        int tilesPerDegree = gameMap.getTilesPerDegree();
        double topleftX = pos.getX() + (gameScreenWidth / 2) / (double) (tilesPerDegree * pixelsPerTile);
        double topleftY = pos.getY() + (gameScreenHeight / 2) / (double) (tilesPerDegree * pixelsPerTile);
        double bottomrightX = pos.getX() - (gameScreenWidth / 2) / (double) (tilesPerDegree * pixelsPerTile);
        double bottomrightY = pos.getY() - (gameScreenHeight / 2) / (double) (tilesPerDegree * pixelsPerTile);
        seaLandMap.initialize(numberOfRows, numberOfColumns, m, tilesPerDegree, topleftX, topleftY, bottomrightX, bottomrightY);
        int x = (int) (gameMap.getTopleftX() - pos.getX()) * tilesPerDegree;
        int y = (int) (gameMap.getTopleftY() - pos.getY()) * tilesPerDegree;
        byte wind = gameMap.getWind(y, x);
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                seaLandMap.setTile(i, j, (byte) 24);
                seaLandMap.setWind(i, j, wind);
            }
        }
        double posX = 0;
        double posY = 0;
        if ((direction <= Math.PI / 4) || (direction >= 7 * Math.PI / 4)) {
            for (int i = 0; i < numberOfRows; i++) {
                seaLandMap.setTile(i, 0, (byte) 16);
                seaLandMap.setTile(i, 1, (byte) 13);
            }
            posX = topleftX;
            posY = ((topleftY - bottomrightY) / 2) + bottomrightY;
        } else if ((direction > 5 * Math.PI / 4) && (direction < 7 * Math.PI / 4)) {
            for (int j = 0; j < numberOfColumns; j++) {
                seaLandMap.setTile(numberOfRows - 2, j, (byte) 12);
                seaLandMap.setTile(numberOfRows - 1, j, (byte) 16);
            }
            posX = ((topleftX - bottomrightX) / 2) + bottomrightX;
            posY = bottomrightY;
        } else if ((direction >= Math.PI / 4) && (direction < 3 * Math.PI / 4)) {
            for (int j = 0; j < numberOfColumns; j++) {
                seaLandMap.setTile(0, j, (byte) 16);
                seaLandMap.setTile(1, j, (byte) 8);
            }
            posX = ((topleftX - bottomrightX) / 2) + bottomrightX;
            posY = topleftY;
        } else {
            for (int i = 0; i < numberOfRows; i++) {
                seaLandMap.setTile(i, numberOfColumns - 1, (byte) 16);
                seaLandMap.setTile(i, numberOfColumns - 2, (byte) 9);
            }
            posX = bottomrightX;
            posY = ((topleftY - bottomrightY) / 2) + bottomrightY;
        }
        return new Position(posX, posY);
    }

    /**
   * Returns the number of tiles per degree
   * @return the number of tiles per degree.
   */
    public int getTilesPerDegree() {
        return map.getTilesPerDegree();
    }

    /**
   * Returns the number of pixels per tile
   * @return the number of pixels
   */
    public int getPixelsPerTile() {
        return pixelsPerTile;
    }

    /**
   * Returns the height of the map.
   * @return the number of rows.
   */
    public int getNumberOfRows() {
        return map.getNumberOfRows();
    }

    /**
   * Returns the width of the map.
   * @return the number of columns.
   */
    public int getNumberOfColumns() {
        return map.getNumberOfColumns();
    }

    /**
   * Returns the top left x coordinate of the map
   * @return the x coordinate.
   */
    public double getTopleftX() {
        return map.getTopleftX();
    }

    /**
   * Returns the top left y coordinate of the map
   * @return the y coordinate.
   */
    public double getTopleftY() {
        return map.getTopleftY();
    }

    /**
   * Returns the bottom right x coordinate of the map
   * @return the x coordinate.
   */
    public double getBottomrightX() {
        return map.getBottomrightX();
    }

    /**
   * Returns the botton right y coordinate of the map
   * @return the y coordinate.
   */
    public double getBottomrightY() {
        return map.getBottomrightY();
    }

    /**
   * Returns the map width in pixels
   * @return the number of pixels
   */
    public int getMapWidth() {
        return pixelsPerTile * map.getNumberOfColumns();
    }

    /**
   * Returns the map height in pixels
   * @return the number of pixels
   */
    public int getMapHeight() {
        return pixelsPerTile * map.getNumberOfRows();
    }

    /**
   * Returns the tile at the given position.
   * @param row the row.
   * @param column the column.
   * @return the tile index at the given pos.
   */
    public byte getTile(int row, int column) {
        return map.getTile(row, column);
    }

    /**
   * Sets the tile at the given position.
   * @param row the row.
   * @param column the column.
   * @param tile the tile index for the given pos.
   */
    public void setTile(int row, int column, byte tile) {
        map.setTile(row, column, tile);
    }

    /**
   * Determines whether the given position is on land or water.
   * @param newPosition the position that should be checked.
   * @return true if the position is in water or false if the position is on land.
   */
    public boolean isInWater(Position newPosition) {
        return getTerrainType(newPosition) == LandTile.WATER_MASK;
    }

    /**
   * Determines the type of terrain at a given position.
   * @param position the world position that should be queried.
   * @return WATER_MASK, LAND_MASK or REEF_MASK to indicate the terrain type at the given location.
   */
    public int getTerrainType(Position position) {
        int testX = CoordinateTransform.convertLongitudeToPixels(position.getX());
        int testY = CoordinateTransform.convertLatitudeToPixels(position.getY());
        byte tileIndex = getTile(testY / pixelsPerTile, testX / pixelsPerTile);
        LandTile landTile = landTileSet.getLandTile(tileIndex);
        return landTile.getTileMaskRGB((testX < 0) ? 0 : (testX % pixelsPerTile), (testY < 0) ? 0 : (testY % pixelsPerTile));
    }

    /**
   * Determines whether the terrain at a given position matches a certain terrain type.
   * @param position the world position that should be queried
   * @param mask The terrain types that should be accepted. The mask is obtained by OR'ing
   * the desired combination of WATER_MASK, LAND_MASK and REEF_MASK together.
   * @return true if the terrain is of one of the types specified by mask.
   */
    public boolean isTerrainOfType(Position position, int mask) {
        return ((getTerrainType(position) & mask) != LandTile.ALL_MASK);
    }

    /**
   * Determines whether the given position is inside the world
   * @param newPosition the position that should be checked.
   * @return true if the position is in the world otherwise false.
   */
    public boolean isInWorld(Position newPosition) {
        double x = newPosition.getX();
        double y = newPosition.getY();
        if (x <= getTopleftX() && x >= getBottomrightX() && y <= getTopleftY() && y >= getBottomrightY()) {
            return true;
        }
        return false;
    }

    /**
   * Centers the viewport on the given location.
   * @param p center of screen.
   */
    public void centerOnLocation(Position p) {
        int viewPosX = CoordinateTransform.convertLongitudeToPixels(p.getX());
        int viewPosY = CoordinateTransform.convertLatitudeToPixels(p.getY());
        x1 = viewPosX - gameScreenWidth / 2;
        y1 = viewPosY - gameScreenHeight / 2;
        if (x1 < 0) {
            x1 = 0;
        }
        if (y1 < 0) {
            y1 = 0;
        }
        if (x1 > getMapWidth() - gameScreenWidth) {
            x1 = getMapWidth() - gameScreenWidth;
        }
        if (y1 > getMapHeight() - gameScreenHeight) {
            y1 = getMapHeight() - gameScreenHeight;
        }
    }

    /**
   * Get the pixel coordinate (x) of the top left corner
   * @return The pixel coordinate
   */
    public int getXOffset() {
        return x1;
    }

    /**
   * Get the pixel coordinate (y) of the top left corner
   * @return The pixel coordinate
   */
    public int getYOffset() {
        return y1;
    }

    /**
   * Returns the wind at the given position.
   *
   * 4 low bit: Wind direction and 4 high bit: Wind speed
   * @param row the row.
   * @param column the column.
   * @return the wind at the given pos.
   */
    public byte getWind(int row, int column) {
        return map.getWind(row, column);
    }

    /**
   * Get the world map as an image
   * @return The image
   */
    public BufferedImage getWorldImage() {
        int numOfTiles = landTileSet.getNumberOfTiles();
        boolean[] water = new boolean[numOfTiles];
        for (byte x = 0; x < numOfTiles; x++) {
            water[x] = landTileSet.getLandTile(x).isWater();
        }
        int maxX = getNumberOfColumns();
        int maxY = getNumberOfRows();
        BufferedImage image = new BufferedImage(maxX, maxY, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                int tileIndex = getTile(y, x);
                if (water[tileIndex]) {
                    image.setRGB(x, y, Color.BLUE.getRGB());
                } else {
                    image.setRGB(x, y, Color.GREEN.getRGB());
                }
            }
        }
        return image;
    }

    /**
   * Finds the nearest position where there is water
   * @param position The world position that should be queried.
   * @return The nearest water position
   */
    public Position findWater(Position position) {
        int testX = CoordinateTransform.convertLongitudeToPixels(position.getX());
        int testY = CoordinateTransform.convertLatitudeToPixels(position.getY());
        byte tileIndex = getTile(testY / pixelsPerTile, testX / pixelsPerTile);
        LandTile landTile = landTileSet.getLandTile(tileIndex);
        return landTile.findWater(position);
    }

    /**
   * Return the data map currently used. This method
   * must only be called from a data editor
   * @return The data map
   */
    public DataMap getDataMap() {
        return map;
    }
}
