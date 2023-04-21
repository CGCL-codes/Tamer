package de.mizi.tilemap.renderer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JPanel;
import de.mizi.controller.GameEvent;
import de.mizi.controller.GameListener;
import de.mizi.tilemap.object.LocatableTilemapObject;
import de.mizi.tilemap.object.TilemapObjectDrawData;

/**
 * The renderer class that draws a whole tilemap.
 * @author mizi
 *
 */
public class TilemapRenderer extends JPanel implements GameListener {

    private TilemapObjectDrawData[][] mapLayout;

    private TilemapObjectDrawData[][] tilesToDraw;

    private LocatableTilemapObject player;

    private LocatableTilemapObject cursor;

    private TilemapObjectRenderer tileRenderer;

    private TilemapViewport viewport;

    private int tileWidth;

    private int tileHeight;

    private boolean showCursor;

    private static final long serialVersionUID = 1L;

    /**
	 * Constructs a new TilemapRenderer object with the given
	 * TilemapObjectRenderer.</br>
	 * Warning: The map layout and the player data will be set to null.
	 * @param tileRenderer the renderer that will be used to draw the tilemap
	 */
    public TilemapRenderer(TilemapObjectRenderer tileRenderer) {
        this(null, null, tileRenderer);
    }

    /**
	 * Constructs a new TilemapRenderer object with the given map layout,
	 * player data and TilemapObjectRenderer.
	 * @param mapLayout the map layout
	 * @param player the player data
	 * @param tileRenderer the renderer that will be used to draw the tilemap 
	 */
    public TilemapRenderer(TilemapObjectDrawData[][] mapLayout, LocatableTilemapObject player, TilemapObjectRenderer tileRenderer) {
        this.mapLayout = mapLayout;
        this.tilesToDraw = new TilemapObjectDrawData[0][0];
        this.player = player;
        this.tileRenderer = tileRenderer;
        if (this.mapLayout == null) {
            this.viewport = new TilemapViewport(0, 0, 1, 1);
        } else {
            this.viewport = new TilemapViewport(0, 0, this.mapLayout[0].length, this.mapLayout.length);
        }
        this.tileWidth = this.tileRenderer.getTileWidth();
        this.tileHeight = this.tileRenderer.getTileHeight();
        updateNumberOfTilesToDraw();
        updateTilesToDraw();
    }

    /**
	 * Sets the viewport position. Negative values are allowed for the x and y
	 * position.
	 * @param x the x position in tiles
	 * @param y the y position in tiles
	 */
    public void setViewportPosition(int x, int y) {
        viewport.x = x;
        viewport.y = y;
        repaint(updateTilesToDraw());
    }

    /**
	 * Sets the viewport size.
	 * @param widthInTiles the width in tiles
	 * @param heightInTiles the height in tiles
	 */
    public void setViewportSize(int widthInTiles, int heightInTiles) {
        viewport.widthInTiles = widthInTiles;
        viewport.heightInTiles = heightInTiles;
        updateNumberOfTilesToDraw();
        repaint(updateTilesToDraw());
    }

    /**
	 * Set the viewport position and size. Negative values are allowed for the
	 * x and y position.
	 * @param x the x position in tiles
	 * @param y the y position in tiles
	 * @param widthInTiles the width in tiles
	 * @param heightInTiles the height in tiles
	 */
    public void setViewport(int x, int y, int widthInTiles, int heightInTiles) {
        setViewportPosition(x, y);
        setViewportSize(widthInTiles, heightInTiles);
    }

    /**
	 * Set the viewport position and size. The data is copied from the
	 * given viewport.
	 * @param newViewportData the new viewport position and size data.
	 */
    public void setViewport(TilemapViewport newViewportData) {
        setViewportPosition(newViewportData.x, newViewportData.y);
        setViewportSize(newViewportData.widthInTiles, newViewportData.heightInTiles);
    }

    /**
	 * Changes the data that shall be rendered.
	 * @param mapLayout the new data that shall be rendered
	 */
    public void setMapLayout(TilemapObjectDrawData[][] mapLayout) {
        this.mapLayout = mapLayout;
        repaint(updateTilesToDraw());
    }

    /**
	 * Changes the renderer that shall be used to render each tile of the map data.
	 * @param renderer the new renderer that shall be used to render each tile of the map data
	 */
    public void setRenderer(TilemapObjectRenderer tileRenderer) {
        this.tileRenderer = tileRenderer;
        this.tileWidth = tileRenderer.getTileWidth();
        this.tileHeight = tileRenderer.getTileHeight();
        repaint();
    }

