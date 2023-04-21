package com.chessclub.simulbot.commands;

import com.chessclub.simulbot.Settings;
import com.chessclub.simulbot.SimulHandler;
import com.chessclub.simulbot.datagrams.Tell;

public class Access {

    public static void access(Tell t) {
        int authorized = Common.isAuthorized(t);
        if (authorized == Settings.STANDARD) {
            SimulHandler.getHandler().tell(t.getHandle(), "You have player access to the bot.");
        } else if (authorized == Settings.SIMUL_GIVER) {
            SimulHandler.getHandler().tell(t.getHandle(), "You have simul-giver access to the bot.");
        } else if (authorized == Settings.MANAGER) {
            SimulHandler.getHandler().tell(t.getHandle(), "You have manager access to the bot.");
        } else if (authorized == Settings.SUPER) {
            SimulHandler.getHandler().tell(t.getHandle(), "You have super-user access to the bot.");
        } else if (authorized == Settings.ADMIN) {
            SimulHandler.getHandler().tell(t.getHandle(), "You have admin access to the bot.");
        } else {
            SimulHandler.getHandler().tell(t.getHandle(), "You have unknown access to the bot.");
        }
    }
}
