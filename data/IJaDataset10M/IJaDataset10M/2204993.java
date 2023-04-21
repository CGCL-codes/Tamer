package com.avaje.ebean.server.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.avaje.ebean.internal.BindParams;
import com.avaje.ebean.internal.SpiExpressionList;
import com.avaje.ebean.internal.SpiQuery;
import com.avaje.ebean.internal.BindParams.OrderedList;
import com.avaje.ebean.server.core.OrmQueryRequest;
import com.avaje.ebean.server.deploy.BeanDescriptor;
import com.avaje.ebean.server.deploy.BeanPropertyAssocMany;
import com.avaje.ebean.server.deploy.DeployParser;
import com.avaje.ebean.server.persist.Binder;
import com.avaje.ebean.server.util.BindParamsParser;
import com.avaje.ebean.util.DefaultExpressionRequest;

/**
 * Compile Query Predicates.
 * <p>
 * This includes the where and having expressions which can be made up of
 * Strings with named parameters or Expression objects.
 * </p>
 * <p>
 * This builds the appropriate bits of where and having clauses and binds the
 * named parameters and expression values into the prepared statement.
 * </p>
 */
public class CQueryPredicates {

    private static final Logger logger = Logger.getLogger(CQueryPredicates.class.getName());

    private final Binder binder;

    private final OrmQueryRequest<?> request;

    private final SpiQuery<?> query;

    private final Object idValue;

    /**
	 * Flag set if this is a SqlSelect type query.
	 */
    private boolean rawSql;

    /**
	 * Named bind parameters.
	 */
    private BindParams bindParams;

    /**
	 * Named bind parameters for the having clause.
	 */
    private OrderedList havingNamedParams;

    /**
	 * Bind values from the where expressions.
	 */
    private ArrayList<Object> whereExprBindValues;

    /**
	 * SQL generated from the where expressions.
	 */
    private String whereExprSql;

    /**
	 * SQL generated from where with named parameters.
	 */
    private String whereRawSql;

    /**
	 * Bind values for having expression.
	 */
    private ArrayList<Object> havingExprBindValues;

    /**
	 * SQL generated from the having expression.
	 */
    private String havingExprSql;

    /**
	 * SQL generated from having with named parameters.
	 */
    private String havingRawSql;

    private String dbHaving;

    /**
	 * logicalWhere with property names converted to db columns.
	 */
    private String dbWhere;

    /**
	 * The order by clause.
	 */
    private String logicalOrderBy;

    private String dbOrderBy;

    /**
	 * Includes from where and order by clauses.
	 */
    private Set<String> predicateIncludes;

    private DeployParser deployParser;

    public CQueryPredicates(Binder binder, OrmQueryRequest<?> request, DeployParser deployParser) {
        this.binder = binder;
        this.request = request;
        this.query = request.getQuery();
        this.deployParser = deployParser;
        bindParams = query.getBindParams();
        idValue = query.getId();
    }

    public String bind(PreparedStatement pstmt) throws SQLException {
        StringBuilder bindLog = new StringBuilder();
        int index = 0;
        if (idValue != null) {
            index = request.getBeanDescriptor().bindId(pstmt, index, idValue);
            bindLog.append(idValue);
        }
        if (bindParams != null) {
            binder.bind(bindParams, index, pstmt, bindLog);
        }
        if (bindParams != null) {
            index = index + bindParams.size();
        }
        if (whereExprBindValues != null) {
            for (int i = 0; i < whereExprBindValues.size(); i++) {
                Object bindValue = whereExprBindValues.get(i);
                binder.bindObject(++index, bindValue, pstmt);
                if (i > 0 || idValue != null) {
                    bindLog.append(", ");
                }
                bindLog.append(bindValue);
            }
        }
        if (havingNamedParams != null) {
            bindLog.append(" havingNamed ");
            binder.bind(havingNamedParams.list(), index, pstmt, bindLog);
            index = index + havingNamedParams.size();
        }
        if (havingExprBindValues != null) {
            bindLog.append(" having ");
            for (int i = 0; i < havingExprBindValues.size(); i++) {
                Object bindValue = havingExprBindValues.get(i);
                binder.bindObject(++index, bindValue, pstmt);
                if (i > 0) {
                    bindLog.append(", ");
                }
                bindLog.append(bindValue);
            }
        }
        return bindLog.toString();
    }

