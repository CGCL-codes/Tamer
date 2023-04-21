package handlers.output;

import handlers.OutputHandler;
import core.Facade;
import structures.ServerChannel;
import gui.TextColor;

/**
 *
 * @author Will
 */
public class JoinHandler extends OutputHandler {

    private static final String[] HOOKS = { "JOIN", "SJOIN" };

    public JoinHandler(Facade mgr) {
        super(mgr);
    }

    @Override
    public String[] getHooks() {
        return HOOKS;
    }

    @Override
    public void process(String msg, ServerChannel dest) {
        String[] splitMsg = msg.split("\\s");
        Facade mgr = getManager();
        if (splitMsg.length == 2) {
            mgr.sendData("JOIN " + splitMsg[1], dest.server);
        } else if ((splitMsg.length == 1) && !dest.equals(ServerChannel.CONSOLE)) {
            mgr.sendData("JOIN " + dest.channel, dest.server);
        } else {
            mgr.println("(ERROR) Command was passed malformed arguments.", dest, TextColor.RED);
            mgr.printDebugMsg(this.getClass().getName() + " was passed illegal arguments. [" + dest + "] " + msg);
        }
    }
}
