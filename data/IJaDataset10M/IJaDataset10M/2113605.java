package de.ios.kontor.sv.stock.impl;

import java.rmi.*;
import de.ios.framework.basic.*;
import de.ios.framework.db2.*;
import de.ios.kontor.utils.*;
import de.ios.kontor.sv.basic.impl.*;
import de.ios.kontor.sv.stock.co.*;

/**
 * StockFactory can create Stock objects.
 *
 * @author js (Joachim Schaaf).
 * @version $Id: StockFactoryImpl.java,v 1.1.1.1 2004/03/24 23:03:44 nanneb Exp $.
 */
public class StockFactoryImpl extends BasicFactoryImpl implements StockFactory {

    /**
   * constructor.
   *
   * @param _db The DBObject-Server.
   * @param _sc The Session-Carrier.
   *
   * @exception java.rmi.RemoteException if the connection to the server failed.
   */
    public StockFactoryImpl(DBObjectServer _db, SessionCarrier _sc) throws RemoteException {
        super(_db, _sc);
    }

    /**
   * Create a new Stock with given DBObject.
   *
   * @param dbo DBObject to be encapsulated within the Stock to create.
   *
   * @return a new Stock.
   *
   * @exception de.ios.framework.basic.FactoryException.
   */
    public Object create(DBObject dbo) throws FactoryException {
        try {
            return new StockImpl(db, sc, (StockDBO) dbo);
        } catch (Throwable t) {
            throw new FactoryException("Create of StockImpl failed.", t);
        }
    }

    /**
   * Create a new Stock.
   *
   * @param _name The name of the stock.
   * @param _description A description of the stock.
   * @exception de.ios.framework.basic.FactoryException If the creation of a Colors failed.
   */
    public Stock create(String _name, String _description) throws FactoryException {
        return (Stock) create(new StockDBO(_name, _description));
    }

    /**
   * Create an empty DBO.
   *
   * @return a newly created empty DBO.
   */
    public BasicDBO createDBO() {
        return new StockDBO();
    }
}
