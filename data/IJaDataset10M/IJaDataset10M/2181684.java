package src.game;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * The Outside Land class.
 */
public class Outside implements Land, Serializable {

    private static final long serialVersionUID = Constants.VERSION;

    /**
	 * The player cannot enter outside land objects.
	 */
    public boolean canEnter() {
        return false;
    }

    /**
	 * Used to get the image file name for the outside land object.
	 */
    public String getImage() {
        return Constants.IMAGE_OUTSIDE;
    }

    /**
	 * Returns false since a player can never be outside.
	 */
    public boolean hasPlayer() {
        return false;
    }

    /**
	 * Meaningless for Outside land objects.
	 */
    public void addEnemy(Enemy enemy) {
    }

    /**
	 * Meaningless for Outside land objects.
	 */
    public void addItem(Item item) {
    }

    /**
	 * Always returns null for outside.
	 */
    public Enemy getEnemy() {
        return null;
    }

    /**
	 * Always returns null for outside.
	 */
    public Item getItem() {
        return null;
    }

    /**
	 * Does nothing, since enemies can't exist outside.
	 */
    public void setEnemy(Enemy enemy) {
    }

    /**
	 * Does nothing, since items can't exist outside.
	 */
    public void setItem(Item item) {
    }

    /**
	 * Outside objects will never be accessible to enemies.
	 */
    public boolean isEnemyAccessible() {
        return false;
    }

    /**
	 * Outside objects can not be destroyed.
	 */
    public boolean isDestroyable() {
        return false;
    }

    /**
	 * Writes this land object to the output stream.
	 */
    public void save(ObjectOutputStream oos) {
        try {
        } catch (Exception e) {
        }
    }

    /**
	 * Outside objects can't be explored.
	 */
    public boolean hasBeenExplored() {
        return false;
    }

    /**
	 * Does nothing for outside objects.
	 * @param bool Boolean
	 */
    public void setHasBeenExplored(boolean bool) {
    }

    /**
	 * Does nothing, since there is no information to load.
	 */
    public void load(ObjectInputStream ois) {
    }

    /**
	 * Does nothing, since there is no information to load.
	 * From the map file.
	 */
    public void initLoad(ObjectInputStream ois) {
    }
}
