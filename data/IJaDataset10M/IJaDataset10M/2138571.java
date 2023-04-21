package net.sourceforge.fluxion.datapublisher.sql.impl;

import net.sourceforge.fluxion.annotations.*;
import net.sourceforge.fluxion.datasource.filter.Filter;
import net.sourceforge.fluxion.datasource.filter.impl.*;
import net.sourceforge.fluxion.datasource.query.Query;
import net.sourceforge.fluxion.datasource.query.QueryGenerationException;
import net.sourceforge.fluxion.datasource.query.QueryGenerator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Generates a {@link Query} that can be executed against a MySQL database.
 *
 * @author Tony Burdett
 * @version 0.2
 * @date 03-Jul-2006
 * @see SQLQuery
 */
public abstract class SQLQueryFactory implements QueryGenerator {

    private static final String EQUALS = "=";

    private static final String LESS_THAN = "<";

    private static final String GREATER_THAN = ">";

    private String dbUri;

    private String user;

    private String password;

    public SQLQueryFactory(String dbUrl, String user, String password) {
        this.dbUri = dbUrl;
        this.user = user;
        this.password = password;
    }

    public <F> Query<F> generateQuery(Filter<F> filter) throws QueryGenerationException {
        Map<String, Set<String>> clauses = interpretFilter(filter, null, new HashMap<String, String>());
        if (filter.getClass().isAnnotationPresent(Bean.class)) {
            Class beanClass = null;
            try {
                beanClass = Class.forName(filter.getClass().getAnnotation(Bean.class).value());
                SQLQuery<F> query = makeQueryInstance(beanClass, filter, dbUri, user, password);
                for (String select : clauses.get(SELECT_CLAUSES)) {
                    query.addSelectClause(select);
                }
                for (String from : clauses.get(FROM_CLAUSES)) {
                    query.addFromClause(from);
                }
                for (String where : clauses.get(WHERE_CLAUSES)) {
                    query.addWhereClause(where);
                }
                return query;
            } catch (ClassNotFoundException e) {
                throw new QueryGenerationException("Can't find the @Bean-specified class", e);
            } catch (Exception e) {
                throw new QueryGenerationException("Inconsistent types: filtered class doesn't match " + beanClass.getSimpleName(), e);
            }
        } else {
            throw new QueryGenerationException("No @Bean annotation on filter of type " + filter.getClass().getName());
        }
    }

    /**
   * Instantiate a new query instance from the parameters given.  This
   * effectively defers the decision about the type of the Query to be
   * instantiated to the concrete implementations of this class.  Usually,
   * implementations of this class will be extremely simple - a single line to
   * instantiate a new concrete implementation of the appropriate class.
   *
   * @param beanClass the beanClass a Query is needed for
   * @param filter    the filter used to make this query
   * @param dbUrl     the URL of the database
   * @param user      the username to use to connect to the database
   * @param password  the password to connect to the database with @return a new
   *                  SQLQuery instance of appropriate type
   * @return the SQLQuery that can be executed
   */
    protected abstract <F> SQLQuery<F> makeQueryInstance(Class<F> beanClass, Filter filter, String dbUrl, String user, String password);

    private static final String SELECT_CLAUSES = "selects";

    private static final String FROM_CLAUSES = "froms";

    private static final String WHERE_CLAUSES = "wheres";

