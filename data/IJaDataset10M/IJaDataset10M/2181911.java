package com.lyrisoft.chat.server.local.command;

import com.lyrisoft.chat.Translator;
import com.lyrisoft.chat.client.IChatClient;

public class SayToRoom implements ICommandProcessorLocal {

    public void process(IChatClient client, String[] args) {
        if (args.length < 4) {
            client.generalError(Translator.getMessage("error.protocol", args[0]));
        } else {
            client.messageFromUser(args[1], args[2], args[3]);
        }
    }
}
