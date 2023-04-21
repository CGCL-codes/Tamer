package com.google.code.rees.scope.conversation.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.code.rees.scope.util.monitor.TimeoutListener;
import com.google.code.rees.scope.util.monitor.TimeoutMonitor;

/**
 * This class extends the {@link DefaultConversationContextManager} and uses a {@link TimeoutMonitor} to
 * monitor conversations for timeouts.  Using the {@link TimeoutListener} interface, it removes conversations
 * from its cache when they have expired.
 * 
 * @author rees.byars
 *
 */
public class TimeoutConversationContextManager extends DefaultConversationContextManager implements TimeoutListener<ConversationContext> {

    private static final long serialVersionUID = -4431057690602876686L;

    private static final Logger LOG = LoggerFactory.getLogger(TimeoutConversationContextManager.class);

    protected TimeoutMonitor<ConversationContext> conversationTimeoutMonitor;

    public void setTimeoutMonitor(TimeoutMonitor<ConversationContext> conversationTimeoutMonitor) {
        this.conversationTimeoutMonitor = conversationTimeoutMonitor;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public ConversationContext getContext(String conversationName, String conversationId) {
        ConversationContext context = super.getContext(conversationName, conversationId);
        context.addTimeoutListener(this);
        this.conversationTimeoutMonitor.addTimeoutable(context);
        return context;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public ConversationContext remove(String conversationName, String conversationId) {
        ConversationContext context = super.remove(conversationName, conversationId);
        if (context != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Conversation has ended, removing from TimeoutMonitor:  " + conversationName + " with ID " + conversationId);
            }
            this.conversationTimeoutMonitor.removeTimeoutable(context);
        }
        return context;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void onTimeout(ConversationContext expiredConversation) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Conversation has timed out:  " + expiredConversation.getConversationName() + " with ID " + expiredConversation.getId());
        }
        super.remove(expiredConversation.getConversationName(), expiredConversation.getId());
    }

    /**
	 * {@inheritDoc}
	 */
    public void destroy() {
        LOG.debug("Destroying TimeoutConversationContextManager.");
        super.destroy();
        LOG.debug("Destroying TimeoutMonitor.");
        this.conversationTimeoutMonitor.destroy();
        LOG.debug("TimeoutConversationContextManager and TimeoutMonitor destroyed.");
    }
}
