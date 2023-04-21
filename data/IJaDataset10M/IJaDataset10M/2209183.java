package org.beepcore.beep.core;

/**
 * Interface StartChannelListener
 *
 * StartChannelListener is an interface specifying the methods that must
 * be implemented by any class that implements logic managing the start and
 * close events on a channel, as well as any other events that may eventually
 * be associated with profiles.
 *
 * @version $Revision: 1.4 $, $Date: 2003/11/18 14:03:08 $
 * @author Eric Dixon
 * @author Huston Franklin
 * @author Jay Kint
 * @author Scott Pead
 */
public interface StartChannelListener {

    /**
     * Callback that determines if a profile should be advertised or not.  The
     * {@link SessionTuningProperties} can be used to determine if
     * certain attributes are in effect before a profile is advertised,
     * such as encryption or authentication.
     * @param session to check other properties such as the credentials of
     * of the session
     * @see SessionTuningProperties for standard properties and their values
     */
    public boolean advertiseProfile(Session session) throws BEEPException;

    /**
     * Called when the underlying BEEP framework receives
     * a "start" element.
     *
     * @param channel A <code>Channel</code> object which represents a channel
     * in this <code>Session</code>.
     * @param data The content of the "profile" element selected for this
     * channel (may be <code>null</code>).
     * @param encoding specifies whether the content of the "profile" element
     * selected for this channel is represented as a base64-encoded string.
     * The <code>encoding</code> is only valid if <code>data</code> is not
     * <code>null</code>.
     *
     * @throws StartChannelException Throwing this exception will cause an
     * error to be returned to the BEEP peer requesting to start a channel.
     * The channel is then discarded.
     */
    public void startChannel(Channel channel, String encoding, String data) throws StartChannelException;

    /**
     * Called when the underlying BEEP framework receives
     * a "close" element.
     *
     *
     * @param channel <code>Channel</code> which received the close request.
     *
     * @throws CloseChannelException Throwing this exception will return an
     * error to the BEEP peer requesting the close.  The channel will remain
     * open.
     */
    public void closeChannel(Channel channel) throws CloseChannelException;
}
