package org.ujorm.orm.dialect;

import java.io.IOException;
import java.util.List;
import org.ujorm.orm.OrmUjo;
import org.ujorm.orm.Query;
import org.ujorm.orm.metaModel.MetaColumn;

public class FirebirdDialect extends org.ujorm.orm.SqlDialect {

    @Override
    public String getJdbcDriver() {
        return "org.firebirdsql.jdbc.FBDriver";
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:firebirdsql:localhost/3050:DbFile?lc_ctype=UTF8";
    }

    /** NO SCHEMA */
    @Override
    public Appendable printCreateSchema(String schema, Appendable out) throws IOException {
        return out;
    }

    /** Print a 'lock clausule' to the end of SQL SELECT statement to use a pessimistic lock.
     * The current database does not support the feature, throw an exception UnsupportedOperationException.
     * <br>The method prints a text "FOR UPDATE WITH LOCK".
     * @param query The UJO query
     */
    @Override
    protected Appendable printLockForSelect(final Query query, final Appendable out) throws IOException, UnsupportedOperationException {
        out.append("FOR UPDATE WITH LOCK");
        return out;
    }

    /** Print a SQL sript to add a new column to the table 
     * <BR> The DDL statement does not contains a word COLUMN.
     */
    @Override
    public Appendable printAlterTable(MetaColumn column, Appendable out) throws IOException {
        out.append("ALTER TABLE ");
        printFullTableName(column.getTable(), out);
        out.append(" ADD ");
        if (column.isForeignKey()) {
            printFKColumnsDeclaration(column, out);
        } else {
            printColumnDeclaration(column, null, out);
        }
        if (column.hasDefaultValue()) {
            final String notNull = " NOT NULL";
            if (column.isMandatory()) {
                int i = out.toString().indexOf(notNull);
                if (i >= 0 && out instanceof StringBuilder) {
                    ((StringBuilder) out).delete(i, i + notNull.length());
                }
            }
            printDefaultValue(column, out);
            if (column.isMandatory()) {
                out.append(notNull);
            }
        }
        return out;
    }

    /** Multi row INSERT is not implemented in this dialect yet due:<br/>
     * Caused by: org.firebirdsql.jdbc.FBSQLException: GDS Exception. 335544436. SQL error code = -804
     */
    @Override
    public boolean isMultiRowInsertSupported() {
        return false;
    }

    /**
     * Implementation is not working
     * @see #isMultiRowInsertSupported()
     */
    @Override
    public Appendable printInsert(List<? extends OrmUjo> bo, int idxFrom, int idxTo, Appendable out) throws IOException {
        return printInsertBySelect(bo, idxFrom, idxTo, "FROM RDB$DATABASE", out);
    }
}
