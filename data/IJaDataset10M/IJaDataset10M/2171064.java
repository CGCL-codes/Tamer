package org.openXpertya.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import org.openXpertya.util.DB;
import org.openXpertya.util.Env;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class MInvoiceBatch extends X_C_InvoiceBatch {

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param C_InvoiceBatch_ID
     * @param trxName
     */
    public MInvoiceBatch(Properties ctx, int C_InvoiceBatch_ID, String trxName) {
        super(ctx, C_InvoiceBatch_ID, trxName);
        if (C_InvoiceBatch_ID == 0) {
            setControlAmt(Env.ZERO);
            setDateDoc(new Timestamp(System.currentTimeMillis()));
            setDocumentAmt(Env.ZERO);
            setIsSOTrx(false);
            setProcessed(false);
        }
    }

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param rs
     * @param trxName
     */
    public MInvoiceBatch(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /** Descripción de Campos */
    private MInvoiceBatchLine[] m_lines = null;

    /**
     * Descripción de Método
     *
     *
     * @param reload
     *
     * @return
     */
    public MInvoiceBatchLine[] getLines(boolean reload) {
        if ((m_lines != null) && !reload) {
            return m_lines;
        }
        String sql = "SELECT * FROM C_InvoiceBatchLine WHERE C_InvoiceBatch_ID=? ORDER BY Line";
        ArrayList list = new ArrayList();
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql, get_TrxName());
            pstmt.setInt(1, getC_InvoiceBatch_ID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new MInvoiceBatchLine(getCtx(), rs, get_TrxName()));
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, sql, e);
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            pstmt = null;
        } catch (Exception e) {
            pstmt = null;
        }
        m_lines = new MInvoiceBatchLine[list.size()];
        list.toArray(m_lines);
        return m_lines;
    }

    /**
     * Descripción de Método
     *
     *
     * @param processed
     */
    public void setProcessed(boolean processed) {
        super.setProcessed(processed);
        if (getID() == 0) {
            return;
        }
        String set = "SET Processed='" + (processed ? "Y" : "N") + "' WHERE C_InvoiceBatch_ID=" + getC_InvoiceBatch_ID();
        int noLine = DB.executeUpdate("UPDATE C_InvoiceBatchLine " + set, get_TrxName());
        m_lines = null;
        log.fine(processed + " - Lines=" + noLine);
    }
}
