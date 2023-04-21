package com.protomatter.pas.cronmanager;

import java.util.*;
import com.protomatter.syslog.Syslog;
import com.protomatter.util.*;
import com.protomatter.pas.event.*;
import com.protomatter.pas.cron.*;

/**
 *  A thread that works for the <tt>PASCronManagerImpl</tt> class.
 */
class PASCronThread extends Thread {

    private PASEventManager em = null;

    private Vector crontab = null;

    private WorkQueue workQueue = null;

    private static long MILLIS_PER_MINUTE = 60000;

    public PASCronThread(PASEventManager em, Vector crontab, WorkQueue queue) {
        this.em = em;
        this.crontab = crontab;
        this.workQueue = queue;
        setDaemon(true);
    }

    public void run() {
        Syslog.info(this, "PASCronThread starting");
        while (true) {
            sleepTillNextMinute();
            workQueue.addWork(new PASCronRunnable(em, crontab, new Date()));
        }
    }

    private void sleepTillNextMinute() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        long millisAfterMinute = (now % MILLIS_PER_MINUTE);
        long millisTillNextMinute = MILLIS_PER_MINUTE - millisAfterMinute;
        try {
            sleep(millisTillNextMinute + 10);
        } catch (Exception x) {
            sleepTillNextMinute();
        }
    }
}
