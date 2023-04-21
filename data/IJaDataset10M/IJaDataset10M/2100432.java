package org.asteriskjava.manager.event;

/**
 * Response to an OriginateAction.
 * 
 * @see org.asteriskjava.manager.action.OriginateAction
 * @author srt
 * @version $Id: OriginateResponseEvent.java 1108 2008-08-16 11:22:50Z srt $
 */
public class OriginateResponseEvent extends ResponseEvent {

    /**
     * Serial version identifier.
     */
    private static final long serialVersionUID = 910724860608259687L;

    private String response;

    private String channel;

    private String context;

    private String exten;

    private String uniqueId;

    private Integer reason;

    private String callerIdNum;

    private String callerIdName;

    /**
     * @param source
     */
    public OriginateResponseEvent(Object source) {
        super(source);
    }

    /**
     * Returns the result of the corresponding Originate action.
     * 
     * @return "Success" or "Failure"
     */
    public String getResponse() {
        return response;
    }

    /**
     * Sets the result of the corresponding Originate action.
     * 
     * @param response "Success" or "Failure"
     */
    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return "Success".equalsIgnoreCase(response);
    }

    /**
     * Returns the name of the channel to connect to the outgoing call.
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Sets the name of the channel to connect to the outgoing call.
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * Returns the name of the context of the extension to connect to.
     */
    public String getContext() {
        return context;
    }

    /**
     * Sets the name of the context of the extension to connect to.
     */
    public void setContext(String context) {
        this.context = context;
    }

    /**
     * Returns the the extension to connect to.
     */
    public String getExten() {
        return exten;
    }

    /**
     * Sets the the extension to connect to.
     */
    public void setExten(String exten) {
        this.exten = exten;
    }

    public Integer getReason() {
        return reason;
    }

    public void setReason(Integer reason) {
        this.reason = reason;
    }

    /**
     * Returns the unique id of the originated channel.
     * 
     * @return the unique id of the originated channel or "&lt;null&gt;" if none
     *         is available.
     */
    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    /**
     * Returns the Caller*ID Number of the originated channel.
     * <p>
     * Available sind Asterisk 1.4.
     * 
     * @return the Caller*ID Number of the originated channel or <code>null</code> if none was set.
     * @since 0.3
     */
    public String getCallerIdNum() {
        return callerIdNum;
    }

    public void setCallerIdNum(String callerId) {
        this.callerIdNum = callerId;
    }

    public void setCallerId(String callerId) {
        if (this.callerIdNum == null) {
            this.callerIdNum = callerId;
        }
    }

    /**
     * Returns the Caller*ID Name of the originated channel.
     * <p>
     * Available sind Asterisk 1.4.
     * 
     * @return the Caller*ID Name of the originated channel or <code>null</code> if none was set.
     */
    public String getCallerIdName() {
        return callerIdName;
    }

    public void setCallerIdName(String callerIdName) {
        this.callerIdName = callerIdName;
    }
}
