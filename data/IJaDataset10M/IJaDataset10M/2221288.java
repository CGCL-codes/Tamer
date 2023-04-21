package com.semperex.cohesion.generic;

import java.sql.Connection;
import com.semperex.cohesion.CohesionException;

public class SynchronizedGenericDAO<T> extends GenericDAO<T> {

    public SynchronizedGenericDAO(Connection sqlConnection, Class cls) {
        super(sqlConnection, cls);
    }

    @Override
    public synchronized T createNew() throws CohesionException {
        return super.createNew();
    }

    @Override
    public synchronized long createNewRankedList() throws CohesionException {
        return super.createNewRankedList();
    }

    @Override
    public synchronized void deleteRankedList(long array_id) throws CohesionException {
        super.deleteRankedList(array_id);
    }

    @Override
    public T[] findBy(String key, Object value, boolean like) throws CohesionException {
        return super.findBy(key, value, like);
    }

    @Override
    public T[] findBy(String key, Object value) throws CohesionException {
        return super.findBy(key, value);
    }

    @Override
    public T findById(Long id) throws CohesionException {
        return super.findById(id);
    }

    @Override
    public T[] findByName(String value) throws CohesionException {
        return super.findByName(value);
    }

    @Override
    public synchronized void join(Object a, Object b) throws CohesionException {
        super.join(a, b);
    }

    @Override
    public synchronized Object[] loadRankedList(long array_id) throws CohesionException {
        return super.loadRankedList(array_id);
    }

    @Override
    public synchronized boolean save(T object) throws CohesionException {
        return super.save(object);
    }

    @Override
    public synchronized long storeRankedList(long array_id, T[] array) throws CohesionException {
        return super.storeRankedList(array_id, array);
    }
}
