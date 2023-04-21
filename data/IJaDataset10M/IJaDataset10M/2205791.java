package server.object;

/** Class with informations when a object was last updated etc. to a client */
public class UpdateObject {

    public long lastSeen;

    public long lastUpdate;

    public int id;

    public boolean lastTakeable;

    /** Create update object */
    public UpdateObject(int id, long time) {
        this.id = id;
        lastSeen = time;
        lastUpdate = time;
    }
}
