package org.opennms.web.event.filter;

import java.sql.SQLException;
import org.opennms.web.element.NetworkElementFactory;
import org.opennms.web.filter.NotEqualOrNullFilter;
import org.opennms.web.filter.SQLType;

/** Encapsulates all node filtering functionality. */
public class NegativeNodeFilter extends NotEqualOrNullFilter<Integer> {

    public static final String TYPE = "nodenot";

    public NegativeNodeFilter(int nodeId) {
        super(TYPE, SQLType.INT, "EVENTS.NODEID", "node.id", nodeId);
    }

    public String getTextDescription() {
        String nodeName = Integer.toString(getValue());
        try {
            nodeName = NetworkElementFactory.getNodeLabel(getValue());
        } catch (SQLException e) {
        }
        return ("node is not " + nodeName);
    }

    public String toString() {
        return ("<WebEventRepository.NegativeNodeFilter: " + this.getDescription() + ">");
    }

    public int getNodeId() {
        return getValue();
    }

    public boolean equals(Object obj) {
        return (this.toString().equals(obj.toString()));
    }
}
