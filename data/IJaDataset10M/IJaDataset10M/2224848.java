package ngs.architecture.hybrid.racs;

import java.util.LinkedList;

/**
 * Records the QoS statistics about a peer. Used to determine if a peer
 * is meeting their QoS requirements.
 */
public class PeerQoSStatistics {

    /**
	 * The maximum number of consecutive messages that may be dropped and
	 * still maintain the QoS requirements.
	 */
    private int qosMaxConsecutive;

    /**
	 * The length of the history used to calculate the loss rate for QoS.
	 */
    private int qosHistoryLength;

    /**
	 * The minimum percentage of messages that must be received.
	 */
    private double qosMinPercentage;

    /**
	 * How many consecutive updates has this peer dropped.
	 */
    private int consecutive = 0;

    /**
	 * The history of updates for this peer. 1 means update received. 0 Means update failed.
	 */
    private LinkedList<Integer> history = new LinkedList<Integer>();

    /**
	 * Percentage of the history received.
	 */
    private double percentage = 100.0;

    /**
	 * Sum of all the updates in the history.
	 */
    private int sum = 0;

    /**
	 * Has a message been received since the last update?
	 */
    private int received = 0;

    /**
	 * @param qosMaxConsecutive The maximum number of consecutive updates that a peer may drop.
	 * @param qosHistoryLength The length of the history for peers.
	 * @param qosMinPercentage The percentage of updates that must arrive from peers.
	 */
    public PeerQoSStatistics(final int qosMaxConsecutive, final int qosHistoryLength, final double qosMinPercentage) throws Exception {
        if (qosMaxConsecutive < 1) {
            throw new Exception("Error! Cannot have less than 1 consecutive drop");
        }
        if (qosHistoryLength < 1) {
            throw new Exception("Error! Cannot have a history less than 1");
        }
        if (qosMinPercentage < 0.0) {
            System.out.println("Warning: minimum percentage of less than 0.0 is illogical");
        }
        if (qosMinPercentage > 100.0) {
            throw new Exception("Error min percentage cannot exceed 100.0");
        }
        this.qosMaxConsecutive = qosMaxConsecutive;
        this.qosHistoryLength = qosHistoryLength;
        this.qosMinPercentage = qosMinPercentage;
    }

    /**
	 * Calculates if this peer is meeting it's QoS requirements, and prepares for the next round.
	 *
	 * @returns true if this peer is meeting its QoS, else false.
	 */
    public boolean meetingQoS() {
        if (history.size() == qosHistoryLength) {
            sum = sum - history.poll().intValue();
        }
        sum = sum + received;
        history.add(new Integer(received));
        percentage = ((double) sum) / ((double) history.size()) * 100.0;
        if (received == 1) {
            consecutive = 0;
        } else {
            consecutive++;
        }
        boolean meetQoS = ((percentage >= qosMinPercentage) && (consecutive <= qosMaxConsecutive));
        received = 0;
        return meetQoS;
    }

    /**
	 * Called when a message is received from this node.
	 */
    public void setReceived() {
        received = 1;
    }
}
