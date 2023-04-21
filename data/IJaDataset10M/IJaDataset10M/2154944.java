package org.freelords.forms.map;

import java.util.EnumMap;
import java.util.Map;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.freelords.map.GameMap;
import org.freelords.forms.map.TileDrawer;
import org.freelords.map.TileType;
import org.freelords.util.geom.Direction;
import org.freelords.util.geom.Point;
import org.freelords.util.geom.Rect;
import org.freelords.util.sound.SoundMap;
import org.freelords.xml.XMLElement;

public class IsoTileDrawerSprite implements TileDrawer {

    /** Multi images containing the terrain tile textures and the different coast tile parts */
    private Image image;

    private int imageHeight;

    private Image coastImage;

    /** Width and height of a terrain tile and the coast tiles */
    private int tileWidth;

    private int tileHeight;

    private int tileWidthCoast;

    private int tileHeightCoast;

    /** The naming of the different tile images (grassland, forest, ...) */
    private Map<TileType, Integer> tilePlacement;

    /** Sounds associated with the various terrains. */
    private SoundMap soundMap;

    /** Const array telling you, which directions are adjacent to each corner of a tile */
    private static final Direction[][] TilesToInspect = new Direction[4][3];

    {
        TilesToInspect[0][0] = Direction.NORTH_WEST;
        TilesToInspect[0][1] = Direction.WEST;
        TilesToInspect[0][2] = Direction.SOUTH_WEST;
        TilesToInspect[1][0] = Direction.NORTH_EAST;
        TilesToInspect[1][1] = Direction.EAST;
        TilesToInspect[1][2] = Direction.SOUTH_EAST;
        TilesToInspect[2][0] = Direction.NORTH_WEST;
        TilesToInspect[2][1] = Direction.NORTH;
        TilesToInspect[2][2] = Direction.NORTH_EAST;
        TilesToInspect[3][0] = Direction.SOUTH_WEST;
        TilesToInspect[3][1] = Direction.SOUTH;
        TilesToInspect[3][2] = Direction.SOUTH_EAST;
    }

    /** Constructor with arguments loaded from an xml-file (tilesets/isometric.tileset.xml) 
	  * Used in UIModule.getTileDrawer() */
    public IsoTileDrawerSprite(@XMLElement(name = "tile-image") Image image, @XMLElement(name = "tile-width") int tileWidth, @XMLElement(name = "tile-height") int tileHeight, @XMLElement(name = "coast-image") Image coastImage, @XMLElement(name = "coast-tile-width") int tileWidthCoast, @XMLElement(name = "coast-tile-height") int tileHeightCoast, @XMLElement(name = "tile-map") Map<TileType, Integer> tileMap, @XMLElement(name = "tile-sounds") SoundMap soundMap) {
        if (image == null) throw new NullPointerException("Image can't be null");
        if (tileWidth <= 0 || tileHeight <= 0) throw new IllegalArgumentException("Tile dimensions must be positive");
        this.image = image;
        imageHeight = image.getBounds().height;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.coastImage = coastImage;
        this.tileWidthCoast = tileWidthCoast;
        this.tileHeightCoast = tileHeightCoast;
        this.tilePlacement = tileMap;
        this.soundMap = soundMap;
        if (soundMap != null) {
            soundMap.check(TileType.class);
        }
        if (this.tilePlacement == null) {
            this.tilePlacement = new EnumMap<TileType, Integer>(TileType.class);
            for (TileType tt : TileType.values()) tilePlacement.put(tt, tt.ordinal());
        } else {
            for (TileType tt : TileType.values()) {
                if (!tilePlacement.containsKey(tt)) {
                    tilePlacement.put(tt, 0);
                }
            }
        }
    }

    /** The drawing routine of one tile/sprite defined by map and tileX, tileY at position (drawX, drawY).
	 *  Is used by IsoTileBackground.draw(). */
    public void draw(GC drawSurface, int drawX, int drawY, GameMap map, int tileX, int tileY) {
        if (tileX == -1) {
            tileX = 0;
        }
        if (tileX == map.getWidth()) {
            tileX = map.getWidth() - 1;
        }
        if (tileY == -1) {
            tileY = 0;
        }
        if (tileY == map.getHeight()) {
            tileY = map.getHeight() - 1;
        }
        TileType tileType = map.getTile(tileX, tileY).getTileType();
        int column = tilePlacement.get(tileType);
        drawSurface.drawImage(image, column * tileWidth, 0, tileWidth, imageHeight, drawX, drawY - (imageHeight - tileHeight), tileWidth, imageHeight);
        if (coastImage != null && tileType == TileType.WATER) {
            int i;
            EnumMap<Direction, Boolean> isWaterOrNone = new EnumMap<Direction, Boolean>(Direction.class);
            for (Direction dir : Direction.values()) {
                Point newpos = dir.shift(new Point(tileX, tileY));
                TileType t = map.getTile(newpos.getX(), newpos.getY()).getTileType();
                if (t == TileType.WATER || t == TileType.NONE) {
                    isWaterOrNone.put(dir, true);
                } else {
                    isWaterOrNone.put(dir, false);
                }
            }
            int[] CornerType = new int[4];
            for (i = 0; i < 4; i++) {
                CornerType[i] = -1;
                if (!isWaterOrNone.get(TilesToInspect[i][0])) {
                    if (!isWaterOrNone.get(TilesToInspect[i][2])) CornerType[i] = 0; else CornerType[i] = 1;
                } else {
                    if (!isWaterOrNone.get(TilesToInspect[i][2])) CornerType[i] = 2; else if (!isWaterOrNone.get(TilesToInspect[i][1])) CornerType[i] = 3;
                }
            }
            i = CornerType[0];
            if (i >= 0) {
                drawSurface.drawImage(coastImage, i * tileWidthCoast, 0 * tileHeightCoast, tileWidthCoast, tileHeightCoast, drawX, drawY + (tileHeight - tileHeightCoast) / 2, tileWidthCoast, tileHeightCoast);
            }
            i = CornerType[1];
            if (i >= 0) {
                drawSurface.drawImage(coastImage, i * tileWidthCoast, 1 * tileHeightCoast, tileWidthCoast, tileHeightCoast, drawX + tileWidth - tileWidthCoast, drawY + (tileHeight - tileHeightCoast) / 2, tileWidthCoast, tileHeightCoast);
            }
            i = CornerType[2];
            if (i >= 0) {
                drawSurface.drawImage(coastImage, i * tileWidthCoast, 2 * tileHeightCoast, tileWidthCoast, tileHeightCoast, drawX + (tileWidth - tileWidthCoast) / 2, drawY, tileWidthCoast, tileHeightCoast);
            }
            i = CornerType[3];
            if (i >= 0) {
                drawSurface.drawImage(coastImage, i * tileWidthCoast, 3 * tileHeightCoast, tileWidthCoast, tileHeightCoast, drawX + (tileWidth - tileWidthCoast) / 2, drawY + tileHeight - tileHeightCoast, tileWidthCoast, tileHeightCoast);
            }
        }
    }

    /** This is the rectangle where a part of image will be copied to */
    public Rect getDrawRectangle(int drawX, int drawY) {
        return new Rect(drawX, drawY - (imageHeight - tileHeight), tileWidth, imageHeight);
    }

    /** Get tile height */
    public int getTileHeight() {
        return tileHeight;
    }

    /** Get tile width */
    public int getTileWidth() {
        return tileWidth;
    }

    public SoundMap getSounds() {
        return soundMap;
    }
}
