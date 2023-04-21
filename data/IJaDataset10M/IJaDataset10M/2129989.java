package server.campaign.commands.leader;

import java.util.StringTokenizer;
import common.SubFaction;
import server.campaign.SArmy;
import server.campaign.SPlayer;
import server.campaign.CampaignMain;
import server.campaign.commands.Command;

public class PromotePlayerCommand implements Command {

    int accessLevel = CampaignMain.cm.getIntegerConfig("factionLeaderLevel");

    public int getExecutionLevel() {
        return accessLevel;
    }

    public void setExecutionLevel(int i) {
        accessLevel = i;
    }

    String syntax = "";

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
        SPlayer leader = CampaignMain.cm.getPlayer(Username);
        SPlayer grunt = null;
        String subFactionName;
        SubFaction subFaction = null;
        boolean isMod = CampaignMain.cm.getServer().isModerator(Username);
        try {
            grunt = CampaignMain.cm.getPlayer(command.nextToken());
            subFactionName = command.nextToken();
        } catch (Exception ex) {
            CampaignMain.cm.toUser("AM:Invalid Syntax: /promoteplayer Player#NewSubFactionName", Username);
            return;
        }
        if (grunt == null) {
            CampaignMain.cm.toUser("AM:Unknown Player", Username);
            return;
        }
        if (!grunt.canBePromoted() && !isMod) {
            CampaignMain.cm.toUser("AM:" + grunt.getName() + " cannot be promoted at this time.", Username);
            return;
        }
        if (!grunt.getMyHouse().getName().equalsIgnoreCase(leader.getMyHouse().getName()) && !CampaignMain.cm.getServer().isModerator(Username)) {
            CampaignMain.cm.toUser("AM:You can only promote players that within your same faction!", Username);
            return;
        }
        subFaction = grunt.getMyHouse().getSubFactionList().get(subFactionName);
        if (subFaction == null) {
            CampaignMain.cm.toUser("AM:That SubFaction does not exist for faction " + grunt.getMyHouse().getName() + ".", Username);
            return;
        }
        int minELO = Integer.parseInt(subFaction.getConfig("MinELO"));
        int minEXP = Integer.parseInt(subFaction.getConfig("MinExp"));
        if (grunt.getSubFactionAccess() > Integer.parseInt(subFaction.getConfig("AccessLevel"))) {
            CampaignMain.cm.toUser("AM:You cannot promote " + grunt.getName() + " to a subfaction with a lower access level, try demoting.", Username);
            return;
        }
        if (grunt.getExperience() < minEXP || grunt.getRating() < minELO) {
            CampaignMain.cm.toUser("AM:Sorry but " + grunt.getName() + " is not skilled enough to join that SubFaction.", Username);
            return;
        }
        grunt.setSubFaction(subFactionName);
        CampaignMain.cm.toUser("PL|SSN|" + subFactionName, grunt.toString(), false);
        CampaignMain.cm.doSendToAllOnlinePlayers("PI|FT|" + grunt.getName() + "|" + grunt.getFluffText(), false);
        CampaignMain.cm.doSendToAllOnlinePlayers("PI|SSN|" + grunt.getName() + "|" + subFactionName, false);
        CampaignMain.cm.toUser("HS|CA|0", grunt.getName(), false);
        CampaignMain.cm.toUser(grunt.getMyHouse().getCompleteStatus(), grunt.getName(), false);
        for (SArmy army : grunt.getArmies()) {
            CampaignMain.cm.getOpsManager().checkOperations(army, true);
        }
        CampaignMain.cm.toUser("AM:Congratulations you have been promoted to SubFaction " + subFactionName + ".", grunt.getName());
        CampaignMain.cm.doSendHouseMail(grunt.getMyHouse(), "NOTE", grunt.getName() + " has been promoted to subfaction " + subFactionName + " by " + leader.getName() + "!");
        CampaignMain.cm.toUser("AM:You've promoted " + grunt.getName() + " to SubFaction " + subFactionName + ".", Username);
        CampaignMain.cm.doSendModMail("NOTE", Username + " promoted " + grunt.getName() + " to SubFaction " + subFactionName + ".");
    }
}
