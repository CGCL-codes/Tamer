package sqlj;

import java.sql.Connection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.SQLException;

public class SqlEkipGoster {

    protected String username = null;

    protected String password = null;

    protected String dataSourceName = "MEDID";

    protected Connection con = null;

    protected UserContext ctx;

    public static class UserContext extends sqlj.runtime.ref.ConnectionContextImpl implements sqlj.runtime.ConnectionContext {

        private static java.util.Map m_typeMap = null;

        private static final sqlj.runtime.ref.ProfileGroup profiles = new sqlj.runtime.ref.ProfileGroup();

        private static UserContext defaultContext = null;

        public UserContext(java.sql.Connection conn) throws java.sql.SQLException {
            super(profiles, conn);
        }

        public UserContext(java.lang.String url, java.lang.String user, java.lang.String password, boolean autoCommit) throws java.sql.SQLException {
            super(profiles, url, user, password, autoCommit);
        }

        public UserContext(java.lang.String url, java.util.Properties info, boolean autoCommit) throws java.sql.SQLException {
            super(profiles, url, info, autoCommit);
        }

        public UserContext(java.lang.String url, boolean autoCommit) throws java.sql.SQLException {
            super(profiles, url, autoCommit);
        }

        public UserContext(sqlj.runtime.ConnectionContext other) throws java.sql.SQLException {
            super(profiles, other);
        }

        public static UserContext getDefaultContext() {
            if (defaultContext == null) {
                java.sql.Connection conn = sqlj.runtime.RuntimeContext.getRuntime().getDefaultConnection();
                if (conn != null) {
                    try {
                        defaultContext = new UserContext(conn);
                    } catch (java.sql.SQLException e) {
                    }
                }
            }
            return defaultContext;
        }

        public static void setDefaultContext(UserContext ctx) {
            defaultContext = ctx;
        }

        public static java.lang.Object getProfileKey(sqlj.runtime.profile.Loader loader, java.lang.String profileName) throws java.sql.SQLException {
            return profiles.getProfileKey(loader, profileName);
        }

        public static sqlj.runtime.profile.Profile getProfile(java.lang.Object profileKey) {
            return profiles.getProfile(profileKey);
        }

        public java.util.Map getTypeMap() {
            return m_typeMap;
        }
    }

    class Cursor1 extends sqlj.runtime.ref.ResultSetIterImpl implements sqlj.runtime.PositionedIterator {

        public Cursor1(sqlj.runtime.profile.RTResultSet resultSet) throws java.sql.SQLException {
            super(resultSet, 4);
        }

        public Cursor1(sqlj.runtime.profile.RTResultSet resultSet, int fetchSize, int maxRows) throws java.sql.SQLException {
            super(resultSet, fetchSize, maxRows, 4);
        }

        public int getCol1() throws java.sql.SQLException {
            return resultSet.getIntNoNull(1);
        }

        public int getCol2() throws java.sql.SQLException {
            return resultSet.getIntNoNull(2);
        }

        public String getCol3() throws java.sql.SQLException {
            return resultSet.getString(3);
        }

        public String getCol4() throws java.sql.SQLException {
            return resultSet.getString(4);
        }
    }

    Cursor1 cursor1 = null;

    public boolean establishConnection() throws Exception {
        if (con == null || con.isClosed()) {
            Context initCtx = new InitialContext();
            DataSource ds = (DataSource) initCtx.lookup(dataSourceName);
            if (username == null) {
                con = ds.getConnection();
            } else {
                con = ds.getConnection(username, password);
            }
            ctx = new UserContext(con);
        }
        return true;
    }

    /***************************************************************************
	 * Execute the database query
	 */
    public void execute(Integer ameliyatno) throws Exception {
        this.username = "db2inst1";
        this.password = "qwerty";
        establishConnection();
        System.out.println("Retrieve some data from the database.");
        {
            sqlj.runtime.ConnectionContext __sJT_connCtx = ctx;
            if (__sJT_connCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_CONN_CTX();
            sqlj.runtime.ExecutionContext __sJT_execCtx = __sJT_connCtx.getExecutionContext();
            if (__sJT_execCtx == null) sqlj.runtime.error.RuntimeRefErrors.raise_NULL_EXEC_CTX();
            synchronized (__sJT_execCtx) {
                sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, SqlEkipGoster_SJProfileKeys.getKey(0), 0);
                try {
                    __sJT_stmt.setIntWrapper(1, ameliyatno);
                    cursor1 = new Cursor1(__sJT_execCtx.executeQuery(), __sJT_execCtx.getFetchSize(), __sJT_execCtx.getMaxRows());
                } finally {
                    __sJT_execCtx.releaseStatement();
                }
            }
        }
    }

    /***************************************************************************
	 * Moves to the next row of the result set if it exsits
	 * 
	 * @return true if there is another row of data
	 */
    public boolean next() throws SQLException {
        return cursor1.next();
    }

    /***************************************************************************
	 * Closes Result Set
	 */
    public void close() throws SQLException {
        con.commit();
        if (ctx != null) {
            ctx.close();
        } else {
            con.close();
        }
    }

    /***************************************************************************
	 * Set the database username
	 */
    public void setUsername(String username) {
        this.username = username;
    }

    /***************************************************************************
	 * Set the database password
	 */
    public void setPassword(String password) {
        this.password = password;
    }

    /***************************************************************************
	 * Set the database data source name
	 */
    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    /***************************************************************************
	 * Get AMELIYAT_AMELIYATNO
	 * 
	 * @return return column AMELIYAT_AMELIYATNO
	 */
    public int getAMELIYAT_AMELIYATNO() throws SQLException {
        return cursor1.getCol1();
    }

    /***************************************************************************
	 * Get AMELIYAT_EKIPID
	 * 
	 * @return return column AMELIYAT_EKIPID
	 */
    public int getAMELIYAT_EKIPID() throws SQLException {
        return cursor1.getCol2();
    }

    /***************************************************************************
	 * Get EKIPDETAY_KIMLIKNO
	 * 
	 * @return return column EKIPDETAY_KIMLIKNO
	 */
    public String getROLREFERANS_ROLTANIMI() throws SQLException {
        return cursor1.getCol3();
    }

    /***************************************************************************
	 * Get EKIPDETAY_KIMLIKNO
	 * 
	 * @return return column EKIPDETAY_KIMLIKNO
	 */
    public String getEKIPDETAY_KIMLIKNO() throws SQLException {
        return cursor1.getCol4();
    }

    /***************************************************************************
	 * Get the database username
	 * 
	 * @return String database username
	 */
    public String getUsername() {
        return username;
    }

    /***************************************************************************
	 * Get the database data source name
	 * 
	 * @return String database data source name
	 */
    public String getDataSourceName() {
        return dataSourceName;
    }
}

class SqlEkipGoster_SJProfileKeys {

    private java.lang.Object[] keys;

    private final sqlj.runtime.profile.Loader loader = sqlj.runtime.RuntimeContext.getRuntime().getLoaderForClass(getClass());

    private static SqlEkipGoster_SJProfileKeys inst = null;

    public static java.lang.Object getKey(int keyNum) throws java.sql.SQLException {
        synchronized (sqlj.SqlEkipGoster_SJProfileKeys.class) {
            if (inst == null) {
                inst = new SqlEkipGoster_SJProfileKeys();
            }
        }
        return inst.keys[keyNum];
    }

    private SqlEkipGoster_SJProfileKeys() throws java.sql.SQLException {
        keys = new java.lang.Object[1];
        keys[0] = SqlEkipGoster.UserContext.getProfileKey(loader, "sqlj.SqlEkipGoster_SJProfile0");
    }
}
