package com.jxml.qare.qhome.db;

import java.sql.*;
import java.math.BigInteger;
import java.util.*;
import com.jxml.quick.*;
import com.jxml.qare.qhome.*;
import com.jxml.qare.qhome.server.*;
import com.jxml.quick.config.*;
import java.io.*;

public final class DBInbox extends DBBase implements DeleteListener {

    static String tableName = "Inbox";

    static DBInbox db = new DBInbox();

    private DBInbox() {
        DBBase.dbBases.put(tableName, this);
        DBAccount.db.addDeleteListener(this);
        DBSession.db.addDeleteListener(this);
    }

    private static Map properties(String properties) throws Exception {
        QDoc schema = CreateProperties.createSchema();
        QDoc doc = Quick.parseString(schema, properties);
        Map rv = (Map) doc.getRoot();
        return rv;
    }

    public String tableName() {
        return tableName;
    }

    public void createTable() throws Exception {
        String query = "CREATE TABLE Inbox (" + "inboxId INT UNSIGNED NOT NULL AUTO_INCREMENT," + "userId INT UNSIGNED NOT NULL," + "INDEX userIdIndex (userId)," + "PRIMARY KEY (inboxId)," + "openingDate DATETIME  NOT NULL," + "dateReceived DATETIME NOT NULL," + "description TINYTEXT NOT NULL," + "applicationId INT UNSIGNED NOT NULL," + "action TINYTEXT NOT NULL," + "fromSystemId INT UNSIGNED NOT NULL," + "message TEXT NOT NULL," + "properties TEXT NOT NULL, " + "gkey CHAR(79)  NOT NULL )";
        createTable(query);
    }

    public void deleteEvent(DeleteEvent deleteEvent) throws Exception {
        Object source = deleteEvent.getSource();
        if (source instanceof DBAccount.Row) {
            DBAccount.Row account = (DBAccount.Row) source;
            List inboxes = get(account.getUser());
            int i, s;
            s = inboxes.size();
            for (i = 0; i < s; ++i) {
                DBInbox.Row inbox = (DBInbox.Row) inboxes.get(i);
                inbox.delete();
            }
        }
    }

    public DBInbox.Row get(int id) throws Exception {
        DBInbox.Row inbox = (DBInbox.Row) getWeak(new Integer(id));
        if (inbox != null) return inbox;
        String query = "select * from Inbox where inboxId=" + id;
        return (DBInbox.Row) getRow(query);
    }

    public DBInbox.Row get(DBUser.Row user, int inboxId) throws Exception {
        DBInbox.Row inbox = (DBInbox.Row) getWeak(new Integer(inboxId));
        if (inbox != null) {
            if (inbox.user.equals(user)) return inbox;
            return null;
        }
        String query = "select * " + "from Inbox  where" + " userId=" + user.getUserId() + " and inboxId=" + inboxId;
        return (DBInbox.Row) getRow(query);
    }

    public DBInbox.Row get(DBUser.Row user, String gkey) throws Exception {
        String query = "select * " + "from Inbox  where" + " userId=" + user.getUserId() + " and gkey=" + "'" + gkey + "'";
        return (DBInbox.Row) getRow(query);
    }

    public List get(DBUser.Row user) throws Exception {
        String query = "select * " + "from Inbox  where" + " userId=" + user.getUserId() + " order by dateReceived";
        return getRows(query);
    }

    public List getInboxRowList(DBUser.Row user, DBQOrderMungeColumn domc, DBQList qList) throws Exception {
        String query = "SELECT distinct Inbox.* FROM Inbox,User ," + " System,Application WHERE  Inbox.userId= " + user.getUserId() + " and " + qList.toSQL() + domc.toSQL();
        List inboxDataList = getRows(query);
        return inboxDataList;
    }

    public boolean haveGkey(String gkey) throws Exception {
        String query = "SELECT * FROM Inbox " + " WHERE gkey= " + "'" + gkey + "'";
        Connection con = Setup.con;
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        return rs.next();
    }

