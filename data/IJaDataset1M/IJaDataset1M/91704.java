package org.opennms.web.alarm.filter;

import org.opennms.web.filter.NotEqualOrNullFilter;
import org.opennms.web.filter.SQLType;

/** Encapsulates all interface filtering functionality. */
public class NegativeInterfaceFilter extends NotEqualOrNullFilter<String> {

    public static final String TYPE = "interfacenot";

    public NegativeInterfaceFilter(String ipAddress) {
        super(TYPE, SQLType.STRING, "IPADDR", "ipAddr", ipAddress);
    }

    public String getTextDescription() {
        return ("interface is not " + getValue());
    }

    public String toString() {
        return ("<AlarmFactory.NegativeInterfaceFilter: " + this.getDescription() + ">");
    }

    public String getIpAddress() {
        return getValue();
    }

    public boolean equals(Object obj) {
        return (this.toString().equals(obj.toString()));
    }
}
