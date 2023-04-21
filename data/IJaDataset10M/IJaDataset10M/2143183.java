package org.redwood.business.etl.dnsresolver;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * The EJBHome interface.
 *
 * @author  M. Lehnert
 * @version 1.0
 */
public interface DnsResolverHome extends EJBHome {

    public static final String COMP_NAME = "DnsResolver";

    public static final String JNDI_NAME = "DnsResolver";

    /**
   * Creates the DNS Resolver Bean.
   *
   * @return    the created EJBObject.
   * @exception RemoteException.
   */
    public DnsResolverObject create() throws RemoteException, CreateException;
}
