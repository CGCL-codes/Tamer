package org.hironico.database.driver.cache;

/**
 * Objet qui modélise un evénnement d'ajout/retrait des objets dans le cache SQL.
 * @author hironico
 * @since 0.0.12
 */
public class SQLCacheEvent {

    public static final int EVENT_TYPE_ADD = -1;

    public static final int EVENT_TYPE_REMOVE = -2;

    public static final int OBJECT_TYPE_SQLQUERY = -3;

    public static final int OBJECT_TYPE_RESULTSET = -4;

    private int eventType = 0;

    private int objectType = 0;

    private Long objectId = new Long(0);

    public SQLCacheEvent(int eventType, int objectType, Long objectId) {
        this.eventType = eventType;
        this.objectType = objectType;
        this.objectId = objectId;
    }

    public int getEventType() {
        return eventType;
    }

    public int getObjectType() {
        return objectType;
    }

    public Long getObjectId() {
        return objectId;
    }
}
