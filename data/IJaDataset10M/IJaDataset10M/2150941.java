package ispyb.server.data.ejb;

import ispyb.common.util.Constants;
import ispyb.server.util.ServerLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * @author LAUNER
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ProteinFullDAO {

    private static final String UPDATE_PROPOSALID_STATEMENT = " UPDATE Protein  set proposalId = ?" + " WHERE proposalId = ?";

    private static ProteinFullDAO instance = new ProteinFullDAO();

    private static DataSource ds = null;

    private ProteinFullDAO() {
    }

    public static ProteinFullDAO getInstance() {
        return ProteinFullDAO.instance;
    }

    public int updateProposalId(Integer newProposalId, Integer oldProposalId) throws SQLException {
        PreparedStatement pst = null;
        int rs = 0;
        Connection connection = getConnection();
        try {
            pst = connection.prepareStatement(UPDATE_PROPOSALID_STATEMENT);
            pst.setInt(1, newProposalId.intValue());
            pst.setInt(2, oldProposalId.intValue());
            rs = pst.executeUpdate();
            return rs;
        } finally {
            closeConnection(null, pst, connection);
        }
    }

    private static Connection getConnection() {
        try {
            if (ds == null) {
                Context initCtx = new InitialContext();
                ds = (DataSource) initCtx.lookup(Constants.getProperty("ISPyB.dbJndiName"));
            }
            return ds.getConnection();
        } catch (Exception e) {
            ServerLogger.getInstance().error("Could not lookup datasource '" + Constants.getProperty("ISPyB.dbJndiName") + "' ", e);
            throw new RuntimeException("Could not lookup datasource", e);
        }
    }

    public static final void closeConnection(ResultSet res, Statement stmt, Connection connection) {
        try {
            if (res != null) {
                res.close();
            }
        } catch (Exception e) {
            ServerLogger.getInstance().error("Could not close result set - ignoring", e);
        }
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (Exception e) {
            ServerLogger.getInstance().error("Could not close statement - ignoring", e);
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            ServerLogger.getInstance().error("Could not close connection - ignoring", e);
        }
    }

    public static final Integer getInteger(ResultSet rs, String colName) throws SQLException {
        int val = rs.getInt(colName);
        if (rs.wasNull()) {
            return null;
        }
        return new Integer(val);
    }

    public static final Double getDouble(ResultSet rs, String colName) throws SQLException {
        double val = rs.getDouble(colName);
        if (rs.wasNull()) {
            return null;
        }
        return new Double(val);
    }

    public static final Byte getByte(ResultSet rs, String colName) throws SQLException {
        byte val = rs.getByte(colName);
        if (rs.wasNull()) {
            return null;
        }
        return new Byte(val);
    }
}
