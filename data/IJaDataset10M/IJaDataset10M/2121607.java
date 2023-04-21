package common.object;

/** Static object, super class for all objects */
public class StaticObject implements Comparable<StaticObject> {

    public final int id;

    public float x, y;

    public int w, h;

    /** Create static object */
    public StaticObject(int id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    /** Compares this object with another by the y coordinate on the screen */
    public int compareTo(StaticObject o) {
        return (int) (this.x + this.y + this.h - o.x - o.y - o.h);
    }

    /** Return type, means kind of unit etc. */
    public int getType() {
        return 0;
    }

    /** Return object type, children should implement this method */
    public int getObjectType() {
        return 0;
    }
}
