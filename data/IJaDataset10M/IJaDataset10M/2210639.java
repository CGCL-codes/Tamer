package test;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import services.Auction;
import services.Bid;
import command.AuctionHouse;
import account.Client;

public class BidTest {

    Client client = new Client("Nice", "Seller", "nice.seller@somewhere.com", "pass", "somewhere");

    Auction auction = new Auction(AuctionHouse.getInstance(), client, "orange", "It_is_to_eat", 1, 4, 2);

    Bid bid = new Bid(auction, client, 14);

    @Test
    public void testGetClient() {
        String clientEmail = bid.getClient().getEmail();
        assertEquals(clientEmail, "nice.seller@somewhere.com");
    }

    @Test
    public void testSetClient() {
        Client client1 = new Client("Nice", "Jolie", "nice.jolie@somewhere.com", "pass", "somewhere");
        bid.setClient(client1);
        String clientEmail = bid.getClient().getEmail();
        assertEquals(clientEmail, "nice.jolie@somewhere.com");
    }

    @Test
    public void testGetAuction() {
        String auctionName = bid.getAuction().getAuctionName();
        assertEquals(auctionName, "orange");
    }

    @Test
    public void testSetAuction() {
        Auction auction1 = new Auction(AuctionHouse.getInstance(), client, "apple", "It_is_to_eat", 1, 4, 2);
        bid.setAuction(auction1);
        assertEquals(bid.getAuction().getAuctionName(), "apple");
    }

    @Test
    public void testGetAmount() {
        assertEquals(bid.getAmount(), 14);
    }

    @Test
    public void testSetAmount() {
        bid.setAmount(15);
        assertEquals(bid.getAmount(), 15);
    }
}
