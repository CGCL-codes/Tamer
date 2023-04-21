package org.echarts.servlet.sip.features.click2DialFlow4;

/**
 * Class representing a request to set up a call between two specified parties.
 *
 */
public class Click2DialFlow4Bean {

    static final String rcsid = "$Name:  $ $Id: Click2DialBean.java,v 1.2 2007/12/12 20:18:40 cheung Exp $";

    private String party1 = null;

    private String fromUserForParty1 = null;

    private String party2 = null;

    private String fromUserForParty2 = null;

    private int timeoutSec = -1;

    private boolean earlyMediaReinvite = true;

    private String uid = null;

    /**
	 * @return first party address (SIP URI)
	 */
    public String getParty1() {
        return party1;
    }

    /** Sets the address of the desired first party (String representation of SIP URI)
	 * @param party1 SIP URI
	 */
    public void setParty1(final String party1) {
        this.party1 = party1;
    }

    /**
	 * @return second party address (SIP URI)
	 */
    public String getParty2() {
        return party2;
    }

    /** Sets the address of the desired second party (String representation of SIP URI)
	 * @param party2 SIP URI
	 */
    public void setParty2(final String party2) {
        this.party2 = party2;
    }

    /**
	 * @return the fromUserForParty1
	 */
    public String getFromUserForParty1() {
        return fromUserForParty1;
    }

    /**
	 * @param fromUserForParty2 the fromUserForParty2 to set
	 */
    public void setFromUserForParty1(String fromUserForParty1) {
        this.fromUserForParty1 = fromUserForParty1;
    }

    /**
	 * @return the fromUserForParty2
	 */
    public String getFromUserForParty2() {
        return fromUserForParty2;
    }

    /**
	 * @param fromUserForParty2 the fromUserForParty2 to set
	 */
    public void setFromUserForParty2(String fromUserForParty2) {
        this.fromUserForParty2 = fromUserForParty2;
    }

    /**
	 * @return timeout value for first party to answer in seconds
	 */
    public int getTimeoutSec() {
        return timeoutSec;
    }

    /** Sets an optional timeout in seconds for first party to answer.
	 * If no answer in the specified number of seconds, then the call
	 * will abort.  Default: -1 (no timeout)
	 * @param timeoutSec timeout value in seconds (-1 for no timeout)
	 */
    public void setTimeoutSec(int timeoutSec) {
        this.timeoutSec = timeoutSec;
    }

    /**
	 * @return current setting of earlyMediaReinvite
	 */
    public boolean getEarlyMediaReinvite() {
        return earlyMediaReinvite;
    }

    /** Specifies whether or not the first party should receive a re-INVITE if the
	 * party returns an early media response.  Default: true.
	 * @param earlyMediaReinvite
	 */
    public void setEarlyMediaReinvite(final boolean earlyMediaReinvite) {
        this.earlyMediaReinvite = earlyMediaReinvite;
    }

    /** Set value of X-ECHARTS-UID header in INVITE to first
		party. Default: null.
		@param uid X-ECHARTS-UID header value - should be unique
	*/
    public void setUid(final String uid) {
        this.uid = uid;
    }

    /** 
		@return value of uid
	*/
    public String getUid() {
        return uid;
    }

    public String toString() {
        return "party1 = " + party1 + "\n" + "fromUserForParty1 = " + fromUserForParty1 + "\n" + "party2 = " + party2 + "\n" + "fromUserForParty2 = " + fromUserForParty2 + "\n" + "party2 = " + party2 + "\n" + "timeoutSec = " + timeoutSec + "\n" + "earlyMediaReinvite = " + earlyMediaReinvite + "\n" + "uid = " + uid + "\n";
    }
}
