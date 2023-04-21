package command.commandAuction;

import command.AuctionHouse;
import command.Command;

public class GetDescription extends command.Command {

    AuctionHouse auctionHouse = AuctionHouse.getInstance();

    public String execute(String canal, String auctionName) {
        if (auctionHouse.getAuctionDesc(auctionName) != null) {
            return canal + "!OK - description of the auction \"" + auctionName + "\": " + auctionHouse.getAuctionDesc(auctionName);
        }
        return canal + "!NOK - couldn't get the description";
    }
}
