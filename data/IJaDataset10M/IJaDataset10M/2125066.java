package org.asteriskjava.manager.event;

/**
 * A HoldedCallEvent is triggered when a channel is put on hold.<p>
 * It is implemented in <code>res/res_features.c</code>
 * 
 * @author srt
 * @version $Id: HoldedCallEvent.java 967 2008-02-03 07:28:32Z srt $
 */
public class HoldedCallEvent extends ManagerEvent {

    /**
     * Serializable version identifier
     */
    private static final long serialVersionUID = 7384290590382334480L;

    private String uniqueId1;

    private String uniqueId2;

    private String channel1;

    private String channel2;

    /**
     * @param source
     */
    public HoldedCallEvent(Object source) {
        super(source);
    }

    /**
     * Returns the unique id of the channel that put the other channel on hold.
     */
    public String getUniqueId1() {
        return uniqueId1;
    }

    /**
     * Sets the unique id of the channel that put the other channel on hold.
     */
    public void setUniqueId1(String uniqueId1) {
        this.uniqueId1 = uniqueId1;
    }

    /**
     * Returns the unique id of the channel that has been put on hold.
     */
    public String getUniqueId2() {
        return uniqueId2;
    }

    /**
     * Sets the unique id of the channel that has been put on hold.
     */
    public void setUniqueId2(String uniqueId2) {
        this.uniqueId2 = uniqueId2;
    }

    /**
     * Returns the name of the channel that put the other channel on hold.
     */
    public String getChannel1() {
        return channel1;
    }

    /**
     * Sets the name of the channel that put the other channel on hold.
     */
    public void setChannel1(String channel1) {
        this.channel1 = channel1;
    }

    /**
     * Returns the name of the channel that has been put on hold.
     */
    public String getChannel2() {
        return channel2;
    }

    /**
     * Sets the name of the channel that has been put on hold.
     */
    public void setChannel2(String channel2) {
        this.channel2 = channel2;
    }
}
