package org.openXpertya.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;
import org.openXpertya.model.MAllocationHdr;
import org.openXpertya.util.DB;
import org.openXpertya.util.Env;
import org.openXpertya.util.Trx;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class AllocationReset extends SvrProcess {

    /** Descripción de Campos */
    private int p_C_BP_Group_ID = 0;

    /** Descripción de Campos */
    private int p_C_BPartner_ID = 0;

    /** Descripción de Campos */
    private Timestamp p_DateAcct_From = null;

    /** Descripción de Campos */
    private Timestamp p_DateAcct_To = null;

    /** Descripción de Campos */
    private int p_C_AllocationHdr_ID = 0;

    /** Descripción de Campos */
    private Trx m_trx = null;

    /**
     * Descripción de Método
     *
     */
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            log.fine("prepare - " + para[i]);
            String name = para[i].getParameterName();
            if (para[i].getParameter() == null) {
                ;
            } else if (name.equals("C_BP_Group_ID")) {
                p_C_BP_Group_ID = para[i].getParameterAsInt();
            } else if (name.equals("C_BPartner_ID")) {
                p_C_BPartner_ID = para[i].getParameterAsInt();
            } else if (name.equals("C_AllocationHdr_ID")) {
                p_C_AllocationHdr_ID = para[i].getParameterAsInt();
            } else if (name.equals("DateAcct")) {
                p_DateAcct_From = (Timestamp) para[i].getParameter();
                p_DateAcct_To = (Timestamp) para[i].getParameter_To();
            } else {
                log.log(Level.SEVERE, "Unknown Parameter: " + name);
            }
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     *
     * @throws Exception
     */
    protected String doIt() throws Exception {
        log.info("C_BP_Group_ID=" + p_C_BP_Group_ID + ", C_BPartner_ID=" + p_C_BPartner_ID + ", DateAcct= " + p_DateAcct_From + " - " + p_DateAcct_To + ", C_AllocationHdr_ID=" + p_C_AllocationHdr_ID);
        m_trx = Trx.get(Trx.createTrxName("AllocReset"), true);
        int count = 0;
        if (p_C_AllocationHdr_ID != 0) {
            MAllocationHdr hdr = new MAllocationHdr(getCtx(), p_C_AllocationHdr_ID, m_trx.getTrxName());
            if (delete(hdr)) {
                count++;
            }
            m_trx.close();
            return "@Deleted@ #" + count;
        }
        StringBuffer sql = new StringBuffer("SELECT * FROM C_AllocationHdr ah " + "WHERE EXISTS (SELECT * FROM C_AllocationLine al " + "WHERE ah.C_AllocationHdr_ID=al.C_AllocationHdr_ID");
        if (p_C_BPartner_ID != 0) {
            sql.append(" AND al.C_BPartner_ID=?");
        } else if (p_C_BP_Group_ID != 0) {
            sql.append(" AND EXISTS (SELECT * FROM C_BPartner bp " + "WHERE bp.C_BPartner_ID=al.C_BPartner_ID AND bp.C_BP_Group_ID=?)");
        } else {
            sql.append(" AND AD_Client_ID=?");
        }
        if (p_DateAcct_From != null) {
            sql.append(" AND TRIM(ah.DateAcct) >= ?");
        }
        if (p_DateAcct_To != null) {
            sql.append(" AND TRIM(ah.DateAcct) <= ?");
        }
        sql.append(" AND al.C_CashLine_ID IS NULL)");
        sql.append(" AND EXISTS (SELECT * FROM C_Period p" + " INNER JOIN C_PeriodControl pc ON (p.C_Period_ID=pc.C_Period_ID AND pc.DocBaseType='CMA') " + "WHERE ah.DateAcct BETWEEN p.StartDate AND p.EndDate)");
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql.toString(), m_trx.getTrxName());
            int index = 1;
            if (p_C_BPartner_ID != 0) {
                pstmt.setInt(index++, p_C_BPartner_ID);
            } else if (p_C_BP_Group_ID != 0) {
                pstmt.setInt(index++, p_C_BP_Group_ID);
            } else {
                pstmt.setInt(index++, Env.getAD_Client_ID(getCtx()));
            }
            if (p_DateAcct_From != null) {
                pstmt.setTimestamp(index++, p_DateAcct_From);
            }
            if (p_DateAcct_To != null) {
                pstmt.setTimestamp(index++, p_DateAcct_To);
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                MAllocationHdr hdr = new MAllocationHdr(getCtx(), rs, m_trx.getTrxName());
                if (delete(hdr)) {
                    count++;
                }
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, sql.toString(), e);
            m_trx.rollback();
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            pstmt = null;
        } catch (Exception e) {
            pstmt = null;
        }
        m_trx.close();
        return "@Deleted@ #" + count;
    }

    /**
     * Descripción de Método
     *
     *
     * @param hdr
     *
     * @return
     */
    private boolean delete(MAllocationHdr hdr) {
        boolean success = false;
        if (hdr.delete(true, m_trx.getTrxName())) {
            log.fine(hdr.toString());
            success = true;
        }
        if (success) {
            m_trx.commit();
        } else {
            m_trx.rollback();
        }
        return success;
    }

    /**
     * Descripción de Método
     *
     */
    private void setBPartner() {
    }
}
