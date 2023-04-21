package org.neodatis.odb.core.server.layers.layer1;

import java.util.Map;
import org.neodatis.odb.ODBRuntimeException;
import org.neodatis.odb.OID;
import org.neodatis.odb.OdbConfiguration;
import org.neodatis.odb.core.OldDatabaseEngine;
import org.neodatis.odb.core.NeoDatisError;
import org.neodatis.odb.core.layers.layer1.IntrospectionCallback;
import org.neodatis.odb.core.layers.layer1.ObjectIntrospectorImpl;
import org.neodatis.odb.core.layers.layer2.meta.AbstractObjectInfo;
import org.neodatis.odb.core.layers.layer2.meta.AttributeIdentification;
import org.neodatis.odb.core.layers.layer2.meta.ClassInfo;
import org.neodatis.odb.core.layers.layer2.meta.NonNativeObjectInfo;
import org.neodatis.odb.core.oid.OIDFactory;
import org.neodatis.odb.core.server.layers.layer2.meta.ClientNonNativeObjectInfo;
import org.neodatis.odb.core.transaction.CacheFactory;
import org.neodatis.odb.core.transaction.ICache;
import org.neodatis.odb.core.transaction.ICrossSessionCache;
import org.neodatis.odb.core.transaction.ISession;
import org.neodatis.tool.wrappers.list.IOdbList;
import org.neodatis.tool.wrappers.list.OdbArrayList;
import org.neodatis.tool.wrappers.map.OdbHashMap;

/**
 * Not thread safe
 * 
 * @author osmadja
 * 
 */
public class ClientObjectIntrospector extends ObjectIntrospectorImpl implements IClientObjectIntrospector {

    /** client oids are sequential ids created by the client side engine. When an object is sent to server, server ids are sent back from server
	 * and client engine replace all local(client) oids by the server oids. */
    protected IOdbList<OID> clientOids;

    /** A map of abstract object info, keys are local ids */
    protected Map<OID, ClientNonNativeObjectInfo> aois;

    protected Map<OID, Object> objects;

    protected ISession session;

    /** This represents the connection to the server*/
    protected String connectionId;

    public ClientObjectIntrospector(OldDatabaseEngine storageEngine, String connectionId) {
        super(storageEngine);
        clientOids = new OdbArrayList<OID>();
        aois = new OdbHashMap<OID, ClientNonNativeObjectInfo>();
        objects = new OdbHashMap<OID, Object>();
        session = storageEngine.getSession(true);
        this.connectionId = connectionId;
    }

    public ISession getSession() {
        return session;
    }

    public NonNativeObjectInfo buildNnoi(Object o, ClassInfo info, AbstractObjectInfo[] values, AttributeIdentification[] attributesIdentification, int[] attributeIds, Map<Object, NonNativeObjectInfo> alreadyReadObjects) {
        ClientNonNativeObjectInfo cnnoi = new ClientNonNativeObjectInfo(null, info, values, attributesIdentification, attributeIds);
        throw new RuntimeException("not implemented");
    }

    public IOdbList<OID> getClientOids() {
        return clientOids;
    }

    public AbstractObjectInfo getMetaRepresentation(Object object, ClassInfo ci, Map<Object, NonNativeObjectInfo> alreadyReadObjects, IntrospectionCallback callback) {
        clientOids.clear();
        aois.clear();
        objects.clear();
        return super.getObjectInfo(object, ci, alreadyReadObjects, callback);
    }

    /**
	 * This method is used to make sure that client oids and server oids are equal.
	 * 
	 * <pre>
	 * When storing an object, the client side does not know the oid that each object will receive. So the client create
	 * temporary (sequential) oids. These oids are sent to the server in the object meta-representations. On the server side,
	 * real OIDs are created and associated to the objects and to the client side ids. After calling the store on the server side
	 * The client use the the synchronizeIds method to replace client ids by the correct server side ids. 
	 * 
	 * </pre>
	 */
    public void synchronizeIds(OID[] clientIds, OID[] serverIds) {
        if (clientIds.length != clientOids.size()) {
            throw new ODBRuntimeException(NeoDatisError.CLIENT_SERVER_SYNCHRONIZE_IDS.addParameter(clientOids.size()).addParameter(clientIds.length));
        }
        ClientNonNativeObjectInfo cnnoi = null;
        ICache cache = getSession().getCache();
        Object object = null;
        OID id = null;
        ICrossSessionCache crossSessionCache = CacheFactory.getCrossSessionCache(storageEngine.getBaseIdentification().getIdentification());
        for (int i = 0; i < clientIds.length; i++) {
            id = clientIds[i];
            cnnoi = aois.get(id);
            object = objects.get(id);
            if (serverIds[i] != null) {
                cnnoi.setOid(serverIds[i]);
                cache.addObject(serverIds[i], object, cnnoi.getHeader());
            }
            if (OdbConfiguration.reconnectObjectsToSession() && serverIds[i] != null) {
                crossSessionCache.addObject(object, serverIds[i]);
            }
        }
    }
}
