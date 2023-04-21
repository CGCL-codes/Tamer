package de.ios.kontor.sv.address.impl;

import java.rmi.*;
import de.ios.framework.basic.*;
import de.ios.framework.db2.*;
import de.ios.kontor.utils.*;
import de.ios.kontor.sv.address.co.*;

/**
 * CreditCardControllerImpl deals with a set of CreditCards
 * within the kontor framework.
 */
public class CreditCardControllerImpl extends java.rmi.server.UnicastRemoteObject implements CreditCardController {

    /**
   * Constructor
   *
   * @param _db - Connection to the database
   * @param _f - a factory to create CreditCard-objects
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public CreditCardControllerImpl(DBObjectServer _db, CreditCardFactoryImpl _f) throws java.rmi.RemoteException {
        db = _db;
        factory = _f;
    }

    /**
   * Get the CreditCard with the matching Object-ID
   * @exception de.ios.kontor.utils.KontorException if the loading of CreditCard failed.
   */
    public CreditCard getCreditCardByOId(long oid) throws KontorException {
        try {
            return (CreditCard) factory.create(db.loadObject(-1, classDBO, oid));
        } catch (de.ios.framework.basic.ServerException e) {
            throw new KontorException("CreditCard: Loading Object by Object-Id failed.", e);
        } catch (FactoryException e) {
            throw new KontorException("Creation of CreditCard failed.", e);
        }
    }

    /**
   * Find CreditCards by some of it's Attributes
   * @exception de.ios.kontor.utils.KontorException if the loading of CreditCards failed.
   */
    public Iterator getCreditCards(CreditCard comp) throws KontorException, RemoteException {
        try {
            CreditCardDBO c = (CreditCardDBO) comp.getDBObject();
            DBWhereClause wc = new DBWhereClause(c);
            DBOrderClause oc = new DBOrderClause(c, null);
            DBResultSet rs = db.fetchObject(-1, c, factory, wc, oc, 20);
            return new DBResultSetIterator(rs);
        } catch (de.ios.framework.basic.ServerException e) {
            throw new KontorException("CreditCard: Loading Objects by Attributes failed.", e);
        } catch (RemoteException e) {
            throw new KontorException("CreditCard: Loading Objects by Attributes failed.", e);
        }
    }

    /**
   * Create a new CreditCard
   * @exception de.ios.kontor.utils.KontorException if the creation of CreditCard failed.
   */
    public CreditCard createCreditCard() throws KontorException {
        try {
            return (CreditCard) factory.create();
        } catch (FactoryException e) {
            throw new KontorException("Creation of CreditCard failed.", e);
        }
    }

    /** Database Connection. */
    protected DBObjectServer db = null;

    /** Class of the DBOject. */
    protected Class classDBO = new CreditCardDBO().getClass();

    /** Factory to create new CreditCards. */
    protected CreditCardFactoryImpl factory = null;
}

;
