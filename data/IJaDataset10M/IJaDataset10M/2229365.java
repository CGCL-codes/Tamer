package server.campaign.commands;

import java.util.Iterator;
import java.util.StringTokenizer;
import common.Planet;
import server.campaign.CampaignMain;
import server.campaign.SPlayer;
import server.campaign.SHouse;
import server.campaign.SPlanet;

public class RangeCommand implements Command {

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
        if (command.hasMoreElements()) {
            int range = Integer.parseInt(command.nextToken());
            SHouse targetfaction = null;
            if (command.hasMoreElements()) {
                String target = command.nextToken();
                targetfaction = CampaignMain.cm.getHouseFromPartialString(target, Username);
                if (targetfaction == null) {
                    CampaignMain.cm.toUser("AM:Could not find faction " + target + ". Try again?", Username, true);
                    return;
                }
            }
            boolean facWorldsOnly = false;
            if (command.hasMoreElements()) {
                try {
                    facWorldsOnly = Boolean.parseBoolean(command.nextToken());
                } catch (Exception e) {
                    CampaignMain.cm.toUser("AM:Improper format. Try: /c range#Distance#Faction#true to return only production worlds.", Username, true);
                    return;
                }
            }
            SPlayer player = CampaignMain.cm.getPlayer(Username);
            SHouse h = player.getMyHouse();
            String result = "These planets are within " + range + " light-years ";
            if (targetfaction != null) {
                if (facWorldsOnly) result += "and " + targetfaction.getName() + " production facilities are present "; else result += "and " + targetfaction.getName() + " military assets are present ";
            }
            result += ":<br>";
            Iterator<Planet> e = CampaignMain.cm.getData().getAllPlanets().iterator();
            while (e.hasNext()) {
                SPlanet p = (SPlanet) e.next();
                if (h.getDistanceTo(p, CampaignMain.cm.getPlayer(Username)) <= range) {
                    if (targetfaction == null) {
                        SHouse owner = p.getOwner();
                        if (facWorldsOnly && owner.equals(targetfaction) && p.getFactoryCount() > 0) {
                            result += "<font color=\"" + p.getOwner().getHouseColor() + "\">" + p.getShortDescription(true) + "</font>" + "<br>";
                        } else {
                            if (owner != null) result += "<font color=\"" + p.getOwner().getHouseColor() + "\">" + p.getShortDescription(true) + "</font>" + "<br>"; else result += p.getSmallStatus(true) + "<br>";
                        }
                    } else {
                        if (p.getInfluence().getInfluence(targetfaction.getId()) > 0) {
                            SHouse owner = p.getOwner();
                            if (owner != null) result += "<font color=\"" + p.getOwner().getHouseColor() + "\">" + p.getShortDescription(true) + "</font>" + "<br>"; else result += p.getSmallStatus(true) + "<br>";
                        }
                    }
                }
            }
            result = result.substring(0, result.length() - 2);
            CampaignMain.cm.toUser("SM|" + result, Username, false);
        }
    }
}
