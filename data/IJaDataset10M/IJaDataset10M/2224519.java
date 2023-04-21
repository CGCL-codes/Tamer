package wsl.mdn.dataview;

import wsl.fw.datasource.*;
import wsl.mdn.dataview.DataSourceDobj;

/**
 * Represents a field in a DataView
 */
public class DataViewField extends DataObject implements Field {

    public static final String ENT_DVFIELD = "TBL_DVFIELD";

    public static final String FLD_ID = "FLD_ID";

    public static final String FLD_NAME = "FLD_NAME";

    public static final String FLD_DATAVIEWID = "FLD_DATAVIEWID";

    public static final String FLD_DESCRIPTION = "FLD_DESCRIPTION";

    public static final String FLD_SOURCE_ENTITY = "FLD_SOURCE_ENTITY";

    public static final String FLD_SOURCE_FIELD = "FLD_SOURCE_FIELD";

    public static final String FLD_DISPLAY_NAME = "FLD_DISPLAY_NAME";

    public static final String FLD_FLAGS = "FLD_FLAGS";

    public static final String FLD_OPTION_LIST = "FLD_OPTION_LIST";

    public static final String FLD_TYPE = "FLD_TYPE";

    private transient DataSourceDobj _sourceDs = null;

    /**
     * Blank ctor
     */
    public DataViewField() {
    }

    /**
     * Static factory method to create the entity to be used by this dataobject
     * and any subclasses. This is called by the DataManager's factory when
     * creating a FIELD entity.
     * @return the created entity.
     */
    public static Entity createEntity() {
        Entity ent = new EntityImpl(ENT_DVFIELD, DataViewField.class);
        ent.addKeyGeneratorData(new DefaultKeyGeneratorData(ENT_DVFIELD, FLD_ID));
        ent.addField(new FieldImpl(FLD_ID, Field.FT_INTEGER, Field.FF_SYSTEM_KEY | Field.FF_UNIQUE_KEY));
        ent.addField(new FieldImpl(FLD_NAME, Field.FT_STRING, Field.FF_NAMING));
        ent.addField(new FieldImpl(FLD_DESCRIPTION, Field.FT_STRING));
        ent.addField(new FieldImpl(FLD_DATAVIEWID, Field.FT_INTEGER));
        ent.addField(new FieldImpl(FLD_SOURCE_ENTITY, Field.FT_STRING));
        ent.addField(new FieldImpl(FLD_SOURCE_FIELD, Field.FT_STRING));
        ent.addField(new FieldImpl(FLD_DISPLAY_NAME, Field.FT_STRING, Field.FF_NONE, 50));
        ent.addField(new FieldImpl(FLD_FLAGS, Field.FT_INTEGER));
        ent.addField(new FieldImpl(FLD_OPTION_LIST, Field.FT_STRING));
        ent.addField(new FieldImpl(FLD_TYPE, Field.FT_INTEGER));
        ent.addJoin(new JoinImpl(ENT_DVFIELD, FLD_DATAVIEWID, DataView.ENT_DATAVIEW, DataView.FLD_ID));
        ent.addJoin(new JoinImpl(ENT_DVFIELD, FLD_SOURCE_ENTITY, EntityDobj.ENT_ENTITY, EntityDobj.FLD_NAME));
        ent.addJoin(new JoinImpl(ENT_DVFIELD, FLD_SOURCE_FIELD, FieldDobj.ENT_FIELD, FieldDobj.FLD_NAME));
        return ent;
    }

    /**
     * @return String the name of the entity that defines this DataObject
     */
    public String getEntityName() {
        return ENT_DVFIELD;
    }

    /**
     * @return int the id
     */
    public int getId() {
        return getIntValue(FLD_ID);
    }

    /**
     * Set the id for this dataviewField.
     * @param id, the id to set
     */
    public void setId(int id) {
        setValue(FLD_ID, id);
    }

    /**
     * @return int the dataview id
     */
    public int getDataViewId() {
        return getIntValue(FLD_DATAVIEWID);
    }

    /**
     * Set the dataview id
     * @param id
     */
    public void setDataViewId(int id) {
        setValue(FLD_DATAVIEWID, id);
    }

