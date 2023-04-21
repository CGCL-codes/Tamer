package org.mundau.market.statistics;

public class Statistics_obj {

    private static Statistics_obj stat = null;

    long successfulRequests;

    long failedRequests;

    public static Statistics_obj getInstance() {
        if (stat == null) {
            stat = new Statistics_obj();
            return stat;
        }
        return stat;
    }

    protected Statistics_obj() {
    }
}
