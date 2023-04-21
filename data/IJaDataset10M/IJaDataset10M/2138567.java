package net.jetrix.commands;

import java.util.*;
import net.jetrix.*;
import net.jetrix.messages.channel.CommandMessage;
import net.jetrix.messages.channel.PlineMessage;

/**
 * Send a message to all members of the player's team.
 *
 * @author Emmanuel Bourg
 * @version $Revision: 798 $, $Date: 2009-02-18 10:24:28 -0500 (Wed, 18 Feb 2009) $
 */
public class TeamMessageCommand extends AbstractCommand implements ParameterCommand {

    public String[] getAliases() {
        return (new String[] { "tmsg", "gu" });
    }

    public String getUsage(Locale locale) {
        return "/tmsg <" + Language.getText("command.params.message", locale) + ">";
    }

    public int getParameterCount() {
        return 1;
    }

    public void execute(CommandMessage m) {
        Client client = (Client) m.getSource();
        if (client.getUser().getTeam() == null) {
            PlineMessage response = new PlineMessage();
            response.setKey("command.tmsg.not_in_team");
            client.send(response);
        } else {
            PlineMessage response = new PlineMessage();
            response.setKey("command.tmsg.format", client.getUser().getName(), m.getText());
            for (Client target : ClientRepository.getInstance().getClients()) {
                if (client.getUser().getTeam().equals(target.getUser().getTeam()) && client != target) {
                    target.send(response);
                }
            }
        }
    }
}
