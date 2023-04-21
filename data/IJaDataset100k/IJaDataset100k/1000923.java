package org.coos.actorframe;

import org.coos.actorframe.application.Application;
import org.coos.javaframe.TraceConstants;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class will monitor the ApplicationSpec xml-file for modifications and
 * perform an application updates.
 * 
 * @author Robert Bjarum, Tellu AS
 */
public class ActorSpecFileMonitor {

    private Application application;

    private File applicationSpecFile;

    private long timerDelay = 1000;

    private Timer timer;

    private long lastModyfied;

    public ActorSpecFileMonitor(Application application, File applicationSpecFile) {
        this.application = application;
        this.applicationSpecFile = applicationSpecFile;
        lastModyfied = applicationSpecFile.lastModified();
    }

    public void setTimerDelay(long timerDelay) {
        this.timerDelay = timerDelay;
    }

    public void startMonitoring() {
        application.applictionActive();
        startTimer();
    }

    private void waitState(int state) {
        try {
            while (application.getState() != state) {
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startTimer() {
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new J2SETimerTask(), timerDelay);
    }

    private class J2SETimerTask extends TimerTask {

        public void run() {
            if (application.getState() != Application.STATE_ACTIVE) {
                startTimer();
                return;
            }
            long modified = applicationSpecFile.lastModified();
            if (modified > lastModyfied) {
                lastModyfied = modified;
                application.getLogger().log(TraceConstants.tlInfo, "File changed: " + applicationSpecFile.getName());
                application.suspend();
                waitState(Application.STATE_SUSPENDED);
                application.getLogger().log(TraceConstants.tlInfo, "Application suspended. State: " + application.getState());
                application.updateApplication();
                waitState(Application.STATE_UPDATED);
                application.resume();
                waitState(Application.STATE_ACTIVE);
            }
            startTimer();
        }
    }
}
