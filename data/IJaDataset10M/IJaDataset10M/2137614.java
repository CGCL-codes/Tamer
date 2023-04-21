package org.caleigo.core.meta;

import org.caleigo.core.*;

public interface IMetaConsumer {

    public void beginDataSource(String codeName, String sourceName, String displayName, String version, boolean readOnly);

    public void beginEntity(String codeName, String sourceName, String displayName, int entityType, int flags, int cacheTime);

    public void addField(String codeName, String sourceName, String displayName, DataType dataType, int length, int flags, Object defValue);

    public void beginEntityRelation(String refEntitySourceName, String targetEntitySourceName, String codeName, String sourceName, String forwardName, String reverseName);

    public void addFieldRelation(String refFieldSourceName, String targetFieldSourceName);

    public void endEntityRelation();

    public void endEntity();

    public void endDataSource();
}
