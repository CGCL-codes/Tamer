package de.ios.kontor.sv.address.impl;

import java.rmi.*;
import de.ios.framework.db2.*;
import de.ios.framework.basic.*;
import de.ios.kontor.utils.*;
import de.ios.kontor.sv.address.co.*;

/**
 * Implements the CreditCard-Interface, encapsulating a CreditCardDBO-Object.
 */
public class CreditCardImpl extends java.rmi.server.UnicastRemoteObject implements CreditCard {

    /**
   * Constructor specifying a DBObjectServer.
   * @param _db a DBObjectServer.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public CreditCardImpl(DBObjectServer _db) throws RemoteException {
        dbo = new CreditCardDBO();
        db = _db;
    }

    /**
   * Constructor for a certain DBObject.
   * @param _dbo the DBObject for that Class.
   * @param _db a DBObjectServer.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public CreditCardImpl(CreditCardDBO _dbo, DBObjectServer _db) throws RemoteException {
        dbo = _dbo;
        db = _db;
    }

    /**
   * Get the Implementation-DBObject.
   * @return the DBObject as Object.
   */
    public Object getDBObject() {
        return dbo;
    }

    /**
   * Store this object.
   * @exception de.ios.kontor.utils.KontorException if the Operation failed due to a Server-Error.
   * @exception de.ios.kontor.utils.KontorUpdateException if the Data to be changed wasn't up-to-date anymore.
   */
    public void store() throws KontorException, KontorUpdateConflict {
        try {
            db.storeObject(dbo);
        } catch (DBUpdateConflict uc) {
            try {
                throw new KontorUpdateConflict("Update-Conflict on store of CreditCardDBO.", new CreditCardImpl((CreditCardDBO) uc.getNewValue(), db));
            } catch (RemoteException r) {
                throw new KontorException("Creation of a new CreditCard failed on handling an Update-Conflict.", r);
            }
        } catch (de.ios.framework.basic.ServerException t) {
            throw new KontorException("Store of CreditCardDBO failed.", t);
        }
    }

    /** The object. */
    protected CreditCardDBO dbo = null;

    /** The Server to handle DBObjects. */
    protected DBObjectServer db = null;
}
