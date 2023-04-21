package com.marcosperon.dao;

import com.marcosperon.bean.Bean;

/**
 * The Class AbstractDAO.
 * 
 * @author marcos
 */
public abstract class AbstractDAO<C extends Bean> implements DAO<C> {

    /** The bean class. */
    protected Class<?> beanClass;

    public AbstractDAO() {
    }

    /**
     * The Constructor.
     * 
     * @param beanClass
     *            the bean class
     */
    public AbstractDAO(Class<?> beanClass) {
        super();
        this.beanClass = beanClass;
    }

    public Object transaction(DAOCommand cmd) {
        if (cmd == null) {
            throw new DAOException("DAOCommand is a null object");
        }
        Transaction tx = null;
        Object obj = null;
        try {
            tx = newTransaction();
            tx.start();
            obj = cmd.execute();
        } catch (DAOException e) {
            throw new DAOException("Transaction is not supported", e);
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new DAOException("Rolling Back Transaction", e);
        } finally {
            if (tx != null) {
                tx.close();
            }
        }
        return obj;
    }

    public void setBeanClass(Class<?> beanClz) {
        beanClass = beanClz;
    }

    public Object execute(DAOCommand cmd) {
        if (cmd == null) {
            throw new DAOException("DAOCommand is a null object");
        }
        return cmd.execute();
    }
}
