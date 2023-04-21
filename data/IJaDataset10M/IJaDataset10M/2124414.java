package org.asky78.chat;

public interface MessageCastingListener {

    public void listen(Message message, User user);

    public void broadcast(Message message);
}
