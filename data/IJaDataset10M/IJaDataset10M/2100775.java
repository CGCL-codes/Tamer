package org.nakedobjects.nos.store.sql;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.adapter.NakedValue;
import org.nakedobjects.noa.reflect.NakedObjectField;

public interface ValueMapper {

    String valueAsDBString(NakedValue value) throws SqlObjectStoreException;

    void setFromDBColumn(String columnName, NakedObjectField field, NakedObject object, Results results) throws SqlObjectStoreException;

    String columnType();
}
