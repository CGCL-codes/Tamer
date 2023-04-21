package com.safi.workshop.sqlexplorer.dbdetail.tab;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import com.safi.workshop.part.AsteriskDiagramEditorPlugin;
import com.safi.workshop.sqlexplorer.IConstants;
import com.safi.workshop.sqlexplorer.dataset.DataSet;
import com.safi.workshop.sqlexplorer.dbproduct.SQLConnection;
import com.safi.workshop.sqlexplorer.plugin.SQLExplorerPlugin;

public abstract class AbstractSQLTab extends AbstractDataSetTab {

    protected static final Logger _logger = Logger.getLogger(AbstractSQLTab.class.getName());

    @Override
    public final DataSet getDataSet() throws Exception {
        DataSet dataSet = null;
        int timeOut = AsteriskDiagramEditorPlugin.getDefault().getPluginPreferences().getInt(IConstants.INTERACTIVE_QUERY_TIMEOUT);
        SQLConnection connection = null;
        ResultSet rs = null;
        Statement stmt = null;
        PreparedStatement pStmt = null;
        try {
            connection = getNode().getSession().grabConnection();
            Object[] params = getSQLParameters();
            if (params == null || params.length == 0) {
                stmt = connection.createStatement();
                stmt.setQueryTimeout(timeOut);
                rs = stmt.executeQuery(getSQL());
            } else {
                pStmt = connection.prepareStatement(getSQL());
                pStmt.setQueryTimeout(timeOut);
                for (int i = 0; i < params.length; i++) {
                    if (params[i] instanceof String) {
                        pStmt.setString(i + 1, (String) params[i]);
                    } else if (params[i] instanceof Integer) {
                        pStmt.setInt(i + 1, ((Integer) params[i]).intValue());
                    } else if (params[i] instanceof String) {
                        pStmt.setLong(i + 1, ((Long) params[i]).longValue());
                    }
                }
                rs = pStmt.executeQuery();
            }
            dataSet = new DataSet(rs, null);
            rs.close();
            rs = null;
        } catch (Exception e) {
            SQLExplorerPlugin.error("Couldn't load source for: " + getNode().getName(), e);
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (SQLException e) {
                SQLExplorerPlugin.error("Error closing result set", e);
            }
            if (stmt != null) try {
                stmt.close();
            } catch (SQLException e) {
                SQLExplorerPlugin.error("Error closing statement", e);
            }
            if (pStmt != null) try {
                pStmt.close();
            } catch (SQLException e) {
                SQLExplorerPlugin.error("Error closing statement", e);
            }
            if (connection != null) getNode().getSession().releaseConnection(connection);
        }
        return dataSet;
    }

    @Override
    public abstract String getLabelText();

    public abstract String getSQL();

    public Object[] getSQLParameters() {
        return null;
    }
}
