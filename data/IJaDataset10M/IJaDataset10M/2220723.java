package de.ios.kontor.sv.order.co;

import java.rmi.*;
import java.util.*;
import de.ios.framework.basic.*;
import de.ios.kontor.utils.*;
import de.ios.kontor.sv.address.co.*;
import de.ios.kontor.sv.basic.co.*;

/**
 * RateController deals with a set of Rates
 * within the kontor framework.
 * It's a abstract baseclass for differrent Rate-types.
 */
public interface RateController extends BasicController {

    /**
   * Get the Rate with the matching oid.
   *
   * @param oid Object-Id of the Rate searched.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   * @exception de.ios.kontor.utils.KontorException if the loading of the Rate failed.
   */
    public Rate getRateByOId(long oid) throws java.rmi.RemoteException, KontorException;

    /**
   * Get the Rate with the matching RateSpecification.
   *
   * @param rspec the RateSpecification of the Rate searched.
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   * @exception de.ios.kontor.utils.KontorException if the loading of the Rate failed.
   */
    public Rate getRateByRateSpecification(RateSpecification rspec) throws java.rmi.RemoteException, KontorException;

    /**
   * Find Rates by some of it's Attributes
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   * @exception de.ios.kontor.utils.KontorException if the loading of Rates failed.
   */
    public Iterator getRates(Rate comp) throws java.rmi.RemoteException, KontorException;

    /**
   * Create a Rate.
   * @exception de.ios.kontor.utils.KontorException if the creation of the Rate failed.
   * @exception java.rmi.RemoteException if the connection to the server failed.
   */
    public Rate createRate() throws KontorException, RemoteException;
}

;
