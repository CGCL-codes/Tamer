package net.cevn;

import java.util.Iterator;
import java.util.LinkedList;
import net.cevn.gl.GLUtilities;
import net.cevn.texture.Texture;
import net.cevn.texture.TextureMap;
import net.cevn.util.Position;
import net.cevn.util.Size;
import org.lwjgl.opengl.GL11;

/**
 * The <code>StarFieldBackground</code> class represents a multiple layered background where each layer can
 * be moved at different speeds to produce the parallax effect. The background is a 2D background
 * drawn in orthographic projection mode. Each layer is comprised of a <code>TileMap</code>. The
 * default tile size is 256x256 pixels and assumes a tiled texture of 1024x1024 pixels.
 * 
 * The background image is 1024x1024 in size. Each 256x256 square within the image is a tile. Each
 * row of 256x256 tiles is a layer in the background. The first layer, layer 0, starts are 0,756
 * because OpenGL uses the lower left-corner for 0,0. The next layer starts at 0,512, and so on
 * for four layers. However, usually only three layers are used or needed for a background. Thus,
 * the background image is comprised of 16 tiles, all 256x256 pixels in size.
 * 
 * The texture coordinates are normalized, meaing a texture size of 1 is the entire texture. A
 * texture coordinate greater than 1, results in a repeating pattern. For the most part, only
 * normalized coordinates, texture, are important.
 * 
 * Three layers are created with staggered tiles. Staggered tiles means that row 1 of the
 * layer starts at column 1 in the tile map, while row 2 of the layer starts are column 2
 * in the tile map instead of column 1. This is to prevent a human distinguishable repeating
 * pattern with the background. The background is also infinite, meaning that as the current
 * tile is incremented to the end of the tile map, it wraps back to the beginning. 
 * 
 * @author Christopher Field <cfield2@gmail.com>
 * @version
 * @since 0.0.1
 */
public class StarFieldBackground {

    /**
	 * The tile size of the background in pixels.
	 */
    public static final int TILE_SIZE = 256;

    /**
	 * The scroll speed of layer furthest from the ship. The scroll speed is relative
	 * to the ship speed. A scroll speed of 1.0f would result in a layer moving at
	 * the same speed as the ship and any planets in the system.
	 */
    public static final float LAYER_1_SCROLL_SPEED = 0.3f;

    /**
	 * The scroll speed of layer second furthest from the ship. The scroll speed is relative
	 * to the ship speed. A scroll speed of 1.0f would result in a layer moving at
	 * the same speed as the ship and any planets in the system.
	 */
    public static final float LAYER_2_SCROLL_SPEED = 0.5f;

    /**
	 * The scroll speed of layer closest from the ship. The scroll speed is relative
	 * to the ship speed. A scroll speed of 1.0f would result in a layer moving at
	 * the same speed as the ship and any planets in the system.
	 */
    public static final float LAYER_3_SCROLL_SPEED = 1.0f;

    /**
	 * The texture coordinate for the start of the first layer in the y direction 
	 * in the background tiled image. The x direction is determined by the number 
	 * of tiles and tile size.
	 */
    public static final int LAYER_1_TEXTURE_LOC = 768;

    /**
	 * The texture coordinate for the start of the second layer in the y direction 
	 * in the background tiled image. The x direction is determined by the number 
	 * of tiles and tile size.
	 */
    public static final int LAYER_2_TEXTURE_LOC = 512;

    /**
	 * The texture coordinate for the start of the third layer in the y direction 
	 * in the background tiled image. The x direction is determined by the number 
	 * of tiles and tile size.
	 */
    public static final int LAYER_3_TEXTURE_LOC = 256;

    /**
	 * The screen width in pixels.
	 */
    private int screenWidth;

    /**
	 * The screen height in pixels.
	 */
    private int screenHeight;

    /**
	 * The background image, which is an OpenGL texture, that contains the tiles for each
	 * tile map as one larger picture divided in a grid of <code>TILE_SIZE</code> sections.
	 */
    private Texture texture;

    /**
	 * The layers, or tile maps, that make up the background.
	 */
    private LinkedList<TileMap> layers = new LinkedList<TileMap>();

    /**
	 * Creates a new <code>Background</code> instance with a texture and screen size.
	 * The screen width and height are used to calculate which tiles in each tile map
	 * to draw and limit the drawn tiles to only those that appear on screen. The
	 * default is three layers.
	 * 
	 * @param texture The texture containing all of the tile images for the layers.
	 * @param screenWidth The screen width in pixels.
	 * @param screenHeight The screen height in pixels.
	 */
    public StarFieldBackground(Texture texture, final int screenWidth, final int screenHeight) {
        this(texture, (float) screenWidth, (float) screenHeight);
    }

