package de.ios.kontor.sv.address.impl;

import java.rmi.*;
import de.ios.framework.basic.*;
import de.ios.framework.db2.*;
import de.ios.kontor.utils.*;
import de.ios.kontor.sv.address.co.*;

/**
 * SupplierControllerImpl deals with a set of Suppliers
 * within the kontor framework.
 */
public class SupplierControllerImpl extends java.rmi.server.UnicastRemoteObject implements SupplierController {

    /**
   * Constructor
   *
   * @param _db - Connection to the database
   * @param _f - a factory to create Supplier-objects
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   */
    public SupplierControllerImpl(DBObjectServer _db, SupplierFactoryImpl _f) throws java.rmi.RemoteException {
        db = _db;
        factory = _f;
    }

    /**
   * Get the Supplier with the matching Object-ID
   * @exception de.ios.kontor.utils.KontorException if the loading of Supplier failed.
   */
    public Supplier getSupplierByOId(long oid) throws KontorException {
        try {
            return (Supplier) factory.create(db.loadObject(-1, classDBO, oid));
        } catch (de.ios.framework.basic.ServerException e) {
            throw new KontorException("Supplier: Loading Object by Object-Id failed.", e);
        } catch (FactoryException e) {
            throw new KontorException("Creation of Supplier failed.", e);
        }
    }

    /**
   * Find Suppliers by some of it's Attributes
   * @exception de.ios.kontor.utils.KontorException if the loading of Suppliers failed.
   */
    public Iterator getSuppliers(Supplier comp) throws KontorException, RemoteException {
        try {
            SupplierDBO c = (SupplierDBO) comp.getDBObject();
            DBWhereClause wc = new DBWhereClause(c);
            DBOrderClause oc = new DBOrderClause(c, null);
            DBResultSet rs = db.fetchObject(-1, c, factory, wc, oc, 20);
            return new DBResultSetIterator(rs);
        } catch (de.ios.framework.basic.ServerException e) {
            throw new KontorException("Supplier: Loading Objects by Attributes failed.", e);
        } catch (RemoteException e) {
            throw new KontorException("Supplier: Loading Objects by Attributes failed.", e);
        }
    }

    /**
   * Create a new Supplier
   * @exception de.ios.kontor.utils.KontorException if the creation of Supplier failed.
   */
    public Supplier createSupplier() throws KontorException {
        try {
            return (Supplier) factory.create();
        } catch (FactoryException e) {
            throw new KontorException("Creation of Supplier failed.", e);
        }
    }

    /** Database Connection. */
    protected DBObjectServer db = null;

    /** Class of the DBOject. */
    protected Class classDBO = new SupplierDBO().getClass();

    /** Factory to create new Suppliers. */
    protected SupplierFactoryImpl factory = null;
}

;
