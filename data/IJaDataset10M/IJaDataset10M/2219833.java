package org.dbe.composer.wfengine.bpel.impl.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.xml.namespace.QName;
import org.dbe.composer.wfengine.bpel.impl.queue.SdlMessageReceiver;
import org.dbe.composer.wfengine.util.SdlUtil;

/**
 * Provides filtering capability for the in-memory queue manager
 * queued receivers listing.
 */
public class MessageReceiverFilterManager {

    /** Default comparator for sorting the list of message receivers. */
    private static final SdlMessageReceiverComparator SORTER = new SdlMessageReceiverComparator();

    /**
     * Returns a filtered list of message receivers.
     * @param aFilter Determines the selection criteria.
     * @param aMessageReceivers All of the available receivers on the queue.
     * @return The filtered results.
     */
    public static MessageReceiverListResult filter(MessageReceiverFilter aFilter, List aMessageReceivers) {
        List matches = new ArrayList();
        int totalRows = 0;
        if (aMessageReceivers != null && !aMessageReceivers.isEmpty()) {
            SdlMessageReceiver[] recs = (SdlMessageReceiver[]) aMessageReceivers.toArray(new SdlMessageReceiver[aMessageReceivers.size()]);
            for (int i = 0; i < recs.length; i++) {
                SdlMessageReceiver receiver = recs[i];
                if (accepts(aFilter, receiver)) {
                    totalRows++;
                    if (aFilter.isWithinRange(totalRows)) {
                        matches.add(receiver);
                    }
                }
            }
        }
        if (!matches.isEmpty()) {
            sort(matches);
        }
        return new MessageReceiverListResult(totalRows, matches);
    }

    /**
     * Returns true if the message receiver meets the filter criteria.
     * @param aFilter The selection criteria.
     * @param aReceiver A queued message receiver.
     */
    protected static boolean accepts(MessageReceiverFilter aFilter, SdlMessageReceiver aReceiver) {
        return isPIDMatch(aFilter, aReceiver) && isPartnerLinkNameMatch(aFilter, aReceiver) && isPortTypeMatch(aFilter, aReceiver) && isOperationMatch(aFilter, aReceiver);
    }

    /**
     * Returns true if there is no process id specified in the filter or
     * if the process id in the filter mathes the receive's process id.
     * @param aFilter The selection criteria.
     * @param aReceiver A queued message receiver.
     */
    static boolean isPIDMatch(MessageReceiverFilter aFilter, SdlMessageReceiver aReceiver) {
        if (!aFilter.isNullProcessId()) {
            return aFilter.getProcessId() == aReceiver.getProcessId();
        } else {
            return true;
        }
    }

    /**
     * Returns true if there is no partner link type name specified in the filter or
     * if the partner link type name in the filter mathes the receive's
     * partner link type name.
     * @param aFilter The selection criteria.
     * @param aReceiver A queued message receiver.
     */
    protected static boolean isPartnerLinkNameMatch(MessageReceiverFilter aFilter, SdlMessageReceiver aReceiver) {
        if (!SdlUtil.isNullOrEmpty(aFilter.getPartnerLinkName())) {
            return aFilter.getPartnerLinkName().equals(aReceiver.getPartnerLinkName());
        } else {
            return true;
        }
    }

    /**
     * Returns true if there is no port type qname specified in the filter or
     * if the port type qname in the filter mathes the receive's port type qname.
     * @param aFilter The selection criteria.
     * @param aReceiver A queued message receiver.
     */
    protected static boolean isPortTypeMatch(MessageReceiverFilter aFilter, SdlMessageReceiver aReceiver) {
        if (aFilter.getPortType() != null) {
            return isNamespaceUriMatch(aFilter.getPortType(), aReceiver) && isLocalPartMatch(aFilter.getPortType(), aReceiver);
        } else {
            return true;
        }
    }

    /**
     * Returns true if there is no namespace specified in the qname or
     * if the qname namespace uri in the filter mathes the receive's
     * port type qname namespace uri.
     * @param aPortType The port type qname.
     * @param aReceiver A queued message receiver.
     */
    static boolean isNamespaceUriMatch(QName aPortType, SdlMessageReceiver aReceiver) {
        if (!SdlUtil.isNullOrEmpty(aPortType.getNamespaceURI())) {
            return aPortType.getNamespaceURI().equals(aReceiver.getPortType().getNamespaceURI());
        } else {
            return true;
        }
    }

    /**
     * Returns true if the local part of the qname matches
     * the receive's port type qname local part.
     * @param aQName The port type qname.
     * @param aReceiver A queued message receiver.
     */
    static boolean isLocalPartMatch(QName aQName, SdlMessageReceiver aReceiver) {
        return aQName.getLocalPart().equals(aReceiver.getPortType().getLocalPart());
    }

    /**
     * Returns true if there is no operation name specified in the filter or
     * if the operation in the filter mathes the receive's operation.
     * @param aFilter The selection criteria.
     * @param aReceiver A queued message receiver.
     */
    static boolean isOperationMatch(MessageReceiverFilter aFilter, SdlMessageReceiver aReceiver) {
        if (!SdlUtil.isNullOrEmpty(aFilter.getOperation())) {
            return aFilter.getOperation().equals(aReceiver.getOperation());
        } else {
            return true;
        }
    }

    /**
     * Sorts the matching queued receiver.
     * @param aMatches
     */
    protected static void sort(List aMatches) {
        Collections.sort(aMatches, SORTER);
    }

    /**
     * Comparator impl.  Does string compares on partnerlink type name,
     * portType, operation and int compares on process id.
     */
    protected static class SdlMessageReceiverComparator implements Comparator {

        /**
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Object a1, Object a2) {
            SdlMessageReceiver receiverOne = (SdlMessageReceiver) a1;
            SdlMessageReceiver receiverTwo = (SdlMessageReceiver) a2;
            int match = receiverOne.getPartnerLinkName().compareTo(receiverTwo.getPartnerLinkName());
            if (match == 0) {
                match = receiverOne.getPortType().toString().compareTo(receiverTwo.getPortType().toString());
                if (match == 0) {
                    match = receiverOne.getOperation().compareTo(receiverTwo.getOperation());
                    if (match == 0) {
                        match = (int) (receiverOne.getProcessId() - receiverTwo.getProcessId());
                    }
                }
            }
            return match;
        }
    }
}
