package org.opennms.web.alarm.filter;

import org.opennms.web.filter.SubstringFilter;

/** Encapsulates all node filtering functionality. */
public class NodeNameLikeFilter extends SubstringFilter {

    public static final String TYPE = "nodenamelike";

    public NodeNameLikeFilter(String substring) {
        super(TYPE, "NODELABEL", "node.label", substring);
    }

    @Override
    public String getSQLTemplate() {
        return " ALARMID IN (SELECT ALARMID FROM ALARMS JOIN NODE ON ALARMS.NODEID=NODE.NODEID WHERE NODE.NODELABEL ILIKE %s) ";
    }

    public String getTextDescription() {
        return ("node name containing \"" + getValue() + "\"");
    }

    public String toString() {
        return ("<AlarmFactory.NodeNameContainingFilter: " + this.getDescription() + ">");
    }

    public String getSubstring() {
        return getValue();
    }

    public boolean equals(Object obj) {
        return (this.toString().equals(obj.toString()));
    }
}
