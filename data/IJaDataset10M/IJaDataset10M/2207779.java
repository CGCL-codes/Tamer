package org.voidness.squaretilesframework;

import java.awt.Dimension;
import org.voidness.oje2d.Engine;
import org.voidness.oje2d.GLColor;
import org.voidness.squaretilesframework.sprites.GLSprite;

/** A square map representation */
public class Camera {

    /** The map to interpret */
    private Map map = null;

    /** The X position where to start drawing the camera (on screen) */
    private int xPos;

    /** The Y position where to start drawing the camera (on screen) */
    private int yPos;

    /** The width of the camera bounds */
    private int width;

    /** The height of the camera bounds */
    private int height;

    /** The actor the camera is following */
    private GLSprite followedActor = null;

    /** Whether we want to show a grid or not */
    private boolean grid;

    /** The grid color */
    private GLColor gridColor = null;

    /** The number of extra tiles to draw outside the camera's bounds */
    private Dimension extraTiles = null;

    /** Whether we are in debug mode or not (red squares showing) */
    private boolean debug;

    /**
     * The default camera constructor.
     * 
     * @param mMap The map to interpret
     * @param mExtraTiles The number of extra tiles to draw (outside camera
     *        bounds)
     * @param mFollowedActor The actor this camera will follow around
     */
    public Camera(Map mMap, Dimension mExtraTiles, GLSprite mFollowedActor) {
        map = mMap;
        followedActor = mFollowedActor;
        extraTiles = mExtraTiles;
        grid = false;
        gridColor = GLColor.GREEN;
        debug = false;
    }

    /**
     * (Re)define the actor followed by the camera
     * 
     * @param mFollowedActor The actor beeing followed
     */
    public void setFollowedActor(GLSprite mFollowedActor) {
        followedActor = mFollowedActor;
    }

    /**
     * (Re)defines the map to be interpreted
     * 
     * @param mMap The map to be drawn
     */
    public void setMap(Map mMap) {
        map = mMap;
    }

    /**
     * (Un)sets the camera debug mode
     * 
     * @param mDebug True for debug mode, false for non-debug mode
     */
    public void setDebug(boolean mDebug) {
        debug = mDebug;
    }

    /**
     * Is the camera in debug mode?
     * 
     * @return True if so, false otherwise
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Define the camera's drawing position (i.e., the bounds on-screen)
     * 
     * @param mX The X start point
     * @param mY The Y start point
     * @param mWidth The width of the camera
     * @param mHeight The height of the camera
     */
    public void setDrawingPosition(int mX, int mY, int mWidth, int mHeight) {
        xPos = mX;
        yPos = mY;
        width = mWidth;
        height = mHeight;
    }

    /**
     * (Un)sets the grid drawing
     * 
     * @param mGrid True to show the grid, false not to show
     * @param mColor The color to draw the grid
     */
    public void setGrid(boolean mGrid, GLColor mColor) {
        grid = mGrid;
        gridColor = mColor;
    }

    /**
     * Is the grid currently beeing shown?
     * 
     * @return True if so, false otherwise
     */
    public boolean getGrid() {
        return grid;
    }

    /**
     * Gets the indexes of the tile currently under the mouse.
     * 
     * @return The indexes of the tile
     */
    public Dimension getTileUnderMouse() {
        Engine mEngine = Engine.getInstance();
        if (mEngine.getMouseX() > xPos && mEngine.getMouseX() <= xPos + width && mEngine.getMouseY() > yPos && mEngine.getMouseY() <= yPos + height) {
            int relativeX = mEngine.getMouseX() - xPos;
            int relativeY = mEngine.getMouseY() - yPos;
            int numberX = width / map.getTileSize();
            int numberY = height / map.getTileSize();
            int initI = followedActor.getTileX() - numberX / 2;
            int initJ = followedActor.getTileY() - numberY / 2;
            int tileX = initI + (relativeX / map.getTileSize());
            int tileY = initJ + (relativeY / map.getTileSize());
            if (tileX >= 0 && tileX < map.getWidth() && tileY >= 0 && tileY < map.getHeight()) return new Dimension(tileX, tileY);
        }
        return new Dimension(-1, -1);
    }

