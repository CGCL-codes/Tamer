package org.yawlfoundation.yawl.logging;

import org.yawlfoundation.yawl.util.XNode;
import java.util.Comparator;

/**
 * Allows XES event nodes within a trace to be sorted based on timestamp
 *
 * Author: Michael Adams
 * Creation Date: 12/08/2011
 */
public class XESTimestampComparator implements Comparator<XNode> {

    public int compare(XNode node1, XNode node2) {
        if (node1 == null) return -1;
        if (node2 == null) return 1;
        if (!node1.getName().equals("event")) return -1;
        if (!node2.getName().equals("event")) return 1;
        String stamp1 = getTimestamp(node1);
        String stamp2 = getTimestamp(node2);
        return stamp1.compareTo(stamp2);
    }

    private String getTimestamp(XNode event) {
        for (XNode date : event.getChildren("date")) {
            if (date.getAttributeValue("key").equals("time:timestamp")) {
                return date.getAttributeValue("value");
            }
        }
        return "0";
    }
}
