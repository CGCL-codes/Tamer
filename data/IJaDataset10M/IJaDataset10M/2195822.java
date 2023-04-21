package org.ikasan.framework.initiator.messagedriven.jca;

import org.apache.log4j.Logger;
import org.ikasan.framework.initiator.messagedriven.ListenerSetupFailureListener;
import org.ikasan.framework.initiator.messagedriven.MessageListenerContainer;
import org.springframework.jms.listener.endpoint.JmsMessageEndpointManager;

/**
 * Extension of Spring's DefaultMessageListenerContainer to expose listener setup failures to a registered Listener
 *
 * @author Ikasan Development Team
 *
 */
public class SpringMessageListenerContainer extends JmsMessageEndpointManager implements MessageListenerContainer {

    private Logger logger = Logger.getLogger(SpringMessageListenerContainer.class);

    /**
     * Flag indicating last attempt to connect was a failure
     */
    private boolean listenerSetupFailure = false;

    /**
     * Registered failure listener
     */
    private ListenerSetupFailureListener listenerSetupExceptionListener;

    public void setListenerSetupExceptionListener(ListenerSetupFailureListener listenerSetupExceptionListener) {
        this.listenerSetupExceptionListener = listenerSetupExceptionListener;
    }

    /**
     * Accessor for listenerSetupFailure flag
     *
     * @return listenerSetupFailure
     */
    public boolean isListenerSetupFailure() {
        return listenerSetupFailure;
    }
}
