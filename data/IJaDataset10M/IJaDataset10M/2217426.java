package com.protomatter.pas.cronmanager;

import java.util.*;
import com.protomatter.syslog.Syslog;
import com.protomatter.pas.event.*;
import com.protomatter.pas.cron.*;

/**
 *  A utility class used by the <tt>PASCronManagerImpl</tt> class.
 */
class PASCronRunnable implements Runnable {

    private PASEventManager em = null;

    private Vector crontab = null;

    private Date date = null;

    public PASCronRunnable(PASEventManager em, Vector crontab, Date date) {
        this.em = em;
        this.crontab = crontab;
        this.date = date;
    }

    public void run() {
        Enumeration e = crontab.elements();
        while (e.hasMoreElements()) {
            CronEntry ce = (CronEntry) e.nextElement();
            if (ce.appliesToDate(date)) {
                PASEvent event = ce.getEvent();
                Enumeration topics = ce.getTopics();
                while (topics.hasMoreElements()) {
                    String topic = (String) topics.nextElement();
                    em.sendEvent(topic, event);
                }
            }
        }
    }
}
