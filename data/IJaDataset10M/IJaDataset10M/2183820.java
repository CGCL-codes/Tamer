package org.iqual.chaplin.msg;

import org.iqual.chaplin.DynaCastUtils;
import org.iqual.chaplin.AggregationType;
import org.iqual.util.Nulls;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;

/**
 * This class passes the message to all receivers. The receivers get this instance as an argument and it is assumed
 * that the receivers call back the chain's <code>process</code> method.
 *
 * @author Zbynek Slajchrt
 * @since Jun 4, 2009 10:34:42 PM
 */
public class MessageReceiversChain {

    private Iterator<MessageReceiver> receivers;

    /**
     * The owner of the chain is a message receiver which creates a new chain. Processing the newly created chain
     * is a part of the receiver's message processing procedure.
     */
    private final MessageReceiver chainOwner;

    private class StackEntry {

        Message contextMessage;

        MessageReplies replies;

        private StackEntry(Message contextMessage, MessageReplies replies) {
            this.contextMessage = contextMessage;
            this.replies = replies;
        }

        public MessageReceiversChain getChain() {
            return MessageReceiversChain.this;
        }

        public Message getContextMessage() {
            return contextMessage;
        }

        public MessageReplies getReplies() {
            return replies;
        }
    }

    static final ThreadLocal<Stack<StackEntry>> contextChain = new ThreadLocal<Stack<StackEntry>>();

    public MessageReceiversChain(MessageReceiver owner, MessageReceiver[] receivers) {
        this(owner, Arrays.asList(receivers).iterator());
    }

    public MessageReceiversChain(MessageReceiver... receivers) {
        this(null, receivers);
    }

    public MessageReceiversChain(Iterator<MessageReceiver> receivers) {
        this(null, receivers);
    }

    public MessageReceiversChain(MessageReceiver owner, Iterator<MessageReceiver> receivers) {
        this.chainOwner = owner;
        this.receivers = receivers;
    }

    public MessageReceiver getChainOwner() {
        return chainOwner;
    }

    public void process(Message message, MessageReplies replies) throws Throwable {
        while (receivers.hasNext()) {
            MessageReceiver receiver = receivers.next();
            if (receiver instanceof FilterableMessageReceiver) {
                FilterableMessageReceiver filterable = (FilterableMessageReceiver) receiver;
                if (!filterable.isMessageAcceptable(message)) {
                    continue;
                }
            }
            pushContextChain(message, replies);
            try {
                receiver.onMessageReceived(message, replies, this);
            } finally {
                popContextChain();
            }
            break;
        }
    }

    /**
     * Stop chain processing.
     */
    public void stop() {
        receivers = Nulls.NULL_ITERATOR;
    }

    public boolean isFinished() {
        return !receivers.hasNext();
    }

    private void pushContextChain(Message message, MessageReplies replies) {
        Stack<StackEntry> chainStack = contextChain.get();
        if (chainStack == null) {
            chainStack = new Stack<StackEntry>();
            contextChain.set(chainStack);
        }
        chainStack.push(new StackEntry(message, replies));
    }

    private void popContextChain() {
        Stack<StackEntry> chainStack = contextChain.get();
        chainStack.pop();
        if (chainStack.isEmpty()) {
            contextChain.remove();
        }
    }

    public static void processInContext(Message message, MessageReplies replies) throws Throwable {
        Stack<StackEntry> chainStack = contextChain.get();
        if (chainStack != null && !chainStack.isEmpty()) {
            MessageReceiversChain chain = chainStack.peek().getChain();
            chain.process(message, replies);
        }
    }

    public static void processInContext(MessageReplies replies, Object... arguments) throws Throwable {
        Stack<StackEntry> chainStack = contextChain.get();
        if (chainStack != null && !chainStack.isEmpty()) {
            StackEntry chainStackEntry = chainStack.peek();
            MessageReceiversChain chain = chainStackEntry.getChain();
            Message message = chainStackEntry.getContextMessage().createClone(arguments);
            chain.process(message, replies);
        }
    }

    /**
     * Invoke the context message chain and hence process the clone of the context message which carries
     * the arguments passed to this method.
     *
     * @param arguments the arguments for the clone of the context message
     * @return the result of processing the message
     * @throws Throwable
     * @throws NoReplyException        after processing the context message the replies container does not contain any
     *                                 reply which could be returned.
     * @throws TooManyRepliesException after processing the context message the replies container contains more than 1 reply
     *                                 so that it is impossible to decide which should be returned.
     * @throws NoContextChainException there is no context message chain bound to the current thread.
     */
    public static <T> T processInContext(Object... arguments) throws Throwable {
        Stack<StackEntry> chainStack = contextChain.get();
        if (chainStack != null && !chainStack.isEmpty()) {
            StackEntry chainStackEntry = chainStack.peek();
            MessageReceiversChain chain = chainStackEntry.getChain();
            Message message = chainStackEntry.getContextMessage().createClone(arguments);
            MessageReplies replies = chainStackEntry.getReplies();
            chain.process(message, replies);
            if (replies.getReplyCount() == 0) {
                if (message.getReturnType() == void.class) {
                    return null;
                } else {
                    throw new NoReplyException();
                }
            } else return (T) MessagingHelper.mergeReplies(message, replies.getReplies(), false);
        } else {
            throw new NoContextChainException();
        }
    }
}
