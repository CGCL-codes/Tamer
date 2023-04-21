package org.speakmon.babble.dcc;

/**
 *
 * @author  speakmon
 */
public final class DccFileSessionManager {

    /** Holds value of property timeout. */
    private long timeout;

    /** Creates a new instance of DccFileSessionManager */
    private DccFileSessionManager() {
    }

    protected void addSession(DccFileSession session) {
    }

    protected void removeSession(DccFileSession session) {
    }

    protected void checkSessions(Object state) {
    }

    protected boolean containsSession(String sessionID) {
    }

    protected DccFileSession lookupSession(String sessionID) {
    }

    public static DccFileSessionManager getDefaultInstance() {
    }

    /** Getter for property timeout.
     * @return Value of property timeout.
     *
     */
    public long getTimeoutPeriod() {
        return this.timeout;
    }

    /** Setter for property timeout.
     * @param timeout New value of property timeout.
     *
     */
    public void setTimeoutPeriod(long timeout) {
        this.timeout = timeout;
    }
}
