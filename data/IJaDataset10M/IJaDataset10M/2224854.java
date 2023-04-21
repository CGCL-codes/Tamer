package org.jlense.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
    Miscellaneous JDBC utilities.


    Contains methods for automatically creating SQL and statements
    from template objects.  Can create insert, update, delete, and
    query statements given a table name and a Class object associated
    with the table.
    Contains methods for automatically reading data from a ResultSet
    into a template object.
    These methods automatically map from the table to objects of the
    given class and from objects to the table.  This mapping scheme
    assumes that associated Class have public member fileds for each
    field in the table and that each table has a single key field named
    "id".



    Contains methods for automatically creating SQL and statements
    from Map objects that contain column keys and associated column
    values.  Can create insert, update, delete, and
    query statements given a Map filled with values keyed by column
    name.
    Contains methods for automatically reading ResultSet records
    into Maps of column values.


    @author ted stockwell
    @version $Revision: 1.1.1.1 $
*/
public class JDBCUtils {

    /**
    Reads values from the current record in the given ResultSet and
    fills the given object with the values.
    This method uses reflection to assign values to fields in the given
    template object.
    */
    public static void read(Object obj, ResultSet resultSet) throws SQLException {
        if (resultSet == null) return;
        Class objClass = obj.getClass();
        ResultSetMetaData metaData = resultSet.getMetaData();
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            String columnName = metaData.getColumnName(i);
            try {
                Field field = objClass.getDeclaredField(columnName);
                int modifier = field.getModifiers();
                if (!Modifier.isPublic(modifier)) continue;
                if (Modifier.isFinal(modifier)) continue;
                if (Modifier.isStatic(modifier)) continue;
                Object resultObject = resultSet.getObject(i);
                field.set(obj, resultObject);
            } catch (IllegalAccessException x) {
                x.printStackTrace();
            } catch (NoSuchFieldException x) {
                x.printStackTrace();
            }
        }
    }

    /**
    Returns SQL for creating a PreparedStatement object that
    inserts instances of the given class into a JDBC database.
    This method uses reflection to create a statement that includes
    every public data member in the given object.
    
    @param objClass     The class of object for which to generate SQL
    */
    public static String getInsertSQL(Class objClass, String tableName) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("INSERT INTO ");
        buffer.append(tableName);
        Field[] fields = objClass.getDeclaredFields();
        buffer.append(" (");
        int fieldCount = 0;
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            int modifier = field.getModifiers();
            if (!Modifier.isPublic(modifier)) continue;
            if (Modifier.isFinal(modifier)) continue;
            if (Modifier.isStatic(modifier)) continue;
            if (0 < i) buffer.append(',');
            buffer.append(field.getName());
            fieldCount++;
        }
        buffer.append(") VALUES (");
        for (int i = 0; i < fieldCount; i++) {
            if (0 < i) buffer.append(',');
            buffer.append('?');
        }
        buffer.append(')');
        String sql = buffer.toString();
        return sql;
    }

    public static PreparedStatement getInsertStatement(Class objClass, String tableName, Connection conn, Object object) throws SQLException {
        String sql = getInsertSQL(objClass, tableName);
        PreparedStatement statement = conn.prepareStatement(sql);
        fillInsertStatement(statement, objClass, object);
        return statement;
    }

    /**
    Fills a PreparedStatement with data values from the given object.
    This method uses reflection to iterate through the public data
    members of the given class and set the values in the given
    statement using values froom the given object.
    
    @param statement    The statement to be filled with values
    @param objClass     The class of object to be inserted
    @param object       The object which is to supply data values
    */
    public static void fillInsertStatement(PreparedStatement statement, Class objClass, Object object) throws SQLException {
        Field[] fields = objClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                Field field = fields[i];
                int modifier = field.getModifiers();
                if (!Modifier.isPublic(modifier)) continue;
                if (Modifier.isFinal(modifier)) continue;
                if (Modifier.isStatic(modifier)) continue;
                statement.setObject(i + 1, mapToSQLType(field.get(object)));
            } catch (IllegalAccessException x) {
                x.printStackTrace();
            }
        }
    }

    /**
    Returns SQL for creating a PreparedStatement object that
    updates instances of the given class in a JDBC database.
    This method uses reflection to create a statement that includes
    every public data member in the given object.
    
    This method assumes that the given class has only a single "key"
    field and that the key field is named "id".
    
    @param objClass The class of object for which to generate SQL
    */
    public static String getUpdateSQL(Class objClass, String tableName, String idFieldName) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("UPDATE ");
        buffer.append(tableName);
        buffer.append(" SET ");
        Field[] fields = objClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            int modifier = field.getModifiers();
            if (!Modifier.isPublic(modifier)) continue;
            if (Modifier.isFinal(modifier)) continue;
            if (Modifier.isStatic(modifier)) continue;
            String fieldName = field.getName();
            if (!fieldName.equalsIgnoreCase(idFieldName)) {
                if (0 < i) buffer.append(',');
                buffer.append(fieldName);
                buffer.append("=?");
            }
        }
        buffer.append(" WHERE ");
        buffer.append(idFieldName);
        buffer.append("=?");
        String sql = buffer.toString();
        return sql;
    }

    public static PreparedStatement getUpdateStatement(Class objClass, String tableName, Connection conn, Object object, String idFieldName) throws SQLException {
        String sql = getUpdateSQL(objClass, tableName, idFieldName);
        PreparedStatement statement = conn.prepareStatement(sql);
        fillUpdateStatement(statement, objClass, object, idFieldName);
        return statement;
    }

    /**
    Fills a PreparedStatement with data values from the given object.
    This method uses reflection to iterate through the public data
    members of the given class and set the values in the given
    statement using values froom the given object.
    
    @param statement    The statement to be filled with values
    @param objClass     The class of object to be inserted
    @param object       The object which is to supply data values
    */
    public static void fillUpdateStatement(PreparedStatement statement, Class objClass, Object object, String idFieldName) throws SQLException {
        Field[] fields = objClass.getDeclaredFields();
        Field idField = null;
        int j = 1;
        for (int i = 0; i < fields.length; i++) {
            try {
                Field field = fields[i];
                int modifier = field.getModifiers();
                if (!Modifier.isPublic(modifier)) continue;
                if (Modifier.isFinal(modifier)) continue;
                if (Modifier.isStatic(modifier)) continue;
                if (!field.getName().equalsIgnoreCase(idFieldName)) {
                    statement.setObject(j++, mapToSQLType(fields[i].get(object)));
                } else idField = fields[i];
            } catch (IllegalAccessException x) {
                x.printStackTrace();
            }
        }
        if (idField == null) {
            System.err.println("No id field in object");
            statement.setObject(j++, new Integer(-1));
        } else {
            try {
                statement.setObject(j++, idField.get(object));
            } catch (IllegalAccessException x) {
                x.printStackTrace();
            }
        }
    }

    /**
    Returns SQL for creating a PreparedStatement object for
    deleting instances of the given class from a JDBC database.
    
    This method assumes that the given class has only a single "key"
    field and that the key field is named "id".
    
    @param  objClass    The class of object for which to generate SQL
    @param  tableName   The name of the table from which to delete the object
    */
    public static String getDeleteSQL(Class objClass, String tableName, String idFieldName) throws SQLException {
        StringBuffer buffer = new StringBuffer();
        buffer.append("DELETE FROM ");
        buffer.append(tableName);
        buffer.append(" WHERE ");
        buffer.append(idFieldName);
        buffer.append("=?");
        String sql = buffer.toString();
        return sql;
    }

    public static PreparedStatement getDeleteStatement(Class objClass, String tableName, Connection conn, Object object, String idFieldName) throws SQLException {
        String sql = getDeleteSQL(objClass, tableName, idFieldName);
        PreparedStatement statement = conn.prepareStatement(sql);
        fillDeleteStatement(statement, objClass, object, idFieldName);
        return statement;
    }

    /**
    Fills a PreparedStatement with data values from the given object.
    
    @param statement    The statement to be filled with values
    @param objClass     The class of object to be deleted
    @param object       The object which is to supply data values
    */
    public static void fillDeleteStatement(PreparedStatement statement, Class objClass, Object object, String idFieldName) throws SQLException {
        try {
            Field idField = objClass.getDeclaredField(idFieldName);
            Object value = idField.get(object);
            statement.setObject(1, mapToSQLType(value));
        } catch (IllegalAccessException x) {
            x.printStackTrace();
        } catch (NoSuchFieldException x) {
            x.printStackTrace();
        }
    }

    /**
    Creates a statement for executing a query using a class definition to create
    a SELECT statement and an object that contains query values.
    
    @param objClass     The class of object for which to generate SQL.
                        A PreparedStatement is created, the name of every publicly
                        declared field in the given class object is included in the
                        statement.
    @param  tableName   The name of the table to query
    @param  conn        A Connection object used to create a Prepared statement
    @param object       The object which is to supply query values.
                        null values in this object are not included in the query statement.
    */
    public static PreparedStatement getSelectStatement(Class objClass, String tableName, Connection conn, Object object) throws SQLException {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT * FROM ");
        buffer.append(tableName);
        Field[] fields = objClass.getDeclaredFields();
        buffer.append(" WHERE ");
        for (int i = 0; i < fields.length; i++) {
            try {
                Field field = fields[i];
                int modifier = field.getModifiers();
                if (!Modifier.isPublic(modifier)) continue;
                if (Modifier.isFinal(modifier)) continue;
                if (Modifier.isStatic(modifier)) continue;
                if (field.get(object) != null) {
                    if (0 < i) buffer.append(" AND ");
                    buffer.append(field.getName());
                    buffer.append("=?");
                }
            } catch (IllegalAccessException x) {
                x.printStackTrace();
            }
        }
        String sql = buffer.toString();
        PreparedStatement statement = conn.prepareStatement(sql);
        for (int i = 0; i < fields.length; i++) {
            try {
                Field field = fields[i];
                int modifier = field.getModifiers();
                if (!Modifier.isPublic(modifier)) continue;
                if (Modifier.isFinal(modifier)) continue;
                if (Modifier.isStatic(modifier)) continue;
                if (field.get(object) != null) statement.setObject(i + 1, mapToSQLType(field.get(object)));
            } catch (IllegalAccessException x) {
                x.printStackTrace();
            }
        }
        return statement;
    }

    /**
     *  Convert the current record in a RecordSet to a Map of values.
     */
    public static final Map toMap(ResultSet resultSet) throws SQLException {
        return CollectionUtils.toMap(resultSet, null);
    }

    /**
     Convert the current record in a RecordSet to a Map of values.
    
      @param  columnToKey  Maps column names in the given record set
              to keys names in the resulting Map.  May be null, in
              which case the keys in the resulting map have the same
              value as the column names.
    
     Convenience method that delegates to CollectionUtils.toProperties
     */
    public static final Map toMap(ResultSet resultSet, Map columnToKey) throws SQLException {
        return CollectionUtils.toMap(resultSet, columnToKey);
    }

    public static final Map fillMap(ResultSet resultSet, Map toFill, Map columnToKey) throws SQLException {
        boolean useCaps = columnToKey == null;
        return CollectionUtils.fillMap(resultSet, toFill, columnToKey, useCaps);
    }

    /**
    Convert the current record in a RecordSet to a Properties
    object (where all the values are strings).
    
    @param  columnToKey  Maps column names in the given record set
              to keys names in the resulting Map.  May be null, in
              which case the keys in the resulting map have the same
              value as the column names.
    
    Convenience method that delegates to CollectionUtils.toProperties
    */
    public static final Map toProperties(ResultSet resultSet, Map columnToKey) throws SQLException {
        return CollectionUtils.toProperties(resultSet, columnToKey);
    }

    /**
    Convenience method that delegates to CollectionUtils.toProperties
    */
    public static final Map toProperties(ResultSet resultSet) throws SQLException {
        return CollectionUtils.toProperties(resultSet);
    }

    /**
    Returns SQL for creating a PreparedStatement object that
    inserts a Map of values into a JDBC database table.
    
    @param  valueList  If not null then the values associated with the
                       returned SQL statement are inserted in this List.
    */
    public static String getInsertSQL(String tableName, Map object, List valueList) throws SQLException {
        StringBuffer buffer = new StringBuffer();
        buffer.append("INSERT INTO ");
        buffer.append(tableName);
        buffer.append(" (");
        int columnCount = 0;
        for (Iterator i = object.entrySet().iterator(); i.hasNext(); ) {
            if (0 < columnCount++) buffer.append(',');
            Map.Entry entry = (Map.Entry) i.next();
            if (valueList != null) valueList.add(entry.getValue());
            buffer.append(entry.getKey().toString());
        }
        buffer.append(") VALUES (");
        for (int i = 0; i < columnCount; i++) {
            if (0 < i) buffer.append(',');
            buffer.append('?');
        }
        buffer.append(')');
        String sql = buffer.toString();
        return sql;
    }

    public static String getInsertSQL(String tableName, Map object) throws SQLException {
        return getInsertSQL(tableName, object, null);
    }

    /**
    Returns a new key for the given table.
    
    @param values     Values to insert keyed by column name.
    */
    public static Object getNewKey(Connection conn, String tableName, String idFieldName) throws SQLException {
        Object key = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT MAX(" + idFieldName + ") FROM " + tableName;
            statement = conn.prepareStatement(sql);
            rs = statement.executeQuery();
            if (rs.next()) {
                int i = rs.getInt(1);
                key = new Integer(i + 1);
            } else key = new Integer(1);
        } finally {
            if (rs != null) rs.close();
            if (statement != null) statement.close();
        }
        return key;
    }

    /**
    Returns SQL for creating a PreparedStatement object that
    inserts a Map of values into a JDBC database table.
    
    @param values     Values to insert keyed by column name.
    */
    public static PreparedStatement getInsertStatement(String tableName, Connection conn, Map object) throws SQLException {
        List valueList = new ArrayList(object.size());
        String sql = getInsertSQL(tableName, object, valueList);
        PreparedStatement statement = conn.prepareStatement(sql);
        fillPreparedStatement(statement, valueList);
        return statement;
    }

    /**
    Fills a PreparedStatement with data values from the given object.
    
    @param statement    The statement to be filled with values
    @param object       The object which is to supply data values
    */
    public static void fillPreparedStatement(PreparedStatement statement, List object) throws SQLException {
        int count = object.size();
        for (int i = 0; i < count; i++) statement.setObject(i + 1, mapToSQLType(object.get(i)));
    }

    /**
    Maps Java data types to SQL data types.
    For instance, maps java.util.Date objects to java.sql.Date objects.
    
    @param object       The object to map
    
    @return   An object suitable for writing to a database
    */
    public static Object mapToSQLType(Object object) throws SQLException {
        if (object instanceof java.util.Date) {
            object = new java.sql.Timestamp(((java.util.Date) object).getTime());
        } else if (object instanceof java.util.TimeZone) {
            int rawOffset = ((java.util.TimeZone) object).getRawOffset();
            object = new Integer(rawOffset / (60 * 60 * 1000));
        }
        return object;
    }

    public static java.sql.Date toDate(Object source) {
        if (source == null) return null;
        if (source instanceof java.sql.Date) return (java.sql.Date) source;
        if (source instanceof java.util.Date) return new java.sql.Date(((java.util.Date) source).getTime());
        if (source instanceof Number) return new java.sql.Date(((Number) source).longValue());
        return null;
    }

    public static java.sql.Timestamp toTimestamp(Object source) {
        if (source == null) return null;
        if (source instanceof Timestamp) return (Timestamp) source;
        if (source instanceof java.sql.Date) return new java.sql.Timestamp(((java.sql.Date) source).getTime());
        if (source instanceof java.util.Date) return new java.sql.Timestamp(((java.util.Date) source).getTime());
        if (source instanceof Number) return new java.sql.Timestamp(((Number) source).longValue());
        return null;
    }

    /**
    Returns SQL for creating a PreparedStatement object that
    updates the associated object in a JDBC database.
    
    This method assumes that the given class has only a single "key"
    field and that the key field is named "id".
    
    @param object The object for which to generate SQL
    @param  valueList  If not null then the values associated with the
                       returned SQL statement are inserted in this List.
    */
    public static String getUpdateSQL(String tableName, Map object, List valueList, String idFieldName) throws SQLException {
        StringBuffer buffer = new StringBuffer();
        buffer.append("UPDATE ");
        buffer.append(tableName);
        buffer.append(" SET ");
        Object idValue = null;
        int columnCount = 0;
        for (Iterator i = object.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            String fieldName = entry.getKey().toString();
            if (!fieldName.equalsIgnoreCase(idFieldName)) {
                if (valueList != null) valueList.add(entry.getValue());
                if (0 < columnCount++) buffer.append(',');
                buffer.append(fieldName);
                buffer.append("=?");
            } else idValue = entry.getValue();
        }
        if (idValue != null) {
            if (valueList != null) valueList.add(idValue);
        } else throw new SQLException("Missing " + idFieldName + " value in value list.");
        buffer.append(" WHERE ");
        buffer.append(idFieldName);
        buffer.append("=?");
        String sql = buffer.toString();
        return sql;
    }

    public static PreparedStatement getUpdateStatement(String tableName, Connection conn, Map object, String idFieldName) throws SQLException {
        List valueList = new ArrayList(object.size());
        String sql = getUpdateSQL(tableName, object, valueList, idFieldName);
        PreparedStatement statement = conn.prepareStatement(sql);
        fillPreparedStatement(statement, valueList);
        return statement;
    }

    /**
    Returns SQL for creating a PreparedStatement object for
    deleting instances of the given class from a JDBC database.
    
    This method assumes that the given class has only a single "key"
    field and that the key field is named "id".
    
    @param  objClass    The class of object for which to generate SQL
    @param  tableName   The name of the table from which to delete the object
    */
    public static String getDeleteSQL(String tableName, Map object, List valueList, String idFieldName) throws SQLException {
        Object valueID = object.get(idFieldName);
        if (valueID == null) throw new SQLException("Missing " + idFieldName + " value.");
        if (valueList != null) valueList.add(valueID);
        StringBuffer buffer = new StringBuffer();
        buffer.append("DELETE FROM ");
        buffer.append(tableName);
        buffer.append(" WHERE ");
        buffer.append(idFieldName);
        buffer.append("=?");
        String sql = buffer.toString();
        return sql;
    }

    public static PreparedStatement getDeleteStatement(String tableName, Connection conn, Map object, String idFieldName) throws SQLException {
        List valueList = new ArrayList(object.size());
        String sql = getDeleteSQL(tableName, object, valueList, idFieldName);
        PreparedStatement statement = conn.prepareStatement(sql);
        fillPreparedStatement(statement, valueList);
        return statement;
    }

    /**
    Creates a statement for executing a query.
    
    @param  tableName   The name of the table to query
    @param  conn        A Connection object used to create a Prepared statement
    @param object       The object which is to supply query values.
                        null values in this object are not included in the query statement.
    @param operators    A map of operarors used to compare values to records.
                        For any given key in the given Map object; if there is no associated
                        entry in operators with the same key then '=' is used to compare values.
    */
    public static PreparedStatement getSelectStatement(String tableName, Connection conn, Map object, Map operators) throws SQLException {
        List valueList = new ArrayList(object.size());
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT * FROM ");
        buffer.append(tableName);
        for (Iterator i = object.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            Object key = entry.getKey();
            buffer.append((0 < valueList.size()) ? " AND " : " WHERE ");
            valueList.add(entry.getValue());
            buffer.append(key.toString());
            String op = null;
            if (operators != null) {
                String t = (String) operators.get(key);
                if (t != null) op = t;
            }
            if (op == null) {
                buffer.append("=?");
            } else {
                buffer.append(op);
                buffer.append(" ?");
            }
        }
        String sql = buffer.toString();
        PreparedStatement statement = conn.prepareStatement(sql);
        fillPreparedStatement(statement, valueList);
        return statement;
    }

    public static PreparedStatement getSelectStatement(String tableName, Connection conn, Map object) throws SQLException {
        return getSelectStatement(tableName, conn, object, null);
    }
}
