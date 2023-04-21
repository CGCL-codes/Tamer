package com.incendiaryblue.cmslite.storage;

import com.incendiaryblue.appframework.AppConfig;
import com.incendiaryblue.appframework.ServerConfig;
import com.incendiaryblue.cmslite.Category;
import com.incendiaryblue.cmslite.Content;
import com.incendiaryblue.cmslite.LiveContent;
import com.incendiaryblue.cmslite.Node;
import com.incendiaryblue.cmslite.StructureItem;
import com.incendiaryblue.cmslite.VersionRef;
import com.incendiaryblue.cmslite.search.SearchHandler;
import com.incendiaryblue.storage.BusinessObject;
import com.incendiaryblue.storage.DatabaseStorageImpl;
import java.io.*;
import java.util.*;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Types;
import com.incendiaryblue.database.SQLDialect;

public abstract class LiveContentStorageImpl extends DatabaseStorageImpl {

    {
        sqlConstants.add("SELECT_OBJECT_SQL", "SELECT L.LIVE_CONTENT_ID, L.NAME, L.DESCRIPTION, L.OWNER_ID, CC.CATEGORY_ID, " + "L.VALID_DATE, L.INVALID_DATE, L.FOREVER, L.VERSION, L.LAST_MODIFIED, L.LAST_MODIFIED_BY, L.WORKFLOW_ID " + "FROM LIVE_CONTENT AS L, CATEGORY_CONTENT AS CC " + "WHERE CC.TYPE = " + Node.CONCRETE_CATEGORY + " AND L.LIVE_CONTENT_ID = ? AND CC.CONTENT_ID = L.LIVE_CONTENT_ID");
        sqlConstants.add("SELECT_OBJECT_XML_ONLY", "SELECT XML FROM " + "LIVE_CONTENT WHERE LIVE_CONTENT_ID = ?");
        sqlConstants.add("SELECT_PK_BY_CONTENT_PATH", "SELECT L.LIVE_CONTENT_ID " + "FROM LIVE_CONTENT AS L, CATEGORY_CONTENT AS CC " + "WHERE CC.TYPE = " + Node.CONCRETE_CATEGORY + " AND CC.CATEGORY_ID = ? AND CC.CONTENT_ID = L.LIVE_CONTENT_ID " + "AND L.NAME = ? AND " + "((L.VALID_DATE <= ? AND L.INVALID_DATE >= ?) OR FOREVER != 0)");
        sqlConstants.add("SELECT_PK_LIST_BY_CATEGORY", "SELECT CC.CONTENT_ID " + "FROM CATEGORY_CONTENT CC, LIVE_CONTENT L " + "WHERE CC.CONTENT_ID = L.LIVE_CONTENT_ID " + " AND CC.TYPE = " + Node.CONCRETE_CATEGORY + " AND CC.CATEGORY_ID = ? AND " + "((L.VALID_DATE <= ? AND L.INVALID_DATE >= ?) OR FOREVER != 0) " + "ORDER BY L.NAME");
        sqlConstants.add("SELECT_PK_LIST_BY_PUBLISH_DATE", "SELECT L.LIVE_CONTENT_ID " + "FROM WORKING_CONTENT W, LIVE_CONTENT L " + "WHERE W.WORKING_CONTENT_ID = L.LIVE_CONTENT_ID AND " + "W.LAST_PUBLISHED >= ? AND " + "((L.VALID_DATE <= ? AND L.INVALID_DATE >= ?) OR L.FOREVER != 0) " + "ORDER BY L.NAME");
        sqlConstants.add("INSERT_OBJECT", "INSERT INTO LIVE_CONTENT(LIVE_CONTENT_ID, NAME, DESCRIPTION, " + "LAST_MODIFIED, OWNER_ID, VALID_DATE, INVALID_DATE, FOREVER, VERSION, " + "LAST_MODIFIED_BY, WORKFLOW_ID) " + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        sqlConstants.add("UPDATE_OBJECT", "UPDATE LIVE_CONTENT SET NAME = ?, DESCRIPTION = ?, LAST_MODIFIED = ?, " + "OWNER_ID = ?, VALID_DATE = ?, INVALID_DATE = ?, FOREVER = ?, VERSION = ?, " + "LAST_MODIFIED_BY = ?, WORKFLOW_ID = ? WHERE LIVE_CONTENT_ID = ?");
        sqlConstants.add("DELETE_OBJECT", "DELETE FROM LIVE_CONTENT WHERE LIVE_CONTENT_ID = ?");
        sqlConstants.add("UPDATE_MODIFIED_BY", "UPDATE LIVE_CONTENT SET LAST_MODIFIED_BY = 1 WHERE LAST_MODIFIED_BY = ?");
        sqlConstants.add("UPDATED_OWNED_BY", "UPDATE LIVE_CONTENT SET OWNER_ID = 1 WHERE OWNER_ID = ?");
        sqlConstants.add("SELECT_INT_FIELD_CONTENT", "SELECT STRUCTURE_ITEM_ID, DATA " + "FROM LC_DATA_INT " + "WHERE CONTENT_ID = ?;");
        sqlConstants.add("SELECT_DATE_FIELD_CONTENT", "SELECT STRUCTURE_ITEM_ID, DATA " + "FROM LC_DATA_DATE " + "WHERE CONTENT_ID = ?;");
        sqlConstants.add("SELECT_FLOAT_FIELD_CONTENT", "SELECT STRUCTURE_ITEM_ID, DATA " + "FROM LC_DATA_FLOAT " + "WHERE CONTENT_ID = ?;");
        sqlConstants.add("SELECT_VARCHAR_FIELD_CONTENT", "SELECT STRUCTURE_ITEM_ID, DATA " + "FROM LC_DATA_VARCHAR " + "WHERE CONTENT_ID = ?;");
        sqlConstants.add("DELETE_VARCHAR_CONTENT", "DELETE FROM LC_DATA_VARCHAR WHERE CONTENT_ID = ?");
        sqlConstants.add("DELETE_TEXT_CONTENT", "DELETE FROM LC_DATA_TEXT WHERE CONTENT_ID = ?");
        sqlConstants.add("DELETE_INT_CONTENT", "DELETE FROM LC_DATA_INT WHERE CONTENT_ID = ?");
        sqlConstants.add("DELETE_FLOAT_CONTENT", "DELETE FROM LC_DATA_FLOAT WHERE CONTENT_ID = ?");
        sqlConstants.add("DELETE_DATE_CONTENT", "DELETE FROM LC_DATA_DATE WHERE CONTENT_ID = ?");
        sqlConstants.add("DELETE_VARCHAR_FIELD", "DELETE FROM LC_DATA_VARCHAR WHERE STRUCTURE_ITEM_ID = ?");
        sqlConstants.add("DELETE_TEXT_FIELD", "DELETE FROM LC_DATA_TEXT WHERE STRUCTURE_ITEM_ID = ?");
        sqlConstants.add("DELETE_INT_FIELD", "DELETE FROM LC_DATA_INT WHERE STRUCTURE_ITEM_ID = ?");
        sqlConstants.add("DELETE_FLOAT_FIELD", "DELETE FROM LC_DATA_FLOAT WHERE STRUCTURE_ITEM_ID = ?");
        sqlConstants.add("DELETE_DATE_FIELD", "DELETE FROM LC_DATA_DATE WHERE STRUCTURE_ITEM_ID = ?");
        sqlConstants.add("INSERT_VARCHAR_FIELD", "INSERT INTO LC_DATA_VARCHAR (CONTENT_ID, STRUCTURE_ITEM_ID, DATA) " + "VALUES (?, ?, ?)");
        sqlConstants.add("INSERT_DATE_FIELD", "INSERT INTO LC_DATA_DATE (CONTENT_ID, STRUCTURE_ITEM_ID, DATA) " + "VALUES (?, ?, ?)");
        sqlConstants.add("INSERT_INT_FIELD", "INSERT INTO LC_DATA_INT (CONTENT_ID, STRUCTURE_ITEM_ID, DATA) " + "VALUES (?, ?, ?)");
        sqlConstants.add("INSERT_FLOAT_FIELD", "INSERT INTO LC_DATA_FLOAT (CONTENT_ID, STRUCTURE_ITEM_ID, DATA) " + "VALUES (?, ?, ?)");
        sqlConstants.add("INSERT_TEXT_FIELD", "INSERT INTO LC_DATA_TEXT (CONTENT_ID, STRUCTURE_ITEM_ID) " + "VALUES (?, ?)");
    }

