package server.campaign.commands;

import java.util.StringTokenizer;
import server.campaign.CampaignMain;
import server.campaign.SPlayer;

/**
 * @author Vertigo
 * (11-feb-2004)
 */
public class MOTDCommand implements Command {

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
        SPlayer p = CampaignMain.cm.getPlayer(Username);
        if (p != null && p.getMyHouse().getMotd().trim().length() > 0) CampaignMain.cm.toUser("(Housemail)<h2><b>MOTD:</b></h2><br>" + p.getMyHouse().getMotd(), Username, true);
    }
}
