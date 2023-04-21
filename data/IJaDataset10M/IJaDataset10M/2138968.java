package org.riverock.dbrevision.db.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import org.apache.commons.lang.StringUtils;
import org.riverock.dbrevision.schema.db.v3.DbDataFieldData;
import org.riverock.dbrevision.schema.db.v3.DbField;
import org.riverock.dbrevision.schema.db.v3.DbForeignKey;
import org.riverock.dbrevision.schema.db.v3.DbPrimaryKey;
import org.riverock.dbrevision.schema.db.v3.DbSequence;
import org.riverock.dbrevision.schema.db.v3.DbTable;
import org.riverock.dbrevision.schema.db.v3.DbView;
import org.riverock.dbrevision.schema.db.v3.DbPrimaryKeyColumn;
import org.riverock.dbrevision.db.Database;
import org.riverock.dbrevision.db.DatabaseManager;
import org.riverock.dbrevision.db.DbPkComparator;
import org.riverock.dbrevision.exception.DbRevisionException;
import org.riverock.dbrevision.exception.ViewAlreadyExistException;
import org.riverock.dbrevision.exception.TableNotFoundException;
import org.riverock.dbrevision.utils.DbUtils;

/**
 * InterBase database connect 
 * $Author: serg_main $
 *
 * $Id: InterbaseDatabase.java 1141 2006-12-14 14:43:29Z serg_main $
 *
 */
@SuppressWarnings({ "UnusedAssignment" })
public class InterbaseDatabase extends Database {

    /**
     * get family for this adapter
     * @return family
     */
    public Family getFamily() {
        return Family.INTERBASE;
    }

    public void setBlobField(String tableName, String fieldName, byte[] bytes, String whereQuery, Object[] objects, int[] fieldTyped) {
    }

    public InterbaseDatabase(Connection conn) {
        super(conn);
    }

    public int getMaxLengthStringField() {
        return 4000;
    }

    public boolean isBatchUpdate() {
        return false;
    }

    public boolean isNeedUpdateBracket() {
        return false;
    }

    public boolean isByteArrayInUtf8() {
        return false;
    }

    public boolean isSchemaSupports() {
        return false;
    }

    @Override
    public boolean isForeignKeyControlSupports() {
        return false;
    }

    @Override
    public void changeForeignKeyState(DbForeignKey key, ForeingKeyState state) {
        if (!isForeignKeyControlSupports()) {
            throw new IllegalStateException("This database type not supported changing state of FK.");
        }
        String s;
        switch(state) {
            case DISABLE:
                s = "DISABLE";
                break;
            case ENABLE:
                s = "ENABLE";
                break;
            default:
                throw new IllegalArgumentException("Unknown state " + state);
        }
        String sql = "ALTER TABLE " + key.getFkTable() + " MODIFY CONSTRAINT " + key.getFk() + " " + s;
        PreparedStatement ps = null;
        try {
            ps = this.getConnection().prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DbRevisionException(e);
        } finally {
            DbUtils.close(ps);
            ps = null;
        }
    }

    @Override
    public void changeNullableState(DbTable table, DbField field, NullableState state) {
        throw new DbRevisionException("Not implemented");
    }

    public String getDefaultSchemaName(DatabaseMetaData databaseMetaData) {
        return null;
    }

    public String getClobField(ResultSet rs, String nameField) {
        return getClobField(rs, nameField, 20000);
    }

    public byte[] getBlobField(ResultSet rs, String nameField, int maxLength) {
        return null;
    }

