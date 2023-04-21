package com.rcreations.persist.dao.impl;

import com.rcreations.persist.BasePersistDao;
import com.rcreations.persist.dao.CrudDao;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author robert
 */
public class CrudDaoImpl extends BasePersistDao implements CrudDao {

    /**
       * {@inheritDoc}
       */
    @Override
    public int insert(Object object) {
        return getPersist().insert(object);
    }

    /**
       * {@inheritDoc}
       */
    @Override
    public int[] insertBatch(Object... objects) {
        return getPersist().insertBatch(objects);
    }

    /**
       * {@inheritDoc}
       */
    @Override
    public <T> T read(Class<T> objectClass, String sql) {
        return getPersist().read(objectClass, sql);
    }

    /**
       * {@inheritDoc}
       */
    @Override
    public <T> T read(Class<T> objectClass, String sql, Object... parameters) {
        return getPersist().read(objectClass, sql, parameters);
    }

    /**
       * {@inheritDoc}
       */
    @Override
    public <T> T readByPrimaryKey(Class<T> objectClass, Object... primaryKeyValues) {
        return getPersist().readByPrimaryKey(objectClass, primaryKeyValues);
    }

    /**
       * {@inheritDoc}
       */
    @Override
    public <T> List<T> readList(Class<T> objectClass, String sql) {
        return getPersist().readList(objectClass, sql);
    }

    /**
       * {@inheritDoc}
       */
    @Override
    public <T> List<T> readList(Class<T> objectClass, String sql, Object... parameters) {
        return getPersist().readList(objectClass, sql, parameters);
    }

    /**
       * {@inheritDoc}
       */
    @Override
    public <T> List<T> readList(Class<T> objectClass) {
        return getPersist().readList(objectClass);
    }

    /**
       * {@inheritDoc}
       */
    @Override
    public <T> Iterator<T> readIterator(Class<T> objectClass, String sql) {
        return getPersist().readIterator(objectClass, sql);
    }

    /**
       * {@inheritDoc}
       */
    @Override
    public <T> Iterator<T> readIterator(Class<T> objectClass, String sql, Object... parameters) {
        return getPersist().readIterator(objectClass, sql, parameters);
    }

    /**
       * {@inheritDoc}
       */
    @Override
    public <T> Iterator<T> readIterator(Class<T> objectClass) {
        return getPersist().readIterator(objectClass);
    }

    /**
       * {@inheritDoc}
       */
    @Override
    public int update(Object object) {
        return getPersist().update(object);
    }

    /**
       * {@inheritDoc}
       */
    @Override
    public int[] updateBatch(Object... objects) {
        return getPersist().updateBatch(objects);
    }

    /**
       * {@inheritDoc}
       */
    @Override
    public int delete(Object object) {
        return getPersist().delete(object);
    }

    /**
       * {@inheritDoc}
       */
    @Override
    public int[] deleteBatch(Object... objects) {
        return getPersist().deleteBatch(objects);
    }
}
