package org.minions.stigma.globals;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.minions.utils.Properties;
import org.minions.utils.logger.Log;

/**
 * Class describing properties of resource set. Contains
 * resource meta-data. It should be sent to client.
 */
public class ResourceSetProperties {

    private static final byte DEFAULT_TILE_WIDTH = 32;

    private static final byte DEFAULT_TILE_HEIGTH = 32;

    private byte tileWidth;

    private byte tileHeight;

    /**
     * Constructor. Loads properties from given file.
     * @param fileName
     *            file from which properties should be read
     */
    public ResourceSetProperties(String fileName) {
        Properties prop = new Properties();
        try {
            InputStream in = new FileInputStream(fileName);
            prop.load(in);
            in.close();
        } catch (IOException e) {
            Log.logger.error(e);
        }
        tileWidth = prop.getProperty("tile-width", DEFAULT_TILE_WIDTH);
        tileHeight = prop.getProperty("tile-height", DEFAULT_TILE_HEIGTH);
    }

    /**
     * Constructor.
     * @param tileHeight
     *            see {@link #getTileHeight()}
     * @param tileWidth
     *            see {@link #getTileWidth()}
     */
    public ResourceSetProperties(byte tileHeight, byte tileWidth) {
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
    }

    /**
     * Returns configured tile width. Stored in file as
     * 'tile-width'.
     * @return configured tile width
     */
    public byte getTileWidth() {
        return tileWidth;
    }

    /**
     * Returns configured tile height. Stored in file as
     * 'tile-height'.
     * @return configured tile height
     */
    public byte getTileHeight() {
        return tileHeight;
    }
}
