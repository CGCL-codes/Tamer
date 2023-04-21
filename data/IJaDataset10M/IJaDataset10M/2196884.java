package org.openXpertya.model;

import org.openXpertya.util.CLogger;
import org.openXpertya.util.DB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

/**
 *      Currency Account Model
 *
 *  @author Comunidad de Desarrollo openXpertya
 *         *Basado en Codigo Original Modificado, Revisado y Optimizado de:
 *         * Jorg Janke
 *  @version $Id: MCurrencyAcct.java,v 1.5 2005/03/11 20:28:38 jjanke Exp $
 */
public class MCurrencyAcct extends X_C_Currency_Acct {

    /** Static Logger */
    private static CLogger s_log = CLogger.getCLogger(MCurrencyAcct.class);

    /**
     *      Load Constructor
     *      @param ctx context
     *      @param rs result set
     * @param trxName
     */
    public MCurrencyAcct(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
     *      Get Currency Account for Currency
     *      @param as accounting schema default
     *      @param C_Currency_ID currency
     *      @return Currency Account or null
     */
    public static MCurrencyAcct get(MAcctSchemaDefault as, int C_Currency_ID) {
        MCurrencyAcct retValue = null;
        String sql = "SELECT * FROM C_Currency_Acct " + "WHERE C_AcctSchema_ID=? AND C_Currency_ID=?";
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql);
            pstmt.setInt(1, as.getC_AcctSchema_ID());
            pstmt.setInt(2, C_Currency_ID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                retValue = new MCurrencyAcct(as.getCtx(), rs, null);
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            s_log.log(Level.SEVERE, "get", e);
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            pstmt = null;
        } catch (Exception e) {
            pstmt = null;
        }
        return retValue;
    }
}