    /**
	 * Creates a new <code>Background</code> instance with a texture and screen size.
	 * The screen width and height are used to calculate which tiles in each tile map
	 * to draw and limit the drawn tiles to only those that appear on screen. The
	 * default is three layers.
	 * 
	 * @param texture The texture containing all of the tile images for the layers.
	 * @param screenWidth The screen width in pixels.
	 * @param screenHeight The screen height in pixels.
	 */
    public StarFieldBackground(Texture texture, final float screenWidth, final float screenHeight) {
        this.texture = texture;
        this.screenWidth = (int) screenWidth;
        this.screenHeight = (int) screenHeight;
        addLayer(createLayer(LAYER_1_TEXTURE_LOC, LAYER_1_SCROLL_SPEED));
        addLayer(createLayer(LAYER_2_TEXTURE_LOC, LAYER_2_SCROLL_SPEED));
        addLayer(createLayer(LAYER_3_TEXTURE_LOC, LAYER_3_SCROLL_SPEED));
    }

    /**
	 * Creates a layer. The layer is a tile map with the width and height based on the
	 * screen size and the tile size. This method assumes the background texture, or image,
	 * is 1024x1024 pixels in size and is divided into 256x256 tiles, resulting in 4 rows
	 * and 4 columns. Each layer is a row in the tiled image with the number of columns
	 * depending on the screen size. The rows are staggered so as not to generate a
	 * repeating pattern during movement. For example, if there are 4 tiles per row,
	 * row 1 would have in order tiles: 0, 1, 2, 3, while row 2 would have in order: 
	 * 1, 2, 3, 0, and row 3: 2, 3, 0, 1, and so on.
	 * 
	 * @param textureLocationY The row in the texture where the image for the tiles start.
	 * @param scrollSpeed The scroll speed for the layer, the larger the value the faster the movement.
	 * @return A tile map representing a staggered tiled layer of the background.
	 */
    private TileMap createLayer(final int textureLocationY, final float scrollSpeed) {
        final int tileMapWidth = (int) Math.ceil(screenWidth / TILE_SIZE);
        final int tileMapHeight = (int) Math.ceil(screenHeight / TILE_SIZE);
        TileMap map = new TileMap(tileMapWidth, tileMapHeight);
        int currentColumn = 0;
        for (int row = 0; row < tileMapHeight; row++) {
            currentColumn = row % tileMapWidth;
            for (int column = 0; column < tileMapWidth; column++) {
                TextureMap tile = new TextureMap(texture);
                tile.setPosition(new Position(column * TILE_SIZE, textureLocationY));
                tile.setSize(new Size(TILE_SIZE, TILE_SIZE));
                map.setTile(currentColumn, row, tile);
                currentColumn = ++currentColumn % tileMapWidth;
            }
        }
        map.setScrollSpeed(scrollSpeed);
        return map;
    }

    /**
	 * Adds a layer to the background.
	 * 
	 * @param map The tile map with tiles of <code>TILE_SIZE</code> size.
	 */
    private void addLayer(final TileMap map) {
        layers.add(map);
    }

    /**
	 * Draws each layer, or tile map, It will also
	 * bind the texture, so that this only occurs once for the entire background, assuming
	 * the background image is 1024x1024 pixels sized image divided into 256x256 pixels tiles.
	 * 
	 * @param shipX The current x position on the screen.
	 * @param shipY The current y position on the screen.
	 */
    public void render(final float shipX, final float shipY) {
        GLUtilities.saveGLState();
        texture.bind();
        GL11.glBegin(GL11.GL_QUADS);
        Iterator<TileMap> i = layers.iterator();
        while (i.hasNext()) {
            drawLayer(i.next(), shipX, shipY);
        }
        GL11.glEnd();
        GLUtilities.restoreGLState();
    }

    /**
	 * Draws a single layer.
	 * 
	 * @param map The tile map.
	 * @param shipX The x location on the screen.
	 * @param shipY The y location on the screen.
	 */
    private void drawLayer(final TileMap map, final float shipX, final float shipY) {
        int offsetX = Math.round(shipX * map.getScrollSpeed());
        int offsetY = Math.round(shipY * map.getScrollSpeed());
        int firstTileY = pixelsToTiles(offsetY);
        int firstTileX = pixelsToTiles(offsetX);
        int lastTileY = firstTileY + pixelsToTiles(screenHeight) + 1;
        int lastTileX = firstTileX + pixelsToTiles(screenWidth) + 1;
        for (int y = firstTileY; y <= lastTileY; y++) {
            for (int x = firstTileX; x <= lastTileX; x++) {
                TextureMap tile = map.getTile(Math.abs(x % map.getWidth()), Math.abs(y % map.getHeight()));
                if (tile != null) {
                    renderTile(tile, tilesToPixels(x) - offsetX, tilesToPixels(y) - offsetY);
                }
            }
        }
    }

