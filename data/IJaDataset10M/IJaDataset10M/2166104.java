package server.campaign.commands.admin;

import java.util.StringTokenizer;
import server.campaign.CampaignMain;
import server.campaign.commands.Command;
import server.MWChatServer.auth.IAuthenticator;

public class AdminUnlockCampaignCommand implements Command {

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
        if (new Boolean(CampaignMain.cm.getConfig("CampaignLock")).booleanValue() != true) {
            CampaignMain.cm.toUser("AM:Campaign is already unlocked.", Username, true);
            return;
        }
        CampaignMain.cm.getConfig().setProperty("CampaignLock", "false");
        CampaignMain.cm.doSendToAllOnlinePlayers("AM:" + Username + " unlocked the campaign!", true);
        CampaignMain.cm.toUser("AM:You unlocked the campaign. Players may now activate.", Username, true);
        CampaignMain.cm.doSendModMail("NOTE", Username + " unlocked the campaign");
    }
}