    /**
     * Draws the camera's interpretation of the map.
     * 
     * @param mAlpha The alpha to use when drawing
     */
    public void draw(float mAlpha) {
        int centerTileX = xPos + width / 2 - map.getTileSize() / 2;
        int centerTileY = yPos + height / 2 - map.getTileSize() / 2;
        int numberX = width / map.getTileSize() + 2 + extraTiles.width * 2;
        int numberY = height / map.getTileSize() + 2 + extraTiles.height * 2;
        int initX = centerTileX - numberX / 2 * map.getTileSize();
        int initY = centerTileY - numberY / 2 * map.getTileSize();
        int initI = followedActor.getTileX() - numberX / 2;
        int initJ = followedActor.getTileY() - numberY / 2;
        int drawX = initX;
        int drawY = initY;
        for (int j = initJ; j < initJ + numberY; j++) {
            for (int i = initI; i < initI + numberX; i++) {
                if (i >= 0 && i < map.getWidth() && j >= 0 && j < map.getHeight()) {
                    if (map.tileAt(i, j) != null) {
                        map.tileAt(i, j).draw(drawX + followedActor.getOffsetX(), drawY + followedActor.getOffsetY(), mAlpha);
                        if (grid) Engine.drawRect(drawX + followedActor.getOffsetX(), drawY + followedActor.getOffsetY(), map.getTileSize(), map.getTileSize(), 1.0f, gridColor);
                    }
                }
                drawX += map.getTileSize();
            }
            drawX = initX;
            drawY += map.getTileSize();
        }
        drawX = initX;
        drawY = initY;
        for (int j = initJ; j < initJ + numberY; j++) {
            for (int i = initI; i < initI + numberX; i++) {
                if (i >= 0 && i < map.getWidth() && j >= 0 && j < map.getHeight()) {
                    if (map.tileAt(i, j) != null) map.tileAt(i, j).drawObject(drawX + followedActor.getOffsetX() + map.getTileSize() / 2, drawY + followedActor.getOffsetY() + map.getTileSize() / 2);
                    map.tileAt(i, j).drawOccupier(drawX + followedActor.getOffsetX() + map.getTileSize() / 2, drawY + followedActor.getOffsetY() + map.getTileSize() / 2);
                }
                drawX += map.getTileSize();
            }
            drawX = initX;
            drawY += map.getTileSize();
        }
        drawX = initX;
        drawY = initY;
        for (int j = initJ; j < initJ + numberY; j++) {
            for (int i = initI; i < initI + numberX; i++) {
                if (i >= 0 && i < map.getWidth() && j >= 0 && j < map.getHeight()) {
                    if (map.tileAt(i, j) != null) {
                        if (debug) {
                            if (!map.tileAt(i, j).isWalkable() || map.tileAt(i, j).isOccupied()) Engine.drawRect(drawX + followedActor.getOffsetX() + 2, drawY + followedActor.getOffsetY() + 2, map.getTileSize() - 4, map.getTileSize() - 4, 2.0f, GLColor.RED);
                        }
                        if (map.tileAt(i, j).getOccupier() != null) {
                            map.tileAt(i, j).getOccupier().drawName(drawX + followedActor.getOffsetX() + map.getTileSize() / 2, drawY + followedActor.getOffsetY() + map.getTileSize() / 2);
                            map.tileAt(i, j).getOccupier().drawText(drawX + followedActor.getOffsetX() + map.getTileSize() / 2, drawY + followedActor.getOffsetY() + map.getTileSize() / 2);
                        }
                    }
                }
                drawX += map.getTileSize();
            }
            drawX = initX;
            drawY += map.getTileSize();
        }
        if (debug) {
            Engine.drawRect(xPos, yPos, width, height, 1.0f, GLColor.WHITE);
            Engine.drawRect(centerTileX, centerTileY, map.getTileSize(), map.getTileSize(), 2.0f, GLColor.GREEN);
            int mouseTileX = centerTileX - (followedActor.getTileX() - getTileUnderMouse().width) * map.getTileSize();
            int mouseTileY = centerTileY - (followedActor.getTileY() - getTileUnderMouse().height) * map.getTileSize();
            Engine.drawRect(mouseTileX, mouseTileY, map.getTileSize(), map.getTileSize(), 1.0f, GLColor.YELLOW);
        }
    }
}
