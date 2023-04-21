package org.openymsg.network;

import org.openymsg.network.event.WaitListener;
import org.openymsg.roster.Roster;

/**
 * @author Giancarlo Frison - Nimbuzz B.V. <giancarlo@nimbuzz.com>
 * 
 */
public abstract class YahooTestAbstract<T extends Roster<U>, U extends YahooUser> {

    protected static final String CHATMESSAGE = "CHATMESSAGE";

    protected Session<T, U> sess1;

    protected Session<T, U> sess2;

    protected WaitListener listener1;

    protected WaitListener listener2;

    protected TstSessions<T, U> testSession;

    protected abstract U createUser(String userId, String group);
}
