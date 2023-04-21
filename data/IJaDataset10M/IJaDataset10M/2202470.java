package com.serena.xmlbridge.adapter.qc9.gen;

import com4j.*;

/**
 * ITDChat Interface
 */
@IID("{D323F3D1-837E-4C0F-9ACB-7CBCDDA557DC}")
public interface ITDChat extends Com4jObject {

    /**
     * Connects to chat.
     */
    @VTID(7)
    java.lang.String connect(java.lang.String chatRoom);

    /**
     * Disconnects from chat.
     */
    @VTID(8)
    void disconnect();

    /**
     * Sends message to chat and recieve new messages.
     */
    @VTID(9)
    java.lang.String putMessage(java.lang.String chatMessage);

    /**
     * Receives new chat messages.
     */
    @VTID(10)
    java.lang.String chatData(boolean getAllMesseges);

    /**
     * Changes chat room.
     */
    @VTID(11)
    void changeChat(java.lang.String newChatRoom);
}
