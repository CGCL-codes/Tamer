package org.opennms.web.outage.filter;

import java.sql.SQLException;
import org.opennms.web.element.NetworkElementFactory;
import org.opennms.web.filter.EqualsFilter;
import org.opennms.web.filter.SQLType;

/** Encapsulates all service filtering functionality. */
public class ServiceFilter extends EqualsFilter<Integer> {

    public static final String TYPE = "service";

    public ServiceFilter(int serviceId) {
        super(TYPE, SQLType.INT, "OUTAGES.SERVICEID", "serviceType.id", serviceId);
    }

    public String getTextDescription() {
        int serviceId = getServiceId();
        String serviceName = Integer.toString(serviceId);
        try {
            serviceName = NetworkElementFactory.getServiceNameFromId(serviceId);
        } catch (SQLException e) {
            throw new IllegalStateException("Could not get the service name for id " + serviceId);
        }
        return (TYPE + " is " + serviceName);
    }

    public String toString() {
        return ("<ServiceFilter: " + this.getDescription() + ">");
    }

    public int getServiceId() {
        return getValue();
    }

    public boolean equals(Object obj) {
        return (this.toString().equals(obj.toString()));
    }
}
