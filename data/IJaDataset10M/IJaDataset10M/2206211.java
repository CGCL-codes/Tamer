package net.sourceforge.dsnk.gui.map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sourceforge.dsnk.gui.map.model.DrawableBlock;
import net.sourceforge.dsnk.gui.map.model.DrawableTile;
import net.sourceforge.dsnk.gui.map.model.DrawableBlock.NeighborDirection;
import net.sourceforge.dsnk.gui.map.util.AgingCache;
import net.sourceforge.dsnk.gui.map.util.CacheItemNotFoundException;
import net.sourceforge.dsnk.logic.LandTileDataReader;
import net.sourceforge.dsnk.logic.Map0Reader;
import net.sourceforge.dsnk.logic.TexIdxReader;
import net.sourceforge.dsnk.logic.TexMapsReader;
import net.sourceforge.dsnk.model.LandTile;
import net.sourceforge.dsnk.model.MapBlock;
import net.sourceforge.dsnk.model.MapCell;
import net.sourceforge.dsnk.model.TexIdx;
import net.sourceforge.dsnk.model.Texture;
import net.sourceforge.dsnk.model.TileGroup;

/**
 * Loads data from the map, including map data, tiles and textures.
 * 
 * @author Jochen Kraushaar
 */
public class MapDataLoader {

    /** reader for reading map data */
    private Map0Reader mapReader = null;

    /** reader for reading texture data */
    private TexMapsReader textureReader = null;

    /** all available land tiles */
    private TileGroup<LandTile>[] tileGroups = null;

    /** index into texture data */
    private TexIdx[] texIdxs = null;

    /** graphics configuration for creating images */
    private GraphicsConfiguration graphicsConfiguration = null;

    private Map<Point, DrawableBlock> loadedBlocks = null;

    private AgingCache<Texture> textureCache = null;

    /**
	 * Constructor
	 * 
	 * @param map0File
	 *            map0.mul file
	 * @param tileDataFile
	 *            tiledata.mul
	 * @param texIdxFile
	 *            texidx.mul
	 * @param texMapsFile
	 *            texmaps.mul
	 * @param gc
	 *            graphics configuration used to create "N/A" image
	 */
    public MapDataLoader(File map0File, File tileDataFile, File texIdxFile, File texMapsFile, GraphicsConfiguration gc) throws IOException {
        this.graphicsConfiguration = gc;
        this.loadedBlocks = new HashMap<Point, DrawableBlock>();
        this.textureCache = new AgingCache<Texture>(64);
        this.mapReader = new Map0Reader(map0File);
        this.textureReader = new TexMapsReader(texMapsFile);
        LandTileDataReader tileReader = new LandTileDataReader(new FileInputStream(tileDataFile));
        this.tileGroups = tileReader.readAll();
        tileReader.close();
        TexIdxReader texIdxReader = new TexIdxReader(new FileInputStream(texIdxFile));
        this.texIdxs = texIdxReader.readAll();
        texIdxReader.close();
    }

    /**
	 * Loads noBlocks drawable blocks around the center block. If e.g. noBlocks
	 * is 2, a 5 * 5 matrix of blocks is loaded with centerBlock at (3, 3).
	 * Additionally all blocks outside this matrix are marked as "discarded".
	 * All already discarded blocks are removed.
	 * 
	 * @param centerBlock
	 *            center of the matrix
	 * @param noBlocks
	 *            number of blocks to load in each direction
	 * @throws IOException
	 *             if blocks cannot be loaded
	 * @throws IllegalArgumentException
	 *             if centerBlock is null or position of block lies not on the
	 *             map
	 */
    public synchronized void loadBlocks(DrawableBlock centerBlock, int noBlocks) throws IOException, IllegalArgumentException {
        if (centerBlock == null) {
            throw new IllegalArgumentException("centerBlock may not be null");
        }
        loadBlocksAround(centerBlock, noBlocks);
        cleanBlocks(centerBlock, noBlocks);
    }

    /**
	 * Cleans the list of loaded blocks to save memory.
	 * 
	 * @param centerBlock
	 *            center block
	 * @param maxDistance
	 *            max distance to center block
	 */
    private void cleanBlocks(DrawableBlock centerBlock, int maxDistance) {
        Point centerPos = centerBlock.getPosition();
        Collection<DrawableBlock> blocks = this.loadedBlocks.values();
        List<DrawableBlock> blocksToRemove = new ArrayList<DrawableBlock>();
        for (DrawableBlock block : blocks) {
            Point blockPos = block.getPosition();
            int distance = Math.abs(centerPos.x - blockPos.x) + Math.abs(centerPos.y - blockPos.y);
            if (distance > maxDistance) {
                blocksToRemove.add(block);
            }
        }
        for (DrawableBlock block : blocksToRemove) {
            this.loadedBlocks.remove(block.getPosition());
        }
        Runtime r = Runtime.getRuntime();
        r.gc();
    }

