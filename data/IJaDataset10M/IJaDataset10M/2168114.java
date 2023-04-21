package org.neodatis.odb.core.layers.layer1;

import org.neodatis.odb.OID;
import org.neodatis.odb.ObjectOid;
import org.neodatis.odb.OdbConfiguration;
import org.neodatis.odb.core.layers.layer2.meta.ObjectInfoHeader;
import org.neodatis.odb.core.session.Session;
import org.neodatis.odb.core.session.cross.CacheFactory;
import org.neodatis.odb.core.session.cross.ICrossSessionCache;
import org.neodatis.odb.core.trigger.TriggerManager;

/**
 * @author olivier
 * 
 */
public class DefaultInstrospectionCallbackForStore implements IntrospectionCallback {

    protected TriggerManager triggerManager;

    protected ICrossSessionCache crossSessionCache;

    protected Session session;

    public DefaultInstrospectionCallbackForStore(Session session, TriggerManager triggerManager) {
        super();
        this.session = session;
        this.triggerManager = triggerManager;
        if (session != null) {
            this.crossSessionCache = CacheFactory.getCrossSessionCache(session.getBaseIdentification().toString());
        }
    }

    public boolean objectFound(Object object, ObjectOid oid) {
        boolean isUpdate = !oid.isNew();
        if (OdbConfiguration.reconnectObjectsToSession()) {
            isUpdate = checkIfObjectMustBeReconnected(object);
        }
        if (!isUpdate) {
            if (triggerManager != null) {
                triggerManager.manageInsertTriggerBefore(object.getClass().getName(), object);
            }
        } else {
            if (triggerManager != null) {
                triggerManager.manageUpdateTriggerBefore(object.getClass().getName(), null, object, null);
            }
        }
        return true;
    }

    /**
	 * Used to check if object must be reconnected to current session
	 * 
	 * <pre>
	 * An object must be reconnected to session if OdbConfiguration.reconnectObjectsToSession() is true
	 * and object is not in local cache and is in cross session cache. In this case
	 * we had it to local cache
	 * 
	 * </pre>
	 * 
	 * @param object
	 */
    private boolean checkIfObjectMustBeReconnected(Object o) {
        if (session == null) {
            return false;
        }
        if (session.getCache().existObject(o)) {
            return true;
        }
        OID oidCrossSession = crossSessionCache.getOid(o);
        if (oidCrossSession != null) {
            ObjectInfoHeader oih = session.getObjectInfoHeaderFromOid(oidCrossSession, true);
            session.addObjectToCache(oidCrossSession, o, oih);
            return true;
        } else {
        }
        return false;
    }
}
