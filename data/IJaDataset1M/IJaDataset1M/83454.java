package org.jaqlib;

import javax.sql.DataSource;
import org.jaqlib.core.WhereClause;
import org.jaqlib.core.WhereCondition;
import org.jaqlib.core.bean.BeanFactory;
import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.core.bean.BeanMappingStrategy;
import org.jaqlib.db.ColumnMapping;
import org.jaqlib.db.DbDefaults;
import org.jaqlib.db.DbDeleteDataSource;
import org.jaqlib.db.DbFromClause;
import org.jaqlib.db.DbInsertDataSource;
import org.jaqlib.db.DbSelectDataSource;
import org.jaqlib.db.DbUpdateDataSource;
import org.jaqlib.db.DeleteFromClause;
import org.jaqlib.db.InClause;
import org.jaqlib.db.IntoClause;
import org.jaqlib.db.Using;

/**
 * Helper class with static methods for database query support (see
 * {@link DatabaseQueryBuilder} for further information).
 * 
 * <p>
 * This class provides static helper methods to easily execute queries against
 * databases via JDBC. <br>
 * Examples are given here: {@link DatabaseQueryBuilder}.
 * </p>
 * This class is thread-safe.
 * 
 * @see DatabaseQueryBuilder
 * @see Database
 * @see DbDefaults
 * @author Werner Fragner
 */
public class DatabaseQB {

    /**
   * This class is not intended to be instantiated.
   */
    private DatabaseQB() {
        throw new UnsupportedOperationException();
    }

    /**
   * Holds singleton instances per thread.
   */
    private static final ThreadLocal<DatabaseQueryBuilder> QUERYBUILDER = new ThreadLocal<DatabaseQueryBuilder>();

    /**
   * Sets a user-defined classloader that is used when creating proxy classes
   * using the {@link #getRecorder(Class)} method.
   * 
   * @param classLoader a not null class loader.
   */
    public static void setClassLoader(ClassLoader classLoader) {
        getQueryBuilder().setClassLoader(classLoader);
    }

    /**
   * @param <T> the type of the result bean.
   * @param beanClass a not null bean class where to store the result of the
   *          query.
   * @return a proxy object that records all method calls. These calls are used
   *         when evaluating the WHERE clause of a query (see examples).
   */
    public static <T> T getRecorder(Class<T> beanClass) {
        return getQueryBuilder().getRecorder(beanClass);
    }

    /**
   * <p>
   * Selects one column of a given database SELECT statement. The SELECT
   * statement that should be used must be specified in the returned
   * {@link DbFromClause}. The {@link DbFromClause} hereby returns a
   * {@link WhereClause} that can be used to specify an arbitrary WHERE
   * condition. This WHERE condition supports AND and OR connectors, the
   * evaluation of custom {@link WhereCondition}s and custom conditions using a
   * method call recording mechanism (see examples and
   * {@link #getRecorder(Class)} for further details).
   * </p>
   * <p>
   * <b>NOTE: the WHERE condition is not executed at database-side but at Java
   * side. Avoid executing SELECT statements with lots of data and then
   * constraining it with the WHERE functionality of JaqLib!</b>.
   * </p>
   * 
   * @param <T> the result column type.
   * @param columnMapping an object defining the desired column.
   * @return the FROM clause to specify the database SELECT statement for the
   *         query.
   */
    public static <T> DbFromClause<T> select(ColumnMapping<T> columnMapping) {
        return getQueryBuilder().select(columnMapping);
    }

    /**
   * <p>
   * Convenience method for selecting a specific column from a table without
   * type safety (that means that this method returns an <tt>Object</tt>).
   * </p>
   * For further details see {@link #select(ColumnMapping)}.
   * 
   * @param columnName the name of the column to select. This name can also be
   *          an alias defined in the SELECT clause of the statement.
   * @return the FROM clause to specify the database SELECT statement for the
   *         query.
   */
    public static DbFromClause<Object> select(String columnName) {
        return select(new ColumnMapping<Object>(columnName));
    }

    /**
   * <p>
   * Uses a given database SELECT statement to fill a user-defined Java bean.
   * The SELECT statement that should be used must be specified in the returned
   * {@link DbFromClause}. The {@link DbFromClause} hereby returns a
   * {@link WhereClause} that can be used to specify an arbitrary WHERE
   * condition. This WHERE condition supports AND and OR connectors, the
   * evaluation of custom {@link WhereCondition}s and custom conditions using a
   * method call recording mechanism (see examples and
   * {@link #getRecorder(Class)} for further details).
   * </p>
   * <p>
   * <b>NOTE: the WHERE condition is not executed at database-side but at Java
   * side. Avoid executing SELECT statements with lots of data and then
   * constraining it with the WHERE functionality of JaqLib!</b>.
   * </p>
   * 
   * @param <T> the result bean type.
   * @param beanClass the desired result bean. This bean must provide a default
   *          constructor. If the bean does not provide one a custom
   *          {@link BeanFactory} must be registered at the {@link BeanMapping}.
   *          This {@link BeanMapping} can be obtained from {@link Database} and
   *          can be used with {@link #select(BeanMapping)}.
   * @return the FROM clause to specify the database SELECT statement for the
   *         query.
   */
    public static <T> DbFromClause<T> select(Class<T> beanClass) {
        return getQueryBuilder().select(beanClass);
    }