    /**
	 * Draws the tile in a quad at the x,y location on screen. The texture should be bound with
	 * the <code>Texture.bind()</code> before calling this method. It is assumed that this
	 * method would be used in a loop and to increase efficiency, this method only draws a quad
	 * with the texture. The setup of the screen and orthographic projection for 2D drawing within
	 * the OpenGL paradigm is left to parent objects that use the <code>Tile</code> class.
	 *
	 * @param tile The tile.
	 * @param x The x coordinate on screen.
	 * @param y The y coordinate on screen.
	 */
    private void renderTile(final TextureMap tile, final float x, final float y) {
        Size size = tile.getSize();
        GL11.glTexCoord2f(tile.getTextureX(), tile.getTextureEndY());
        GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(tile.getTextureX(), tile.getTextureY());
        GL11.glVertex2f(x, y + size.getHeight());
        GL11.glTexCoord2f(tile.getTextureEndX(), tile.getTextureY());
        GL11.glVertex2f(x + size.getWidth(), y + size.getHeight());
        GL11.glTexCoord2f(tile.getTextureEndX(), tile.getTextureEndY());
        GL11.glVertex2f(x + size.getWidth(), y);
    }

    /**
	 * Converts the screen pixel location to a tile based on the tile size.
	 * 
	 * @param pixels The pixels
	 * @return The tile.
	 */
    private int pixelsToTiles(final int pixels) {
        return (int) Math.floor((float) pixels / TILE_SIZE);
    }

    /**
	 * Converts a tile to a pixel.
	 * 
	 * @param numTiles The tile number, either the row or column of the tile in the tile map.
	 * @return The pixel location on screen.
	 */
    private int tilesToPixels(final int numTiles) {
        return numTiles * TILE_SIZE;
    }

    /**
	 * The <code>TileMap</code> class represents a grid of tiles on the screen. All tiles have the same
	 * size. Each tile map has a different scroll speed, which is how fast the map moves when the screen
	 * position is moved. This is allow for multiple maps on one screen with different scroll speeds to
	 * implement the parallax effect in 2D. A negative scroll speed is also possible and results in
	 * movement in the opposite, or reverse, direction.
	 * 
	 * @author Christopher Field <cfield2@gmail.com>
	 * @version
	 * @since 0.0.1
	 */
    private class TileMap {

        /**
		 * The tiles that make up the map.
		 */
        private TextureMap[][] tiles;

        /**
		 * The scroll speed. The larger the value, the fast the map will move. The smaller the value,
		 * the slower the map will move during screen movement. A negative value causes the map to
		 * move in the opposite, or reverse, direction.
		 */
        private float scrollSpeed = 1.0f;

        /**
		 * Creates a new <code>TileMap</code> instance with a specific width and height in tiles.
		 * 
		 * @param width The width of the map in tiles.
		 * @param height The height of the map in tiles.
		 */
        public TileMap(final int width, final int height) {
            tiles = new TextureMap[width][height];
        }

        /**
		 * Sets the scroll speeed. The scroll speed is how fast the map will move while the
		 * screen is moving. A larger value indicates a faster movement, while a smaller
		 * value indicates a slower movement. A negative value reverses the movement direction.
		 * 
		 * @param scrollSpeed
		 */
        public void setScrollSpeed(final float scrollSpeed) {
            this.scrollSpeed = scrollSpeed;
        }

        /**
		 * Gets the scroll speed. The larger the value, the faster the movement. A negative value
		 * indicates movement in the reverse direction.
		 * 
		 * @return The scroll speed.
		 */
        public float getScrollSpeed() {
            return scrollSpeed;
        }

        /**
		 * Gets the width of the map in tiles.
		 * 
		 * @return The width in number of tiles.
		 */
        public int getWidth() {
            return tiles.length;
        }

        /**
		 * Gets the height of the map in tiles.
		 * 
		 * @return The hieght in number of tiles.
		 */
        public int getHeight() {
            return tiles[0].length;
        }

        /**
		 * Gets a tile.
		 * 
		 * @param x The column.
		 * @param y The row.
		 * @return The tile at the column x and row y.
		 */
        public TextureMap getTile(final int x, final int y) {
            if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
                return null;
            } else {
                return tiles[x][y];
            }
        }

        /**
		 * Gets the size of the tiles in the map in pixels. The tiles are squares.
		 * 
		 * @return The size of the tiles in pixels.
		 */
        public float getTileSize() {
            return tiles[0][0].getSize().getWidth();
        }

        /**
		 * Sets a tile in the map.
		 * 
		 * @param x The column of the tile.
		 * @param y The row of the tile.
		 * @param tile The tile.
		 */
        public void setTile(final int x, final int y, TextureMap tile) {
            tiles[x][y] = tile;
        }
    }
}
