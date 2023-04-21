package com.objectwave.persist.bean;

import com.objectwave.persist.QueryException;
import java.rmi.*;
import javax.ejb.*;

/**
 * @author  dhoag
 * @version  $Id: JGrinderQuery.java,v 2.2 2005/02/13 03:26:28 dave_hoag Exp $
 */
public interface JGrinderQuery extends EJBObject {

    /**
	 * @param  q
	 * @return
	 * @exception  QueryException
	 * @exception  RemoteException
	 */
    public java.lang.Object find(com.objectwave.persist.ObjectQuery q) throws QueryException, RemoteException;

    /**
	 * @param  q
	 * @param  at
	 * @return
	 * @exception  QueryException
	 * @exception  RemoteException
	 */
    public java.util.Vector findAttributes(com.objectwave.persist.ObjectQuery q, java.lang.String[] at) throws QueryException, RemoteException;

    /**
	 * @param  q
	 * @return
	 * @exception  QueryException
	 * @exception  RemoteException
	 */
    public com.objectwave.persist.Persistence findUnique(com.objectwave.persist.ObjectQuery q) throws QueryException, RemoteException;

    /**
	 *  A unit test for JUnit
	 *
	 * @return
	 * @exception  RemoteException
	 */
    public com.objectwave.persist.ObjectQuery testIt() throws RemoteException;

    /**
	 * @param  q
	 * @return
	 * @exception  QueryException
	 * @exception  RemoteException
	 */
    public int count(com.objectwave.persist.ObjectQuery q) throws QueryException, RemoteException;
}
