package com.avaje.ebean.server.query;

import java.sql.SQLException;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;
import com.avaje.ebean.BackgroundExecutor;
import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebean.bean.BeanCollectionTouched;
import com.avaje.ebean.config.dbplatform.DatabasePlatform;
import com.avaje.ebean.internal.BeanIdList;
import com.avaje.ebean.server.core.Message;
import com.avaje.ebean.server.core.OrmQueryRequest;
import com.avaje.ebean.server.jmx.MAdminLogging;
import com.avaje.ebean.server.persist.Binder;

/**
 * Handles the Object Relational fetching.
 */
public class CQueryEngine {

    private static final Logger logger = Logger.getLogger(CQueryEngine.class.getName());

    private final CQueryBuilder queryBuilder;

    private final MAdminLogging logControl;

    private final BackgroundExecutor backgroundExecutor;

    public CQueryEngine(DatabasePlatform dbPlatform, MAdminLogging logControl, Binder binder, BackgroundExecutor backgroundExecutor) {
        this.logControl = logControl;
        this.backgroundExecutor = backgroundExecutor;
        this.queryBuilder = new CQueryBuilder(backgroundExecutor, dbPlatform, binder);
    }

    public <T> CQuery<T> buildQuery(OrmQueryRequest<T> request) {
        return queryBuilder.buildQuery(request);
    }