    /**
	 * Recursively loads the blocks around the center block.
	 * 
	 * @param centerBlock
	 *            center block
	 * @param remainingDistance
	 *            as long as remainingDistance is greater than 1, neighbors of
	 *            the neighbors are also loaded
	 * @throws IOException
	 *             if blocks cannot be loaded
	 */
    private void loadBlocksAround(DrawableBlock centerBlock, int remainingDistance) throws IOException {
        List<DrawableBlock> nextBlocks = new ArrayList<DrawableBlock>();
        remainingDistance--;
        Point pos = centerBlock.getPosition();
        if ((pos != null) && (coordOnMap(pos))) {
            if (centerBlock.getTiles() == null) {
                MapBlock mapBlock = mapReader.read(pos.x, pos.y);
                DrawableBlock tmp = convertToDrawableBlock(mapBlock);
                centerBlock.setTiles(tmp.getTiles());
            }
            for (NeighborDirection direction : NeighborDirection.values()) {
                Point newPos = getNeighborPosition(pos, direction);
                DrawableBlock neighbor = loadedBlocks.get(newPos);
                if (neighbor == null) {
                    if (coordOnMap(newPos)) {
                        MapBlock mapBlock = mapReader.read(newPos.x, newPos.y);
                        neighbor = convertToDrawableBlock(mapBlock);
                        neighbor.setPosition(newPos);
                        loadedBlocks.put(newPos, neighbor);
                    }
                }
                nextBlocks.add(neighbor);
            }
            if (remainingDistance > 0) {
                for (DrawableBlock nextBlock : nextBlocks) {
                    loadBlocksAround(nextBlock, remainingDistance);
                }
            }
        } else {
            throw new IllegalArgumentException("position of centerBlock lies not on map");
        }
    }

    /**
	 * Gets the position in the direction to the given position.
	 * 
	 * @param pos
	 *            base position
	 * @param direction
	 *            direction of new position
	 * @return new position
	 */
    private Point getNeighborPosition(Point pos, NeighborDirection direction) {
        Point neighborPoint = new Point(pos.x, pos.y);
        switch(direction) {
            case TOP:
                neighborPoint.y = pos.y - 1;
                break;
            case RIGHT:
                neighborPoint.x = pos.x + 1;
                break;
            case BOTTOM:
                neighborPoint.y = pos.y + 1;
                break;
            case LEFT:
                neighborPoint.x = pos.x - 1;
                break;
            default:
                throw new IllegalArgumentException("Unknown direction: " + direction.name());
        }
        return neighborPoint;
    }

    /**
	 * Returns if the given coordinate is part of the map.
	 * 
	 * @param coordinate
	 *            coordinate to check
	 * @return true if the given coordinate lies on the map.
	 */
    private boolean coordOnMap(Point coordinate) {
        boolean xOk = (coordinate.x >= 0) && (coordinate.x < Map0Reader.MAP_WIDTH);
        boolean yOk = (coordinate.y >= 0) && (coordinate.y < Map0Reader.MAP_HEIGHT);
        return xOk && yOk;
    }

    /**
	 * Converts a MapBlock into a DrawableBlock.
	 * 
	 * @param mapBlock
	 *            MapBlock object
	 * @return DrawableBlock object
	 * @throws IOException
	 *             if a texture of the underlying cells cannot be read
	 */
    private DrawableBlock convertToDrawableBlock(MapBlock mapBlock) throws IOException {
        DrawableBlock drawableBlock = new DrawableBlock();
        DrawableTile[][] drawableTiles = new DrawableTile[8][8];
        MapCell[] cells = mapBlock.getCells();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                drawableTiles[x][y] = convertToDrawableTile(cells[(y * 8) + x]);
                Rectangle bounds = drawableTiles[x][y].getRelativeBounds();
                bounds.x = x * bounds.width;
                bounds.y = y * bounds.height;
            }
        }
        drawableBlock.setTiles(drawableTiles);
        return drawableBlock;
    }

    /**
	 * Converts a MapCell object into a DrawableTile object.
	 * 
	 * @param mapCell
	 *            MapCell object
	 * @return DrawableTile object
	 * @throws IOException
	 *             if textures for drawable tile cannot be read.
	 */
    private DrawableTile convertToDrawableTile(MapCell mapCell) throws IOException {
        DrawableTile drawableTile = new DrawableTile();
        int index = mapCell.getIndex();
        int tileGroupIdx = index / 32;
        int tileIdx = index % 32;
        LandTile tile = this.tileGroups[tileGroupIdx].getTiles()[tileIdx];
        TexIdx textureIndex = this.texIdxs[(int) tile.getTextureId()];
        Texture texture = null;
        try {
            texture = this.textureCache.get(textureIndex.getStart());
        } catch (CacheItemNotFoundException e) {
            texture = textureReader.read(textureIndex);
            this.textureCache.put(textureIndex.getStart(), texture);
        }
        Image textureImg = null;
        if (texture != null) {
            textureImg = texture.getImage();
        } else {
            textureImg = graphicsConfiguration.createCompatibleImage(64, 64, Transparency.TRANSLUCENT);
            Graphics g = textureImg.getGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 64, 64);
            g.setColor(Color.WHITE);
            g.drawString("N/A", 22, 35);
        }
        Rectangle bounds = new Rectangle(0, 0, 64, 64);
        drawableTile.setAltitude(mapCell.getAltitude());
        drawableTile.setFlags(tile.getFlags());
        drawableTile.setName(tile.getName());
        drawableTile.setTexture(textureImg);
        drawableTile.setRelativeBounds(bounds);
        return drawableTile;
    }

    /**
	 * @return the loadedBlocks
	 */
    public Map<Point, DrawableBlock> getLoadedBlocks() {
        return loadedBlocks;
    }
}
