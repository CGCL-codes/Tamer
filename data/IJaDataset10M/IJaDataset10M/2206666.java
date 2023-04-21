package gov.sns.apps.jeri.data;

import java.sql.ResultSet;
import java.util.*;
import java.math.*;
import gov.sns.apps.jeri.data.Device;

/**
 * Provides a class to encompass a MPS channel. This class holds the data from
 * the MACHINE_MODE table.
 * 
 * @author Chris Fowlkes
 */
public class MPSChannel extends RDBData implements Cloneable {

    /**
   * Holds the channel number.
   */
    private int number;

    /**
   * Holds the <CODE>Device</CODE> associated with the <CODE>MPSChannel</CODE>, 
   * if any.
   * @attribute 
   */
    private Device device;

    /**
   * Holds the locked indicator. Default is "N".
   */
    private String lockedIndicator;

    /**
   * Holds the in use indicator. Default is "N".
   */
    private String inUseIndicator;

    /**
   * Holds the SW jumper value. Default is "N".
   */
    private String swJumper;

    /**
   * Holds the values for the binary fields in the channel.
   */
    private HashMap binaryValues;

    /**
   * Holds the limit for the channel. Defaults to 4.
   */
    private Integer limit;

    /**
   * Holds the rwt for the channel. Defaults to 300.
   */
    private Integer rate;

    /**
   * Holds the <CODE>MPSBoard</CODE> to which the channel belongs.
   */
    private MPSBoard board;

    /**
   * Holds the name of the RDB column for the number property.
   */
    public static final String NUMBER_COLUMN_NAME = "CHANNEL_NBR";

    /**
   * Holds the name of the RDB column for the locked indicator property.
   */
    public static final String LOCKED_INDICATOR_COLUMN_NAME = "LOCKED_IND";

    /**
   * Holds the name of the RDB column for the SWJumper property.
   */
    public static final String SW_JUMPER_COLUMN_NAME = "SW_JUMP";

    /**
   * Holds the name of the RDB column for the limit property.
   */
    public static final String LIMIT_COLUMN_NAME = "LIMIT";

    /**
   * Holds the name of the RDB column for the rate property.
   */
    public static final String RATE_COLUMN_NAME = "RATE";

    /**
   * Holds the name of the RDB column for the device property.
   */
    public static final String DEVICE_COLUMN_NAME = "MPS_DVC_ID";

    /**
   * Holds the name of the RDB column for the channel in use property.
   */
    public static final String IN_USE_INDICATOR_COLUMN_NAME = "CHAN_IN_USE_IND";

    /**
   * Gets the instances of <CODE>MPSChannelAudit</CODE> that have been added to 
   * the channel.
   */
    private ArrayList audits = new ArrayList();

    /**
   * Creates a new <CODE>MPSChannel</CODE>.
   */
    public MPSChannel() {
    }

    /**
   * Creates a new <CODE>MPSChannel</CODE> with the given channel number.
   * 
   * @param number The number of the new channel.
   * @throws java.sql.SQLException Thrown on SQL error.
   */
    public MPSChannel(int number) {
        this();
        setNumber(number);
    }

    /**
   * Creates a new <CODE>MPSChannel</CODE> from the given data.
   * 
   * @param data The RDB data with which to create the <CODE>MPSChannel</CODE>.
   */
    public MPSChannel(ResultSet data) throws java.sql.SQLException {
        super(data);
    }

    /**
   * Sets the number of the channel.
   * 
   * @param number The number of the channel.
   */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
   * Gets the number of the channel.
   * 
   * @return The number of the channel.
   */
    public int getNumber() {
        return number;
    }

    /**
   * Sets the <CODE>Device</CODE> asociated with the channel.
   * 
   * @param signal The <CODE>Device</CODE> associated with the channel.
   */
    public void setDevice(Device device) {
        this.device = device;
    }

    /**
   * Gets the <CODE>Device</CODE> associated with the channel.
   * 
   * @return The <CODE>Device</CODE> associated with the channel.
   */
    public Device getDevice() {
        return device;
    }

    /**
   * Sets the locked indicator for the channel. This property reflects the value 
   * of the LOCKED_IND database field.
   * 
   * @param lockedIndicator The new value for the LOCKED_IND field, "Y" or "N".
   */
    public void setLockedIndicator(String lockedIndicator) {
        this.lockedIndicator = lockedIndicator;
    }

    /**
   * Gets the locked indicator for the channel. This property reflects the value 
   * of the LOCKED_IND database field.
   * 
   * @return The current value for the LOCKED_IND field, "Y" or "N".
   */
    public String getLockedIndicator() {
        if (lockedIndicator == null) return "N";
        return lockedIndicator;
    }

    /**
   * Sets the in use indicator for the channel. This property reflects the value 
   * of the CHAN_IN_USE_IND database field.
   * 
   * @param newInUseIndicator The new value for the IN_USE_IND field, "Y" or "N".
   */
    public void setInUseIndicator(String newInUseIndicator) {
        inUseIndicator = newInUseIndicator;
    }

