package org.openXpertya.report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;
import org.openXpertya.model.X_PA_ReportLineSet;
import org.openXpertya.util.DB;

/**
 * Descripción de Clase
 *
 *
 * @version    2.0, 22.03.06
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class MReportLineSet extends X_PA_ReportLineSet {

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param PA_ReportLineSet_ID
     * @param trxName
     */
    public MReportLineSet(Properties ctx, int PA_ReportLineSet_ID, String trxName) {
        super(ctx, PA_ReportLineSet_ID, trxName);
        if (PA_ReportLineSet_ID == 0) {
        } else {
            loadLines();
        }
    }

    /** Descripción de Campos */
    private CReportLine[] m_lines = null;

    /**
     * Descripción de Método
     *
     */
    private void loadLines() {
        ArrayList list = new ArrayList();
        String sql = "SELECT * FROM PA_ReportLine " + "WHERE PA_ReportLineSet_ID=? AND IsActive='Y' " + "ORDER BY SeqNo";
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql);
            pstmt.setInt(1, getPA_ReportLineSet_ID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new CReportLine(getCtx(), rs, null));
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.severe("MReportLineSet.loadLines: " + e);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
            pstmt = null;
        }
        m_lines = new CReportLine[list.size()];
        list.toArray(m_lines);
        log.info("ID=" + getPA_ReportLineSet_ID() + " - Size=" + list.size());
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public CReportLine[] getLiness() {
        return m_lines;
    }

    /**
     * Descripción de Método
     *
     */
    public void list() {
        System.out.println(toString());
        if (m_lines == null) {
            return;
        }
        for (int i = 0; i < m_lines.length; i++) {
            m_lines[i].list();
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("MReportLineSet[").append(getID()).append(" - ").append(getName()).append("]");
        return sb.toString();
    }
}
