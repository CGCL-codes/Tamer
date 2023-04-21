package blueprint4j.db;

import java.util.Vector;
import java.sql.SQLException;
import java.sql.Connection;
import blueprint4j.utils.*;

public class Database {

    public static final DatabaseTypeHSQL DBTYPE_HSSQL = new DatabaseTypeHSQL();

    public static final DatabaseTypeMYSQL DBTYPE_MYSQL = new DatabaseTypeMYSQL();

    public static final DatabaseTypeINFORMX DBTYPE_INFMX = new DatabaseTypeINFORMX();

    public static final DatabaseTypeSQLSVR DBTYPE_MSSQL = new DatabaseTypeSQLSVR();

    private VectorTable tables = new VectorTable();

    private DatabaseType connect_type = null;

    private DBPool dbpool = null;

    private DataBaseUtils dbutils = null;

    public Database(DatabaseType connect_type, String dbname, String username, String password) throws SQLException {
        this.connect_type = connect_type;
        if (connect_type == DBTYPE_HSSQL) {
            dbutils = new DataBaseUtilsHSQL();
        }
        if (connect_type == DBTYPE_MYSQL) {
            dbutils = new DataBaseUtilsMySQL();
        }
        if (connect_type == DBTYPE_INFMX) {
            dbutils = new DataBaseUtilsInformix();
        }
        if (connect_type == DBTYPE_MSSQL) {
            dbutils = new DataBaseUtilsMSSQL();
        }
    }

    public DBConnection getDC() throws DataException {
        return null;
    }

    public DBConnection getNC(boolean keepalive) throws DataException {
        return dbpool.get(keepalive);
    }

    /**
	 * Each database has tables
	 */
    public static class Table {

        private VectorMaintenance maintained = new VectorMaintenance();

        public static class Index {
        }

        public static class Maintenance {
        }

        public static class VectorMaintenance {

            public VectorMaintenance() {
            }

            private Vector store = new Vector();

            public Maintenance get(int pos) {
                return (Maintenance) store.get(pos);
            }

            public void add(Maintenance item) {
                store.add(item);
            }

            public boolean remove(Maintenance item) {
                return store.remove(item);
            }

            public Maintenance remove(int pos) {
                return (Maintenance) store.remove(pos);
            }

            public int size() {
                return store.size();
            }
        }
    }

    public static class VectorTable {

        private Vector store = new Vector();

        public Table get(int pos) {
            return (Table) store.get(pos);
        }

        public void add(Table item) {
            store.add(item);
        }

        public boolean remove(Table item) {
            return store.remove(item);
        }

        public Table remove(int pos) {
            return (Table) store.remove(pos);
        }

        public int size() {
            return store.size();
        }
    }

    public static class DatabaseType {
    }

    public static final class DatabaseTypeHSQL extends DatabaseType {

        public DatabaseTypeHSQL() {
        }
    }

    public static final class DatabaseTypeMYSQL extends DatabaseType {

        public DatabaseTypeMYSQL() {
        }
    }

    public static final class DatabaseTypeSQLSVR extends DatabaseType {

        public DatabaseTypeSQLSVR() {
        }
    }

    public static final class DatabaseTypeINFORMX extends DatabaseType {

        public DatabaseTypeINFORMX() {
        }
    }

    public static class DBPool implements ThreadScheduable {

        private static int keepopen = Settings.getInt("database.connection.open", 10);

        public VectorDBConnection dbcons = new VectorDBConnection();

        private String url = null, user = null, pass = null;

        public DBPool(String url, String user, String pass) {
            this.url = url;
            this.user = user;
            this.pass = pass;
        }

        public boolean keepAlive() {
            return true;
        }

        public int sleepTime() {
            return 10000;
        }

        public DBConnection get(boolean keepalive) {
            return null;
        }

        /**
		 * Cleanup all unused connections greater then level 
		 */
        public void process() throws Exception {
        }

        public void close() throws Exception {
        }
    }

    public static class DBConnection {

        private Connection connect = null;

        private boolean closed = false, autocommit = false;

        public DBConnection(String classForName, String url, String username, String password, boolean autocommit) throws SQLException {
            try {
                Class.forName(classForName);
            } catch (ClassNotFoundException cnfe) {
                cnfe.printStackTrace();
                throw new DataException("Error creating connection: " + cnfe.getMessage());
            }
            connect = java.sql.DriverManager.getConnection(url, username, password);
            this.autocommit = autocommit;
            connect.setAutoCommit(autocommit);
        }

        public void commit() throws SQLException {
            if (autocommit) {
                connect.commit();
            }
        }

        public void close() {
            closed = true;
        }

        public void open() {
            closed = true;
        }

        public boolean isClosed() {
            return closed;
        }

        protected void closeConnection() throws SQLException {
            if (!connect.isClosed()) {
                connect.close();
            }
        }

        static class VectorDBConnection {

            private Vector store = new Vector();

            public DBConnection get(int pos) {
                return (DBConnection) store.get(pos);
            }

            public void add(DBConnection item) {
                store.add(item);
            }

            public boolean remove(DBConnection item) {
                return store.remove(item);
            }

            public DBConnection remove(int pos) {
                return (DBConnection) store.remove(pos);
            }

            public DBConnection get() {
                synchronized (store) {
                    for (int i = 0; i < size(); i++) {
                        if (get(i).isClosed()) {
                            get(i).open();
                            return get(i);
                        }
                    }
                    return null;
                }
            }

            public void closeExtra(int keepopen) throws SQLException {
                synchronized (store) {
                    for (int i = 0; size() > keepopen && i < size(); i++) {
                        if (get(i).isClosed()) {
                            get(i).closeConnection();
                            remove(i--);
                        }
                    }
                }
            }

            public void close() throws SQLException {
                for (int i = 0; i < size(); i++) {
                    get(i).closeConnection();
                }
                store.clear();
            }

            public int size() {
                return store.size();
            }
        }
    }
}
