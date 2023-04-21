package org.opennms.web.event.filter;

import org.opennms.web.filter.EqualsFilter;
import org.opennms.web.filter.SQLType;

/** Encapsulates all interface filtering functionality. */
public class IfIndexFilter extends EqualsFilter<Integer> {

    public static final String TYPE = "ifindex";

    public IfIndexFilter(int ifIndex) {
        super(TYPE, SQLType.INT, "ifIndex", "ifIndex", ifIndex);
    }

    public String toString() {
        return ("<WebEventRepository.InterfaceFilter: " + this.getDescription() + ">");
    }

    public int getIfIndex() {
        return getValue();
    }

    public boolean equals(Object obj) {
        return (this.toString().equals(obj.toString()));
    }
}
