package net.jini.event;

import java.rmi.RemoteException;
import net.jini.core.lease.LeaseDeniedException;

/**
 * The <code>PullEventMailbox</code> interface allows clients to specify and use
 * a third party for the purpose of storing and retrieving events. It extends
 * <code>EventMailbox</code> by by adding the ability to synchronously retrieve
 * events from the event mailbox service.
 * 
 * @author Sun Microsystems, Inc.
 * 
 * @see MailboxRegistration
 * 
 * @since 2.1
 * 
 */
public interface PullEventMailbox extends EventMailbox {

    /**
	 * Defines the interface to the event mailbox service. Event mailbox clients
	 * utilize this service by invoking the <code>pullRegister</code> method to
	 * register themselves with the service.
	 * 
	 * @param leaseDuration
	 *            the requested lease duration in milliseconds
	 * @return A new <code>MailboxPullRegistration</code>
	 * 
	 * @throws IllegalArgumentException
	 *             if <code>leaseDuration</code> is not positive or
	 *             <code>Lease.ANY</code>.
	 * 
	 * @throws java.rmi.RemoteException
	 *             if there is a communication failure between the client and
	 *             the service.
	 * 
	 * @throws net.jini.core.lease.LeaseDeniedException
	 *             if the mailbox service is unable or unwilling to grant this
	 *             registration request.
	 */
    MailboxPullRegistration pullRegister(long leaseDuration) throws RemoteException, LeaseDeniedException;
}
