package fi.gemwars.gameobjects;

import java.util.HashMap;
import java.util.Map;
import fi.gemwars.gameobjects.map.ItemTypes;
import fi.gemwars.io.ResourceManager;
import fi.gemwars.io.Resources;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Items are a basic entity type.
 *
 */
public class Item extends AEntity {

    /**
	 * Hashmap for item textures
	 */
    private static Map<ItemTypes, Image> itemTextures = new HashMap<ItemTypes, Image>();

    /**
	 * Tile number from the left starting from index 0
	 * Textures are loaded from windb_all.png
	 */
    public ItemTypes itemType;

    /**
	 * Basic width for a Gemwars tile, same as height 56px
	 */
    public static final int TILE_WIDTH = 56;

    /**
	 * Basic height for a Gemwars tile, same as width 56px
	 */
    public static final int TILE_HEIGHT = 56;

    /**
	 * Create a new item
	 * @param itemType type of the item
	 */
    public Item(ItemTypes itemType) {
        this.itemType = itemType;
        if (itemTextures.size() == 0) {
            ResourceManager manager = ResourceManager.getInstance();
            Image textures = manager.getImage(Resources.ITEM_TEXTURES.name());
            for (ItemTypes type : ItemTypes.values()) {
                if (textures != null) {
                    Image texture = textures.getSubImage(TILE_WIDTH * (type.ordinal()), 0, TILE_WIDTH, TILE_HEIGHT);
                    itemTextures.put(type, texture);
                }
            }
        }
    }

    @Override
    public void render(GameContainer cont, Graphics grap) throws SlickException {
        itemTextures.get(itemType).draw(positionX * TILE_WIDTH, positionY * TILE_HEIGHT);
    }

    @Override
    public void update(GameContainer cont, int delta) throws SlickException {
    }
}
