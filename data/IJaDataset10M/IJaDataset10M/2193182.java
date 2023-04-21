package org.epoline.jdms.program.jdbc;

import java.sql.*;
import org.epoline.jsf.jdms.TSProgManager;
import org.epoline.jsf.jdms.TSProgram;
import org.epoline.jsf.jdms.dl.JDmsRequest;

public class PHC402 implements TSProgram {

    /**
	 * These strings contain the SQL code for next-id.
	*/
    private static String PHS1 = "SELECT PCKPSTKEY, PCKMOD " + "FROM TPH035_PACKAGE " + "WHERE ( PCKKEY = ? ) ";

    private static String TABPHS1 = "402PHS1";

    private static String PHS2 = "SELECT PSTKEY " + "FROM TPH037_PCKSTAT " + "WHERE ( PSTINDSTATUS = ? ) ";

    private static String TABPHS2 = "402PHS2";

    private static String PHS3 = "INSERT INTO TPH036_PCKHIS " + "( PHSKEY , PHSXX0 , PHSXX1 , PHSPCKKEY , PHSTIME , PHSORISTATUS , PHSNEWSTATUS , PHSUSER , PHSMOD ) " + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";

    private static String TABPHS3 = "402PHS3";

    private static String PHS4 = "UPDATE TPH035_PACKAGE " + "SET    PCKPSTKEY = ?, PCKMOD = ? " + "WHERE  PCKKEY = ? ";

    private static String TABPHS4 = "402PHS4";

    public JDmsRequest exec(Connection con, JDmsRequest req) {
        return phs(con, req);
    }

    /**
	 * GET : get an object using the primary key.
	 * Retrieve this object from the database.
	*/
    private JDmsRequest phs(Connection con, JDmsRequest req) {
        JDmsRequest rc = req;
        String sin = req.sCommarea;
        if (sin == null) {
            sin = new String(req.bCommarea);
        }
        if (sin.length() < 60) {
            rc.TSCode = -99;
            rc.TSMessage = "Illegal length of commarea.";
            return rc;
        }
        PHC036.PRPckhis history = new PHC036.PRPckhis();
        String sinkey = sin.substring(0, 15);
        String sinpstkey = "               ";
        String sinmod = sin.substring(30, 56);
        String oldstat = sin.substring(56, 58);
        String newstat = sin.substring(58, 60);
        String user = sin.substring(60, 80);
        String dmscode = "00";
        String sqlcode = "+00000";
        if (oldstat.equals(newstat)) {
            rc.TSCode = -99;
            rc.TSMessage = "Nothing done. Old status = new status " + newstat;
            return rc;
        }
        PreparedStatement stm = null;
        StringBuffer buffer = new StringBuffer(sinkey);
        int count = 0;
        ResultSet rs = null;
        boolean doCommit = false;
        try {
            if (con.getAutoCommit()) {
                con.setAutoCommit(false);
                doCommit = true;
            }
            stm = TSProgManager.getStatement(con, TABPHS1, PHS1);
            stm.setString(1, sinkey);
            rs = stm.executeQuery();
            if (rs.next()) {
                count = 1;
                String mod = rs.getString(2);
                rs.close();
                rs = null;
                if (!mod.substring(0, 10).equals(sinmod.substring(0, 10))) {
                    count = 0;
                    dmscode = "02";
                } else {
                    stm.close();
                    stm = null;
                    stm = TSProgManager.getStatement(con, TABPHS2, PHS2);
                    stm.setString(1, oldstat);
                    rs = stm.executeQuery();
                    if (!rs.next()) {
                        count = 0;
                        dmscode = "03";
                        rs.close();
                        rs = null;
                    } else {
                        rs.close();
                        rs = null;
                        stm.close();
                        stm = null;
                        stm = TSProgManager.getStatement(con, TABPHS2, PHS2);
                        stm.setString(1, newstat);
                        rs = stm.executeQuery();
                        if (!rs.next()) {
                            count = 0;
                            dmscode = "04";
                        } else {
                            sinpstkey = rs.getString(1);
                        }
                        rs.close();
                        rs = null;
                        stm.close();
                        stm = null;
                    }
                }
            } else {
                rs.close();
                rs = null;
                count = 0;
                dmscode = "01";
                sqlcode = "+00100";
                rc.SQLCode = +100;
                rc.SQLMessage = "XSYSSEQ-REC with key " + sinkey + " not found.";
            }
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception e) {
                }
                stm = null;
            }
            Timestamp stamp = new Timestamp(new java.util.Date().getTime());
            if (count > 0) {
                history.newId(user);
                stm = TSProgManager.getStatement(con, TABPHS3, PHS3);
                stm.setString(1, history.PHSKEY);
                stm.setString(2, "  ");
                stm.setString(3, "   ");
                stm.setString(4, sinkey);
                stm.setTimestamp(5, stamp);
                stm.setString(6, oldstat);
                stm.setString(7, newstat);
                stm.setString(8, user);
                stm.setTimestamp(9, stamp);
                count = stm.executeUpdate();
                stm.close();
                stm = null;
                stm = TSProgManager.getStatement(con, TABPHS4, PHS4);
                stm.setString(1, sinpstkey);
                stm.setTimestamp(2, stamp);
                stm.setString(3, sinkey);
                count = stm.executeUpdate();
                stm.close();
                stm = null;
                if (doCommit) {
                    con.commit();
                }
            }
            buffer.append(sinpstkey);
            buffer.append((stamp.toString() + "000000").substring(0, 26));
            buffer.append(oldstat);
            buffer.append(newstat);
            buffer.append(user);
            buffer.append(dmscode);
            buffer.append(sqlcode);
        } catch (SQLException e) {
            if (rs != null) {
                try {
                    rs.close();
                    rs = null;
                } catch (SQLException e1) {
                }
            }
            if (stm != null) {
                try {
                    stm.close();
                    stm = null;
                } catch (SQLException e1) {
                }
            }
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            buffer.append(sinpstkey);
            buffer.append(sinmod);
            buffer.append(oldstat);
            buffer.append(newstat);
            buffer.append(user);
            buffer.append("05");
            buffer.append(e.getErrorCode());
            rc.SQLCode = e.getErrorCode();
            rc.SQLMessage = e.getSQLState();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    rs = null;
                } catch (SQLException e) {
                }
            }
            if (stm != null) {
                try {
                    stm.close();
                    stm = null;
                } catch (SQLException e) {
                }
            }
            if (doCommit) {
                try {
                    con.rollback();
                } catch (SQLException e) {
                }
            }
        }
        rc.bCommarea = buffer.toString().getBytes();
        rc.sCommarea = null;
        return rc;
    }
}
