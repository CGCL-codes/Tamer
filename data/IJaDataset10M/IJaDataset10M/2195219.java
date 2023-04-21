package net.sf.freesimrc.managers.aircraft;

import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class AircraftManagerUpdater {

    private static final String TIMEOUT_NAME = "timeoutThread";

    private static final long TIMEOUT_PARAM = 11 * 1000;

    private static final long TIMEOUT_RATE = 4 * 1000;

    protected AircraftManager aircraftManager;

    protected Hashtable tasks;

    public AircraftManagerUpdater(AircraftManager aircraftManager) {
        this.aircraftManager = aircraftManager;
        this.tasks = new Hashtable();
    }

    public void finalize() {
        stopAllTasks();
    }

    public void startDefaultTasks() {
        Timer timer = null;
        TimerTask task = null;
        task = new AircraftTimeoutThread(TIMEOUT_PARAM);
        timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, TIMEOUT_RATE);
        tasks.put(TIMEOUT_NAME, timer);
    }

    public void stopAllTasks() {
        Collection timers = tasks.values();
        for (Iterator it = timers.iterator(); it.hasNext(); ) {
            Timer timer = (Timer) it.next();
            timer.cancel();
        }
        tasks.clear();
    }

    public void startCustomTask(String name, TimerTask task, long rate) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, rate);
        tasks.put(name, timer);
    }

    public void stopCustomTask(String name) {
        Object o = tasks.get(name);
        if (o != null && o instanceof Timer) {
            ((Timer) o).cancel();
            tasks.remove(name);
        }
    }

    class AircraftTimeoutThread extends TimerTask {

        protected long timeout;

        public AircraftTimeoutThread(long timeout) {
            this.timeout = timeout;
        }

        public void run() {
            long currentTime = (new Date()).getTime();
            Collection aircrafts = aircraftManager.getAllAircraft();
            for (Iterator it = aircrafts.iterator(); it.hasNext(); ) {
                Aircraft aircraft = (Aircraft) it.next();
                if (aircraft.hasEchoes()) {
                    long time = aircraft.getLastTime().getTime();
                    if (currentTime - time > timeout) {
                        aircraftManager.clearEchoes(aircraft.getCallsign());
                    }
                }
            }
        }
    }
}
