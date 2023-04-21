package com.alesj.blueberry.spring.timestamp.impl;

import com.alesj.blueberry.spring.timestamp.Timestamper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

/**
 * @author <a href="mailto:ales.justin@genera-lynx.com">Ales Justin</a>
 */
public class SystemTimestamper implements Timestamper {

    private Log log = LogFactory.getLog(getClass());

    public synchronized long timestamp() {
        long timestamp = System.currentTimeMillis();
        log.info("Timestamping: " + timestamp);
        return timestamp;
    }

    public synchronized Date serverDate() {
        return new Date(timestamp());
    }
}
