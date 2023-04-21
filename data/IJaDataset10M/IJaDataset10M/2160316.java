package org.jtools.mvc.impl;

import java.io.Serializable;
import java.util.Map;
import org.jpattern.data.Data;
import org.jpattern.data.DataHelper;
import org.jpattern.data.DataTransaction;
import org.jtools.mvc.MVCConstants;
import org.jtools.mvc.MVCData;
import org.jtools.mvc.MVCDataHelper;
import org.jtools.mvc.MVCServer;
import org.jtools.mvc.MVCTransaction;
import org.jtools.mvc.MVCValue;
import org.jtools.mvc.impl.util.CollectionFactory;

public class SimpleMVCData<T_EntityId, T_Key, T_ColumnId> implements MVCDataHelper<T_EntityId, T_Key, T_ColumnId>, Data, MVCTransaction, Serializable {

    public static final class SimpleValue<C, V> implements MVCValue.Writeable<C, V> {

        private long time;

        private V value;

        private final C column;

        public SimpleValue(C column) {
            this.column = column;
        }

        public V getMVCValue() {
            return value;
        }

        public long getMVCTime() {
            return time;
        }

        public C getMVCColumn() {
            return column;
        }

        public V setMVCValue(V value) {
            if (value == this.value) return value;
            V old = value;
            this.value = value;
            this.time = MVCConstants.TIMESTAMP_NEW;
            return old;
        }

        public boolean isMVCValueSet() {
            return value != null;
        }
    }

    private final boolean writeable;

    private Class getMVCColumnIdClass() {
        return mvcServer.getMVCServerMetaData().getMVCEntity(mvcEntityId).getMVCColumnIdClass();
    }

    public MVCValue<T_ColumnId, ?> getMVCValue(T_ColumnId column) {
        if (mvcValues == null) return null;
        return mvcValues.get(column);
    }

    public void executeMVC() {
    }

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    private transient MVCServer<T_EntityId> mvcServer;

    public MVCServer<T_EntityId> getMVCServer() {
        return mvcServer;
    }

    private T_EntityId mvcEntityId;

    public T_EntityId getMVCEntityId() {
        return mvcEntityId;
    }

    private final Map<T_ColumnId, MVCValue.Writeable<T_ColumnId, ?>> mvcValues;

    public MVCValue[] getMVCValues() {
        return mvcValues.values().toArray(new MVCValue[mvcValues.size()]);
    }

    public MVCValue.Writeable[] getMVCWriteableValues() {
        return mvcValues.values().toArray(new MVCValue.Writeable[mvcValues.size()]);
    }

    protected SimpleMVCData(MVCData<T_EntityId, T_Key, T_ColumnId> from, boolean writeable) {
        this.mvcServer = from.getMVCServer();
        this.mvcEntityId = from.getMVCEntityId();
        MVCValue.Writeable<T_ColumnId, ?>[] arr = from.getMVCWriteableValues();
        this.mvcValues = CollectionFactory.getCollectionFactory().createMap(getMVCColumnIdClass());
        for (MVCValue.Writeable<T_ColumnId, ?> val : arr) this.mvcValues.put(val.getMVCColumn(), val);
        this.writeable = writeable;
    }

    protected SimpleMVCData(MVCServer<T_EntityId> server, T_EntityId entityId, boolean writeable) {
        this.mvcServer = server;
        this.mvcEntityId = entityId;
        this.mvcValues = CollectionFactory.getCollectionFactory().createMap(getMVCColumnIdClass());
        this.writeable = writeable;
    }

    public MVCData<T_EntityId, T_Key, T_ColumnId> toMVCData(boolean clone) {
        if (writeable || clone) return new SimpleMVCData<T_EntityId, T_Key, T_ColumnId>(this, false);
        return this;
    }

    public MVCDataHelper<T_EntityId, T_Key, T_ColumnId> toMVCWriteableData(boolean clone) {
        if (!clone) return this;
        return new SimpleMVCData<T_EntityId, T_Key, T_ColumnId>(this, true);
    }

    public MVCValue.Writeable<T_ColumnId, ? extends Object> getMVCWriteableValue(T_ColumnId column) {
        MVCValue.Writeable<T_ColumnId, ? extends Object> value = mvcValues.get(column);
        if (value == null) {
            value = new SimpleValue<T_ColumnId, Object>(column);
            mvcValues.put(column, value);
        }
        return value;
    }

    public final MVCTransaction toMVCTransaction() {
        return (MVCTransaction) toMVCData(false);
    }

    public Object getDataId() {
        return getMVCEntityId();
    }

    public Data toData(boolean clone) {
        return (Data) toMVCData(clone);
    }

    public DataHelper toHelper(boolean clone) {
        return (DataHelper) toMVCWriteableData(clone);
    }

    public DataTransaction toTransaction() {
        return (DataTransaction) toMVCTransaction();
    }

    public Object getValue(Object columnId) {
        return getMVCValue((T_ColumnId) columnId).getMVCValue();
    }

    public boolean isValueSet(Object columnId) {
        return getMVCValue((T_ColumnId) columnId).isMVCValueSet();
    }
}
