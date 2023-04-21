package be.fedict.eid.applet.service.signer.time;

import java.util.Date;

/**
 * Implementation of a clock using the local system time.
 * 
 * @author Frank Cornelis
 * 
 */
public class LocalClock implements Clock {

    public Date getTime() {
        return new Date();
    }
}
