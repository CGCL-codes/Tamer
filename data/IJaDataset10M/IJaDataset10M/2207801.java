package server.MWChatServer.commands;

import server.MWChatServer.MWChatClient;
import server.MWChatServer.MWChatServer;
import server.MWChatServer.Translator;

public class UnknownCommand extends CommandBase {

    public boolean process(MWChatClient client, String[] args) {
        client.generalError(Translator.getMessage("unknown_command"));
        return false;
    }

    public void processDistributed(String client, String origin, String[] args, MWChatServer server) {
    }
}
