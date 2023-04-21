package com.google.code.jdde.event;

import com.google.code.jdde.ddeml.CallbackParameters;
import com.google.code.jdde.server.DdeServer;
import com.google.code.jdde.server.ServerConversation;

/**
 * 
 * @author Vitor Costa
 */
public class ExecuteEvent extends ConversationEvent<DdeServer, ServerConversation> {

    private String command;

    public ExecuteEvent(DdeServer server, ServerConversation conversation, CallbackParameters parameters) {
        super(server, conversation);
        command = new String(parameters.getHdata());
    }

    public String getCommand() {
        return command;
    }
}