    private void buildBindHavingRawSql(boolean buildSql) {
        if (buildSql || bindParams != null) {
            havingRawSql = query.getAdditionalHaving();
            if (havingRawSql != null && bindParams != null) {
                havingNamedParams = BindParamsParser.parseNamedParams(bindParams, havingRawSql);
                havingRawSql = havingNamedParams.getPreparedSql();
            }
        } else {
        }
    }

    /**
	 * Convert named parameters into an OrderedList.
	 */
    private void buildBindWhereRawSql(boolean buildSql) {
        if (buildSql || bindParams != null) {
            whereRawSql = buildWhereRawSql();
            if (bindParams != null) {
                whereRawSql = BindParamsParser.parse(bindParams, whereRawSql);
            }
        } else {
        }
    }

    private String buildWhereRawSql() {
        String whereRaw = query.getWhere();
        if (whereRaw == null) {
            whereRaw = "";
        }
        String additionalWhere = query.getAdditionalWhere();
        if (additionalWhere != null) {
            whereRaw += additionalWhere;
        }
        return whereRaw;
    }

    /**
	 * This combines the sql from named/positioned parameters and expressions.
	 */
    public void prepare(boolean buildSql, boolean parseRaw) {
        buildBindWhereRawSql(buildSql);
        buildBindHavingRawSql(buildSql);
        SpiExpressionList<?> whereExp = query.getWhereExpressions();
        DefaultExpressionRequest whereExpReq = new DefaultExpressionRequest(request);
        if (whereExp != null) {
            whereExprBindValues = whereExp.buildBindValues(whereExpReq);
            if (buildSql) {
                whereExprSql = whereExp.buildSql(whereExpReq);
            }
        }
        SpiExpressionList<?> havingExpr = query.getHavingExpressions();
        DefaultExpressionRequest havingExpReq = new DefaultExpressionRequest(request);
        if (havingExpr != null) {
            havingExprBindValues = havingExpr.buildBindValues(havingExpReq);
            if (buildSql) {
                havingExprSql = havingExpr.buildSql(havingExpReq);
            }
        }
        if (buildSql) {
            parsePropertiesToDbColumns(parseRaw);
        }
    }

    /**
	 * Parse/Convert property names to database columns in the where and order
	 * by clauses etc.
	 */
    private void parsePropertiesToDbColumns(boolean parseRaw) {
        if (deployParser == null) {
            deployParser = request.getBeanDescriptor().createDeployPropertyParser();
        }
        dbWhere = deriveWhere(parseRaw);
        dbHaving = deriveHaving(parseRaw);
        logicalOrderBy = deriveOrderByWithMany(request.getManyProperty());
        if (logicalOrderBy != null) {
            dbOrderBy = deployParser.parse(logicalOrderBy);
        }
        predicateIncludes = deployParser.getIncludes();
    }

    /**
	 * Replace the table alias place holders.
	 */
    public void parseTableAlias(SqlTreeAlias alias) {
        if (dbWhere != null) {
            dbWhere = alias.parse(dbWhere);
        }
        if (dbHaving != null) {
            dbHaving = alias.parse(dbHaving);
        }
        if (dbOrderBy != null) {
            dbOrderBy = alias.parse(dbOrderBy);
        }
    }

    private String parse(boolean parseRaw, String raw, String expr) {
        if (expr == null || expr.trim().length() == 0) {
            return parseRaw ? deployParser.parse(raw) : raw;
        } else {
            if (parseRaw) {
                return deployParser.parse(raw) + " and " + deployParser.parse(expr);
            } else {
                return raw + " and " + deployParser.parse(expr);
            }
        }
    }

