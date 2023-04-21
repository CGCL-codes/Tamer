package org.josef.dmc;

import static org.josef.annotations.Status.Stage.PRODUCTION;
import static org.josef.annotations.Status.UnitTests.COMPLETE;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import org.josef.annotations.Review;
import org.josef.annotations.Reviews;
import org.josef.annotations.Status;
import org.josef.util.CDebug;
import org.josef.util.CEnumeration;
import org.josef.util.CString;
import org.josef.util.CodeDescription;
import org.josef.util.LocalizableEnumeration;

/**
 * Utility class for creating SQL statements.
 * @author Kees Schotanus
 * @version 1.0 $Revision: 2974 $
 */
@Status(stage = PRODUCTION, unitTests = COMPLETE)
@Reviews({ @Review(by = "Kees Schotanus", at = "2009-04-13") })
public final class SqlStatementUtil {

    /**
     * Private constructor prevents creation of an instance outside this class.
     */
    private SqlStatementUtil() {
    }

    /**
     * Determines whether the supplied expression contains a SQL wildcard
     * character.
     * @param expression Expression to check for SQL wildcard characters.
     * @return True when the supplied expression contains one or more SQL
     *  wildcard characters, false otherwise.
     * @throws NullPointerException When the supplied expression is null.
     */
    public static boolean containsWildcard(final String expression) {
        CDebug.checkParameterNotNull(expression, "parameter");
        return expression.indexOf('_') >= 0 || expression.indexOf('%') >= 0;
    }

    /**
     * Creates a SQL parameter that can be used in a SQL LIKE clause suitable
     * for selecting columns that start with the supplied expression.
     * <br>This method adds the "%" wildcard to end of the supplied expression
     * unless the supplied expression already contains a "%" wildcard character
     * as the last character.
     * @param expression The SQL expression.
     * @return A SQL expression that can be used in a SQL LIKE clause to fetch
     *  columns that start with the supplied expression.
     * @throws NullPointerException When the supplied expression is null.
     */
    public static String createStartsWithParameter(final String expression) {
        CDebug.checkParameterNotNull(expression, "expression");
        return expression.endsWith("%") ? expression : expression + "%";
    }

    /**
     * Creates a SQL parameter that can be used in a SQL LIKE clause suitable
     * for selecting columns that end with the supplied expression.
     * <br>This method adds the "%" wildcard at the beginning of the supplied
     * expression unless the supplied expression already contains a "%" wildcard
     * character as the first character.
     * @param expression The SQL expression.
     * @return A SQL expression that can be used in a SQL LIKE clause to fetch
     *  columns that end with the supplied expression.
     * @throws NullPointerException When the supplied expression is null.
     */
    public static String createEndsWithParameter(final String expression) {
        CDebug.checkParameterNotNull(expression, "expression");
        return expression.startsWith("%") ? expression : "%" + expression;
    }

    /**
     * Creates an IN clause for a SQL statement.
     * @param databaseColumn The database column as it should appear in the
     *  SQL statement.
     *  <br>It's the responsibility of the programmer that the supplied
     *  databaseColumn is valid within the SQL statement. Examples:
     *  <ul>
     *    <li>TABLE_NAME.COLUMN_NAME</li>
     *    <li>ALIAS.COLUMN_NAME</li>
     *    <li>COLUMN_NAME</li>
     *  </ul>
     * @param values The values to appear in the IN clause.
     *  <br>All values should probably be of the same type but this is not
     *  enforced by this method.
     * @return An IN clause.
     * @throws IllegalArgumentException When either the supplied databaseColumn
     *  or the supplied values array is empty.
     * @throws NullPointerException When either the supplied databaseColumn or
     *  the supplied values array is null.
     */
    public static String createInClause(final String databaseColumn, final Object... values) {
        CDebug.checkParameterNotEmpty(databaseColumn, "databaseColumn");
        CDebug.checkParameterNotEmpty(values, "values");
        final StringBuilder inClause = new StringBuilder();
        inClause.append(databaseColumn).append(" IN(");
        inClause.append(createCommaSeparatedValues(values));
        inClause.append(")");
        return inClause.toString();
    }

    /**
     * Creates a NOT IN clause for a SQL statement.
     * @param databaseColumn The database column as it should appear in the
     *  SQL statement.
     *  <br>It's the responsibility of the programmer that the supplied
     *  databaseColumn is valid within the SQL statement. Examples:
     *  <ul>
     *    <li>TABLE_NAME.COLUMN_NAME</li>
     *    <li>ALIAS.COLUMN_NAME</li>
     *    <li>COLUMN_NAME</li>
     *  </ul>
     * @param values The values to appear in the NOT IN clause.
     *  <br>All values should probably be of the same type but this is not
     *  enforced by this method.
     * @return A NOT IN clause.
     * @throws IllegalArgumentException When either the supplied databaseColumn
     *  or the supplied values array is empty.
     * @throws NullPointerException When either the supplied databaseColumn or
     *  the supplied values array is null.
     */
    public static String createNotInClause(final String databaseColumn, final Object... values) {
        CDebug.checkParameterNotEmpty(databaseColumn, "databaseColumn");
        CDebug.checkParameterNotEmpty(values, "values");
        final StringBuilder inClause = new StringBuilder();
        inClause.append(databaseColumn).append(" NOT IN(");
        inClause.append(createCommaSeparatedValues(values));
        inClause.append(")");
        return inClause.toString();
    }

