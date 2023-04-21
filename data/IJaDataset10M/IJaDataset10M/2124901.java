package com.gwtaf.ext.core.client.store;

import com.gwtaf.ext.core.client.record.IRecordField;
import com.gwtaf.ext.core.client.record.recordfields.IntegerRecordField;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;

public class GenericStoreAdapter<T> implements IStoreAdapter<T> {

    private GroupingStoreEx<T> store;

    private IRecordField displayField;

    private IRecordField idField;

    private IRecordField dataField;

    public GenericStoreAdapter(GroupingStoreEx<T> store, IRecordField displayField, IRecordField dataField, IRecordField idField) {
        this.store = store;
        this.displayField = displayField;
        this.idField = idField;
        this.dataField = dataField;
    }

    @SuppressWarnings("unchecked")
    public T getData(String id) {
        Record r = null;
        if (this.idField instanceof IntegerRecordField) r = store.lookupRecord(this.idField, Integer.valueOf(id)); else r = store.lookupRecord(this.idField, id);
        if (r != null) return (T) dataField.getValue(r);
        return null;
    }

    public String getID(T data) {
        if (data == null) return null;
        Object id = this.idField.transform(data);
        if (id == null) return null;
        Record r = store.lookupRecord(this.idField, id);
        if (r == null) store.add(data);
        return id.toString();
    }

    public Store getStore() {
        return store;
    }

    public String getDisplayField() {
        return displayField.getName();
    }

    public String getValueField() {
        return this.idField.getName();
    }
}