    public void createTable(DbTable table) {
        if (table == null || table.getFields().isEmpty()) {
            return;
        }
        String sql = "create table " + table.getT() + "\n" + "(";
        boolean isFirst = true;
        for (DbField field : table.getFields()) {
            if (!isFirst) sql += ","; else isFirst = !isFirst;
            sql += "\n" + field.getName();
            switch(field.getType()) {
                case Types.NUMERIC:
                case Types.DECIMAL:
                    if (field.getDigit() != 0) {
                        sql += " DECIMAL(" + (field.getSize() == null || field.getSize() > 38 ? 38 : field.getSize()) + ',' + field.getDigit() + ")";
                    } else {
                        if (field.getSize() == 1) sql += " SMALLINT"; else sql += " DOUBLE PRECISION";
                    }
                    break;
                case Types.INTEGER:
                    sql += " INTEGER";
                    break;
                case Types.DOUBLE:
                    sql += " DOUBLE";
                    break;
                case Types.CHAR:
                    sql += " VARCHAR(1)";
                    break;
                case Types.VARCHAR:
                    sql += " VARCHAR(" + field.getSize() + ")";
                    break;
                case Types.TIMESTAMP:
                case Types.DATE:
                    sql += " DATETIME";
                    break;
                case Types.LONGVARCHAR:
                    sql += " VARCHAR(10)";
                    break;
                case Types.LONGVARBINARY:
                    sql += " LONGVARBINARY";
                    break;
                default:
                    final String es = "unknown field type field - " + field.getName() + " javaType - " + field.getType();
                    throw new IllegalStateException(es);
            }
            if (field.getDef() != null) {
                String val = field.getDef().trim();
                if (DatabaseManager.checkDefaultTimestamp(val)) {
                    val = "current_timestamp";
                }
                sql += (" DEFAULT " + val);
            }
            if (field.getNullable() == DatabaseMetaData.columnNoNulls) {
                sql += " NOT NULL ";
            }
        }
        if (table.getPk() != null && table.getPk().getColumns().size() != 0) {
            DbPrimaryKey pk = table.getPk();
            sql += ",\nCONSTRAINT " + pk.getPk() + " PRIMARY KEY (\n";
            Collections.sort(pk.getColumns(), DbPkComparator.getInstance());
            isFirst = true;
            for (DbPrimaryKeyColumn column : pk.getColumns()) {
                if (!isFirst) {
                    sql += ",";
                } else {
                    isFirst = !isFirst;
                }
                sql += column.getC();
            }
            sql += "\n)";
        }
        sql += "\n)";
        Statement st = null;
        try {
            st = this.getConnection().createStatement();
            st.execute(sql);
            int count = st.getUpdateCount();
        } catch (SQLException e) {
            final String es = "SQL:\n" + sql;
            throw new DbRevisionException(es, e);
        } finally {
            DbUtils.close(st);
            st = null;
        }
    }

    public void dropTable(DbTable table) {
        dropTable(table.getT());
    }

    public void dropTable(String nameTable) {
        if (nameTable == null) {
            return;
        }
        String sql = "drop table " + nameTable;
        Statement st = null;
        try {
            st = this.getConnection().createStatement();
            st.execute(sql);
            int count = st.getUpdateCount();
        } catch (SQLException e) {
            final String es = "Error drop table " + nameTable;
            throw new DbRevisionException(es, e);
        } finally {
            DbUtils.close(st);
            st = null;
        }
    }

    public void dropSequence(String nameSequence) {
    }

    public void addColumn(DbTable table, DbField field) {
        String sql = "alter table " + table.getT() + " add " + field.getName() + " ";
        switch(field.getType()) {
            case Types.NUMERIC:
            case Types.DECIMAL:
                sql += " DOUBLE PRECISION";
                break;
            case Types.INTEGER:
                sql += " INTEGER";
                break;
            case Types.DOUBLE:
                sql += " DOUBLE";
                break;
            case Types.CHAR:
                sql += " VARCHAR(1)";
                break;
            case Types.VARCHAR:
                sql += (" VARCHAR(" + field.getSize() + ") ");
                break;
            case Types.TIMESTAMP:
            case Types.DATE:
                sql += " DATETIME";
                break;
            case Types.LONGVARCHAR:
                sql += " VARCHAR(10)";
                break;
            case Types.LONGVARBINARY:
                sql += " LONGVARBINARY";
                break;
            default:
                final String es = "unknown field type field - " + field.getName() + " javaType - " + field.getType();
                throw new IllegalStateException(es);
        }
        if (field.getDef() != null) {
            String val = field.getDef().trim();
            if (DatabaseManager.checkDefaultTimestamp(val)) val = "current_timestamp";
            sql += (" DEFAULT " + val);
        }
        if (field.getNullable() == DatabaseMetaData.columnNoNulls) {
            sql += " NOT NULL ";
        }
        Statement ps = null;
        try {
            ps = this.getConnection().createStatement();
            ps.executeUpdate(sql);
            this.getConnection().commit();
        } catch (SQLException e) {
            throw new DbRevisionException(e);
        } finally {
            DbUtils.close(ps);
            ps = null;
        }
    }

