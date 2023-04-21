package org.swana.daemon;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.log4j.Logger;

/**
 * The firewall is used to block the repeated access that not exceeds the expire time. <br/>
 * It will clean the expired entries in firewall at specified time inverval.<br/>
 * 
 * @author Wang Yuxing
 * 
 */
public class AccessFirewall {

    Timer timer = new Timer();

    private int expireTimeDuration = 5;

    private int flushTimeDuration = 2;

    private boolean enabled = true;

    private Map<Object, Calendar> map = new HashMap<Object, Calendar>();

    private Logger logger = null;

    private static AccessFirewall af = null;

    public AccessFirewall() {
        logger = Logger.getLogger(this.getClass().getName());
        logger.setAdditivity(false);
        scheduleCleaningThread();
    }

    /**
	 * Singleton.
	 * 
	 * @return
	 */
    public static AccessFirewall getInstance() {
        if (af == null) {
            af = new AccessFirewall();
        }
        return af;
    }

    private void scheduleCleaningThread() {
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                int count = 0;
                if (af == null) return;
                Set keys = map.keySet();
                if (keys.size() == 0) {
                    return;
                }
                logger.info("\n");
                synchronized (af) {
                    for (Iterator it = keys.iterator(); it.hasNext(); ) {
                        Object key = it.next();
                        Calendar expireTime = map.get(key);
                        if (Calendar.getInstance().after(expireTime)) {
                            map.remove(key);
                            count++;
                            logger.debug("_");
                        }
                    }
                }
                if (logger.isDebugEnabled()) {
                    logger.info("\n " + Calendar.getInstance().getTime() + " : " + count + " access logs " + " have expired " + getExpireTimeDuration() + "(s) and cleaned from firewall.\n");
                }
            }
        }, 0, this.getFlushTimeDuration() * 1000);
        StringBuilder buf = new StringBuilder(100);
        buf.append("\n").append(Calendar.getInstance().getTime()).append(" Access Firewall ready to clean expired access logs every ").append(this.getFlushTimeDuration()).append(" seconds\n");
        logger.info(buf.toString());
    }

    /**
	 * Check the GUID of current access whether it can overpass.
	 * 
	 * @param guid
	 * @return
	 */
    public boolean overpass(String guid) {
        if (this.enabled == false) {
            logger.debug("-");
            return true;
        }
        synchronized (this) {
            if (map.get(guid) == null) {
                Calendar expireTime = Calendar.getInstance();
                expireTime.add(Calendar.SECOND, this.getExpireTimeDuration());
                map.put(guid, expireTime);
                logger.debug("-");
                return true;
            }
        }
        logger.debug("#");
        return false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled == false) {
            timer.cancel();
        } else {
            scheduleCleaningThread();
        }
    }

    public int getExpireTimeDuration() {
        return expireTimeDuration;
    }

    public void setExpireTimeDuration(int expireTimeDuration) {
        this.expireTimeDuration = expireTimeDuration;
    }

    public int getFlushTimeDuration() {
        return flushTimeDuration;
    }

    public void setFlushTimeDuration(int flushTimeDuration) {
        this.flushTimeDuration = flushTimeDuration;
        timer.cancel();
        logger.info("\nFlush schedule has been canceled because of the change of flush time duration\n");
        scheduleCleaningThread();
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        AccessFirewall.getInstance().setExpireTimeDuration(6);
        AccessFirewall.getInstance().setFlushTimeDuration(8);
        boolean ticket = AccessFirewall.getInstance().overpass("hello");
        System.out.println(ticket);
        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ticket = AccessFirewall.getInstance().overpass("hello");
        System.out.println(ticket);
    }
}
