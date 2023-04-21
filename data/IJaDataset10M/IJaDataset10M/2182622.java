package server.campaign.commands;

import java.util.StringTokenizer;
import server.campaign.CampaignMain;

public class HouseMailCommand implements Command {

    int accessLevel = 0;

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
        if (accessLevel != 0) {
            int userLevel = CampaignMain.cm.getServer().getUserLevel(Username);
            if (userLevel < getExecutionLevel()) {
                CampaignMain.cm.toUser("AM:Insufficient access level for command. Level: " + userLevel + ". Required: " + accessLevel + ".", Username, true);
                return;
            }
        }
        if (command.countTokens() < 1) return;
        String toSend = command.nextToken();
        while (command.hasMoreElements()) toSend += "#" + command.nextToken();
        if (toSend.trim().length() == 0) return;
        CampaignMain.cm.doSendHouseMail(CampaignMain.cm.getPlayer(Username).getMyHouse(), Username, toSend);
    }
}
