package net.compoundeye.hexciv.map;

import net.compoundeye.hexciv.GUI.Texture;
import java.util.*;

/**
 * Resource with a name, bonus tile yields, improved tile yields, texture,
 * occurrences etc.
 */
public final class Resource {

    private final int bonusFood, bonusHammers, bonusCommerce, improvedFood, improvedHammers, improvedCommerce;

    private final String name, textureFilename;

    private Texture texture = null;

    private EnumMap<TileMap.TileType, Boolean> occurence;

    /**
	 * Creates a new resource. Sets all occurence values to <code>false</code>
	 * initially.
	 * 
	 * @param bonusFood Bonus food a tile with this resource yields
	 * @param bonusHammers Bonus hammers
	 * @param bonusCommerce Bonus commerce
	 * @param improvedFood Bonus food an improved tile with this resource yields
	 * @param improvedHammers Bonus hammers
	 * @param improvedCommerce Bonus commerce
	 * @param textureFilename Filename of the resource texture
	 */
    public Resource(final String name, final int bonusFood, final int bonusHammers, final int bonusCommerce, final int improvedFood, final int improvedHammers, final int improvedCommerce, final String textureFilename) {
        this.name = name;
        this.bonusFood = bonusFood;
        this.bonusHammers = bonusHammers;
        this.bonusCommerce = bonusCommerce;
        this.improvedFood = improvedFood;
        this.improvedHammers = improvedHammers;
        this.improvedCommerce = improvedCommerce;
        this.textureFilename = textureFilename;
        occurence = new EnumMap<TileMap.TileType, Boolean>(TileMap.TileType.class);
        for (TileMap.TileType tileType : TileMap.TileType.values()) {
            occurence.put(tileType, false);
        }
    }

    /**
	 * Sets for one TileType whether this resource can occur there or not.
	 * 
	 * @param tileType the <code>TileMap.TileType</code> to set the occurence
	 * for
	 * @param occurs whether the tileType can occur there or not
	 * @see TileMap
	 */
    public void setOccurence(TileMap.TileType tileType, boolean occurs) {
        occurence.put(tileType, occurs);
    }

    /**
	 * Returns whether this resource can occur on a given TileType or not.
	 * 
	 * @param tileType the <code>TileMap.TileType</code> to check
	 * @return true if this resource can occur on the <code>TileType</code>,
	 * false otherwise
	 */
    public boolean occurs(TileMap.TileType tileType) {
        return occurence.get(tileType);
    }

    /**
	 * Getter/Setter methods
	 */
    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    /**
	 * Getter methods
	 */
    public int getBonusCommerce() {
        return bonusCommerce;
    }

    public int getBonusFood() {
        return bonusFood;
    }

    public int getBonusHammers() {
        return bonusHammers;
    }

    public int getImprovedCommerce() {
        return improvedCommerce;
    }

    public int getImprovedFood() {
        return improvedFood;
    }

    public int getImprovedHammers() {
        return improvedHammers;
    }

    public String getName() {
        return name;
    }

    public String getTextureFilename() {
        return textureFilename;
    }
}
