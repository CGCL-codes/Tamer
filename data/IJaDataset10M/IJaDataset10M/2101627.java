package joom.commands;

import joom.*;
import joom.state.*;
import java.io.*;
import java.util.*;

/**
 * The Shout command.
 * @author Christopher Brind
 */
public class Shout extends CommandHandler {

    public JOOMState execute(JOOMState state, SocketHandler sh) throws IOException, JOOMException {
        Player p = sh.getPlayer();
        String sWords = "";
        String asArgs[] = getArgs();
        for (int iLoop = 1; iLoop < asArgs.length; iLoop++) {
            sWords = sWords + asArgs[iLoop] + " ";
        }
        sWords = sWords.trim();
        String sType = "say";
        if (sWords.endsWith("?")) {
            sType = "ask";
        }
        String sMessage = p.getUserName() + " shouts, \"" + sWords + "\"";
        Iterator i = PlayerManager.iterateConnectedUsers();
        while (i.hasNext()) {
            Player other = (Player) i.next();
            if (other != p) {
                other.sendMessage(sMessage);
            }
        }
        sh.println("you shout, \"" + sWords + "\"");
        return state;
    }
}
