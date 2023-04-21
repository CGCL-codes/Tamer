package net.grinder.communication;

import net.grinder.util.ListenerSupport;

/**
 * Passive {@link Sender}class that delegates incoming messages to a chain of
 * handlers until one claims to have handled the message.
 *
 * @author Philip Aston
 * @version $Revision: 2790 $
 */
public final class HandlerChainSender implements Sender {

    private final ListenerSupport m_messageHandlers = new ListenerSupport();

    /**
   * Handler interface.
   */
    public interface MessageHandler {

        /**
     * The handler implements this to receive a message.
     *
     * @param message
     *          The message.
     * @return <code>true</code>=> The handler processed the message.
     * @throws CommunicationException
     *           If an error occurred. If a handler throws an exception,
     *           subsequent handlers will not be called.
     *
     */
        boolean process(Message message) throws CommunicationException;

        /**
     * Notify the handler that we've been shutdown.
     */
        void shutdown();
    }

    /**
   * Add a message hander.
   *
   * @param messageHandler The message handler.
   */
    public void add(MessageHandler messageHandler) {
        m_messageHandlers.add(messageHandler);
    }

    /**
   * Adapt a {@link Sender} to be a message handler.
   *
   * @param sender The sender.
   */
    public void add(final Sender sender) {
        add(new MessageHandler() {

            public boolean process(Message message) throws CommunicationException {
                sender.send(message);
                return false;
            }

            public void shutdown() {
                sender.shutdown();
            }
        });
    }

    /**
   * Sends a message to each handler until one claims to have handled the
   * message.
   *
   * @param message The message.
   * @throws CommunicationException If one of the handlers failed.
   */
    public void send(final Message message) throws CommunicationException {
        final CommunicationException[] exception = new CommunicationException[1];
        m_messageHandlers.apply(new ListenerSupport.HandlingInformer() {

            public boolean inform(Object listener) {
                try {
                    return ((MessageHandler) listener).process(message);
                } catch (CommunicationException e) {
                    exception[0] = e;
                    return true;
                }
            }
        });
        if (exception[0] != null) {
            throw exception[0];
        }
    }

    /**
  * Shutdown all our handlers.
  */
    public void shutdown() {
        m_messageHandlers.apply(new ListenerSupport.Informer() {

            public void inform(Object listener) {
                ((MessageHandler) listener).shutdown();
            }
        });
    }
}
