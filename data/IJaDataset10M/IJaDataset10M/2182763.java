package pipboy.persistency.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import pipboy.persistency.core.IPersistentRecordManager;
import pipboy.persistency.core.PersistentRecordException;
import pipboy.persistency.core.impl.DefaultPersistentRecordManager;
import pipboy.persistency.dao.DaoUtils;
import pipboy.persistency.dao.IDaoTemplate;
import pipboy.persistency.dao.callback.ConnectionCallback;
import pipboy.persistency.dao.callback.ManageRecordCallback;
import pipboy.persistency.dao.callback.PreparedStatementCallback;
import pipboy.persistency.dao.callback.ResultSetCallback;
import pipboy.persistency.dao.callback.StatementCallback;
import pipboy.persistency.transaction.Transaction;

/**
 * @author Joard
 * @since 2007/09/03
 * @version 0.0.0.1
 *
 */
public class DefaultDaoTemplate implements IDaoTemplate {

    static Logger logger = Logger.getLogger(DefaultDaoTemplate.class);

    /**
	 * 获取数据库连接
	 * 默认从db.properties里获得数据库连接信息
	 * 并且优先通过jndi来获取数据源
	 * @return Connection
	 * @throws SQLException
	 * @throws PersistentRecordException
	 */
    private Connection getConnection() throws SQLException, PersistentRecordException {
        Properties db = new Properties();
        try {
            final InputStream dbStream = this.getClass().getClassLoader().getResourceAsStream("db.properties");
            db.load(dbStream);
            dbStream.close();
        } catch (IOException ioe) {
            throw new PersistentRecordException("Couldn't find db.properties!");
        }
        String jndiName = db.getProperty("jndiname");
        if (DaoUtils.isNullOrEmpty(jndiName)) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException cnfe) {
                throw new RuntimeException(cnfe);
            }
            String database = db.getProperty("database");
            String username = db.getProperty("username");
            String password = db.getProperty("password");
            String host = db.getProperty("host");
            String url = "jdbc:mysql://" + host + "/" + database;
            logger.debug("db url :" + url);
            return DriverManager.getConnection(url, username, password);
        } else {
            Context ctx;
            DataSource ds;
            try {
                ctx = new InitialContext();
                String lookup = "java:comp/env/" + jndiName;
                logger.debug("Context lookupStr :" + lookup);
                ds = (DataSource) ctx.lookup(lookup);
            } catch (NamingException ne) {
                throw new PersistentRecordException(ne);
            }
            return ds.getConnection();
        }
    }

    public Object execute(ConnectionCallback action) throws SQLException, PersistentRecordException {
        final Connection conn = getConnection();
        try {
            return action.doInConnection(conn);
        } finally {
            DaoUtils.closeConnection(conn);
        }
    }

    public Object execute(StatementCallback action) throws SQLException, PersistentRecordException {
        final Connection conn = getConnection();
        try {
            final Statement stmt = conn.createStatement();
            try {
                return action.doInStatement(stmt);
            } finally {
                DaoUtils.closeStatement(stmt);
            }
        } finally {
            DaoUtils.closeConnection(conn);
        }
    }

    public Object execute(PreparedStatementCallback action) throws SQLException, PersistentRecordException {
        final Connection conn = getConnection();
        try {
            final PreparedStatement stmt = conn.prepareStatement(action.getSQL());
            try {
                return action.doInPreparedStatement(stmt);
            } finally {
                DaoUtils.closeStatement(stmt);
            }
        } finally {
            DaoUtils.closeConnection(conn);
        }
    }

    public Object execute(ResultSetCallback action) throws SQLException, PersistentRecordException {
        final Connection conn = getConnection();
        try {
            final PreparedStatement stmt = conn.prepareStatement(action.getSQL());
            try {
                Object para;
                List params = action.getParamsList();
                for (int i = 0; i < params.size(); i++) {
                    para = params.get(i);
                    stmt.setObject((i + 1), para);
                }
                final ResultSet rs = stmt.executeQuery();
                try {
                    return action.doInResultSet(rs);
                } finally {
                    DaoUtils.closeResultSet(rs);
                }
            } finally {
                DaoUtils.closeStatement(stmt);
            }
        } finally {
            DaoUtils.closeConnection(conn);
        }
    }

    public Object manageRecord(ManageRecordCallback action) throws SQLException, PersistentRecordException {
        final Connection conn = getConnection();
        final Transaction trans = new Transaction(conn);
        try {
            trans.beginTransaction();
            final IPersistentRecordManager manager = new DefaultPersistentRecordManager(trans);
            Object result = action.doInPersistentRecordManager(manager);
            trans.commit();
            return result;
        } catch (SQLException se) {
            trans.rollback();
            throw se;
        } catch (PersistentRecordException pe) {
            trans.rollback();
            throw pe;
        } catch (Exception ex) {
            trans.rollback();
            throw new RuntimeException(ex);
        } finally {
            DaoUtils.closeConnection(conn);
        }
    }
}
