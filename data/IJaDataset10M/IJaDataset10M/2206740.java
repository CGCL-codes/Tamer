package com.continuent.tungsten.manager.storage.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.continuent.tungsten.manager.common.ManagementException;
import com.continuent.tungsten.manager.common.ResourceDefinition;

/**
 * @author <a href="mailto:joe.daly@continuent.com">Joe Daly</a>
 * @version 1.0
 */
public class ResourceTable extends DatabaseConstants {

    static Logger logger = Logger.getLogger(ResourceTable.class);

    Connection dbConn = null;

    public ResourceTable(Connection dbConn) throws ManagementException {
        this.dbConn = dbConn;
        create();
    }

    public void initialize() throws ManagementException {
        CommonSQL.dropTable(RESOURCE_TABLE, dbConn);
        create();
    }

    private void create() throws ManagementException {
        if (CommonSQL.tableExists(RESOURCE_TABLE, dbConn)) {
            return;
        }
        try {
            Statement statement = dbConn.createStatement();
            StringBuilder builder = new StringBuilder(500);
            builder.append("CREATE TABLE ");
            builder.append(RESOURCE_TABLE);
            builder.append("( RESOURCENAME VARCHAR(255) NOT NULL PRIMARY KEY, ");
            builder.append(" MEMBERNAME VARCHAR(255), ");
            builder.append(" MEMBERUUID VARCHAR(255), ");
            builder.append(" HANDLERNAME VARCHAR(255)) ");
            if (logger.isDebugEnabled()) {
                logger.debug("creating table: " + builder.toString());
            }
            statement.execute(builder.toString());
            statement.close();
        } catch (SQLException e) {
            logger.error("Unable to create resource table", e);
            throw new ManagementException(ManagementException.Type.INITIALIZATION_ERROR, "Unable to create resource table", e);
        }
    }

    public void insert(ResourceDefinition rd) throws ManagementException {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Attempting to insert resource: " + rd.toString());
            }
            PreparedStatement prepStatement = dbConn.prepareStatement("INSERT INTO " + RESOURCE_TABLE + " VALUES(?,?,?,?)");
            prepStatement.setString(1, rd.getResourceName());
            prepStatement.setString(2, rd.getMemberName());
            prepStatement.setString(3, rd.getMemberUUID());
            prepStatement.setString(4, rd.getHandlerName());
            prepStatement.executeUpdate();
            prepStatement.close();
        } catch (SQLException se) {
            if (logger.isDebugEnabled()) {
                logger.debug("Unable to insert resource defintion: " + rd.toString());
            }
            logger.error("Unable to insert resource defintion", se);
            throw new ManagementException(ManagementException.Type.STORAGE_ERROR, "Unable to insert resource defintion", se);
        }
    }

    public List<ResourceDefinition> getAllResourceDefinitionsForMember(String memberUUID) throws ManagementException {
        try {
            Statement statement = dbConn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + RESOURCE_TABLE + " WHERE MEMBERUUID = '" + memberUUID + "'");
            List<ResourceDefinition> rds = constructResourceDefinitionsFromResultSet(resultSet);
            resultSet.close();
            statement.close();
            return rds;
        } catch (SQLException sqe) {
            logger.error("Unable to retreive definitions by member", sqe);
            throw new ManagementException(ManagementException.Type.STORAGE_ERROR, "Unable to retreive definitions by member", sqe);
        }
    }

    public List<ResourceDefinition> getAllResourceDefintions() throws ManagementException {
        try {
            Statement statement = dbConn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + RESOURCE_TABLE);
            List<ResourceDefinition> rds = constructResourceDefinitionsFromResultSet(resultSet);
            resultSet.close();
            statement.close();
            return rds;
        } catch (SQLException sqe) {
            logger.error("Unable to retreive definitions by member", sqe);
            throw new ManagementException(ManagementException.Type.STORAGE_ERROR, "Unable to retreive definitions by member", sqe);
        }
    }

    public int remove(String memberName) throws ManagementException {
        if (logger.isDebugEnabled()) {
            logger.debug("Attempting to delete all resource definitions for member: " + memberName);
        }
        try {
            Statement statement = dbConn.createStatement();
            int deletion = statement.executeUpdate("DELETE FROM " + RESOURCE_TABLE + " WHERE MEMBERNAME = '" + memberName + "'");
            statement.close();
            return deletion;
        } catch (SQLException sqe) {
            logger.error("Unable to delete all resource definitions for member: " + memberName, sqe);
            throw new ManagementException(ManagementException.Type.STORAGE_ERROR, "Unable to delete all resource definitions for member: " + memberName, sqe);
        }
    }

    public int remove(String resourceName, String memberUUID) throws ManagementException {
        if (logger.isDebugEnabled()) {
            logger.debug("Attempting to delete resource definition resourceName: " + resourceName + " memberUUID: " + memberUUID);
        }
        try {
            Statement statement = dbConn.createStatement();
            int deletion = statement.executeUpdate("DELETE FROM " + RESOURCE_TABLE + " WHERE RESOURCENAME = '" + resourceName + "' AND MEMBERUUID = '" + memberUUID + "'");
            statement.close();
            return deletion;
        } catch (SQLException sqe) {
            logger.error("Unable to delete resource definition resourceName " + resourceName + " memberUUID: " + memberUUID, sqe);
            throw new ManagementException(ManagementException.Type.STORAGE_ERROR, "Unable to delete resource definition resourceName " + resourceName + " memberUUID: " + memberUUID, sqe);
        }
    }

    public int remove(ResourceDefinition rd) throws ManagementException {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Attempting to delete resource definition: " + rd.toString());
            }
            Statement statement = dbConn.createStatement();
            int deletion = statement.executeUpdate("DELETE FROM " + RESOURCE_TABLE + " WHERE RESOURCENAME = '" + rd.getResourceName() + "'");
            statement.close();
            return deletion;
        } catch (SQLException sqe) {
            logger.warn("Unable to delete resource definition: " + rd.toString(), sqe);
            throw new ManagementException(ManagementException.Type.STORAGE_ERROR, "Unable to delete resource definition: " + rd.toString(), sqe);
        }
    }

    private List<ResourceDefinition> constructResourceDefinitionsFromResultSet(ResultSet resultSet) throws SQLException, ManagementException {
        List<ResourceDefinition> events = new ArrayList<ResourceDefinition>();
        while (resultSet.next()) {
            String resourceName = resultSet.getString(1);
            String memberName = resultSet.getString(2);
            String memberUUID = resultSet.getString(3);
            String handlerName = resultSet.getString(4);
            ResourceDefinition rd = new ResourceDefinition(handlerName, resourceName, memberName, memberUUID);
            events.add(rd);
        }
        return events;
    }
}
