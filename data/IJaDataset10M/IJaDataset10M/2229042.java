package server.campaign.commands.admin;

import java.util.StringTokenizer;
import server.campaign.CampaignMain;
import server.campaign.SPlayer;
import server.campaign.SHouse;
import server.campaign.commands.Command;
import server.MWChatServer.auth.IAuthenticator;

public class ForcedDefectCommand implements Command {

    int accessLevel = IAuthenticator.ADMIN;

    String syntax = "Player Name#Faction Name";

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
        SPlayer p = null;
        SHouse h = null;
        boolean playerOnline = false;
        try {
            String name = command.nextToken();
            playerOnline = CampaignMain.cm.isLoggedIn(name);
            p = CampaignMain.cm.getPlayer(name);
            h = CampaignMain.cm.getHouseFromPartialString(command.nextToken(), null);
        } catch (Exception e) {
            CampaignMain.cm.toUser("AM:Improper command. Try: /c forceddefect#player#faction", Username, true);
            return;
        }
        if (p == null) {
            CampaignMain.cm.toUser("AM:Couldn't find a player with that name.", Username, true);
            return;
        }
        if (h == null) {
            CampaignMain.cm.toUser("AM:Couldn't find a faction with that name.", Username, true);
            return;
        }
        if (CampaignMain.cm.isSynchingBB()) {
            CampaignMain.cm.MySQL.removeUserFromHouseForum(p.getForumID(), p.getMyHouse().getForumID());
        }
        String clientVersion = p.getPlayerClientVersion();
        p.getMyHouse().removeLeader(p.getName());
        p.getMyHouse().removePlayer(p, false);
        p.setMyHouse(h);
        p.setSubFaction(h.getZeroLevelSubFaction());
        if (playerOnline) {
            CampaignMain.cm.getPlayer(p.getName());
            CampaignMain.cm.doLoginPlayer(p.getName());
        }
        if (CampaignMain.cm.isSynchingBB()) {
            CampaignMain.cm.MySQL.addUserToHouseForum(p.getForumID(), p.getMyHouse().getForumID());
        }
        CampaignMain.cm.toUser("AM:" + Username + " forced you to defect to " + h.getName(), p.getName(), true);
        CampaignMain.cm.toUser("AM:You forced " + p.getName() + " to defect to " + h.getName(), Username, true);
        CampaignMain.cm.doSendModMail("NOTE", Username + " forced " + p.getName() + " to defect to " + h.getName());
        p.setPlayerClientVersion(clientVersion);
    }
}
