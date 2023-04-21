package jstarcraft.tile;

import java.awt.Graphics2D;
import jstarcraft.core.GridMap;
import jstarcraft.core.GridMapRender;
import jstarcraft.core.ResourceManager.Constant;

public abstract class AbstractTile implements Tile {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    protected int id;

    protected float x, y;

    protected int tileX, tileY;

    protected boolean selected;

    protected float health;

    protected GridMap gridMap;

    protected GridMapRender gm;

    protected String UUID;

    public AbstractTile(int id) {
        this.id = id;
    }

    public abstract Tile clone(int x, int y, GridMap gridMap);

    public abstract void draw(Graphics2D g, int offsetX, int offsetY);

    public void update(long elapsedTime) {
    }

    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public boolean isSelected() {
        return selected;
    }

    public void focus() {
        selected = true;
    }

    public void blur() {
        selected = false;
    }

    public int getType() {
        return id / Constant.TYPE_SIZE;
    }

    public int getId() {
        return id;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public boolean operate(int x, int y, String uuid) {
        return false;
    }

    public float getHealth() {
        return health;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String uuid) {
        UUID = uuid;
    }
}
