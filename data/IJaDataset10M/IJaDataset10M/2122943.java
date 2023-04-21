package ecks.services.modules.SrvHelp;

import ecks.Configuration;
import ecks.protocols.Generic;
import ecks.services.Service;
import ecks.services.SrvChannel;
import ecks.services.SrvHelp;
import ecks.services.modules.CommandDesc;
import ecks.services.modules.bCommand;

public class Sync extends bCommand {

    public final CommandDesc Desc = new CommandDesc("sync", 1, true, CommandDesc.access_levels.A_NONE, "Initiates a help request", "[channel]");

    public CommandDesc getDesc() {
        return Desc;
    }

    public void handle_command(Service who, String user, String replyto, String arguments) {
        String whatchan = "";
        String whom = "";
        String args[] = arguments.split(" ");
        boolean silent = false;
        whatchan = replyto;
        whom = user;
        if (args.length > 0 && (!(args[0].equals("")))) {
            if (args[0].startsWith("#")) {
                whatchan = args[0];
            }
            if (args[0].equals("silent")) silent = true;
        }
        whom = whom.toLowerCase();
        whatchan = whatchan.toLowerCase();
        if (whatchan.startsWith("#")) {
            if (((SrvHelp) who).getChannels().containsKey(whatchan)) {
                if (((SrvChannel) Configuration.getSvc().get(Configuration.chanservice)).getChannels().containsKey(whatchan)) if (Generic.Users.containsKey(whom)) if (!((SrvChannel) Configuration.getSvc().get(Configuration.chanservice)).getChannels().get(whatchan).getUsers().containsKey(Generic.Users.get(whom).authhandle)) {
                    ((SrvHelp) who).getChannels().get(whatchan).queue.add(Generic.Users.get(whom));
                    Generic.curProtocol.outPRVMSG(who, whom, "Welcome to " + whatchan + ", " + whom + ". Your are in position #" + ((SrvHelp) who).getChannels().get(whatchan).queue.indexOf(Generic.Users.get(whom)) + ". Please wait patiently until one of the channel operators responds to you.");
                }
            } else if (!silent) Generic.curProtocol.outPRVMSG(who, replyto, "Error: Not a registered channel!");
        } else if (!silent) Generic.curProtocol.outPRVMSG(who, replyto, "Error: Not a channel!");
    }
}
