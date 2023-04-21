package javax.agent.service.transport;

import javax.agent.JasBean;
import javax.agent.Locator;
import javax.agent.TransportMessage;
import javax.agent.service.Service;

/**
 * The TransportSystem provides a collection of transport services and
 * transport system level methods.  These methods facilitate message
 * sending and receiving independent of specific underlying transport
 * services.
 *
 * @author A. Spydell
 * @author G. Arnold
 * @since 1.0
 */
public interface TransportSystem extends Service {

    /** The well-known service name. */
    String SERVICE_TYPE = "message-transport-system";

    /**
    * Returns an enumeration of the available underlying message
    * transport services.
    *
    * @return an  array of transport services available to the agent.
    * @exception TransportFailure if any transport related failure occurs.
    */
    MessageTransportService[] getMessageTransportServices() throws TransportFailure;

    /**
    * Returns a message transport service capable of supporting the
    * locators underlying message transport type.
    *
    * @param l specifies the transport service required.
    * @return a transport service specified by the argument.
    * @exception NotLocatableException if a message transport service
    *            is not available supporting the specified locator.
    * @exception TransportFailure if any transport related failure occurs.
    */
    MessageTransportService getMessageTransportService(Locator l) throws NotLocatableException, TransportFailure;

    /**
    * Returns a message transport service capable of supporting the
    * properties specified in the JasBean.
    *
    * @param env the environment specifing a particular transport service.
    * @return a transport service specified by the argument. 
    * @exception NotLocatableException if a message transport service
    *            is not available supporting the specified environment.
    * @exception TransportFailure if any transport related failure occurs.
    */
    MessageTransportService getMessageTransportService(JasBean env) throws NotLocatableException, TransportFailure;

    /**
    * A convenience method directed to a transport service specified 
    * by the provided environment returning a new message sending
    * endpoint.
    *
    * @param env the environment specifing a particular transport service.
    * @return MessageSender a message sending endpoint capable of
    *          supporting the specified environment.
    * @exception NotLocatableException if a message transport service
    *            is not available supporting the specified environment.
    * @exception TransportFailure if any transport related failure occurs.
    */
    MessageSender newMessageSender(JasBean env) throws NotLocatableException, TransportFailure;

    /**
    * A convenience method directed to a transport service specified 
    * by the provided environment returning a new message receiving
    * endpoint.
    *
    * @param env the environment specifing a particular transport service.
    * @return MessageReceiver a message receiving endpoint capable of
    *          supporting the specified environment.
    * @exception NotLocatableException if a message transport service
    *            is not available supporting the specified environment.
    * @exception TransportFailure if any transport related failure occurs.
    */
    MessageReceiver newMessageReceiver(JasBean env) throws NotLocatableException, TransportFailure;

    /**
    * A convenience method directed to a message sending endpoint
    * specified by the message receiver's locator.
    *
    * @param msg the message to send to the receiving agent.
    * @exception NotLocatableException if the receiver's implicit 
    *             <tt>Locator</tt> does not correspond to a remote endpoint.
    * @exception NoSuchTransportException if a message transport service
    *            is not available to support the specified environment
    * @exception TransportFailure if any transport related failure occurs.
    */
    void sendMessage(TransportMessage msg) throws NotLocatableException, NoSuchTransportException, TransportFailure;
}
