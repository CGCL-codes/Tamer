package javax.agent.service.transport;

import javax.agent.JasBean;
import javax.agent.Locator;
import javax.agent.TransportMessage;
import javax.agent.service.ServiceProperties;
import javax.agent.service.Service;

/**
 * The MessageTransportService defines the operations that can be made
 * on a generic transport service.  These are:
 * <ul>
 *    <li> the ability to set service properties,
 *    <li> verify if a locators transport type is supported,
 *    <li> establish local locators,
 *    <li>send messages.
 * </ul>
 *
 * @author A. Spydell
 * @author G. Arnold
 * @since 1.0
 */
public interface MessageTransportService extends Service {

    /**
    * Returns the service properties associated with the transport
    * service.
    *
    * @return the transport's service properties. 
    * @exception TransportFailure if any transport related failure occurs.
    */
    ServiceProperties getServiceProperties() throws TransportFailure;

    /**
    * Determines if the specified locator is supported by the transport
    * service.
    * @param l the locator to test for supportability.
    * @return true if the specified locator is supported.
    * @exception TransportFailure if any transport related failure occurs.
    */
    boolean isSupported(Locator l) throws TransportFailure;

    /**
    * Returns a new local locator whose transport type is supported
    * by the transport service.
    *
    * @return a Locator.
    * @exception TransportFailure if any transport related failure occurs.
    */
    Locator newLocalLocator() throws TransportFailure;

    /**
    * Returns a new local locator whos transport type is supported
    * by the transport service and has the provided environment properties.
    * @exception TransportFailure if any transport related failure occurs.
    */
    Locator newLocalLocator(JasBean env) throws TransportFailure;

    /**
    * Returns a new MessageSender capable of binding to message receivers
    * supported by this transport service.
    *
    * @return MessageSender a message sender with general capabilities.
    * @exception TransportFailure if any transport related failure occurs.
    */
    MessageSender newMessageSender() throws TransportFailure;

    /**
    * Returns a new MessageSender capable of binding to message
    * receivers supported by this transport service and having
    * the specified properties.
    * @param env specific attributes required of the returned message
    *        sender.
    * @return MessageSender a message sender with specific capabilities.
    * @exception TransportFailure if any transport related failure occurs.
    */
    MessageSender newMessageSender(JasBean env) throws TransportFailure;

    /**
    * Returns a new MessageReceiver suitable for local binding.
    *
    * @return MessageReceiver a message receiver with general
    *          capabilities.
    * @exception TransportFailure if any transport related failure occurs.
    */
    MessageReceiver newMessageReceiver() throws TransportFailure;

    /**
    * Returns a new MessageReceiver suitable for local binding having
    * the specified properties.
    *
    * @param env specific attributes required of the returned message
    *        receiver.
    * @return MessageReceiver a message receiver with specific
    *          capabilities.
    * @exception TransportFailure if any transport related failure occurs.
    */
    MessageReceiver newMessageReceiver(JasBean env) throws TransportFailure;

    /**
    * A convenience method directed to a message sending endpoint
    * specified by the message receiver's locator.
    *
    * @param msg the message to send to the receiving agent.
    * @exception NotLocatableException if a message transport service
    *            is not available supporting the specified environment.
    * @exception TransportFailure if any transport related failure occurs.
    */
    void sendMessage(TransportMessage msg) throws NotLocatableException, TransportFailure;

    /**
    * Create an empty TransportMessage suitable for attribute population
    * and use.  The returned value may be <code>null</code>.
    *
    * @return an empty TransportMessage
    */
    TransportMessage createTransportMessage();
}
