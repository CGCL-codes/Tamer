package org.datanucleus.sql4o;

import com.db4o.reflect.ReflectField;

/**
 * Implementation of a row of results.
 */
public class ObjectWrapper implements Result {

    private Object ob;

    private ObjectSetWrapper objectSetWrapper;

    public ObjectWrapper(ObjectSetWrapper objectSetWrapper, Object ob) {
        this.objectSetWrapper = objectSetWrapper;
        this.ob = ob;
    }

    public Object getObject(int fieldIndex) throws Sql4oException {
        ReflectField f = objectSetWrapper.getFieldForColumn(fieldIndex);
        return getFieldValue(f, ob);
    }

    public Object getObject(String fieldName) throws Sql4oException {
        ReflectField f = objectSetWrapper.getFieldForColumn(fieldName);
        return getFieldValue(f, ob);
    }

    private Object getFieldValue(ReflectField f, Object ob) {
        return f.get(ob);
    }

    public Object getBaseObject(int objectIndex) {
        return ob;
    }

    /**
     * Accessor for the number of returned fields. If the user has specified particular fields then returns that number.
     * If the user specified * notation then the total number of fields is returned.
     * @return Number of fields per item
     */
    public int getNumberOfFields() {
        return objectSetWrapper.getNumberOfFields();
    }

    /**
     * Accessor for the name of a field returned in an index position.
     * @param fieldIndex Position
     * @return The field name returned there
     */
    public String getFieldNameForColumn(int fieldIndex) {
        return objectSetWrapper.getFieldNameForColumn(fieldIndex);
    }
}
