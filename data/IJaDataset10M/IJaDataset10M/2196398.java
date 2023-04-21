package ecks.services.modules.SrvOper;

import ecks.protocols.Generic;
import ecks.services.Service;
import ecks.services.modules.CommandDesc;
import ecks.services.modules.bCommand;

public class Oper extends bCommand {

    public final CommandDesc Desc = new CommandDesc("oper", 0, true, CommandDesc.access_levels.A_OPER, "Gives you +o", "");

    public CommandDesc getDesc() {
        return Desc;
    }

    public void handle_command(Service who, String user, String replyto, String arguments) {
        Generic.curProtocol.outMODE(who, Generic.Users.get(user.toLowerCase()), "+o", "");
    }
}
