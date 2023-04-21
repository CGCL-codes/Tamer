package org.nakedobjects.persistence.nhibernate;

import System.Data.DbType;
import NHibernate.Dialect.Dialect;

/**
 * Fix the fact that the standard NHibernate Dialect doesn't allow DbType.Date
 */
public class SQLiteDialect extends Dialect {

    public SQLiteDialect() {
        RegisterColumnType(DbType.Binary, "BLOB");
        RegisterColumnType(DbType.Byte, "INTEGER");
        RegisterColumnType(DbType.Int16, "INTEGER");
        RegisterColumnType(DbType.Int32, "INTEGER");
        RegisterColumnType(DbType.Int64, "INTEGER");
        RegisterColumnType(DbType.SByte, "INTEGER");
        RegisterColumnType(DbType.UInt16, "INTEGER");
        RegisterColumnType(DbType.UInt32, "INTEGER");
        RegisterColumnType(DbType.UInt64, "INTEGER");
        RegisterColumnType(DbType.Currency, "NUMERIC");
        RegisterColumnType(DbType.Decimal, "NUMERIC");
        RegisterColumnType(DbType.Double, "NUMERIC");
        RegisterColumnType(DbType.Single, "NUMERIC");
        RegisterColumnType(DbType.VarNumeric, "NUMERIC");
        RegisterColumnType(DbType.String, "TEXT");
        RegisterColumnType(DbType.AnsiStringFixedLength, "TEXT");
        RegisterColumnType(DbType.StringFixedLength, "TEXT");
        RegisterColumnType(DbType.DateTime, "DATETIME");
        RegisterColumnType(DbType.Date, "DATETIME");
        RegisterColumnType(DbType.Time, "DATETIME");
        RegisterColumnType(DbType.Boolean, "INTEGER");
    }

    public String get_IdentitySelectString() {
        return "select last_insert_rowid()";
    }

    public boolean get_HasAlterTable() {
        return false;
    }

    public boolean get_DropConstraints() {
        return false;
    }

    public boolean get_SupportsForUpdate() {
        return false;
    }

    public boolean get_SupportsSubSelects() {
        return false;
    }
}
