package com.valtech.bootcamp.carRental.business.customer;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import javax.ejb.FinderException;

public interface CustomerHome extends EJBHome {

    /**
     * @param csh CustomerStateHolder object
     */
    Customer create(CustomerStateHolder csh) throws CreateException, RemoteException;

    /**
     * @param pk String value of the primary key
     */
    Customer findByPrimaryKey(String pk) throws FinderException, RemoteException;
}
