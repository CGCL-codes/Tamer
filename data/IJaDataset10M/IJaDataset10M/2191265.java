package org.compiere.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Msg;

/**
 *	System Element Model
 *	
 *  @author Jorg Janke
 *  @version $Id: M_Element.java,v 1.3 2006/07/30 00:58:37 jjanke Exp $
 */
public class M_Element extends X_AD_Element {

    /**
	 * 	Get case sensitive Column Name
	 *	@param columnName case insentitive column name
	 *	@return case sensitive column name
	 */
    public static String getColumnName(String columnName) {
        return getColumnName(columnName, null);
    }

    /**
	 * 	Get case sensitive Column Name
	 *	@param columnName case insentitive column name
	 *  @param trxName optional transaction name
	 *	@return case sensitive column name
	 */
    public static String getColumnName(String columnName, String trxName) {
        if (columnName == null || columnName.length() == 0) return columnName;
        String retValue = columnName;
        String sql = "SELECT ColumnName FROM AD_Element WHERE UPPER(ColumnName)=?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = DB.prepareStatement(sql, trxName);
            pstmt.setString(1, columnName.toUpperCase());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                retValue = rs.getString(1);
                if (rs.next()) s_log.warning("Not unique: " + columnName + " -> " + retValue + " - " + rs.getString(1));
            } else s_log.warning("No found: " + columnName);
        } catch (Exception e) {
            s_log.log(Level.SEVERE, columnName, e);
        } finally {
            DB.close(rs, pstmt);
            rs = null;
            pstmt = null;
        }
        return retValue;
    }

    /**
	 * 	Get Element
	 * 	@param ctx context
	 *	@param columnName case insentitive column name
	 *	@return case sensitive column name
	 */
    public static M_Element get(Properties ctx, String columnName) {
        return get(ctx, columnName, null);
    }

    /**
	 * 	Get Element
	 * 	@param ctx context
	 *	@param columnName case insentitive column name
	 *  @param trxName optional transaction name
	 *	@return case sensitive column name
	 */
    public static M_Element get(Properties ctx, String columnName, String trxName) {
        if (columnName == null || columnName.length() == 0) return null;
        M_Element retValue = null;
        String sql = "SELECT * FROM AD_Element WHERE UPPER(ColumnName)=?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = DB.prepareStatement(sql, trxName);
            pstmt.setString(1, columnName.toUpperCase());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                retValue = new M_Element(ctx, rs, trxName);
                if (rs.next()) s_log.warning("Not unique: " + columnName + " -> " + retValue + " - " + rs.getString("ColumnName"));
            }
        } catch (Exception e) {
            s_log.log(Level.SEVERE, sql, e);
        } finally {
            DB.close(rs, pstmt);
            rs = null;
            pstmt = null;
        }
        return retValue;
    }

    /**
	 * 	Get Element
	 * 	@param ctx context
	 *	@param columnName case insentitive column name
	 *	@param columnName case insentitive column name
	 *	@param trxName trx
	 *	@return case sensitive column name
	 */
    public static M_Element getOfColumn(Properties ctx, int AD_Column_ID, String trxName) {
        if (AD_Column_ID == 0) return null;
        M_Element retValue = null;
        String sql = "SELECT * FROM AD_Element e " + "WHERE EXISTS (SELECT * FROM AD_Column c " + "WHERE c.AD_Element_ID=e.AD_Element_ID AND c.AD_Column_ID=?)";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = DB.prepareStatement(sql, trxName);
            pstmt.setInt(1, AD_Column_ID);
            rs = pstmt.executeQuery();
            if (rs.next()) retValue = new M_Element(ctx, rs, trxName);
        } catch (Exception e) {
            s_log.log(Level.SEVERE, sql, e);
        } finally {
            DB.close(rs, pstmt);
            rs = null;
            pstmt = null;
        }
        return retValue;
    }

    /**
	 * 	Get Element
	 * 	@param ctx context
	 *	@param columnName case insentitive column name
	 *	@return case sensitive column name
	 */
    public static M_Element getOfColumn(Properties ctx, int AD_Column_ID) {
        return getOfColumn(ctx, AD_Column_ID, null);
    }

    /**	Logger	*/
    private static CLogger s_log = CLogger.getCLogger(M_Element.class);

    /**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Element_ID element
	 *	@param trxName transaction
	 */
    public M_Element(Properties ctx, int AD_Element_ID, String trxName) {
        super(ctx, AD_Element_ID, trxName);
        if (AD_Element_ID == 0) {
        }
    }

    /**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
    public M_Element(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
	 * 	Minimum Constructor
	 *	@param ctx context
	 *	@param columnName column
	 *	@param EntityType entity type
	 *	@param trxName trx
	 */
    public M_Element(Properties ctx, String columnName, String EntityType, String trxName) {
        super(ctx, 0, trxName);
        setColumnName(columnName);
        setName(columnName);
        setPrintName(columnName);
        setEntityType(EntityType);
    }

    @Override
    protected boolean beforeSave(boolean newRecord) {
        if (newRecord || is_ValueChanged(COLUMNNAME_ColumnName)) {
            String columnName = getColumnName().trim();
            if (getColumnName().length() != columnName.length()) setColumnName(columnName);
            String sql = "select count(*) from AD_Element where UPPER(ColumnName)=?";
            if (!newRecord) sql += " AND AD_Element_ID<>" + get_ID();
            int no = DB.getSQLValue(null, sql, columnName.toUpperCase());
            if (no > 0) {
                log.saveError("SaveErrorNotUnique", Msg.getElement(getCtx(), COLUMNNAME_ColumnName));
                return false;
            }
        }
        return true;
    }

    /**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
    protected boolean afterSave(boolean newRecord, boolean success) {
        if (!newRecord) {
            StringBuffer sql = new StringBuffer("UPDATE AD_Column SET ColumnName=").append(DB.TO_STRING(getColumnName())).append(", Name=").append(DB.TO_STRING(getName())).append(", Description=").append(DB.TO_STRING(getDescription())).append(", Help=").append(DB.TO_STRING(getHelp())).append(" WHERE AD_Element_ID=").append(get_ID());
            int no = DB.executeUpdate(sql.toString(), get_TrxName());
            log.fine("afterSave - Columns updated #" + no);
            sql = new StringBuffer("UPDATE AD_Field SET Name=").append(DB.TO_STRING(getName())).append(", Description=").append(DB.TO_STRING(getDescription())).append(", Help=").append(DB.TO_STRING(getHelp())).append(" WHERE AD_Column_ID IN (SELECT AD_Column_ID FROM AD_Column WHERE AD_Element_ID=").append(get_ID()).append(") AND IsCentrallyMaintained='Y'");
            no = DB.executeUpdate(sql.toString(), get_TrxName());
            log.fine("Fields updated #" + no);
            sql = new StringBuffer("UPDATE AD_Process_Para SET ColumnName=").append(DB.TO_STRING(getColumnName())).append(", Name=").append(DB.TO_STRING(getName())).append(", Description=").append(DB.TO_STRING(getDescription())).append(", Help=").append(DB.TO_STRING(getHelp())).append(", AD_Element_ID=").append(get_ID()).append(" WHERE UPPER(ColumnName)=").append(DB.TO_STRING(getColumnName().toUpperCase())).append(" AND IsCentrallyMaintained='Y' AND AD_Element_ID IS NULL");
            no = DB.executeUpdate(sql.toString(), get_TrxName());
            sql = new StringBuffer("UPDATE AD_Process_Para SET ColumnName=").append(DB.TO_STRING(getColumnName())).append(", Name=").append(DB.TO_STRING(getName())).append(", Description=").append(DB.TO_STRING(getDescription())).append(", Help=").append(DB.TO_STRING(getHelp())).append(" WHERE AD_Element_ID=").append(get_ID()).append(" AND IsCentrallyMaintained='Y'");
            no += DB.executeUpdate(sql.toString(), get_TrxName());
            log.fine("Parameters updated #" + no);
            sql = new StringBuffer("UPDATE AD_PrintFormatItem pi SET PrintName=").append(DB.TO_STRING(getPrintName())).append(", Name=").append(DB.TO_STRING(getName())).append(" WHERE IsCentrallyMaintained='Y'").append(" AND EXISTS (SELECT * FROM AD_Column c ").append("WHERE c.AD_Column_ID=pi.AD_Column_ID AND c.AD_Element_ID=").append(get_ID()).append(")");
            no = DB.executeUpdate(sql.toString(), get_TrxName());
            log.fine("PrintFormatItem updated #" + no);
            no = DB.executeUpdate(sql.toString(), get_TrxName());
            log.fine("InfoWindow updated #" + no);
        }
        return success;
    }

    /**
	 * 	String Representation
	 *	@return info
	 */
    public String toString() {
        StringBuffer sb = new StringBuffer("M_Element[");
        sb.append(get_ID()).append("-").append(getColumnName()).append("]");
        return sb.toString();
    }
}
