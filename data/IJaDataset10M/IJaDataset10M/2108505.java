package net.ucef.server.chat;

/** <p>Grant administrator privileges to user.</p>
 *
 * @author Bjoern Wuest, Germany
 * @version 20020626, 0.1
 */
public class CGrantAdminCommand implements IChatCommand {

    public boolean canExecute(IChatUser User) {
        return User.isAdmin();
    }

    public String commandIdentifier() {
        return "grantadmin";
    }

    public String execute(CChatSystem ChatSystem, IChatUser User, String Message) throws EChat {
        if (!(canExecute(User))) {
            throw new EChat("Only administrators can run this command.");
        }
        try {
            ChatSystem.getDataStore().setChatAdmin(Message.trim(), true);
        } catch (EDataStore Ex) {
            throw new EChat("Could not set administrator privileges to user '" + Message.trim() + "'. See nested exception for detailed information.", Ex);
        }
        return "Granted administrator privileges to '" + Message.trim() + "'";
    }

    public String longHelp() {
        return (shortHelp() + CChatSystem.CRLF + "Usage: grant <username>");
    }

    public String shortHelp() {
        return "Grant administrator privileges to user.";
    }
}
