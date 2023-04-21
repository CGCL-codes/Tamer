package org.speakmon.babble.events;

/**
 *
 * @author  speakmon
 */
public interface AwayEventHandler {

    public void onAway(String nick, String awayMessage);
}
