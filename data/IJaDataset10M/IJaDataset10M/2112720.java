package server.campaign.commands.admin;

import java.util.StringTokenizer;
import common.AdvancedTerrain;
import common.Continent;
import common.CampaignData;
import server.campaign.commands.Command;
import server.campaign.CampaignMain;
import server.campaign.SPlanet;
import server.MWChatServer.auth.IAuthenticator;

public class AdminCreateTerrainCommand implements Command {

    int accessLevel = IAuthenticator.ADMIN;

    String syntax = "Planet Name#TerrainType#Chance";

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
        try {
            SPlanet p = CampaignMain.cm.getPlanetFromPartialString(command.nextToken(), Username);
            String terraintype = command.nextToken();
            int chance = Integer.parseInt(command.nextToken());
            if (p == null) {
                CampaignMain.cm.toUser("Planet not found:", Username, true);
                return;
            }
            Continent cont = new Continent(chance, CampaignMain.cm.getData().getTerrainByName(terraintype));
            p.getEnvironments().add(cont);
            if (new Boolean(CampaignMain.cm.getConfig("UseStaticMaps")).booleanValue()) {
                AdvancedTerrain aTerrain = new AdvancedTerrain();
                p.getAdvancedTerrain().put(new Integer(cont.getEnvironment().getId()), aTerrain);
            }
            p.updated();
            CampaignMain.cm.toUser("Terrain added to " + p.getName() + "(" + terraintype + ").", Username, true);
            CampaignMain.cm.doSendModMail("NOTE", Username + " added terrain to planet " + p.getName() + "(" + terraintype + ").");
        } catch (Exception ex) {
            CampaignData.mwlog.errLog(ex);
        }
    }
}
