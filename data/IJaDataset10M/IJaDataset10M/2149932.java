package org.equanda.reporting.bean;

import org.apache.log4j.Logger;
import org.equanda.reporting.driver.RecordBatch;
import org.jboss.annotation.ejb.RemoteBinding;
import javax.ejb.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

/**
 * Get data for reporting, accessed by reporting driver
 *
 * @author NetRom team
 * @author <a href="mailto:joachim@progs.be">Joachim Van der Auwera</a>
 */
@Stateful
@Remote(ReporterEJB.class)
@RemoteBinding(jndiBinding = "org.equanda.reporting.bean.ReporterEJB")
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
public class ReporterBean implements ReporterEJB, Serializable {

    private transient HashMap<Integer, QueryInfo> map;

    private int size = 25;

    private transient Connection connection;

    private static final Logger log = Logger.getLogger(ReporterBean.class);

    private transient Random random = new Random();

    private Object lookup(InitialContext jndi, String name) {
        try {
            return jndi.lookup(name);
        } catch (NamingException ex) {
            return null;
        }
    }

    public int prepare(String query) {
        if (map == null) {
            map = new HashMap<Integer, QueryInfo>();
        }
        try {
            long start = System.currentTimeMillis();
            if (connection == null) {
                InitialContext jndi = new InitialContext();
                Integer intObj = (Integer) lookup(jndi, "Reporter/BatchSize");
                if (intObj != null) size = intObj;
                String driver = (String) lookup(jndi, "Reporter/Driver");
                if (driver == null) driver = "org.firebirdsql.jdbc.FBDriver";
                String url = (String) lookup(jndi, "Reporter/URL");
                if (url == null) url = "jdbc:firebirdsql:localhost/3050:/data/equanda-test.fdb";
                String user = (String) lookup(jndi, "Reporter/User");
                if (user == null) user = "SYSDBA";
                String password = (String) lookup(jndi, "Reporter/Password");
                if (password == null) password = "masterkey";
                jndi.close();
                Class.forName(driver);
                Properties properties = new Properties();
                properties.setProperty("user", user);
                properties.setProperty("password", password);
                if (log.isDebugEnabled()) log.debug("get connection with " + url);
                connection = DriverManager.getConnection(url, properties);
                connection.setAutoCommit(false);
                connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
                if (log.isDebugEnabled()) {
                    log.debug("connect " + ((System.currentTimeMillis() - start) / 1000.0) + 's');
                }
            }
            start = System.currentTimeMillis();
            PreparedStatement statement = connection.prepareStatement(query);
            Integer id = generateId();
            map.put(id, new QueryInfo(statement));
            if (log.isDebugEnabled()) {
                log.debug("prepare " + ((System.currentTimeMillis() - start) / 1000.0) + 's');
            }
            return id;
        } catch (NamingException ne) {
            log.error("Probable configuration problem", ne);
            throw new EJBException(ne);
        } catch (SQLException sqle) {
            log.error("Cannot prepare statement", sqle);
            throw new EJBException(sqle);
        } catch (ClassNotFoundException cnfe) {
            log.error("Cannot load driver class", cnfe);
            throw new EJBException(cnfe);
        }
    }

    public RecordBatch execute(int id) throws SQLException {
        long start = System.currentTimeMillis();
        if (map == null || !map.containsKey(id)) {
            throw new EJBException("invalid id " + id);
        }
        PreparedStatement statement;
        QueryInfo qi = map.get(id);
        statement = qi.statement;
        ResultSet rs = statement.executeQuery();
        qi.resultSet = rs;
        ResultSetMetaData rsmd = rs.getMetaData();
        ArrayList<String> fieldNames = new ArrayList<String>();
        qi.fieldCount = rsmd.getColumnCount();
        for (int i = 0; i < qi.fieldCount; i++) {
            fieldNames.add(rsmd.getColumnName(i + 1).toUpperCase().replace('.', '_'));
        }
        RecordBatch batch = nextRecords(qi, fieldNames);
        if (log.isDebugEnabled()) log.debug("execute " + ((System.currentTimeMillis() - start) / 1000.0) + 's');
        return batch;
    }

    public RecordBatch next(int id) throws SQLException {
        long start = System.currentTimeMillis();
        if (map == null || !map.containsKey(id)) {
            throw new EJBException("invalid id " + id);
        }
        RecordBatch batch = nextRecords(map.get(id), null);
        if (log.isDebugEnabled()) log.debug("next " + ((System.currentTimeMillis() - start) / 1000.0) + 's');
        return batch;
    }

