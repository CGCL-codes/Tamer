package com.angel.architecture.hibernate3.interceptors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.CallbackException;
import org.hibernate.EntityMode;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import java.io.Serializable;
import java.util.Iterator;

/**
 *  Este interceptor se usa mas que nada para obtener los cambios del cache de nivel 1 del usuario(Session)
 * y sincronizar todos sus cambios segun la logica que el persistentObject correspondiente tenga implementada
 *
 * @author ALeinvand
 * @version $Revision: 1.13 $
 */
public class PersistentHibernateInterceptor implements Interceptor {

    private static final Log log = LogFactory.getLog(PersistentHibernateInterceptor.class);

    /**
     * METODO SOBRESCRITO
     * @see org.hibernate.Interceptor#onLoad(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
     */
    public boolean onLoad(Object entity, Serializable arg1, Object[] arg2, String[] arg3, Type[] arg4) throws CallbackException {
        log.debug("onLoad method");
        return false;
    }

    /**
     * METODO SOBRESCRITO:
     *  Este metodo intercepta todos los flush de una session y sincroniza el objeto si este tiene implementado
     * el metodo sincronizar.
     * @see org.hibernate.Interceptor#onFlushDirty(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
     */
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) throws CallbackException {
        log.debug("onFlushDirty method");
        log.debug("entity: " + entity.getClass());
        return false;
    }

    /**
     * METODO SOBRESCRITO
     * @see org.hibernate.Interceptor#onSave(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
     */
    public boolean onSave(Object entity, Serializable arg1, Object[] arg2, String[] arg3, Type[] arg4) throws CallbackException {
        log.debug("onSave method");
        log.debug("entity: " + entity.getClass());
        return false;
    }

    /**
     * METODO SOBRESCRITO
     * @see org.hibernate.Interceptor#onDelete(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
     */
    public void onDelete(Object entity, Serializable arg1, Object[] arg2, String[] arg3, Type[] arg4) throws CallbackException {
        log.debug("onDelete method");
    }

    /**
     * METODO SOBRESCRITO
     * @see org.hibernate.Interceptor#preFlush(java.util.Iterator)
     */
    @SuppressWarnings("unchecked")
    public void preFlush(Iterator arg0) throws CallbackException {
        log.debug("preFlush method");
    }

    /**
     * METODO SOBRESCRITO
     * @see org.hibernate.Interceptor#postFlush(java.util.Iterator)
     */
    @SuppressWarnings("unchecked")
    public void postFlush(Iterator arg0) throws CallbackException {
        log.debug("postFlush method");
    }

    /**
     * METODO SOBRESCRITO
     * @see org.hibernate.Interceptor#findDirty(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
     */
    public int[] findDirty(Object arg0, Serializable arg1, Object[] arg2, Object[] arg3, String[] arg4, Type[] arg5) {
        log.debug("isFindDirty method");
        log.debug("entity: " + arg0.getClass());
        return null;
    }

    public void onCollectionRecreate(Object arg0, Serializable arg1) throws CallbackException {
    }

    public void onCollectionRemove(Object arg0, Serializable arg1) throws CallbackException {
    }

    public void onCollectionUpdate(Object arg0, Serializable arg1) throws CallbackException {
    }

    public Boolean isTransient(Object arg0) {
        return null;
    }

    public Object instantiate(String arg0, EntityMode arg1, Serializable arg2) throws CallbackException {
        return null;
    }

    public String getEntityName(Object arg0) throws CallbackException {
        return null;
    }

    public Object getEntity(String arg0, Serializable arg1) throws CallbackException {
        return null;
    }

    public void afterTransactionBegin(Transaction arg0) {
    }

    public void beforeTransactionCompletion(Transaction arg0) {
    }

    public void afterTransactionCompletion(Transaction arg0) {
    }

    public String onPrepareStatement(String arg0) {
        return null;
    }
}
