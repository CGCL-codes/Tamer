package server.campaign.commands;

import java.util.StringTokenizer;
import server.campaign.CampaignMain;
import server.campaign.SHouse;
import server.campaign.SPlayer;
import server.campaign.SUnit;
import common.Unit;
import common.util.StringUtils;

public class TransferUnitCommand implements Command {

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
        String targetPlayer;
        int unitid;
        try {
            targetPlayer = (String) command.nextElement();
            unitid = Integer.parseInt((String) command.nextElement());
        } catch (Exception e) {
            CampaignMain.cm.toUser("AM:Improper format. Try: /c transferunit#TargetPlayer#UnitID", Username, true);
            return;
        }
        SPlayer targetplayer = CampaignMain.cm.getPlayer(targetPlayer);
        SPlayer player = CampaignMain.cm.getPlayer(Username);
        SHouse house = player.getMyHouse();
        boolean usesTechs = CampaignMain.cm.getBooleanConfig("UseTechnicians") && !CampaignMain.cm.isUsingAdvanceRepair();
        if (house.isNewbieHouse()) {
            CampaignMain.cm.toUser("AM:Players in SOL may not transfer units.", Username, true);
            return;
        }
        if (player.mayAcquireWelfareUnits()) {
            CampaignMain.cm.toUser("AM:You may not transfer any of your units while you are on welfare.", Username, true);
            return;
        }
        if (targetplayer == null) {
            CampaignMain.cm.toUser("AM:Could not find target player.", Username, true);
            return;
        }
        SUnit m = player.getUnit(unitid);
        if (m == null) {
            CampaignMain.cm.toUser("AM:You do not own Unit #" + unitid + ".", Username, true);
            return;
        }
        if (player.getAmountOfTimesUnitExistsInArmies(unitid) > 0 && player.getDutyStatus() >= SPlayer.STATUS_ACTIVE) {
            CampaignMain.cm.toUser("AM:You may not tranfer units which are in active armies.", Username, true);
            return;
        }
        if (!targetplayer.getMyHouse().equals(player.getMyHouse()) && !targetplayer.getMyHouse().getHouseFightingFor(targetplayer).equals(player.getMyHouse())) {
            CampaignMain.cm.toUser("AM:" + targetplayer.getName() + " is not in your faction. You cannot send him units.", Username, true);
            return;
        } else if (targetplayer.getFreeBays() < SUnit.getHangarSpaceRequired(m, targetplayer.getMyHouse()) && !usesTechs) {
            CampaignMain.cm.toUser("AM:" + targetplayer.getName() + " has no room for that unit.", Username, true);
            return;
        } else if (!targetplayer.getMyHouse().isLoggedIntoFaction(targetplayer.getName())) {
            CampaignMain.cm.toUser("AM:" + targetplayer.getName() + " is not logged in. You may only transfer to players who are online.", Username, true);
            return;
        } else if (Boolean.parseBoolean(house.getConfig("IPCheck"))) {
            if (CampaignMain.cm.getServer().getIP(player.getName()).toString().equals(CampaignMain.cm.getServer().getIP(targetplayer.getName()).toString())) {
                CampaignMain.cm.toUser("AM:" + targetplayer.getName() + " has the same IP as you do. You can't send him units.", Username, true);
                return;
            }
        }
        if (!targetplayer.hasRoomForUnit(m.getType(), m.getWeightclass())) {
            CampaignMain.cm.toUser("AM:Sorry, " + targetplayer.getName() + " already has the maximum number of " + Unit.getWeightClassDesc(m.getWeightclass()) + " " + Unit.getTypeClassDesc(m.getType()) + "s", Username);
            return;
        }
        float transferPayment = Float.parseFloat(house.getConfig("TransferPayment"));
        int senderCost = Math.round(transferPayment * player.getCurrentTechPayment());
        int receiverCost = Math.round(transferPayment * targetplayer.getCurrentTechPayment());
        String modName = m.getModelName();
        if (!Boolean.parseBoolean(house.getConfig("SenderPaysOnTransfer"))) {
            senderCost = 0;
        }
        if (senderCost > player.getMoney()) {
            CampaignMain.cm.toUser("AM:You tried to send " + StringUtils.aOrAn(modName, true) + " to " + targetPlayer + ", but you cannot afford the transfer payment (" + CampaignMain.cm.moneyOrFluMessage(true, true, senderCost) + ").", Username, true);
            return;
        }
        if (!Boolean.parseBoolean(house.getConfig("ReceiverPaysOnTransfer"))) {
            receiverCost = 0;
        }
        if (receiverCost > targetplayer.getMoney()) {
            CampaignMain.cm.toUser("AM:You tried to send " + StringUtils.aOrAn(modName, true) + " to " + targetPlayer + ", but he cannot afford the transfer payment. Transfer aborted.", Username, true);
            CampaignMain.cm.toUser("AM:" + Username + " tried to send you " + StringUtils.aOrAn(modName, true) + "; however, you cannot afford the tech payment the transfer would trigger (" + CampaignMain.cm.moneyOrFluMessage(true, true, receiverCost) + ").", targetPlayer, true);
            return;
        }
        boolean confirmedSend = false;
        if (command.hasMoreElements() && command.nextToken().equals("CONFIRM")) confirmedSend = true;
        int scrapLevel = Integer.parseInt(house.getConfig("TransferScrapLevel"));
        if (m.getMaintainanceLevel() <= scrapLevel && !confirmedSend) {
            CampaignMain.cm.toUser("AM:The unit you are trying to tranfer is not well maintained." + " Equipment which is already in a poor state of repair may be" + " irreparably damaged in transit.<br>" + " <a href=\"MEKWARS/c transferunit#" + targetplayer.getName() + "#" + unitid + "#CONFIRM\">Click here to send the unit anyway</a>", Username, true);
            return;
        } else if (m.getMaintainanceLevel() <= scrapLevel && confirmedSend) {
            int rnd = CampaignMain.cm.getRandomNumber(100) + 1;
            if (rnd > m.getMaintainanceLevel()) {
                int mechscrapprice = 0;
                if (house.getIntegerConfig("ScrapsAllowed") > 0) {
                    mechscrapprice = (int) (player.getMyHouse().getPriceForUnit(m.getWeightclass(), m.getType()) * Double.parseDouble(house.getConfig("ScrapCostMultiplier")));
                    if (player.getMoney() < mechscrapprice) mechscrapprice = player.getMoney();
                    player.addMoney(-mechscrapprice);
                }
                int flutolose = player.getInfluence();
                player.addInfluence(-flutolose);
                String toSend = "AM:The " + modName + " didn't survive transit intact. HQ is displeased (";
                if (mechscrapprice > 0) toSend += CampaignMain.cm.moneyOrFluMessage(true, false, -mechscrapprice, true) + ", ";
                toSend += CampaignMain.cm.moneyOrFluMessage(false, false, -flutolose, true) + ").";
                CampaignMain.cm.toUser(toSend, player.getName(), true);
                CampaignMain.cm.toUser("AM:" + player.getName() + " tried to send you a " + modName + ", but it didn't survive the trip.", targetplayer.getName(), true);
                player.removeUnit(m.getId(), true);
                return;
            }
        }
        String toSender = "AM:You transferred the " + modName + " to " + targetplayer.getName() + ".";
        if (senderCost > 0) {
            toSender += " Paid " + CampaignMain.cm.moneyOrFluMessage(true, false, senderCost) + " to your technicians.";
            player.addMoney(-senderCost);
        }
        String toReceiver = "AM:" + Username + " sent you " + StringUtils.aOrAn(modName, true) + ".";
        if (receiverCost > 0) {
            toReceiver += "Paid " + CampaignMain.cm.moneyOrFluMessage(true, false, receiverCost) + " to your technicians.";
            targetplayer.addMoney(-receiverCost);
        }
        CampaignMain.cm.toUser(toSender, Username, true);
        CampaignMain.cm.toUser(toReceiver, targetPlayer, true);
        player.removeUnit(m.getId(), true);
        targetplayer.addUnit(m, true);
        if (CampaignMain.cm.isUsingMySQL()) {
            player.toDB();
            targetplayer.toDB();
        }
        if (player.mayAcquireWelfareUnits()) CampaignMain.cm.doSendModMail("NOTE", Username + " has used the Transfer Unit Command to send himself into welfare.");
    }
}
