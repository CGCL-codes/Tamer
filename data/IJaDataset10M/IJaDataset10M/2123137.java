package com.quikj.ace.messages.vo.talk;

public class RTPMessage implements TalkMessageInterface {

    private static final long serialVersionUID = -2833426684637704389L;

    private MediaElements media = null;

    private long sessionId = -1;

    private boolean parse = false;

    private String from = null;

    public RTPMessage() {
    }

    /**
	 * Getter for property from.
	 * 
	 * @return Value of property from.
	 */
    public java.lang.String getFrom() {
        return from;
    }

    public MediaElements getMediaElements() {
        return media;
    }

    public long getSessionId() {
        return sessionId;
    }

    /**
	 * Getter for property parse.
	 * 
	 * @return Value of property parse.
	 */
    public boolean isParse() {
        return parse;
    }

    /**
	 * Setter for property from.
	 * 
	 * @param from
	 *            New value of property from.
	 */
    public void setFrom(java.lang.String from) {
        this.from = from;
    }

    public void setMediaElements(MediaElements media) {
        this.media = media;
    }

    /**
	 * Setter for property parse.
	 * 
	 * @param parse
	 *            New value of property parse.
	 */
    public void setParse(boolean parse) {
        this.parse = parse;
    }

    public void setSessionId(long session) {
        sessionId = session;
    }
}
