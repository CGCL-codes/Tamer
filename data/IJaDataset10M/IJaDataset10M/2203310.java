package com.customwars.client.ui.sprite;

import com.customwars.client.io.img.slick.ImageStrip;
import com.customwars.client.model.Observable;
import com.customwars.client.model.map.Location;
import com.customwars.client.model.map.Tile;
import com.customwars.client.model.map.TileMap;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * TiledSprite extends Sprite adding a tile Location.
 * it will handle animations that don't fit to a tile by
 * #1 translating the Image frame up or
 * #2 centering the Image frame
 * A TiledSprite moves from one Location to another. It cannot move out of the map bounds
 */
public class TileSprite extends Sprite implements Observable {

    private TileMap<Tile> map;

    private int tileSize;

    private Location location;

    private int frameHeightOffset;

    private int frameWidthOffset;

    private boolean renderInCenter;

    private int effectRange;

    public TileSprite(Location location, TileMap<Tile> map) {
        super(null);
        init(map, location, null);
    }

    public TileSprite(int x, int y, Location location, TileMap<Tile> map) {
        super(x, y);
        init(map, location, null);
    }

    public TileSprite(Animation anim, Location location, TileMap<Tile> map) {
        super(anim);
        init(map, location, anim);
    }

    public TileSprite(Image img, Location location, TileMap<Tile> map) {
        super(img, 0, 0);
        init(map, location, null);
    }

    public TileSprite(ImageStrip imageStrip, int delay, Location location, TileMap<Tile> map) {
        super(null, 0, 0);
        init(map, location, new Animation(imageStrip.toArray(), delay));
    }

    private void init(TileMap<Tile> map, Location location, Animation anim) {
        if (map != null) setMap(map);
        if (location != null) setLocation(location);
        if (anim != null) setAnim(anim);
        effectRange = 0;
    }

    public void activate() {
        setLocation(getX() - 1, getY());
        setLocation(getX() + 1, getY());
    }

    public void render(Graphics g) {
        if (super.canRenderAnim(g)) {
            translateOffset(g, renderInCenter);
            super.render(g);
            undoTranslateOffset(g);
        }
    }

    /**
   * Calculate and store the image offsets, translate the graphics
   *
   * @param center True -> Center on the tile
   *               False -> Images that don't fit to the tile are drawn higher,
   *               so that the image base is equal with the tile base
   */
    void translateOffset(Graphics g, boolean center) {
        Image img = anim.getCurrentFrame();
        if (img != null) {
            if (center) {
                frameHeightOffset = (img.getHeight() - tileSize) / 2;
                frameWidthOffset = (img.getWidth() - tileSize) / 2;
            } else {
                frameHeightOffset = img.getHeight() - tileSize;
                frameWidthOffset = (img.getWidth() - tileSize) / 2;
            }
            g.translate(-frameWidthOffset, -frameHeightOffset);
        }
    }

    /**
   * UndoWrapper the translation to the graphics, by using the stored offsets
   */
    void undoTranslateOffset(Graphics g) {
        g.translate(frameWidthOffset, frameHeightOffset);
        frameWidthOffset = 0;
        frameHeightOffset = 0;
    }

    public void setMap(TileMap<Tile> map) {
        this.map = map;
        this.tileSize = map.getTileSize();
    }

    /**
   * Converts newLocation to x,y coordinates and change the position of the sprite
   * if no map is set or newLocation is not valid within the map then the sprite won't move.
   *
   * @param newLocation the location the sprite should move to
   */
    public void setLocation(Location newLocation) {
        if (map != null && map.isValid(newLocation) && this.location != newLocation) {
            this.location = newLocation;
            super.setLocation(newLocation.getCol() * tileSize, newLocation.getRow() * tileSize);
        }
    }

    public void setRenderInCenter(boolean renderInCenter) {
        this.renderInCenter = renderInCenter;
    }

    public void setEffectRange(int effectRange) {
        if (effectRange <= 0) {
            throw new IllegalArgumentException("Effect range cannot be negative or 0");
        }
        this.effectRange = effectRange;
    }

    public Location getLocation() {
        return location;
    }

    public int getEffectRange() {
        return effectRange;
    }
}
