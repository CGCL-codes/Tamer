package ecskernel.coordinate.dummy.auction;

import java.util.ArrayList;
import ecskernel.coordinate.IAgent;
import ecskernel.coordinate.IMessage;
import ecskernel.coordinate.IReasoner;
import ecskernel.coordinate.auction.AAuctioneer;
import ecskernel.coordinate.auction.ABidder;
import ecskernel.coordinate.auction.messages.IAllocationMsg;
import ecskernel.coordinate.auction.objects.IGood;
import ecskernel.coordinate.tasks.ATask;

public class DummyBidder extends ABidder {

    public DummyBidder(IAgent agent) {
        super(agent);
    }

    public void receiveAllocation(IAllocationMsg msg) {
        System.out.println("DummyBidder received " + msg.getType());
    }

    public void sendBid(IGood good, AAuctioneer auctioneer) {
        DummyBid bid = new DummyBid(good, this, 10);
        DummyBidMessage msg = new DummyBidMessage(bid, this);
        Main.INSTITUTION.getCommunicationNetwork().direct(msg, auctioneer, this);
    }

    public void sendBiddingComplete(IGood good, AAuctioneer auctioneer) {
        DummyBiddingCompleteMsg msg = new DummyBiddingCompleteMsg(good, this);
        Main.INSTITUTION.getCommunicationNetwork().direct(msg, auctioneer, this);
    }

    public IAgent getAgent() {
        return null;
    }

    public IReasoner getReasoner() {
        return null;
    }

    public void receiveMessage(IMessage message) {
        System.out.println("Bidder received message " + message.getType());
    }

    public ArrayList<ATask> getTasks() {
        return null;
    }
}
