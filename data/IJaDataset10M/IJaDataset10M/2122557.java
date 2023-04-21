package org.speakmon.babble.events;

import org.speakmon.babble.UserInfo;

/**
 *
 * @author  speakmon
 */
public interface KickEventHandler {

    public void onKick(UserInfo user, String channel, String kickee, String reason);
}
