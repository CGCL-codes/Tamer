package net.java.sip.communicator.service.protocol.event;

import java.util.*;

/**
 *
 * @author Emil Ivov
 */
public interface PresenceStatusListener extends EventListener {

    public void contactPresenceStatusChanged();
}
