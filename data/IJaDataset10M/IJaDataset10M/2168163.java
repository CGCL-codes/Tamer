package org.hip.kernel.bom.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Set;
import org.hip.kernel.bom.DomainObjectHome;
import org.hip.kernel.bom.model.ObjectDef;
import org.hip.kernel.bom.model.TypeDef;

/**
 * 	This is the abstract implementation of prepared Statements.
 * 
 * 	@author		Benno Luthiger
 */
public abstract class SqlPreparedStatement extends CommittableStatement {

    protected DomainObjectHome home;

    protected PreparedStatement statement;

    protected String sqlString;

    /**
	 * SqlPreparedStatement default constructor.
	 */
    protected SqlPreparedStatement() {
        super();
    }

    /**
	 * This method converts the specified ValueType to
	 * an SQL type code defined by java.sql.Types.
	 *
	 * @return int SQL type code defined by java.sql.Types
	 * @param inValueType java.lang.String
	 */
    protected int convertToSqlType(String inValueType) {
        int outValue;
        if (inValueType == TypeDef.Date) {
            outValue = Types.DATE;
        } else if (inValueType == TypeDef.Timestamp) {
            outValue = Types.DATE;
        } else if (inValueType == TypeDef.String) {
            outValue = Types.CHAR;
        } else if (inValueType == TypeDef.Number) {
            outValue = Types.INTEGER;
        } else if (inValueType == TypeDef.Binary) {
            outValue = Types.BINARY;
        } else {
            throw new Error("Value type : " + inValueType + " defined in " + home.toString() + " not supported");
        }
        return outValue;
    }

    /**
	 * Returns the name of the table accessed through the specified ObjectDef
	 *
	 * @return java.lang.String
	 * @param inDef org.hip.kernel.bom.model.ObjectDef
	 */
    protected String getTablename(ObjectDef inDef) {
        Set<String> lTables = inDef.getTableNames2();
        String outTable = lTables.toArray(new String[1])[0];
        if (outTable == null) {
            throw new Error("Table not defined for : " + home.toString());
        }
        if (lTables.size() > 1) {
            throw new Error("More than one table defined for : " + home.toString());
        }
        return outTable;
    }

    /**
	 * Sets a value to the statement after doing type check
	 *
	 * @param inValue java.lang.Object
	 */
    protected void setValueToStatement(Object inValue, int inPosition) {
        String lValueString;
        try {
            if (inValue instanceof Timestamp) {
                statement.setTimestamp(inPosition, (Timestamp) inValue);
            } else if (inValue instanceof Date) {
                statement.setDate(inPosition, (Date) inValue);
            } else if (inValue instanceof Number) {
                lValueString = ((Number) inValue).toString();
                if (inValue instanceof BigDecimal) {
                    statement.setBigDecimal(inPosition, (BigDecimal) inValue);
                } else {
                    statement.setInt(inPosition, ((Number) inValue).intValue());
                }
            } else if (inValue instanceof String) {
                lValueString = (String) inValue;
                statement.setString(inPosition, lValueString);
            } else if (inValue instanceof File) {
                File lFile = (File) inValue;
                try {
                    statement.setBinaryStream(inPosition, new FileInputStream(lFile), (int) lFile.length());
                } catch (FileNotFoundException exc) {
                    throw new Error(exc);
                }
            } else if (inValue instanceof Blob) {
                statement.setBlob(inPosition, (Blob) inValue);
            } else if (inValue instanceof byte[]) {
                statement.setBytes(inPosition, (byte[]) inValue);
            } else {
                throw new Error("Value : " + inValue.toString() + " (class " + inValue.getClass().getName() + ") defined as key in " + home.toString() + " not supported");
            }
        } catch (SQLException exc) {
            throw new Error("SQL Error while settings values in a prepared insert statement : " + exc.toString());
        }
    }
}
