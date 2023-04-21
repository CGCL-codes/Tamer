package org.adempierelbr.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for LBR_AverageCost
 *  @author ADempiereLBR (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_LBR_AverageCost extends PO implements I_LBR_AverageCost, I_Persistent {

    /**
	 *
	 */
    private static final long serialVersionUID = 20110323L;

    /** Standard Constructor */
    public X_LBR_AverageCost(Properties ctx, int LBR_AverageCost_ID, String trxName) {
        super(ctx, LBR_AverageCost_ID, trxName);
    }

    /** Load Constructor */
    public X_LBR_AverageCost(Properties ctx, ResultSet rs, String trxName) {
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
        StringBuffer sb = new StringBuffer("X_LBR_AverageCost[").append(get_ID()).append("]");
        return sb.toString();
    }

    public I_C_Period getC_Period() throws RuntimeException {
        return (I_C_Period) MTable.get(getCtx(), I_C_Period.Table_Name).getPO(getC_Period_ID(), get_TrxName());
    }

    /** Set Period.
		@param C_Period_ID 
		Period of the Calendar
	  */
    public void setC_Period_ID(int C_Period_ID) {
        if (C_Period_ID < 1) set_Value(COLUMNNAME_C_Period_ID, null); else set_Value(COLUMNNAME_C_Period_ID, Integer.valueOf(C_Period_ID));
    }

    /** Get Period.
		@return Period of the Calendar
	  */
    public int getC_Period_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_C_Period_ID);
        if (ii == null) return 0;
        return ii.intValue();
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

    /** Set Average Cost.
		@param LBR_AverageCost_ID 
		Defines the Average Cost
	  */
    public void setLBR_AverageCost_ID(int LBR_AverageCost_ID) {
        if (LBR_AverageCost_ID < 1) set_ValueNoCheck(COLUMNNAME_LBR_AverageCost_ID, null); else set_ValueNoCheck(COLUMNNAME_LBR_AverageCost_ID, Integer.valueOf(LBR_AverageCost_ID));
    }

    /** Get Average Cost.
		@return Defines the Average Cost
	  */
    public int getLBR_AverageCost_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_LBR_AverageCost_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set AvgCost - Confirm Manufactured.
		@param lbr_AvgCost_ConfirmMAN AvgCost - Confirm Manufactured	  */
    public void setlbr_AvgCost_ConfirmMAN(String lbr_AvgCost_ConfirmMAN) {
        set_Value(COLUMNNAME_lbr_AvgCost_ConfirmMAN, lbr_AvgCost_ConfirmMAN);
    }

    /** Get AvgCost - Confirm Manufactured.
		@return AvgCost - Confirm Manufactured	  */
    public String getlbr_AvgCost_ConfirmMAN() {
        return (String) get_Value(COLUMNNAME_lbr_AvgCost_ConfirmMAN);
    }

    /** Set AvgCost - Confirm Purchased.
		@param lbr_AvgCost_ConfirmPUR AvgCost - Confirm Purchased	  */
    public void setlbr_AvgCost_ConfirmPUR(String lbr_AvgCost_ConfirmPUR) {
        set_Value(COLUMNNAME_lbr_AvgCost_ConfirmPUR, lbr_AvgCost_ConfirmPUR);
    }

    /** Get AvgCost - Confirm Purchased.
		@return AvgCost - Confirm Purchased	  */
    public String getlbr_AvgCost_ConfirmPUR() {
        return (String) get_Value(COLUMNNAME_lbr_AvgCost_ConfirmPUR);
    }

    /** Set AvgCost - Create Record Manufactured.
		@param lbr_AvgCost_CreateMAN AvgCost - Create Record Manufactured	  */
    public void setlbr_AvgCost_CreateMAN(String lbr_AvgCost_CreateMAN) {
        set_Value(COLUMNNAME_lbr_AvgCost_CreateMAN, lbr_AvgCost_CreateMAN);
    }

    /** Get AvgCost - Create Record Manufactured.
		@return AvgCost - Create Record Manufactured	  */
    public String getlbr_AvgCost_CreateMAN() {
        return (String) get_Value(COLUMNNAME_lbr_AvgCost_CreateMAN);
    }

    /** Set AvgCost - Create Record Purchased.
		@param lbr_AvgCost_CreatePUR AvgCost - Create Record Purchased	  */
    public void setlbr_AvgCost_CreatePUR(String lbr_AvgCost_CreatePUR) {
        set_Value(COLUMNNAME_lbr_AvgCost_CreatePUR, lbr_AvgCost_CreatePUR);
    }

    /** Get AvgCost - Create Record Purchased.
		@return AvgCost - Create Record Purchased	  */
    public String getlbr_AvgCost_CreatePUR() {
        return (String) get_Value(COLUMNNAME_lbr_AvgCost_CreatePUR);
    }

    /** Set AvgCost - RePost Acct.
		@param lbr_AvgCost_RePostAcct AvgCost - RePost Acct	  */
    public void setlbr_AvgCost_RePostAcct(String lbr_AvgCost_RePostAcct) {
        set_Value(COLUMNNAME_lbr_AvgCost_RePostAcct, lbr_AvgCost_RePostAcct);
    }

    /** Get AvgCost - RePost Acct.
		@return AvgCost - RePost Acct	  */
    public String getlbr_AvgCost_RePostAcct() {
        return (String) get_Value(COLUMNNAME_lbr_AvgCost_RePostAcct);
    }

    /** Set Step 1.
		@param lbr_AvgStep1 Step 1	  */
    public void setlbr_AvgStep1(boolean lbr_AvgStep1) {
        set_Value(COLUMNNAME_lbr_AvgStep1, Boolean.valueOf(lbr_AvgStep1));
    }

    /** Get Step 1.
		@return Step 1	  */
    public boolean islbr_AvgStep1() {
        Object oo = get_Value(COLUMNNAME_lbr_AvgStep1);
        if (oo != null) {
            if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set Step 2.
		@param lbr_AvgStep2 Step 2	  */
    public void setlbr_AvgStep2(boolean lbr_AvgStep2) {
        set_Value(COLUMNNAME_lbr_AvgStep2, Boolean.valueOf(lbr_AvgStep2));
    }

    /** Get Step 2.
		@return Step 2	  */
    public boolean islbr_AvgStep2() {
        Object oo = get_Value(COLUMNNAME_lbr_AvgStep2);
        if (oo != null) {
            if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set Step 3.
		@param lbr_AvgStep3 Step 3	  */
    public void setlbr_AvgStep3(boolean lbr_AvgStep3) {
        set_Value(COLUMNNAME_lbr_AvgStep3, Boolean.valueOf(lbr_AvgStep3));
    }

    /** Get Step 3.
		@return Step 3	  */
    public boolean islbr_AvgStep3() {
        Object oo = get_Value(COLUMNNAME_lbr_AvgStep3);
        if (oo != null) {
            if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set Step 4.
		@param lbr_AvgStep4 Step 4	  */
    public void setlbr_AvgStep4(boolean lbr_AvgStep4) {
        set_Value(COLUMNNAME_lbr_AvgStep4, Boolean.valueOf(lbr_AvgStep4));
    }

    /** Get Step 4.
		@return Step 4	  */
    public boolean islbr_AvgStep4() {
        Object oo = get_Value(COLUMNNAME_lbr_AvgStep4);
        if (oo != null) {
            if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set Step 5.
		@param lbr_AvgStep5 Step 5	  */
    public void setlbr_AvgStep5(boolean lbr_AvgStep5) {
        set_Value(COLUMNNAME_lbr_AvgStep5, Boolean.valueOf(lbr_AvgStep5));
    }

    /** Get Step 5.
		@return Step 5	  */
    public boolean islbr_AvgStep5() {
        Object oo = get_Value(COLUMNNAME_lbr_AvgStep5);
        if (oo != null) {
            if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }

    public I_M_CostElement getM_CostElement() throws RuntimeException {
        return (I_M_CostElement) MTable.get(getCtx(), I_M_CostElement.Table_Name).getPO(getM_CostElement_ID(), get_TrxName());
    }

    /** Set Cost Element.
		@param M_CostElement_ID 
		Product Cost Element
	  */
    public void setM_CostElement_ID(int M_CostElement_ID) {
        if (M_CostElement_ID < 1) set_Value(COLUMNNAME_M_CostElement_ID, null); else set_Value(COLUMNNAME_M_CostElement_ID, Integer.valueOf(M_CostElement_ID));
    }

    /** Get Cost Element.
		@return Product Cost Element
	  */
    public int getM_CostElement_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_M_CostElement_ID);
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

    /** Set Processed.
		@param Processed 
		The document has been processed
	  */
    public void setProcessed(boolean Processed) {
        set_Value(COLUMNNAME_Processed, Boolean.valueOf(Processed));
    }

    /** Get Processed.
		@return The document has been processed
	  */
    public boolean isProcessed() {
        Object oo = get_Value(COLUMNNAME_Processed);
        if (oo != null) {
            if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }
}
