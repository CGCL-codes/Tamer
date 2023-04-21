package org.jagent.service.transport.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.agent.Locator;
import javax.agent.TransportMessage;

/**
 * The RMIMessageReceiver describes the RMI features layered onto
 * a MessageReceiver.  This interface defines the RMI communication
 * channel from an RMIMessageSender.
 *
 * @author A. Spydell
 * @since 1.0
 */
public interface RMIMessageReceiver extends Remote {

    /**
    * Gets the Locator underwhich this RMIMessageReceiver is bound.
    */
    Locator getLocator() throws RemoteException;

    /**
    * Injection point of incoming RMI messages.
    */
    void acceptMessage(TransportMessage msg) throws RemoteException;
}
