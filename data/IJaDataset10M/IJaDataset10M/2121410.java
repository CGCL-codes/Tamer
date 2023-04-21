package com.kopiright.dbtar.archiver.core;

import java.sql.Types;

public class SqlType {

    public static final int VAR = 0;

    public static final int CONST = 1;

    public static final int BOTH = 2;

    public static final int UNKNOWN = 3;

    public int iId;

    public String sName;

    public int iSize;

    /** @param id This is the sql type as returned by JDBC
   */
    public SqlType(int id, String name, int size) {
        iId = id;
        sName = name;
        iSize = size;
        if (iSize < 0) {
            iSize = 0;
        }
    }

    public String toString() {
        return "[id:" + iId + ", name:" + sName + "]";
    }

    public boolean isBoolean() {
        return (iId == Types.BIT) || (iId == Types.BOOLEAN);
    }

    public boolean isInteger() {
        return (iId == Types.BIGINT) || (iId == Types.INTEGER) || (iId == Types.SMALLINT) || (iId == Types.TINYINT);
    }

    public boolean isDecimal() {
        return iId == Types.DECIMAL;
    }

    public boolean isOther() {
        return iId == Types.OTHER;
    }

    public boolean isReal() {
        return (iId == Types.DECIMAL) || (iId == Types.DOUBLE) || (iId == Types.FLOAT) || (iId == Types.NUMERIC) || (iId == Types.REAL);
    }

    public boolean isVarchar() {
        return iId == Types.VARCHAR;
    }

    public boolean isNumber() {
        return isInteger() || isReal();
    }

    public boolean isChar() {
        return iId == Types.CHAR;
    }

    public boolean isString() {
        return (iId == Types.CHAR) || (iId == Types.VARCHAR);
    }

    public boolean isDate() {
        return iId == Types.DATE;
    }

    public boolean isTime() {
        return iId == Types.TIME;
    }

    public boolean isTimeStamp() {
        return iId == Types.TIMESTAMP;
    }

    public boolean isTemporalType() {
        return (isDate() || isTime() || isTimeStamp());
    }

    public boolean isBinary() {
        return iId == Types.BINARY;
    }

    public boolean isVarBinary() {
        return iId == Types.VARBINARY;
    }

    public boolean isLongVarBinary() {
        return iId == Types.LONGVARBINARY;
    }

    public boolean isBinaryType() {
        return isBinary() || isVarBinary() || isLongVarBinary();
    }

    public boolean isLongVarChar() {
        return iId == Types.LONGVARCHAR;
    }

    public boolean isBlob() {
        return iId == Types.BLOB;
    }

    public boolean isClob() {
        return iId == Types.CLOB;
    }
}
