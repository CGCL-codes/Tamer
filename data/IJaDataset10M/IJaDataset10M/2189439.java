package org.speakmon.babble.events;

import org.speakmon.babble.dcc.DccUserInfo;

/**
 *
 * @author  speakmon
 */
public interface DccGetRequestEventHandler {

    public void onDccGetRequest(DccUserInfo dccUserInfo, String filename, boolean turbo);
}
