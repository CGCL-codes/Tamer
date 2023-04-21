package org.compiere.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.compiere.util.*;

/** Generated Model for C_BP_Size
 *  @author Jorg Janke (generated) 
 *  @version Release 2.6.1 - $Id$ */
public class X_C_BP_Size extends PO {

    /** Standard Constructor
@param ctx context
@param C_BP_Size_ID id
@param trxName transaction
*/
    public X_C_BP_Size(Properties ctx, int C_BP_Size_ID, String trxName) {
        super(ctx, C_BP_Size_ID, trxName);
    }

    /** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
    public X_C_BP_Size(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /** AD_Table_ID=912 */
    public static final int Table_ID = 912;

    /** TableName=C_BP_Size */
    public static final String Table_Name = "C_BP_Size";

    protected static KeyNamePair Model = new KeyNamePair(912, "C_BP_Size");

    protected BigDecimal accessLevel = new BigDecimal(3);

    /** AccessLevel
@return 3 - Client - Org 
*/
    protected int get_AccessLevel() {
        return accessLevel.intValue();
    }

    /** Load Meta Data
@param ctx context
@return PO Info
*/
    protected POInfo initPO(Properties ctx) {
        POInfo poi = POInfo.getPOInfo(ctx, Table_ID);
        return poi;
    }

    /** Info
@return info
*/
    public String toString() {
        StringBuffer sb = new StringBuffer("X_C_BP_Size[").append(get_ID()).append("]");
        return sb.toString();
    }

    /** Set BP Size.
@param C_BP_Size_ID Business Partner Size */
    public void setC_BP_Size_ID(int C_BP_Size_ID) {
        if (C_BP_Size_ID < 1) throw new IllegalArgumentException("C_BP_Size_ID is mandatory.");
        set_ValueNoCheck("C_BP_Size_ID", new Integer(C_BP_Size_ID));
    }

    /** Get BP Size.
@return Business Partner Size */
    public int getC_BP_Size_ID() {
        Integer ii = (Integer) get_Value("C_BP_Size_ID");
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Description.
@param Description Optional short description of the record */
    public void setDescription(String Description) {
        if (Description != null && Description.length() > 255) {
            log.warning("Length > 255 - truncated");
            Description = Description.substring(0, 254);
        }
        set_Value("Description", Description);
    }

    /** Get Description.
@return Optional short description of the record */
    public String getDescription() {
        return (String) get_Value("Description");
    }

    /** Set Comment/Help.
@param Help Comment or Hint */
    public void setHelp(String Help) {
        if (Help != null && Help.length() > 2000) {
            log.warning("Length > 2000 - truncated");
            Help = Help.substring(0, 1999);
        }
        set_Value("Help", Help);
    }

    /** Get Comment/Help.
@return Comment or Hint */
    public String getHelp() {
        return (String) get_Value("Help");
    }

    /** Set Name.
@param Name Alphanumeric identifier of the entity */
    public void setName(String Name) {
        if (Name == null) throw new IllegalArgumentException("Name is mandatory.");
        if (Name.length() > 120) {
            log.warning("Length > 120 - truncated");
            Name = Name.substring(0, 119);
        }
        set_Value("Name", Name);
    }

    /** Get Name.
@return Alphanumeric identifier of the entity */
    public String getName() {
        return (String) get_Value("Name");
    }

    /** Get Record ID/ColumnName
@return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() {
        return new KeyNamePair(get_ID(), getName());
    }

    /** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
    public void setValue(String Value) {
        if (Value == null) throw new IllegalArgumentException("Value is mandatory.");
        if (Value.length() > 40) {
            log.warning("Length > 40 - truncated");
            Value = Value.substring(0, 39);
        }
        set_Value("Value", Value);
    }

    /** Get Search Key.
@return Search key for the record in the format required - must be unique */
    public String getValue() {
        return (String) get_Value("Value");
    }
}
