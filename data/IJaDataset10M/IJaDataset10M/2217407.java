package org.tinymarbles.impl.hibernate;

public interface ConversationFactory {

    Conversation create();

    void close();
}
