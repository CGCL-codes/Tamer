package rice.scribe.testing;

/**
 * @(#) DistTopicLog.java
 *
 * A log class corresponding to each topic to maintain the information about 
 * the sequence numbers received or published corresponding to this topic.
 *
 * @version $Id: DistTopicLog.java,v 1.1.1.1 2003/06/17 21:10:47 egs Exp $
 * @author Atul Singh
 * @author Animesh Nandi 
 */
public class DistTopicLog {

    private int lastSeqNumRecv = -1;

    private long lastRecvTime;

    private int seqNumToPublish = -1;

    private int count = 1;

    private boolean unsubscribed = false;

    public DistTopicLog() {
    }

    public int getLastSeqNumRecv() {
        return lastSeqNumRecv;
    }

    public void setLastSeqNumRecv(int seqno) {
        lastSeqNumRecv = seqno;
        return;
    }

    public int getSeqNumToPublish() {
        return seqNumToPublish;
    }

    public void setSeqNumToPublish(int seqno) {
        seqNumToPublish = seqno;
        return;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int value) {
        count = value;
        return;
    }

    public boolean getUnsubscribed() {
        return unsubscribed;
    }

    public void setUnsubscribed(boolean value) {
        unsubscribed = value;
        return;
    }

    public long getLastRecvTime() {
        return lastRecvTime;
    }

    public void setLastRecvTime(long value) {
        lastRecvTime = value;
        return;
    }
}