    public DBRow createObj(ResultSet rs) throws Exception {
        int inboxId = rs.getInt("inboxId");
        DBUser.Row user = Setup.dbUser.get(rs.getInt("userId"));
        java.util.Date openingDate = Setup.dateFormater.parse(rs.getString("openingDate"));
        java.util.Date dateRecieved = Setup.dateFormater.parse(rs.getString("dateReceived"));
        String action = rs.getString("action");
        DBSystem.Row fromSystem = Setup.dbSystem.get(rs.getInt("fromSystemId"));
        String message = rs.getString("message");
        String properties = rs.getString("properties");
        String description = rs.getString("description");
        DBApplication.Row application = Setup.dbApplication.get(rs.getInt("applicationId"));
        String gkey = rs.getString("gkey");
        return new DBInbox.Row(inboxId, user, openingDate, dateRecieved, description, application, action, fromSystem, message, properties, gkey);
    }

    public int getMaxPendingId() throws Exception {
        String query = "select MAX(inboxId) from Inbox";
        Connection con = Setup.con;
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs == null) throw new Exception("null result set");
        rs.next();
        int pid = rs.getInt(1);
        stmt.close();
        return pid;
    }

    public Row create(DBUser.Row user, String description, java.util.Date openingDate, DBApplication.Row application, String action, DBSystem.Row fromSystem, String message, String properties) throws Exception {
        return create(user, description, openingDate, application, action, fromSystem, message, properties, "");
    }

    public Row create(DBUser.Row user, String description, java.util.Date openingDate, DBApplication.Row application, String action, DBSystem.Row fromSystem, String message, String properties, String gkey) throws Exception {
        java.util.Date dateReceived = createCurrentDate();
        String dr = Setup.dateFormater.format(dateReceived);
        String od = Setup.dateFormater.format(openingDate);
        String query = "insert into Inbox (" + "userId, " + "openingDate, " + "dateReceived, " + "description, " + "applicationId, " + "action, " + "fromSystemId, " + "message," + "properties," + "gkey )" + " values (" + user.getUserId() + ", " + "'" + od + "', " + "'" + dr + "', " + "'" + description + "', " + application.getApplicationId() + ", " + "'" + action + "', " + fromSystem.getSystemId() + ", " + "'" + message + "', " + "'" + properties + "', " + "'" + gkey + "')";
        int rc = executeUpdate(query);
        int inboxId = getMaxPendingId();
        DBInbox.Row row = get(user, inboxId);
        if (row == null) System.out.println("Row is null &" + "user=" + user.getName() + " & inboxId= " + inboxId);
        sendCreateEvent(row);
        return row;
    }

    public void backup(DataOutputStream dos) throws Exception {
        System.out.println("Writing backup for table " + tableName());
        int lst = Setup.dbAccount.getMaxPendingId();
        int nxt = 1;
        int cnt = QConfig.getBackupBlockCount();
        Connection con = Setup.con;
        Statement stmt = con.createStatement();
        while (nxt <= lst) {
            int lim = nxt + cnt;
            String query = "select * from Inbox" + " where userId>" + (nxt - 1) + " and userId<" + lim + " order by userId,inboxId";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                DBRow o = createObj(rs);
                o.backup(dos);
            }
            nxt = lim;
        }
        stmt.close();
    }

    public void restore(String fileNamePrefix, DataInputStream dis) throws Exception {
        restored = true;
        System.out.println("Restoring table " + tableName());
        while (true) {
            String gkey;
            java.util.Date openingDate;
            try {
                gkey = restoreString(dis);
                openingDate = new java.util.Date(dis.readLong());
            } catch (EOFException eof) {
                return;
            }
            java.util.Date dateReceived = new java.util.Date(dis.readLong());
            DBUser.Row user = Setup.dbUser.get(restoreString(dis));
            DBApplication.Row application = Setup.dbApplication.get(restoreString(dis));
            DBSystem.Row fromSystem = Setup.dbSystem.get(restoreString(dis));
            String action = restoreString(dis);
            String message = restoreString(dis);
            String description = restoreString(dis);
            String properties = restoreString(dis);
            String dr = Setup.dateFormater.format(dateReceived);
            String od = Setup.dateFormater.format(openingDate);
            String query = "insert into Inbox (" + "userId, " + "openingDate, " + "dateReceived, " + "description, " + "applicationId, " + "action, " + "fromSystemId, " + "message," + "properties," + "gkey)" + " values (" + user.getUserId() + ", " + "'" + od + "', " + "'" + dr + "', " + "'" + description + "', " + application.getApplicationId() + ", " + "'" + action + "', " + fromSystem.getSystemId() + ", " + "'" + message + "', " + "'" + properties + "'" + "'" + gkey + "')";
            int rc = executeUpdate(query);
        }
    }

    public final class Row implements DBRow {

        private int inboxId;

        private java.util.Date openingDate;

        private java.util.Date dateRecieved;

        private DBUser.Row user;

        private DBApplication.Row application;

        private DBSystem.Row fromSystem;

        private String action;

        public String message;

        private String description;

        private String properties;

        private boolean closed = false;

        private String gkey;

        public Row(int inboxId, DBUser.Row user, java.util.Date openingDate, java.util.Date dateRecieved, String description, DBApplication.Row application, String action, DBSystem.Row fromSystem, String message, String properties, String gkey) {
            this.inboxId = inboxId;
            this.user = user;
            this.openingDate = openingDate;
            this.dateRecieved = dateRecieved;
            this.description = description;
            this.application = application;
            this.action = action;
            this.fromSystem = fromSystem;
            this.message = message;
            this.properties = properties;
            this.gkey = gkey;
        }

        public List getKeys() {
            List l = new ArrayList();
            l.add(getKey());
            return l;
        }

        public Object getKey() {
            return new Integer(inboxId);
        }

        public void update() throws Exception {
            String dr = Setup.dateFormater.format(getDateRecieved());
            String query = "UPDATE Inbox " + "SET  message= '" + message + "'," + "  description= '" + getDescription() + "'," + "  dateReceived= '" + dr + "'" + " WHERE inboxId=" + inboxId;
            int rc = executeUpdate(query);
        }

        public void delete() throws Exception {
            if (closed) return;
            closed = true;
            sendDeleteEvent(this);
            deleteWeak(this);
            String query = "delete from Inbox where" + " inboxId=" + getInboxId();
            int rc = executeUpdate(query);
        }

        public boolean closed() {
            return closed;
        }

        public int getInboxId() {
            return inboxId;
        }

        public DBUser.Row getUser() {
            return user;
        }

        public String getGkey() {
            return gkey;
        }

        public java.util.Date getOpeningDate() {
            return openingDate;
        }

        public java.util.Date getDateRecieved() {
            return dateRecieved;
        }

        public void setDateReceived(java.util.Date dateRecievied) {
            this.dateRecieved = dateRecieved;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public DBApplication.Row getApplication() {
            return application;
        }

        public DBSystem.Row getFromSystem() {
            return fromSystem;
        }

        public String getAction() {
            return action;
        }

        public Map getProperties() throws Exception {
            QDoc schema = CreateProperties.createSchema();
            QDoc doc = Quick.parseString(schema, properties);
            Map rv = (Map) doc.getRoot();
            return rv;
        }

        public String properties() {
            return properties;
        }

        public boolean equals(Object o) {
            if (!(o instanceof DBInbox.Row)) return false;
            DBInbox.Row a = (DBInbox.Row) o;
            return inboxId == a.inboxId;
        }

        public SDO getSDO(QAppPlugin qapp) {
            try {
                ClassLoader cl = qapp.getClassLoader();
                String schemaClassName = ESDO.schemaClassName(qapp.packageName(), action);
                QDoc schema = QSchemaFactory.clCreate(schemaClassName, cl);
                QDoc doc = Quick.parseString(schema, message);
                SDO root = (SDO) doc.getRoot();
                return root;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public void setSDO(SDO sdo) {
            try {
                message = sdo.toXML();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void eval(Map prop, List args, ClassLoader cl) throws Exception {
            TreeMap np = new TreeMap();
            Collection values = getProperties().values();
            Iterator it = values.iterator();
            while (it.hasNext()) {
                SessionPropertyString sps = (SessionPropertyString) it.next();
                np.put(sps.key, sps.value);
            }
            Set set = prop.keySet();
            it = set.iterator();
            while (it.hasNext()) {
                Object key = it.next();
                if (!"war".equals(key) && !"indirectPath".equals(key)) {
                    Object value = prop.get(key);
                    np.put(key, value);
                }
            }
            np.put("description", description);
            np.put("sendDate", QConfig.getDisplayDate(openingDate));
            QAppPlugin qapp = QHome.getPlugin(prop);
            getSDO(qapp).eval(np, args, cl);
        }

        public void backup(DataOutputStream dos) throws Exception {
            dos.writeLong(openingDate.getTime());
            dos.writeLong(dateRecieved.getTime());
            backupString(dos, user.getName());
            backupString(dos, application.getName());
            backupString(dos, fromSystem.getUrl());
            backupString(dos, action);
            backupString(dos, message);
            backupString(dos, description);
            backupString(dos, properties);
            backupString(dos, gkey);
        }
    }
}