    /**
     * Quotes a SQL value.
     * <br>Dates and Strings are surrounded with single quotes. String values
     * that already contain single quotes are treated specially by replacing the
     * contained single quotes by two single quotes. All other types are
     * returned as: value.toString();<br>
     * Note: Be careful with dates, you probably want to supply dates in the
     * proper string format instead of an actual Date Object.
     * @param value The value to quote.
     * @return The properly quoted, supplied value.
     * @throws NullPointerException When the supplied value is null.
     */
    public static String quoteSqlValue(final Object value) {
        CDebug.checkParameterNotNull(value, "value");
        String result = value.toString();
        if (value instanceof String) {
            result = "'" + CString.replace(result, "'", "''") + ("'");
        } else if (value instanceof Date) {
            result = "'" + result + "'";
        }
        return result;
    }

    /**
     * Creates a list of comma separated (database column) values.
     * <br>Each value will be properly (single) quoted when applicable.
     * @param values The values that must be comma separated.
     * @return A list of comma separated values, which can possibly be empty,
     *  but never null.
     */
    public static String createCommaSeparatedValues(final Object... values) {
        if (values == null || values.length == 0) {
            return "";
        }
        final StringBuilder csv = new StringBuilder(quoteSqlValue(values[0]));
        for (int i = 1; i < values.length; ++i) {
            csv.append(",").append(quoteSqlValue(values[i]));
        }
        return csv.toString();
    }

    /**
     * Creates a String which can be used as an argument to Oracle's decode
     * function.
     * <br>The syntax of the decode function is:
     * {@code decode(expression, code, value [,code, value]... [,defaultValue])}
     * <br>Note: This method does not handle the defaultValue. For a method that
     * does, see: {@link #createDecodeArgument(String, Collection, String)}.
     * @param <T> The type of the localizable enumeration.
     * @param expression The decode expression.
     * @param localizableEnumeration The localizable enumeration to be used in
     *  the decode function.
     * @param locale The locale to localize the descriptions of the supplied
     *  constants.
     *  <br>When null is supplied, the default Locale is used.
     * @return A String which can be used as an argument to Oracle's decode
     *  function.
     * @throws NullPointerException When either the supplied expression or
     *  localizableEnumeration is null.
     * @throws IllegalArgumentException When the supplied expression is empty.
     */
    public static <T extends Enum<T> & LocalizableEnumeration> String createDecodeArgument(final String expression, final Class<T> localizableEnumeration, final Locale locale) {
        CDebug.checkParameterNotEmpty(expression, "expression");
        CDebug.checkParameterNotNull(localizableEnumeration, "localizableEnumeration");
        return createDecodeArgument(expression, CEnumeration.getCodeDescriptions(localizableEnumeration, locale), null);
    }

    /**
     * Creates a String which can be used as an argument to Oracle's decode
     * function.
     * <br>The syntax of the decode function is:
     * {@code decode(expression, code, value [,code, value]... [,defaultValue])}
     * @param expression The decode expression.
     * @param codeDescriptions The code/description pairs to be used in the
     *  decode function.
     * @param defaultValue The optional default value.
     * @return A String which can be used as an argument to Oracle's decode
     *  function.
     * @throws NullPointerException When either the supplied expression or
     *  codeDescriptions is null.
     * @throws IllegalArgumentException When the supplied expression or
     *  codeDescriptions is empty.
     */
    public static String createDecodeArgument(final String expression, final Collection<? extends CodeDescription> codeDescriptions, final String defaultValue) {
        CDebug.checkParameterNotEmpty(expression, "expression");
        CDebug.checkParameterNotEmpty(codeDescriptions, "codeDescriptions");
        final StringBuilder result = new StringBuilder(expression);
        for (final CodeDescription codeDescription : codeDescriptions) {
            result.append(",").append(codeDescription.getCode().toString());
            result.append(",").append(SqlStatementUtil.quoteSqlValue(codeDescription.getDescription()));
        }
        if (CString.isNotEmpty(defaultValue)) {
            result.append(",").append(SqlStatementUtil.quoteSqlValue(defaultValue));
        }
        return result.toString();
    }

    /**
     * Creates a SQL statement to call a stored procedure.
     * @param name Name of the stored procedure.
     * @param parameters The (optional) parameters.
     * @return A SQL statement to call a stored procedure.
     * @throws NullPointerException When the supplied name is null.
     */
    public static String createStoredProcedureCall(final String name, final StoredProcedureParameter... parameters) {
        CDebug.checkParameterNotEmpty(name, "name");
        final StringBuilder sqlStatement = new StringBuilder("{call ");
        sqlStatement.append(name);
        if (parameters != null && parameters.length > 0) {
            sqlStatement.append("(?");
            for (int i = 1; i < parameters.length; ++i) {
                sqlStatement.append(",?");
            }
            sqlStatement.append(")");
        }
        sqlStatement.append("}");
        return sqlStatement.toString();
    }
}
