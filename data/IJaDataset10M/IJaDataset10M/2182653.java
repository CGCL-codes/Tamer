package net.jetrix.commands;

import java.util.Locale;
import net.jetrix.messages.channel.CommandMessage;
import net.jetrix.messages.channel.PlineMessage;
import net.jetrix.Language;
import net.jetrix.User;
import net.jetrix.Client;

/**
 * Set the away status of a player.
 *
 * @author Emmanuel Bourg
 * @version $Revision: 798 $, $Date: 2009-02-18 10:24:28 -0500 (Wed, 18 Feb 2009) $
 * @since 0.2
 */
public class AwayCommand extends AbstractCommand {

    public String[] getAliases() {
        return new String[] { "away", "afk" };
    }

    public String getUsage(Locale locale) {
        return "/afk <" + Language.getText("command.params.message", locale) + ">";
    }

    public void execute(CommandMessage message) {
        Client client = (Client) message.getSource();
        User user = client.getUser();
        PlineMessage response = new PlineMessage();
        if (user.getStatus() == User.STATUS_AFK) {
            user.setStatus(User.STATUS_OK);
            response.setKey("command.away.off");
        } else {
            user.setStatus(User.STATUS_AFK);
            response.setKey("command.away.on");
            if (message.getParameterCount() > 0) {
                user.setProperty("command.away.message", message.getText());
            } else {
                user.setProperty("command.away.message", null);
            }
        }
        client.send(response);
    }
}
