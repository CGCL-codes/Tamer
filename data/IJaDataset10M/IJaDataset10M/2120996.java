package de.ios.kontor.sv.address.impl;

import java.rmi.*;
import de.ios.framework.basic.*;
import de.ios.framework.db2.*;
import de.ios.kontor.utils.*;
import de.ios.kontor.sv.address.co.*;
import de.ios.kontor.sv.basic.impl.*;

/**
 * CustomerFactory can create Customer-Objects. If you want to use
 * your own Customer-Type you have to implement your own Factory and
 * hand it over to the CustomerController when creating it.
 */
public class CustomerFactoryImpl extends BasicFactoryImpl implements CustomerFactory, DBFactory {

    /**
   * Constructor.
   * 
   * @param db DBObjectServer for access to database.
   * @param _sc The Session Carrier
   * @exception java.rmi.RemoteException If the connection to the Server failed.
   */
    public CustomerFactoryImpl(DBObjectServer _db, SessionCarrier _sc) throws java.rmi.RemoteException {
        super(_db, _sc);
    }

    /**
   * Create a Customer from a CustomerDBO.
   *
   * @param dbo DBObject to be encapsulated within the Customer to create.
   * @exception de.ios.kontor.utils.FactoryException If the connection to the Server failed.
   */
    public Object create(DBObject _dbo) throws FactoryException {
        try {
            return new CustomerImpl(db, sc, (CustomerDBO) _dbo);
        } catch (Throwable t) {
            throw new FactoryException("Create of Customer with DBO failed.", t);
        }
    }

    /**
   * Create a Business-Object's DBO (not supported by DummyFactory).
   */
    public BasicDBO createDBO() {
        return new CustomerDBO();
    }
}

;