    @Override
    public void update(GameEvent event) {
        switch(event.eventType) {
            case NEW_MAP_LAYOUT:
                mapLayout = event.getMapLayout();
                repaint(updateTilesToDraw());
                break;
            case NEW_PLAYER:
                player = event.getObjectData();
                repaint(updateTilesToDraw());
                break;
            case PLAYER_POSITION_CHANGED:
                player.setX(event.getX());
                player.setY(event.getY());
                repaint(updateTilesToDraw());
                break;
            case NEW_CURSOR:
                cursor = event.getObjectData();
                repaint(updateTilesToDraw());
                break;
            case CURSOR_POSITION_CHANGED:
                cursor.setX(event.getX());
                cursor.setY(event.getY());
                repaint(updateTilesToDraw());
                break;
            case SET_SHOW_CURSOR:
                showCursor = event.getBooleanValue();
                repaint(updateTilesToDraw());
        }
    }

    @Override
    public Dimension getPreferredSize() {
        int width = viewport.widthInTiles * tileWidth;
        int height = viewport.heightInTiles * tileHeight;
        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x;
        int y;
        for (int row = 0; row < viewport.heightInTiles; ++row) {
            y = row * tileHeight;
            for (int column = 0; column < viewport.widthInTiles; ++column) {
                if (tilesToDraw[row][column] != null) {
                    x = column * tileWidth;
                    tileRenderer.drawTile(g, x, y, tilesToDraw[row][column]);
                }
            }
        }
    }

    private void updateNumberOfTilesToDraw() {
        tilesToDraw = new TilemapObjectDrawData[viewport.heightInTiles][];
        for (int row = 0; row < tilesToDraw.length; ++row) {
            tilesToDraw[row] = new TilemapObjectDrawData[viewport.widthInTiles];
        }
    }

    private Rectangle updateTilesToDraw() {
        if (mapLayout == null) {
            return new Rectangle(0, 0, 0, 0);
        }
        Rectangle clip = new Rectangle();
        int greatestRow = 0;
        int greatestColumn = 0;
        boolean noClipSet = true;
        int mapDataRow;
        int mapDataColumn;
        int maxRows = mapLayout.length;
        int maxColumns = mapLayout[0].length;
        for (int row = 0; row < viewport.heightInTiles; ++row) {
            mapDataRow = viewport.y + row;
            for (int column = 0; column < viewport.widthInTiles; ++column) {
                mapDataColumn = viewport.x + column;
                if (!mapLayout[mapDataRow][mapDataColumn].equals(tilesToDraw[row][column])) {
                    if (noClipSet) {
                        clip.x = column;
                        clip.y = row;
                        greatestColumn = column;
                        greatestRow = row;
                        noClipSet = false;
                    } else {
                        clip.x = Math.min(clip.x, column);
                        clip.y = Math.min(clip.y, row);
                        greatestColumn = Math.max(greatestColumn, column);
                        greatestRow = Math.max(greatestRow, row);
                    }
                    if (mapDataRow >= 0 && mapDataRow < maxRows && mapDataColumn >= 0 && mapDataColumn < maxColumns) {
                        tilesToDraw[row][column] = mapLayout[mapDataRow][mapDataColumn];
                    } else {
                        tilesToDraw[row][column] = null;
                    }
                }
            }
        }
        clip.width = Math.abs(greatestColumn - clip.x) + 1;
        clip.height = Math.abs(greatestRow - clip.y) + 1;
        if (player != null) {
            int playerColumn = player.getX() - viewport.x;
            int playerRow = player.getY() - viewport.y;
            if (playerRow >= 0 && playerRow < maxRows && playerColumn >= 0 && playerColumn < maxColumns) {
                clip.add(new Rectangle(playerColumn, playerRow, 1, 1));
                tilesToDraw[playerRow][playerColumn] = player.getDrawData();
            }
        }
        if (showCursor && cursor != null) {
            int cursorColumn = cursor.getX() - viewport.x;
            int cursorRow = cursor.getY() - viewport.y;
            if (cursorRow >= 0 && cursorRow < maxRows && cursorColumn >= 0 && cursorColumn < maxColumns) {
                clip.add(new Rectangle(cursorColumn, cursorRow, 1, 1));
                tilesToDraw[cursorRow][cursorColumn] = cursor.getDrawData();
            }
        }
        clip.x *= tileWidth;
        clip.y *= tileHeight;
        clip.width *= tileWidth;
        clip.height *= tileHeight;
        return clip;
    }
}
