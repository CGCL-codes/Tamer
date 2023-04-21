package org.riverock.dbrevision.db.factory;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.riverock.dbrevision.annotation.schema.db.*;
import org.riverock.dbrevision.db.DatabaseAdapter;
import org.riverock.dbrevision.db.DatabaseManager;
import org.riverock.dbrevision.exception.DbRevisionException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * InterBase database connect 
 * $Author: serg_main $
 *
 * $Id: InterbaseAdapter.java 1141 2006-12-14 14:43:29Z serg_main $
 *
 */
@SuppressWarnings({ "UnusedAssignment" })
public class InterbaseAdapter extends DatabaseAdapter {

    private static Logger log = Logger.getLogger(InterbaseAdapter.class);

    /**
     * get family for this adapter
     * @return family
     */
    public Family getFamily() {
        return Family.INTERBASE;
    }

    public InterbaseAdapter(Connection conn) {
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

    public String getClobField(ResultSet rs, String nameField) throws SQLException {
        return getClobField(rs, nameField, 20000);
    }

    public byte[] getBlobField(ResultSet rs, String nameField, int maxLength) throws Exception {
        return null;
    }

    public void createTable(DbTable table) {
        if (table == null || table.getFields().isEmpty()) {
            return;
        }
        String sql = "create table " + table.getName() + "\n" + "(";
        boolean isFirst = true;
        for (DbField field : table.getFields()) {
            if (!isFirst) sql += ","; else isFirst = !isFirst;
            sql += "\n" + field.getName();
            switch(field.getJavaType()) {
                case Types.NUMERIC:
                case Types.DECIMAL:
                    if (field.getDecimalDigit() != 0) {
                        sql += " DECIMAL(" + (field.getSize() == null || field.getSize() > 38 ? 38 : field.getSize()) + ',' + field.getDecimalDigit() + ")";
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
                    field.setJavaStringType("unknown field type field - " + field.getName() + " javaType - " + field.getJavaType());
                    System.out.println("unknown field type field - " + field.getName() + " javaType - " + field.getJavaType());
            }
            if (field.getDefaultValue() != null) {
                String val = field.getDefaultValue().trim();
                if (DatabaseManager.checkDefaultTimestamp(val)) {
                    val = "current_timestamp";
                }
                sql += (" DEFAULT " + val);
            }
            if (field.getNullable() == DatabaseMetaData.columnNoNulls) {
                sql += " NOT NULL ";
            }
        }
        if (table.getPrimaryKey() != null && table.getPrimaryKey().getColumns().size() != 0) {
            DbPrimaryKey pk = table.getPrimaryKey();
            String namePk = pk.getColumns().get(0).getPkName();
            sql += ",\nCONSTRAINT " + namePk + " PRIMARY KEY (\n";
            int seq = Integer.MIN_VALUE;
            isFirst = true;
            if (true) throw new RuntimeException("Need implement PK comparator");
            sql += "\n)";
        }
        sql += "\n)";
        Statement st = null;
        try {
            st = this.getConnection().createStatement();
            st.execute(sql);
            int count = st.getUpdateCount();
            if (log.isDebugEnabled()) log.debug("count of processed records " + count);
        } catch (SQLException e) {
            throw new DbRevisionException(e);
        } finally {
            DatabaseManager.close(st);
            st = null;
        }
    }

    public void createForeignKey(DbTable view) {
    }

    public void dropTable(DbTable table) {
        dropTable(table.getName());
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
            if (log.isDebugEnabled()) log.debug("count of deleted object " + count);
        } catch (SQLException e) {
            log.error("Error drop table " + nameTable, e);
            throw new DbRevisionException(e);
        } finally {
            DatabaseManager.close(st);
            st = null;
        }
    }

    public void dropSequence(String nameSequence) {
    }

    public void dropConstraint(DbImportedPKColumn impPk) {
        if (impPk == null) {
            return;
        }
        String sql = "ALTER TABLE " + impPk.getPkTableName() + " DROP CONSTRAINT " + impPk.getPkName();
        PreparedStatement ps = null;
        try {
            ps = this.getConnection().prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DbRevisionException(e);
        } finally {
            DatabaseManager.close(ps);
            ps = null;
        }
    }

    public void addColumn(DbTable table, DbField field) {
        String sql = "alter table " + table.getName() + " add " + field.getName() + " ";
        switch(field.getJavaType()) {
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
                field.setJavaStringType("unknown field type field - " + field.getName() + " javaType - " + field.getJavaType());
                System.out.println("unknown field type field - " + field.getName() + " javaType - " + field.getJavaType());
        }
        if (field.getDefaultValue() != null) {
            String val = field.getDefaultValue().trim();
            if (DatabaseManager.checkDefaultTimestamp(val)) val = "current_timestamp";
            sql += (" DEFAULT " + val);
        }
        if (field.getNullable() == DatabaseMetaData.columnNoNulls) {
            sql += " NOT NULL ";
        }
        if (log.isDebugEnabled()) log.debug("Interbase addColumn sql - \n" + sql);
        Statement ps = null;
        try {
            ps = this.getConnection().createStatement();
            ps.executeUpdate(sql);
            this.getConnection().commit();
        } catch (SQLException e) {
            throw new DbRevisionException(e);
        } finally {
            DatabaseManager.close(ps);
            ps = null;
        }
    }

    public String getOnDeleteSetNull() {
        return "ON DELETE NO ACTION";
    }

    public String getDefaultTimestampValue() {
        return "current_timestamp";
    }

    public List<DbView> getViewList(String schemaPattern, String tablePattern) {
        return DatabaseManager.getViewList(getConnection(), schemaPattern, tablePattern);
    }

    public List<DbSequence> getSequnceList(String schemaPattern) {
        return new ArrayList<DbSequence>();
    }

    public String getViewText(DbView view) {
        return null;
    }

    public void createView(DbView view) {
        if (view == null || view.getName() == null || view.getName().length() == 0 || view.getText() == null || view.getText().length() == 0) return;
        String sql_ = "CREATE VIEW " + view.getName() + " AS " + StringUtils.replace(view.getText(), "||", "+");
        Statement ps = null;
        try {
            ps = this.getConnection().createStatement();
            ps.execute(sql_);
        } catch (SQLException e) {
            String errorString = "Error create view. Error code " + e.getErrorCode() + "\n" + sql_;
            log.error(errorString, e);
            throw new DbRevisionException(errorString, e);
        } finally {
            DatabaseManager.close(ps);
            ps = null;
        }
    }

    public void createSequence(DbSequence seq) {
    }

    public void setLongVarbinary(PreparedStatement ps, int index, DbDataFieldData fieldData) throws SQLException {
        ps.setNull(index, Types.VARCHAR);
    }

    public void setLongVarchar(PreparedStatement ps, int index, DbDataFieldData fieldData) throws SQLException {
        ps.setString(index, "");
    }

    public String getClobField(ResultSet rs, String nameField, int maxLength) throws SQLException {
        return null;
    }

    /**
     * ���������� �������� ��������(������������������) ��� ������� ����� ������������������.
     * ��� ������ ��������� � ������ ����� ������ ����� ���� ������ �� �������.
     *
     * @param sequence - String. ��� ����������������� ��� ��������� ���������� ��������.
     * @return long - ��������� �������� ��� ����� �� ������������������
     * @throws SQLException
     */
    public long getSequenceNextValue(CustomSequence sequence) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = getConnection().prepareStatement("select max(" + sequence.getColumnName() + ") max_id from " + sequence.getTableName());
            rs = ps.executeQuery();
            if (rs.next()) return rs.getLong(1) + 1;
        } finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
        return 1;
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
