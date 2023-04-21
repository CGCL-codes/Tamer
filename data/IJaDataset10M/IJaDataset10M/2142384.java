package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

/** Generated Model for LBR_BPartnerCategory
 *  @author OSEB (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_LBR_BPartnerCategory extends PO implements I_LBR_BPartnerCategory, I_Persistent {

    /**
	 *
	 */
    private static final long serialVersionUID = 20100713L;

    /** Standard Constructor */
    public X_LBR_BPartnerCategory(Properties ctx, int LBR_BPartnerCategory_ID, String trxName) {
        super(ctx, LBR_BPartnerCategory_ID, trxName);
    }

    /** Load Constructor */
    public X_LBR_BPartnerCategory(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 7 - System - Client - Org 
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
        StringBuffer sb = new StringBuffer("X_LBR_BPartnerCategory[").append(get_ID()).append("]");
        return sb.toString();
    }

    /** Set Description.
		@param Description 
		Optional short description of the record
	  */
    public void setDescription(String Description) {
        set_Value(COLUMNNAME_Description, Description);
    }

    /** Get Description.
		@return Optional short description of the record
	  */
    public String getDescription() {
        return (String) get_Value(COLUMNNAME_Description);
    }

    /** Set BPartner Category.
		@param LBR_BPartnerCategory_ID 
		Primary key table LBR_BPartnerCategory
	  */
    public void setLBR_BPartnerCategory_ID(int LBR_BPartnerCategory_ID) {
        if (LBR_BPartnerCategory_ID < 1) set_ValueNoCheck(COLUMNNAME_LBR_BPartnerCategory_ID, null); else set_ValueNoCheck(COLUMNNAME_LBR_BPartnerCategory_ID, Integer.valueOf(LBR_BPartnerCategory_ID));
    }

    /** Get BPartner Category.
		@return Primary key table LBR_BPartnerCategory
	  */
    public int getLBR_BPartnerCategory_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_LBR_BPartnerCategory_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
    public void setName(String Name) {
        set_Value(COLUMNNAME_Name, Name);
    }

    /** Get Name.
		@return Alphanumeric identifier of the entity
	  */
    public String getName() {
        return (String) get_Value(COLUMNNAME_Name);
    }
}
