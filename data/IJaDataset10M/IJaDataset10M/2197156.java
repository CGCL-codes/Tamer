package org.compiere.model;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

/** Generated Model for U_RoleConfig
 *  @author Adempiere (generated) 
 *  @version Release 3.4.0s - $Id$ */
public class X_U_RoleConfig extends PO implements I_U_RoleConfig, I_Persistent {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    /** Standard Constructor */
    public X_U_RoleConfig(Properties ctx, int U_RoleConfig_ID, String trxName) {
        super(ctx, U_RoleConfig_ID, trxName);
    }

    /** Load Constructor */
    public X_U_RoleConfig(Properties ctx, ResultSet rs, String trxName) {
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
        StringBuffer sb = new StringBuffer("X_U_RoleConfig[").append(get_ID()).append("]");
        return sb.toString();
    }

    public I_AD_Role getAD_Role() throws Exception {
        Class<?> clazz = MTable.getClass(I_AD_Role.Table_Name);
        I_AD_Role result = null;
        try {
            Constructor<?> constructor = null;
            constructor = clazz.getDeclaredConstructor(new Class[] { Properties.class, int.class, String.class });
            result = (I_AD_Role) constructor.newInstance(new Object[] { getCtx(), new Integer(getAD_Role_ID()), get_TrxName() });
        } catch (Exception e) {
            log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
            log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
            throw e;
        }
        return result;
    }

    /** Set Role.
		@param AD_Role_ID 
		Responsibility Role
	  */
    public void setAD_Role_ID(int AD_Role_ID) {
        if (AD_Role_ID < 0) throw new IllegalArgumentException("AD_Role_ID is mandatory.");
        set_Value(COLUMNNAME_AD_Role_ID, Integer.valueOf(AD_Role_ID));
    }

    /** Get Role.
		@return Responsibility Role
	  */
    public int getAD_Role_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_AD_Role_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Can Create.
		@param IsCanCreate Can Create	  */
    public void setIsCanCreate(boolean IsCanCreate) {
        set_Value(COLUMNNAME_IsCanCreate, Boolean.valueOf(IsCanCreate));
    }

    /** Get Can Create.
		@return Can Create	  */
    public boolean isCanCreate() {
        Object oo = get_Value(COLUMNNAME_IsCanCreate);
        if (oo != null) {
            if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set Can Edit.
		@param IsCanEdit Can Edit	  */
    public void setIsCanEdit(boolean IsCanEdit) {
        set_Value(COLUMNNAME_IsCanEdit, Boolean.valueOf(IsCanEdit));
    }

    /** Get Can Edit.
		@return Can Edit	  */
    public boolean isCanEdit() {
        Object oo = get_Value(COLUMNNAME_IsCanEdit);
        if (oo != null) {
            if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set Can View.
		@param IsCanView Can View	  */
    public void setIsCanView(boolean IsCanView) {
        set_Value(COLUMNNAME_IsCanView, Boolean.valueOf(IsCanView));
    }

    /** Get Can View.
		@return Can View	  */
    public boolean isCanView() {
        Object oo = get_Value(COLUMNNAME_IsCanView);
        if (oo != null) {
            if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set Read Only.
		@param IsReadOnly 
		Field is read only
	  */
    public void setIsReadOnly(boolean IsReadOnly) {
        set_Value(COLUMNNAME_IsReadOnly, Boolean.valueOf(IsReadOnly));
    }

    /** Get Read Only.
		@return Field is read only
	  */
    public boolean isReadOnly() {
        Object oo = get_Value(COLUMNNAME_IsReadOnly);
        if (oo != null) {
            if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
    public void setName(String Name) {
        if (Name == null) throw new IllegalArgumentException("Name is mandatory.");
        if (Name.length() > 60) {
            log.warning("Length > 60 - truncated");
            Name = Name.substring(0, 60);
        }
        set_Value(COLUMNNAME_Name, Name);
    }

    /** Get Name.
		@return Alphanumeric identifier of the entity
	  */
    public String getName() {
        return (String) get_Value(COLUMNNAME_Name);
    }

    /** U_ConfigName_ID AD_Reference_ID=52011 */
    public static final int U_CONFIGNAME_ID_AD_Reference_ID = 52011;

    /** Set Configuration Name.
		@param U_ConfigName_ID Configuration Name	  */
    public void setU_ConfigName_ID(int U_ConfigName_ID) {
        if (U_ConfigName_ID < 1) throw new IllegalArgumentException("U_ConfigName_ID is mandatory.");
        set_Value(COLUMNNAME_U_ConfigName_ID, Integer.valueOf(U_ConfigName_ID));
    }

    /** Get Configuration Name.
		@return Configuration Name	  */
    public int getU_ConfigName_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_U_ConfigName_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Role Config.
		@param U_RoleConfig_ID Role Config	  */
    public void setU_RoleConfig_ID(int U_RoleConfig_ID) {
        if (U_RoleConfig_ID < 1) throw new IllegalArgumentException("U_RoleConfig_ID is mandatory.");
        set_ValueNoCheck(COLUMNNAME_U_RoleConfig_ID, Integer.valueOf(U_RoleConfig_ID));
    }

    /** Get Role Config.
		@return Role Config	  */
    public int getU_RoleConfig_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_U_RoleConfig_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }
}
