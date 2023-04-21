package rbe;

import java.util.Random;

public class EBB extends EB {

    private int minBID;

    private int maxBID;

    private int minAID;

    private int maxAID;

    private int minTID;

    private int maxTID;

    private int minDelta;

    private int maxDelta;

    public Random rand = new Random();

    public EBB(RBE rbe, int[][] prob, EBTransition[][] trans, int max, String name, int minBID, int maxBID, int minAID, int maxAID, int minTID, int maxTID, int minDelta, int maxDelta) {
        super(rbe, prob, trans, max, name);
        this.minBID = minBID;
        this.maxBID = maxBID;
        this.minAID = minAID;
        this.maxAID = maxAID;
        this.minTID = minTID;
        this.maxTID = maxTID;
        this.minDelta = minDelta;
        this.maxDelta = maxDelta;
    }

    public int nextBID() {
        return (nextInt(maxBID - minBID + 1) + minBID);
    }

    public int nextTID() {
        return (nextInt(maxTID - minTID + 1) + minTID);
    }

    public int nextAID() {
        return (nextInt(maxAID - minAID + 1) + minAID);
    }

    public int nextDelta() {
        return (nextInt(maxDelta - minDelta + 1) + minDelta);
    }

    public int nextInt(int range) {
        int i = Math.abs(rand.nextInt());
        return (i % (range));
    }
}
