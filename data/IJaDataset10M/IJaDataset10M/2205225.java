package com.agfa.db.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import org.dcm4che.data.Dataset;
import org.dcm4che.data.DcmEncodeParam;
import org.dcm4che.data.DcmObjectFactory;
import org.dcm4che.dict.Tags;

public class Jpdbi {

    public static final String VERSION = "2.0";

    public static final String ID = "$Id: Jpdbi.java 13142 2010-04-10 18:20:41Z kianusch $";

    public static final String REVISION = "$Revision: 13142 $";

    static final int PATIENT = 1;

    static final int STUDY = 2;

    static final int SERIE = 3;

    static final int INSTANCE = 4;

    static final int PATH = 5;

    static final int QUERY_SELECT = 0;

    static final int QUERY_FROM = 1;

    static final int QUERY_JOIN = 2;

    static final int QUERY_LINKS = 3;

    static final int QUERY_WHERE = 4;

    static final int QUERY_GROUP = 5;

    static final int QUERY_ORDER = 6;

    public static void UpdateStudyModality(Connection connection, long pk, CommandLine cfg) {
        String sql = "select distinct MODALITY from SERIES where STUDY_FK=" + pk;
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            String MODALITIES = "";
            String MOD = null;
            while (rs.next()) {
                MOD = rs.getString(1);
                if (MODALITIES.length() > 0) MODALITIES += "\\";
                MODALITIES += MOD;
            }
            rs.close();
            sql = "update STUDY set MODS_IN_STUDY='" + MODALITIES + "' where PK=" + pk;
            if (cfg.debug) System.err.println("DEBUG: " + sql); else stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void UpdateField(PreparedStatement stmt, Long pk, boolean debug) throws SQLException {
        if (debug) System.err.println("DEBUG: Update: < PK=" + pk + " >"); else {
            stmt.setLong(1, pk);
            stmt.execute();
        }
    }

    static void UpdateField(ResultSet rs, PreparedStatement stmt, Long pk, String level, String[][] update, boolean debug) throws SQLException, IOException {
        String field = "";
        if (level.equals("PATIENT")) field = "PAT_ATTRS";
        if (level.equals("STUDY")) field = "STUDY_ATTRS";
        if (level.equals("SERIES")) field = "SERIES_ATTRS";
        if (level.equals("INSTANCE")) field = "INST_ATTRS";
        if (debug) System.err.println("DEBUG: Reading " + field + "...");
        Blob bl = rs.getBlob(field);
        if (bl != null) {
            InputStream bis = bl.getBinaryStream();
            Dataset ds = DcmObjectFactory.getInstance().newDataset();
            ds.readFile(bis, null, -1);
            bis.close();
            if (debug) {
                System.err.println("<" + field + " OLD>");
                ds.dumpDataset(System.err, null);
                System.err.println("</" + field + "OLD>");
            }
            for (int loop = 0; loop < update.length; loop++) {
                String DcmField = update[loop][1];
                String DcmValue = update[loop][2];
                if (DcmField != null) {
                    int TAG = 0;
                    if (DcmField.startsWith("x", 0)) TAG = Integer.parseInt(DcmField.substring(1), 16); else TAG = Tags.forName(DcmField);
                    if (DcmValue == null) {
                        if (ds.contains(TAG)) ds.remove(TAG); else ds = null;
                    } else {
                        ds.putXX(TAG, DcmValue);
                    }
                }
            }
            if (debug) {
                System.err.println("<" + field + " NEW>");
                if (ds == null) {
                    System.err.println("No Changes to DataSet");
                } else {
                    ds.dumpDataset(System.err, null);
                }
                System.err.println("</" + field + " NEW>");
            }
            if (ds != null) {
                if (debug) System.err.println("DEBUG: Update < PK=" + pk + " >"); else {
                    int len = ds.calcLength(DcmEncodeParam.EVR_LE);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream(len);
                    ds.writeDataset(bos, DcmEncodeParam.EVR_LE);
                    stmt.setBinaryStream(1, new ByteArrayInputStream(bos.toByteArray()), len);
                    stmt.setLong(2, pk);
                    stmt.execute();
                }
            }
        }
    }

    private static void ParseQuery(Connection conn, String[] query, String[][] update, CommandLine cfg) throws SQLException, IOException {
        Statement stmt = conn.createStatement();
        ResultSet rs = null;
        String SQLStatement = "";
        if (cfg.nonempty) {
            SQLStatement += "from " + query[QUERY_FROM] + " ";
            SQLStatement += "where " + query[QUERY_LINKS] + " " + query[QUERY_WHERE];
        } else {
            SQLStatement += "from " + query[QUERY_JOIN] + " ";
            SQLStatement += "where " + query[QUERY_WHERE];
        }
        String CountStatement = "select COUNT(*) CNT " + SQLStatement;
        String QueryStatement = "select " + query[QUERY_SELECT] + " " + SQLStatement;
        if (cfg.debug) {
            System.err.println("DEBUG: Count: < " + CountStatement + " >");
            System.err.println("DEBUG: Query: < " + QueryStatement + " >");
        }
        boolean multi = false;
        boolean UpdModality = false;
        String UpdateStatement = null;
        String UpdateLevel = null;
        boolean DoUpdate = false;
        PreparedStatement UpdStmt = null;
        if (update != null) {
            UpdateLevel = update[0][3].toUpperCase();
            if (update[0][4].equals("t")) multi = true;
            for (int loop = 0; loop < update.length; loop++) {
                if (update[loop][0] != null) {
                    if (UpdateStatement == null) UpdateStatement = ""; else if (UpdateStatement.length() > 0) UpdateStatement += ",";
                    UpdateStatement += update[loop][0].toUpperCase();
                    UpdateStatement += (update[loop][2] == null) ? "=null" : "='" + update[loop][2] + "'";
                    if (update[loop][0].equals("MODALITY")) UpdModality = true;
                }
            }
            if (!cfg.updateDS.isEmpty()) {
                if (UpdateStatement == null) UpdateStatement = ""; else if (UpdateStatement.length() > 0) UpdateStatement += ",";
                if (UpdateLevel.equals("PATIENT")) UpdateStatement += "PAT_ATTRS";
                if (UpdateLevel.equals("STUDY")) UpdateStatement += "STUDY_ATTRS";
                if (UpdateLevel.equals("SERIES")) UpdateStatement += "SERIES_ATTRS";
                UpdateStatement += "=?";
            }
            UpdateStatement += " where PK=?";
            DoUpdate = true;
            if (cfg.debug) {
                System.err.println("DEBUG: Update: < " + "update " + UpdateLevel + " set " + UpdateStatement + " >");
            }
        }
        rs = stmt.executeQuery(CountStatement);
        rs.next();
        long rows = rs.getLong(1);
        rs.close();
        if (rows > 0) {
            if (DoUpdate) {
                if (!multi && rows != 1) {
                    _System.exit(1, "Multiple Updates not allowed on this Configuration.");
                }
                if (cfg.expert || rows == 1 || rows == cfg.UpdCount) {
                    UpdStmt = conn.prepareStatement("update " + UpdateLevel + " set " + UpdateStatement);
                } else {
                    _System.exit(1, "Updating more than one entry.  Please provide option \"--count " + rows + "\"");
                }
            }
            rs = stmt.executeQuery(QueryStatement);
            ResultSetMetaData md = rs.getMetaData();
            long PK;
            long LastPK = -1;
            while (rs.next()) {
                PK = Display.Patient(rs, md, cfg);
                if (DoUpdate && UpdateLevel.equals("PATIENT") && PK > -1) {
                    if (cfg.updateDS.isEmpty()) UpdateField(UpdStmt, PK, cfg.debug); else UpdateField(rs, UpdStmt, PK, UpdateLevel, update, cfg.debug);
                }
                if (cfg.levels.get(STUDY)) {
                    PK = Display.Study(rs, md, cfg);
                    if (DoUpdate && PK > -1) {
                        if (UpdModality && PK != LastPK && LastPK > -1) UpdateStudyModality(conn, LastPK, cfg);
                        if (UpdateLevel.equals("STUDY")) {
                            if (cfg.updateDS.isEmpty()) UpdateField(UpdStmt, PK, cfg.debug); else UpdateField(rs, UpdStmt, PK, UpdateLevel, update, cfg.debug);
                        }
                        LastPK = PK;
                    }
                }
                if (cfg.levels.get(SERIE)) {
                    PK = Display.Serie(rs, md, cfg);
                    if (DoUpdate && UpdateLevel.equals("SERIES") && PK > -1) {
                        if (cfg.updateDS.isEmpty()) UpdateField(UpdStmt, PK, cfg.debug); else UpdateField(rs, UpdStmt, PK, UpdateLevel, update, cfg.debug);
                    }
                }
                if (cfg.levels.get(INSTANCE)) {
                    Display.Instance(rs, md, cfg);
                }
                if (cfg.levels.get(PATH)) {
                    Display.Path(rs, md, cfg);
                }
            }
            if (DoUpdate && UpdModality && LastPK > -1) UpdateStudyModality(conn, LastPK, cfg);
            rs.close();
        } else {
            System.err.println("Query returns 0 rows.");
        }
        stmt.close();
    }

    public static void main(String[] argv) {
        System.setProperty("java.awt.headless", "true");
        Connection conn = null;
        String query[] = null;
        String update[][] = null;
        CommandLine cfg = new CommandLine(argv);
        if (cfg.debug) {
            System.err.println("DEBUG: Connect Url: < " + cfg.jdbcUrl + " >");
        }
        try {
            conn = DriverManager.getConnection(cfg.jdbcUrl);
            cfg.setDatabase(conn);
            update = Query.BuildUpdate(cfg);
            query = Query.Build(cfg);
            ParseQuery(conn, query, update, cfg);
            conn.close();
        } catch (Exception e) {
            if (cfg.debug) e.printStackTrace(); else _System.exit(1, e.toString());
        }
        _System.exit(0);
    }
}
