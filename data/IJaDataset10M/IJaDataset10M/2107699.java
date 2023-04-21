package common.object;

import common.*;

/** Object on a map */
public class MapObject extends StaticObject {

    public int objectType;

    public int spawningTime;

    public byte alliance;

    public int npcType;

    public int mapArea;

    /** Create map object */
    public MapObject(int id, int x, int y, int objectType) {
        super(id, x, y);
        this.objectType = objectType;
    }

    /** Get x position as an int */
    public int getX() {
        return (int) x;
    }

    /** Get y position as an int */
    public int getY() {
        return (int) y;
    }

    /** Return type */
    public int getType() {
        return id;
    }

    /** Return object type */
    public int getObjectType() {
        return objectType;
    }
}
