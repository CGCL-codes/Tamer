package org.epoline.jdms.program.jdbc;

import java.sql.*;
import org.epoline.jsf.jdms.plugin.JDmsCommarea;
import org.epoline.jsf.jdms.dl.JDmsRequest;
import org.epoline.jsf.jdms.TSProgram;
import org.epoline.jsf.jdms.TSProgManager;

public class PHC061 implements TSProgram {

    private static String XX0 = "  ";

    private static String XX1 = "   ";

    private static final String ADJUST = "000000";

    static class PRDoskey {

        static int LEN = 83;

        private static long lasttime = 0L;

        private static Object lock = new Object();

        public String DKEKEY;

        public String DKEDTYKEY;

        public String DKEDOSKEY;

        public String DkeNumber;

        public String DKEMOD;

        public PRDoskey() {
        }

        public PRDoskey(ResultSet rs) throws SQLException {
            DKEKEY = rs.getString(1);
            if (DKEKEY.length() > 15) {
                DKEKEY = DKEKEY.substring(0, 15);
            }
            DKEDTYKEY = rs.getString(2);
            if (DKEDTYKEY.length() > 15) {
                DKEDTYKEY = DKEDTYKEY.substring(0, 15);
            }
            DKEDOSKEY = rs.getString(3);
            if (DKEDOSKEY.length() > 15) {
                DKEDOSKEY = DKEDOSKEY.substring(0, 15);
            }
            DkeNumber = rs.getString(4);
            if (DkeNumber.length() > 12) {
                DkeNumber = DkeNumber.substring(0, 12);
            }
            DKEMOD = (rs.getString(5) + ADJUST).substring(0, 26);
        }

        public PRDoskey(String buf) {
            DKEKEY = buf.substring(0, 15);
            DKEDTYKEY = buf.substring(15, 30);
            DKEDOSKEY = buf.substring(30, 45);
            DkeNumber = buf.substring(45, 57);
            DKEMOD = buf.substring(57, 83);
        }

        public void newId(String userid) {
            long time = System.currentTimeMillis();
            synchronized (lock) {
                while (time == lasttime) {
                    time = System.currentTimeMillis();
                }
                lasttime = time;
            }
            DKEKEY = (Long.toString(time, 36).toUpperCase() + userid + "       ").substring(0, 15);
        }

        public StringBuffer toBuffer() {
            StringBuffer buf = new StringBuffer();
            buf.append(DKEKEY);
            buf.append(DKEDTYKEY);
            buf.append(DKEDOSKEY);
            buf.append(DkeNumber);
            buf.append(DKEMOD);
            return buf;
        }

        public StringBuffer asBuffer() {
            StringBuffer buf = new StringBuffer();
            buf.append(REC_SEP);
            buf.append("1461");
            buf.append(FIELD_SEP);
            buf.append(DKEKEY);
            buf.append(FIELD_SEP);
            buf.append(DKEDTYKEY);
            buf.append(FIELD_SEP);
            buf.append(DKEDOSKEY);
            buf.append(FIELD_SEP);
            buf.append(DkeNumber);
            buf.append(FIELD_SEP);
            buf.append(DKEMOD);
            buf.append(FIELD_SEP);
            return buf;
        }
    }

    /**
 * These strings contain the SQL code for adding an Object.
*/
    private static String ADD = null;

    private static String TABADD = "PHC061ADD";

