package model.dungeon;

import model.abstracts.GameObject;

/**
 *
 * @author Vince Kane
 */
public class DungeonFeatures {

    public static final String SOLID_ROCK = "PermanentWall";

    public static final String GRANITE = "Granite";

    public static final String FLOOR = "Floor";

    public static GameObject get(String feature) {
        GameObject object = null;
        if (feature.equals(SOLID_ROCK)) object = PermanentWall.instance();
        return object;
    }
}
