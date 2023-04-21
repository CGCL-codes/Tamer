package interconnect;

import util.IntVector;

public class VerificationElem {

    private int routerID;

    private IntVector routerChannelNumber;

    public VerificationElem() {
        this(-1);
    }

    public VerificationElem(int routerID) {
        this.routerID = routerID;
        this.routerChannelNumber = new IntVector();
    }

    public boolean verifyChannel(int routerChannelNumber) {
        return this.routerChannelNumber.enqueue(routerChannelNumber);
    }

    public int getRouterID() {
        return this.routerID;
    }

    public void destroy() {
        this.routerChannelNumber.destroy();
    }
}
