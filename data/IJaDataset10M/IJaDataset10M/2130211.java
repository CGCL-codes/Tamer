package org.adempierelbr.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for LBR_DocPrint
 *  @author ADempiereLBR (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_LBR_DocPrint extends PO implements I_LBR_DocPrint, I_Persistent {

    /**
	 *
	 */
    private static final long serialVersionUID = 20110202L;

    /** Standard Constructor */
    public X_LBR_DocPrint(Properties ctx, int LBR_DocPrint_ID, String trxName) {
        super(ctx, LBR_DocPrint_ID, trxName);
    }

    /** Load Constructor */
    public X_LBR_DocPrint(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 4 - System 
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
        StringBuffer sb = new StringBuffer("X_LBR_DocPrint[").append(get_ID()).append("]");
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

    /** Set Create Fields.
		@param lbr_CreateFields 
		Processo to Create Document Fields
	  */
    public void setlbr_CreateFields(String lbr_CreateFields) {
        set_Value(COLUMNNAME_lbr_CreateFields, lbr_CreateFields);
    }

    /** Get Create Fields.
		@return Processo to Create Document Fields
	  */
    public String getlbr_CreateFields() {
        return (String) get_Value(COLUMNNAME_lbr_CreateFields);
    }

    /** Set DocPrint.
		@param LBR_DocPrint_ID 
		Primary key table LBR_DocPrint
	  */
    public void setLBR_DocPrint_ID(int LBR_DocPrint_ID) {
        if (LBR_DocPrint_ID < 1) set_ValueNoCheck(COLUMNNAME_LBR_DocPrint_ID, null); else set_ValueNoCheck(COLUMNNAME_LBR_DocPrint_ID, Integer.valueOf(LBR_DocPrint_ID));
    }

    /** Get DocPrint.
		@return Primary key table LBR_DocPrint
	  */
    public int getLBR_DocPrint_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_LBR_DocPrint_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Has SubDoc.
		@param lbr_HasSubDoc 
		Identifies if the Document has SubDocuments
	  */
    public void setlbr_HasSubDoc(boolean lbr_HasSubDoc) {
        set_Value(COLUMNNAME_lbr_HasSubDoc, Boolean.valueOf(lbr_HasSubDoc));
    }

    /** Get Has SubDoc.
		@return Identifies if the Document has SubDocuments
	  */
    public boolean islbr_HasSubDoc() {
        Object oo = get_Value(COLUMNNAME_lbr_HasSubDoc);
        if (oo != null) {
            if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set IsSubDoc.
		@param lbr_IsSubDoc 
		Defines if this Documento is a SubDocument
	  */
    public void setlbr_IsSubDoc(boolean lbr_IsSubDoc) {
        set_Value(COLUMNNAME_lbr_IsSubDoc, Boolean.valueOf(lbr_IsSubDoc));
    }

    /** Get IsSubDoc.
		@return Defines if this Documento is a SubDocument
	  */
    public boolean islbr_IsSubDoc() {
        Object oo = get_Value(COLUMNNAME_lbr_IsSubDoc);
        if (oo != null) {
            if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }

    public org.adempierelbr.model.I_LBR_MatrixPrinter getLBR_MatrixPrinter() throws RuntimeException {
        return (org.adempierelbr.model.I_LBR_MatrixPrinter) MTable.get(getCtx(), org.adempierelbr.model.I_LBR_MatrixPrinter.Table_Name).getPO(getLBR_MatrixPrinter_ID(), get_TrxName());
    }

    /** Set Matrix Printer.
		@param LBR_MatrixPrinter_ID 
		Primary key table LBR_MatrixPrinter
	  */
    public void setLBR_MatrixPrinter_ID(int LBR_MatrixPrinter_ID) {
        if (LBR_MatrixPrinter_ID < 1) set_Value(COLUMNNAME_LBR_MatrixPrinter_ID, null); else set_Value(COLUMNNAME_LBR_MatrixPrinter_ID, Integer.valueOf(LBR_MatrixPrinter_ID));
    }

    /** Get Matrix Printer.
		@return Primary key table LBR_MatrixPrinter
	  */
    public int getLBR_MatrixPrinter_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_LBR_MatrixPrinter_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Number of Columns.
		@param lbr_NoCols 
		Identifies the Number of Columns
	  */
    public void setlbr_NoCols(int lbr_NoCols) {
        set_Value(COLUMNNAME_lbr_NoCols, Integer.valueOf(lbr_NoCols));
    }

    /** Get Number of Columns.
		@return Identifies the Number of Columns
	  */
    public int getlbr_NoCols() {
        Integer ii = (Integer) get_Value(COLUMNNAME_lbr_NoCols);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Number of Rows.
		@param lbr_NoRows 
		Identifies the Number of Rows (If this is a SubDocument, enter 0 for unlimited)
	  */
    public void setlbr_NoRows(int lbr_NoRows) {
        set_Value(COLUMNNAME_lbr_NoRows, Integer.valueOf(lbr_NoRows));
    }

    /** Get Number of Rows.
		@return Identifies the Number of Rows (If this is a SubDocument, enter 0 for unlimited)
	  */
    public int getlbr_NoRows() {
        Integer ii = (Integer) get_Value(COLUMNNAME_lbr_NoRows);
        if (ii == null) return 0;
        return ii.intValue();
    }

    public org.adempierelbr.model.I_LBR_DocPrint getlbr_SubDoc() throws RuntimeException {
        return (org.adempierelbr.model.I_LBR_DocPrint) MTable.get(getCtx(), org.adempierelbr.model.I_LBR_DocPrint.Table_Name).getPO(getlbr_SubDoc_ID(), get_TrxName());
    }

    /** Set SubDoc_ID.
		@param lbr_SubDoc_ID 
		Identifies the ID of the SubDocument
	  */
    public void setlbr_SubDoc_ID(int lbr_SubDoc_ID) {
        if (lbr_SubDoc_ID < 1) set_Value(COLUMNNAME_lbr_SubDoc_ID, null); else set_Value(COLUMNNAME_lbr_SubDoc_ID, Integer.valueOf(lbr_SubDoc_ID));
    }

    /** Get SubDoc_ID.
		@return Identifies the ID of the SubDocument
	  */
    public int getlbr_SubDoc_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_lbr_SubDoc_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set SubDoc Row.
		@param lbr_SubDocRow 
		Identifies the Starter Row of the SubDocument
	  */
    public void setlbr_SubDocRow(int lbr_SubDocRow) {
        set_Value(COLUMNNAME_lbr_SubDocRow, Integer.valueOf(lbr_SubDocRow));
    }

    /** Get SubDoc Row.
		@return Identifies the Starter Row of the SubDocument
	  */
    public int getlbr_SubDocRow() {
        Integer ii = (Integer) get_Value(COLUMNNAME_lbr_SubDocRow);
        if (ii == null) return 0;
        return ii.intValue();
    }

    public org.adempierelbr.model.I_LBR_DocPrint getlbr_SubDoc2() throws RuntimeException {
        return (org.adempierelbr.model.I_LBR_DocPrint) MTable.get(getCtx(), org.adempierelbr.model.I_LBR_DocPrint.Table_Name).getPO(getlbr_SubDoc2_ID(), get_TrxName());
    }

    /** Set SubDoc_ID (2).
		@param lbr_SubDoc2_ID 
		Identifies the ID of the SubDocument
	  */
    public void setlbr_SubDoc2_ID(int lbr_SubDoc2_ID) {
        if (lbr_SubDoc2_ID < 1) set_Value(COLUMNNAME_lbr_SubDoc2_ID, null); else set_Value(COLUMNNAME_lbr_SubDoc2_ID, Integer.valueOf(lbr_SubDoc2_ID));
    }

    /** Get SubDoc_ID (2).
		@return Identifies the ID of the SubDocument
	  */
    public int getlbr_SubDoc2_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_lbr_SubDoc2_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Table Name.
		@param lbr_TableName 
		Identifies the Table or View Name
	  */
    public void setlbr_TableName(String lbr_TableName) {
        set_Value(COLUMNNAME_lbr_TableName, lbr_TableName);
    }

    /** Get Table Name.
		@return Identifies the Table or View Name
	  */
    public String getlbr_TableName() {
        return (String) get_Value(COLUMNNAME_lbr_TableName);
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
