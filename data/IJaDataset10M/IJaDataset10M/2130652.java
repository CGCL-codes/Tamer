package org.mobicents.servlet.sip.restcomm.callmanager;

import java.util.Set;

public interface ConferenceCenter {

    public Conference getConference(final String name) throws ConferenceCenterException;

    public Set<String> getConferenceNames();

    public void removeConference(final String name);
}