    public String getOnDeleteSetNull() {
        return "ON DELETE NO ACTION";
    }

    public String getDefaultTimestampValue() {
        return "current_timestamp";
    }

    public List<DbSequence> getSequnceList(String schemaPattern) {
        return new ArrayList<DbSequence>();
    }

    public String getViewText(DbView view) {
        return null;
    }

    public void createView(DbView view) {
        if (view == null || view.getT() == null || view.getT().length() == 0 || view.getText() == null || view.getText().length() == 0) return;
        String sql_ = "CREATE VIEW " + view.getT() + " AS " + StringUtils.replace(view.getText(), "||", "+");
        Statement ps = null;
        try {
            ps = this.getConnection().createStatement();
            ps.execute(sql_);
        } catch (SQLException e) {
            if (testExceptionViewExists(e)) {
                throw new ViewAlreadyExistException("View " + view.getT() + " already exist.");
            }
            if (testExceptionTableNotFound(e)) {
                throw new TableNotFoundException("View " + view.getT() + " refered to unknown table.");
            }
            String errorString = "Error create view. Error code " + e.getErrorCode() + "\n" + sql_;
            throw new DbRevisionException(errorString, e);
        } finally {
            DbUtils.close(ps);
            ps = null;
        }
    }

    public void createSequence(DbSequence seq) {
    }

    public void setLongVarbinary(PreparedStatement ps, int index, DbDataFieldData fieldData) {
        try {
            ps.setNull(index, Types.VARCHAR);
        } catch (SQLException e) {
            throw new DbRevisionException(e);
        }
    }

    public void setLongVarchar(PreparedStatement ps, int index, DbDataFieldData fieldData) {
        try {
            ps.setString(index, "");
        } catch (SQLException e) {
            throw new DbRevisionException(e);
        }
    }

    public String getClobField(ResultSet rs, String nameField, int maxLength) {
        return null;
    }

    public boolean testExceptionTableNotFound(Exception e) {
        if (((SQLException) e).getErrorCode() == 208) return true;
        return false;
    }

    public boolean testExceptionIndexUniqueKey(Exception e, String index) {
        if (e instanceof SQLException) {
            if (((SQLException) e).getErrorCode() == -(org.hsqldb.Trace.VIOLATION_OF_UNIQUE_INDEX)) return true;
        }
        return false;
    }

    public boolean testExceptionIndexUniqueKey(Exception e) {
        return false;
    }

    public boolean testExceptionTableExists(Exception e) {
        if (e instanceof SQLException) {
            if (((SQLException) e).getErrorCode() == 335544351) return true;
        }
        return false;
    }

    public boolean testExceptionViewExists(Exception e) {
        if (e instanceof SQLException) {
            if (((SQLException) e).getErrorCode() == 2714) return true;
        }
        return false;
    }

    public boolean testExceptionSequenceExists(Exception e) {
        return false;
    }

    public boolean testExceptionConstraintExists(Exception e) {
        if (e instanceof SQLException) {
            if (((SQLException) e).getErrorCode() == -(org.hsqldb.Trace.CONSTRAINT_ALREADY_EXISTS)) return true;
        }
        return false;
    }
}
