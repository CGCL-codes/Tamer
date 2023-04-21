package net.sf.hibernate4gwt.core.store.stateful;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import net.sf.hibernate4gwt.core.IPersistenceUtil;
import net.sf.hibernate4gwt.core.store.IProxyStore;
import net.sf.hibernate4gwt.exception.NotHibernateObjectException;
import net.sf.hibernate4gwt.exception.ProxyStoreException;
import net.sf.hibernate4gwt.exception.TransientHibernateObjectException;

/**
 * Proxy store for stateful web application
 * @author bruno.marchesson
 *
 */
public class HttpSessionProxyStore implements IProxyStore {

    /**
	 * The storage thread local
	 */
    private static ThreadLocal<HttpSession> _httpSession = new ThreadLocal<HttpSession>();

    /**
	 * The associated persistence util
	 */
    protected IPersistenceUtil _persistenceUtil;

    /**
	 * Store the current HTTP session in the thread local
	 */
    public static void setHttpSession(HttpSession session) {
        _httpSession.set(session);
    }

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
        String id = UniqueNameGenerator.generateUniqueName(pojoId, _persistenceUtil.getUnenhancedClass(pojo.getClass()));
        HttpSession httpSession = getSession();
        Map<String, Map<String, Serializable>> pojoMap = (Map<String, Map<String, Serializable>>) httpSession.getAttribute(id);
        if (pojoMap == null) {
            pojoMap = new HashMap<String, Map<String, Serializable>>();
        }
        pojoMap.put(property, proxyInformations);
        httpSession.setAttribute(id, pojoMap);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Serializable> getProxyInformations(Object pojo, String property) {
        String id = null;
        try {
            id = UniqueNameGenerator.generateUniqueName(_persistenceUtil, pojo);
        } catch (TransientHibernateObjectException e) {
            return null;
        } catch (NotHibernateObjectException e) {
            return null;
        }
        HttpSession httpSession = getSession();
        Map<String, Map<String, Serializable>> pojoMap = (Map<String, Map<String, Serializable>>) httpSession.getAttribute(id);
        if (pojoMap == null) {
            return null;
        } else {
            return pojoMap.get(property);
        }
    }

    @SuppressWarnings("unchecked")
    public void removeProxyInformations(Object pojo, String property) {
        String id = UniqueNameGenerator.generateUniqueName(_persistenceUtil, pojo);
        HttpSession httpSession = getSession();
        Map<String, Map<String, Serializable>> pojoMap = (Map<String, Map<String, Serializable>>) httpSession.getAttribute(id);
        if (pojoMap != null) {
            pojoMap.remove(property);
            if (pojoMap.isEmpty()) {
                httpSession.removeAttribute(id);
            } else {
                httpSession.setAttribute(id, pojoMap);
            }
        }
    }

    /**
	 * @return the HTTP session stored in thread local
	 */
    private HttpSession getSession() {
        HttpSession session = (HttpSession) _httpSession.get();
        if (session == null) {
            throw new ProxyStoreException("No HTTP session stored", null);
        }
        return session;
    }
}