    private static final String CONTENT_CREATE_FAILED_MSG = "Could not create content item, due to following: %";

    private static boolean TRANSACTIONS_ENABLED = (ServerConfig.get("db_enable_transactions") != null) && (ServerConfig.get("db_enable_transactions").equalsIgnoreCase("true"));

    private SearchHandler searchHandler = (SearchHandler) AppConfig.getDefaultComponent(SearchHandler.class);

    public BusinessObject getObject(Object primaryKey, Connection connection) throws SQLException {
        SQLDialect dialect = getDatabase().getSQLDialect();
        PreparedStatement statement = connection.prepareStatement(sqlConstants.get("SELECT_OBJECT_SQL"));
        try {
            ResultSet resultSet;
            int i;
            statement.setObject(1, primaryKey);
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            int contentID;
            String name;
            String description;
            int ownerID;
            Integer categoryID;
            Date validDate;
            Date invalidDate;
            boolean validForever;
            VersionRef version;
            Date lastModified;
            Integer modifiedBy;
            Integer workflowID;
            i = 0;
            contentID = resultSet.getInt(++i);
            name = resultSet.getString(++i);
            description = resultSet.getString(++i);
            ownerID = resultSet.getInt(++i);
            categoryID = new Integer(resultSet.getInt(++i));
            validDate = dialect.getDate(resultSet, ++i);
            invalidDate = dialect.getDate(resultSet, ++i);
            validForever = resultSet.getInt(++i) != 0;
            version = VersionRef.parse(resultSet.getString(++i));
            lastModified = dialect.getDate(resultSet, ++i);
            modifiedBy = (Integer) resultSet.getObject(++i);
            workflowID = (Integer) resultSet.getObject(++i);
            statement.close();
            LiveContent liveContent = new LiveContent(contentID, name, description, lastModified, ownerID, categoryID, validDate, invalidDate, validForever, version, modifiedBy, workflowID);
            try {
                liveContent.setFieldMap(loadFieldMap(primaryKey, connection));
            } catch (IOException e) {
                throw new SQLException("Error reading from text field: " + e);
            }
            return liveContent;
        } finally {
            statement.close();
        }
    }

