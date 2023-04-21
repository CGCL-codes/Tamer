package com.lyrisoft.chat.server.local.command;

import com.lyrisoft.chat.Translator;
import com.lyrisoft.chat.client.IChatClient;

public class AckPartRoom implements ICommandProcessorLocal {

    public void process(IChatClient client, String[] args) {
        if (args.length < 1) {
            client.generalError(Translator.getMessage("error.protocol", args[0]));
        } else {
            client.ackPartRoom(args[1]);
        }
    }
}