    /**
   * Gets the in use indicator for the channel. This property reflects the value 
   * of the CHAN_IN_USE_IND database field.
   * 
   * @return The current value for the IN_USE_IND field, "Y" or "N".
   */
    public String getInUseIndicator() {
        if (inUseIndicator == null) return "N";
        return inUseIndicator;
    }

    /**
   * Sets the value for the SW jumper property. This property  reflects the 
   * value of the SW_JUMP database field.
   * 
   * @param swJumper The new value for the SW jumper property.
   */
    public void setSWJumper(String swJumper) {
        this.swJumper = swJumper;
    }

    /**
   * Gets the value for the SW jumper property. This property  reflects the 
   * value of the SW_JUMP database field.
   * 
   * @return The value for the SW jumper property.
   */
    public String getSWJumper() {
        if (swJumper == null) return "N";
        return swJumper;
    }

    /**
   * Sets the value for the given binary field. This method is used to store the 
   * value of the binary data stored in the table that does not have a property
   * of it's own.
   * 
   * @param fieldName The name of the binary field.
   * @param value The value of the binary field.
   */
    public void setBinaryValue(String fieldName, BigInteger value) {
        if (binaryValues == null) createBinaryColumnMap();
        binaryValues.put(fieldName, value);
    }

    /**
   * Gets the value for the given binary field. This method is used to retrieve 
   * the value of the binary data stored in the table that does not have a 
   * property of it's own.
   * 
   * @param fieldName The name of the binary field to return the value of.
   * @return The value of the given binary field.
   */
    public BigInteger getBinaryValue(String fieldName) {
        if (binaryValues == null) createBinaryColumnMap();
        return (BigInteger) binaryValues.get(fieldName);
    }

    /**
   * Returns the <CODE>String</CODE> value of the channel. This method returns
   * an empty string.
   * 
   * @return The <CODE>String</CODE> value for the channel (always an empty string).
   */
    @Override
    public String toString() {
        return "";
    }

    /**
   * Gets the value for the limit property. This property reflects the value 
   * of the LIMIT field in the database.
   * 
   * @return The limit for the channel.
   */
    public int getLimit() {
        if (limit == null) return 4; else return limit.intValue();
    }

    /**
   * Sets the value for the limit property. This property reflects the value 
   * of the LIMIT field in the database.
   * 
   * @param limit The limit for the channel.
   */
    public void setLimit(int limit) {
        this.limit = new Integer(limit);
    }

    /**
   * Gets the rate for the channel. This property reflects the value of the RATE
   * database field.
   * 
   * @return The rate for the channel.
   */
    public int getRate() {
        if (rate == null) return 300;
        return rate.intValue();
    }

    /**
   * Sets the rate for the channel. This property reflects the value of the RATE
   * database field.
   * 
   * @param rate The rate for the channel.
   */
    public void setRate(int rate) {
        this.rate = new Integer(rate);
    }

    /**
   * Sets the <CODE>MPSBoard</CODE> this channel belongs to. This method is
   * called by the <CODE>addChannel</CODE> method in the <CODE>MPSBoard</CODE>
   * class and shouild not be called directly. To associate a channel with a 
   * board, just add the channel to the board.
   * 
   * @param board The <CODE>MPSBoard</CODE> the channel belongs to.
   */
    void setBoard(MPSBoard board) {
        this.board = board;
    }

    /**
   * Gets the <CODE>MPSBoard</CODE> to which this channel has been added.
   * 
   * @return The <CODE>MPSBoard</CODE> the channel belongs to.
   */
    public MPSBoard getBoard() {
        return board;
    }

    /**
   * Provides a clone of the <CODE>MPSChannel</CODE>. This method does not clone 
   * the value of the board property.
   * 
   * @return A clone of the <CODE>MPSChannel</CODE>.
   */
    @Override
    public Object clone() {
        MPSChannel clone = new MPSChannel(getNumber());
        clone.setLimit(getLimit());
        clone.setLockedIndicator(getLockedIndicator());
        clone.setRate(getRate());
        Device cloneDevice = getDevice();
        if (cloneDevice != null) cloneDevice = (Device) cloneDevice.clone();
        clone.setDevice(cloneDevice);
        clone.setSWJumper(getSWJumper());
        clone.setInDatabase(isInDatabase());
        String[] fieldNames = getBinaryFieldNames();
        for (int i = 0; i < fieldNames.length; i++) clone.setBinaryValue(fieldNames[i], getBinaryValue(fieldNames[i]));
        return clone;
    }

    /**
   * Gets the names of all of the binary fields that have been set for this
   * <CODE>MPSChannel</CODE>.
   * 
   * @return The names of the binary fields that this <CODE>MPSChannel</CODE> contains.
   */
    public String[] getBinaryFieldNames() {
        if (binaryValues == null) createBinaryColumnMap();
        Set keys = binaryValues.keySet();
        return (String[]) keys.toArray(new String[keys.size()]);
    }

    /**
   * Returns the schema name.
   * 
   * @return The name of the RDB schema that corresponds to the class, or <CODE>null</CODE> if none applies.
   */
    @Override
    protected String getSchemaName() {
        return "EPICS";
    }

