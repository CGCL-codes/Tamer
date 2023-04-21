package org.exolab.jms.tools.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.exolab.jms.persistence.PersistenceException;
import org.exolab.jms.persistence.SQLHelper;

/**
 * This class provides methods for examining a database schema.
 *
 * @author <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.2 $ $Date: 2005/09/04 07:16:22 $
 */
public class SchemaBrowser {

    /**
     * The connection to the database.
     */
    private final Connection _connection;

    /**
     * The set of data types supported by the RDBMS.
     */
    private final TypeSet _types;

    /**
     * The type mapper, used to convert between the type requested by the
     * schema, and those supported by the RDBMS.
     */
    private final TypeMapper _mapper;

    /**
     * Construct a new <code>SchemaBrowser</code>.
     *
     * @param connection the JDBC connection
     * @throws PersistenceException if database meta-data can't be obtained
     */
    public SchemaBrowser(Connection connection) throws PersistenceException {
        _connection = connection;
        _types = new TypeSet(_connection);
        _mapper = new TypeMapper(_types);
    }

    /**
     * Returns the schema for the specified table.
     *
     * @param name the table name
     * @return the schema for the table identified by <code>name</code>
     * @throws PersistenceException if the named table doesn't exist, or the
     *                              schema cannot be obtained
     */
    public Table getTable(String name) throws PersistenceException {
        Table result = new Table();
        result.setName(name);
        PreparedStatement select = null;
        try {
            select = _connection.prepareStatement("select * from " + name + " where 1 = 0");
            ResultSet set = select.executeQuery();
            ResultSetMetaData metaData = set.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); ++i) {
                String columnName = metaData.getColumnName(i);
                int dataType = metaData.getColumnType(i);
                long precision = metaData.getPrecision(i);
                int nullable = metaData.isNullable(i);
                String typeName = metaData.getColumnTypeName(i);
                Type type = _mapper.getType(dataType, precision);
                if (type == null) {
                    type = _types.getNearestType(dataType, precision);
                    if (type == null) {
                        throw new InvalidTypeException("JDBC driver error. Type=" + dataType + ", precision=" + precision + "(SQL type=" + typeName + ") isn't supported by " + "Connection.getMetaData().getTypeInfo(), " + "but is referred to by " + "Connection.getMetaData().getColumns()");
                    }
                }
                Attribute attribute = new Attribute();
                attribute.setName(columnName);
                attribute.setType(type.getSymbolicType());
                if (nullable == DatabaseMetaData.columnNoNulls) {
                    attribute.setNotNull(true);
                } else {
                    attribute.setNotNull(false);
                }
                result.addAttribute(attribute);
            }
        } catch (SQLException exception) {
            throw new PersistenceException("Failed to determine the schema of table=" + name, exception);
        } finally {
            SQLHelper.close(select);
        }
        return result;
    }

    /**
     * Returns the {@link Type} for an {@link Attribute}.
     *
     * @param attribute the attribute
     * @return the type
     * @throws PersistenceException if {@link Attribute#getType} is invalid, or
     *                              the RDBMS doesn't support the type
     */
    public Type getType(Attribute attribute) throws PersistenceException {
        Type result = null;
        Type type = Type.getType(attribute.getType());
        Type map = _mapper.getType(type.getType(), type.getPrecision());
        if (map == null) {
            throw new PersistenceException("Database does not support type=" + attribute.getType());
        }
        if (type.getType() != map.getType()) {
            result = map;
        } else {
            boolean parameters = type.getParameters();
            long precision = type.getPrecision();
            if (precision <= map.getPrecision()) {
                if (precision == -1) {
                    precision = map.getPrecision();
                    parameters = map.getParameters();
                }
                result = new Type(map.getType(), map.getName(), precision, parameters);
            } else {
                throw new PersistenceException(attribute.getName() + type + " exceeds precision for " + map + " precision=" + map.getPrecision());
            }
        }
        return result;
    }

    /**
     * Returns true if a table exists.
     *
     * @param table the name of the table
     */
    public boolean getTableExists(String table) throws PersistenceException {
        boolean result = false;
        String name = table;
        ResultSet set = null;
        try {
            DatabaseMetaData metaData = _connection.getMetaData();
            if (metaData.storesUpperCaseIdentifiers()) {
                name = name.toUpperCase();
            }
            set = metaData.getTables(_connection.getCatalog(), null, name, null);
            if (set.next()) {
                result = true;
            }
        } catch (SQLException exception) {
            throw new PersistenceException("Failed to determine if table=" + table + " exists", exception);
        } finally {
            SQLHelper.close(set);
        }
        return result;
    }
}
