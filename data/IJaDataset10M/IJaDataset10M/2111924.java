package org.epoline.jdms.program.jdbc;

import java.sql.*;
import org.epoline.jsf.jdms.plugin.JDmsCommarea;
import org.epoline.jsf.jdms.dl.JDmsRequest;
import org.epoline.jsf.jdms.TSProgram;
import org.epoline.jsf.jdms.TSProgManager;

public class PHC019 implements TSProgram {

    private static String XX0 = "  ";

    private static String XX1 = "   ";

    private static final String ADJUST = "000000";

    static class PRDossier {

        static int LEN = 87;

        private static long lasttime = 0L;

        private static Object lock = new Object();

        public String DOSKEY;

        public String DosCoverFlags;

        public String DosOriType;

        public String DosOriNumber;

        public String DOSMOD;

        public PRDossier() {
        }

        public PRDossier(ResultSet rs) throws SQLException {
            DOSKEY = rs.getString(1);
            if (DOSKEY.length() > 15) {
                DOSKEY = DOSKEY.substring(0, 15);
            }
            DosCoverFlags = rs.getString(2);
            if (DosCoverFlags.length() > 32) {
                DosCoverFlags = DosCoverFlags.substring(0, 32);
            }
            DosOriType = rs.getString(3);
            if (DosOriType.length() > 2) {
                DosOriType = DosOriType.substring(0, 2);
            }
            DosOriNumber = rs.getString(4);
            if (DosOriNumber.length() > 12) {
                DosOriNumber = DosOriNumber.substring(0, 12);
            }
            DOSMOD = (rs.getString(5) + ADJUST).substring(0, 26);
        }

        public PRDossier(String buf) {
            DOSKEY = buf.substring(0, 15);
            DosCoverFlags = buf.substring(15, 47);
            DosOriType = buf.substring(47, 49);
            DosOriNumber = buf.substring(49, 61);
            DOSMOD = buf.substring(61, 87);
        }

        public void newId(String userid) {
            long time = System.currentTimeMillis();
            synchronized (lock) {
                while (time == lasttime) {
                    time = System.currentTimeMillis();
                }
                lasttime = time;
            }
            DOSKEY = (Long.toString(time, 36).toUpperCase() + userid + "       ").substring(0, 15);
        }

        public StringBuffer toBuffer() {
            StringBuffer buf = new StringBuffer();
            buf.append(DOSKEY);
            buf.append(DosCoverFlags);
            buf.append(DosOriType);
            buf.append(DosOriNumber);
            buf.append(DOSMOD);
            return buf;
        }

        public StringBuffer asBuffer() {
            StringBuffer buf = new StringBuffer();
            buf.append(REC_SEP);
            buf.append("1419");
            buf.append(FIELD_SEP);
            buf.append(DOSKEY);
            buf.append(FIELD_SEP);
            buf.append(DosCoverFlags);
            buf.append(FIELD_SEP);
            buf.append(DosOriType);
            buf.append(FIELD_SEP);
            buf.append(DosOriNumber);
            buf.append(FIELD_SEP);
            buf.append(DOSMOD);
            buf.append(FIELD_SEP);
            return buf;
        }
    }

    /**
 * These strings contain the SQL code for adding an Object.
*/
    private static String ADD = null;

    private static String TABADD = "PHC019ADD";

