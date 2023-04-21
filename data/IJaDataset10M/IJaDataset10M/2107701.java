package org.granite.tide.seam;

/**
 * @author cingram
 */
public interface ITidePersistenceManager {

    public Object attachEntity(Object entity, String propertyName, boolean merge);

    public Object mergeEntity(Object entity);

    public Object initializeObject(Object object);
}
