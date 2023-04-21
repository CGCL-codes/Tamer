package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.AttributeExtractorFactroy;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtilsFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeHeavy;

/**
 * @author AYADI
 * 
 */
public class MySqlAdtAptAttributes extends AdtAptAttributes {

    /**
	 *
	 */
    public MySqlAdtAptAttributes(final int type) {
        super(type);
    }

    /**
     * This method registers a given attribute into the hdb database This method
     * does not take care of id parameter of the given attribute as this
     * parameter is managed in the database side (autoincrement).
     * 
     * @param attributeHeavy
     *            the attribute to register
     * @throws ArchivingException
     */
    private void registerInADT(final AttributeHeavy attributeHeavy) throws ArchivingException {
        final IDBConnection dbConn = ConnectionFactory.getInstance(archType);
        if (dbConn == null) {
            return;
        }
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        final String tableName = dbConn.getSchema() + "." + ConfigConst.TABS[0];
        String query = "INSERT INTO " + tableName + " (";
        for (int i = 1; i < ConfigConst.TAB_DEF.length - 1; i++) {
            query = query + ConfigConst.TAB_DEF[i] + ", ";
        }
        query = query + ConfigConst.TAB_DEF[ConfigConst.TAB_DEF.length - 1] + ")";
        query = query + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
        try {
            conn = dbConn.getConnection();
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setTimestamp(1, attributeHeavy.getRegistration_time());
            preparedStatement.setString(2, attributeHeavy.getAttribute_complete_name());
            preparedStatement.setString(3, attributeHeavy.getAttribute_device_name());
            preparedStatement.setString(4, attributeHeavy.getDomain());
            preparedStatement.setString(5, attributeHeavy.getFamily());
            preparedStatement.setString(6, attributeHeavy.getMember());
            preparedStatement.setString(7, attributeHeavy.getAttribute_name());
            preparedStatement.setInt(8, attributeHeavy.getData_type());
            preparedStatement.setInt(9, attributeHeavy.getData_format());
            preparedStatement.setInt(10, attributeHeavy.getWritable());
            preparedStatement.setInt(11, attributeHeavy.getMax_dim_x());
            preparedStatement.setInt(12, attributeHeavy.getMax_dim_y());
            preparedStatement.setInt(13, attributeHeavy.getLevel());
            preparedStatement.setString(14, attributeHeavy.getCtrl_sys());
            preparedStatement.setInt(15, attributeHeavy.getArchivable());
            preparedStatement.setInt(16, attributeHeavy.getSubstitute());
            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            String message = "";
            if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE) || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1) {
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
            } else {
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;
            }
            final String reason = GlobalConst.QUERY_FAILURE;
            final String desc = "Failed while executing MySqlAdtAptAttributes.registerInADT() method...";
            throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass().getName(), e);
        } finally {
            ConnectionCommands.close(preparedStatement);
            dbConn.closeConnection(conn);
        }
    }

    /**
     * This method registers a given attribute into the hdb database It inserts
     * a record in the "Attribute Properties Table" <I>(mySQL only)</I> This
     * method does not take care of id parameter of the given attribute as this
     * parameter is managed in the database side (autoincrement).
     * 
     * @param attributeHeavy
     *            the attribute to register
     * @throws ArchivingException
     */
    private void registerInAPT(final int id, final AttributeHeavy attributeHeavy) throws ArchivingException {
        final IDBConnection dbConn = ConnectionFactory.getInstance(archType);
        if (dbConn == null) {
            return;
        }
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        final String tableName = dbConn.getSchema() + "." + ConfigConst.TABS[1];
        String insertQuery;
        insertQuery = "INSERT INTO " + tableName + " (";
        for (int i = 0; i < ConfigConst.TAB_PROP.length - 1; i++) {
            insertQuery = insertQuery + ConfigConst.TAB_PROP[i] + ", ";
        }
        insertQuery = insertQuery + ConfigConst.TAB_PROP[ConfigConst.TAB_PROP.length - 1] + ")";
        insertQuery = insertQuery + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?) ";
        try {
            conn = dbConn.getConnection();
            preparedStatement = conn.prepareStatement(insertQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.setTimestamp(2, attributeHeavy.getRegistration_time());
            String description = attributeHeavy.getDescription();
            final int maxDescriptionLength = 255;
            if (description != null && description.length() > maxDescriptionLength) {
                description = description.substring(0, maxDescriptionLength);
            }
            preparedStatement.setString(3, description);
            preparedStatement.setString(4, attributeHeavy.getLabel());
            preparedStatement.setString(5, attributeHeavy.getUnit());
            preparedStatement.setString(6, attributeHeavy.getStandard_unit());
            preparedStatement.setString(7, attributeHeavy.getDisplay_unit());
            preparedStatement.setString(8, attributeHeavy.getFormat());
            preparedStatement.setString(9, attributeHeavy.getMin_value());
            preparedStatement.setString(10, attributeHeavy.getMax_value());
            preparedStatement.setString(11, attributeHeavy.getMin_alarm());
            preparedStatement.setString(12, attributeHeavy.getMax_alarm());
            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            String message = "";
            if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE) || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1) {
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
            } else {
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;
            }
            final String reason = GlobalConst.QUERY_FAILURE;
            final String desc = "Failed while executing MySqlAdtAptAttributes.registerInAPT() method...";
            throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass().getName(), e);
        } finally {
            ConnectionCommands.close(preparedStatement);
            dbConn.closeConnection(conn);
        }
    }

    @Override
    protected String getRequest(final String attributeName) throws ArchivingException {
        final IDBConnection dbConn = ConnectionFactory.getInstance(archType);
        if (dbConn == null) {
            return null;
        }
        final String str = "SELECT * FROM " + dbConn.getSchema() + "." + ConfigConst.TABS[0] + " WHERE " + ConfigConst.TAB_DEF[2] + "='" + attributeName.trim() + "'";
        return str;
    }

    /**
     * This method registers a given attribute into the hdb database It inserts
     * a record in the "Attribute Definition Table" <I>(mySQL only)</I> This
     * methos does not take care of id parameter of the given attribute as this
     * parameter is managed in the database side (autoincrement).
     * 
     * @param attributeHeavy
     *            the attribute to register
     * @throws ArchivingException
     */
    @Override
    public void registerAttribute(final AttributeHeavy attributeHeavy) throws ArchivingException {
        registerInADT(attributeHeavy);
        final int id = ids.getAttID(attributeHeavy.getAttribute_complete_name());
        registerInAPT(id, attributeHeavy);
    }

    /**
     * test if attribute's table exist, otherwise, it creates a new table
     * 
     * @param attributeName
     * @throws ArchivingException
     */
    @Override
    public void createAttributeTableIfNotExist(final String attributeName, final AttributeHeavy attr) throws ArchivingException {
        final int id = ids.getAttID(attributeName);
        final String tableName = DbUtilsFactory.getInstance(archType).getTableName(id);
        if (!isTableExist(tableName)) {
            AttributeExtractorFactroy.getAttributeExtractor(archType).getDataGetters().buildAttributeTab(tableName, attr.getData_type(), attr.getData_format(), attr.getWritable());
        }
    }

    private boolean isTableExist(final String tableName) throws ArchivingException {
        final IDBConnection dbConn = ConnectionFactory.getInstance(archType);
        if (dbConn == null) {
            return false;
        }
        final String query = "SHOW TABLES LIKE " + "\"" + tableName + "\"";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        try {
            conn = dbConn.getConnection();
            stmt = conn.createStatement();
            rset = stmt.executeQuery(query);
            while (rset.next()) {
                return rset.getString(1).equalsIgnoreCase(tableName);
            }
        } catch (final SQLException e) {
            throw new ArchivingException(e);
        } finally {
            ConnectionCommands.close(stmt);
            ConnectionCommands.close(rset);
            dbConn.closeConnection(conn);
        }
        return false;
    }
}
