package de.mogwai.common.dao.inmem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import de.mogwai.common.business.entity.Entity;
import de.mogwai.common.dao.Dao;

/**
 * Generic Inmem DAO.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-06-17 14:26:37 $
 * @param <T> Der Typ der Entity
 */
public class GenericInmemDao<T extends Entity> implements Dao {

    private long counter = 1;

    private Map<Long, T> cache = new HashMap<Long, T>();

    public void delete(Object aValue) {
        Entity theEntity = (Entity) aValue;
        if (theEntity.getId() != null) {
            cache.remove(theEntity.getId());
        }
    }

    public Object getById(Class aClass, Long aId) {
        return cache.get(aId);
    }

    public List<T> findAll() {
        Vector<T> theResult = new Vector<T>();
        theResult.addAll(cache.values());
        return theResult;
    }

    public void save(Object aValue) {
        Entity theEntity = (Entity) aValue;
        if (theEntity.getId() == null) {
            theEntity.setId(counter++);
            cache.put(theEntity.getId(), (T) theEntity);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Entity refresh(final Entity entity) {
        return entity;
    }
}
