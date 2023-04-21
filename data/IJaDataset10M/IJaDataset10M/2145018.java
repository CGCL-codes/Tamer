package server.campaign.commands.admin;

import java.util.StringTokenizer;
import server.campaign.commands.Command;
import server.campaign.CampaignMain;
import server.MWChatServer.auth.IAuthenticator;

public class CyclopsOptimizeCommand implements Command {

    int accessLevel = IAuthenticator.ADMIN;

    String syntax = "";

    public int getExecutionLevel() {
        return accessLevel;
    }

    public void setExecutionLevel(int i) {
        accessLevel = i;
    }

    public String getSyntax() {
        return syntax;
    }

    public void process(StringTokenizer command, String Username) {
        int userLevel = CampaignMain.cm.getServer().getUserLevel(Username);
        if (userLevel < getExecutionLevel()) {
            CampaignMain.cm.toUser("AM:Insufficient access level for command. Level: " + userLevel + ". Required: " + accessLevel + ".", Username, true);
            return;
        }
        CampaignMain.cm.getMWCC().optimize();
    }
}