    /**
   * Returns the name if the table.
   * 
   * @return The name of the RDB table that corresponds to the class.
   */
    @Override
    protected String getTableName() {
        return "MACHINE_MODE";
    }

    /**
   * Should return the value of the RDB field as stored in the 
   * <CODE>RDBData</CODE> instance.
   * 
   * @return The value of the property that corresponds to the given RDB field.
   */
    @Override
    protected Object getValue(String rdbFieldName) {
        if (rdbFieldName.equals(NUMBER_COLUMN_NAME)) return new Integer(getNumber());
        if (rdbFieldName.equals(LOCKED_INDICATOR_COLUMN_NAME)) return getLockedIndicator();
        if (rdbFieldName.equals(SW_JUMPER_COLUMN_NAME)) return getSWJumper();
        if (rdbFieldName.equals(LIMIT_COLUMN_NAME)) return new Integer(getLimit());
        if (rdbFieldName.equals(RATE_COLUMN_NAME)) return new Integer(getRate());
        if (rdbFieldName.equals(DEVICE_COLUMN_NAME)) {
            Device device = getDevice();
            if (device == null) return null;
            return device.getID();
        }
        if (rdbFieldName.equals(IN_USE_INDICATOR_COLUMN_NAME)) return getInUseIndicator();
        return getBinaryValue(rdbFieldName);
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
        if (rdbFieldName.equals(NUMBER_COLUMN_NAME)) setNumber(((Number) value).intValue()); else if (rdbFieldName.equals(LOCKED_INDICATOR_COLUMN_NAME)) setLockedIndicator((String) value); else if (rdbFieldName.equals(SW_JUMPER_COLUMN_NAME)) setSWJumper((String) value); else if (rdbFieldName.equals(LIMIT_COLUMN_NAME)) setLimit(((Number) value).intValue()); else if (rdbFieldName.equals(RATE_COLUMN_NAME)) setRate(((Number) value).intValue()); else if (rdbFieldName.equals(DEVICE_COLUMN_NAME)) {
            Device device = getDevice();
            if (device == null) setDevice(new Device((String) value)); else device.setID((String) value);
        } else if (rdbFieldName.equals(IN_USE_INDICATOR_COLUMN_NAME)) setInUseIndicator((String) value); else if (value instanceof byte[]) setBinaryValue(rdbFieldName, new BigInteger((byte[]) value));
    }

    /**
   * Creates the <CODE>HashMap</CODE> used to hold the binary column values.
   */
    private void createBinaryColumnMap() {
        binaryValues = new HashMap(38);
    }

    /**
   * Gets the number of instances of <CODE>MPSChannelAudit</CODE> that have been 
   * added to the channel.
   * 
   * @return The number of instances of <CODE>MPSChannelAudit</CODE> that have been added to the channel.
   */
    public int getAuditCount() {
        return audits.size();
    }

    /**
   * Gets the <CODE>MPSChannelIndex</CODE> at the given index.
   * 
   * @param index The index of the <CODE>MPSChannelAudit</CODE> to return.
   * @return The <CODE>MPSChannelAudit</CODE> at the given index.
   */
    public MPSChannelAudit getAuditAt(int index) {
        return (MPSChannelAudit) audits.get(index);
    }

    /**
   * Gets the <CODE>MPSChannelAudit</CODE> with the given <CODE>Date</CODE>.
   * 
   * @param auditDate The <CODE>Date</CODE> of the <CODE>MPSChannelAudit</CODE> to return. Returns <CODE>null</CODE> if no matching <CODE>MPSChannelAudit</CODE> is found.
   * @return
   */
    public MPSChannelAudit getAudit(java.sql.Date auditDate) {
        int auditCount = getAuditCount();
        for (int i = 0; i < auditCount; i++) {
            MPSChannelAudit audit = getAuditAt(i);
            if (audit.getDate().equals(auditDate)) return audit;
        }
        return null;
    }

    /**
   * Returns the <CODE>MPSChannelAudit</CODE> with the newest date.
   * 
   * @return The latest <CODE>MPSChannelAudit</CODE>.
   */
    public MPSChannelAudit getLatestAudit() {
        int auditCount = getAuditCount();
        java.sql.Date latestDate = null;
        MPSChannelAudit latestAudit = null;
        for (int i = 0; i < auditCount; i++) {
            MPSChannelAudit audit = getAuditAt(i);
            if (latestDate == null) {
                latestDate = audit.getDate();
                latestAudit = audit;
            } else {
                java.sql.Date newDate = audit.getDate();
                if (latestDate.before(newDate)) {
                    latestDate = newDate;
                    latestAudit = audit;
                }
            }
        }
        return latestAudit;
    }

    /**
   * Adds the given <CODE>MPSChannelAudit</CODE> to the <CODE>MPSChannel</CODE>.
   * 
   * @param audit The <CODE>MPSChannelAudit</CODE> to add to the <CODE>MPSChannel</CODE>.
   */
    public void addAudit(MPSChannelAudit audit) {
        audits.add(audit);
        if (audit.getChannel() != this) audit.setChannel(this);
    }
}
