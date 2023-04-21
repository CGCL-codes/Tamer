package command.commandAuction;

import command.AuctionHouse;

public class GetStartDate extends command.Command {

    AuctionHouse auctionHouse = AuctionHouse.getInstance();

    public String execute(String canal, String auctionName) {
        if (auctionHouse.getAuctionEndDate(auctionName) != null) {
            return canal + "!OK - the start date of the auction \"" + auctionName + "\": " + String.valueOf(auctionHouse.getAuctionStartDate(auctionName));
        }
        return canal + "!NOK - couldn't get the start date";
    }
}
