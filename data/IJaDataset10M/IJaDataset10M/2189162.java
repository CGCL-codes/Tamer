package org.granite.tide;

/**
 * @author cingram
 */
public interface ITidePersistenceManager {

    public Object attachEntity(Object entity, String propertyName, boolean merge);

    public Object mergeEntity(Object entity);

    public Object findEntity(Object entity);

    public boolean isInitialized(Object object);

    public Object initializeObject(Object object);
}
