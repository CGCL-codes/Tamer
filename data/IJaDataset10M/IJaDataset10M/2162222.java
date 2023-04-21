package com.knowgate.dataobjs;

import java.sql.DatabaseMetaData;
import java.sql.Types;

/**
 * <p>Object representing metadata for a database table column.</p>
 * @author Sergio Montoro Ten
 * @version 1.0
 */
public final class DBColumn {

    public DBColumn(String sTable, String sColName, short iColType, String sColType, int iPrecision, int iDecDigits, int iNullable, int iColPos) {
        sName = sColName;
        iPosition = iColPos;
        sTableName = sTable;
        iSQLType = iColType;
        sSQLTypeName = sColType;
        iMaxSize = iPrecision;
        iDecimalDigits = iDecDigits;
        bNullable = (iNullable == DatabaseMetaData.columnNullable);
    }

    /**
   *
   * @return Column Name
   */
    public String getName() {
        return sName;
    }

    /**
   *
   * @return Column Position (starting at column 1)
   */
    public int getPosition() {
        return iPosition;
    }

    /**
   *
   * @return Name of table containing this column
   */
    public String getTableName() {
        return sTableName;
    }

    /**
   *
   * @return Column SQL Type
   * @see java.sql.Types
   */
    public short getSqlType() {
        return iSQLType;
    }

    /**
   *
   * @return SQL Type Name
   */
    public String getSqlTypeName() {
        return sSQLTypeName;
    }

    /**
   *
   * @return Column Precision
   */
    public int getPrecision() {
        return iMaxSize;
    }

    /**
   *
   * @return Decimal Digits
   */
    public int getDecimalDigits() {
        return iDecimalDigits;
    }

    /**
   *
   * @return Allows NULLs?
   */
    public boolean isNullable() {
        return bNullable;
    }

    private String sName;

    private int iPosition;

    private String sTableName;

    private short iSQLType;

    private String sSQLTypeName;

    private int iMaxSize;

    private int iDecimalDigits;

    private boolean bNullable;
}
