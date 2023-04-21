package mipt.data.impl;

import java.util.ArrayList;
import mipt.data.Data;
import mipt.data.MutableComparableData;

/**
 * Data with contents and parent, uses Decorator pattern and Chain of Responsibility pattern.
 * Field structure is not decorated with respect to contents; but Decorator is used regarding parent:
 *  if the parent is not null and this.contents returns no value in getter, the getter is delegated to parent;
 *  otherwise the value from contents is returned. Note: to differ real null value from absent field in contents,
 *  you can add fields with special "absent" values ({@link #setAbsentChecker(AbsentChecker)}).
 * ChainDatas are designed to form "chains" of parents-children where root parent contain "general" data
 *  and children of deeper level contain more "specific" data. Any child can "override" ("hide") parent's
 *  field values or add own fields.
 * Note: setters are not changed in comparison with DataDecorator (they work with this.contents).
 * Note: the set of field names/values are the union of contents' fields and parent's fields
 *  (fields with special "absent" values are excluded; the parent's fields are in the end of the array).
 * @author Evdokimov
 */
public class ChainData extends DataDecorator {

    private Data parent;

    /**
	 * If absentChecker is not null, null values returned by contents is not ordinarily treated as absent!
	 * (Either you should provide special absent values for all fields or you Data contents should generate
	 *  some exception when field is absent - e.g. ArrayData does that).
	 */
    private AbsentChecker absentChecker = null;

    /**
	 * Intended for flexible checks of absent values: can solve problem of null treated as an absent value,
	 *  can implement different behavior for different fields or for different getters (field types).
	 * This interface can also be used for processing getType() method.
	 */
    public static interface AbsentChecker {

        /**
		 * Returns true if value is absent. 
		 * @param field - can be null only for virtual fields - e.g. "type".
		 * @param getterName - the getter name's part after "get" prefix (determines "access field type").
		 *  Is null for untyped getter (get()). Is "Type" for check in Data.getType().
		 */
        boolean isAbsent(Object value, String field, String getterName);
    }

    /**
	 * Treats nulls as non-absent values, the only absent value : value.toString().equals("ABSENT").
	 * Uses null (instead of "ABSENT") for getType(), but you should know that getType() is usually "".
	 * Otherwise does not use field or field type name.
	 * @author Evdokimov
	 */
    public static class DefaultAbsentChecker implements AbsentChecker {

        protected Object ABSENT = "ABSENT";

        public DefaultAbsentChecker() {
        }

        public DefaultAbsentChecker(Object absent) {
            this.ABSENT = absent;
        }

        public final Object getABSENT() {
            return ABSENT;
        }

        /**
		 * @see mipt.data.impl.ChainData.AbsentChecker#isAbsent(java.lang.Object, java.lang.String, java.lang.String)
		 */
        public boolean isAbsent(Object value, String field, String getterName) {
            if (value == null) return "Type".equals(getterName);
            return value.toString().equals(ABSENT);
        }
    }

    /**
	 * 
	 */
    public ChainData() {
    }

    /**
	 * @param contents
	 * @param parent
	 */
    public ChainData(MutableComparableData contents, Data parent) {
        super(contents);
        setParent(parent);
    }

    /**
	 * @return parent
	 */
    public final Data getParent() {
        if (parent == null) parent = initParent();
        return parent;
    }

    /**
	 * Override if parent is loaded rather than set externally
	 */
    protected Data initParent() {
        return null;
    }

    /**
	 * @param parent
	 */
    public void setParent(Data parent) {
        this.parent = parent;
    }

    /**
	 * @return absentChecker
	 */
    public final AbsentChecker getAbsentChecker() {
        return absentChecker;
    }

    /**
	 * The default is null (so null fields are treated as absent => can't "hide" parent's field).
	 * @see DefaultAbsentChecker
	 * @param absentChecker
	 */
    public void setAbsentChecker(AbsentChecker absentChecker) {
        this.absentChecker = absentChecker;
    }

    @Override
    public final Object get(String field) {
        return getImpl(field, null);
    }

    protected Object getImpl(String field, String getterName) {
        Object value = getContents().get(field);
        if (getParent() != null && (getAbsentChecker() == null ? value == null : getAbsentChecker().isAbsent(value, field, getterName))) return parent.get(field);
        return value;
    }

    @Override
    public String[] getFieldNames() {
        String[] contentsNames = super.getFieldNames();
        if (getParent() == null) return contentsNames;
        String parentNames[] = parent.getFieldNames();
        ArrayList<String> names = new ArrayList<String>(contentsNames.length + parentNames.length);
        for (String name : contentsNames) if (getAbsentChecker() == null || !getAbsentChecker().isAbsent(get(name), name, null)) names.add(name);
        for (String name : parentNames) if (!names.contains(name)) names.add(name);
        contentsNames = new String[names.size()];
        return names.toArray(contentsNames);
    }

    @Override
    public String getType() {
        String type = getContents().getType();
        if (getParent() != null && (getAbsentChecker() == null ? type == null : getAbsentChecker().isAbsent(type, null, "Type"))) return parent.getType();
        return type;
    }

    /**
	 * Unlike {@link DataDecorator#getFinalContents(mipt.data.Data), this method scans the given data
	 *  to ChainData, gets final parent from it and gets final contents of this parent. 
	 * Final parent is the parent which do not have its parent (ChainData with no parent or not ChainData).
	 * Like {@link DataDecorator#getFinalContents(mipt.data.Data) unwraps the data from any DataWrappers at the first stage.
	 * @param data Any data e.g. FieldCacheData or DefaultDataWrapper
	 */
    public static MutableComparableData getFinalParentContents(Data data) {
        data = DefaultDataWrapper.unwrap(data);
        boolean checkChain = true;
        while (data instanceof DataDecorator) {
            if (checkChain && data instanceof ChainData) {
                Data d = ((ChainData) data).getParent();
                if (d == null) checkChain = false; else data = d;
            } else {
                data = ((DataDecorator) data).getContents();
            }
        }
        return (MutableComparableData) data;
    }

    /**
	 * Returns not contents but the parent itself (immediate parent). Can return null.
	 * Like {@link DataDecorator#getFinalContents(mipt.data.Data) unwraps the data from any DataWrappers at the first stage.
	 * @param data Any data e.g. FieldCacheData or DefaultDataWrapper
	 */
    public static Data getParent(Data data) {
        data = DefaultDataWrapper.unwrap(data);
        while (data instanceof DataDecorator) {
            if (data instanceof ChainData) {
                return ((ChainData) data).getParent();
            } else {
                data = ((DataDecorator) data).getContents();
            }
        }
        return null;
    }
}
