package server.campaign.commands;

import java.util.StringTokenizer;
import server.campaign.CampaignMain;
import server.campaign.SPlayer;
import server.util.MWPasswd;

public class SetMyLogoCommand implements Command {

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
        SPlayer player = CampaignMain.cm.getPlayer(Username);
        if (!command.hasMoreTokens()) {
            player.setMyLogo(player.getMyHouse().getLogo());
        } else {
            String newLogo = command.nextToken();
            if (MWPasswd.getRecord(Username) == null) {
                CampaignMain.cm.toUser("AM:You cannot set a logo until you registered your Name. Please use the File Menu -> Register Nickname to do so!", Username, true);
                return;
            }
            if (newLogo.trim().length() < 1) player.setMyLogo(player.getMyHouse().getLogo()); else player.setMyLogo(newLogo);
        }
        CampaignMain.cm.toUser("PL|SUL|" + player.getMyLogo(), Username, false);
        CampaignMain.cm.toUser("AM:You've set your Logo to " + player.getMyLogo(), Username, true);
        CampaignMain.cm.toUser("AM:It'll look like this: <img height=\"150\" width=\"150\" src =\"" + player.getMyLogo() + "\">", Username, true);
    }
}
