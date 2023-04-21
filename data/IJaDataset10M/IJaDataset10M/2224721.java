package com.completex.objective.components.persistency;

import java.util.HashMap;
import java.util.Map;

/**
 * Container for one Record value.
 * Normally it would map to a database table column value.
 *
 * @author Gennady Krizhevsky
 */
public class PersistentEntry implements Cloneable {

    private MetaColumn column;

    private Record record;

    private Object value;

    private Object originalValue;

    private boolean dirty;

    private boolean autoPadChars;

    public static final String KEY_VALUE = "value";

    public static final String KEY_ORIGINAL_VALUE = "originalValue";

    /**
     * Factory method
     *
     * @param useRecord if true new PersistentEntry will be created with Record inside
     * @return new PersistentEntry
     */
    public PersistentEntry newInstance(boolean useRecord) {
        return useRecord ? new PersistentEntry(column, record.newRecord()) : new PersistentEntry(column, null);
    }

    /**
     *
     * @param column parent MetaColumn
     * @param record parent Record
     */
    public PersistentEntry(MetaColumn column, Record record) {
        this.column = column;
        this.record = record;
    }

    /**
     * Set parent Record
     *
     * @param record
     */
    void setRecord(Record record) {
        this.record = record;
    }

    /**
     *
     * @return parent Record
     */
    public Record getRecord() {
        return record;
    }

    /**
     *
     * @return parent MetaColumn
     */
    public MetaColumn getColumn() {
        return column;
    }

    /**
     * Copies value, originalValue and dirty flag from PersistentEntry passed as parameter to
     * this one
     *
     * @param entry
     * @return itself
     */
    PersistentEntry copyValues(PersistentEntry entry) {
        value = entry.getValue();
        originalValue = entry.getOriginalValue();
        dirty = entry.isDirty();
        return this;
    }

    /**
     * Set autoPadChars flag meaning that for ColumnType.CHAR types
     * the value stored in String will be right padded with spaces to the length of
     * the parent MetaColumn
     *
     * @see MetaColumn#v2c(Object)
     * @param autoPadChars
     */
    public void setAutoPadChars(boolean autoPadChars) {
        this.autoPadChars = autoPadChars;
    }

    /**
     *
     * @return true ColumnType.CHAR types have to be auto padded to the length of
     * the parent MetaColumn
     */
    public boolean isAutoPadChars() {
        return autoPadChars;
    }

    /**
     *
     * @return value stored in this entry
     */
    public Object getValue() {
        return value;
    }

    /**
     * Set value if it is different from the one kept in this.value field
     *
     * @param value
     */
    public void setValueIfDiff(Object value) {
        if (different(this.value, value)) {
            setValue(value);
        }
    }

    /**
     * Set value
     *
     * @param value
     */
    public void setValue(Object value) {
        value(value);
        setDirty(true);
    }

    /**
     *
     * @return get value as Number
     * @throws OdalRuntimePersistencyException if type transformation is not possible
     */
    public Number getNumber() {
        try {
            return (Number) getValue();
        } catch (ClassCastException e) {
            handleClassCastException(e, value, "Number");
        }
        return null;
    }

    /**
     *
     * @return get value as String
     * @throws OdalRuntimePersistencyException if type transformation is not possible
     */
    public String getString() {
        try {
            return (String) getValue();
        } catch (ClassCastException e) {
            handleClassCastException(e, value, "String");
        }
        return null;
    }

    private void handleClassCastException(Exception e, Object sourceValue, String targetClass) {
        if (e instanceof ClassCastException) {
            throw new OdalRuntimePersistencyException(": Cannot cast value of class [" + (sourceValue != null ? sourceValue.getClass().getName() : "null") + "] to class [" + targetClass + "]", e);
        }
    }

    /**
     *
     * @return map representation of PersistentEntry
     */
    protected Map toMap() {
        HashMap map = new HashMap();
        map.put(KEY_VALUE, value);
        map.put(KEY_ORIGINAL_VALUE, originalValue);
        return map;
    }