    /**
	 * Build and execute the row count query.
	 */
    public <T> BeanIdList findIds(OrmQueryRequest<T> request) {
        CQueryFetchIds rcQuery = queryBuilder.buildFetchIdsQuery(request);
        try {
            String sql = rcQuery.getGeneratedSql();
            sql = sql.replace(Constants.NEW_LINE, ' ');
            if (logControl.isDebugGeneratedSql()) {
                System.out.println(sql);
            }
            if (logControl.isLogQuery(MAdminLogging.SQL)) {
                request.getTransaction().log(sql);
            }
            BeanIdList list = rcQuery.findIds();
            if (logControl.isLogQuery(MAdminLogging.SUMMARY)) {
                request.getTransaction().log(rcQuery.getSummary());
            }
            if (!list.isFetchingInBackground() && request.getQuery().isFutureFetch()) {
                logger.fine("Future findIds completed!");
                request.getTransaction().end();
            }
            return list;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    /**
	 * Build and execute the row count query.
	 */
    public <T> int findRowCount(OrmQueryRequest<T> request) {
        CQueryRowCount rcQuery = queryBuilder.buildRowCountQuery(request);
        try {
            String sql = rcQuery.getGeneratedSql();
            sql = sql.replace(Constants.NEW_LINE, ' ');
            if (logControl.isDebugGeneratedSql()) {
                System.out.println(sql);
            }
            if (logControl.isLogQuery(MAdminLogging.SQL)) {
                request.getTransaction().log(sql);
            }
            int rowCount = rcQuery.findRowCount();
            if (logControl.isLogQuery(MAdminLogging.SUMMARY)) {
                request.getTransaction().log(rcQuery.getSummary());
            }
            if (request.getQuery().isFutureFetch()) {
                logger.fine("Future findRowCount completed!");
                request.getTransaction().end();
            }
            return rowCount;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    /**
	 * Find a list/map/set of beans.
	 */
    public <T> BeanCollection<T> findMany(OrmQueryRequest<T> request) {
        boolean useBackgroundToContinueFetch = false;
        CQuery<T> cquery = queryBuilder.buildQuery(request);
        request.setCancelableQuery(cquery);
        try {
            if (logControl.isDebugGeneratedSql()) {
                logSqlToConsole(cquery);
            }
            if (logControl.isLogQuery(MAdminLogging.SQL)) {
                logSql(cquery);
            }
            if (!cquery.prepareBindExecuteQuery()) {
                logger.finest("Future fetch already cancelled");
                return null;
            }
            BeanCollection<T> beanCollection = cquery.readCollection();
            if (request.getQuery().isSharedInstance()) {
                beanCollection.setSharedInstance();
            }
            BeanCollectionTouched collectionTouched = request.getQuery().getBeanCollectionTouched();
            if (collectionTouched != null) {
                beanCollection.setBeanCollectionTouched(collectionTouched);
            }
            if (cquery.useBackgroundToContinueFetch()) {
                request.setBackgroundFetching();
                useBackgroundToContinueFetch = true;
                BackgroundFetch fetch = new BackgroundFetch(cquery);
                FutureTask<Integer> future = new FutureTask<Integer>(fetch);
                beanCollection.setBackgroundFetch(future);
                backgroundExecutor.execute(future);
            }
            if (logControl.isLogQuery(MAdminLogging.SUMMARY)) {
                logFindManySummary(cquery);
            }
            return beanCollection;
        } catch (SQLException e) {
            String sql = cquery.getGeneratedSql();
            String m = Message.msg("fetch.error", e.getMessage(), sql);
            throw new PersistenceException(m, e);
        } finally {
            if (useBackgroundToContinueFetch) {
            } else {
                if (cquery != null) {
                    cquery.close();
                }
                if (request.getQuery().isFutureFetch()) {
                    logger.fine("Future fetch completed!");
                    request.getTransaction().end();
                }
            }
        }
    }

    /**
	 * Find and return a single bean using its unique id.
	 */
    public <T> T find(OrmQueryRequest<T> request) {
        T bean = null;
        CQuery<T> cquery = queryBuilder.buildQuery(request);
        try {
            if (logControl.isDebugGeneratedSql()) {
                logSqlToConsole(cquery);
            }
            if (logControl.isLogQuery(MAdminLogging.SQL)) {
                logSql(cquery);
            }
            cquery.prepareBindExecuteQuery();
            if (cquery.readBean()) {
                bean = cquery.getLoadedBean();
            }
            if (logControl.isLogQuery(MAdminLogging.SUMMARY)) {
                logFindSummary(cquery);
            }
            return bean;
        } catch (SQLException e) {
            String sql = cquery.getGeneratedSql();
            String msg = Message.msg("fetch.error", e.getMessage(), sql);
            throw new PersistenceException(msg, e);
        } finally {
            cquery.close();
        }
    }

    /**
	 * Log the generated SQL to the console.
	 */
    private void logSqlToConsole(CQuery<?> build) {
        String sql = build.getGeneratedSql();
        String summary = build.getSummary();
        StringBuilder sb = new StringBuilder(1000);
        sb.append("<sql summary='").append(summary).append("'>");
        sb.append(Constants.NEW_LINE);
        sb.append(sql);
        sb.append(Constants.NEW_LINE).append("</sql>");
        System.out.println(sb.toString());
    }

    /**
	 * Log the generated SQL to the transaction log.
	 */
    private void logSql(CQuery<?> query) {
        String sql = query.getGeneratedSql();
        sql = sql.replace(Constants.NEW_LINE, ' ');
        query.getTransaction().log(sql);
    }

    /**
	 * Log the FindById summary to the transaction log.
	 */
    private void logFindSummary(CQuery<?> q) {
        StringBuilder msg = new StringBuilder(200);
        msg.append("FindById");
        msg.append(" exeMicros[").append("" + q.getQueryExecutionTimeMicros()).append("]");
        msg.append(" rows[").append(q.getLoadedRowDetail());
        msg.append("]");
        String beanType = q.getBeanType();
        msg.append(" type[").append(beanType).append("]");
        msg.append(" bind[").append(q.getBindLog()).append("]");
        q.getTransaction().log(msg.toString());
    }

    /**
	 * Log the FindMany to the transaction log.
	 */
    private void logFindManySummary(CQuery<?> q) {
        StringBuilder msg = new StringBuilder(200);
        msg.append("FindMany");
        msg.append(" exeMicros[").append(q.getQueryExecutionTimeMicros()).append("]");
        msg.append(" rows[").append(q.getLoadedRowDetail());
        msg.append("] type[");
        String beanType = q.getBeanType();
        msg.append(beanType).append("]");
        msg.append(" name[").append(q.getName()).append("]");
        msg.append(" predicates[").append(q.getLogWhereSql()).append("]");
        msg.append(" bind[").append(q.getBindLog()).append("]");
        q.getTransaction().log(msg.toString());
    }
}
