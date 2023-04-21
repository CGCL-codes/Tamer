package org.apache.torque.adapter;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Query;

/**
 * This class is the abstract base for any database adapter
 * Support for new databases is added by subclassing this
 * class and implementing its abstract methods, and by
 * registering the new database adapter and its corresponding
 * JDBC driver in the service configuration file.
 *
 * <p>The Torque database adapters exist to present a uniform
 * interface to database access across all available databases.  Once
 * the necessary adapters have been written and configured,
 * transparent swapping of databases is theoretically supported with
 * <i>zero code changes</i> and minimal configuration file
 * modifications.
 *
 * <p>Torque uses the driver class name to find the right adapter.
 * A JDBC driver corresponding to your adapter must be added to the properties
 * file, using the fully-qualified class name of the driver. If no driver is
 * specified for your database, <code>driver.default</code> is used.
 *
 * <pre>
 * #### MySQL MM Driver
 * database.default.driver=org.gjt.mm.mysql.Driver
 * database.default.url=jdbc:mysql://localhost/DATABASENAME
 * </pre>
 *
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @author <a href="mailto:bmclaugh@algx.net">Brett McLaughlin</a>
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @author <a href="mailto:vido@ldh.org">Augustin Vidovic</a>
 * @version $Id: AbstractDBAdapter.java,v 1.2 2007-02-10 01:28:51 psy666m Exp $
 */
public abstract class AbstractDBAdapter implements DB {

    /**
     * Empty constructor.
     */
    protected AbstractDBAdapter() {
    }

    /**
     * This method is used to ignore case.
     *
     * @param in The string to transform to upper case.
     * @return The upper case string.
     */
    public abstract String toUpperCase(String in);

    /**
     * Returns the character used to indicate the beginning and end of
     * a piece of text used in a SQL statement (generally a single
     * quote).
     *
     * @return The text delimeter.
     */
    public char getStringDelimiter() {
        return '\'';
    }

    /**
     * Returns the constant from the {@link
     * org.apache.torque.adapter.IDMethod} interface denoting which
     * type of primary key generation method this type of RDBMS uses.
     *
     * @return IDMethod constant
     */
    public abstract String getIDMethodType();

    /**
     * Returns SQL used to get the most recently inserted primary key.
     * Databases which have no support for this return
     * <code>null</code>.
     *
     * @param obj Information used for key generation.
     * @return The most recently inserted database key.
     */
    public abstract String getIDMethodSQL(Object obj);

    /**
     * Locks the specified table.
     *
     * @param con The JDBC connection to use.
     * @param table The name of the table to lock.
     * @throws SQLException No Statement could be created or executed.
     */
    public abstract void lockTable(Connection con, String table) throws SQLException;

    /**
     * Unlocks the specified table.
     *
     * @param con The JDBC connection to use.
     * @param table The name of the table to unlock.
     * @throws SQLException No Statement could be created or executed.
     */
    public abstract void unlockTable(Connection con, String table) throws SQLException;

    /**
     * This method is used to ignore case.
     *
     * @param in The string whose case to ignore.
     * @return The string in a case that can be ignored.
     */
    public abstract String ignoreCase(String in);

    /**
     * This method is used to ignore case in an ORDER BY clause.
     * Usually it is the same as ignoreCase, but some databases
     * (Interbase for example) does not use the same SQL in ORDER BY
     * and other clauses.
     *
     * @param in The string whose case to ignore.
     * @return The string in a case that can be ignored.
     */
    public String ignoreCaseInOrderBy(final String in) {
        return ignoreCase(in);
    }

    /**
     * This method is used to check whether the database natively
     * supports limiting the size of the resultset.
     *
     * @return True if the database natively supports limiting the
     * size of the resultset.
     */
    public boolean supportsNativeLimit() {
        return false;
    }

    /**
     * This method is used to check whether the database natively
     * supports returning results starting at an offset position other
     * than 0.
     *
     * @return True if the database natively supports returning
     * results starting at an offset position other than 0.
     */
    public boolean supportsNativeOffset() {
        return false;
    }

    /**
     * This method is used to generate the database specific query
     * extension to limit the number of record returned.
     *
     * @param query The query to modify
     * @param offset the offset Value
     * @param limit the limit Value
     *
     * @throws TorqueException if any error occurs when building the query
     */
    public void generateLimits(final Query query, final int offset, final int limit) throws TorqueException {
        if (supportsNativeLimit()) {
            query.setLimit(String.valueOf(limit));
        }
    }

    /**
    * This method is for the SqlExpression.quoteAndEscape rules.  The rule is,
    * any string in a SqlExpression with a BACKSLASH will either be changed to
    * "\\" or left as "\".  SapDB does not need the escape character.
    *
    * @return true if the database needs to escape text in SqlExpressions.
    */
    public boolean escapeText() {
        return true;
    }

    /**
     * This method is used to check whether the database supports
     * limiting the size of the resultset.
     *
     * @return The limit style for the database.
     * @deprecated This should not be exposed to the outside
     */
    @Deprecated
    public int getLimitStyle() {
        return LIMIT_STYLE_NONE;
    }

    /**
     * This method is used to format any date string.
     * Database can use different default date formats.
     *
     * @param date the Date to format
     * @return The proper date formatted String.
     */
    public String getDateString(final Date date) {
        Timestamp ts = null;
        if (date instanceof Timestamp) {
            ts = (Timestamp) date;
        } else {
            ts = new Timestamp(date.getTime());
        }
        return "{ts '" + ts + "'}";
    }

    /**
     * This method is used to format a boolean string.
     *
     * @param b the Boolean to format
     * @return The proper date formatted String.
     */
    public String getBooleanString(final Boolean b) {
        return Boolean.TRUE.equals(b) ? "1" : "0";
    }

    /**
     * Whether ILIKE should be used for case insensitive like clauses.
     *
     * As most databases do not use ILIKE, this implementation returns false.
     * This behaviour may be overwritten in subclasses.
     *
     * @return true if ilike should be used for case insensitive likes,
     *         false if ignoreCase should be applied to the compared strings.
     */
    public boolean useIlike() {
        return false;
    }

    /**
     * Whether an escape clause in like should be used.
     * Example : select * from AUTHOR where AUTHOR.NAME like '\_%' ESCAPE '\';
     *
     * As most databases do not need the escape clause, this implementation
     * always returns <code>false</code>. This behaviour can be overwritten
     * in subclasses.
     *
     * @return whether the escape clause should be appended or not.
     */
    public boolean useEscapeClauseForLike() {
        return false;
    }
}
