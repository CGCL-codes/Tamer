package ecks.services.modules;

import ecks.protocols.Generic;
import ecks.services.Service;

public class cmdLoadModule extends bCommand {

    public final CommandDesc Desc = new CommandDesc("loadmod", 1, true, CommandDesc.access_levels.A_SRA, "Load a module.", "<path to module>");

    public CommandDesc getDesc() {
        return Desc;
    }

    public void handle_command(Service who, String user, String replyto, String arguments) {
        try {
            who.addCommand(((CommandModule) Class.forName(arguments).newInstance()).getName().toLowerCase(), (CommandModule) Class.forName(arguments).newInstance());
            Generic.curProtocol.outPRVMSG(who, replyto, "Loading: Success!");
        } catch (ClassCastException e) {
            e.printStackTrace();
            Generic.curProtocol.outPRVMSG(who, replyto, "Error: That's not one of my modules!");
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Generic.curProtocol.outPRVMSG(who, replyto, "Error: Module not found.");
        }
    }
}
