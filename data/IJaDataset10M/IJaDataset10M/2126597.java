package org.opennms.web.event.filter;

import org.opennms.netmgt.model.OnmsSeverity;
import org.opennms.web.filter.EqualsFilter;
import org.opennms.web.filter.SQLType;

/** Encapsulates severity filtering functionality. */
public class SeverityFilter extends EqualsFilter<Integer> {

    public static final String TYPE = "severity";

    public SeverityFilter(int severity) {
        super(TYPE, SQLType.INT, "EVENTSEVERITY", "eventSeverity", severity);
    }

    public SeverityFilter(OnmsSeverity severity) {
        this(severity.getId());
    }

    public String getTextDescription() {
        return (TYPE + "=" + OnmsSeverity.get(getSeverity()).getLabel());
    }

    public String toString() {
        return ("<WebEventRepository.SeverityFilter: " + getDescription() + ">");
    }

    public int getSeverity() {
        return getValue();
    }

    public boolean equals(Object obj) {
        return (this.toString().equals(obj.toString()));
    }
}
