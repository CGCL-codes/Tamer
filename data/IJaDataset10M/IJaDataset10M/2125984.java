package com.lyrisoft.chat.client.command;

import com.lyrisoft.chat.client.Client;

/**
 * Handle an emote message
 */
public class Emote extends UserCommandProcessor {

    /**
     * @see com.lyrisoft.chat.server.local.ChatServerLocal#emoteToRoom
     */
    public void process(String input, String arg, Client client) throws NotEnoughArgumentsException {
        String[] args = decompose(input, 2);
        if (arg.startsWith("PRIVATE__")) {
            String user = arg.substring(9, arg.length());
            client.getServerInterface().emoteToUser(user, args[1]);
            client.emoteToUserPrivate(user, args[1]);
        } else {
            client.getServerInterface().emoteToRoom(arg, args[1]);
        }
    }
}
