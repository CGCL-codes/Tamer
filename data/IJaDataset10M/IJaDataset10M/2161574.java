package org.jtools.mvc.impl.meta;

import org.jtools.mvc.meta.MVCKeyMetaData;

/**
 * TODO type-description
 * @author <a href="mailto:rainer.noack@jtools.org">Rainer Noack</a>
 */
public class SimpleMVCKeyMetaData<KeyIdType, ColumnIdType> implements MVCKeyMetaData<KeyIdType, ColumnIdType> {

    private KeyIdType keyId;

    private Class<ColumnIdType> columnIdClass;

    /**
     * 
     */
    public SimpleMVCKeyMetaData(KeyIdType keyId, Class<ColumnIdType> columnIdClass) {
        this.keyId = keyId;
        this.columnIdClass = columnIdClass;
    }

    public KeyIdType getMVCKeyId() {
        return keyId;
    }

    public Class<ColumnIdType> getMVCColumnIdClass() {
        return columnIdClass;
    }
}
