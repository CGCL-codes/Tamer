package command.commandAuction;

import command.AuctionHouse;
import command.Command;

public class GetMinBid extends command.Command {

    AuctionHouse auctionHouse = AuctionHouse.getInstance();

    public String execute(String canal, String auctionName) {
        if (auctionHouse.getAuctionMinBid(auctionName) != null) {
            return canal + "!OK - minimal bid of the auction \"" + auctionName + "\": " + auctionHouse.getAuctionMinBid(auctionName);
        }
        return canal + "!NOK - couldn't get the end date";
    }
}
