package mapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import domain.AbstractDomainObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Абстрактный маппер Содержит методы по работе с базой данных
 * User: serb Date: 20.03.2006 Time: 19:20:12
 */
public abstract class AbstractDBMapper {

    private static Connection conn = null;

    protected static Statement stmt = null;

    private static String databasePath = null;

    private static String user = "";

    private static String pwd = "";

    private static final int FETCH_SIZE = 1000;

    public static String getUser() {
        return user;
    }

    public static void setUser(final String user) {
        AbstractDBMapper.user = user;
    }

    public static String getPwd() {
        return pwd;
    }

    public static void setPwd(final String pwd) {
        AbstractDBMapper.pwd = pwd;
    }

    public static String getDatabasePath() {
        return databasePath;
    }

    public static void setDatabasePath(final String databasePath) {
        AbstractDBMapper.databasePath = databasePath;
    }

    protected abstract String getSelectSQL();

    protected abstract String getInsertSQL();

    protected abstract String getUpdateSQL();

    protected abstract PreparedStatement getSelectStatement(AbstractDomainObject domainObject, PreparedStatement ps) throws SQLException;

    protected abstract PreparedStatement getInsertStatement(AbstractDomainObject domainObject, PreparedStatement insertStatement) throws SQLException;

    protected abstract PreparedStatement getUpdateStatement(AbstractDomainObject domainObject, PreparedStatement updateStatement) throws SQLException;

    /**
     * Выполняет инициализацию подключения к базе данных
     */
    protected static synchronized void beginTransaction() throws SQLException {
        if (conn == null || conn.isClosed()) {
            final String connectionString = "jdbc:derby:" + getDatabasePath() + ";create=true";
            conn = DriverManager.getConnection(connectionString, user, pwd);
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            stmt.setFetchSize(FETCH_SIZE);
        }
    }

    protected static synchronized void endTransaction() throws SQLException {
        conn.commit();
    }

    /**
     * Функция запрашивает данные из БД(данные кешируются)
     *
     * @param sql запрос для поиска
     * @return Данные запроса
     * @throws SQLException
     */
    protected synchronized ResultSet doFind(final String sql) throws SQLException {
        beginTransaction();
        PreparedStatement ps = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
        conn.setTransactionIsolation(conn.TRANSACTION_READ_UNCOMMITTED);
        ps.setFetchSize(FETCH_SIZE);
        ResultSet rs = ps.executeQuery();
        rs.setFetchSize(FETCH_SIZE);
        return rs;
    }

    private synchronized ResultSet doSelect(final AbstractDomainObject domainObject) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(getSelectSQL());
        ps = getSelectStatement(domainObject, ps);
        ps.setFetchSize(FETCH_SIZE);
        return ps.executeQuery();
    }

    private synchronized void doInsert(final AbstractDomainObject domainObject) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(getInsertSQL());
        ps = getInsertStatement(domainObject, ps);
        ps.setFetchSize(FETCH_SIZE);
        ps.execute();
    }

    private synchronized void doUpdate(final AbstractDomainObject domainObject) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(getUpdateSQL());
        ps = getUpdateStatement(domainObject, ps);
        ps.setFetchSize(FETCH_SIZE);
        ps.execute();
    }

    /**
     * Функция выполняет коммит списка объектов
     *
     * @param map список объектов для комммита
     * @throws SQLException
     */
    protected void doCommit(List<? extends AbstractDomainObject> list) throws SQLException {
        beginTransaction();
        for (int count = 0; count < list.size(); ++count) {
            if (count % 500 == 0) {
                endTransaction();
                beginTransaction();
            }
            final AbstractDomainObject domainObject = list.get(count);
            if (domainObject == null) {
                continue;
            }
            if (domainObject.isChange()) {
                try {
                    if (doSelect(domainObject).next()) {
                        doUpdate(domainObject);
                    } else {
                        doInsert(domainObject);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                domainObject.setChange(Boolean.FALSE);
            }
        }
        endTransaction();
    }

    public static synchronized void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
            if (getDatabasePath() != null) {
                Connection tempconn = DriverManager.getConnection("jdbc:derby:" + getDatabasePath() + ";shutdown=true");
                tempconn.close();
            }
        } catch (SQLException ex) {
            if (!ex.getMessage().contains("shutdown")) {
                ex.printStackTrace();
            }
        }
    }
}
