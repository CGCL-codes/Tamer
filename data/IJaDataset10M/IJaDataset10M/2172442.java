package javax.agent.service.transport;

import javax.agent.Locator;
import javax.agent.TransportMessage;

/**
 * The MessageSender is an EndPoint created by a transport service.
 * It is used as the means by which messages are sent from an agent
 * to another agent.  The message sending process consists of first
 * binding to a remote message receiver, as specified by a locator,
 * and then invoking the sendMessage() method.
 *
 * @author A. Spydell
 * @author G. Arnold
 * @since 1.0
 */
public interface MessageSender extends EndPoint {

    /**
    * Binds the MessageSender to a remote endpoint (i.e. MessageReciever)
    * as specified by the locator.
    *
    * @param l the Locator specifing the remote endpoint.
    * @exception NotLocatableException thrown if l does not correspond to
    *            a remote endpoint.
    * @exception TransportFailure if any transport related failure occurs.
    */
    void bindToRemoteLocator(Locator l) throws NotLocatableException, TransportFailure;

    /**
    * Returns the remote locator of the endpoint that the MessageSender
    * is bound.
    *
    * @return Locator the remote endpoint to which the MessageSender
    *         is bound.
    * @exception NotBoundException thrown if the MessageSender is not
    *            bound.
    * @exception TransportFailure if any transport related failure occurs.
    */
    Locator getRemoteLocator() throws NotBoundException, TransportFailure;

    /**
    * Causes the message to be delivered to the remote endpoint to
    * which the MessageSender is bound.
    *
    * @param msg the message to deliver to the remote endpoint.
    * @exception NotLocatableException thrown if l does not correspond to
    *            a remote endpoint.
    * @exception TransportFailure if any transport related failure occurs.
    */
    void sendMessage(TransportMessage msg) throws NotLocatableException, TransportFailure;
}
