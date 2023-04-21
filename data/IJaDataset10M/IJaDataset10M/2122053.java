package org.asteriskjava.manager.event;

/**
 * A QueueEntryEvent is triggered in response to a QueueStatusAction and
 * contains information about an entry in a queue.<p>
 * It is implemented in <code>apps/app_queue.c</code>
 * 
 * @see org.asteriskjava.manager.action.QueueStatusAction
 * @author srt
 * @version $Id: QueueEntryEvent.java 1360 2009-09-04 01:08:57Z srt $
 */
public class QueueEntryEvent extends ResponseEvent {

    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 3419114730595151514L;

    private String queue;

    private Integer position;

    private String uniqueId;

    private String channel;

    private String callerId;

    private String callerIdName;

    private Long wait;

    /**
     * @param source
     */
    public QueueEntryEvent(Object source) {
        super(source);
    }

    /**
     * Returns the name of the queue that contains this entry.
     */
    public String getQueue() {
        return queue;
    }

    /**
     * Sets the name of the queue that contains this entry.
     */
    public void setQueue(String queue) {
        this.queue = queue;
    }

    /**
     * Returns the position of this entry in the queue.
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * Sets the position of this entry in the queue.
     */
    public void setPosition(Integer position) {
        this.position = position;
    }

    /**
     * Returns the name of the channel of this entry.
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Returns the unique id of the channel of this entry.<p>
     * Available since Asterisk 1.6.
     *
     * @return the unique id of the channel of this entry.
     * @since 1.0.0
     */
    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    /**
     * Sets the name of the channel of this entry.
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * Returns the the Caller*ID number of this entry.
     * 
     * @return the the Caller*ID number of this entry.
     */
    public String getCallerId() {
        return callerId;
    }

    /**
     * Sets the the Caller*ID number of this entry.
     * 
     * @param callerId the the Caller*ID number of this entry.
     */
    public void setCallerId(String callerId) {
        this.callerId = callerId;
    }

    /**
     * Returns the Caller*ID name of this entry.
     * 
     * @return the Caller*ID name of this entry.
     * @since 0.2
     */
    public String getCallerIdName() {
        return callerIdName;
    }

    /**
     * Sets the Caller*ID name of this entry.
     * 
     * @param callerIdName the Caller*ID name of this entry.
     * @since 0.2
     */
    public void setCallerIdName(String callerIdName) {
        this.callerIdName = callerIdName;
    }

    /**
     * Returns the number of seconds this entry has spent in the queue.
     */
    public Long getWait() {
        return wait;
    }

    /**
     * Sets the number of seconds this entry has spent in the queue.
     */
    public void setWait(Long wait) {
        this.wait = wait;
    }
}
