package com.lettuce.client.spring;

import java.sql.Connection;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;
import com.lettuce.transaction.QueryTransaction;
import com.lettuce.transaction.IQueryTransactionManager;

/**
 * LettuceTransactionManager for spring framework.
 * 
 * @author Zhigang Xie
 * @version 1.0, 2010-05-26
 * @since JDK 1.5
 */
public class LettuceTransactionManager extends DataSourceTransactionManager implements IQueryTransactionManager {

    private static final long serialVersionUID = 1L;

    private static ThreadLocal<QueryTransaction> qtx = new ThreadLocal<QueryTransaction>();

    private static ThreadLocal<Integer> qtxReference = new ThreadLocal<Integer>() {

        @Override
        protected Integer initialValue() {
            return new Integer(0);
        }
    };

    public LettuceTransactionManager() {
    }

    public LettuceTransactionManager(DataSource dataSource) {
        super(dataSource);
    }

    public QueryTransaction getQueryTransaction() {
        return qtx.get();
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        super.doBegin(transaction, definition);
        Connection conn = DataSourceUtils.getConnection(getDataSource());
        if (qtxReference.get() == 0) {
            qtx.set(new QueryTransaction(conn));
        }
        qtxReference.set(qtxReference.get() + 1);
    }

    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        if (qtxReference.get() > 1) {
            qtxReference.set(qtxReference.get() - 1);
        } else if (qtxReference.get() == 1) {
            qtx.get().commit();
            qtx.set(null);
            qtxReference.set(qtxReference.get() - 1);
        }
        super.doCommit(status);
    }

    @Override
    protected Object doGetTransaction() {
        return super.doGetTransaction();
    }

    @Override
    protected void doResume(Object transaction, Object suspendedResources) {
        super.doResume(transaction, suspendedResources);
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        if (qtxReference.get() > 1) {
            qtxReference.set(qtxReference.get() - 1);
        } else if (qtxReference.get() == 1) {
            qtx.get().rollback();
            qtx.set(null);
            qtxReference.set(qtxReference.get() - 1);
        }
        super.doRollback(status);
    }

    @Override
    protected void doSetRollbackOnly(DefaultTransactionStatus status) {
        super.doSetRollbackOnly(status);
    }

    @Override
    protected Object doSuspend(Object transaction) {
        return super.doSuspend(transaction);
    }

    @Override
    protected boolean isExistingTransaction(Object transaction) {
        return super.isExistingTransaction(transaction);
    }
}
