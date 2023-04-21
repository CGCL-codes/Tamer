package se.ucr.openqregdemo.bean.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.TreeMap;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import se.ucr.db.DbHandler;
import se.ucr.db.PersistentTransactionBean;
import se.ucr.db.PrimaryKey;
import se.ucr.openqregdemo.bean.Evregistration;
import se.ucr.openqregdemo.bean.EvregistrationKey;

/**
* This is the beanBase class for the evregistration table.
* Do NOT add or alter any code in this class. */
public abstract class EvregistrationBeanBase extends Evregistration implements PersistentTransactionBean {

    /**
 * log is a Log4J Logger
 */
    private static final Logger log = Logger.getLogger(EvregistrationBeanBase.class);

    /**
 * SELECT_STATEMENT is a String with a sql as a preparedstatement that retrives
 * the class values from the database
 */
    public static final String SELECT_STATEMENT = "SELECT MCEID, VAR, VAL FROM evregistration WHERE MCEID = ? AND VAR = ?";

    /**
 * INSERT_STATEMENT is a String with a sql as an preparedstatement that creates
 * the class values in the database
 */
    public static final String INSERT_STATEMENT = "INSERT INTO evregistration(MCEID, VAR, VAL) VALUES(?, ?, ?)";

    /**
 * UPDATE_STATEMENT is a String with a sql as an preparedstatement that creates
 * the class values in the database
 */
    public static final String UPDATE_STATEMENT = "UPDATE evregistration SET VAL = ? WHERE MCEID = ? AND VAR = ?";

    /**
 * DELETE_STATEMENT is a String with a sql as a preparedstatement that retrives
 * the class values from the database
 */
    public static final String DELETE_STATEMENT = "DELETE  FROM evregistration WHERE MCEID = ? AND VAR = ?";

    /**
 * @return The primaryKey
 * 
 */
    public PrimaryKey getPrimaryKey() {
        return new EvregistrationKey(getMceid(), getVar());
    }

    /**
* @see se.ucr.db.PersistentBean#create()
*/
    public void create() throws SQLException {
        Connection con = null;
        try {
            con = DbHandler.getConnection();
            create(con);
        } finally {
            if (null != con) {
                con.close();
                con = null;
            }
        }
    }

    /**
* @see se.ucr.db.PersistentTransactionBean#create(java.sql.Connection)
*/
    public final int create(Connection con) throws SQLException {
        beforeCreate(con);
        PreparedStatement pStmt = null;
        try {
            pStmt = con.prepareStatement(INSERT_STATEMENT);
            pStmt.setObject(1, getMceid());
            pStmt.setObject(2, getVar());
            pStmt.setObject(3, getVal());
            int rowcount = pStmt.executeUpdate();
            if (log.isDebugEnabled()) {
                log.log(Level.DEBUG, "Create: " + this.toString());
            }
            afterCreate(con);
            return rowcount;
        } finally {
            if (null != pStmt) {
                pStmt.close();
                pStmt = null;
            }
        }
    }

    protected abstract void beforeCreate(Connection con) throws SQLException;

    protected abstract void afterCreate(Connection con) throws SQLException;

    /**
* @see se.ucr.db.PersistentBean#store()
*/
    public void store() throws SQLException {
        Connection con = null;
        try {
            con = DbHandler.getConnection();
            store(con);
        } finally {
            if (null != con) {
                con.close();
                con = null;
            }
        }
    }

    /**
* @see se.ucr.db.PersistentTransactionBean#store(java.sql.Connection)
*/
    public final int store(Connection con) throws SQLException {
        beforeStore(con);
        PreparedStatement pStmt = null;
        try {
            pStmt = con.prepareStatement(UPDATE_STATEMENT);
            pStmt.setObject(1, getVal());
            pStmt.setObject(2, getMceid());
            pStmt.setObject(3, getVar());
            int rowcount = pStmt.executeUpdate();
            if (log.isDebugEnabled()) {
                log.log(Level.DEBUG, "Store: " + this.toString());
            }
            afterStore(con);
            return rowcount;
        } finally {
            if (null != pStmt) {
                pStmt.close();
                pStmt = null;
            }
        }
    }

    protected abstract void beforeStore(Connection con) throws SQLException;

    protected abstract void afterStore(Connection con) throws SQLException;

    /**
 * @see se.ucr.db.PersistentBean#remove()
 */
    public void remove() throws SQLException {
        Connection con = null;
        try {
            con = DbHandler.getConnection();
            remove(con);
        } finally {
            if (null != con) {
                con.close();
                con = null;
            }
        }
    }

    /**
 * @see se.ucr.db.PersistentTransactionBean#remove(java.sql.Connection)
 */
    public final int remove(Connection con) throws SQLException {
        beforeRemove(con);
        PreparedStatement pStmt = null;
        try {
            pStmt = con.prepareStatement(DELETE_STATEMENT);
            pStmt.setObject(1, getMceid());
            pStmt.setObject(2, getVar());
            int rowcount = pStmt.executeUpdate();
            if (log.isDebugEnabled()) {
                log.log(Level.DEBUG, "Remove: " + this.toString());
            }
            afterRemove(con);
            return rowcount;
        } finally {
            if (null != pStmt) {
                pStmt.close();
                pStmt = null;
            }
        }
    }

    protected abstract void beforeRemove(Connection con) throws SQLException;

    protected abstract void afterRemove(Connection con) throws SQLException;

    public abstract void afterPopulate(Connection con) throws SQLException;

    /**
* Retruns a TreeMap with all not null attributes as stringvalues
* @return a TreeMap with stringrepresentation of all not null attributes
*/
    public TreeMap<String, Object> getTreeMap() {
        TreeMap<String, Object> tr = new TreeMap<String, Object>();
        if (null != getMceid()) {
            tr.put("EVREGISTRATION_MCEID", getMceid().toString());
        }
        if (null != getVar()) {
            tr.put("EVREGISTRATION_VAR", getVar().toString());
        }
        if (null != getVal()) {
            tr.put("EVREGISTRATION_VAL", getVal().toString());
        }
        return tr;
    }
}