    /**
     * Returns the description of the dataview
     * @return String
     */
    public String getDescription() {
        return getStringValue(FLD_DESCRIPTION);
    }

    /**
     * Sets the entity description into the dataview
     * @param name
     * @return void
     */
    public void setDescription(String name) {
        setValue(FLD_DESCRIPTION, name);
    }

    /**
     * Returns the display name of the dataview field
     * @return String
     */
    public String getDisplayName() {
        return getStringValue(FLD_DISPLAY_NAME);
    }

    /**
     * Sets the display name into the dataview field
     * @param name
     * @return void
     */
    public void setDisplayName(String displayName) {
        setValue(FLD_DISPLAY_NAME, displayName);
    }

    /**
     * Returns the source entity name
     * @return String the source entity name
     */
    public String getSourceEntity() {
        return getStringValue(FLD_SOURCE_ENTITY);
    }

    /**
     * Sets the source entity name
     * @param val the value to set
     */
    public void setSourceEntity(String val) {
        setValue(FLD_SOURCE_ENTITY, val);
    }

    /**
     * Returns the source field name
     * @return String the source field name
     */
    public String getSourceField() {
        return getStringValue(FLD_SOURCE_FIELD);
    }

    /**
     * Sets the source field name
     * @param val the value to set
     */
    public void setSourceField(String val) {
        setValue(FLD_SOURCE_FIELD, val);
    }

    /**
     * Returns the option list of the dataview field
     * @return String
     */
    public String getOptionList() {
        return getStringValue(FLD_OPTION_LIST);
    }

    /**
     * Sets the option list into the dataview field
     * @param list
     * @return void
     */
    public void setOptionList(String list) {
        setValue(FLD_OPTION_LIST, list);
    }

    /**
     * Set the source datasource
     * @param sourceDs the Fields DataSourceDobj
     */
    public void setSourceDataSource(DataSourceDobj sourceDs) {
        _sourceDs = sourceDs;
    }

    /**
     * @return DataSourceDobj the source datasource
     */
    public DataSourceDobj getSourceDataSource() {
        return _sourceDs;
    }

    /**
     * Returns the field name
     * @return String the field name
     */
    public String getName() {
        return getStringValue(FLD_NAME);
    }

    /**
     * Sets the field name
     * @param val the value to set
     */
    public void setName(String val) {
        setValue(FLD_NAME, val);
    }

    /**
     * Returns the field type
     * @return int the field type
     */
    public int getType() {
        return getIntValue(FLD_TYPE);
    }

    /**
     * Sets the field type
     * @param val the value to set
     */
    public void setType(int val) {
        setValue(FLD_TYPE, val);
    }

    /**
     * Returns the field flags
     * @return int the field flags
     */
    public int getFlags() {
        if (getObjectValue(FLD_FLAGS) == null) return 0; else return getIntValue(FLD_FLAGS);
    }

    /**
     * Sets the field flags
     * @param val the value to set
     */
    public void setFlags(int val) {
        setValue(FLD_FLAGS, val);
    }

    /**
     * returns true if the param flags are set
     * @param flag the flag to check
     * @return true if the param flags are set
     */
    public boolean hasFlag(int flag) {
        return ((getFlags() & flag) > 0);
    }

    /**
     * @return the column size, the number of characters for string fields.
     */
    public int getColumnSize() {
        return -1;
    }

    /**
     * @param columnSize, the number of characters for string fields.
     */
    public void setColumnSize(int columnSize) {
    }

    /**
     * @return the number of decimal digits
     */
    public int getDecimalDigits() {
        return 0;
    }

    /**
     * @param n, the number of decimal digits in a number
     */
    public void setDecimalDigits(int n) {
    }

    /**
     * Implementation of Comparable interface to allow fields to be sorted.
     */
    public int compareTo(Object o) {
        return -1;
    }

    /**
     * @return the native type of the field
     */
    public int getNativeType() {
        return 0;
    }

    /**
     * @param n, the native type of the field
     */
    public void setNativeType(int n) {
    }
}
