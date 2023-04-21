package org.adempierelbr.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for LBR_CNAB
 *  @author ADempiereLBR (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_LBR_CNAB extends PO implements I_LBR_CNAB, I_Persistent {

    /**
	 *
	 */
    private static final long serialVersionUID = 20110202L;

    /** Standard Constructor */
    public X_LBR_CNAB(Properties ctx, int LBR_CNAB_ID, String trxName) {
        super(ctx, LBR_CNAB_ID, trxName);
    }

    /** Load Constructor */
    public X_LBR_CNAB(Properties ctx, ResultSet rs, String trxName) {
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
        StringBuffer sb = new StringBuffer("X_LBR_CNAB[").append(get_ID()).append("]");
        return sb.toString();
    }

    public I_C_BankAccount getC_BankAccount() throws RuntimeException {
        return (I_C_BankAccount) MTable.get(getCtx(), I_C_BankAccount.Table_Name).getPO(getC_BankAccount_ID(), get_TrxName());
    }

    /** Set Bank Account.
		@param C_BankAccount_ID 
		Account at the Bank
	  */
    public void setC_BankAccount_ID(int C_BankAccount_ID) {
        if (C_BankAccount_ID < 1) set_Value(COLUMNNAME_C_BankAccount_ID, null); else set_Value(COLUMNNAME_C_BankAccount_ID, Integer.valueOf(C_BankAccount_ID));
    }

    /** Get Bank Account.
		@return Account at the Bank
	  */
    public int getC_BankAccount_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_C_BankAccount_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Registered.
		@param IsRegistered 
		The application is registered.
	  */
    public void setIsRegistered(boolean IsRegistered) {
        set_Value(COLUMNNAME_IsRegistered, Boolean.valueOf(IsRegistered));
    }

    /** Get Registered.
		@return The application is registered.
	  */
    public boolean isRegistered() {
        Object oo = get_Value(COLUMNNAME_IsRegistered);
        if (oo != null) {
            if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set Selected.
		@param IsSelected Selected	  */
    public void setIsSelected(boolean IsSelected) {
        set_Value(COLUMNNAME_IsSelected, Boolean.valueOf(IsSelected));
    }

    /** Get Selected.
		@return Selected	  */
    public boolean isSelected() {
        Object oo = get_Value(COLUMNNAME_IsSelected);
        if (oo != null) {
            if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }

    public org.adempierelbr.model.I_LBR_Boleto getLBR_Boleto() throws RuntimeException {
        return (org.adempierelbr.model.I_LBR_Boleto) MTable.get(getCtx(), org.adempierelbr.model.I_LBR_Boleto.Table_Name).getPO(getLBR_Boleto_ID(), get_TrxName());
    }

    /** Set Boleto.
		@param LBR_Boleto_ID 
		Primary Key table LBR_Boleto
	  */
    public void setLBR_Boleto_ID(int LBR_Boleto_ID) {
        if (LBR_Boleto_ID < 1) set_Value(COLUMNNAME_LBR_Boleto_ID, null); else set_Value(COLUMNNAME_LBR_Boleto_ID, Integer.valueOf(LBR_Boleto_ID));
    }

    /** Get Boleto.
		@return Primary Key table LBR_Boleto
	  */
    public int getLBR_Boleto_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_LBR_Boleto_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set CNAB Field1.
		@param lbr_CNABField1 
		CNAB Field1
	  */
    public void setlbr_CNABField1(String lbr_CNABField1) {
        set_Value(COLUMNNAME_lbr_CNABField1, lbr_CNABField1);
    }

    /** Get CNAB Field1.
		@return CNAB Field1
	  */
    public String getlbr_CNABField1() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField1);
    }

    /** Set CNAB Field10.
		@param lbr_CNABField10 
		CNAB Field10
	  */
    public void setlbr_CNABField10(String lbr_CNABField10) {
        set_Value(COLUMNNAME_lbr_CNABField10, lbr_CNABField10);
    }

    /** Get CNAB Field10.
		@return CNAB Field10
	  */
    public String getlbr_CNABField10() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField10);
    }

    /** Set CNAB Field11.
		@param lbr_CNABField11 
		CNAB Field11
	  */
    public void setlbr_CNABField11(String lbr_CNABField11) {
        set_Value(COLUMNNAME_lbr_CNABField11, lbr_CNABField11);
    }

    /** Get CNAB Field11.
		@return CNAB Field11
	  */
    public String getlbr_CNABField11() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField11);
    }

    /** Set CNAB Field12.
		@param lbr_CNABField12 
		CNAB Field12
	  */
    public void setlbr_CNABField12(String lbr_CNABField12) {
        set_Value(COLUMNNAME_lbr_CNABField12, lbr_CNABField12);
    }

    /** Get CNAB Field12.
		@return CNAB Field12
	  */
    public String getlbr_CNABField12() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField12);
    }

    /** Set CNAB Field13.
		@param lbr_CNABField13 
		CNAB Field13
	  */
    public void setlbr_CNABField13(String lbr_CNABField13) {
        set_Value(COLUMNNAME_lbr_CNABField13, lbr_CNABField13);
    }

    /** Get CNAB Field13.
		@return CNAB Field13
	  */
    public String getlbr_CNABField13() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField13);
    }

    /** Set CNAB Field14.
		@param lbr_CNABField14 
		CNAB Field14
	  */
    public void setlbr_CNABField14(String lbr_CNABField14) {
        set_Value(COLUMNNAME_lbr_CNABField14, lbr_CNABField14);
    }

    /** Get CNAB Field14.
		@return CNAB Field14
	  */
    public String getlbr_CNABField14() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField14);
    }

    /** Set CNAB Field15.
		@param lbr_CNABField15 
		CNAB Field15
	  */
    public void setlbr_CNABField15(String lbr_CNABField15) {
        set_Value(COLUMNNAME_lbr_CNABField15, lbr_CNABField15);
    }

    /** Get CNAB Field15.
		@return CNAB Field15
	  */
    public String getlbr_CNABField15() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField15);
    }

    /** Set CNAB Field16.
		@param lbr_CNABField16 
		CNAB Field16
	  */
    public void setlbr_CNABField16(String lbr_CNABField16) {
        set_Value(COLUMNNAME_lbr_CNABField16, lbr_CNABField16);
    }

    /** Get CNAB Field16.
		@return CNAB Field16
	  */
    public String getlbr_CNABField16() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField16);
    }

    /** Set CNAB Field17.
		@param lbr_CNABField17 
		CNAB Field17
	  */
    public void setlbr_CNABField17(String lbr_CNABField17) {
        set_Value(COLUMNNAME_lbr_CNABField17, lbr_CNABField17);
    }

    /** Get CNAB Field17.
		@return CNAB Field17
	  */
    public String getlbr_CNABField17() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField17);
    }

    /** Set CNAB Field18.
		@param lbr_CNABField18 
		CNAB Field18
	  */
    public void setlbr_CNABField18(String lbr_CNABField18) {
        set_Value(COLUMNNAME_lbr_CNABField18, lbr_CNABField18);
    }

    /** Get CNAB Field18.
		@return CNAB Field18
	  */
    public String getlbr_CNABField18() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField18);
    }

    /** Set CNAB Field19.
		@param lbr_CNABField19 
		CNAB Field19
	  */
    public void setlbr_CNABField19(String lbr_CNABField19) {
        set_Value(COLUMNNAME_lbr_CNABField19, lbr_CNABField19);
    }

    /** Get CNAB Field19.
		@return CNAB Field19
	  */
    public String getlbr_CNABField19() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField19);
    }

    /** Set CNAB Field2.
		@param lbr_CNABField2 
		CNAB Field2
	  */
    public void setlbr_CNABField2(String lbr_CNABField2) {
        set_Value(COLUMNNAME_lbr_CNABField2, lbr_CNABField2);
    }

    /** Get CNAB Field2.
		@return CNAB Field2
	  */
    public String getlbr_CNABField2() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField2);
    }

    /** Set CNAB Field20.
		@param lbr_CNABField20 
		CNAB Field20
	  */
    public void setlbr_CNABField20(String lbr_CNABField20) {
        set_Value(COLUMNNAME_lbr_CNABField20, lbr_CNABField20);
    }

    /** Get CNAB Field20.
		@return CNAB Field20
	  */
    public String getlbr_CNABField20() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField20);
    }

    /** Set CNAB Field21.
		@param lbr_CNABField21 
		CNAB Field21
	  */
    public void setlbr_CNABField21(String lbr_CNABField21) {
        set_Value(COLUMNNAME_lbr_CNABField21, lbr_CNABField21);
    }

    /** Get CNAB Field21.
		@return CNAB Field21
	  */
    public String getlbr_CNABField21() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField21);
    }

    /** Set CNAB Field22.
		@param lbr_CNABField22 
		CNAB Field22
	  */
    public void setlbr_CNABField22(String lbr_CNABField22) {
        set_Value(COLUMNNAME_lbr_CNABField22, lbr_CNABField22);
    }

    /** Get CNAB Field22.
		@return CNAB Field22
	  */
    public String getlbr_CNABField22() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField22);
    }

    /** Set CNAB Field23.
		@param lbr_CNABField23 
		CNAB Field23
	  */
    public void setlbr_CNABField23(String lbr_CNABField23) {
        set_Value(COLUMNNAME_lbr_CNABField23, lbr_CNABField23);
    }

    /** Get CNAB Field23.
		@return CNAB Field23
	  */
    public String getlbr_CNABField23() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField23);
    }

    /** Set CNAB Field24.
		@param lbr_CNABField24 
		CNAB Field24
	  */
    public void setlbr_CNABField24(String lbr_CNABField24) {
        set_Value(COLUMNNAME_lbr_CNABField24, lbr_CNABField24);
    }

    /** Get CNAB Field24.
		@return CNAB Field24
	  */
    public String getlbr_CNABField24() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField24);
    }

    /** Set CNAB Field25.
		@param lbr_CNABField25 
		CNAB Field25
	  */
    public void setlbr_CNABField25(String lbr_CNABField25) {
        set_Value(COLUMNNAME_lbr_CNABField25, lbr_CNABField25);
    }

    /** Get CNAB Field25.
		@return CNAB Field25
	  */
    public String getlbr_CNABField25() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField25);
    }

    /** Set CNAB Field26.
		@param lbr_CNABField26 
		CNAB Field26
	  */
    public void setlbr_CNABField26(String lbr_CNABField26) {
        set_Value(COLUMNNAME_lbr_CNABField26, lbr_CNABField26);
    }

    /** Get CNAB Field26.
		@return CNAB Field26
	  */
    public String getlbr_CNABField26() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField26);
    }

    /** Set CNAB Field27.
		@param lbr_CNABField27 
		CNAB Field27
	  */
    public void setlbr_CNABField27(String lbr_CNABField27) {
        set_Value(COLUMNNAME_lbr_CNABField27, lbr_CNABField27);
    }

    /** Get CNAB Field27.
		@return CNAB Field27
	  */
    public String getlbr_CNABField27() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField27);
    }

    /** Set CNAB Field28.
		@param lbr_CNABField28 
		CNAB Field28
	  */
    public void setlbr_CNABField28(String lbr_CNABField28) {
        set_Value(COLUMNNAME_lbr_CNABField28, lbr_CNABField28);
    }

    /** Get CNAB Field28.
		@return CNAB Field28
	  */
    public String getlbr_CNABField28() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField28);
    }

    /** Set CNAB Field29.
		@param lbr_CNABField29 
		CNAB Field29
	  */
    public void setlbr_CNABField29(String lbr_CNABField29) {
        set_Value(COLUMNNAME_lbr_CNABField29, lbr_CNABField29);
    }

    /** Get CNAB Field29.
		@return CNAB Field29
	  */
    public String getlbr_CNABField29() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField29);
    }

    /** Set CNAB Field3.
		@param lbr_CNABField3 
		CNAB Field3
	  */
    public void setlbr_CNABField3(String lbr_CNABField3) {
        set_Value(COLUMNNAME_lbr_CNABField3, lbr_CNABField3);
    }

    /** Get CNAB Field3.
		@return CNAB Field3
	  */
    public String getlbr_CNABField3() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField3);
    }

    /** Set CNAB Field30.
		@param lbr_CNABField30 
		CNAB Field30
	  */
    public void setlbr_CNABField30(String lbr_CNABField30) {
        set_Value(COLUMNNAME_lbr_CNABField30, lbr_CNABField30);
    }

    /** Get CNAB Field30.
		@return CNAB Field30
	  */
    public String getlbr_CNABField30() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField30);
    }

    /** Set CNAB Field31.
		@param lbr_CNABField31 
		CNAB Field31
	  */
    public void setlbr_CNABField31(String lbr_CNABField31) {
        set_Value(COLUMNNAME_lbr_CNABField31, lbr_CNABField31);
    }

    /** Get CNAB Field31.
		@return CNAB Field31
	  */
    public String getlbr_CNABField31() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField31);
    }

    /** Set CNAB Field32.
		@param lbr_CNABField32 
		CNAB Field32
	  */
    public void setlbr_CNABField32(String lbr_CNABField32) {
        set_Value(COLUMNNAME_lbr_CNABField32, lbr_CNABField32);
    }

    /** Get CNAB Field32.
		@return CNAB Field32
	  */
    public String getlbr_CNABField32() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField32);
    }

    /** Set CNAB Field33.
		@param lbr_CNABField33 
		CNAB Field33
	  */
    public void setlbr_CNABField33(String lbr_CNABField33) {
        set_Value(COLUMNNAME_lbr_CNABField33, lbr_CNABField33);
    }

    /** Get CNAB Field33.
		@return CNAB Field33
	  */
    public String getlbr_CNABField33() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField33);
    }

    /** Set CNAB Field34.
		@param lbr_CNABField34 
		CNAB Field34
	  */
    public void setlbr_CNABField34(String lbr_CNABField34) {
        set_Value(COLUMNNAME_lbr_CNABField34, lbr_CNABField34);
    }

    /** Get CNAB Field34.
		@return CNAB Field34
	  */
    public String getlbr_CNABField34() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField34);
    }

    /** Set CNAB Field35.
		@param lbr_CNABField35 
		CNAB Field35
	  */
    public void setlbr_CNABField35(String lbr_CNABField35) {
        set_Value(COLUMNNAME_lbr_CNABField35, lbr_CNABField35);
    }

    /** Get CNAB Field35.
		@return CNAB Field35
	  */
    public String getlbr_CNABField35() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField35);
    }

    /** Set CNAB Field36.
		@param lbr_CNABField36 
		CNAB Field36
	  */
    public void setlbr_CNABField36(String lbr_CNABField36) {
        set_Value(COLUMNNAME_lbr_CNABField36, lbr_CNABField36);
    }

    /** Get CNAB Field36.
		@return CNAB Field36
	  */
    public String getlbr_CNABField36() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField36);
    }

    /** Set CNAB Field37.
		@param lbr_CNABField37 
		CNAB Field37
	  */
    public void setlbr_CNABField37(String lbr_CNABField37) {
        set_Value(COLUMNNAME_lbr_CNABField37, lbr_CNABField37);
    }

    /** Get CNAB Field37.
		@return CNAB Field37
	  */
    public String getlbr_CNABField37() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField37);
    }

    /** Set CNAB Field38.
		@param lbr_CNABField38 
		CNAB Field38
	  */
    public void setlbr_CNABField38(String lbr_CNABField38) {
        set_Value(COLUMNNAME_lbr_CNABField38, lbr_CNABField38);
    }

    /** Get CNAB Field38.
		@return CNAB Field38
	  */
    public String getlbr_CNABField38() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField38);
    }

    /** Set CNAB Field39.
		@param lbr_CNABField39 
		CNAB Field39
	  */
    public void setlbr_CNABField39(String lbr_CNABField39) {
        set_Value(COLUMNNAME_lbr_CNABField39, lbr_CNABField39);
    }

    /** Get CNAB Field39.
		@return CNAB Field39
	  */
    public String getlbr_CNABField39() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField39);
    }

    /** Set CNAB Field4.
		@param lbr_CNABField4 
		CNAB Field4
	  */
    public void setlbr_CNABField4(String lbr_CNABField4) {
        set_Value(COLUMNNAME_lbr_CNABField4, lbr_CNABField4);
    }

    /** Get CNAB Field4.
		@return CNAB Field4
	  */
    public String getlbr_CNABField4() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField4);
    }

    /** Set CNAB Field40.
		@param lbr_CNABField40 
		CNAB Field40
	  */
    public void setlbr_CNABField40(String lbr_CNABField40) {
        set_Value(COLUMNNAME_lbr_CNABField40, lbr_CNABField40);
    }

    /** Get CNAB Field40.
		@return CNAB Field40
	  */
    public String getlbr_CNABField40() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField40);
    }

    /** Set CNAB Field41.
		@param lbr_CNABField41 
		CNAB Field41
	  */
    public void setlbr_CNABField41(String lbr_CNABField41) {
        set_Value(COLUMNNAME_lbr_CNABField41, lbr_CNABField41);
    }

    /** Get CNAB Field41.
		@return CNAB Field41
	  */
    public String getlbr_CNABField41() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField41);
    }

    /** Set CNAB Field42.
		@param lbr_CNABField42 
		CNAB Field42
	  */
    public void setlbr_CNABField42(String lbr_CNABField42) {
        set_Value(COLUMNNAME_lbr_CNABField42, lbr_CNABField42);
    }

    /** Get CNAB Field42.
		@return CNAB Field42
	  */
    public String getlbr_CNABField42() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField42);
    }

    /** Set CNAB Field43.
		@param lbr_CNABField43 
		CNAB Field43
	  */
    public void setlbr_CNABField43(String lbr_CNABField43) {
        set_Value(COLUMNNAME_lbr_CNABField43, lbr_CNABField43);
    }

    /** Get CNAB Field43.
		@return CNAB Field43
	  */
    public String getlbr_CNABField43() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField43);
    }

    /** Set CNAB Field44.
		@param lbr_CNABField44 
		CNAB Field44
	  */
    public void setlbr_CNABField44(String lbr_CNABField44) {
        set_Value(COLUMNNAME_lbr_CNABField44, lbr_CNABField44);
    }

    /** Get CNAB Field44.
		@return CNAB Field44
	  */
    public String getlbr_CNABField44() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField44);
    }

    /** Set CNAB Field45.
		@param lbr_CNABField45 
		CNAB Field45
	  */
    public void setlbr_CNABField45(String lbr_CNABField45) {
        set_Value(COLUMNNAME_lbr_CNABField45, lbr_CNABField45);
    }

    /** Get CNAB Field45.
		@return CNAB Field45
	  */
    public String getlbr_CNABField45() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField45);
    }

    /** Set CNAB Field46.
		@param lbr_CNABField46 
		CNAB Field46
	  */
    public void setlbr_CNABField46(String lbr_CNABField46) {
        set_Value(COLUMNNAME_lbr_CNABField46, lbr_CNABField46);
    }

    /** Get CNAB Field46.
		@return CNAB Field46
	  */
    public String getlbr_CNABField46() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField46);
    }

    /** Set CNAB Field47.
		@param lbr_CNABField47 
		CNAB Field47
	  */
    public void setlbr_CNABField47(String lbr_CNABField47) {
        set_Value(COLUMNNAME_lbr_CNABField47, lbr_CNABField47);
    }

    /** Get CNAB Field47.
		@return CNAB Field47
	  */
    public String getlbr_CNABField47() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField47);
    }

    /** Set CNAB Field48.
		@param lbr_CNABField48 
		CNAB Field48
	  */
    public void setlbr_CNABField48(String lbr_CNABField48) {
        set_Value(COLUMNNAME_lbr_CNABField48, lbr_CNABField48);
    }

    /** Get CNAB Field48.
		@return CNAB Field48
	  */
    public String getlbr_CNABField48() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField48);
    }

    /** Set CNAB Field49.
		@param lbr_CNABField49 
		CNAB Field49
	  */
    public void setlbr_CNABField49(String lbr_CNABField49) {
        set_Value(COLUMNNAME_lbr_CNABField49, lbr_CNABField49);
    }

    /** Get CNAB Field49.
		@return CNAB Field49
	  */
    public String getlbr_CNABField49() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField49);
    }

    /** Set CNAB Field5.
		@param lbr_CNABField5 
		CNAB Field5
	  */
    public void setlbr_CNABField5(String lbr_CNABField5) {
        set_Value(COLUMNNAME_lbr_CNABField5, lbr_CNABField5);
    }

    /** Get CNAB Field5.
		@return CNAB Field5
	  */
    public String getlbr_CNABField5() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField5);
    }

    /** Set CNAB Field50.
		@param lbr_CNABField50 
		CNAB Field50
	  */
    public void setlbr_CNABField50(String lbr_CNABField50) {
        set_Value(COLUMNNAME_lbr_CNABField50, lbr_CNABField50);
    }

    /** Get CNAB Field50.
		@return CNAB Field50
	  */
    public String getlbr_CNABField50() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField50);
    }

    /** Set CNAB Field51.
		@param lbr_CNABField51 
		CNAB Field51
	  */
    public void setlbr_CNABField51(String lbr_CNABField51) {
        set_Value(COLUMNNAME_lbr_CNABField51, lbr_CNABField51);
    }

    /** Get CNAB Field51.
		@return CNAB Field51
	  */
    public String getlbr_CNABField51() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField51);
    }

    /** Set CNAB Field52.
		@param lbr_CNABField52 
		CNAB Field52
	  */
    public void setlbr_CNABField52(String lbr_CNABField52) {
        set_Value(COLUMNNAME_lbr_CNABField52, lbr_CNABField52);
    }

    /** Get CNAB Field52.
		@return CNAB Field52
	  */
    public String getlbr_CNABField52() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField52);
    }

    /** Set CNAB Field53.
		@param lbr_CNABField53 
		CNAB Field53
	  */
    public void setlbr_CNABField53(String lbr_CNABField53) {
        set_Value(COLUMNNAME_lbr_CNABField53, lbr_CNABField53);
    }

    /** Get CNAB Field53.
		@return CNAB Field53
	  */
    public String getlbr_CNABField53() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField53);
    }

    /** Set CNAB Field6.
		@param lbr_CNABField6 
		CNAB Field6
	  */
    public void setlbr_CNABField6(String lbr_CNABField6) {
        set_Value(COLUMNNAME_lbr_CNABField6, lbr_CNABField6);
    }

    /** Get CNAB Field6.
		@return CNAB Field6
	  */
    public String getlbr_CNABField6() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField6);
    }

    /** Set CNAB Field7.
		@param lbr_CNABField7 
		CNAB Field7
	  */
    public void setlbr_CNABField7(String lbr_CNABField7) {
        set_Value(COLUMNNAME_lbr_CNABField7, lbr_CNABField7);
    }

    /** Get CNAB Field7.
		@return CNAB Field7
	  */
    public String getlbr_CNABField7() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField7);
    }

    /** Set CNAB Field8.
		@param lbr_CNABField8 
		CNAB Field8
	  */
    public void setlbr_CNABField8(String lbr_CNABField8) {
        set_Value(COLUMNNAME_lbr_CNABField8, lbr_CNABField8);
    }

    /** Get CNAB Field8.
		@return CNAB Field8
	  */
    public String getlbr_CNABField8() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField8);
    }

    /** Set CNAB Field9.
		@param lbr_CNABField9 
		CNAB Field9
	  */
    public void setlbr_CNABField9(String lbr_CNABField9) {
        set_Value(COLUMNNAME_lbr_CNABField9, lbr_CNABField9);
    }

    /** Get CNAB Field9.
		@return CNAB Field9
	  */
    public String getlbr_CNABField9() {
        return (String) get_Value(COLUMNNAME_lbr_CNABField9);
    }

    /** Set CNAB.
		@param LBR_CNAB_ID 
		Primary Key table LBR_CNAB
	  */
    public void setLBR_CNAB_ID(int LBR_CNAB_ID) {
        if (LBR_CNAB_ID < 1) set_ValueNoCheck(COLUMNNAME_LBR_CNAB_ID, null); else set_ValueNoCheck(COLUMNNAME_LBR_CNAB_ID, Integer.valueOf(LBR_CNAB_ID));
    }

    /** Get CNAB.
		@return Primary Key table LBR_CNAB
	  */
    public int getLBR_CNAB_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_LBR_CNAB_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Document Date.
		@param lbr_DocDate 
		Identifies the Document Date
	  */
    public void setlbr_DocDate(Timestamp lbr_DocDate) {
        set_Value(COLUMNNAME_lbr_DocDate, lbr_DocDate);
    }

    /** Get Document Date.
		@return Identifies the Document Date
	  */
    public Timestamp getlbr_DocDate() {
        return (Timestamp) get_Value(COLUMNNAME_lbr_DocDate);
    }

    /** Set IsCancelled.
		@param lbr_IsCancelled 
		Defines if the Document IsCancelled
	  */
    public void setlbr_IsCancelled(boolean lbr_IsCancelled) {
        set_Value(COLUMNNAME_lbr_IsCancelled, Boolean.valueOf(lbr_IsCancelled));
    }

    /** Get IsCancelled.
		@return Defines if the Document IsCancelled
	  */
    public boolean islbr_IsCancelled() {
        Object oo = get_Value(COLUMNNAME_lbr_IsCancelled);
        if (oo != null) {
            if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set Routing No.
		@param RoutingNo 
		Bank Routing Number
	  */
    public void setRoutingNo(String RoutingNo) {
        set_Value(COLUMNNAME_RoutingNo, RoutingNo);
    }

    /** Get Routing No.
		@return Bank Routing Number
	  */
    public String getRoutingNo() {
        return (String) get_Value(COLUMNNAME_RoutingNo);
    }
}
