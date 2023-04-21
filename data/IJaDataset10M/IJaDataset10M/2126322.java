package org.datanucleus.store.rdbms.mapping;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.datanucleus.ClassNameConstants;
import org.datanucleus.exceptions.NucleusDataStoreException;
import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.mapped.DatastoreField;
import org.datanucleus.store.mapped.MappedStoreManager;
import org.datanucleus.store.mapped.mapping.JavaTypeMapping;
import org.datanucleus.store.rdbms.schema.SQLTypeInfo;
import org.datanucleus.store.rdbms.table.Column;

/**
 * Mapping of a BOOLEAN RDBMS type.
 */
public class BooleanRDBMSMapping extends RDBMSMapping {

    /**
     * @param storeMgr Store Manager
     * @param mapping The java type mapping
     */
    protected BooleanRDBMSMapping(MappedStoreManager storeMgr, JavaTypeMapping mapping) {
        super(storeMgr, mapping);
    }

    /**
     * Constructor.
     * @param mapping Java type mapping
     * @param storeMgr Store Manager
     * @param field Field to be mapped
     */
    public BooleanRDBMSMapping(JavaTypeMapping mapping, MappedStoreManager storeMgr, DatastoreField field) {
        super(storeMgr, mapping);
        column = (Column) field;
        initialize();
    }

    private void initialize() {
        initTypeInfo();
    }

    /**
     * Accessor for whether the mapping is boolean-based.
     * @return Whether the mapping is boolean based
     */
    public boolean isBooleanBased() {
        return true;
    }

    public SQLTypeInfo getTypeInfo() {
        if (column != null && column.getColumnMetaData().getSqlType() != null) {
            return storeMgr.getSQLTypeInfoForJDBCType(Types.BOOLEAN, column.getColumnMetaData().getSqlType());
        }
        return storeMgr.getSQLTypeInfoForJDBCType(Types.BOOLEAN);
    }

    public void setBoolean(Object ps, int param, boolean value) {
        try {
            ((PreparedStatement) ps).setBoolean(param, value);
        } catch (SQLException e) {
            throw new NucleusDataStoreException(LOCALISER_RDBMS.msg("055001", "boolean", "" + value, column, e.getMessage()), e);
        }
    }

    public boolean getBoolean(Object rs, int param) {
        boolean value;
        try {
            value = ((ResultSet) rs).getBoolean(param);
        } catch (SQLException e) {
            throw new NucleusDataStoreException(LOCALISER_RDBMS.msg("055002", "Boolean", "" + param, column, e.getMessage()), e);
        }
        return value;
    }

    /**
     * Setter for booleans stored as String datastore types.
     * @param ps PreparedStatement
     * @param param Number of the field
     * @param value Value of the boolean
     */
    public void setString(Object ps, int param, String value) {
        try {
            if (value == null) {
                ((PreparedStatement) ps).setNull(param, getTypeInfo().getDataType());
            } else {
                ((PreparedStatement) ps).setBoolean(param, value.equals("Y") ? true : false);
            }
        } catch (SQLException e) {
            throw new NucleusDataStoreException(LOCALISER_RDBMS.msg("055001", "String", "" + value, column, e.getMessage()), e);
        }
    }

    /**
     * Accessor for the value for a boolean field stored as a String datastore type.
     * @param rs ResultSet
     * @param param number of the parameter.
     * @return The value
     */
    public String getString(Object rs, int param) {
        String value;
        try {
            value = (((ResultSet) rs).getBoolean(param) ? "Y" : "N");
        } catch (SQLException e) {
            throw new NucleusDataStoreException(LOCALISER_RDBMS.msg("055002", "String", "" + param, column, e.getMessage()), e);
        }
        return value;
    }

    public void setObject(Object ps, int param, Object value) {
        try {
            if (value == null) {
                ((PreparedStatement) ps).setNull(param, getTypeInfo().getDataType());
            } else {
                if (value instanceof String) {
                    ((PreparedStatement) ps).setBoolean(param, value.equals("Y") ? true : false);
                } else if (value instanceof Boolean) {
                    ((PreparedStatement) ps).setBoolean(param, ((Boolean) value).booleanValue());
                } else {
                    throw new NucleusUserException(LOCALISER_RDBMS.msg("055004", value, column));
                }
            }
        } catch (SQLException e) {
            throw new NucleusDataStoreException(LOCALISER_RDBMS.msg("055001", "Object", "" + value, column, e.getMessage()), e);
        }
    }

    public Object getObject(Object rs, int param) {
        Object value;
        try {
            boolean b = ((ResultSet) rs).getBoolean(param);
            if (((ResultSet) rs).wasNull()) {
                value = null;
            } else {
                if (getJavaTypeMapping().getJavaType().getName().equals(ClassNameConstants.JAVA_LANG_STRING)) {
                    value = (b ? "Y" : "N");
                } else {
                    value = (b ? Boolean.TRUE : Boolean.FALSE);
                }
            }
        } catch (SQLException e) {
            throw new NucleusDataStoreException(LOCALISER_RDBMS.msg("055002", "Object", "" + param, column, e.getMessage()), e);
        }
        return value;
    }
}
