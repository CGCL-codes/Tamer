package lang;

/**
 * User: Test
 * Date: 15.03.2008
 * Time: 20:47:20
 */
public class SQLQueryColumnNotFoundException extends Throwable {

    public SQLQueryColumnNotFoundException(String table, String column) {
        super("Column " + column + " does not exist in table " + table);
    }
}
