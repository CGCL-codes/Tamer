package org.echarts.jain.sip;

import java.util.TimerTask;
import org.apache.log4j.Logger;

class RtpPresenceDetectionTask extends TimerTask {

    private Object objToNotify;

    private long waitStartTime;

    private int maxWait;

    private RTPEndpoint endPoint;

    public enum Reason {

        MaxWait, Presence
    }

    ;

    private Reason reason = null;

    private Logger logger;

    RtpPresenceDetectionTask(int maxWait, Object objToNotify, long waitStartTime, RTPEndpoint endPoint, E4JSHelper e4jsHelper) {
        this.waitStartTime = waitStartTime;
        this.maxWait = maxWait;
        this.objToNotify = objToNotify;
        this.endPoint = endPoint;
        this.reason = null;
        this.logger = e4jsHelper.getLogger();
    }

    public Reason getReason() {
        return this.reason;
    }

    public void run() {
        long currTime = System.currentTimeMillis();
        long lastTimeStamp = endPoint.getLastRtpRecvdTimestamp();
        logger.debug("Timer Task : lastTimeStamp = " + lastTimeStamp + " waitStartTime = " + waitStartTime);
        if (lastTimeStamp > waitStartTime) {
            if (objToNotify != null) {
                this.reason = Reason.Presence;
                synchronized (objToNotify) {
                    objToNotify.notify();
                }
            }
            this.cancel();
            logger.debug("Speech detected");
            return;
        }
        if (currTime - waitStartTime >= this.maxWait) {
            if (objToNotify != null) {
                this.reason = Reason.MaxWait;
                synchronized (objToNotify) {
                    objToNotify.notify();
                }
            }
            logger.debug("Speech detection: max wait exceeded");
            this.cancel();
            return;
        }
        logger.debug("Speech not detected. max wait not reached");
    }
}
