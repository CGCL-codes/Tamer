package rtjdds.rtps.transport;

import rtjdds.rtps.messages.elements.LocatorList;
import rtjdds.rtps.messages.elements.LocatorUDPv4;
import rtjdds.rtps.types.Locator_t;

public class LocatorHelper {

    public Sender getSender(Locator_t locator) {
        return null;
    }

    public Sender getSender(LocatorUDPv4 locator) {
        return null;
    }

    public Sender[] getSender(LocatorList locator) {
        return null;
    }

    public Receiver getReceiver(Locator_t locator) {
        return null;
    }

    public Receiver getReceiver(LocatorUDPv4 locator) {
        return null;
    }

    public Receiver[] getReceiver(LocatorList locator) {
        return null;
    }
}
