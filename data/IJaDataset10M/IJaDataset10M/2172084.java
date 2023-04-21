package gov.sns.apps.jeri.data;

import gov.sns.apps.jeri.Main;
import java.sql.ResultSet;
import java.util.Arrays;

/**
 * Provides a class for holding the values in the SgnlFld table.
 * 
 * @author Chris Fowlkes
 */
public class SignalField extends RDBData implements Cloneable {

    /**
   * Holds the value of the fld_val field in the table.
   */
    private String value;

    /**
   * Holds the values in the sgnl_fld_def table for the <CODE>Signal</CODE>.
   * @attribute 
   */
    private SignalFieldType type;

    /**
   * Provides a flag to easily determine if the field is already in the 
   * database. <CODE>false</CODE> by default.
   */
    private boolean inDatabase = false;

    /**
   * Holds the <CODE>Signal</CODE> to which this <CODE>SignalField</CODE> 
   * belongs.
   * @attribute 
   */
    private Signal signal;

    /**
   * Holds the name of the RDB column for the internal value property.
   */
    public static final String VALUE_COLUMN_NAME = "FLD_VAL";

    /**
   * Holds the name of the RDB column for the type property.
   */
    public static final String TYPE_COLUMN_NAME = "FLD_ID";

    /**
   * Holds the name of the RDB column for the class.
   */
    public static final String SIGNAL_FIELD_TABLE_NAME = "SGNL_FLD";

    /**
   * Creates a new <CODE>SignalField</CODE>.
   */
    public SignalField() {
    }

    /**
   * Creates and initializes a new <CODE>SignalField</CODE>.
   * 
   * @param internalValue The value for the field.
   */
    public SignalField(String value) {
        this();
        setValue(value);
    }

    /**
   * Creates a <CODE>SignalField</CODE> from the given RDB data.
   * 
   * @param data The data from which to create the <CODE>SignalField</CODE>.
   * @throws java.sql.SQLException Thrown on SQL error.
   */
    public SignalField(ResultSet data) throws java.sql.SQLException {
        super(data);
        String[] columnNames = RDBData.findColumnNames(data);
        if (Arrays.binarySearch(columnNames, TYPE_COLUMN_NAME) >= 0) setType(new SignalFieldType(data));
        resetChangedFlag();
    }

    /**
   * Sets the value of the field.
   * 
   * @param internalValue The new value for the internal value property.
   */
    public void setValue(String value) {
        String oldValue = this.value;
        this.value = value;
        firePropertyChange("value", oldValue, value);
        markFieldChanged(VALUE_COLUMN_NAME);
    }

    /**
   * Gets the value of the field.
   * 
   * @return The value for the field.
   */
    public String getValue() {
        return value;
    }

    /**
   * Returns the signal field type. This value represents the data in the 
   * sgnl_fld_def table.
   * 
   * @return The <CODE>SignalFieldType</CODE> for the field, or null if the field has no corresponding entry in the sgnl_fld_def table.
   */
    public SignalFieldType getType() {
        return type;
    }

    /**
   * Sets the <CODE>SignalFieldType</CODE> for the <CODE>SignalField</CODE>.
   * 
   * @param type The <CODE>SignalFieldType</CODE> that holds the data from the sgnl_fld_type table for the <CODE>SignalField</CODE>.
   */
    public void setType(SignalFieldType type) {
        SignalFieldType oldValue = this.type;
        this.type = type;
        firePropertyChange("type", oldValue, type);
        markFieldChanged(TYPE_COLUMN_NAME);
    }

    /**
   * Returns the <CODE>String</CODE> value of the <CODE>SignalField</CODE>. This
   * method calls the <CODE>getValue()</CODE> method and returns the value or an
   * empty <CODE>String</CODE> if <CODE>null</CODE> is returned by 
   * <CODE>getValue()</CODE>.
   * 
   * @return The value of the field.
   */
    @Override
    public String toString() {
        String stringValue = getValue();
        if (stringValue == null) stringValue = "";
        return stringValue;
    }

    /**
   * Allows the in database flag to be set for the field. This is needed to 
   * determine if the query to save the data should be an insert or update 
   * statement. The default value is <CODE>false</CODE>.
   * 
   * @param inDatabase Pass as <CODE>true</CODE> if the field is already in the database, <CODE>false</CODE> if not.
   */
    @Override
    public void setInDatabase(boolean inDatabase) {
        this.inDatabase = inDatabase;
    }

    /**
   * Gets the in database flag for the field. This is needed to determine if the 
   * query to save the data should be an insert or update statement. The default 
   * value is <CODE>false</CODE>.
   * 
   * @return <CODE>true</CODE> if the field is already in the database, <CODE>false</CODE> if not.
   */
    @Override
    public boolean isInDatabase() {
        return inDatabase;
    }