    public void close(Integer id) {
        if (map == null || !map.containsKey(id)) {
            throw new EJBException("invalid id " + id);
        }
        QueryInfo info = map.remove(id);
        ResultSet rs = info.resultSet;
        PreparedStatement statement = info.statement;
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            log.error("Problem in ResultSet.close()", e);
        }
        try {
            if (statement != null) statement.close();
        } catch (SQLException e) {
            log.error("Problem in Statement.close()", e);
        }
        if (log.isDebugEnabled()) log.debug("map after close " + map.keySet());
    }

    public void close(int id) {
        close(new Integer(id));
    }

    public void close() {
        if (log.isDebugEnabled()) log.debug("close all resultsets");
        if (map != null) {
            Object[] ids = map.keySet().toArray();
            for (Object id : ids) close((Integer) id);
        }
    }

    public void setInt(int id, int i, int i1) throws SQLException {
        if (map == null || !map.containsKey(id)) {
            throw new EJBException("invalid id " + id);
        }
        PreparedStatement statement = (map.get(id)).statement;
        statement.setInt(i, i1);
    }

    public void setDouble(int id, int i, double v) throws SQLException {
        if (map == null || !map.containsKey(id)) {
            throw new EJBException("invalid id " + id);
        }
        PreparedStatement statement = (map.get(id)).statement;
        statement.setDouble(i, v);
    }

    public void setNull(int id, int i, int i1) throws SQLException {
        if (map == null || !map.containsKey(id)) {
            throw new EJBException("invalid id " + id);
        }
        PreparedStatement statement = (map.get(id)).statement;
        statement.setNull(i, i1);
    }

    public void setString(int id, int i, String s) throws SQLException {
        if (map == null || !map.containsKey(id)) {
            throw new EJBException("invalid id " + id);
        }
        PreparedStatement statement = (map.get(id)).statement;
        statement.setString(i, s);
    }

    public void setBoolean(int id, int i, boolean b) throws SQLException {
        if (map == null || !map.containsKey(id)) {
            throw new EJBException("invalid id " + id);
        }
        PreparedStatement statement = (map.get(id)).statement;
        statement.setBoolean(i, b);
    }

    public void setDate(int id, int i, Date date) throws SQLException {
        if (map == null || !map.containsKey(id)) {
            throw new EJBException("invalid id " + id);
        }
        PreparedStatement statement = (map.get(id)).statement;
        statement.setDate(i, date);
    }

    public void setTimestamp(int id, int i, Timestamp timestamp) throws SQLException {
        if (map == null || !map.containsKey(id)) {
            throw new EJBException("invalid id " + id);
        }
        PreparedStatement statement = (map.get(id)).statement;
        statement.setTimestamp(i, timestamp);
    }

    private RecordBatch nextRecords(QueryInfo qi, ArrayList<String> fieldNames) throws SQLException {
        ResultSet rs = qi.resultSet;
        ArrayList records = new ArrayList();
        RecordBatch batch = new RecordBatch(fieldNames, records);
        int k = 0;
        if (fieldNames != null) {
            if (!rs.next()) {
                batch.setEof();
            }
        }
        for (; !batch.getEof() && (k < size); k++) {
            Object[] obj = new Object[qi.fieldCount];
            for (int i = 0; i < qi.fieldCount; i++) {
                obj[i] = rs.getObject(i + 1);
            }
            records.add(obj);
            if (!rs.next()) {
                batch.setEof();
            }
        }
        return batch;
    }

    public void ejbPassivate() throws EJBException {
        if (log.isDebugEnabled()) log.debug("ejbPassivate called");
        remove();
        throw new EJBException("ReportBean is passivated, inactive for too long");
    }

    @Remove
    public void remove() {
        close();
        if (connection != null) {
            try {
                connection.commit();
            } catch (SQLException e) {
                log.error("Cannot commit", e);
            }
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                log.error("Cannot close", e);
            }
        }
    }

    public void ejbActivate() {
    }

    public void setSessionContext(SessionContext sessionContext) {
    }

    class QueryInfo {

        ResultSet resultSet;

        PreparedStatement statement;

        int fieldCount;

        public QueryInfo(PreparedStatement s) {
            statement = s;
        }
    }

    private Integer generateId() {
        Integer id = random.nextInt();
        while (map.containsKey(id)) {
            id = random.nextInt();
        }
        return id;
    }
}
