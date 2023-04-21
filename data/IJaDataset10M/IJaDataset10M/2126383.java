package server.campaign.commands.admin;

import java.util.StringTokenizer;
import common.Unit;
import server.campaign.commands.Command;
import server.campaign.SHouse;
import server.campaign.CampaignMain;
import server.MWChatServer.auth.IAuthenticator;

public class AdminGrantComponentsCommand implements Command {

    int accessLevel = IAuthenticator.ADMIN;

    String syntax = "faction#type#weight#numcomponents";

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
        SHouse h = null;
        String typestring = "";
        String weightstring = "";
        int comps = -1;
        int unitType = Unit.MEK;
        int unitWeight = Unit.LIGHT;
        try {
            h = (SHouse) CampaignMain.cm.getData().getHouseByName(command.nextToken());
            typestring = command.nextToken();
            weightstring = command.nextToken();
            comps = Integer.parseInt(command.nextToken());
        } catch (Exception e) {
            CampaignMain.cm.toUser("Improper command. Try: /c admingrancomponents#faction#type#weight#numcomponents", Username, true);
            return;
        }
        if (h == null) {
            CampaignMain.cm.toUser("Couldn't find a faction with that name.", Username, true);
            return;
        }
        try {
            unitType = Integer.parseInt(typestring);
        } catch (Exception ex) {
            unitType = Unit.getTypeIDForName(typestring);
        }
        try {
            unitWeight = Integer.parseInt(weightstring);
        } catch (Exception ex) {
            unitWeight = Unit.getWeightIDForName(weightstring.toUpperCase());
        }
        h.addPP(unitWeight, unitType, comps, true);
        CampaignMain.cm.toUser("You granted " + comps + " Comps to " + h.getName(), Username, true);
        CampaignMain.cm.doSendModMail("NOTE", Username + " granted " + comps + " Comps to " + h.getName());
    }
}
