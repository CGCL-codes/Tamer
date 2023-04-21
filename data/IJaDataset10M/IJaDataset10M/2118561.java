package org.jgentleframework.integration.remoting.rmi.support;

import java.rmi.Remote;
import java.rmi.RemoteException;
import org.jgentleframework.integration.remoting.RemoteInvocation;
import org.jgentleframework.integration.remoting.RemotingException;

/**
 * The Interface RmiWrappingBeanExporter.
 * 
 * @author LE QUOC CHUNG - mailto: <a
 *         href="mailto:skydunkpro@yahoo.com">skydunkpro@yahoo.com</a>
 * @date Mar 16, 2008
 */
public interface RmiWrappingBeanExporter extends Remote {

    /**
	 * Executes the remote invocation.
	 * 
	 * @param invocation
	 *            the invocation
	 * @return the object
	 * @throws RemoteException
	 *             the remote exception
	 * @throws RemotingException
	 *             the remoting exception
	 */
    public Object invoke(RemoteInvocation invocation) throws RemoteException, RemotingException;
}
