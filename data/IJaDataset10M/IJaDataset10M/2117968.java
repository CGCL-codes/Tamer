package mipt.data.store.impl;

import mipt.data.*;
import mipt.data.store.*;
import mipt.data.store.table.DataChanger;
import mipt.data.store.table.DataStorage;

/**
 * Implementation for owned PersistentData methods (with DataStorage)
 *   but not for Data methods (and MutableData methods).
 * Note: supports getting ints, longs or strings (ids) for Data fields. 
 * @author Evdokimov
 */
public abstract class AbstractPersistentData implements PersistentData {

    protected DataID id;

    protected DataStorage storage;

    /**
	 * 
	 * @param id mipt.data.store.DataID
	 * @param storage mipt.data.store.DataStorage
	 */
    public AbstractPersistentData(DataID id, DataStorage storage) {
        this.id = id;
        this.storage = storage;
    }

    /**
	 * This method coincides with (but not implement!) mipt.data.DataWrapper.changeAs() method. 
	 */
    public boolean changeAs(ComparableData newContents) {
        if (storage instanceof DataChanger) return ((DataChanger) storage).changeData(this, newContents);
        if (newContents.equals(this)) return true;
        setFields(newContents.getFields());
        return storage.saveData(this);
    }

    /**
	 * @see mipt.data.store.StoredData#delete()
	 */
    public final boolean delete() {
        return storage.deleteData(this);
    }

    /**
	 * @see mipt.data.store.StoredData#save()
	 */
    public final boolean save() {
        if (storage == null) return false;
        return storage.saveData(this);
    }

    /**
	 * @see mipt.data.store.PersistentData#getID()
	 */
    public final DataID getID() {
        return id;
    }

    /**
	 * @see mipt.data.DataWrapper#getId()
	 */
    public int getId() {
        return getID().toInt();
    }

    /**
	 * This method could be overridden if you use not DefaultDataID & not TypedLongDataID.
	 * @see mipt.data.Data#getType()
	 */
    public String getType() {
        return storage != null ? storage.getDataType() : getType(id);
    }

    /**
	 * Implementation of getType() for DefaultDataID and TypedLongDataID.
	 * Returns "" for other DataID implementations.  
	 */
    public static String getType(DataID id) {
        if (id instanceof DefaultDataID) return ((DefaultDataID) id).getType();
        if (id instanceof TypedLongDataID) return ((TypedLongDataID) id).getType();
        return "";
    }

    /**
	 * This method is not defined in PersistentData interface because all can be done by data itself
	 */
    public final DataStorage getStorage() {
        return storage;
    }

    /**
	 * Is needed rarely 
	 */
    public void setStorage(DataStorage storage) {
        this.storage = storage;
    }

    /**
	 * The same as in AbstractData but uses this.isFieldEqual().
	 */
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Data)) return false;
        Data data = (Data) obj;
        String[] fieldNames = getFieldNames();
        int n = fieldNames.length;
        for (int i = 0; i < n; i++) {
            if (!isFieldEqual(get(fieldNames[i]), data.get(fieldNames[i]))) return false;
        }
        return true;
    }

    /**
	 * @see mipt.data.Data#isFieldEqual(java.lang.Object, java.lang.Object)
	 */
    public boolean isFieldEqual(Object field, Object otherField) {
        if (field == null) {
            return otherField == null;
        } else {
            if (field instanceof DataSet) {
                return true;
            } else if (field instanceof PersistentData) {
                field = ((PersistentData) field).getID().toReference();
            }
            if (otherField != null) {
                if (otherField instanceof DataSet) {
                    return true;
                } else if (otherField instanceof PersistentData) {
                    otherField = ((PersistentData) otherField).getID().toReference();
                }
                if (field.equals(otherField)) return true;
            }
            return field.equals(otherField);
        }
    }

    /**
	 * @see mipt.data.Data#isFieldNull(java.lang.Object)
	 */
    public boolean isFieldNull(Object fieldValue) {
        if (fieldValue == null) return true;
        return false;
    }

    /**
	 * 
	 * @return boolean
	 * @param field java.lang.Object
	 */
    protected boolean parseBoolean(Object field) {
        if (field == null) return false;
        return ((Boolean) field).booleanValue();
    }

    /**
	 * 
	 * @return double
	 * @param field java.lang.Object
	 */
    protected double parseDouble(Object field) {
        if (field == null) return Double.NaN;
        return ((Double) field).doubleValue();
    }

    /**
	 * 
	 * @return int
	 * @param field java.lang.Object
	 */
    protected int parseInt(Object field) {
        if (field == null) return Integer.MIN_VALUE;
        if (field instanceof Number) return ((Number) field).intValue(); else return ((PersistentData) field).getID().toInt();
    }

    /**
	 * 
	 * @return long
	 * @param field java.lang.Object
	 */
    protected long parseLong(Object field) {
        if (field == null) return Long.MIN_VALUE;
        if (field instanceof Number) return ((Number) field).longValue(); else return ((PersistentData) field).getID().toLong();
    }

    /**
	 * 
	 * @return String
	 * @param field java.lang.Object
	 */
    protected String parseString(Object field) {
        if (field == null) return null;
        if (field instanceof PersistentData) return ((PersistentData) field).getID().toString(); else return field.toString();
    }

    /**
	 * 
	 */
    public String toString() {
        return id == null ? "(no id)" : id.toString();
    }
}
