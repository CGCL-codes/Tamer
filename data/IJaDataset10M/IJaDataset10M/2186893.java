package net.compoundeye.hexciv.GUI;

import net.compoundeye.hexciv.map.*;

/**
 * Abstract base class for all world map views (play map, minimap, tactical map etc.).
 */
public abstract class WorldMapView {

    protected int screenPosX, screenPosY, width, height;

    protected WorldMap worldMap;

    /**
	 * Constructs a world map view object for a given world map at a specified
	 * position on the screen and a given size.
	 * 
	 * @param screenPosX The x coordinate on the screen the view is to be displayed at
	 * @param screenPosY The y coordinate
	 * @param width The width of the view
	 * @param height The height of the view
	 * @param worldMap The <code>WorldMap</code> to display a view of
	 */
    public WorldMapView(int screenPosX, int screenPosY, int width, int height, WorldMap worldMap) {
        this.screenPosX = screenPosX;
        this.screenPosY = screenPosY;
        this.width = width;
        this.height = height;
        this.worldMap = worldMap;
    }

    /**
	 * Renders the view of the world map.
	 */
    public abstract void render();

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getScreenPosX() {
        return screenPosX;
    }

    public void setScreenPosX(int screenPosX) {
        this.screenPosX = screenPosX;
    }

    public int getScreenPosY() {
        return screenPosY;
    }

    public void setScreenPosY(int screenPosY) {
        this.screenPosY = screenPosY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }
}
