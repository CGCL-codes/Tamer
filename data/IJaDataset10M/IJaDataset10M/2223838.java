package org.datanucleus.store.rdbms.mapping.oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.datanucleus.exceptions.NucleusDataStoreException;
import org.datanucleus.store.mapped.DatastoreField;
import org.datanucleus.store.mapped.MappedStoreManager;
import org.datanucleus.store.mapped.mapping.JavaTypeMapping;
import org.datanucleus.store.rdbms.adapter.RDBMSAdapter;
import org.datanucleus.store.rdbms.mapping.CharRDBMSMapping;
import org.datanucleus.store.rdbms.schema.OracleTypeInfo;
import org.datanucleus.store.rdbms.schema.SQLTypeInfo;

/**
 * Mapping for an Oracle XMLType type.
 **/
public class XMLTypeRDBMSMapping extends CharRDBMSMapping {

    /**
     * @param storeMgr Store Manager
     * @param mapping Java type mapping
     */
    protected XMLTypeRDBMSMapping(MappedStoreManager storeMgr, JavaTypeMapping mapping) {
        super(storeMgr, mapping);
    }

    /**
     * Constructor.
     * @param mapping Java type mapping
     * @param storeMgr Store Manager
     * @param field Field to be mapped
     */
    public XMLTypeRDBMSMapping(JavaTypeMapping mapping, MappedStoreManager storeMgr, DatastoreField field) {
        super(mapping, storeMgr, field);
    }

    protected void initialize() {
        initTypeInfo();
    }

    public SQLTypeInfo getTypeInfo() {
        return storeMgr.getSQLTypeInfoForJDBCType(OracleTypeInfo.TYPES_SYS_XMLTYPE);
    }

    /**
     * Method to extract a String from the ResultSet at the specified position
     * @param rs The Result Set
     * @param param The parameter position
     * @return the String
     */
    public String getString(Object rs, int param) {
        String value = null;
        try {
            oracle.sql.OPAQUE o = (oracle.sql.OPAQUE) ((ResultSet) rs).getObject(param);
            if (o != null) {
                value = oracle.xdb.XMLType.createXML(o).getStringVal();
            }
            if (getDatabaseAdapter().supportsOption(RDBMSAdapter.NULL_EQUALS_EMPTY_STRING)) {
                if (value != null && value.equals(getDatabaseAdapter().getSurrogateForEmptyStrings())) {
                    value = "";
                }
            }
        } catch (SQLException e) {
            throw new NucleusDataStoreException(LOCALISER_RDBMS.msg("055001", "String", "" + param, column, e.getMessage()), e);
        }
        return value;
    }

    /**
     * Method to set a String at the specified position in the JDBC PreparedStatement.
     * @param ps The PreparedStatement
     * @param param Parameter position
     * @param value The value to set
     */
    public void setString(Object ps, int param, String value) {
        try {
            if (value == null) {
                if (column.isDefaultable() && column.getDefaultValue() != null) {
                    ((PreparedStatement) ps).setString(param, column.getDefaultValue().toString().trim());
                } else {
                    ((PreparedStatement) ps).setNull(param, getTypeInfo().getDataType(), "SYS.XMLTYPE");
                }
            } else {
                ((PreparedStatement) ps).setString(param, value);
            }
        } catch (SQLException e) {
            throw new NucleusDataStoreException(LOCALISER_RDBMS.msg("055001", "String", "" + value, column, e.getMessage()), e);
        }
    }
}
