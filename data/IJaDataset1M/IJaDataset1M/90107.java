package genorm.runtime;

import java.util.*;
import javax.sql.DataSource;
import java.sql.*;

/**
	This class if for transactionless connections to the db
	All connections are associated with the statements and closed
	when the statement is closed
*/
public class GenOrmDudConnection implements GenOrmConnection {

    private GenOrmDSEnvelope m_envelope;

    public GenOrmDudConnection(GenOrmDSEnvelope dse) {
        m_envelope = dse;
    }

    /**
		Sets a property to be associated with this connection
	*/
    public void setProperty(String name, Object value) {
    }

    /**
		Gets a property set on this connection
	*/
    public Object getProperty(String name) {
        return (null);
    }

    /**
		Returns a unique record instance for this transaction.
		If the record is in the cache it returns the cached record otherwise the 
		record is added to the cache and returned
	*/
    public GenOrmRecord getUniqueRecord(GenOrmRecord rec) {
        return (rec);
    }

    /**
		Returns the cached record if it exists, null otherwise
	*/
    public GenOrmRecord getCachedRecord(GenOrmRecordKey key) {
        return (null);
    }

    boolean isInTransaction(GenOrmRecordKey key) {
        return (false);
    }

    /**
		Flush all modified records that are part of the current transaction
	*/
    public void flush() {
    }

    public void commit() {
    }

    public boolean isCommitted() {
        return (true);
    }

    public void close() {
    }

    public void rollback() {
    }

    public Connection getConnection() {
        return (null);
    }

    public GenOrmKeyGenerator getKeyGenerator(String table) {
        return (m_envelope.getKeyGenerator(table));
    }

    public boolean addToTransaction(GenOrmRecord goi) {
        return (false);
    }

    /**
		The connection will be closed when the statement is closed
	*/
    public Statement createStatement() throws SQLException {
        Connection con = m_envelope.getDataSource().getConnection();
        con.setAutoCommit(true);
        Statement stmt = con.createStatement();
        return (new GenOrmStatement(con, stmt));
    }

    /**
		The connection will be closed when the statement is closed
	*/
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        Connection con = m_envelope.getDataSource().getConnection();
        con.setAutoCommit(true);
        PreparedStatement stmt = con.prepareStatement(sql);
        return (new GenOrmPreparedStatement(con, stmt));
    }
}
