package com.hyd.dao;

import com.hyd.dao.database.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 简单事务。不包含 onSuccess 和 onFailure 方法，当执行失败时直接抛出异常。
 */
public abstract class SimpleTransaction {

    static final Logger log = LoggerFactory.getLogger(SimpleTransaction.class);

    protected abstract void execute() throws Exception;

    /**
     * 执行事务。
     *
     * @throws DAOException 如果事务执行失败
     */
    public void run() throws DAOException {
        TransactionManager.start();
        try {
            execute();
            TransactionManager.commit();
        } catch (Exception e) {
            if (TransactionManager.getLevel() > 1) {
                TransactionManager.downLevel();
                if (e instanceof DAOException) {
                    throw (DAOException) e;
                } else {
                    throw new DAOException(e);
                }
            }
            TransactionManager.rollback();
            if (e instanceof DAOException) {
                throw (DAOException) e;
            } else {
                throw new DAOException(e);
            }
        }
    }
}
