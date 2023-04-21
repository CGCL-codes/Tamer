package net.sourceforge.huntforgold.model.map;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.log4j.Logger;

/**
 * A data map.
 */
public class DataMap {

    /** The logger */
    private static Logger log = Logger.getLogger(DataMap.class);

    /** The number of rows */
    private int nRows;

    /** The number of columns */
    private int nCols;

    /** The map */
    private long[][] map;

    /** Tiles per degree */
    private int tilesPerDegree;

    /** The top left x position */
    private double topleftX;

    /** The top left y position */
    private double topleftY;

    /** The bottom right x position */
    private double bottomrightX;

    /** The bottom right y position */
    private double bottomrightY;

    /**
   * Constructor
   */
    public DataMap() {
    }

    /**
   * Initialize a new data map.
   * @param numberOfRows height of map.
   * @param numberOfColumns width of map.
   * @param map double array containing the map for each position.
   * @param tilesPerDegree number of tiles per degree
   * @param topleftX the top left x coordinate of the map
   * @param topleftY the top left y coordinate of the map
   * @param bottomrightX the bottom right x coordinate of the map
   * @param bottomrightY the bottom right y coordinate of the map
   */
    public synchronized void initialize(int numberOfRows, int numberOfColumns, long[][] map, int tilesPerDegree, double topleftX, double topleftY, double bottomrightX, double bottomrightY) {
        this.nRows = numberOfRows;
        this.nCols = numberOfColumns;
        this.map = map;
        this.tilesPerDegree = tilesPerDegree;
        this.topleftX = topleftX;
        this.topleftY = topleftY;
        this.bottomrightX = bottomrightX;
        this.bottomrightY = bottomrightY;
        if (log.isDebugEnabled()) {
            log.debug("numberOfRows=" + numberOfRows + ", numberOfColumns=" + numberOfColumns + ", map=" + map + ", tilesPerDegree=" + tilesPerDegree + ", topleftX=" + topleftX + ", topleftY=" + topleftY + ", bottomrightX=" + bottomrightX + ", bottomrightY=" + bottomrightY);
        }
    }

    /**
   * Loads a map from an input stream
   *
   * @param is The map input stream
   * @exception IOException Thrown if an I/O error occurs
   */
    public void load(InputStream is) throws IOException {
        GZIPInputStream gzip = new GZIPInputStream(is);
        DataInputStream data = new DataInputStream(gzip);
        long[][] values;
        int row = data.readInt();
        int col = data.readInt();
        int tilesPerDegree = data.readInt();
        double topleftX = data.readDouble();
        double topleftY = data.readDouble();
        double bottomrightX = data.readDouble();
        double bottomrightY = data.readDouble();
        values = new long[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                values[i][j] = data.readLong();
            }
        }
        data.close();
        initialize(row, col, values, tilesPerDegree, topleftX, topleftY, bottomrightX, bottomrightY);
    }

    /**
   * Saves the map to an output stream
   * @param os The output stream
   * @exception IOException Thrown if an I/O error occurs
   */
    public void save(OutputStream os) throws IOException {
        GZIPOutputStream gzip = new GZIPOutputStream(os);
        DataOutputStream data = new DataOutputStream(gzip);
        data.writeInt(nRows);
        data.writeInt(nCols);
        data.writeInt(getTilesPerDegree());
        data.writeDouble(getTopleftX());
        data.writeDouble(getTopleftY());
        data.writeDouble(getBottomrightX());
        data.writeDouble(getBottomrightY());
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                data.writeLong(map[i][j]);
            }
        }
        data.flush();
        data.close();
    }

    /**
   * Returns the tile at the given position.
   * @param row the row.
   * @param column the column.
   * @return the tile index at the given pos.
   */
    public byte getTile(int row, int column) {
        if (row < 0 || row >= nRows || column < 0 || column >= nCols) {
            log.warn("Getting tile outside map: (" + row + ", " + column + ")");
            return 0;
        }
        return (byte) (map[row][column] & 0xFF);
    }

    /**
   * Sets the tile at the given position.
   * @param row the row.
   * @param column the column.
   * @param tile the tile index for the given pos.
   */
    public void setTile(int row, int column, byte tile) {
        if (row < 0 || row >= nRows || column < 0 || column >= nCols) {
            log.warn("Setting tile outside map: (" + row + ", " + column + ") with tile: " + tile);
        } else {
            long temp = map[row][column];
            temp = temp & 0xFFFFFFFFFFFFFF00L;
            temp = temp | tile;
            map[row][column] = temp;
        }
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
        if (row < 0 || row >= nRows || column < 0 || column >= nCols) {
            log.warn("Getting wind outside map: (" + row + ", " + column + ")");
            return 0;
        }
        long temp = (long) (map[row][column] >>> 8);
        return (byte) (temp & 0xFF);
    }

    /**
   * Sets the wind at the given position.
   *
   * 4 low bit: Wind direction and 4 high bit: Wind speed
   * @param row the row.
   * @param column the column.
   * @param wind the wind for the given pos.
   */
    public void setWind(int row, int column, byte wind) {
        if (row < 0 || row >= nRows || column < 0 || column >= nCols) {
            log.warn("Setting wind outside map: (" + row + ", " + column + ") with value: " + wind);
        } else {
            long temp = map[row][column];
            long w = wind;
            w = w << 8;
            temp = temp & 0xFFFFFFFFFFFF00FFL;
            temp = temp | w;
            map[row][column] = temp;
        }
    }

    /**
   * Returns the height of the map.
   * @return the number of rows.
   */
    public int getNumberOfRows() {
        return nRows;
    }

    /**
   * Returns the width of the map.
   * @return the number of columns.
   */
    public int getNumberOfColumns() {
        return nCols;
    }

    /**
   * Returns the number of tiles per degree
   * @return the number of tiles per degree.
   */
    public int getTilesPerDegree() {
        return tilesPerDegree;
    }

    /**
   * Returns the top left x coordinate of the map
   * @return the x coordinate.
   */
    public double getTopleftX() {
        return topleftX;
    }

    /**
   * Returns the top left y coordinate of the map
   * @return the y coordinate.
   */
    public double getTopleftY() {
        return topleftY;
    }

    /**
   * Returns the bottom right x coordinate of the map
   * @return the x coordinate.
   */
    public double getBottomrightX() {
        return bottomrightX;
    }

    /**
   * Returns the botton right y coordinate of the map
   * @return the y coordinate.
   */
    public double getBottomrightY() {
        return bottomrightY;
    }
}
