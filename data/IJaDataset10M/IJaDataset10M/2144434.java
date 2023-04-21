package net.services.servicebus.transport.client;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.services.servicebus.ConnectivitySettings;
import net.services.servicebus.IMessageBufferMessageListener;
import net.services.servicebus.ITokenProvider;
import net.services.servicebus.SBMessages;
import net.services.servicebus.ServiceType;
import net.services.servicebus.util.MessageParser;

/**
 * A singleton instance of MessageListener which listens on MBMR.
 * 
 * @author Anish
 */
public class ResponseMessageListener implements IMessageBufferMessageListener {

    private static final Logger LOGGER = Logger.getLogger(ResponseMessageListener.class.getName());

    private static Hashtable<String, ResponseMessageListener> handlers = new Hashtable<String, ResponseMessageListener>();

    private static Hashtable<String, ResponseCorrelationContext> responseContextMapper = new Hashtable<String, ResponseCorrelationContext>();

    private String uri = null;

    private ITokenProvider tokenProvider;

    private ResponseCorrelationContext context = null;

    private ServiceType serviceType = null;

    public static ResponseMessageListener getResponseHandler(String uri, ITokenProvider tokenProvider) {
        if (handlers.containsKey(uri)) {
            LOGGER.log(Level.FINE, SBMessages.LISTENER_FOUND_URI(uri));
            return handlers.get(uri);
        } else {
            ResponseMessageListener handler = new ResponseMessageListener(uri, tokenProvider);
            handlers.put(uri, handler);
            LOGGER.log(Level.FINE, SBMessages.LISTENER_INSERTION_URI(uri));
            return handler;
        }
    }

    private ResponseMessageListener(String uri, ITokenProvider tokenProvider) {
        this(uri, tokenProvider, ServiceType.UNICAST);
    }

    private ResponseMessageListener(String uri, ITokenProvider tokenProvider, ServiceType serviceType) {
        this.uri = uri;
        this.tokenProvider = tokenProvider;
        this.serviceType = serviceType;
        initializeMessageBuffer();
    }

    private void initializeMessageBuffer() {
        ConnectivitySettings.getMessageBufferClient(tokenProvider).getReceiver().subscribe(tokenProvider, this);
    }

    public String getUri() {
        return uri;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void processMessage(String message) {
        MessageParser parser = MessageParser.parse(message);
        String relatesTo = parser.getRelatesTo();
        ResponseCorrelationContext context = (relatesTo == null) ? this.context : responseContextMapper.get(relatesTo);
        if (context != null) {
            Object waitHandle = context.getWaitHandle();
            synchronized (waitHandle) {
                context.setResponseMessage(message);
                waitHandle.notify();
            }
        } else {
        }
    }

    /**
     * Blocks and retrieve the response message from MessageBuffer.
     * 
     * Blocks the current thread for a maximum of 30 seconds to retrive the 
     * asynchronous response message from Service Bus.
     * 
     * @param id MessageId of the request
     * @return Response message (null if response didn't come in 30 seconds)
     */
    public String getResponseMessage(String id, long timeOutIntervel) {
        ResponseCorrelationContext context = new ResponseCorrelationContext();
        this.context = context;
        LOGGER.log(Level.FINE, SBMessages.RESPONSE_CORRELATION_CONTEXT_CREATE(id));
        responseContextMapper.put(id, context);
        LOGGER.log(Level.FINE, SBMessages.RESPONSE_CORRELATION_CONTEXT_INSET(id));
        try {
            Object waitHandle = context.getWaitHandle();
            synchronized (waitHandle) {
                waitHandle.wait(timeOutIntervel);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, SBMessages.EXCEPTION_RESPONSE_WAIT_HANDLE(timeOutIntervel, ex.getMessage()));
            ;
        }
        responseContextMapper.remove(id);
        LOGGER.log(Level.FINE, SBMessages.RESPONSE_CORRELATION_CONTEXT_REMOVE(id));
        return context.getResponseMessage();
    }
}
