package com.db.mdm.mssql;

import com.db.mdm.AuditRecord;
import com.db.mdm.MDMAuditThread;
import com.db.mdm.MDMConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author srkrishnan
 */
public class MSSQLConnection implements MDMConnection {

    String host;

    String user;

    String pass;

    String db;

    String dbClass;

    int port;

    protected String id;

    protected String name;

    Connection con = null;

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public String getId() {
        return id;
    }

    /**
     * Set the value of id
     *
     * @param id new value of id
     */
    public void setId(String id) {
        this.id = id;
    }

    public MSSQLConnection() {
    }

    public MSSQLConnection(String name, String host, String user, String pass, String db, int port) {
        this.name = name;
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.db = db;
        this.port = port;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDbClass() {
        return dbClass;
    }

    public void setDbClass(String dbClass) {
        this.dbClass = dbClass;
    }

    public Connection connectDB() {
        String dbUrl = "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + db;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            if (con == null || con.isClosed()) {
                con = DriverManager.getConnection(dbUrl, user, pass);
            }
            if (!con.isClosed()) {
                System.out.println("Connected to " + dbUrl + " successfully.....");
            }
            return con;
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(MSSQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            Logger.getLogger(MSSQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void createTriggers(MDMConnection auditConnection) {
        createInsertTrigger(auditConnection);
        createUpdateTrigger(auditConnection);
        createDeleteTrigger(auditConnection);
    }

    public void createInsertTrigger(MDMConnection auditConnection) {
        Connection conn = null;
        try {
            conn = this.getCon();
            ResultSet rsTable = conn.getMetaData().getTables(null, null, null, new String[] { "TABLE" });
            PreparedStatement prpsmt;
            while (rsTable.next()) {
                System.out.println(rsTable.getString(3));
                prpsmt = conn.prepareStatement("SELECT Column_name,Data_Type,Is_Nullable FROM INFORMATION_SCHEMA.Columns where TABLE_NAME ='" + rsTable.getString(3) + "'");
                ResultSet rsColumn = prpsmt.executeQuery();
                String droptriggerInsertStr = "IF EXISTS (SELECT name FROM sysobjects WHERE name = " + "'" + rsTable.getString(1) + "_" + rsTable.getString(3) + "_insert'" + "AND type = 'TR') DROP TRIGGER " + rsTable.getString(1) + "_" + rsTable.getString(3) + "_insert;";
                String triggerInsertStr = "CREATE TRIGGER " + rsTable.getString(1) + "_" + rsTable.getString(3) + "_insert ON " + rsTable.getString(3) + " FOR INSERT as";
                triggerInsertStr += " insert into " + ((MSSQLConnection) auditConnection).getDb() + "..AUD_" + rsTable.getString(3) + " (";
                String triggertmp = "";
                while (rsColumn.next()) {
                    triggerInsertStr += "" + rsColumn.getString(1) + ",";
                    triggertmp += rsColumn.getString(1) + ",";
                }
                triggerInsertStr += "AUD_trigflag,AUD_id";
                triggertmp += "'insert',NewID()";
                prpsmt = conn.prepareStatement(droptriggerInsertStr);
                prpsmt.executeUpdate();
                prpsmt = conn.prepareStatement(triggerInsertStr + ") select " + triggertmp + " from inserted");
                prpsmt.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void createUpdateTrigger(MDMConnection auditConnection) {
        Connection conn = null;
        try {
            conn = this.getCon();
            ResultSet rsTable = conn.getMetaData().getTables(null, null, null, new String[] { "TABLE" });
            PreparedStatement prpsmt;
            while (rsTable.next()) {
                System.out.println(rsTable.getString(3));
                prpsmt = conn.prepareStatement("SELECT Column_name,Data_Type,Is_Nullable FROM INFORMATION_SCHEMA.Columns where TABLE_NAME ='" + rsTable.getString(3) + "'");
                ResultSet rsColumn = prpsmt.executeQuery();
                String droptriggerInsertStr = "IF EXISTS (SELECT name FROM sysobjects WHERE name = " + "'" + rsTable.getString(1) + "_" + rsTable.getString(3) + "_update'" + "AND type = 'TR') DROP TRIGGER " + rsTable.getString(1) + "_" + rsTable.getString(3) + "_update;";
                String triggerInsertStr = "CREATE TRIGGER " + rsTable.getString(1) + "_" + rsTable.getString(3) + "_update ON " + rsTable.getString(3) + " FOR UPDATE as";
                triggerInsertStr += " insert into " + ((MSSQLConnection) auditConnection).getDb() + "..AUD_" + rsTable.getString(3) + " (";
                String triggertmp = "";
                while (rsColumn.next()) {
                    triggerInsertStr += "" + rsColumn.getString(1) + ",";
                    triggertmp += rsColumn.getString(1) + ",";
                }
                triggerInsertStr += "AUD_trigflag,AUD_id";
                triggertmp += "'update',NewID()";
                prpsmt = conn.prepareStatement(droptriggerInsertStr);
                prpsmt.executeUpdate();
                prpsmt = conn.prepareStatement(triggerInsertStr + ") select " + triggertmp + " from inserted");
                prpsmt.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void createDeleteTrigger(MDMConnection auditConnection) {
        Connection conn = null;
        try {
            conn = this.getCon();
            ResultSet rsTable = conn.getMetaData().getTables(null, null, null, new String[] { "TABLE" });
            PreparedStatement prpsmt;
            while (rsTable.next()) {
                System.out.println(rsTable.getString(3));
                prpsmt = conn.prepareStatement("SELECT Column_name,Data_Type,Is_Nullable FROM INFORMATION_SCHEMA.Columns where TABLE_NAME ='" + rsTable.getString(3) + "'");
                ResultSet rsColumn = prpsmt.executeQuery();
                String droptriggerInsertStr = "IF EXISTS (SELECT name FROM sysobjects WHERE name = " + "'" + rsTable.getString(1) + "_" + rsTable.getString(3) + "_delete'" + "AND type = 'TR') DROP TRIGGER " + rsTable.getString(1) + "_" + rsTable.getString(3) + "_delete;";
                String triggerInsertStr = "CREATE TRIGGER " + rsTable.getString(1) + "_" + rsTable.getString(3) + "_delete ON " + rsTable.getString(3) + " FOR DELETE as";
                triggerInsertStr += " insert into " + ((MSSQLConnection) auditConnection).getDb() + "..AUD_" + rsTable.getString(3) + " (";
                String triggertmp = "";
                while (rsColumn.next()) {
                    triggerInsertStr += "" + rsColumn.getString(1) + ",";
                    triggertmp += rsColumn.getString(1) + ",";
                }
                triggerInsertStr += "AUD_trigflag,AUD_id";
                triggertmp += "'delete',NewID()";
                prpsmt = conn.prepareStatement(droptriggerInsertStr);
                prpsmt.executeUpdate();
                prpsmt = conn.prepareStatement(triggerInsertStr + ") select " + triggertmp + " from deleted");
                prpsmt.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void createRepoTables(MDMConnection repoMdmconnection) {
        Connection conn = null;
        try {
            conn = conn = this.getCon();
            PreparedStatement prpsmt = conn.prepareStatement("IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = object_id(N'[repotable]') AND OBJECTPROPERTY(id, N'IsUserTable') = 1) CREATE TABLE repotable (  id nchar(50) NOT NULL PRIMARY KEY,  db nchar(20) DEFAULT NULL,  xml nchar(500) DEFAULT NULL);");
            prpsmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void createAuditTables(MDMConnection auditMdmConnection, MDMConnection repoMdmConnection) {
        String primaryField = null;
        Connection conn = null;
        Connection auditcon = null;
        try {
            conn = conn = this.getCon();
            auditcon = (Connection) auditMdmConnection.getCon();
            ResultSet rsTable = conn.getMetaData().getTables(null, null, null, new String[] { "TABLE" });
            PreparedStatement prpsmt;
            while (rsTable.next()) {
                System.out.println(rsTable.getString(3));
                prpsmt = conn.prepareStatement("SELECT Column_Name,Data_Type,Is_Nullable FROM Information_schema.Columns WHERE Table_Name = '" + rsTable.getString(3) + "'");
                ResultSet rsColumn = prpsmt.executeQuery();
                String querystr = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = object_id(N'[AUD_" + rsTable.getString(3) + "]') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)CREATE TABLE " + "AUD_" + rsTable.getString(3) + " (";
                while (rsColumn.next()) {
                    querystr += "" + rsColumn.getString(1) + " " + rsColumn.getString(2) + (rsColumn.getString(3).equalsIgnoreCase("NO") ? " NOT NULL" : "") + ",";
                }
                querystr += "AUD_trigflag nchar(20),AUD_id nchar(50))";
                prpsmt = auditcon.prepareStatement(querystr);
                prpsmt.executeUpdate();
                MDMAuditThread auditMDMThread = new MDMAuditThread(auditMdmConnection, repoMdmConnection, "AUD_" + rsTable.getString(3));
                Thread auditThread = new Thread(auditMDMThread, "AUD_" + rsTable.getString(3));
                ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
                scheduler.scheduleWithFixedDelay(auditThread, 0, 300, TimeUnit.MILLISECONDS);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(MSSQLConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public AuditRecord getAuditRecordinXML(MDMConnection auditMdmConnection, String TableName) {
        String dbxml = "";
        PreparedStatement prpsmt = null;
        ResultSet rs;
        AuditRecord auditrecord = new AuditRecord();
        Connection conn = null;
        try {
            conn = conn = this.getCon();
            prpsmt = conn.prepareStatement("SELECT * FROM " + TableName);
            rs = prpsmt.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            if (rs.next()) {
                dbxml = "";
                dbxml += "<db><table>" + md.getTableName(1) + "</table>" + "<record><id>" + rs.getString("AUD_id") + "</id><trigflag>" + rs.getString("AUD_trigflag") + "</trigflag><data>";
                for (int i = 1; i <= columnCount; i++) {
                    if (!md.getColumnName(i).equals("AUD_id") && !md.getColumnName(i).equals("AUD_trigflag")) {
                        dbxml += "<column><name>" + md.getColumnName(i) + "</name><value>" + rs.getString(i) + "</value></column>";
                    }
                }
                dbxml += "</data></record></db>";
                auditrecord.setId(rs.getString("AUD_id"));
                prpsmt = conn.prepareStatement("DELETE FROM " + TableName + " WHERE AUD_id='" + rs.getString("AUD_id") + "'");
                prpsmt.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        auditrecord.setDbxml(dbxml);
        return auditrecord;
    }

    public void insertAuditRecordintoRepo(MDMConnection repoConnection, AuditRecord auditrecord) {
        Connection conn = null;
        try {
            conn = (Connection) repoConnection.getCon();
            System.out.println("Insert record " + auditrecord.getId() + " in repo.");
            PreparedStatement prpsmt = conn.prepareStatement("INSERT INTO repotable (id,db,xml) VALUES (?,?,?)");
            prpsmt.setString(1, auditrecord.getId());
            prpsmt.setString(2, auditrecord.getName());
            prpsmt.setString(3, auditrecord.getDbxml());
            prpsmt.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
