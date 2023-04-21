package com.avaje.ebean.server.query;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;
import com.avaje.ebean.config.dbplatform.DatabasePlatform;
import com.avaje.ebean.config.dbplatform.SqlLimitResponse;
import com.avaje.ebean.config.dbplatform.SqlLimiter;
import com.avaje.ebean.internal.SpiQuery;
import com.avaje.ebean.server.core.OrmQueryRequest;
import com.avaje.ebean.server.deploy.BeanDescriptor;
import com.avaje.ebean.server.deploy.DeployNamedQuery;
import com.avaje.ebean.server.deploy.DeployParser;
import com.avaje.ebean.server.deploy.RawSqlSelect;
import com.avaje.ebean.server.persist.Binder;
import com.avaje.ebean.server.querydefn.OrmQueryLimitRequest;

/**
 * Factory for SqlSelectClause based on raw sql.
 * <p>
 * Its job is to execute the sql, read the meta data to determine the columns to
 * bean property mapping.
 * </p>
 */
public class RawSqlSelectClauseBuilder {

    private static final Logger logger = Logger.getLogger(RawSqlSelectClauseBuilder.class.getName());

    private final Binder binder;

    private final SqlLimiter dbQueryLimiter;

    public RawSqlSelectClauseBuilder(DatabasePlatform dbPlatform, Binder binder) {
        this.binder = binder;
        this.dbQueryLimiter = dbPlatform.getSqlLimiter();
    }

    /**
	 * Build based on the includes and using the BeanJoinTree.
	 */
    public <T> CQuery<T> build(OrmQueryRequest<T> request) throws PersistenceException {
        SpiQuery<T> query = request.getQuery();
        BeanDescriptor<T> desc = request.getBeanDescriptor();
        DeployNamedQuery namedQuery = desc.getNamedQuery(query.getName());
        RawSqlSelect sqlSelect = namedQuery.getSqlSelect();
        DeployParser parser = sqlSelect.createDeployPropertyParser();
        CQueryPredicates predicates = new CQueryPredicates(binder, request, parser);
        predicates.prepare(true, false);
        SqlTreeAlias alias = new SqlTreeAlias(sqlSelect.getTableAlias());
        predicates.parseTableAlias(alias);
        String sql = null;
        try {
            boolean includeRowNumColumn = false;
            String orderBy = sqlSelect.getOrderBy(predicates);
            sql = sqlSelect.buildSql(orderBy, predicates, request);
            if (query.hasMaxRowsOrFirstRow() && dbQueryLimiter != null) {
                SqlLimitResponse limitSql = dbQueryLimiter.limit(new OrmQueryLimitRequest(sql, orderBy, query));
                includeRowNumColumn = limitSql.isIncludesRowNumberColumn();
                sql = limitSql.getSql();
            } else {
                sql = "select " + sql;
            }
            SqlTree sqlTree = sqlSelect.getSqlTree();
            CQueryPlan queryPlan = new CQueryPlan(sql, sqlTree, true, includeRowNumColumn, "");
            CQuery<T> compiledQuery = new CQuery<T>(request, predicates, queryPlan);
            return compiledQuery;
        } catch (Exception e) {
            String msg = "Error with " + desc.getFullName() + " query:\r" + sql;
            logger.log(Level.SEVERE, msg);
            throw new PersistenceException(e);
        }
    }
}
