package net.sf.hibernate4gwt.core.store.stateful;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import net.sf.hibernate4gwt.core.IPersistenceUtil;
import net.sf.hibernate4gwt.core.store.IProxyStore;
import net.sf.hibernate4gwt.exception.NotHibernateObjectException;
import net.sf.hibernate4gwt.exception.TransientHibernateObjectException;

/**
 * In Memory Proxy Information Store.
 * This class stores POJO in a simple hashmap
 * @author bruno.marchesson
 *
 */
public class InMemoryProxyStore implements IProxyStore {

    /**
	 * The store hashmap
	 */
    protected Map<String, Map<String, Serializable>> _map = new HashMap<String, Map<String, Serializable>>();

    /**
	 * The associated persistence util
	 */
    protected IPersistenceUtil _persistenceUtil;

    /**
	 * @return the persistence Util implementation
	 */
    public IPersistenceUtil getPersistenceUtil() {
        return _persistenceUtil;
    }

    /**
	 * @param persistenceUtil the persistence Util to set
	 */
    public void setPersistenceUtil(IPersistenceUtil persistenceUtil) {
        this._persistenceUtil = persistenceUtil;
    }

    @SuppressWarnings("unchecked")
    public void storeProxyInformations(Object pojo, Serializable pojoId, String property, Map<String, Serializable> proxyInformations) {
        _map.put(computeKey(pojo, pojoId, property), proxyInformations);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Serializable> getProxyInformations(Object pojo, String property) {
        try {
            return _map.get(computeKey(pojo, property));
        } catch (TransientHibernateObjectException ex) {
            return null;
        } catch (NotHibernateObjectException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public void removeProxyInformations(Object pojo, String property) {
        _map.remove(computeKey(pojo, property));
    }

    /**
	 * Compute the hashmap key
	 * @param pojo
	 * @param property
	 * @return
	 */
    protected String computeKey(Object pojo, Serializable id, String property) {
        Class<?> pojoClass = _persistenceUtil.getUnenhancedClass(pojo.getClass());
        return UniqueNameGenerator.generateUniqueName(id, pojoClass) + '.' + property;
    }

    /**
	 * Compute the hashmap key
	 * @param pojo
	 * @param property
	 * @return
	 */
    protected String computeKey(Object pojo, String property) {
        return UniqueNameGenerator.generateUniqueName(_persistenceUtil, pojo) + '.' + property;
    }
}
