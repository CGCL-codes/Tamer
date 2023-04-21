package org.dhcp4java.server.filter;

import java.util.LinkedList;
import java.util.List;
import org.dhcp4java.DHCPPacket;

/**
 * 
 * <p>By convention the result is false except if at least one sub-filter is true.
 * This means that the result is false if the sub-filter list is empty.
 * 
 * @author Stephan Hadinger
 *
 */
public final class OrFilter implements RequestFilter {

    private final LinkedList<RequestFilter> filters;

    public OrFilter() {
        this.filters = new LinkedList<RequestFilter>();
    }

    public OrFilter(RequestFilter[] filters) {
        this();
        if (filters == null) {
            throw new NullPointerException("filters must not be null");
        }
        for (RequestFilter element : filters) {
            this.filters.add(element);
        }
    }

    public boolean isRequestAccepted(DHCPPacket request) {
        for (RequestFilter filter : this.filters) {
            if ((filter != null) && (filter.isRequestAccepted(request))) {
                return true;
            }
        }
        return false;
    }

    /**
	 * @return Returns the filters.
	 */
    public List<RequestFilter> getFilters() {
        return filters;
    }
}
