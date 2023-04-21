package de.ios.kontor.sv.stock.co;

import java.rmi.*;
import de.ios.framework.basic.*;
import de.ios.kontor.utils.*;
import de.ios.kontor.sv.basic.co.*;

/**
 * KindOfStockTransactionController deals with a set of KindOfStockTransactions.
 *
 * @author js (Joachim Schaaf).
 * @version $Id: KindOfStockTransactionController.java,v 1.1.1.1 2004/03/24 23:03:40 nanneb Exp $.
 */
public interface KindOfStockTransactionController extends BasicController {

    /**
   * Create a new KindOfStockTransaction.
   *
   * @return a new KindOfStockTransaction.
   *
   * @exception de.ios.framework.basic.KontorException.
   * @exception java.rmi.RemoteException if the connection to the server failed.
   */
    public KindOfStockTransaction createKindOfStockTransaction() throws java.rmi.RemoteException, KontorException;

    /**
   * Create a new KindOfStockTransaction with attributes
   *
   * @param _kind The KindOfStockTransaction
   *
   * @return a new KindOfStockTransaction with attributes.
   *
   * @exception de.ios.framework.basic.KontorException.
   * @exception java.rmi.RemoteException if the connection to the server failed.
   */
    public KindOfStockTransaction createKindOfStockTransaction(String _kind) throws java.rmi.RemoteException, KontorException;

    /**
   * Get the KindOfStockTransaction with the given object-id.
   *
   * @param oid the object-id
   *
   * @return the KindOfStockTransaction with the given object-id.
   *
   * @exception de.ios.framework.basic.KontorException.
   * @exception java.rmi.RemoteException if the connection to the server failed.
   */
    public KindOfStockTransaction getKindOfStockTransactionByOId(long oid) throws java.rmi.RemoteException, KontorException;

    /**
   * Get all KindOfStockTransactions that match the given KindOfStockTransaction object.
   *
   * @param o a filled KindOfStockTransaction object.
   *
   * @return an Iterator with all KindOfStockTransaction that match the given KindOfStockTransaction object.
   *
   * @exception de.ios.framework.basic.KontorException.
   * @exception java.rmi.RemoteException if the connection to the server failed.
   */
    public Iterator getKindOfStockTransactions(KindOfStockTransaction o) throws java.rmi.RemoteException, KontorException;

    /**
   * Find all KindOfStockTransactions using Data Carrier/direct SQL
   *
   * @return an Iterator with all KindOfStockTransactions as KindOfStockTransactionDC objects.
   *
   * @exception de.ios.framework.basic.KontorException.
   * @exception java.rmi.RemoteException if the connection to the server failed.
   */
    public Iterator getKindOfStockTransactionDC() throws java.rmi.RemoteException, KontorException;
}
