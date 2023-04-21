package facade;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import account.Client;
import services.Auction;
import services.AuctionState;
import services.Bid;

public class BidObserver implements Observer {

    Auction auction;

    public void update(Observable obj, Object arg) {
        if (arg instanceof Bid) {
            Auction auction = (Auction) obj;
            Bid bid = (Bid) arg;
            String auctionName = auction.getAuctionName();
            System.out.println("Auction " + auctionName + " has a new price " + bid.getAmount() + " by " + bid.getClient().getEmail());
        }
    }
}
