package org.light.portal.search;

import org.light.portal.core.model.Entity;

/**
 * 
 * @author Jianmin Liu
 **/
public interface Indexer {

    public static final String _TYPE_ID = "entity";

    public static final String _ENTRY_ID = "entryId";

    public static final char _SORT_LAST = 255;

    public void init();

    public boolean isReady();

    public String getType();

    public void reIndex(long orgId);

    public void reIndex(Class klass, long orgId);

    public void reIndex(long orgId, boolean replicationFlag);

    public void reIndex(Class klass, long orgId, boolean replicationFlag);

    public void deleteIndex(Entity entity);

    public void updateIndex(Entity entity);
}
