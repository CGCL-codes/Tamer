package server.campaign.commands.mod;

import java.util.StringTokenizer;
import server.campaign.CampaignMain;
import server.campaign.SPlayer;
import server.campaign.commands.Command;
import server.MWChatServer.auth.IAuthenticator;

public class GrantInfluenceCommand implements Command {

    int accessLevel = IAuthenticator.MODERATOR;

    String syntax = "Player Name#Amount";

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
        SPlayer p = CampaignMain.cm.getPlayer(command.nextToken());
        int amount = Integer.parseInt(command.nextToken());
        if (p != null) {
            p.addInfluence(amount);
            CampaignMain.cm.toUser("AM:You've been granted " + CampaignMain.cm.moneyOrFluMessage(false, true, amount, true) + " from " + Username, p.getName(), true);
            CampaignMain.cm.toUser("AM:You granted " + CampaignMain.cm.moneyOrFluMessage(false, true, amount, true) + " to " + p.getName(), Username, true);
            CampaignMain.cm.doSendModMail("NOTE", Username + " granted " + CampaignMain.cm.moneyOrFluMessage(false, true, amount, true) + " to " + p.getName());
        }
    }
}
