package org.compiere.model;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;
import org.compiere.util.KeyNamePair;

/** Generated Model for S_ResourceUnAvailable
 *  @author Adempiere (generated) 
 *  @version Release 3.3.1t - $Id$ */
public class X_S_ResourceUnAvailable extends PO implements I_S_ResourceUnAvailable, I_Persistent {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    /** Standard Constructor */
    public X_S_ResourceUnAvailable(Properties ctx, int S_ResourceUnAvailable_ID, String trxName) {
        super(ctx, S_ResourceUnAvailable_ID, trxName);
    }

    /** Load Constructor */
    public X_S_ResourceUnAvailable(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel() {
        return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO(Properties ctx) {
        POInfo poi = POInfo.getPOInfo(ctx, Table_ID, get_TrxName());
        return poi;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("X_S_ResourceUnAvailable[").append(get_ID()).append("]");
        return sb.toString();
    }

    /** Set Date From.
		@param DateFrom 
		Starting date for a range
	  */
    public void setDateFrom(Timestamp DateFrom) {
        if (DateFrom == null) throw new IllegalArgumentException("DateFrom is mandatory.");
        set_Value(COLUMNNAME_DateFrom, DateFrom);
    }

    /** Get Date From.
		@return Starting date for a range
	  */
    public Timestamp getDateFrom() {
        return (Timestamp) get_Value(COLUMNNAME_DateFrom);
    }

    /** Set Date To.
		@param DateTo 
		End date of a date range
	  */
    public void setDateTo(Timestamp DateTo) {
        set_Value(COLUMNNAME_DateTo, DateTo);
    }

    /** Get Date To.
		@return End date of a date range
	  */
    public Timestamp getDateTo() {
        return (Timestamp) get_Value(COLUMNNAME_DateTo);
    }

    /** Set Description.
		@param Description 
		Optional short description of the record
	  */
    public void setDescription(String Description) {
        if (Description != null && Description.length() > 255) {
            log.warning("Length > 255 - truncated");
            Description = Description.substring(0, 255);
        }
        set_Value(COLUMNNAME_Description, Description);
    }

    /** Get Description.
		@return Optional short description of the record
	  */
    public String getDescription() {
        return (String) get_Value(COLUMNNAME_Description);
    }

    /** Set Resource Unavailability.
		@param S_ResourceUnAvailable_ID Resource Unavailability	  */
    public void setS_ResourceUnAvailable_ID(int S_ResourceUnAvailable_ID) {
        if (S_ResourceUnAvailable_ID < 1) throw new IllegalArgumentException("S_ResourceUnAvailable_ID is mandatory.");
        set_ValueNoCheck(COLUMNNAME_S_ResourceUnAvailable_ID, Integer.valueOf(S_ResourceUnAvailable_ID));
    }

    /** Get Resource Unavailability.
		@return Resource Unavailability	  */
    public int getS_ResourceUnAvailable_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_S_ResourceUnAvailable_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    public I_S_Resource getS_Resource() throws Exception {
        Class<?> clazz = MTable.getClass(I_S_Resource.Table_Name);
        I_S_Resource result = null;
        try {
            Constructor<?> constructor = null;
            constructor = clazz.getDeclaredConstructor(new Class[] { Properties.class, int.class, String.class });
            result = (I_S_Resource) constructor.newInstance(new Object[] { getCtx(), new Integer(getS_Resource_ID()), get_TrxName() });
        } catch (Exception e) {
            log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
            log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
            throw e;
        }
        return result;
    }

    /** Set Resource.
		@param S_Resource_ID 
		Resource
	  */
    public void setS_Resource_ID(int S_Resource_ID) {
        if (S_Resource_ID < 1) throw new IllegalArgumentException("S_Resource_ID is mandatory.");
        set_ValueNoCheck(COLUMNNAME_S_Resource_ID, Integer.valueOf(S_Resource_ID));
    }

    /** Get Resource.
		@return Resource
	  */
    public int getS_Resource_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_S_Resource_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() {
        return new KeyNamePair(get_ID(), String.valueOf(getS_Resource_ID()));
    }
}
