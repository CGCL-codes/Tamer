package command.commandAuction;

import command.AuctionHouse;
import exception.SecurityPolicyViolationException;

public class PlaceBid extends command.Command {

    AuctionHouse auctionHouse = AuctionHouse.getInstance();

    public PlaceBid(AuctionHouse auctionHouse) {
        this.auctionHouse = auctionHouse;
    }

    public String execute(String canal, String params) throws SecurityPolicyViolationException {
        String[] _params = params.split(" ", 2);
        if (_params.length != 2) {
            return canal + "!NOK - The command arguments are not correct, please try again.";
        }
        String auctionName = _params[0];
        double bid = Double.valueOf(_params[1].trim()).doubleValue();
        String placeBidResponse = auctionHouse.placeBid(auctionName, canal, bid);
        if (placeBidResponse.equals("OK")) {
            return canal + "!OK - You have successfully placed a bid of " + bid + " on the auction \"" + auctionName + "\".";
        } else {
            return canal + "!NOK - Sorry, the attempt to place a bid of " + bid + ", on the auction \"" + auctionName + "\", has failed. The command have returned an error: " + placeBidResponse + ".";
        }
    }
}
