package mipt.data.store.impl;

import mipt.common.Named;
import mipt.data.*;
import mipt.data.store.*;
import mipt.data.store.search.Criteria;
import mipt.data.store.search.Criterion;
import mipt.data.store.table.DataLoader;
import mipt.data.store.table.DataStorage;

/**
 * DefaultPersistentData using DataLoader to swizzle contents (if it is not set in constructor),
 *   and then to swizzle Object fields (usually LOBs), Data fields and DataSet fields.
 * Also can have name (it is senseless to create this object without contents and without name simultaneously).
 * Note: the name can often be also accessed through the contents but only this
 *  data (not the contents) implements mipt.common.Named interface used in UI lists, comboboxes, trees. 
 * Note: DataLoader is responsible for format of storing unswizzled DataSet fields but this.getBoolean()
 *  (only!) assumes that such fields is booleans. 
 */
public class DefaultSwizzlingData extends DefaultPersistentData implements SwizzlingData, Named {

    protected DataLoader loader;

    private String name;

    /**
	 * 
	 */
    public DefaultSwizzlingData(DataID id, DataStorage storage, DataLoader loader, MutableComparableData contents) {
        this(id, storage, loader, contents, null);
    }

    /**
	 * 
	 */
    public DefaultSwizzlingData(DataID id, DataStorage storage, DataLoader loader, String name) {
        this(id, storage, loader, null, name);
    }

    /**
	 * 
	 */
    public DefaultSwizzlingData(DataID id, DataStorage storage, DataLoader loader, MutableComparableData contents, String name) {
        super(id, storage, contents);
        this.loader = loader;
        this.name = name;
    }

    /**
	 * @return loader
	 */
    public final DataLoader getLoader() {
        return loader;
    }

    /**
	 * @see mipt.data.DataWrapper#getContents()
	 */
    public final MutableComparableData getContents() {
        if (contents == null) contents = loader.loadDataContents(id);
        return contents;
    }

    public void unloadContents() {
        contents = null;
    }

    /**
	 * @see mipt.common.Named#getName()
	 */
    public final String getName() {
        return name;
    }

    /**
	 * @see mipt.common.Named#setName(String)
	 * @see #setNameBoth(String)
	 */
    public boolean setName(String name) {
        this.name = name;
        return true;
    }

    /**
	 * Sets the name both in this object and in the contents (if the contents contains name).
	 * Note that save() is needed after setting a name to reflect the change in the storage;
	 *  that is why this method can be used only in the case when immediate saving is bad.  
	 * @return false if name field does not exist (=> it can't be changed in the contents);
	 *  but this.name is set even in that case.
	 * @see mipt.common.Named#changeName(java.lang.String)
	 */
    public boolean setNameBoth(String name) {
        this.name = name;
        String nameFieldName = loader.getNameFieldName(this.getType());
        if (nameFieldName == null) return false;
        return getContents().set(name, nameFieldName);
    }

    /**
	 * @see mipt.common.Named#setName(String)
	 * Sets the name both in this object and in the contents (if the contents contains name)
	 *  and immediately changes it in the storage.  
	 * @return false if name field does not exist (=> it can't be changed in the contents
	 *  and in the storage); but this.name is set even in that case.
	 */
    public boolean changeName(String name) {
        this.name = name;
        String nameFieldName = loader.getNameFieldName(this.getType());
        if (nameFieldName == null) return false;
        return getChanger().set(name, nameFieldName);
    }

    public final Object get(String field) {
        return get(null, field);
    }

    public Object get(String dataType, String field) {
        Object value = getContents().get(field);
        if (value == null) {
            value = loader.loadField(id, null, field, dataType);
            if (value != null) getContents().set(value, field);
        }
        return value;
    }

    /**
	 * @see mipt.data.Data#getBoolean(java.lang.String)
	 */
    public boolean getBoolean(String field) {
        try {
            return super.getBoolean(field);
        } catch (ClassCastException e) {
            Object result = getContents().get(field);
            if (!(result instanceof DataSet)) throw new ClassCastException(field + " should be of boolean or DataSet type");
            return true;
        }
    }

    public final Data getData(String field) {
        return getData(null, field);
    }

    public final Data getData(String dataType, String field) {
        Object value = getContents().get(field);
        if (value instanceof Data) return (Data) value;
        Data data = loader.loadDataField(id, value, field, dataType);
        getContents().set(data, field);
        return data;
    }

    public final DataSet getDataSet(String field) {
        return getDataSet(null, field);
    }

    public final DataSet getDataSet(String dataType, String field) {
        Object value = getContents().get(field);
        if (value instanceof DataSet) return (DataSet) value;
        DataSet dataSet = loader.loadDataSetField(id, value, field, dataType);
        if (dataSet != null) getContents().set(dataSet, field);
        return dataSet;
    }

    /**
	 * Return true if the given field is null.
	 */
    public boolean isFieldNull(Object fieldValue) {
        return StatelessPersistentData.isValueNull(fieldValue);
    }

    /**
	 * 
	 * @return java.lang.Object
	 * @param obj java.lang.Object
	 */
    public static final Object unswizzleObject(Object obj) {
        if (obj == null) return null;
        if (obj instanceof PersistentData) return ((PersistentData) obj).getID().toReference(); else if (obj instanceof DataSet) return null;
        return obj;
    }

    /**
	 * 
	 * @return java.lang.Object
	 * @param obj java.lang.Object
	 */
    public static final Object[] unswizzleArray(Object[] obj) {
        if (obj == null) return null;
        for (int i = 0; i < obj.length; i++) {
            obj[i] = unswizzleObject(obj[i]);
        }
        return obj;
    }

    /**
	 * Replaces the given Data field in the given contents with DataID.toReference() and DataSet field with null
	 */
    public static final void unswizzleField(MutableComparableData contents, String name) {
        Object field = contents.get(name);
        if (field == null) return;
        Object obj = unswizzleObject(field);
        if (field != obj) contents.set(obj, name);
    }

    /**
	 * Replaces all Data fields in the given contents with DataID.toReference() and DataSet fields with null
	 */
    public static final void unswizzleFields(MutableComparableData contents) {
        String fieldNames[] = contents.getFieldNames();
        for (int i = 0; i < fieldNames.length; i++) unswizzleField(contents, fieldNames[i]);
    }

    /**
	 * 
	 */
    public static final Criteria unswizzle(Criteria wheres) {
        if (wheres == null) return null;
        int n = wheres.getCritCount();
        for (int i = 0; i < n; i++) {
            unswizzle(wheres.getCrit(i));
        }
        return wheres;
    }

    /**
	 * 
	 */
    public static final Criterion unswizzle(Criterion where) {
        if (where == null) return null;
        where.setFieldValue(unswizzleObject(where.getFieldValue()));
        return where;
    }

    public void unswizzleFields() {
        unswizzleFields(getContents());
    }

    public void unswizzleField(String name) {
        unswizzleField(getContents(), name);
    }
}
