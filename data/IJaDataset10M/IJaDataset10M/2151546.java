package de.ios.kontor.sv.order.impl;

import java.rmi.*;
import de.ios.framework.basic.*;
import de.ios.framework.db2.*;
import de.ios.kontor.utils.*;
import de.ios.kontor.sv.order.co.*;
import de.ios.kontor.sv.basic.impl.*;

/**
 * OrderServiceLineEntryFactory can create OrderServiceLineEntry-Objects. If you want to use
 * your own OrderServiceLineEntry-Type you have to implement your own Factory and
 * hand it over to the OrderServiceLineEntryController when creating it.
 */
public class OrderServiceLineEntryFactoryImpl extends BasicFactoryImpl implements OrderServiceLineEntryFactory {

    /**
   * Constructor.
   * @param _db the DBObjectServer for access to the Database.
   * @param _sc the SessionCarrier.
   * @exception java.rmi.RemoteException If the connection to the Server failed.
   */
    public OrderServiceLineEntryFactoryImpl(DBObjectServer _db, SessionCarrier _sc) throws RemoteException {
        super(_db, _sc);
    }

    /**
   * Create a Business-Object from a DBO.
   * @param dbo DBObject to be encapsulated within the Business-Object to create.
   * @exception de.ios.kontor.utils.FactoryException If the connection to the Server failed.
   */
    public Object create(DBObject dbo) throws FactoryException {
        try {
            return new OrderServiceLineEntryImpl(db, sc, (OrderServiceLineEntryDBO) dbo, getKind());
        } catch (Throwable t) {
            throw new FactoryException("Create of OrderServiceLineEntryImpl failed.", t);
        }
    }

    /**
   * Create a Business-Object's DBO (not supported by DummyFactory).
   */
    public BasicDBO createDBO() {
        return new OrderServiceLineEntryDBO();
    }

    /**
   * Get kind of Order/Invoice-LineEntryController
   */
    public String getKind() {
        return "Service";
    }
}

;
