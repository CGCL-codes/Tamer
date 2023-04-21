package org.octaedr.upnp.core;

import org.octaedr.httpudp.Request;

/**
 * Discovery search request.
 */
public class SearchRequest extends Request {

    /**
     * Constructs search request.
     * 
     * @param delay
     *            Seconds to delay response
     * @param searchTarget
     *            Search target.
     */
    public SearchRequest(final int delay, final String searchTarget) {
        super("M-SEARCH", "*", HTTP_VERSION__1_1);
        if (delay <= 0) {
            throw new IllegalArgumentException("Delay must be positive");
        }
        if (searchTarget == null) {
            throw new IllegalArgumentException("Search target may not be null");
        }
        setHeader("HOST", "239.255.255.250:1900");
        setHeader("MAN", "\"ssdp:discover\"");
        setHeader("MX", Integer.toString(delay));
        setHeader("ST", searchTarget);
    }

    SearchRequest(Request request) {
        super(request);
    }

    public int getDelayTime() {
        try {
            String value = getHeaderValue("MX");
            if (value != null) {
                return Integer.parseInt(value, 10);
            }
        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public String getSearchTarget() {
        return getHeaderValue("ST");
    }

    static boolean canExtend(Request request) {
        String value;
        value = request.getHeaderValue("MAN");
        if (value == null || !value.equals("\"ssdp:discover\"")) {
            return false;
        }
        value = request.getHeaderValue("HOST");
        if (value == null || !value.equals("239.255.255.250:1900")) {
            return false;
        }
        value = request.getHeaderValue("ST");
        if (value == null || value.length() == 0) {
            return false;
        }
        value = request.getHeaderValue("MX");
        if (value == null) {
            return false;
        }
        try {
            int mx = Integer.parseInt(value, 10);
            if (mx <= 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
