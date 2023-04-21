package de.ios.kontor.sv.order.impl;

import java.rmi.*;
import de.ios.framework.basic.*;
import de.ios.framework.db2.*;
import de.ios.kontor.utils.*;
import de.ios.kontor.sv.order.co.*;
import de.ios.kontor.sv.basic.impl.*;

/**
 * ServiceFactory can create Service-Objects. If you want to use
 * your own Service-Type you have to implement your own Factory and
 * hand it over to the ServiceController when creating it.
 */
public class ServiceFactoryImpl extends BasicFactoryImpl implements ServiceFactory {

    /**
   * Constructor.
   * @param _db the DBObjectServer for access to the Database.
   * @param _sc the SessionCarrier.
   * @exception java.rmi.RemoteException If the connection to the Server failed.
   */
    public ServiceFactoryImpl(DBObjectServer _db, SessionCarrier _sc) throws RemoteException {
        super(_db, _sc);
    }

    /**
   * Create a Business-Object from a DBO.
   * @param dbo DBObject to be encapsulated within the Business-Object to create.
   * @exception de.ios.kontor.utils.FactoryException If the connection to the Server failed.
   */
    public Object create(DBObject dbo) throws FactoryException {
        try {
            return new ServiceImpl(db, sc, (ServiceDBO) dbo);
        } catch (Throwable t) {
            throw new FactoryException("Create of ServiceImpl failed.", t);
        }
    }

    /**
   * Create a Business-Object's DBO (not supported by DummyFactory).
   */
    public BasicDBO createDBO() {
        return new ServiceDBO();
    }
}

;
