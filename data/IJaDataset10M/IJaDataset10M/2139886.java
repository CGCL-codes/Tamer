package server.campaign.commands.admin;

import java.util.HashMap;
import java.util.StringTokenizer;
import server.campaign.commands.Command;
import server.campaign.CampaignMain;
import server.campaign.SHouse;
import server.campaign.SPlanet;
import server.MWChatServer.auth.IAuthenticator;

public class AdminChangePlanetOwnerCommand implements Command {

    int accessLevel = IAuthenticator.ADMIN;

    String syntax = "planet#newfaction";

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
        SPlanet p = null;
        SHouse h = null;
        try {
            p = CampaignMain.cm.getPlanetFromPartialString(command.nextToken(), Username);
            h = CampaignMain.cm.getHouseFromPartialString(command.nextToken(), Username);
        } catch (Exception e) {
            CampaignMain.cm.toUser("Improper command. Try: /c adminchangeplanetowner#planet#newfaction", Username, true);
            return;
        }
        if (p == null) {
            CampaignMain.cm.toUser("Could not find a matching planet.", Username, true);
            return;
        }
        if (h == null) {
            CampaignMain.cm.toUser("Could not find a matching faction.", Username, true);
            return;
        }
        p.setOwner(p.getOwner(), h, true);
        HashMap<Integer, Integer> flu = new HashMap<Integer, Integer>();
        flu.put(h.getId(), p.getConquestPoints());
        p.getInfluence().setInfluence(flu);
        p.updated();
        CampaignMain.cm.toUser("You gave ownership of " + p.getName() + " to " + h.getName() + ".", Username, true);
        CampaignMain.cm.doSendModMail("NOTE", Username + " gave ownership of " + p.getName() + " to " + h.getName() + ".");
    }
}
