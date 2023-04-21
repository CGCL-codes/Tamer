package net.sf.babble.events;

import java.util.EventListener;

/**
 * Receives <code>PublicMessageEvent</code>s.
 * @version $Id: PublicMessageEventHandler.java 262 2007-02-06 06:55:29 +0000 (Tue, 06 Feb 2007) speakmon $
 * @author Ben Speakmon
 */
public interface PublicMessageEventHandler extends EventListener {

    public void onPublic(PublicMessageEvent publicMessageEvent);
}
