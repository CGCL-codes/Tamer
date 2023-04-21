package com.xerox.amazonws.ec2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * This is a container class for endpoint state
 */
public class HealthCheck {

    private String target;

    private int interval;

    private int timeout;

    private int unhealthyThreshold;

    private int healthyThreshold;

    public HealthCheck(String target, int interval, int timeout, int unhealthyThreshold, int healthyThreshold) {
        this.target = target;
        this.interval = interval;
        this.timeout = timeout;
        this.unhealthyThreshold = unhealthyThreshold;
        this.healthyThreshold = healthyThreshold;
    }

    public String getTarget() {
        return target;
    }

    public int getInterval() {
        return interval;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getUnhealthyThreshold() {
        return unhealthyThreshold;
    }

    public int getHealthyThreshold() {
        return healthyThreshold;
    }

    public String toString() {
        return "HealthCheck[target=" + target + ", interval=" + interval + ", timeout=" + timeout + ", unhealthyThreshold=" + unhealthyThreshold + ", healthyThreshold=" + healthyThreshold + "]";
    }
}