    public Object getPrimaryKey(String keyName, Object keyValue, Connection connection) throws SQLException {
        SQLDialect dialect = getDatabase().getSQLDialect();
        Object primaryKey = null;
        PreparedStatement statement = null;
        if (keyName.equals(Content.QUERY_BY_CONTENT_PATH)) {
            Content.ContentItemKey contentPath = (Content.ContentItemKey) keyValue;
            Category category = contentPath.getCategory();
            String contentName = contentPath.getContentName();
            Object categoryID = category.getPrimaryKey();
            Date now = new Date();
            statement = connection.prepareStatement(sqlConstants.get("SELECT_PK_BY_CONTENT_PATH"));
            int i = 0;
            statement.setObject(++i, categoryID);
            statement.setString(++i, contentName);
            dialect.setDate(statement, ++i, now);
            dialect.setDate(statement, ++i, now);
        } else throw new IllegalArgumentException("Unknown key: " + keyName);
        try {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) primaryKey = new Integer(resultSet.getInt(1));
            resultSet.close();
        } finally {
            statement.close();
        }
        return primaryKey;
    }

    public List getPrimaryKeyList(String keyName, Object keyValue, Connection connection) throws SQLException {
        SQLDialect dialect = getDatabase().getSQLDialect();
        List primaryKeyList = new ArrayList();
        if (keyName.equals(Content.QUERY_BY_CATEGORY)) {
            PreparedStatement statement = connection.prepareStatement(sqlConstants.get("SELECT_PK_LIST_BY_CATEGORY"));
            try {
                Category category = (Category) keyValue;
                Object categoryID = category.getPrimaryKey();
                ResultSet resultSet;
                Date now = new Date();
                int i = 0;
                statement.setObject(++i, categoryID);
                dialect.setDate(statement, ++i, now);
                dialect.setDate(statement, ++i, now);
                resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    int contentID = resultSet.getInt("CONTENT_ID");
                    primaryKeyList.add(new Integer(contentID));
                }
            } finally {
                statement.close();
            }
        } else if (keyName.equals(Content.QUERY_BY_PUBLISH_DATE)) {
            Date d = (Date) keyValue;
            Date now = new Date();
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement(sqlConstants.get("SELECT_PK_LIST_BY_PUBLISH_DATE"));
                dialect.setDate(ps, 1, d);
                dialect.setDate(ps, 2, now);
                dialect.setDate(ps, 3, now);
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    primaryKeyList.add(new Integer(resultSet.getInt(1)));
                }
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } else throw new IllegalArgumentException("Unknown key: " + keyName);
        return primaryKeyList;
    }

    public void store(BusinessObject businessObject, Connection connection) throws SQLException {
        SQLDialect dialect = getDatabase().getSQLDialect();
        Content content = (Content) businessObject;
        Object categoryID = content.getCategory().getPrimaryKey();
        Object contentID = null;
        int contentState = content.getState();
        PreparedStatement statement = null;
        Date modifyDate = content.getModifyDate();
        Date validDate = content.getValidDate();
        Date invalidDate = content.getInvalidDate();
        boolean validForever = content.isValidForever();
        VersionRef version = content.getVersion();
        Integer workflowID = content.getWorkflowId();
        int i;
        if (validDate == null) validDate = new Date();
        if (invalidDate == null) invalidDate = new Date();
        i = 0;
        try {
            if (content.getState() == BusinessObject.STATE_CREATED) {
                statement = connection.prepareStatement(sqlConstants.get("INSERT_OBJECT"));
                statement.setInt(++i, ((Integer) content.getPrimaryKey()).intValue());
            } else {
                statement = connection.prepareStatement(sqlConstants.get("UPDATE_OBJECT"));
            }
            statement.setString(++i, content.getName());
            statement.setString(++i, content.getDescription());
            dialect.setDate(statement, ++i, modifyDate);
            statement.setObject(++i, content.getOwnerID());
            dialect.setDate(statement, ++i, validDate);
            dialect.setDate(statement, ++i, invalidDate);
            statement.setInt(++i, validForever ? 1 : 0);
            statement.setString(++i, version.toString());
            statement.setObject(++i, content.getModifiedBy());
            if (workflowID != null) statement.setObject(++i, workflowID); else statement.setNull(++i, Types.INTEGER);
            if (contentState != BusinessObject.STATE_CREATED) {
                contentID = content.getPrimaryKey();
                statement.setObject(++i, contentID);
            }
            statement.executeUpdate();
            storeFieldMap(content, connection);
            content.setState(BusinessObject.STATE_STORED);
        } finally {
            if (statement != null) statement.close();
        }
    }

    private void storeFieldMap(Content c, Connection conn) throws SQLException {
        SQLDialect dialect = getDatabase().getSQLDialect();
        if (TRANSACTIONS_ENABLED) {
            conn.setAutoCommit(false);
        }
        try {
            Object thisKey = c.getPrimaryKey();
            deleteFieldContent(thisKey, conn);
            PreparedStatement ps = null;
            StructureItem nextItem;
            Map fieldMap = c.getFieldMap();
            String type;
            Object value, siKey;
            for (Iterator i = c.getStructure().getStructureItems().iterator(); i.hasNext(); ) {
                nextItem = (StructureItem) i.next();
                type = nextItem.getDataType().toUpperCase();
                siKey = nextItem.getPrimaryKey();
                value = fieldMap.get(nextItem.getName());
                if (type.equals(StructureItem.DATE)) {
                    ps = conn.prepareStatement(sqlConstants.get("INSERT_DATE_FIELD"));
                    ps.setObject(1, thisKey);
                    ps.setObject(2, siKey);
                    dialect.setDate(ps, 3, (java.util.Date) value);
                    ps.executeUpdate();
                } else if (type.equals(StructureItem.INT) || type.equals(StructureItem.FLOAT) || type.equals(StructureItem.VARCHAR)) {
                    ps = conn.prepareStatement(sqlConstants.get("INSERT_" + type + "_FIELD"));
                    ps.setObject(1, thisKey);
                    ps.setObject(2, siKey);
                    if (value != null) {
                        ps.setObject(3, value);
                    } else {
                        int sqlType = Types.INTEGER;
                        if (type.equals(StructureItem.FLOAT)) {
                            sqlType = Types.FLOAT;
                        } else if (type.equals(StructureItem.VARCHAR)) {
                            sqlType = Types.VARCHAR;
                        }
                        ps.setNull(3, sqlType);
                    }
                    ps.executeUpdate();
                } else if (type.equals(StructureItem.TEXT)) {
                    setTextField(c, siKey, (String) value, conn);
                }
                if (ps != null) {
                    ps.close();
                    ps = null;
                }
            }
            if (TRANSACTIONS_ENABLED) {
                conn.commit();
            }
        } catch (SQLException e) {
            if (TRANSACTIONS_ENABLED) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (TRANSACTIONS_ENABLED) {
                conn.setAutoCommit(true);
            }
        }
    }

    private Map loadFieldMap(Object primaryKey, Connection conn) throws SQLException, IOException {
        SQLDialect dialect = getDatabase().getSQLDialect();
        Collection textFieldKeys = new ArrayList();
        Map fields = new HashMap();
        PreparedStatement st = null;
        ResultSet rs;
        Integer id = null;
        String[] sqlStatementNames = { "SELECT_INT_FIELD_CONTENT", "SELECT_DATE_FIELD_CONTENT", "SELECT_FLOAT_FIELD_CONTENT", "SELECT_VARCHAR_FIELD_CONTENT" };
        int numStatements = sqlStatementNames.length;
        try {
            for (int i = 0; i < numStatements; i++) {
                String statementName = sqlStatementNames[i];
                st = conn.prepareStatement(sqlConstants.get(statementName));
                st.setObject(1, primaryKey);
                rs = st.executeQuery();
                while (rs.next()) {
                    Object value;
                    if (statementName.equals("SELECT_DATE_FIELD_CONTENT")) {
                        value = dialect.getDate(rs, 2);
                    } else {
                        value = rs.getObject(2);
                    }
                    StructureItem si = StructureItem.getStructureItem((Integer) rs.getObject(1));
                    if (si != null) {
                        fields.put(si.getName(), value);
                    }
                }
                st.close();
            }
            loadTextFields(primaryKey, conn, fields);
            return fields;
        } finally {
            if (st != null) st.close();
        }
    }

    private void deleteFieldContent(Object primaryKey, Connection conn) throws SQLException {
        PreparedStatement ps = null;
        String[] types = { "VARCHAR", "DATE", "INT", "FLOAT", "TEXT" };
        try {
            for (int i = 0; i < types.length; i++) {
                ps = conn.prepareStatement(sqlConstants.get("DELETE_" + types[i] + "_CONTENT"));
                ps.setObject(1, primaryKey);
                ps.executeUpdate();
                ps.close();
                ps = null;
            }
        } finally {
            if (ps != null) ps.close();
        }
    }

    public void delete(BusinessObject businessObject, Connection connection) throws SQLException {
        PreparedStatement statement = null;
        Object contentID = businessObject.getPrimaryKey();
        try {
            statement = connection.prepareStatement(sqlConstants.get("DELETE_OBJECT"));
            statement.setObject(1, contentID);
            statement.executeUpdate();
            statement.close();
            deleteFieldContent(contentID, connection);
        } finally {
            if (statement != null) statement.close();
        }
    }

    public void delete(String keyName, Object keyValue, Connection conn) throws SQLException {
        if (keyName.equals(Content.STRUCTURE_ITEM_DATA)) {
            PreparedStatement ps = null;
            try {
                Integer key = (Integer) keyValue;
                StructureItem si = StructureItem.getStructureItem(key);
                if (si != null) {
                    String query = sqlConstants.get("DELETE_" + si.getDataType() + "_FIELD");
                    ps = conn.prepareStatement(query);
                    ps.setInt(1, key.intValue());
                    ps.executeUpdate();
                }
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } else if (keyName.equals("UPDATE_MODIFIED_BY")) {
            PreparedStatement ps = null;
            Integer key = (Integer) keyValue;
            String query = sqlConstants.get("UPDATE_MODIFIED_BY");
            ps = conn.prepareStatement(query);
            ps.setInt(1, key.intValue());
            ps.executeUpdate();
            String query2 = sqlConstants.get("UPDATED_OWNED_BY");
            ps = conn.prepareStatement(query2);
            ps.setInt(1, key.intValue());
            ps.executeUpdate();
        } else {
            throw new IllegalArgumentException("Unknown key: '" + keyName + "'");
        }
    }

    public abstract void loadTextFields(Object primaryKey, Connection conn, Map fields) throws SQLException;

    public abstract String getTextField(Object contentId, Object structureItemId, Connection conn) throws SQLException;

    public abstract void setTextField(Content content, Object structureItemId, String value, Connection conn) throws SQLException;
}
