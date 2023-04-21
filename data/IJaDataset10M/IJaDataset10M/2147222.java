package net.sf.lostclan.graphics;

import net.sf.lostclan.resources.ResourceManager;
import org.apache.commons.logging.Log;
import net.sf.lostclan.util.LogFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A list of equally sized tiles.
 *
 * @author Bart Cremers
 * @since 13-dec-2006
 */
public class TileSet {

    /**
     * The logger.
     */
    private static Log log = LogFactory.getLog();

    /**
     * The individual tiles.
     */
    private Tile[] tiles;

    /**
     * The tile size.
     */
    private int tileSize;

    /**
     * Default constructor.
     */
    protected TileSet() {
    }

    /**
     * Creates a new tileset from the specified image.<br> 
     * The image is chopped into a list of equally sized images, each
     * having the specified dimensions.
     *
     * @param tileName     The source image that contains all the tiles.
     * @param tileWidth    The width in pixels of each tile.
     * @param tileHeight   The height in pixels of each tile.
     * @param transparency The desired transparency property of the resulting images. This must be one of the constants
     *                     defined in the class java.awt.Transparency.
     *
     * @throws java.io.IOException if an IO error occurs while loading the source image.
     */
    public TileSet(String tileName, int tileWidth, int tileHeight, int transparency) throws IOException {
        this.tileSize = tileWidth;
        if (tileWidth != tileHeight) {
            log.warn("Tile width (" + tileWidth + ") is not equal to tile height (" + tileHeight + ")");
        }
        ResourceManager resourceManager = ResourceManager.getResourceManager();
        BufferedImage tileImage = resourceManager.getImageResource("resources.images." + tileName, transparency, true);
        int nCols = tileImage.getWidth() / tileWidth;
        int nRows = tileImage.getHeight() / tileHeight;
        GraphicsConfiguration gfxConfig = GraphicsManager.getGraphicsConfiguration();
        java.util.List<Tile> tilesList = new ArrayList<Tile>();
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                BufferedImage t = gfxConfig.createCompatibleImage(tileWidth, tileHeight, transparency);
                Graphics g = t.getGraphics();
                g.drawImage(tileImage, -col * tileWidth, -row * tileHeight, null);
                tilesList.add(new Tile(t, tileWidth, tileHeight));
            }
        }
        tiles = new Tile[tilesList.size()];
        for (int i = 0; i < tilesList.size(); i++) {
            tiles[i] = tilesList.get(i);
        }
    }

    /**
     * Returns the number of tiles in this tileset.
     *
     * @return The number of tiles.
     */
    public int getNumberOfTiles() {
        return tiles.length;
    }

    /**
     * Returns the image with the specified index in this tileset.
     *
     * @param index The index of the desired image.
     *
     * @return The tile with the specified index.
     */
    public Tile getTile(byte index) {
        return tiles[index];
    }

    /**
     * Returns the tile size.
     *
     * @return The tile size
     */
    public int getTileSize() {
        return tileSize;
    }

    protected void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }

    protected void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }
}
