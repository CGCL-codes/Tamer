package org.compiere.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * 	Maintain AD_Table_ID/Record_ID constraint
 *	
 *  @author Jorg Janke
 *  @version $Id: PO_Record.java,v 1.4 2006/07/30 00:58:04 jjanke Exp $
 */
public class PO_Record {

    /**	Parent Tables		*/
    private static int[] s_parents = new int[] { X_C_Order.Table_ID, X_CM_Container.Table_ID };

    private static String[] s_parentNames = new String[] { X_C_Order.Table_Name, X_CM_Container.Table_Name };

    private static int[] s_parentChilds = new int[] { X_C_OrderLine.Table_ID, X_CM_Container_Element.Table_ID };

    private static String[] s_parentChildNames = new String[] { X_C_OrderLine.Table_Name, X_CM_Container_Element.Table_Name };

    /**	Cascade Table ID			*/
    private static int[] s_cascades = new int[] { X_AD_Attachment.Table_ID, X_AD_Archive.Table_ID, X_K_Index.Table_ID, X_AD_Note.Table_ID };

    /**	Cascade Table Names			*/
    private static String[] s_cascadeNames = new String[] { X_AD_Attachment.Table_Name, X_AD_Archive.Table_Name, X_K_Index.Table_Name, X_AD_Note.Table_Name };

    /**	Restrict Table ID			*/
    private static int[] s_restricts = new int[] { X_R_Request.Table_ID, X_CM_Chat.Table_ID };

    /**	Restrict Table Names			*/
    private static String[] s_restrictNames = new String[] { X_R_Request.Table_Name, X_CM_Chat.Table_Name };

    /**	Logger	*/
    private static CLogger log = CLogger.getCLogger(PO_Record.class);

    /**
	 * 	Delete Cascade including (selected)parent relationships
	 *	@param AD_Table_ID table
	 *	@param Record_ID record
	 *	@param trxName transaction
	 *	@return false if could not be deleted
	 */
    static boolean deleteCascade(int AD_Table_ID, int Record_ID, String trxName) {
        for (int i = 0; i < s_cascades.length; i++) {
            if (s_cascades[i] != AD_Table_ID) {
                if (s_cascadeNames[i].equals(X_AD_Attachment.Table_Name)) {
                    MAttachment attach = MAttachment.get(Env.getCtx(), AD_Table_ID, Record_ID);
                    if (attach != null) {
                        attach.delete(true, trxName);
                    }
                    continue;
                }
                Object[] params = new Object[] { new Integer(AD_Table_ID), new Integer(Record_ID) };
                StringBuffer sql = new StringBuffer("DELETE FROM ").append(s_cascadeNames[i]).append(" WHERE AD_Table_ID=? AND Record_ID=?");
                int no = DB.executeUpdate(sql.toString(), params, false, trxName);
                if (no > 0) log.config(s_cascadeNames[i] + " (" + AD_Table_ID + "/" + Record_ID + ") #" + no); else if (no < 0) {
                    log.severe(s_cascadeNames[i] + " (" + AD_Table_ID + "/" + Record_ID + ") #" + no);
                    return false;
                }
            }
        }
        for (int j = 0; j < s_parents.length; j++) {
            if (s_parents[j] == AD_Table_ID) {
                int AD_Table_IDchild = s_parentChilds[j];
                Object[] params = new Object[] { new Integer(AD_Table_IDchild), new Integer(Record_ID) };
                for (int i = 0; i < s_cascades.length; i++) {
                    StringBuffer sql = new StringBuffer("DELETE FROM ").append(s_cascadeNames[i]).append(" WHERE AD_Table_ID=? AND Record_ID IN (SELECT ").append(s_parentChildNames[j]).append("_ID FROM ").append(s_parentChildNames[j]).append(" WHERE ").append(s_parentNames[j]).append("_ID=?)");
                    int no = DB.executeUpdate(sql.toString(), params, false, trxName);
                    if (no > 0) log.config(s_cascadeNames[i] + " " + s_parentNames[j] + " (" + AD_Table_ID + "/" + Record_ID + ") #" + no); else if (no < 0) {
                        log.severe(s_cascadeNames[i] + " " + s_parentNames[j] + " (" + AD_Table_ID + "/" + Record_ID + ") #" + no);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
	 * 	An entry Exists for restrict table/record combination
	 *	@param AD_Table_ID table
	 *	@param Record_ID record
	 *	@param trxName transaction
	 *	@return error message (Table Name) or null
	 */
    static String exists(int AD_Table_ID, int Record_ID, String trxName) {
        for (int i = 0; i < s_restricts.length; i++) {
            StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM ").append(s_restrictNames[i]).append(" WHERE AD_Table_ID=? AND Record_ID=?");
            int no = DB.getSQLValue(trxName, sql.toString(), AD_Table_ID, Record_ID);
            if (no > 0) return s_restrictNames[i];
        }
        return null;
    }

    /**
	 * 	Validate all tables for AD_Table/Record_ID relationships
	 */
    static void validate() {
        String sql = "SELECT AD_Table_ID, TableName FROM AD_Table WHERE IsView='N' ORDER BY TableName";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = DB.prepareStatement(sql, null);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                validate(rs.getInt(1), rs.getString(2));
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, sql, e);
        } finally {
            DB.close(rs, pstmt);
            rs = null;
            pstmt = null;
        }
    }

    /**
	 * 	Validate all tables for AD_Table/Record_ID relationships
	 *	@param AD_Table_ID table
	 */
    static void validate(int AD_Table_ID) {
        MTable table = new MTable(Env.getCtx(), AD_Table_ID, null);
        if (table.isView()) log.warning("Ignored - View " + table.getTableName()); else validate(table.getAD_Table_ID(), table.getTableName());
    }

    /**
	 * 	Validate Table for Table/Record
	 *	@param AD_Table_ID table
	 *	@param TableName Name
	 */
    private static void validate(int AD_Table_ID, String TableName) {
        for (int i = 0; i < s_cascades.length; i++) {
            StringBuffer sql = new StringBuffer("DELETE FROM ").append(s_cascadeNames[i]).append(" WHERE AD_Table_ID=").append(AD_Table_ID).append(" AND Record_ID NOT IN (SELECT ").append(TableName).append("_ID FROM ").append(TableName).append(")");
            int no = DB.executeUpdate(sql.toString(), null);
            if (no > 0) log.config(s_cascadeNames[i] + " (" + AD_Table_ID + "/" + TableName + ") Invalid #" + no);
        }
    }
}
