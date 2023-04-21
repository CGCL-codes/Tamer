package server.campaign.commands;

import java.util.StringTokenizer;
import server.campaign.CampaignMain;
import server.campaign.SPlayer;
import server.campaign.market2.MarketListing;

public class RecallCommand implements Command {

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
        if (p == null) {
            CampaignMain.cm.toUser("AM:Null SPlayer while recalling unit. Report immediately!", Username, true);
            return;
        }
        int auctionID = -1;
        try {
            auctionID = Integer.parseInt(command.nextToken());
        } catch (Exception e) {
            CampaignMain.cm.toUser("AM:Improper format. Try: /c recall#AuctionID", Username, true);
            return;
        }
        MarketListing auction = CampaignMain.cm.getMarket().getListingByID(auctionID);
        if (auction == null) {
            CampaignMain.cm.toUser("AM:There is no auction with ID#" + auctionID + ".", Username, true);
            return;
        }
        if (!auction.getSellerName().equalsIgnoreCase(Username)) {
            CampaignMain.cm.toUser("AM:Only the selling player may terminate an auction.", Username, true);
            return;
        }
        if (auction.getAllBids().size() > 0) {
            CampaignMain.cm.toUser("AM:There are bids on the " + auction.getListedModelName() + ". Sale may not be stopped.", Username, true);
            return;
        }
        CampaignMain.cm.getMarket().removeListing(auctionID);
        CampaignMain.cm.toUser("The " + auction.getListedModelName() + " is no longer for sale on the Market.", Username, true);
        CampaignMain.cm.doSendHouseMail(p.getMyHouse(), "NOTE", p.getName() + " cancelled an auction [" + auction.getListedModelName() + "].");
    }
}