    /**
     * Populates PersistentEntry from its map representation
     *
     */
    protected void fromMap(Map map) {
        value = map.get(KEY_VALUE);
        originalValue = map.get(KEY_ORIGINAL_VALUE);
    }

    /**
     * Set both current and original values.
     * This method should be called only if Record state is NEW_INITIALIZING
     *
     * @param value
     * @param originalValue
     */
    public void setValue(Object value, Object originalValue) {
        setUnmarkedValue(value, originalValue);
        setDirty(true);
    }

    /**
     * Set both current and original values w/o changing its "dirty" state
     *
     * @param value
     * @param originalValue
     */
    public void setUnmarkedValue(Object value, Object originalValue) {
        if (!ColumnType.isBinary(getColumn().getType())) {
            setOriginalValue(originalValue);
        } else {
            setOriginalValue(ColumnType.NULL_BINARY_OBJECT);
        }
        value(value);
    }

    private void value(Object value) {
        if (autoPadChars) {
            this.value = column.v2c(value);
        } else {
            this.value = value;
        }
    }

    /**
     *
     * @return originalValue
     */
    public Object getOriginalValue() {
        return originalValue;
    }

    /**
     *
     * @return originalValue as String
     * @throws OdalRuntimePersistencyException if type transformation is not possible
     */
    public String getOriginalString() {
        try {
            return (String) getOriginalValue();
        } catch (ClassCastException e) {
            handleClassCastException(e, value, "String");
        }
        return null;
    }

    /**
     *
     * @return originalValue as Number
     * @throws OdalRuntimePersistencyException if type transformation is not possible
     */
    public Number getOriginalNumber() {
        try {
            return (Number) getOriginalValue();
        } catch (ClassCastException e) {
            handleClassCastException(e, value, "Number");
        }
        return null;
    }

    private void setOriginalValue(Object originalValue) {
        this.originalValue = originalValue;
    }

    /**
     * Set "dirty" flag
     *
     * @param dirty
     */
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
        if (!column.isPrimaryKey() && record != null) {
            record.setHasDirtyNonKeyFields(dirty);
        }
        if (dirty && record != null) {
            record.moveRecordState();
        }
    }

    /**
     *
     * @return "dirty" flag
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     *
     * @param before
     * @param after
     * @return true if "before" and "after" objects are different
     */
    public static boolean different(Object before, Object after) {
        if (before == null && after == null) {
            return false;
        }
        if (before != null && !before.equals(after) || after != null && !after.equals(before)) {
            if (before != null && after != null) {
                if (before instanceof Number) {
                    return !String.valueOf(before).equals(String.valueOf(after));
                }
            }
            return true;
        }
        return false;
    }

    public ColumnType getType() {
        return column == null ? null : column.getType();
    }

    /**
     *
     * @return copy of this object
     * @throws CloneNotSupportedException
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Object cloneDeep() throws CloneNotSupportedException {
        PersistentEntry entry = (PersistentEntry) super.clone();
        entry.column = column.cloneSafe();
        return entry;
    }

    public PersistentEntry cloneSafe() {
        try {
            return (PersistentEntry) clone();
        } catch (CloneNotSupportedException e) {
            throw new OdalRuntimePersistencyException("Cannot clone PersistentEntry", e);
        }
    }

    public PersistentEntry cloneDeepSafe() {
        try {
            return (PersistentEntry) cloneDeep();
        } catch (CloneNotSupportedException e) {
            throw new OdalRuntimePersistencyException("Cannot clone PersistentEntry", e);
        }
    }

    public String toString() {
        return new StringBuffer().append("{").append(" column = ").append(column.getColumnName()).append(" value = ").append(value).append(" originalValue = ").append(originalValue).append(" dirty = ").append(dirty).append("}").toString();
    }
}