    /**
 * ADD : adds an object to the database.
 * This method generates an internal key and timestamp.
*/
    private JDmsRequest add(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRDoskey obj = null;
        JDmsRequest rc = req;
        obj = new PRDoskey(new String(comm.getRecordLayout()));
        obj.newId(req.userid);
        if (ADD == null) {
            ADD = "INSERT INTO " + TSProgManager.SCHEMA + ".TPH061_DOSKEY ( DKEKEY , DKEXX0 , DKEXX1 , DKEDTYKEY , DKEDOSKEY , DKENUMBER , DKEMOD )" + " VALUES ( ?, ?, ?, ?, ?, ?, " + TSProgManager.TIMESTAMP + " ) ";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABADD, ADD);
            stm.setString(1, obj.DKEKEY);
            stm.setString(2, XX0);
            stm.setString(3, XX1);
            stm.setString(4, obj.DKEDTYKEY);
            stm.setString(5, obj.DKEDOSKEY);
            stm.setString(6, obj.DkeNumber);
            int count = stm.executeUpdate();
            stm.close();
            stm = TSProgManager.getStatement(con, TABGET, GET);
            stm.setString(1, obj.DKEKEY);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                PRDoskey rsobj = new PRDoskey(rs);
                comm.setRecordLayout(rsobj.toBuffer());
                comm.setReturnCode(0);
                comm.setReturnCount(count);
                rc.SQLCode = 0;
            } else {
                comm.setReturnCode(100);
                comm.setReturnMessage(obj.DKEKEY + " not found.");
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

    private static String TABLOAD = "PHC061LOAD";

    /**
 * LOAD : adds an object to the database.
 * This method does not generates an internal key and timestamp.
*/
    private JDmsRequest load(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRDoskey obj = null;
        JDmsRequest rc = req;
        obj = new PRDoskey(new String(comm.getRecordLayout()));
        if (LOAD == null) {
            LOAD = "INSERT INTO " + TSProgManager.SCHEMA + ".TPH061_DOSKEY ( DKEKEY , DKEXX0 , DKEXX1 , DKEDTYKEY , DKEDOSKEY , DKENUMBER , DKEMOD )" + " VALUES ( ?, ?, ?, ?, ?, ?, ? ) ";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABLOAD, LOAD);
            stm.setString(1, obj.DKEKEY);
            stm.setString(2, XX0);
            stm.setString(3, XX1);
            stm.setString(4, obj.DKEDTYKEY);
            stm.setString(5, obj.DKEDOSKEY);
            stm.setString(6, obj.DkeNumber);
            stm.setTimestamp(7, java.sql.Timestamp.valueOf(obj.DKEMOD));
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

    private static String TABUPD = "PHC061UPD";

    /**
 * UPD : Updates an object in the database.
 * This method checks the timestamp.
*/
    private JDmsRequest upd(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRDoskey obj = null;
        JDmsRequest rc = req;
        obj = new PRDoskey(new String(comm.getRecordLayout()));
        if (UPD == null) {
            UPD = "UPDATE " + TSProgManager.SCHEMA + ".TPH061_DOSKEY SET DKEDTYKEY = ?  , DKEDOSKEY = ?  , DKENUMBER = ?  , DKEMOD = " + TSProgManager.TIMESTAMP + " WHERE DKEKEY = ? AND DKEMOD = ? ";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABUPD, UPD);
            stm.setString(4, obj.DKEKEY);
            stm.setTimestamp(5, java.sql.Timestamp.valueOf(obj.DKEMOD));
            stm.setString(1, obj.DKEDTYKEY);
            stm.setString(2, obj.DKEDOSKEY);
            stm.setString(3, obj.DkeNumber);
            int count = stm.executeUpdate();
            stm.close();
            stm = TSProgManager.getStatement(con, TABGET, GET);
            stm.setString(1, obj.DKEKEY);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                PRDoskey rsobj = new PRDoskey(rs);
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

    private static String TABDEL = "PHC061DEL";

    /**
 * DEL : deletes an object to the database.
*/
    private JDmsRequest del(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRDoskey obj = null;
        JDmsRequest rc = req;
        obj = new PRDoskey(new String(comm.getRecordLayout()));
        if (DEL == null) {
            DEL = "DELETE FROM " + TSProgManager.SCHEMA + ".TPH061_DOSKEY WHERE DKEKEY = ? AND DKEMOD = ? ";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABDEL, DEL);
            stm.setString(1, obj.DKEKEY);
            stm.setTimestamp(2, java.sql.Timestamp.valueOf(obj.DKEMOD));
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
    private static String GET = "SELECT DKEKEY , DKEDTYKEY , DKEDOSKEY , DKENUMBER , DKEMOD" + " FROM " + TSProgManager.SCHEMA + ".TPH061_DOSKEY WHERE DKEKEY = ? ";

    private static String TABGET = "PHC061GET";

    /**
 * GET : get an object using the primary key.
 * Retrieve this object from the database.
*/
    private JDmsRequest get(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRDoskey obj = null;
        JDmsRequest rc = req;
        obj = new PRDoskey(new String(comm.getRecordLayout()));
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABGET, GET);
            stm.setString(1, obj.DKEKEY);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                PRDoskey rsobj = new PRDoskey(rs);
                comm.setRecordLayout(rsobj.toBuffer());
                comm.setReturnCode(0);
                comm.setReturnCount(1);
                rc.SQLCode = 0;
            } else {
                comm.setReturnCode(100);
                comm.setReturnMessage(obj.DKEKEY + " not found.");
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

    private static String TABRETA = "PHC061RETA";

    /**
 * RETA : get all objects from a table.
*/
    private JDmsRequest reta(Connection con, JDmsRequest req, JDmsCommarea comm) {
        JDmsRequest rc = req;
        if (RETA == null) {
            RETA = "SELECT DKEKEY , DKEDTYKEY , DKEDOSKEY , DKENUMBER , DKEMOD" + " FROM " + TSProgManager.SCHEMA + ".TPH061_DOSKEY ORDER BY DKEKEY ";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABRETA, RETA);
            ResultSet rs = stm.executeQuery();
            StringBuffer buf = new StringBuffer();
            int count = 0;
            while (rs.next()) {
                PRDoskey rsobj = new PRDoskey(rs);
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

    private static String TABMOD = "PHC061MOD";

    /**
 * MOD : get all objects from a table.
*/
    private JDmsRequest mod(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRDoskey obj = null;
        JDmsRequest rc = req;
        obj = new PRDoskey(new String(comm.getRecordLayout()));
        if (MOD == null) {
            MOD = "SELECT DKEKEY , DKEDTYKEY , DKEDOSKEY , DKENUMBER , DKEMOD" + " FROM " + TSProgManager.SCHEMA + ".TPH061_DOSKEY WHERE DKEMOD >= ? ORDER BY DKEMOD ";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABMOD, MOD);
            if (obj.DKEMOD.startsWith(" ")) {
                stm.setNull(1, Types.TIMESTAMP);
            } else {
                stm.setTimestamp(1, Timestamp.valueOf(obj.DKEMOD));
            }
            ResultSet rs = stm.executeQuery();
            StringBuffer buf = new StringBuffer();
            int count = 0;
            while (rs.next()) {
                PRDoskey rsobj = new PRDoskey(rs);
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
 * SI1 will contain the SQL code for retrieval dke_Key
*/
    private static String SI1 = null;

    private static String TABSI1 = "PHC061SI1";

    /**
 * si1: get an object using dke_Key
 * tries to retrieve this object from the database.
*/
    private JDmsRequest si1(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRDoskey obj = null;
        JDmsRequest rc = req;
        obj = new PRDoskey(new String(comm.getRecordLayout()));
        if (SI1 == null) {
            SI1 = "SELECT DKEKEY , DKEDTYKEY , DKEDOSKEY , DKENUMBER , DKEMOD" + " FROM " + TSProgManager.SCHEMA + ".TPH061_DOSKEY WHERE DKENUMBER = ? ";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABSI1, SI1);
            stm.setString(1, obj.DkeNumber);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                PRDoskey rsobj = new PRDoskey(rs);
                comm.setRecordLayout(rsobj.toBuffer());
                comm.setReturnCode(0);
                comm.setReturnCount(1);
                rc.SQLCode = 0;
            } else {
                comm.setReturnCode(100);
                comm.setReturnMessage(obj.DKEKEY + " not found.");
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
 * SI1A will contain the SQL code for retrieval dke_Key
*/
    private static String SI1A = null;

    private static String TABSI1A = "PHC061SI1A";

    /**
 * si1a: get objects using dke_Key
 * tries to retrieve all object from the database.
*/
    private JDmsRequest si1a(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRDoskey obj = null;
        JDmsRequest rc = req;
        obj = new PRDoskey(new String(comm.getRecordLayout()));
        if (SI1A == null) {
            SI1A = "SELECT DKEKEY , DKEDTYKEY , DKEDOSKEY , DKENUMBER , DKEMOD" + " FROM " + TSProgManager.SCHEMA + ".TPH061_DOSKEY WHERE ( DKENUMBER >= ?  ) ORDER BY DKENUMBER ";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABSI1A, SI1A);
            stm.setString(1, obj.DkeNumber);
            ResultSet rs = stm.executeQuery();
            StringBuffer buf = new StringBuffer();
            int count = 0;
            while (rs.next()) {
                PRDoskey rsobj = new PRDoskey(rs);
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
        } else if (act.equals("SDTY")) {
            return sdty(con, req, comm);
        } else if (act.equals("SDOS")) {
            return sdos(con, req, comm);
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

    /**
 * SDTY will contain the SQL code for retrieval by SDTY
*/
    private static String SDTY = null;

    private static String TABSDTY = "PHC061SDTY";

    /**
 * sdty: get an object using SDTY
 * tries to retrieve this object from the database.
*/
    private JDmsRequest sdty(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRDoskey obj = null;
        JDmsRequest rc = req;
        obj = new PRDoskey(new String(comm.getRecordLayout()));
        if (SDTY == null) {
            SDTY = "SELECT DKEKEY , DKEDTYKEY , DKEDOSKEY , DKENUMBER , DKEMOD" + " FROM " + TSProgManager.SCHEMA + ".TPH061_DOSKEY WHERE DKEDTYKEY = ? ORDER BY DKEKEY";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABSDTY, SDTY);
            stm.setString(1, obj.DKEDTYKEY);
            ResultSet rs = stm.executeQuery();
            StringBuffer buf = new StringBuffer();
            int count = 0;
            while (rs.next()) {
                PRDoskey rsobj = new PRDoskey(rs);
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
 * SDOS will contain the SQL code for retrieval by SDOS
*/
    private static String SDOS = null;

    private static String TABSDOS = "PHC061SDOS";

    /**
 * sdos: get an object using SDOS
 * tries to retrieve this object from the database.
*/
    private JDmsRequest sdos(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRDoskey obj = null;
        JDmsRequest rc = req;
        obj = new PRDoskey(new String(comm.getRecordLayout()));
        if (SDOS == null) {
            SDOS = "SELECT DKEKEY , DKEDTYKEY , DKEDOSKEY , DKENUMBER , DKEMOD" + " FROM " + TSProgManager.SCHEMA + ".TPH061_DOSKEY WHERE DKEDOSKEY = ? ORDER BY DKEKEY";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABSDOS, SDOS);
            stm.setString(1, obj.DKEDOSKEY);
            ResultSet rs = stm.executeQuery();
            StringBuffer buf = new StringBuffer();
            int count = 0;
            while (rs.next()) {
                PRDoskey rsobj = new PRDoskey(rs);
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
}
