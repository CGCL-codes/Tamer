package com.sun.corba.se.impl.protocol;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import com.sun.corba.se.pept.protocol.ClientRequestDispatcher;
import com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher;
import com.sun.corba.se.spi.protocol.LocalClientRequestDispatcherFactory;
import com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher;
import com.sun.corba.se.spi.protocol.RequestDispatcherRegistry;
import com.sun.corba.se.spi.oa.ObjectAdapterFactory;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.impl.orbutil.DenseIntMapImpl;

/**
 * This is a registry of all subcontract ID dependent objects.  This includes:
 * LocalClientRequestDispatcherFactory, ClientRequestDispatcher, ServerSubcontract, and 
 * ObjectAdapterFactory. 
 */
public class RequestDispatcherRegistryImpl implements RequestDispatcherRegistry {

    private ORB orb;

    protected int defaultId;

    private DenseIntMapImpl SDRegistry;

    private DenseIntMapImpl CSRegistry;

    private DenseIntMapImpl OAFRegistry;

    private DenseIntMapImpl LCSFRegistry;

    private Set objectAdapterFactories;

    private Set objectAdapterFactoriesView;

    private Map stringToServerSubcontract;

    public RequestDispatcherRegistryImpl(ORB orb, int defaultId) {
        this.orb = orb;
        this.defaultId = defaultId;
        SDRegistry = new DenseIntMapImpl();
        CSRegistry = new DenseIntMapImpl();
        OAFRegistry = new DenseIntMapImpl();
        LCSFRegistry = new DenseIntMapImpl();
        objectAdapterFactories = new HashSet();
        objectAdapterFactoriesView = Collections.unmodifiableSet(objectAdapterFactories);
        stringToServerSubcontract = new HashMap();
    }

    public synchronized void registerClientRequestDispatcher(ClientRequestDispatcher csc, int scid) {
        CSRegistry.set(scid, csc);
    }

    public synchronized void registerLocalClientRequestDispatcherFactory(LocalClientRequestDispatcherFactory csc, int scid) {
        LCSFRegistry.set(scid, csc);
    }

    public synchronized void registerServerRequestDispatcher(CorbaServerRequestDispatcher ssc, int scid) {
        SDRegistry.set(scid, ssc);
    }

    public synchronized void registerServerRequestDispatcher(CorbaServerRequestDispatcher scc, String name) {
        stringToServerSubcontract.put(name, scc);
    }

    public synchronized void registerObjectAdapterFactory(ObjectAdapterFactory oaf, int scid) {
        objectAdapterFactories.add(oaf);
        OAFRegistry.set(scid, oaf);
    }

    public CorbaServerRequestDispatcher getServerRequestDispatcher(int scid) {
        CorbaServerRequestDispatcher sdel = (CorbaServerRequestDispatcher) (SDRegistry.get(scid));
        if (sdel == null) sdel = (CorbaServerRequestDispatcher) (SDRegistry.get(defaultId));
        return sdel;
    }

    public CorbaServerRequestDispatcher getServerRequestDispatcher(String name) {
        CorbaServerRequestDispatcher sdel = (CorbaServerRequestDispatcher) stringToServerSubcontract.get(name);
        if (sdel == null) sdel = (CorbaServerRequestDispatcher) (SDRegistry.get(defaultId));
        return sdel;
    }

    public LocalClientRequestDispatcherFactory getLocalClientRequestDispatcherFactory(int scid) {
        LocalClientRequestDispatcherFactory factory = (LocalClientRequestDispatcherFactory) (LCSFRegistry.get(scid));
        if (factory == null) {
            factory = (LocalClientRequestDispatcherFactory) (LCSFRegistry.get(defaultId));
        }
        return factory;
    }

    public ClientRequestDispatcher getClientRequestDispatcher(int scid) {
        ClientRequestDispatcher subcontract = (ClientRequestDispatcher) (CSRegistry.get(scid));
        if (subcontract == null) {
            subcontract = (ClientRequestDispatcher) (CSRegistry.get(defaultId));
        }
        return subcontract;
    }

    public ObjectAdapterFactory getObjectAdapterFactory(int scid) {
        ObjectAdapterFactory oaf = (ObjectAdapterFactory) (OAFRegistry.get(scid));
        if (oaf == null) oaf = (ObjectAdapterFactory) (OAFRegistry.get(defaultId));
        return oaf;
    }

    public Set getObjectAdapterFactories() {
        return objectAdapterFactoriesView;
    }
}