    private Map<String, Set<String>> interpretFilter(Filter filter, String joiningTo, Map<String, String> tableAliases) throws QueryGenerationException {
        Map<String, Set<String>> clauseMap = new HashMap<String, Set<String>>();
        clauseMap.put(SELECT_CLAUSES, new HashSet<String>());
        clauseMap.put(FROM_CLAUSES, new HashSet<String>());
        clauseMap.put(WHERE_CLAUSES, new HashSet<String>());
        if (filter instanceof All) {
            clauseMap.get(WHERE_CLAUSES).add(joiningTo + " IS NOT NULL");
            return clauseMap;
        } else if (filter instanceof And) {
            And and = (And) filter;
            String whereClause = "(";
            int i = 1;
            for (Object o : and.getValues()) {
                Filter value = (Filter) o;
                Map<String, Set<String>> resultMap = interpretFilter(value, joiningTo, tableAliases);
                for (String select : resultMap.get(SELECT_CLAUSES)) {
                    clauseMap.get(SELECT_CLAUSES).add(select);
                }
                for (String from : resultMap.get(FROM_CLAUSES)) {
                    clauseMap.get(FROM_CLAUSES).add(from);
                }
                if (resultMap.get(WHERE_CLAUSES).size() > 1) {
                    whereClause = whereClause + "(";
                }
                int j = 1;
                for (String where : resultMap.get(WHERE_CLAUSES)) {
                    whereClause = whereClause + where;
                    if (j < resultMap.get(WHERE_CLAUSES).size()) {
                        whereClause = whereClause + " AND ";
                    }
                    j++;
                }
                if (resultMap.get(WHERE_CLAUSES).size() > 1) {
                    whereClause = whereClause + ")";
                }
                if (i < and.getValues().size()) {
                    whereClause = whereClause + " AND ";
                }
                i++;
            }
            whereClause = whereClause + ")";
            clauseMap.get(WHERE_CLAUSES).add(whereClause);
            return clauseMap;
        }
        if (filter instanceof ExactValue) {
            ExactValue exact = (ExactValue) filter;
            String operator = EQUALS;
            String value = exact.getValue().toString();
            clauseMap.get(WHERE_CLAUSES).add(joiningTo + " " + operator + " \'" + value + "\'");
            return clauseMap;
        } else if (filter instanceof GreaterThan) {
            GreaterThan greater = (GreaterThan) filter;
            String operator = GREATER_THAN;
            Number value = greater.getValue();
            clauseMap.get(WHERE_CLAUSES).add(joiningTo + " " + operator + " \'" + value + "\'");
            return clauseMap;
        } else if (filter instanceof Join) {
            Join join = (Join) filter;
            String operator = EQUALS;
            String className = join.getBeanClassName();
            String simpleClassName = className.substring(className.lastIndexOf(".") + 1, className.length());
            String table = simpleClassName.replace("Bean", "").toLowerCase();
            String param = join.getBeanMethodName().replace("get", "").toLowerCase();
            String value = table + "." + param;
            clauseMap.get(WHERE_CLAUSES).add(joiningTo + " " + operator + " \'" + value + "\'");
            return clauseMap;
        } else if (filter instanceof LessThan) {
            LessThan less = (LessThan) filter;
            String operator = LESS_THAN;
            Number value = less.getValue();
            clauseMap.get(WHERE_CLAUSES).add(joiningTo + " " + operator + " \'" + value + "\'");
            return clauseMap;
        } else if (filter instanceof None) {
            clauseMap.get(WHERE_CLAUSES).add(joiningTo + " IS NULL");
            return clauseMap;
        } else if (filter instanceof Or) {
            Or or = (Or) filter;
            String whereClause = "(";
            int i = 1;
            for (Object o : or.getValues()) {
                Filter value = (Filter) o;
                Map<String, Set<String>> resultMap = interpretFilter(value, joiningTo, tableAliases);
                for (String select : resultMap.get(SELECT_CLAUSES)) {
                    clauseMap.get(SELECT_CLAUSES).add(select);
                }
                for (String from : resultMap.get(FROM_CLAUSES)) {
                    clauseMap.get(FROM_CLAUSES).add(from);
                }
                if (resultMap.get(WHERE_CLAUSES).size() > 1) {
                    whereClause = whereClause + "(";
                }
                int j = 1;
                for (String where : resultMap.get(WHERE_CLAUSES)) {
                    whereClause = whereClause + where;
                    if (j < resultMap.get(WHERE_CLAUSES).size()) {
                        whereClause = whereClause + " AND ";
                    }
                    j++;
                }
                if (resultMap.get(WHERE_CLAUSES).size() > 1) {
                    whereClause = whereClause + ")";
                }
                if (i < or.getValues().size()) {
                    whereClause = whereClause + " OR ";
                }
                i++;
            }
            whereClause = whereClause + ")";
            clauseMap.get(WHERE_CLAUSES).add(whereClause);
            return clauseMap;
        } else {
            if (filter.getClass().isAnnotationPresent(Bean.class)) {
                try {
                    Class<?> beanClass = Class.forName(filter.getClass().getAnnotation(Bean.class).value());
                    String table = "";
                    String tableRole = "";
                    if (beanClass.isAnnotationPresent(TableNameAnn.class)) {
                        table = beanClass.getAnnotation(TableNameAnn.class).value();
                        if (tableAliases.get(table) != null) {
                            tableRole = tableAliases.get(table);
                        } else {
                            tableRole = table;
                        }
                    } else {
                        throw new QueryGenerationException("The Filter points to a " + "BeanClass which has no associated Table (no @TableNameAnn)");
                    }
                    for (Method filterMethod : filter.getClass().getMethods()) {
                        if (filterMethod.getName().startsWith("get")) {
                            if (filterMethod.isAnnotationPresent(BeanMethod.class)) {
                                String beanMethodName = filterMethod.getAnnotation(BeanMethod.class).value();
                                if (!beanMethodName.startsWith("get")) {
                                    throw new QueryGenerationException("Unmatch bean methods - Filter method is a getter," + " but associated bean method isn't!");
                                }
                                Method beanMethod = null;
                                try {
                                    beanMethod = beanClass.getMethod(beanMethodName);
                                } catch (NoSuchMethodException e) {
                                    throw new QueryGenerationException("Can't find the method on the bean matching " + "the filter annotation", e);
                                }
                                if (beanMethod.isAnnotationPresent(DataPropertyAnn.class)) {
                                    if (tableRole == table) {
                                        clauseMap.get(FROM_CLAUSES).add(table);
                                    } else {
                                        clauseMap.get(FROM_CLAUSES).add(table + " " + tableRole);
                                    }
                                    clauseMap.get(SELECT_CLAUSES).add(tableRole.concat("." + beanMethodName.replaceFirst("get", "")).toLowerCase());
                                    try {
                                        Object o = filterMethod.invoke(filter);
                                        if (o != null && o instanceof Filter) {
                                            Filter<?> result = (Filter) o;
                                            String param = tableRole + "." + beanMethod.getName().replaceFirst("get", "").toLowerCase();
                                            Map<String, Set<String>> resultMap = interpretFilter(result, param, tableAliases);
                                            for (String select : resultMap.get(SELECT_CLAUSES)) {
                                                clauseMap.get(SELECT_CLAUSES).add(select);
                                            }
                                            for (String from : resultMap.get(FROM_CLAUSES)) {
                                                clauseMap.get(FROM_CLAUSES).add(from);
                                            }
                                            for (String where : resultMap.get(WHERE_CLAUSES)) {
                                                clauseMap.get(WHERE_CLAUSES).add(where);
                                            }
                                        }
                                    } catch (IllegalAccessException e) {
                                        throw new QueryGenerationException("Unable to access a method on the filter class", e);
                                    } catch (InvocationTargetException e) {
                                        throw new QueryGenerationException("Unable to invoke a method on the filter class", e);
                                    }
                                } else if (beanMethod.isAnnotationPresent(ObjectPropertyAnn.class)) {
                                    if (tableRole == table) {
                                        clauseMap.get(FROM_CLAUSES).add(table);
                                    } else {
                                        clauseMap.get(FROM_CLAUSES).add(table + " " + tableRole);
                                    }
                                    for (Method m : beanClass.getMethods()) {
                                        if (m.isAnnotationPresent(ExternalLinks.class)) {
                                            for (ExternalLink link : m.getAnnotation(ExternalLinks.class).value()) {
                                                if (link.role().equals(beanMethodName)) {
                                                    clauseMap.get(SELECT_CLAUSES).add(tableRole.concat("." + m.getName().replaceFirst("get", "")).toLowerCase());
                                                }
                                            }
                                        }
                                    }
                                    try {
                                        Object o = filterMethod.invoke(filter);
                                        if (o != null && o instanceof Filter) {
                                            Filter<?> result = (Filter) o;
                                            Class<?> linkedBeanClass = beanMethod.getReturnType();
                                            if (linkedBeanClass == Set.class) {
                                                Type type = beanMethod.getGenericReturnType();
                                                if (type instanceof ParameterizedType) {
                                                    Type[] generics = ((ParameterizedType) type).getActualTypeArguments();
                                                    if (generics.length == 1) {
                                                        Type t = generics[0];
                                                        linkedBeanClass = (Class) t;
                                                    } else {
                                                        throw new QueryGenerationException("The Filter (" + filter + ") refers to a set, but " + "unwrapping the type of the set failed " + "(not exactly 1 generic class type)");
                                                    }
                                                } else {
                                                    throw new QueryGenerationException("The Filter (" + filter + ") refers to a set, but " + "unwrapping the type of the set failed " + "(no generic class type declared)");
                                                }
                                            }
                                            String linkedTable = "";
                                            String linkedTableRole = "";
                                            if (linkedBeanClass.isAnnotationPresent(TableNameAnn.class)) {
                                                linkedTable = linkedBeanClass.getAnnotation(TableNameAnn.class).value();
                                                if (beanMethod.getName().contains("Bean_role_")) {
                                                    linkedTableRole = beanMethod.getName().substring(beanMethod.getName().lastIndexOf("Bean_role_") + 5, beanMethod.getName().lastIndexOf("_"));
                                                } else {
                                                    linkedTableRole = linkedTable;
                                                }
                                            } else {
                                                throw new QueryGenerationException("The Filter (" + filter + ") refers to a BeanClass (" + linkedBeanClass + ") which has no associated " + "Table (no @TableNameAnn)");
                                            }
                                            if (linkedTableRole != linkedTable) {
                                                tableAliases.put(linkedTable, linkedTableRole);
                                            }
                                            Map<String, Set<String>> resultMap = interpretFilter(result, null, tableAliases);
                                            for (String select : resultMap.get(SELECT_CLAUSES)) {
                                                clauseMap.get(SELECT_CLAUSES).add(select);
                                            }
                                            for (String from : resultMap.get(FROM_CLAUSES)) {
                                                clauseMap.get(FROM_CLAUSES).add(from);
                                            }
                                            for (String where : resultMap.get(WHERE_CLAUSES)) {
                                                clauseMap.get(WHERE_CLAUSES).add(where);
                                            }
                                            for (Method m : beanClass.getMethods()) {
                                                if (m.isAnnotationPresent(ExternalLinks.class)) {
                                                    for (ExternalLink link : m.getAnnotation(ExternalLinks.class).value()) {
                                                        if (link.role().matches(beanMethod.getName())) {
                                                            String leftClause = tableRole + "." + m.getName().replaceFirst("get", "").toLowerCase();
                                                            String rightClause = linkedTableRole + "." + link.linksTo().replaceFirst("get", "").toLowerCase();
                                                            clauseMap.get(WHERE_CLAUSES).add(leftClause + "=" + rightClause);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } catch (IllegalAccessException e) {
                                        throw new QueryGenerationException("Unable to access a method on the filter class", e);
                                    } catch (InvocationTargetException e) {
                                        throw new QueryGenerationException("Unable to invoke a method on the filter class", e);
                                    }
                                }
                            }
                        }
                    }
                } catch (ClassNotFoundException e) {
                    throw new QueryGenerationException("Unable to find the @Bean class on a linked filter", e);
                }
                return clauseMap;
            }
        }
        throw new QueryGenerationException("Unrecognised filter type for SQL: " + filter + "; " + filter.getClass().getSimpleName());
    }
}
