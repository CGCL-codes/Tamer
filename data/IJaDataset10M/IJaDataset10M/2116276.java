package ecskernel.coordinate.dummy.auction;

import java.util.Date;
import ecskernel.coordinate.AGovernor;
import ecskernel.coordinate.auction.messages.IBiddingCompleteMsg;
import ecskernel.coordinate.auction.objects.IGood;

public class DummyBiddingCompleteMsg implements IBiddingCompleteMsg {

    IGood good;

    AGovernor sender;

    Date timestamp;

    public DummyBiddingCompleteMsg(IGood good, AGovernor sender) {
        this.good = good;
        this.sender = sender;
        this.timestamp = new Date();
    }

    public IGood getGood() {
        return good;
    }

    public AGovernor getSender() {
        return sender;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }
}
