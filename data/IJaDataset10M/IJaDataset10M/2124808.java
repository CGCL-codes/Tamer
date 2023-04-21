package com.lyrisoft.chat.client.command;

import com.lyrisoft.chat.client.Client;

/**
 * Handle a ping message
 */
public class Ping extends UserCommandProcessor {

    /**
     * @see com.lyrisoft.chat.server.local.ChatServerLocal#sendPing
     */
    public void process(String input, String arg, Client client) throws NotEnoughArgumentsException {
        String[] args = decompose(input, 2);
        client.getServerInterface().sendPing(args[1], String.valueOf(System.currentTimeMillis()));
    }
}
