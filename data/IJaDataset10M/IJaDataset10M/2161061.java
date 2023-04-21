package server.campaign.commands.admin;

import java.util.StringTokenizer;
import server.campaign.commands.Command;
import server.campaign.CampaignMain;
import server.campaign.SHouse;
import server.MWChatServer.auth.IAuthenticator;

public class AdminSaveFactionConfigsCommand implements Command {

    int accessLevel = IAuthenticator.ADMIN;

    String syntax = "Faction Name";

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
        String faction = "";
        try {
            faction = command.nextToken();
        } catch (Exception ex) {
            CampaignMain.cm.toUser("Invalid syntax. Try: AdminSaveFactionConfigs#faction", Username, true);
            return;
        }
        SHouse h = CampaignMain.cm.getHouseFromPartialString(faction, Username);
        if (h == null) return;
        h.populateUnitLimits();
        h.populateBMLimits();
        if (CampaignMain.cm.isUsingMySQL()) h.saveConfigFileToDB(); else h.saveConfigFile();
        h.setUsedMekBayMultiplier(h.getFloatConfig("UsedPurchaseCostMulti"));
        CampaignMain.cm.toUser("AM:Status saved!", Username, true);
        CampaignMain.cm.doSendModMail("NOTE", Username + " has saved " + faction + "'s configs");
    }
}
