package org.subrecord.model;

import org.subrecord.util.Commons;

/**
 * Model of a full/absolute record identifier domain-table-id
 * 
 * @author przemek
 * 
 */
public class RecordIdentity<T> extends TableIdentity<T> {

    private T id;

    public RecordIdentity(T domain, T table, T id) {
        super(domain, table);
        this.id = id;
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return Commons.glue("domain=", getDomain(), ", table=", getTable(), ", id=", id);
    }
}
