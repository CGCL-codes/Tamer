package org.compiere.model;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

/** Generated Model for M_LotCtlExclude
 *  @author Adempiere (generated) 
 *  @version Release 3.3.1t - $Id$ */
public class X_M_LotCtlExclude extends PO implements I_M_LotCtlExclude, I_Persistent {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    /** Standard Constructor */
    public X_M_LotCtlExclude(Properties ctx, int M_LotCtlExclude_ID, String trxName) {
        super(ctx, M_LotCtlExclude_ID, trxName);
    }

    /** Load Constructor */
    public X_M_LotCtlExclude(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 2 - Client 
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
        StringBuffer sb = new StringBuffer("X_M_LotCtlExclude[").append(get_ID()).append("]");
        return sb.toString();
    }

    public I_AD_Table getAD_Table() throws Exception {
        Class<?> clazz = MTable.getClass(I_AD_Table.Table_Name);
        I_AD_Table result = null;
        try {
            Constructor<?> constructor = null;
            constructor = clazz.getDeclaredConstructor(new Class[] { Properties.class, int.class, String.class });
            result = (I_AD_Table) constructor.newInstance(new Object[] { getCtx(), new Integer(getAD_Table_ID()), get_TrxName() });
        } catch (Exception e) {
            log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
            log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
            throw e;
        }
        return result;
    }

    /** Set Table.
		@param AD_Table_ID 
		Database Table information
	  */
    public void setAD_Table_ID(int AD_Table_ID) {
        if (AD_Table_ID < 1) throw new IllegalArgumentException("AD_Table_ID is mandatory.");
        set_Value(COLUMNNAME_AD_Table_ID, Integer.valueOf(AD_Table_ID));
    }

    /** Get Table.
		@return Database Table information
	  */
    public int getAD_Table_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_AD_Table_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Sales Transaction.
		@param IsSOTrx 
		This is a Sales Transaction
	  */
    public void setIsSOTrx(boolean IsSOTrx) {
        set_Value(COLUMNNAME_IsSOTrx, Boolean.valueOf(IsSOTrx));
    }

    /** Get Sales Transaction.
		@return This is a Sales Transaction
	  */
    public boolean isSOTrx() {
        Object oo = get_Value(COLUMNNAME_IsSOTrx);
        if (oo != null) {
            if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set Exclude Lot.
		@param M_LotCtlExclude_ID 
		Exclude the ability to create Lots in Attribute Sets
	  */
    public void setM_LotCtlExclude_ID(int M_LotCtlExclude_ID) {
        if (M_LotCtlExclude_ID < 1) throw new IllegalArgumentException("M_LotCtlExclude_ID is mandatory.");
        set_ValueNoCheck(COLUMNNAME_M_LotCtlExclude_ID, Integer.valueOf(M_LotCtlExclude_ID));
    }

    /** Get Exclude Lot.
		@return Exclude the ability to create Lots in Attribute Sets
	  */
    public int getM_LotCtlExclude_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_M_LotCtlExclude_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    public I_M_LotCtl getM_LotCtl() throws Exception {
        Class<?> clazz = MTable.getClass(I_M_LotCtl.Table_Name);
        I_M_LotCtl result = null;
        try {
            Constructor<?> constructor = null;
            constructor = clazz.getDeclaredConstructor(new Class[] { Properties.class, int.class, String.class });
            result = (I_M_LotCtl) constructor.newInstance(new Object[] { getCtx(), new Integer(getM_LotCtl_ID()), get_TrxName() });
        } catch (Exception e) {
            log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
            log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
            throw e;
        }
        return result;
    }

    /** Set Lot Control.
		@param M_LotCtl_ID 
		Product Lot Control
	  */
    public void setM_LotCtl_ID(int M_LotCtl_ID) {
        if (M_LotCtl_ID < 1) throw new IllegalArgumentException("M_LotCtl_ID is mandatory.");
        set_ValueNoCheck(COLUMNNAME_M_LotCtl_ID, Integer.valueOf(M_LotCtl_ID));
    }

    /** Get Lot Control.
		@return Product Lot Control
	  */
    public int getM_LotCtl_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_M_LotCtl_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }
}
