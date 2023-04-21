package org.wisigoth.chat.client;

/**
 * A message receiver listen to {@link Message}
 * @author tof
 *
 */
public interface MessageConsumer {

    public void consume(Message message);

    public MessageFilter getFilter();
}
