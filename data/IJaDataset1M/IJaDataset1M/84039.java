package com.leemba.monitor.server.objects.reports;

import java.util.Date;

/**
 *
 * @author mrjohnson
 */
public class StatusHistogramBin {

    private Date reported;

    private int count;

    public int getCount() {
        return count;
    }

    public StatusHistogramBin setCount(int count) {
        this.count = count;
        return this;
    }

    public Date getReported() {
        return reported;
    }

    public StatusHistogramBin setReported(Date reported) {
        this.reported = reported;
        return this;
    }
}
