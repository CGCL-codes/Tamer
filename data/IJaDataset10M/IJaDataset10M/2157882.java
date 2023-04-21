package rapor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Date;

public class RaporEkle {

    protected String username = null;

    protected String password = null;

    protected String driverName = "com.ibm.db2.jcc.DB2Driver";

    protected String url = "jdbc:db2://niki.gsu.edu.tr:50000/ZEYNEP:retrieveMessagesFromServerOnGetMessage=true;";

    protected Connection con = null;

    protected UserContext ctx;

    protected String RaporTuru, HekimNo;

    protected int MuayeneId;

    protected java.sql.Date Tarih;

    public RaporEkle(int Id, String Tur, java.util.Date Tarih, String HNo) {
        this.MuayeneId = Id;
        this.RaporTuru = Tur;
        this.Tarih = new java.sql.Date(Tarih.getTime());
        this.HekimNo = HNo;
    }

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
            super(resultSet, 5);
        }

        public Cursor1(sqlj.runtime.profile.RTResultSet resultSet, int fetchSize, int maxRows) throws java.sql.SQLException {
            super(resultSet, fetchSize, maxRows, 5);
        }

        public int getCol1() throws java.sql.SQLException {
            return resultSet.getIntNoNull(1);
        }

        public int getCol2() throws java.sql.SQLException {
            return resultSet.getIntNoNull(2);
        }

        public int getCol3() throws java.sql.SQLException {
            return resultSet.getIntNoNull(3);
        }

        public java.sql.Date getCol4() throws java.sql.SQLException {
            return resultSet.getDate(4);
        }

        public String getCol5() throws java.sql.SQLException {
            return resultSet.getString(5);
        }
    }

    Cursor1 cursor1 = null;

    public boolean establishConnection() throws Exception {
        if (con == null || con.isClosed()) {
            Class.forName(getDriverName()).newInstance();
            if (username == null) {
                con = DriverManager.getConnection(url);
            } else {
                con = DriverManager.getConnection(url, username, password);
            }
            ctx = new UserContext(con);
        }
        return true;
    }

    /***************************************************************************
	 * Execute the database query
	 */
    public void execute() throws Exception {
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
                sqlj.runtime.profile.RTStatement __sJT_stmt = __sJT_execCtx.registerStatement(__sJT_connCtx, RaporEkle_SJProfileKeys.getKey(0), 0);
                try {
                    __sJT_stmt.setInt(1, MuayeneId);
                    __sJT_stmt.setString(2, RaporTuru);
                    __sJT_stmt.setDate(3, Tarih);
                    __sJT_stmt.setString(4, HekimNo);
                    __sJT_execCtx.executeUpdate();
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
        con.setAutoCommit(false);
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
	 * Set the database driver name
	 */
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    /***************************************************************************
	 * Set the database url
	 */
    public void setUrl(String url) {
        this.url = url;
    }

    /***************************************************************************
	 * Get RAPOR_RAPORID
	 * 
	 * @return return column RAPOR_RAPORID
	 */
    public int getRAPOR_RAPORID() throws SQLException {
        return cursor1.getCol1();
    }

    /***************************************************************************
	 * Get RAPOR_MUAYENEISLEMID
	 * 
	 * @return return column RAPOR_MUAYENEISLEMID
	 */
    public int getRAPOR_MUAYENEISLEMID() throws SQLException {
        return cursor1.getCol2();
    }

    /***************************************************************************
	 * Get RAPOR_RAPORTURU
	 * 
	 * @return return column RAPOR_RAPORTURU
	 */
    public int getRAPOR_RAPORTURU() throws SQLException {
        return cursor1.getCol3();
    }

    /***************************************************************************
	 * Get RAPOR_RAPORTARIHI
	 * 
	 * @return return column RAPOR_RAPORTARIHI
	 */
    public java.sql.Date getRAPOR_RAPORTARIHI() throws SQLException {
        return cursor1.getCol4();
    }

    /***************************************************************************
	 * Get RAPOR_HEKIMKIMLIKNO
	 * 
	 * @return return column RAPOR_HEKIMKIMLIKNO
	 */
    public String getRAPOR_HEKIMKIMLIKNO() throws SQLException {
        return cursor1.getCol5();
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
	 * Get the database driver name
	 * 
	 * @return String database driver manager name
	 */
    public String getDriverName() {
        return driverName;
    }

    /***************************************************************************
	 * Get the database url
	 * 
	 * @return String database url
	 */
    public String getUrl() {
        return url;
    }
}

class RaporEkle_SJProfileKeys {

    private java.lang.Object[] keys;

    private final sqlj.runtime.profile.Loader loader = sqlj.runtime.RuntimeContext.getRuntime().getLoaderForClass(getClass());

    private static RaporEkle_SJProfileKeys inst = null;

    public static java.lang.Object getKey(int keyNum) throws java.sql.SQLException {
        synchronized (rapor.RaporEkle_SJProfileKeys.class) {
            if (inst == null) {
                inst = new RaporEkle_SJProfileKeys();
            }
        }
        return inst.keys[keyNum];
    }

    private RaporEkle_SJProfileKeys() throws java.sql.SQLException {
        keys = new java.lang.Object[1];
        keys[0] = RaporEkle.UserContext.getProfileKey(loader, "rapor.RaporEkle_SJProfile0");
    }
}
