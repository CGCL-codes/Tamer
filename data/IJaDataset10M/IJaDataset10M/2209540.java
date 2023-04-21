package com.driller.controller;

import java.util.List;
import java.util.Map;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.driller.controller.db.DatabaseHelper;

public class SpringConfigurationController {

    private static ClassPathXmlApplicationContext applicationContext;

    private static final String LOCK = new String("SpringConfigurationController");

    private SpringConfigurationController() {
    }

    static {
        synchronized (LOCK) {
            if (applicationContext == null) {
                applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <X> X getSpringBean(final Class<X> beanType) {
        final Map<String, X> beansOfType = applicationContext.getBeansOfType(beanType);
        if (beansOfType.isEmpty()) {
            throw new IllegalArgumentException("Beantype matched no beans: " + beanType);
        }
        if (beansOfType.size() > 1) {
            throw new IllegalArgumentException("Beantype matched too many beans: " + beanType);
        }
        return beansOfType.values().iterator().next();
    }

    /**
   * Helper method that will invoke an sql statement. The statement can contain
   * ?, in which case an object array containing the parameters must be passed
   * in.
   * 
   * @param sql a valid sql statement, containing parameter placeholder
   *          characters
   * @param params an array of parameters for each placeholder character in the
   *          statement
   * @return the number of records affected by statement
   */
    protected int executeSql(String sql, Object... params) {
        final DatabaseHelper helper = getDatabaseHelper();
        return helper.executeSql(sql, params);
    }

    public static DatabaseHelper getDatabaseHelper() {
        return (DatabaseHelper) applicationContext.getBean("databaseHelper");
    }

    /**
   * Helper method that will invoke an sql statement. This statement cannot
   * contain any parameter placeholder characters.
   * 
   * @param sql a valid sql statement, containing no parameter placeholder
   *          characters
   * @return the number of records affected by statement
   */
    protected int executeSql(String sql) {
        final DatabaseHelper helper = getDatabaseHelper();
        return helper.executeSql(sql);
    }

    /**
   * Execute an SQL query and return a Map of results. This is to be used in
   * conjunction with the assertSqlResult statements.
   * 
   * @see #executeQuery(String, Object...)
   * @param sql the SQL to run
   * @return A list of Maps representing the database rows.
   */
    protected List<Map<String, Object>> executeQuery(String sql) {
        return executeQuery(sql, new Object[] {});
    }

    /**
   * Execute an SQL query and return a Map of results. This is to be used in
   * conjunction with the assertSqlResult statements
   * 
   * @param sql the SQL to run
   * @param args arguments to the SQL, replaces "?" in order
   * @return A list of Maps representing the database rows.
   */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected List<Map<String, Object>> executeQuery(String sql, Object... args) {
        final DatabaseHelper helper = getDatabaseHelper();
        final List result;
        if (args != null) {
            result = helper.executeQueryForList(sql, args);
        } else {
            result = helper.executeQueryForList(sql);
        }
        return result;
    }

    /**
   * Create a list of argument types to pass into the jdbc template along side
   * the parameters. This ensures the jdbc driver is able to handle nulls
   * properly
   * 
   * @param params the list of parameter values
   * @return a list of parameter object types
   */
    protected static int[] getArgumentTypes(Object[] params) {
        final DatabaseHelper helper = getDatabaseHelper();
        return helper.getArgumentTypes(params);
    }
}
