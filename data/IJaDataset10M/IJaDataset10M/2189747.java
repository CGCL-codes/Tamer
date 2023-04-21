package net.java.slee.resource.diameter.gx;

/**
 * GxSessionActivity.java.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:carl-magnus.bjorkell@emblacom.com"> Carl-Magnus Björkell </a>
 */
public interface GxSessionActivity {

    /**
     * Provides session state information. CC session must conform to CC FSM as described in <a href="link http://rfc.net/rfc4006.html#s7">section 7 of rfc4006
     * </a>.
     *
     * @return instance of {@link CreditControlSessionState}
     */
    GxSessionState getState();

    /**
     * Return a message factory to be used to create concrete implementations of
     * credit control messages.
     *
     * @return a message factory instance.
     */
    GxMessageFactory getGxMessageFactory();

    /**
     * Returns the session ID of the credit control session, which uniquely
     * identifies the session.
     *
     * @return the session ID
     */
    String getSessionId();
}