    /**
   * Compare this instance of <CODE>SignalField</CODE> to the one passed in.
   * 
   * @param object The <CODE>SignalField</CODE> to which to compare.
   * @return <CODE>true</CODE> if the fields are equivalent in type and value, <CODE>false</CODE> otherwise.
   */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof SignalField)) return false;
        SignalField compareTo = (SignalField) obj;
        if (!Main.compare(getType(), compareTo.getType())) return false;
        if (!Main.compare(getValue(), compareTo.getValue())) return false;
        if (isInDatabase() ^ compareTo.isInDatabase()) return false;
        Signal signal = getSignal();
        Signal compareSignal = compareTo.getSignal();
        if (signal == null) {
            if (compareSignal != null) return false;
        } else {
            if (compareSignal == null) return false;
            if (!signal.getID().equals(compareSignal.getID())) return false;
        }
        return true;
    }

    /**
   * Returns a hash code for the <CODE>SignalField</CODE>. If the 
   * <CODE>equals</CODE> method for a class returns <CODE>true</CODE>, the 
   * <CODE>hashCode</CODE> methods for those instances of <CODE>Object</CODE> 
   * must also return the same value. The reverse is not the case, meaning that
   * just because the value returned by the <CODE>hashCode()</CODE> methods of 
   * two instances of a class is the same equality can not be assumed.
   * 
   * @return A hash code for the <CODE>SignalField</CODE>.
   */
    @Override
    public int hashCode() {
        int hashCode = 1;
        hashCode = hashCode * 37 + findPropertyHashCode(getType());
        hashCode = hashCode * 37 + findPropertyHashCode(getValue());
        Signal signal = getSignal();
        if (signal == null) hashCode = hashCode * 37 + 0; else hashCode = hashCode * 37 + findPropertyHashCode(signal.getID());
        return hashCode;
    }

    /**
   * Checks the given <CODE>Object</CODE> for <CODE>null</CODE> before invoking 
   * <CODE>hashCode()</CODE> on it. If <CODE>null</CODE> is passed in, 
   * <CODE>0</CODE> is returned, otherwise the value returned by the 
   * <CODE>hashCode</CODE> method is returned.
   * 
   * @param propertyValue The <CODE>Object</CODE> of which to return the hash code.
   * @return The hash code for the given <CODE>Object</CODE>.
   */
    private int findPropertyHashCode(Object propertyValue) {
        if (propertyValue == null) return 0; else return propertyValue.hashCode();
    }

    /**
   * Creates a new instance of <CODE>SignalField</CODE> that is equal to the 
   * existing one.
   * 
   * @return A copy of the <CODE>SignalField</CODE>.
   */
    @Override
    protected Object clone() {
        SignalField clone = new SignalField(getValue());
        SignalFieldType type = getType();
        if (type != null) clone.setType((SignalFieldType) type.clone());
        clone.setInDatabase(isInDatabase());
        return clone;
    }

    /**
   * Sets the <CODE>Signal</CODE> that this <CODE>SignalField</CODE> belongs to.
   * This method is called by the <CODE>Signal</CODE> when the field is added to 
   * it.
   * 
   * @param signal The <CODE>Signal</CODE> that contains the <CODE>SignalField</CODE>
   */
    protected void setSignal(Signal signal) {
        this.signal = signal;
    }

    /**
   * Gets the <CODE>Signal</CODE> that the <CODE>SignalField</CODE> wass added 
   * to.
   * 
   * @return The <CODE>Signal</CODE> that the <CODE>SignalField</CODE> was added to.
   */
    public Signal getSignal() {
        return signal;
    }

    /**
   * Returns the name of the schema to which the SGNL_FLD RDB table belongs.
   * 
   * @return The name of the RDB schema that corresponds to the class.
   */
    @Override
    protected String getSchemaName() {
        return "EPICS";
    }

    /**
   * Returns the name of the RDB table for the class.
   * 
   * @return The name of the RDB table that corresponds to the class.
   */
    @Override
    protected String getTableName() {
        return SIGNAL_FIELD_TABLE_NAME;
    }

    /**
   * Should return the value of the RDB field as stored in the 
   * <CODE>RDBData</CODE> instance.
   * 
   * @param rdbFieldName The name of the field of which to return the value.
   * @return The value of the property that corresponds to the given RDB field.
   */
    @Override
    protected Object getValue(String rdbFieldName) {
        if (rdbFieldName.equals(VALUE_COLUMN_NAME)) return getValue();
        if (rdbFieldName.equals(TYPE_COLUMN_NAME)) return getType().getID();
        return null;
    }

    /**
   * Should set the value of the property to the value of the RDB field. If the 
   * field name passed in does not correspond to a property in the class, it 
   * should just be ignored.
   * 
   * @param rdbFieldName The name of the field for the data.
   * @param value The value of the RDB field.
   * @return The value of the property that corresponds to the given RDB field.
   */
    @Override
    protected void setValue(String rdbFieldName, Object value) {
        if (rdbFieldName.equalsIgnoreCase(VALUE_COLUMN_NAME)) setValue((String) value);
    }
}
