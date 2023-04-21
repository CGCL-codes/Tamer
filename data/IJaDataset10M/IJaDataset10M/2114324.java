package org.epoline.jdms.program.jdbc;

import java.sql.*;
import org.epoline.jsf.jdms.plugin.JDmsCommarea;
import org.epoline.jsf.jdms.dl.JDmsRequest;
import org.epoline.jsf.jdms.TSProgram;
import org.epoline.jsf.jdms.TSProgManager;

public class PHC039 implements TSProgram {

    private static String XX0 = "  ";

    private static String XX1 = "   ";

    private static final String ADJUST = "000000";

    static class PRPaperfile {

        static int LEN = 83;

        private static long lasttime = 0L;

        private static Object lock = new Object();

        public String PAPKEY;

        public String PAPPTYKEY;

        public String PAPORGKEY;

        public String PapPhxName;

        public String PAPMOD;

        public PRPaperfile() {
        }

        public PRPaperfile(ResultSet rs) throws SQLException {
            PAPKEY = rs.getString(1);
            if (PAPKEY.length() > 15) {
                PAPKEY = PAPKEY.substring(0, 15);
            }
            PAPPTYKEY = rs.getString(2);
            if (PAPPTYKEY.length() > 15) {
                PAPPTYKEY = PAPPTYKEY.substring(0, 15);
            }
            PAPORGKEY = rs.getString(3);
            if (PAPORGKEY.length() > 15) {
                PAPORGKEY = PAPORGKEY.substring(0, 15);
            }
            PapPhxName = rs.getString(4);
            if (PapPhxName.length() > 12) {
                PapPhxName = PapPhxName.substring(0, 12);
            }
            PAPMOD = (rs.getString(5) + ADJUST).substring(0, 26);
        }

        public PRPaperfile(String buf) {
            PAPKEY = buf.substring(0, 15);
            PAPPTYKEY = buf.substring(15, 30);
            PAPORGKEY = buf.substring(30, 45);
            PapPhxName = buf.substring(45, 57);
            PAPMOD = buf.substring(57, 83);
        }

        public void newId(String userid) {
            long time = System.currentTimeMillis();
            synchronized (lock) {
                while (time == lasttime) {
                    time = System.currentTimeMillis();
                }
                lasttime = time;
            }
            PAPKEY = (Long.toString(time, 36).toUpperCase() + userid + "       ").substring(0, 15);
        }

        public StringBuffer toBuffer() {
            StringBuffer buf = new StringBuffer();
            buf.append(PAPKEY);
            buf.append(PAPPTYKEY);
            buf.append(PAPORGKEY);
            buf.append(PapPhxName);
            buf.append(PAPMOD);
            return buf;
        }

        public StringBuffer asBuffer() {
            StringBuffer buf = new StringBuffer();
            buf.append(REC_SEP);
            buf.append("1439");
            buf.append(FIELD_SEP);
            buf.append(PAPKEY);
            buf.append(FIELD_SEP);
            buf.append(PAPPTYKEY);
            buf.append(FIELD_SEP);
            buf.append(PAPORGKEY);
            buf.append(FIELD_SEP);
            buf.append(PapPhxName);
            buf.append(FIELD_SEP);
            buf.append(PAPMOD);
            buf.append(FIELD_SEP);
            return buf;
        }
    }

    /**
 * These strings contain the SQL code for adding an Object.
*/
    private static String ADD = null;

    private static String TABADD = "PHC039ADD";

