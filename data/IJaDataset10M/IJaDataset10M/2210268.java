package de.mguennewig.pobjects.jdbc;

import java.sql.*;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import de.mguennewig.pobjects.*;
import de.mguennewig.pobjects.metadata.*;

/** Container implementation for MS-SQL Server databases.
 *
 * @author Michael G�nnewig
 * @see #getCapabilities()
 * @see de.mguennewig.pobjtool.ddl.SqlServer
 */
public class SqlServerContainer extends JdbcContainer {

    /** MS-SQL default database port. */
    public static final int DEFAULT_PORT = 1433;

    private static final Pattern FKEY_PATTERN = Pattern.compile("(?i:REFERENCE constraint) \"([^\"]+)\".* table \"[^\".]+\\.([^\"]+)\", column '([^']+)'");

    private static boolean forceEncryption = true;

    static {
        PObjUtils.tryToLoadClass("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    }

    /**
   * Returns whether a new created container will enforce an encrypted
   * connection to the MS-SQL database.
   */
    public static final boolean getForceEncryption() {
        return forceEncryption;
    }

    /**
   * Sets whether a new created container will enforce an encrypted
   * connection to the MS-SQL database.
   */
    public static void setForceEncryption(final boolean encrypt) {
        forceEncryption = encrypt;
    }

    /** Creates a container without a connection to a MS-SQL Server.
   *
   * @throws NullPointerException if {@code null} is passed for the dictionary.
   */
    public SqlServerContainer(final PObjDictionary dict, final String schema) {
        super(dict, schema);
    }

    /**
   * Creates a container that opens a connection to the specified MS-SQL Server.
   *
   * <p><b>NOTE:</b> Auto-commitment will be turned off which is normally
   * turned on for JDBC.</p>
   *
   * @param hostname The host name on which the DBMS runs. If {@code null}
   *   it defaults to <code>localhost</code>.
   * @param port The port on which the DBMS listens. If <code>null</code> the
   *   default port is used.
   * @param database The name of the database.
   * @param username The user name that should be used for authentication.
   * @param password The password that should be used for authentication.
   * @param dict
   * @param schema
   * @throws NullPointerException if {@code null} is passed for the dictionary.
   * @throws SQLException if no connection to the database can be opened, or
   *   an error occurred during initialization
   * @see #initializeConnection()
   */
    public SqlServerContainer(final String hostname, final Integer port, final String database, final String username, final String password, final PObjDictionary dict, final String schema) throws SQLException {
        super(hostname, port, database, username, password, dict, schema);
    }

    /**
   * Creates a container with a specified connection to a DBMS, which must
   * denote a MS-SQL Server compatible DBMS.
   *
   * <p><b>NOTE:</b> Auto-commitment will be turned off which is normally
   * turned on for JDBC.</p>
   */
    public SqlServerContainer(final Connection conn, final PObjDictionary dict, final String schema) throws SQLException {
        super(conn, dict, schema);
    }

    /** {@inheritDoc} */
    @Override
    public String getConnectURL(final String hostname, final Integer port, final String database) {
        final StringBuilder sb = new StringBuilder();
        sb.append("jdbc:sqlserver://");
        if (hostname == null) sb.append("localhost"); else sb.append(hostname);
        if (port != null) {
            sb.append(':');
            sb.append(port);
        }
        if (database != null) {
            sb.append(";database=");
            sb.append(database);
        }
        return sb.toString();
    }

    /** {@inheritDoc} */
    @Override
    public Properties getConnectProperties() {
        final Properties props = super.getConnectProperties();
        if (getForceEncryption()) props.setProperty("encrypt", Boolean.TRUE.toString());
        return props;
    }

    /** Returns the capabilities of the DBMS.
   *
   * <p>Currently returns {@link #SUPPORTS_SQL99_JOIN} and
   * {@link #SUPPORTS_LIMIT_OFFSET}.</p>
   */
    @Override
    public int getCapabilities() {
        return SUPPORTS_SQL99_JOIN;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean isConstraintException(final SQLException e) {
        return (e.getMessage().toLowerCase().indexOf("constraint") != -1);
    }

    /** {@inheritDoc} */
    @Override
    protected PObjConstraintException mapConstraintException(final SQLException e, final ClassDecl cd, final Record obj) {
        final PObjConstraintException ce = new PObjConstraintException(obj);
        final String msg = e.getMessage().trim().toLowerCase();
        ce.initCause(e);
        for (int i = 0; i < cd.getNumConstraints(); i++) {
            final Constraint c = cd.getConstraint(i);
            if (msg.indexOf(c.getSchemaName().toLowerCase()) != -1) {
                if (c instanceof UniqueConstraint) {
                    final Field[] fields = ((UniqueConstraint) c).getFields();
                    ce.addAll(fields, c.getSchemaName(), c);
                } else if (c instanceof CheckConstraint) {
                    ce.add(null, c.getSchemaName(), c);
                }
                break;
            }
        }
        final Matcher m = FKEY_PATTERN.matcher(e.getMessage().trim());
        if (m.find()) {
            final String constraintName = m.group(1);
            final String srcTableName = m.group(2);
            final ClassDecl srcTable = getClassDecl(srcTableName);
            ForeignKeyConstraint fkey = null;
            if (srcTable != null) fkey = (ForeignKeyConstraint) srcTable.getConstraint(constraintName);
            if (fkey == null) {
                ce.add(cd.getIdField(), "still referenced by an object in " + srcTableName + " see constraint " + constraintName, null);
            } else {
                ce.addAll(fkey.getTargetFields(), "still referenced by an object in " + srcTableName, fkey);
            }
        }
        return ce;
    }

    /** {@inheritDoc}
   *
   * @throws UnsupportedOperationException as MS-SQL does not support sequences
   *   with its own name
   */
    @Override
    public String getSequenceCurrentValue(final Sequence seq) {
        throw new UnsupportedOperationException("MS-SQL does not support sequences with own name");
    }

    /** {@inheritDoc}
   *
   * @throws UnsupportedOperationException as MS-SQL does not support sequences
   *   with its own name
   */
    @Override
    public String getSequenceNextValue(final Sequence seq) {
        throw new UnsupportedOperationException("MS-SQL does not support sequences with own name");
    }

    /** {@inheritDoc} */
    @Override
    public String insertObject(final PObject obj) throws PObjConstraintException, PObjSQLException {
        ClassDecl te = obj.getClassDecl();
        final ClassDecl base = te.getBottomClass();
        final String idGen;
        final String id;
        if (base.getNumColumns() == 0) idGen = "IDENT_INCR('" + getQualifiedName(base) + "')"; else idGen = null;
        try {
            if (idGen != null) setIdentityInsert(base, true);
            if (!insertObjectTable(obj, base, idGen)) return null;
            id = getSequenceValue("SELECT IDENT_CURRENT(?)", getQualifiedName(base));
            assert id != null;
            for (; te != null && te != base; te = te.getBaseClass()) {
                if (te.isMaterializedTable() && !insertObjectTable(obj, te, id)) {
                    throw new PObjSQLException("Failed to insert object into table `" + te.getSchemaName() + "'");
                }
            }
        } finally {
            if (idGen != null) setIdentityInsert(base, false);
        }
        transactionObjects.add(obj);
        cache.put(PObject.getKey(obj.getClassDecl(), id), obj);
        return id;
    }

    private void setIdentityInsert(final ClassDecl table, final boolean on) throws PObjSQLException {
        final StringBuilder sb = new StringBuilder(64);
        Statement statm = null;
        sb.append("SET IDENTITY_INSERT ");
        sb.append(getQualifiedName(table));
        sb.append(on ? " ON" : " OFF");
        try {
            statm = getConnection().createStatement();
            statm.execute(sb.toString());
        } catch (SQLException e) {
            throw new PObjSQLException(e);
        } finally {
            closeStatement(statm);
        }
    }
}
