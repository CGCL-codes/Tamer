package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

/**
 * Filter for packets where the "from" field exactly matches a specified JID. If the specified
 * address is a bare JID then the filter will match any address whose bare JID matches the
 * specified JID. But if the specified address is a full JID then the filter will only match
 * if the sender of the packet matches the specified resource.
 *
 * @author Gaston Dombiak
 */
public class FromMatchesFilter implements PacketFilter {

    private String address;

    /**
     * Flag that indicates if the checking will be done against bare JID addresses or full JIDs.
     */
    private boolean matchBareJID = false;

    /**
     * Creates a "from" filter using the "from" field part. If the specified address is a bare JID
     * then the filter will match any address whose bare JID matches the specified JID. But if the
     * specified address is a full JID then the filter will only match if the sender of the packet
     * matches the specified resource.
     *
     * @param address the from field value the packet must match. Could be a full or bare JID.
     */
    public FromMatchesFilter(String address) {
        if (address == null) {
            throw new IllegalArgumentException("Parameter cannot be null.");
        }
        this.address = address.toLowerCase();
        matchBareJID = "".equals(StringUtils.parseResource(address));
    }

    public boolean accept(Packet packet) {
        if (packet.getFrom() == null) {
            return false;
        } else if (matchBareJID) {
            return packet.getFrom().toLowerCase().startsWith(address);
        } else {
            return address.equals(packet.getFrom().toLowerCase());
        }
    }

    public String toString() {
        return "FromMatchesFilter: " + address;
    }
}