    /**
   * This method basically provides the same functionality as
   * {@link #select(Class)}. But it gives more flexibility in defining the
   * mapping between SELECT statement results to Java bean instance fields. This
   * mapping can defined with a {@link BeanMapping} instance. For building these
   * instances see {@link Database}.
   * 
   * @param <T> the result bean type.
   * @param beanMapping a bean definition that holds information how to map the
   *          result of the SELECT statement to a Java bean.
   * @return the FROM clause to specify the database SELECT statement for the
   *         query.
   */
    public static <T> DbFromClause<T> select(BeanMapping<T> beanMapping) {
        return getQueryBuilder().select(beanMapping);
    }

    /**
   * Inserts the given bean into the table that must be specified in the
   * returned {@link IntoClause}. The mapping between the given bean field
   * values to the according table columns must be specified in the
   * {@link Using} object that is returned by
   * {@link IntoClause#into(org.jaqlib.db.DbInsertDataSource)}.
   * 
   * @param <T> the type of the bean.
   * @param bean a not null bean to insert.
   * @return the INTO clause to specify the table where to insert the bean.
   */
    public static <T> IntoClause<T> insert(T bean) {
        return getQueryBuilder().insert(bean);
    }

    /**
   * Updates the given bean in the table that must be specified in the returned
   * {@link InClause}. The mapping between the given bean field values to the
   * according table columns must be specified in the {@link Using} object that
   * is returned by {@link InClause#in(org.jaqlib.db.DbUpdateDataSource)}.
   * 
   * @param <T> the type of the bean.
   * @param bean a not null bean to update.
   * @return the IN clause to specify the table where to update the bean.
   */
    public static <T> InClause<T> update(T bean) {
        return getQueryBuilder().update(bean);
    }

    /**
   * Deletes a table that is specified in the returned {@link DeleteFromClause}.
   * 
   * @return the FROM clause to specify the table where to delete records.
   */
    public static DeleteFromClause delete() {
        return getQueryBuilder().delete();
    }

    /**
   * @param dataSource a not null {@link DataSource} for obtaining a JDBC
   *          connection.
   * @param sql a not null SELECT statement.
   * @return an object representing the source for a database query.
   */
    public static DbSelectDataSource getSelectDataSource(DataSource dataSource, String sql) {
        return Database.getSelectDataSource(dataSource, sql);
    }

    /**
   * @param dataSource a not null {@link DataSource} for obtaining a JDBC
   *          connection.
   * @param tableName the not null name of the table where to insert the record.
   * @return an object representing the source for a (or multiple) database
   *         insert.
   */
    public static DbInsertDataSource getInsertDataSource(DataSource dataSource, String tableName) {
        return Database.getInsertDataSource(dataSource, tableName);
    }

    /**
   * @param dataSource a not null {@link DataSource} for obtaining a JDBC
   *          connection.
   * @param tableName the not null name of the table where to update the record.
   * @param whereClause the where clause of the UPDATE statement without the
   *          WHERE keyword.
   * @return an object representing the source for a (or multiple) database
   *         update.
   */
    public static DbUpdateDataSource getUpdateDataSource(DataSource dataSource, String tableName, String whereClause) {
        return Database.getUpdateDataSource(dataSource, tableName, whereClause);
    }

    /**
   * @param dataSource a not null {@link DataSource} for obtaining a JDBC
   *          connection.
   * @param tableName the not null name of the table where to update the record.
   * @return an object representing the source for a (or multiple) database
   *         update.
   */
    public static DbUpdateDataSource getUpdateDataSource(DataSource dataSource, String tableName) {
        return Database.getUpdateDataSource(dataSource, tableName);
    }

    /**
   * Gets a datasource for deleting a set of records of a table. Which records
   * should be deleted can be specified with a given where clause (without the
   * WHERE keyword).
   * 
   * @param dataSource a not null {@link DataSource} for obtaining a JDBC
   *          connection.
   * @param tableName the not null name of the table where to delete the
   *          records.
   * @param whereClause the where clause of the DELETE statement without the
   *          WHERE keyword.
   * @return an object representing the source for a database table delete.
   */
    public static DbDeleteDataSource getDeleteDataSource(DataSource dataSource, String tableName, String whereClause) {
        return Database.getDeleteDataSource(dataSource, tableName, whereClause);
    }

    /**
   * Gets a datasource for deleting all records of a table.
   * 
   * @param dataSource a not null {@link DataSource} for obtaining a JDBC
   *          connection.
   * @param tableName the not null name of the table where to delete the
   *          records.
   * @return an object representing the source for a database table delete.
   */
    public static DbDeleteDataSource getDeleteDataSource(DataSource dataSource, String tableName) {
        return Database.getDeleteDataSource(dataSource, tableName);
    }

    /**
   * See {@link BeanMapping#build(Class)}.
   */
    public static <T> BeanMapping<T> getDefaultBeanMapping(Class<? extends T> beanClass) {
        return getQueryBuilder().getDefaultBeanMapping(beanClass);
    }

    /**
   * See {@link BeanMapping#build(BeanMappingStrategy, Class)}.
   */
    public static <T> BeanMapping<T> getBeanMapping(BeanMappingStrategy mappingStrategy, Class<? extends T> beanClass) {
        return getQueryBuilder().getBeanMapping(mappingStrategy, beanClass);
    }

    /**
   * See return tag.
   * 
   * @return the query builder instance for the current thread.
   */
    static DatabaseQueryBuilder getQueryBuilder() {
        DatabaseQueryBuilder queryBuilder = QUERYBUILDER.get();
        if (queryBuilder == null) {
            queryBuilder = new DatabaseQueryBuilder();
            QUERYBUILDER.set(queryBuilder);
        }
        return queryBuilder;
    }
}
