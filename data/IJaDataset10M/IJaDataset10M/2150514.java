package org.gudy.azureus2.core3.ipfilter.impl;

import org.gudy.azureus2.core3.util.*;
import org.gudy.azureus2.core3.ipfilter.*;

public class BadIpImpl implements BadIp {

    protected String ip;

    protected int warning_count;

    protected long last_time;

    protected BadIpImpl(String _ip) {
        ip = _ip;
    }

    protected int incrementWarnings() {
        last_time = SystemTime.getCurrentTime();
        return (++warning_count);
    }

    public String getIp() {
        return (ip);
    }

    public int getNumberOfWarnings() {
        return (warning_count);
    }

    public long getLastTime() {
        return (last_time);
    }
}