    /**
 * ADD : adds an object to the database.
 * This method generates an internal key and timestamp.
*/
    private JDmsRequest add(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRDossier obj = null;
        JDmsRequest rc = req;
        obj = new PRDossier(new String(comm.getRecordLayout()));
        obj.newId(req.userid);
        if (ADD == null) {
            ADD = "INSERT INTO " + TSProgManager.SCHEMA + ".TPH019_DOSSIER ( DOSKEY , DOSXX0 , DOSXX1 , DOSCOVERFLAGS , DOSORITYPE , DOSORINUMBER , DOSMOD )" + " VALUES ( ?, ?, ?, ?, ?, ?, " + TSProgManager.TIMESTAMP + " ) ";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABADD, ADD);
            stm.setString(1, obj.DOSKEY);
            stm.setString(2, XX0);
            stm.setString(3, XX1);
            stm.setString(4, obj.DosCoverFlags);
            stm.setString(5, obj.DosOriType);
            stm.setString(6, obj.DosOriNumber);
            int count = stm.executeUpdate();
            stm.close();
            stm = TSProgManager.getStatement(con, TABGET, GET);
            stm.setString(1, obj.DOSKEY);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                PRDossier rsobj = new PRDossier(rs);
                comm.setRecordLayout(rsobj.toBuffer());
                comm.setReturnCode(0);
                comm.setReturnCount(count);
                rc.SQLCode = 0;
            } else {
                comm.setReturnCode(100);
                comm.setReturnMessage(obj.DOSKEY + " not found.");
            }
            rs.close();
        } catch (SQLException e) {
            rc.SQLCode = e.getErrorCode();
            rc.SQLMessage = e.getSQLState();
            comm.setReturnCode(rc.SQLCode);
            comm.setReturnMessage(e.getMessage());
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException esql) {
                }
            }
        }
        rc.sCommarea = comm.toString();
        return rc;
    }

    /**
 * These strings contain the SQL code for loading an Object.
*/
    private static String LOAD = null;

    private static String TABLOAD = "PHC019LOAD";

    /**
 * LOAD : adds an object to the database.
 * This method does not generates an internal key and timestamp.
*/
    private JDmsRequest load(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRDossier obj = null;
        JDmsRequest rc = req;
        obj = new PRDossier(new String(comm.getRecordLayout()));
        if (LOAD == null) {
            LOAD = "INSERT INTO " + TSProgManager.SCHEMA + ".TPH019_DOSSIER ( DOSKEY , DOSXX0 , DOSXX1 , DOSCOVERFLAGS , DOSORITYPE , DOSORINUMBER , DOSMOD )" + " VALUES ( ?, ?, ?, ?, ?, ?, ? ) ";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABLOAD, LOAD);
            stm.setString(1, obj.DOSKEY);
            stm.setString(2, XX0);
            stm.setString(3, XX1);
            stm.setString(4, obj.DosCoverFlags);
            stm.setString(5, obj.DosOriType);
            stm.setString(6, obj.DosOriNumber);
            stm.setTimestamp(7, java.sql.Timestamp.valueOf(obj.DOSMOD));
            int count = stm.executeUpdate();
            comm.setRecordLayout(obj.toBuffer());
            comm.setReturnCode(0);
            comm.setReturnCount(count);
            rc.SQLCode = 0;
        } catch (SQLException e) {
            rc.SQLCode = e.getErrorCode();
            rc.SQLMessage = e.getSQLState();
            comm.setReturnCode(rc.SQLCode);
            comm.setReturnMessage(e.getMessage());
        } catch (IllegalArgumentException e) {
            rc.SQLCode = -180;
            rc.SQLMessage = e.getMessage();
            comm.setReturnCode(rc.SQLCode);
            comm.setReturnMessage(e.getMessage());
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException esql) {
                }
            }
        }
        rc.sCommarea = comm.toString();
        return rc;
    }

    /**
 * These strings contain the SQL code for updating.
*/
    private static String UPD = null;

    private static String TABUPD = "PHC019UPD";

    /**
 * UPD : Updates an object in the database.
 * This method checks the timestamp.
*/
    private JDmsRequest upd(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRDossier obj = null;
        JDmsRequest rc = req;
        obj = new PRDossier(new String(comm.getRecordLayout()));
        if (UPD == null) {
            UPD = "UPDATE " + TSProgManager.SCHEMA + ".TPH019_DOSSIER SET DOSCOVERFLAGS = ?  , DOSORITYPE = ?  , DOSORINUMBER = ?  , DOSMOD = " + TSProgManager.TIMESTAMP + " WHERE DOSKEY = ? AND DOSMOD = ? ";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABUPD, UPD);
            stm.setString(4, obj.DOSKEY);
            stm.setTimestamp(5, java.sql.Timestamp.valueOf(obj.DOSMOD));
            stm.setString(1, obj.DosCoverFlags);
            stm.setString(2, obj.DosOriType);
            stm.setString(3, obj.DosOriNumber);
            int count = stm.executeUpdate();
            stm.close();
            stm = TSProgManager.getStatement(con, TABGET, GET);
            stm.setString(1, obj.DOSKEY);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                PRDossier rsobj = new PRDossier(rs);
                comm.setRecordLayout(rsobj.toBuffer());
                comm.setReturnCode(0);
                comm.setReturnCount(count);
                rc.SQLCode = 0;
            }
            rs.close();
        } catch (SQLException e) {
            rc.SQLCode = e.getErrorCode();
            rc.SQLMessage = e.getSQLState();
            comm.setReturnCode(rc.SQLCode);
            comm.setReturnMessage(e.getMessage());
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException esql) {
                }
            }
        }
        rc.sCommarea = comm.toString();
        return rc;
    }

    /**
 * These strings contain the SQL code for deleting an Object.
*/
    private static String DEL = null;

    private static String TABDEL = "PHC019DEL";

    /**
 * DEL : deletes an object to the database.
*/
    private JDmsRequest del(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRDossier obj = null;
        JDmsRequest rc = req;
        obj = new PRDossier(new String(comm.getRecordLayout()));
        if (DEL == null) {
            DEL = "DELETE FROM " + TSProgManager.SCHEMA + ".TPH019_DOSSIER WHERE DOSKEY = ? AND DOSMOD = ? ";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABDEL, DEL);
            stm.setString(1, obj.DOSKEY);
            stm.setTimestamp(2, java.sql.Timestamp.valueOf(obj.DOSMOD));
            int count = stm.executeUpdate();
            if (count == 1) {
                comm.setReturnCode(0);
                comm.setReturnCount(1);
                rc.SQLCode = 0;
            } else {
                rc.SQLCode = 100;
                rc.SQLMessage = "Unable to delete object";
                comm.setReturnCode(rc.SQLCode);
                comm.setReturnMessage(rc.SQLMessage);
                comm.setReturnCount(0);
            }
        } catch (SQLException e) {
            rc.SQLCode = e.getErrorCode();
            rc.SQLMessage = e.getSQLState();
            comm.setReturnCode(rc.SQLCode);
            comm.setReturnMessage(e.getMessage());
        } catch (IllegalArgumentException e) {
            rc.SQLCode = -180;
            rc.SQLMessage = e.getMessage();
            comm.setReturnCode(rc.SQLCode);
            comm.setReturnMessage(e.getMessage());
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException esql) {
                }
            }
        }
        rc.sCommarea = comm.toString();
        return rc;
    }

    /**
 * These strings contain the SQL code for retrieval by key.
*/
    private static String GET = "SELECT DOSKEY , DOSCOVERFLAGS , DOSORITYPE , DOSORINUMBER , DOSMOD" + " FROM " + TSProgManager.SCHEMA + ".TPH019_DOSSIER WHERE DOSKEY = ? ";

    private static String TABGET = "PHC019GET";

    /**
 * GET : get an object using the primary key.
 * Retrieve this object from the database.
*/
    private JDmsRequest get(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRDossier obj = null;
        JDmsRequest rc = req;
        obj = new PRDossier(new String(comm.getRecordLayout()));
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABGET, GET);
            stm.setString(1, obj.DOSKEY);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                PRDossier rsobj = new PRDossier(rs);
                comm.setRecordLayout(rsobj.toBuffer());
                comm.setReturnCode(0);
                comm.setReturnCount(1);
                rc.SQLCode = 0;
            } else {
                comm.setReturnCode(100);
                comm.setReturnMessage(obj.DOSKEY + " not found.");
            }
            rs.close();
        } catch (SQLException e) {
            rc.SQLCode = e.getErrorCode();
            rc.SQLMessage = e.getSQLState();
            comm.setReturnCode(rc.SQLCode);
            comm.setReturnMessage(e.getMessage());
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException esql) {
                }
            }
        }
        rc.sCommarea = comm.toString();
        return rc;
    }

    /**
 * These strings contain the SQL code for bulk retrieval.
*/
    private static String RETA = null;

    private static String TABRETA = "PHC019RETA";

    /**
 * RETA : get all objects from a table.
*/
    private JDmsRequest reta(Connection con, JDmsRequest req, JDmsCommarea comm) {
        JDmsRequest rc = req;
        if (RETA == null) {
            RETA = "SELECT DOSKEY , DOSCOVERFLAGS , DOSORITYPE , DOSORINUMBER , DOSMOD" + " FROM " + TSProgManager.SCHEMA + ".TPH019_DOSSIER ORDER BY DOSKEY ";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABRETA, RETA);
            ResultSet rs = stm.executeQuery();
            StringBuffer buf = new StringBuffer();
            int count = 0;
            while (rs.next()) {
                PRDossier rsobj = new PRDossier(rs);
                buf.append(rsobj.toBuffer());
                count++;
            }
            rs.close();
            comm.setRecordLayout(buf);
            comm.setReturnCode(100);
            comm.setReturnCount(count);
            rc.SQLCode = 100;
        } catch (SQLException e) {
            rc.SQLCode = e.getErrorCode();
            rc.SQLMessage = e.getSQLState();
            comm.setReturnCode(rc.SQLCode);
            comm.setReturnMessage(e.getMessage());
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException esql) {
                }
            }
        }
        rc.sCommarea = comm.toString();
        return rc;
    }

    /**
 * These strings contain the SQL code for bulk retrieval.
*/
    private static String MOD = null;

    private static String TABMOD = "PHC019MOD";

    /**
 * MOD : get all objects from a table.
*/
    private JDmsRequest mod(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRDossier obj = null;
        JDmsRequest rc = req;
        obj = new PRDossier(new String(comm.getRecordLayout()));
        if (MOD == null) {
            MOD = "SELECT DOSKEY , DOSCOVERFLAGS , DOSORITYPE , DOSORINUMBER , DOSMOD" + " FROM " + TSProgManager.SCHEMA + ".TPH019_DOSSIER WHERE DOSMOD >= ? ORDER BY DOSMOD ";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABMOD, MOD);
            if (obj.DOSMOD.startsWith(" ")) {
                stm.setNull(1, Types.TIMESTAMP);
            } else {
                stm.setTimestamp(1, Timestamp.valueOf(obj.DOSMOD));
            }
            ResultSet rs = stm.executeQuery();
            StringBuffer buf = new StringBuffer();
            int count = 0;
            while (rs.next()) {
                PRDossier rsobj = new PRDossier(rs);
                buf.append(rsobj.toBuffer());
                count++;
            }
            rs.close();
            comm.setRecordLayout(buf);
            comm.setReturnCode(100);
            comm.setReturnCount(count);
            rc.SQLCode = 100;
        } catch (SQLException e) {
            rc.SQLCode = e.getErrorCode();
            rc.SQLMessage = e.getSQLState();
            comm.setReturnCode(rc.SQLCode);
            comm.setReturnMessage(e.getMessage());
        } catch (IllegalArgumentException e) {
            rc.SQLCode = -180;
            rc.SQLMessage = e.getMessage();
            comm.setReturnCode(rc.SQLCode);
            comm.setReturnMessage(e.getMessage());
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException esql) {
                }
            }
        }
        rc.sCommarea = comm.toString();
        return rc;
    }

    /**
 * SI1 will contain the SQL code for retrieval dos_Number
*/
    private static String SI1 = null;

    private static String TABSI1 = "PHC019SI1";

    /**
 * si1: get an object using dos_Number
 * tries to retrieve this object from the database.
*/
    private JDmsRequest si1(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRDossier obj = null;
        JDmsRequest rc = req;
        obj = new PRDossier(new String(comm.getRecordLayout()));
        if (SI1 == null) {
            SI1 = "SELECT DOSKEY , DOSCOVERFLAGS , DOSORITYPE , DOSORINUMBER , DOSMOD" + " FROM " + TSProgManager.SCHEMA + ".TPH019_DOSSIER WHERE DOSORITYPE = ? AND DOSORINUMBER = ? ";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABSI1, SI1);
            stm.setString(1, obj.DosOriType);
            stm.setString(2, obj.DosOriNumber);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                PRDossier rsobj = new PRDossier(rs);
                comm.setRecordLayout(rsobj.toBuffer());
                comm.setReturnCode(0);
                comm.setReturnCount(1);
                rc.SQLCode = 0;
            } else {
                comm.setReturnCode(100);
                comm.setReturnMessage(obj.DOSKEY + " not found.");
            }
            rs.close();
        } catch (SQLException e) {
            rc.SQLCode = e.getErrorCode();
            rc.SQLMessage = e.getSQLState();
            comm.setReturnCode(rc.SQLCode);
            comm.setReturnMessage(e.getMessage());
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException esql) {
                }
            }
        }
        rc.sCommarea = comm.toString();
        return rc;
    }

    /**
 * SI1A will contain the SQL code for retrieval dos_Number
*/
    private static String SI1A = null;

    private static String TABSI1A = "PHC019SI1A";

    /**
 * si1a: get objects using dos_Number
 * tries to retrieve all object from the database.
*/
    private JDmsRequest si1a(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRDossier obj = null;
        JDmsRequest rc = req;
        obj = new PRDossier(new String(comm.getRecordLayout()));
        if (SI1A == null) {
            SI1A = "SELECT DOSKEY , DOSCOVERFLAGS , DOSORITYPE , DOSORINUMBER , DOSMOD" + " FROM " + TSProgManager.SCHEMA + ".TPH019_DOSSIER WHERE ( DOSORITYPE >= ?  ) AND NOT ( ( DOSORITYPE = ? ) AND  ( DOSORINUMBER <= ? ) ) ORDER BY DOSORITYPE , DOSORINUMBER ";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABSI1A, SI1A);
            stm.setString(1, obj.DosOriType);
            stm.setString(2, obj.DosOriType);
            stm.setString(3, obj.DosOriNumber);
            ResultSet rs = stm.executeQuery();
            StringBuffer buf = new StringBuffer();
            int count = 0;
            while (rs.next()) {
                PRDossier rsobj = new PRDossier(rs);
                buf.append(rsobj.toBuffer());
                count++;
            }
            rs.close();
            comm.setRecordLayout(buf);
            comm.setReturnCode(100);
            comm.setReturnCount(count);
            rc.SQLCode = 100;
        } catch (SQLException e) {
            rc.SQLCode = e.getErrorCode();
            rc.SQLMessage = e.getSQLState();
            comm.setReturnCode(rc.SQLCode);
            comm.setReturnMessage(e.getMessage());
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException esql) {
                }
            }
        }
        rc.sCommarea = comm.toString();
        return rc;
    }

    public JDmsRequest exec(Connection con, JDmsRequest req) {
        JDmsCommarea comm = new JDmsCommarea();
        comm.parseFrom(req);
        String act = comm.getActionId();
        if (act.equals("GET ")) {
            return get(con, req, comm);
        } else if (act.equals("ADD ")) {
            return add(con, req, comm);
        } else if (act.equals("LOAD")) {
            return load(con, req, comm);
        } else if (act.equals("DEL ")) {
            return del(con, req, comm);
        } else if (act.equals("UPD ")) {
            return upd(con, req, comm);
        } else if (act.equals("SI1 ")) {
            return si1(con, req, comm);
        } else if (act.equals("SI1A")) {
            return si1a(con, req, comm);
        } else if (act.equals("RETA")) {
            return reta(con, req, comm);
        } else if (act.equals("MOD ")) {
            return mod(con, req, comm);
        }
        JDmsRequest rc = req;
        rc.TSCode = -99;
        rc.TSMessage = "Illegal request";
        return rc;
    }
}
