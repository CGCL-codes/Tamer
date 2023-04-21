package org.saforum.safapp.phase.messages;

import java.util.Date;

public abstract class PhaseMessage {

    public static final int INVALID_SID = -1;

    public static final int MAX_DATA_LENGTH = 1308;

    private int sid;

    private PhaseMessageType type;

    private Date received;

    protected PhaseMessage(int sid, PhaseMessageType type) {
        this.sid = sid;
        this.type = type;
    }

    /**
	 * Returns the sensor Id of the message.
	 */
    public int getSid() {
        return sid;
    }

    /**
	 * Returns the type of the message.
	 * @return
	 */
    public PhaseMessageType getType() {
        return type;
    }

    public void setTime(long sec, long usec) {
        Date d = new Date();
        d.setTime(sec * 1000 + usec / 1000);
        received = d;
    }

    public Date getReceiveTime() {
        return received;
    }

    public Date getReceivedDate() {
        return received;
    }
}
