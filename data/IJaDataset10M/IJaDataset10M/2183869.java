package net.jetrix.commands;

import java.util.*;
import net.jetrix.messages.channel.CommandMessage;
import net.jetrix.messages.channel.PlineMessage;
import net.jetrix.*;

/**
 * Show the spectators in the channel.
 *
 * @author Emmanuel Bourg
 * @version $Revision: 798 $, $Date: 2009-02-18 10:24:28 -0500 (Wed, 18 Feb 2009) $
 */
public class SpecListCommand extends AbstractCommand {

    public String[] getAliases() {
        return new String[] { "speclist", "slist" };
    }

    public void execute(CommandMessage m) {
        Client client = (Client) m.getSource();
        StringBuilder message = new StringBuilder();
        Iterator specators = client.getChannel().getSpectators();
        while (specators.hasNext()) {
            Client spectator = (Client) specators.next();
            message.append(spectator.getUser().getName());
            if (specators.hasNext()) {
                message.append(", ");
            }
        }
        PlineMessage response = new PlineMessage();
        response.setKey("command.speclist.format", message.toString());
        client.send(response);
    }
}
