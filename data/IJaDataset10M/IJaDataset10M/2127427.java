package mipt.data.store.impl;

import mipt.data.*;
import mipt.data.store.DataID;
import mipt.data.store.table.DataChanger;
import mipt.data.store.table.DataStorage;

/**
 * Assumes that this object contain the reference to a Data storing fields in memory. 
 * Implements getters and setters through abstract getRealData() and getMutableRealData().
 * @author Evdokimov
 */
public abstract class AccessPersistentData extends AbstractPersistentData {

    protected MutableData changer = null;

    public static class Changer implements MutableData {

        protected AccessPersistentData parent;

        public Changer(AccessPersistentData parent) {
            this.parent = parent;
        }

        public boolean set(Object newValue, String field) {
            if (parent.getStorage() instanceof DataChanger) {
                Object oldValue = parent.getRealData().get(field);
                if (parent.isFieldEqual(oldValue, newValue)) return true;
                return ((DataChanger) parent.getStorage()).changeField(parent, newValue, field);
            }
            parent.set(newValue, field);
            return parent.getStorage().saveData(parent);
        }

        public boolean setBoolean(boolean newValue, String field) {
            return set(newValue ? Boolean.TRUE : Boolean.FALSE, field);
        }

        public boolean setInt(int newValue, String field) {
            return set(new Integer(newValue), field);
        }

        public boolean setDouble(double newValue, String field) {
            return set(new Double(newValue), field);
        }

        public boolean setFields(Object[] newFields) {
            if (parent.getStorage() instanceof DataChanger) return ((DataChanger) parent.getStorage()).changeData(parent, newFields);
            parent.setFields(newFields);
            return parent.getStorage().saveData(parent);
        }
    }

    /**
	 * 
	 */
    public AccessPersistentData(DataID id, DataStorage storage) {
        super(id, storage);
    }

    public final MutableData getChanger() {
        if (changer == null) changer = initChanger();
        return changer;
    }

    /**
	 * 
	 */
    protected MutableData initChanger() {
        return new Changer(this);
    }

    public Object get(String field) {
        return getRealData().get(field);
    }

    public boolean getBoolean(String field) {
        return parseBoolean(getRealData().get(field));
    }

    public final int getInt(String field) {
        return parseInt(getRealData().get(field));
    }

    public final long getLong(String field) {
        return parseLong(getRealData().get(field));
    }

    public final String getString(String field) {
        return parseString(getRealData().get(field));
    }

    public Data getData(String field) {
        return getRealData().getData(field);
    }

    public DataSet getDataSet(String field) {
        return getRealData().getDataSet(field);
    }

    public final double getDouble(String field) {
        return parseDouble(getRealData().get(field));
    }

    public final String[] getFieldNames() {
        return getRealData().getFieldNames();
    }

    public final Object[] getFields() {
        return getRealData().getFields();
    }

    public boolean set(Object newValue, String field) {
        return getRealMutableData().set(newValue, field);
    }

    public final boolean setBoolean(boolean newValue, String field) {
        return getRealMutableData().setBoolean(newValue, field);
    }

    public final boolean setDouble(double newValue, String field) {
        return getRealMutableData().setDouble(newValue, field);
    }

    public final boolean setFields(Object[] fields) {
        return getRealMutableData().setFields(fields);
    }

    public final boolean setInt(int newValue, String field) {
        return getRealMutableData().setInt(newValue, field);
    }

    /**
	 * 
	 */
    public String toString() {
        String result = super.toString();
        if (getRealData() != null) {
            result += ("=" + getRealData().toString());
        }
        return result;
    }

    /**
	 * 
	 */
    public abstract Data getRealData();

    /**
	 * 
	 */
    public abstract MutableData getRealMutableData();
}
