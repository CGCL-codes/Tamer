package org.jagent.service.transport;

import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.agent.JasBean;
import javax.agent.JasConstants;
import javax.agent.Locator;
import javax.agent.TransportMessage;
import javax.agent.service.Service;
import javax.agent.service.ServiceException;
import javax.agent.service.ServiceFailure;
import javax.agent.service.ServiceProperties;
import javax.agent.service.transport.MessageReceiver;
import javax.agent.service.transport.MessageSender;
import javax.agent.service.transport.MessageTransportService;
import javax.agent.service.transport.NotLocatableException;
import javax.agent.service.transport.NoSuchTransportException;
import javax.agent.service.transport.TransportException;
import javax.agent.service.transport.TransportFailure;
import javax.agent.service.transport.TransportSystem;
import org.jagent.service.spi.transport.MessageTransportManager;
import org.jagent.util.URIUtility;
import org.jagent.service.ServicePropertiesImpl;

/**
 * The BasicTransportSystem is a class that provides the infrastructure
 * necessary to support fully realizied TransportSystems.  It provides
 * two methods that allow MessageTransportServices to be loaded and
 * unloaded at runtime.  These transport services are stored in a 
 * Hashtable in a key-value pair relationship.  The key is a String
 * that must be unique and have some meaning with regard to the
 * MessageTransportService it keys to
 * (e.g. rmi :=> RMIMessageTransportService).
 * 
 * @author A. Spydell
 * @since 1.0
 */
public class BasicTransportSystem implements TransportSystem {

    private Hashtable services;

    private ServiceProperties itsSP;

    /**
    * Create a new BasicTransportSystem.
    */
    public BasicTransportSystem() {
        services = new Hashtable();
        itsSP = new ServicePropertiesImpl();
    }

    public ServiceProperties getServiceProperties() throws ServiceException, ServiceFailure {
        ServiceProperties p = new ServicePropertiesImpl();
        p.setAll(itsSP);
        return p;
    }

    public void setServiceProperties(ServiceProperties props) throws ServiceException, ServiceFailure {
        itsSP.setAll(props);
    }

    /**
    * Removes all the MessageTransportServices from the storage
    * Hashtable.
    */
    public void shutdown() {
        services.clear();
    }

    /**
    * Loads a MessageTransportService, as described by the 
    * ServiceProperties, and maps it under the given key.
    *
    * @param type the key to use in the mapping.
    * @param env the environment describing the MessageTransportService
    *           to load.
    *
    * @exception TransportException thrown if the MessageTransportManager
    *           cannot acquire or create the MessageTransportService or
    *           the key is already used as a mapping.
    */
    public void loadTransportService(String type, ServiceProperties env) throws TransportException {
        if (services.containsKey(type)) throw new TransportException("Transport service already loaded.");
        try {
            MessageTransportService service = MessageTransportManager.getMessageTransportService(env);
            services.put(type, service);
        } catch (Exception err) {
            throw new TransportException("Cannot load transport service.", err);
        }
    }

    /**
    * Removes the specified MessageTransportService from the Hashtable.
    */
    public MessageTransportService unloadTransportService(String type) {
        return (MessageTransportService) services.remove(type);
    }

    /**
    * Returns all of the currently available MessageTransportServices.
    *
    * @exception TransportFailure never thrown in this context.
    */
    public MessageTransportService[] getMessageTransportServices() throws TransportFailure {
        MessageTransportService[] array = new MessageTransportService[services.size()];
        int i = 0;
        Enumeration keys = services.keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            array[i++] = (MessageTransportService) services.get(key);
        }
        return array;
    }

    /**
    * Returns the MessageTransportService associated with the given
    * Locator.
    *
    * @see #getMessageTransportService(JasBean)
    */
    public MessageTransportService getMessageTransportService(Locator l) throws NotLocatableException, TransportFailure {
        return getMessageTransportService((JasBean) l);
    }

    /**
    * Returns the MessageTransportService associated with the given
    * JasBean.  An address is being acquired from the JasBean by the
    * the use of the well-known {@link JasConstants#LOCATOR_ADDRESS}.
    * If the resultant is not <code>null</code> and it is a String,
    * then it is parsed as a URI.  The protocol of the URI is used
    * as the key in the mappings to MessageTransportServices.  So,
    * if the protocol is not <code>null</code> and the Hashtable
    * contains the key, the the mapped to MessageTransportService
    * is returned.
    *
    * @param env a JasBean describing a particular MessageTransportService.
    *
    * @return MessageTransportService the service described by the JasBean.
    *
    * @exception NotLocatableException thrown if the JasBean does not
    *               contain the <code>JasConstants.LOCATOR_ADDRESS</code>
    *               or the parsed URI does not have a protocol or the
    *               Hashtable does not contain the protocol.
    * @exception TransportFailure never thrown in this context.
    *
    * @see URIUtility#parseAsURI(String)
    */
    public MessageTransportService getMessageTransportService(JasBean env) throws NotLocatableException, TransportFailure {
        Object address = env.get(JasConstants.LOCATOR_ADDRESS);
        if (address != null && address instanceof String) {
            try {
                String protocol = URIUtility.parseAsURI((String) address)[0];
                if (protocol != null && services.containsKey(protocol)) return (MessageTransportService) services.get(protocol);
            } catch (MalformedURLException err) {
            }
        }
        throw new NotLocatableException();
    }

    /**
    * Returns a new MessageSender that supports the intent of the
    * given JasBean environment.
    *
    * @param env the environment describing a particular MessageSender.
    *
    * @return MessageSender the MessageSender described by the JasBean.
    *
    * @exception NotLocatableException thrown if the MessageTransportService
    *        described by the JasBean is not available.
    * @exception TransportFailure never thrown in this context.
    *
    * @see #getMessageTransportService(JasBean)
    */
    public MessageSender newMessageSender(JasBean env) throws NotLocatableException, TransportFailure {
        return getMessageTransportService(env).newMessageSender(env);
    }

    /**
    * Returns a new MessageReceiver that supports the intent of the
    * given JasBean environment.
    *
    * @param env the environment describing a particular MessageReceiver.
    *
    * @return MessageReceiver the MessageReceiver described by the JasBean.
    *
    * @exception NotLocatableException thrown if the MessageTransportService
    *        described by the JasBean is not available.
    * @exception TransportFailure never thrown in this context.
    *
    * @see #getMessageTransportService(JasBean)
    */
    public MessageReceiver newMessageReceiver(JasBean env) throws NotLocatableException, TransportFailure {
        return getMessageTransportService(env).newMessageReceiver(env);
    }

    /**
    * Sends the TransportMessage to its intended receiver.
    */
    public void sendMessage(TransportMessage msg) throws NotLocatableException, NoSuchTransportException, TransportFailure {
        getMessageTransportService(msg.getReceiver()).sendMessage(msg);
    }
}