    private String deriveWhere(boolean parseRaw) {
        if (whereRawSql == null || whereRawSql.trim().length() == 0) {
            return deployParser.parse(whereExprSql);
        } else {
            return parse(parseRaw, whereRawSql, whereExprSql);
        }
    }

    private String deriveHaving(boolean parseRaw) {
        if (havingRawSql == null || havingRawSql.trim().length() == 0) {
            return deployParser.parse(havingExprSql);
        } else {
            return parse(parseRaw, havingRawSql, havingExprSql);
        }
    }

    /**
	 * There is a many property so we need to make sure the ordering is
	 * appropriate.
	 */
    private String deriveOrderByWithMany(BeanPropertyAssocMany<?> manyProp) {
        if (manyProp == null) {
            return query.getOrderBy();
        }
        String orderBy = query.getOrderBy();
        BeanDescriptor<?> desc = request.getBeanDescriptor();
        String orderById = desc.getDefaultOrderBy();
        if (orderBy == null) {
            orderBy = orderById;
        }
        String manyOrderBy = manyProp.getFetchOrderBy();
        if (manyOrderBy != null) {
            orderBy = orderBy + " , " + manyProp.getName() + "." + manyOrderBy;
        }
        if (request.isFindById()) {
            return orderBy;
        }
        if (orderBy.startsWith(orderById)) {
            return orderBy;
        }
        int manyPos = orderBy.indexOf(manyProp.getName());
        int idPos = orderBy.indexOf(" " + orderById);
        if (manyPos == -1) {
            return orderBy;
        }
        if (idPos > -1 && idPos < manyPos) {
        } else {
            if (idPos > manyPos) {
                String msg = "A Query on [" + desc + "] includes a join to a 'many' association [" + manyProp.getName();
                msg += "] with an incorrect orderBy [" + orderBy + "]. The id property [" + orderById + "]";
                msg += " must come before the many property [" + manyProp.getName() + "] in the orderBy.";
                msg += " Ebean has automatically modified the orderBy clause to do this.";
                logger.log(Level.WARNING, msg);
            }
            orderBy = orderBy.substring(0, manyPos) + orderById + ", " + orderBy.substring(manyPos);
        }
        return orderBy;
    }

    /**
	 * Return the bind values for the where expression.
	 */
    public ArrayList<Object> getWhereExprBindValues() {
        return whereExprBindValues;
    }

    /**
	 * Return the db column version of the combined where clause.
	 */
    public String getDbHaving() {
        return dbHaving;
    }

    /**
	 * Return the db column version of the combined where clause.
	 */
    public String getDbWhere() {
        return dbWhere;
    }

    /**
	 * Return the db column version of the order by clause.
	 */
    public String getDbOrderBy() {
        return dbOrderBy;
    }

    /**
	 * Return the includes required for the where and order by clause.
	 */
    public Set<String> getPredicateIncludes() {
        return predicateIncludes;
    }

    /**
	 * The where sql with named bind parameters converted to ?.
	 */
    public String getWhereRawSql() {
        return whereRawSql;
    }

    /**
	 * The where sql from the expression objects.
	 */
    public String getWhereExpressionSql() {
        return whereExprSql;
    }

    /**
	 * The having sql with named bind parameters converted to ?.
	 */
    public String getHavingRawSql() {
        return havingRawSql;
    }

    /**
	 * The having sql from the expression objects.
	 */
    public String getHavingExpressionSql() {
        return havingExprSql;
    }

    public String getLogWhereSql() {
        if (rawSql) {
            return "";
        } else {
            String logPred = getDbWhere();
            if (logPred == null) {
                return "";
            } else if (logPred.length() > 400) {
                logPred = logPred.substring(0, 400) + " ...";
            }
            return logPred;
        }
    }
}