    /**
 * ADD : adds an object to the database.
 * This method generates an internal key and timestamp.
*/
    private JDmsRequest add(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRPaperfile obj = null;
        JDmsRequest rc = req;
        obj = new PRPaperfile(new String(comm.getRecordLayout()));
        obj.newId(req.userid);
        if (ADD == null) {
            ADD = "INSERT INTO " + TSProgManager.SCHEMA + ".TPH039_PAPERFILE ( PAPKEY , PAPXX0 , PAPXX1 , PAPPTYKEY , PAPORGKEY , PAPPHXNAME , PAPMOD )" + " VALUES ( ?, ?, ?, ?, ?, ?, " + TSProgManager.TIMESTAMP + " ) ";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABADD, ADD);
            stm.setString(1, obj.PAPKEY);
            stm.setString(2, XX0);
            stm.setString(3, XX1);
            stm.setString(4, obj.PAPPTYKEY);
            stm.setString(5, obj.PAPORGKEY);
            stm.setString(6, obj.PapPhxName);
            int count = stm.executeUpdate();
            stm.close();
            stm = TSProgManager.getStatement(con, TABGET, GET);
            stm.setString(1, obj.PAPKEY);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                PRPaperfile rsobj = new PRPaperfile(rs);
                comm.setRecordLayout(rsobj.toBuffer());
                comm.setReturnCode(0);
                comm.setReturnCount(count);
                rc.SQLCode = 0;
            } else {
                comm.setReturnCode(100);
                comm.setReturnMessage(obj.PAPKEY + " not found.");
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

    private static String TABLOAD = "PHC039LOAD";

    /**
 * LOAD : adds an object to the database.
 * This method does not generates an internal key and timestamp.
*/
    private JDmsRequest load(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRPaperfile obj = null;
        JDmsRequest rc = req;
        obj = new PRPaperfile(new String(comm.getRecordLayout()));
        if (LOAD == null) {
            LOAD = "INSERT INTO " + TSProgManager.SCHEMA + ".TPH039_PAPERFILE ( PAPKEY , PAPXX0 , PAPXX1 , PAPPTYKEY , PAPORGKEY , PAPPHXNAME , PAPMOD )" + " VALUES ( ?, ?, ?, ?, ?, ?, ? ) ";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABLOAD, LOAD);
            stm.setString(1, obj.PAPKEY);
            stm.setString(2, XX0);
            stm.setString(3, XX1);
            stm.setString(4, obj.PAPPTYKEY);
            stm.setString(5, obj.PAPORGKEY);
            stm.setString(6, obj.PapPhxName);
            stm.setTimestamp(7, java.sql.Timestamp.valueOf(obj.PAPMOD));
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

    private static String TABUPD = "PHC039UPD";

    /**
 * UPD : Updates an object in the database.
 * This method checks the timestamp.
*/
    private JDmsRequest upd(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRPaperfile obj = null;
        JDmsRequest rc = req;
        obj = new PRPaperfile(new String(comm.getRecordLayout()));
        if (UPD == null) {
            UPD = "UPDATE " + TSProgManager.SCHEMA + ".TPH039_PAPERFILE SET PAPPTYKEY = ?  , PAPORGKEY = ?  , PAPPHXNAME = ?  , PAPMOD = " + TSProgManager.TIMESTAMP + " WHERE PAPKEY = ? AND PAPMOD = ? ";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABUPD, UPD);
            stm.setString(4, obj.PAPKEY);
            stm.setTimestamp(5, java.sql.Timestamp.valueOf(obj.PAPMOD));
            stm.setString(1, obj.PAPPTYKEY);
            stm.setString(2, obj.PAPORGKEY);
            stm.setString(3, obj.PapPhxName);
            int count = stm.executeUpdate();
            stm.close();
            stm = TSProgManager.getStatement(con, TABGET, GET);
            stm.setString(1, obj.PAPKEY);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                PRPaperfile rsobj = new PRPaperfile(rs);
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

    private static String TABDEL = "PHC039DEL";

    /**
 * DEL : deletes an object to the database.
*/
    private JDmsRequest del(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRPaperfile obj = null;
        JDmsRequest rc = req;
        obj = new PRPaperfile(new String(comm.getRecordLayout()));
        if (DEL == null) {
            DEL = "DELETE FROM " + TSProgManager.SCHEMA + ".TPH039_PAPERFILE WHERE PAPKEY = ? AND PAPMOD = ? ";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABDEL, DEL);
            stm.setString(1, obj.PAPKEY);
            stm.setTimestamp(2, java.sql.Timestamp.valueOf(obj.PAPMOD));
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
    private static String GET = "SELECT PAPKEY , PAPPTYKEY , PAPORGKEY , PAPPHXNAME , PAPMOD" + " FROM " + TSProgManager.SCHEMA + ".TPH039_PAPERFILE WHERE PAPKEY = ? ";

    private static String TABGET = "PHC039GET";

    /**
 * GET : get an object using the primary key.
 * Retrieve this object from the database.
*/
    private JDmsRequest get(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRPaperfile obj = null;
        JDmsRequest rc = req;
        obj = new PRPaperfile(new String(comm.getRecordLayout()));
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABGET, GET);
            stm.setString(1, obj.PAPKEY);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                PRPaperfile rsobj = new PRPaperfile(rs);
                comm.setRecordLayout(rsobj.toBuffer());
                comm.setReturnCode(0);
                comm.setReturnCount(1);
                rc.SQLCode = 0;
            } else {
                comm.setReturnCode(100);
                comm.setReturnMessage(obj.PAPKEY + " not found.");
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

    private static String TABRETA = "PHC039RETA";

    /**
 * RETA : get all objects from a table.
*/
    private JDmsRequest reta(Connection con, JDmsRequest req, JDmsCommarea comm) {
        JDmsRequest rc = req;
        if (RETA == null) {
            RETA = "SELECT PAPKEY , PAPPTYKEY , PAPORGKEY , PAPPHXNAME , PAPMOD" + " FROM " + TSProgManager.SCHEMA + ".TPH039_PAPERFILE ORDER BY PAPKEY ";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABRETA, RETA);
            ResultSet rs = stm.executeQuery();
            StringBuffer buf = new StringBuffer();
            int count = 0;
            while (rs.next()) {
                PRPaperfile rsobj = new PRPaperfile(rs);
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

    private static String TABMOD = "PHC039MOD";

    /**
 * MOD : get all objects from a table.
*/
    private JDmsRequest mod(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRPaperfile obj = null;
        JDmsRequest rc = req;
        obj = new PRPaperfile(new String(comm.getRecordLayout()));
        if (MOD == null) {
            MOD = "SELECT PAPKEY , PAPPTYKEY , PAPORGKEY , PAPPHXNAME , PAPMOD" + " FROM " + TSProgManager.SCHEMA + ".TPH039_PAPERFILE WHERE PAPMOD >= ? ORDER BY PAPMOD ";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABMOD, MOD);
            if (obj.PAPMOD.startsWith(" ")) {
                stm.setNull(1, Types.TIMESTAMP);
            } else {
                stm.setTimestamp(1, Timestamp.valueOf(obj.PAPMOD));
            }
            ResultSet rs = stm.executeQuery();
            StringBuffer buf = new StringBuffer();
            int count = 0;
            while (rs.next()) {
                PRPaperfile rsobj = new PRPaperfile(rs);
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
        } else if (act.equals("SPTY")) {
            return spty(con, req, comm);
        } else if (act.equals("SORG")) {
            return sorg(con, req, comm);
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
 * SPTY will contain the SQL code for retrieval by SPTY
*/
    private static String SPTY = null;

    private static String TABSPTY = "PHC039SPTY";

    /**
 * spty: get an object using SPTY
 * tries to retrieve this object from the database.
*/
    private JDmsRequest spty(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRPaperfile obj = null;
        JDmsRequest rc = req;
        obj = new PRPaperfile(new String(comm.getRecordLayout()));
        if (SPTY == null) {
            SPTY = "SELECT PAPKEY , PAPPTYKEY , PAPORGKEY , PAPPHXNAME , PAPMOD" + " FROM " + TSProgManager.SCHEMA + ".TPH039_PAPERFILE WHERE PAPPTYKEY = ? ORDER BY PAPKEY";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABSPTY, SPTY);
            stm.setString(1, obj.PAPPTYKEY);
            ResultSet rs = stm.executeQuery();
            StringBuffer buf = new StringBuffer();
            int count = 0;
            while (rs.next()) {
                PRPaperfile rsobj = new PRPaperfile(rs);
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
 * SORG will contain the SQL code for retrieval by SORG
*/
    private static String SORG = null;

    private static String TABSORG = "PHC039SORG";

    /**
 * sorg: get an object using SORG
 * tries to retrieve this object from the database.
*/
    private JDmsRequest sorg(Connection con, JDmsRequest req, JDmsCommarea comm) {
        PRPaperfile obj = null;
        JDmsRequest rc = req;
        obj = new PRPaperfile(new String(comm.getRecordLayout()));
        if (SORG == null) {
            SORG = "SELECT PAPKEY , PAPPTYKEY , PAPORGKEY , PAPPHXNAME , PAPMOD" + " FROM " + TSProgManager.SCHEMA + ".TPH039_PAPERFILE WHERE PAPORGKEY = ? ORDER BY PAPKEY";
        }
        PreparedStatement stm = null;
        try {
            stm = TSProgManager.getStatement(con, TABSORG, SORG);
            stm.setString(1, obj.PAPORGKEY);
            ResultSet rs = stm.executeQuery();
            StringBuffer buf = new StringBuffer();
            int count = 0;
            while (rs.next()) {
                PRPaperfile rsobj = new PRPaperfile(rs);
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
