package org.openymsg.legacy.network.event;

import org.openymsg.legacy.network.YahooConference;

public class SessionConferenceLogonEvent extends AbstractSessionConferenceEvent {

    private static final long serialVersionUID = 6550109967442109240L;

    public SessionConferenceLogonEvent(Object o, String to, String from, YahooConference conference) {
        super(o, to, from, null, conference);
    }
}
