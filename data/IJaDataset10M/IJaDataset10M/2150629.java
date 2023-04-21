package org.compiere.report;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Report Line Set Model
 *
 *  @author Jorg Janke
 *  @version $Id: MReportLineSet.java,v 1.3 2006/08/03 22:16:52 jjanke Exp $
 */
public class MReportLineSet extends X_PA_ReportLineSet {

    /**
	 * 	Constructor
	 * 	@param ctx context
	 * 	@param PA_ReportLineSet_ID id
	 * 	@param trxName transaction
	 */
    public MReportLineSet(Properties ctx, int PA_ReportLineSet_ID, String trxName) {
        super(ctx, PA_ReportLineSet_ID, trxName);
        if (PA_ReportLineSet_ID == 0) {
        } else loadLines();
    }

    /**	Contained Lines			*/
    private MReportLine[] m_lines = null;

    /**
	 * 	Load Lines
	 */
    private void loadLines() {
        ArrayList<MReportLine> list = new ArrayList<MReportLine>();
        String sql = "SELECT * FROM PA_ReportLine " + "WHERE PA_ReportLineSet_ID=? AND IsActive='Y' " + "ORDER BY SeqNo";
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql, get_TrxName());
            pstmt.setInt(1, getPA_ReportLineSet_ID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) list.add(new MReportLine(getCtx(), rs, get_TrxName()));
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, sql, e);
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (Exception e) {
            }
            pstmt = null;
        }
        m_lines = new MReportLine[list.size()];
        list.toArray(m_lines);
        log.finest("ID=" + getPA_ReportLineSet_ID() + " - Size=" + list.size());
    }

    /**
	 * 	Get Liness
	 *	@return array of lines
	 */
    public MReportLine[] getLiness() {
        return m_lines;
    }

    /**
	 * 	List Info
	 */
    public void list() {
        System.out.println(toString());
        if (m_lines == null) return;
        for (int i = 0; i < m_lines.length; i++) m_lines[i].list();
    }

    /**
	 * 	String representation
	 *	@return info
	 */
    public String toString() {
        StringBuffer sb = new StringBuffer("MReportLineSet[").append(get_ID()).append(" - ").append(getName()).append("]");
        return sb.toString();
    }
}
