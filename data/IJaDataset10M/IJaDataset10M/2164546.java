package destal.shared.world;

import java.awt.Point;

/**
 * Presents a position in the world
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 */
public class WorldPoint extends Point.Double {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8050762219871420694L;

    /**
	 * Creates a new WorldPoint without specific coordinates
	 */
    public WorldPoint() {
        super();
    }

    /**
	 * Creates a new WorldPoint with the specified coordinates
	 * @param x The horizontal position
	 * @param y The vertical position
	 */
    public WorldPoint(int x, int y) {
        super(x, y);
    }

    /**
	 * Creates a new WorldPoint with the specified coordinates
	 * @param x The horizontal position
	 * @param y The vertical position
	 */
    public WorldPoint(double x, double y) {
        super(x, y);
    }

    /**
	 * Creates a new WorldPoint using the specified position of the chunk
	 * in the world and the specified position in the chunk
	 * @param xLevel The chunk's horizontal position in the world
	 * @param yLevel The chunk's vertical position in the world
	 * @param xChunk The horizontal position in the chunk
	 * @param yChunk The vertical position in the chunk
	 */
    public WorldPoint(int xLevel, int yLevel, int xChunk, int yChunk) {
        this(xLevel * World.CHUNK_SIZE + xChunk, yLevel * World.CHUNK_SIZE + yChunk);
    }

    /**
	 * Returns the current chunk's location in the world
	 * @return The current chunk's location as a Point
	 */
    public Point getChunkLocation() {
        return new Point((int) this.x / World.CHUNK_SIZE, (int) this.y / World.CHUNK_SIZE);
    }

    /**
	 * Returns the location in the current chunk
	 * @return The location in the current chunk as a Point
	 */
    public Point getLocationInChunk() {
        return new Point((int) (this.getX() % World.CHUNK_SIZE), (int) (this.getY() % World.CHUNK_SIZE));
    }

    /**
	 * Transforms the location of the current WorldPoint to an location on a graphics
	 * object using the specified coordinates as the WorldPoint in the upper left hand
	 * corner of the graphics object
	 * @param x The horizontal position of the upper left hand corner
	 * @param y The vertical position of the upper left hand corner
	 * @return The coordinate of the current WorldPoint to display it on the graphics
	 * 		   object as a Point
	 */
    public Point getLocationOnPanel(double x, double y) {
        return new Point((int) ((this.getX() - x) * World.BLOCK_PAINTSIZE), (int) ((this.getY() - y) * World.BLOCK_PAINTSIZE));
    }

    @Override
    public String toString() {
        return "WorldPoint [x=" + x + ", y=" + y + "]";
    }
}
