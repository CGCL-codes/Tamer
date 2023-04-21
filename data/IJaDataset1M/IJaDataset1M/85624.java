package org.iqual.chaplin.msg;

/**
 * @author Zbynek Slajchrt
 * @since 5.12.2009 19:55:17
 */
public class ReplyTargetSelector implements TargetSelector {

    private final Object sender;

    public ReplyTargetSelector() {
        Message ctxMsg = MessageReceiversChain.getContextMessage();
        if (ctxMsg == null) {
            throw new IllegalStateException("No message in the context.");
        }
        if (ctxMsg.getSource() == null) {
            throw new IllegalStateException("The source in the context message is null.");
        }
        sender = ctxMsg.getSource();
    }

    public boolean isTarget(Object component, Message message) {
        return sender.equals(component);
    }
}
