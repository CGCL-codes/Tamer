package mipt.data.store.indexed;

import mipt.data.DataSet;
import mipt.data.MutableData;
import mipt.data.indexed.IndexedData;
import mipt.data.indexed.IndexedMutableComparableData;
import mipt.data.indexed.IndexedMutableData;
import mipt.data.store.DataID;
import mipt.data.store.impl.DefaultSwizzlingData;
import mipt.data.store.table.DataLoader;
import mipt.data.store.table.DataStorage;

/**
 * Unlike store.indexed.IndexedDefaultSwizzlingData, use all methods of IndexedDataLoader
 *  but not reduce non-indexed methods to indexed ones
 * @author Evdokimov
 */
public class DefaultIndexedSwizzlingData extends DefaultSwizzlingData implements IndexedSwizzlingData {

    /**
	 * DataLoader is encouraged to be IndexedDataLoader
	 */
    public DefaultIndexedSwizzlingData(DataID id, DataStorage storage, DataLoader loader, IndexedMutableComparableData contents) {
        super(id, storage, loader, contents);
    }

    /**
	 * 
	 * @return mipt.data.indexed.IndexedMutableComparableData
	 */
    public final IndexedMutableComparableData getIndexedContents() {
        return (IndexedMutableComparableData) getContents();
    }

    /**
	 * 
	 * @return mipt.data.indexed.IndexedMutableData
	 */
    public final IndexedMutableData getIndexedChanger() {
        return (IndexedMutableData) getChanger();
    }

    /**
	 * 
	 */
    protected MutableData initChanger() {
        return new IndexedAccessPersistentData.IndexedChanger(this, getIndexedContents(), getIndexedContents());
    }

    public final double getDouble(int index) {
        return parseDouble(getIndexedContents().get(index));
    }

    public final int getInt(int index) {
        return parseInt(getIndexedContents().get(index));
    }

    public final long getLong(int index) {
        return parseLong(getIndexedContents().get(index));
    }

    public final String getString(int index) {
        return parseString(getIndexedContents().get(index));
    }

    public final int getFieldCount() {
        return getIndexedContents().getFieldCount();
    }

    public final boolean set(Object newValue, int index) {
        return getIndexedContents().set(newValue, index);
    }

    public final boolean setBoolean(boolean newValue, int index) {
        return getIndexedContents().setBoolean(newValue, index);
    }

    public final boolean setDouble(double newValue, int index) {
        return getIndexedContents().setDouble(newValue, index);
    }

    public final boolean setInt(int newValue, int index) {
        return getIndexedContents().setInt(newValue, index);
    }

    /**
	 * @see mipt.data.indexed.IndexedData#getBoolean(int)
	 */
    public boolean getBoolean(int index) {
        try {
            return parseBoolean(getIndexedContents().get(index));
        } catch (ClassCastException e) {
            Object result = getIndexedContents().get(index);
            if (!(result instanceof DataSet)) throw new ClassCastException("field(" + index + ") should be of boolean or DataSet type");
            return true;
        }
    }

    public final Object get(int index) {
        return get(null, index);
    }

    public Object get(String dataType, int index) {
        Object value = getIndexedContents().get(index);
        if (value == null) {
            if (loader instanceof IndexedDataLoader) value = ((IndexedDataLoader) loader).loadField(id, null, index, dataType); else value = loader.loadField(id, null, getIndexedContents().fieldName(index), dataType);
            if (value != null) getIndexedContents().set(value, index);
        }
        return value;
    }

    public final IndexedData getData(int fieldIndex) {
        return getData(null, fieldIndex);
    }

    public final IndexedData getData(String dataType, int fieldIndex) {
        Object value = getIndexedContents().get(fieldIndex);
        if (value instanceof IndexedData) return (IndexedData) value;
        IndexedData data;
        if (loader instanceof IndexedDataLoader) data = ((IndexedDataLoader) loader).loadDataField(id, value, fieldIndex, dataType); else data = (IndexedData) loader.loadDataField(id, value, getIndexedContents().fieldName(fieldIndex), dataType);
        getIndexedContents().set(data, fieldIndex);
        return data;
    }

    public final DataSet getDataSet(int index) {
        return getDataSet(null, index);
    }

    public final DataSet getDataSet(String dataType, int index) {
        Object value = getIndexedContents().get(index);
        if (value instanceof DataSet) return (DataSet) value;
        DataSet dataSet;
        if (loader instanceof IndexedDataLoader) dataSet = ((IndexedDataLoader) loader).loadDataSetField(id, value, index, dataType); else dataSet = loader.loadDataSetField(id, value, getIndexedContents().fieldName(index), dataType);
        if (dataSet != null) getIndexedContents().set(dataSet, index);
        return dataSet;
    }

    /**
	 * Replaces the given Data field in the given contents with DataID.toReference() and DataSet field with null
	 */
    public static final void unswizzleField(IndexedMutableComparableData contents, int index) {
        Object field = contents.get(index);
        if (field == null) return;
        Object obj = DefaultSwizzlingData.unswizzleObject(field);
        if (field != obj) contents.set(obj, index);
    }

    /**
	 * Replaces all Data fields in the given contents with DataID.toReference() and DataSet fields with null
	 */
    public static final void unswizzleFields(IndexedMutableComparableData contents) {
        int n = contents.getFieldCount();
        for (int i = 0; i < n; i++) unswizzleField(contents, i);
    }

    public final void unswizzleField(String field) {
        unswizzleField(getIndexedContents().fieldIndex(field));
    }

    public void unswizzleField(int index) {
        unswizzleField(getIndexedContents(), index);
    }

    public void unswizzleFields() {
        unswizzleFields(getIndexedContents());
    }
}
