package server.campaign.commands;

import java.util.LinkedList;
import java.util.StringTokenizer;
import common.CampaignData;
import common.Unit;
import common.campaign.pilot.Pilot;
import server.campaign.CampaignMain;
import server.campaign.SPlayer;

/**
 * Return a human readable string that describes the pilots
 * currently in a player's personal queues.
 */
public class DisplayPlayerPersonalPilotQueueCommand implements Command {

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
        boolean hasQueuedPilots = false;
        StringBuilder toReturn = new StringBuilder();
        for (int weightClass = Unit.LIGHT; weightClass <= Unit.ASSAULT; weightClass++) {
            LinkedList<Pilot> currList = p.getPersonalPilotQueue().getPilotQueue(Unit.MEK, weightClass);
            if (currList.size() != 0) {
                hasQueuedPilots = true;
                toReturn.append(Unit.getWeightClassDesc(weightClass) + ":<UL>");
                for (int i = 0; i < currList.size(); i++) {
                    Pilot currPil = currList.get(i);
                    toReturn.append("<LI>#" + currPil.getPilotId() + " " + currPil.getName() + " (" + currPil.getGunnery() + "/" + currPil.getPiloting());
                    String skills = currPil.getSkillString(true);
                    if (skills != null && skills.trim().length() != 0) {
                        toReturn.append(", ");
                        toReturn.append(skills);
                    }
                    toReturn.append(")");
                    if (currPil.getHits() > 0) toReturn.append(" Hits: " + currPil.getHits());
                    toReturn.append("</LI>");
                }
                toReturn.append("</UL>");
            }
        }
        if (hasQueuedPilots) toReturn.insert(0, "<u>Mek Pilots</u>:<br>");
        hasQueuedPilots = false;
        StringBuilder toReturnProtos = new StringBuilder();
        for (int weightClass = Unit.LIGHT; weightClass <= Unit.ASSAULT; weightClass++) {
            LinkedList<Pilot> currList = p.getPersonalPilotQueue().getPilotQueue(Unit.PROTOMEK, weightClass);
            if (currList.size() != 0) {
                hasQueuedPilots = true;
                toReturnProtos.append(Unit.getWeightClassDesc(weightClass) + ":<UL>");
                for (int i = 0; i < currList.size(); i++) {
                    Pilot currPil = currList.get(i);
                    toReturnProtos.append("<LI>#" + currPil.getPilotId() + " " + currPil.getName() + " (" + currPil.getGunnery());
                    String skills = currPil.getSkillString(true);
                    if (skills != null && skills.trim().length() != 0) {
                        toReturnProtos.append(", ");
                        toReturnProtos.append(skills);
                    }
                    toReturnProtos.append(")");
                    if (currPil.getHits() > 0) toReturnProtos.append(" Hits: " + currPil.getHits());
                    toReturnProtos.append("</LI>");
                }
                toReturnProtos.append("</UL>");
            }
        }
        if (hasQueuedPilots) {
            toReturnProtos.insert(0, "<u>ProtoMek Pilots</u>:<br>");
            toReturn.append(toReturnProtos);
        }
        hasQueuedPilots = false;
        StringBuilder toReturnAero = new StringBuilder();
        for (int weightClass = Unit.LIGHT; weightClass <= Unit.ASSAULT; weightClass++) {
            LinkedList<Pilot> currList = p.getPersonalPilotQueue().getPilotQueue(Unit.AERO, weightClass);
            if (currList.size() != 0) {
                hasQueuedPilots = true;
                toReturnAero.append(Unit.getWeightClassDesc(weightClass) + ":<UL>");
                for (int i = 0; i < currList.size(); i++) {
                    Pilot currPil = currList.get(i);
                    toReturnAero.append("<LI>#" + currPil.getPilotId() + " " + currPil.getName() + " (" + currPil.getGunnery() + "/" + currPil.getPiloting());
                    String skills = currPil.getSkillString(true);
                    if (skills != null && skills.trim().length() != 0) {
                        toReturnAero.append(", ");
                        toReturnAero.append(skills);
                    }
                    toReturnAero.append(")");
                    if (currPil.getHits() > 0) toReturnAero.append(" Hits: " + currPil.getHits());
                    toReturnAero.append("</LI>");
                }
                toReturnAero.append("</UL>");
            }
        }
        if (hasQueuedPilots) {
            toReturnAero.insert(0, "<u>Aero Pilots</u>:<br>");
            toReturn.append(toReturnAero);
        }
        if (toReturn.length() > 0) CampaignMain.cm.toUser("SM|" + toReturn.toString(), Username, false); else CampaignMain.cm.toUser("SM|You don't have any reserve pilots at the moment.", Username, false);
    }
}
