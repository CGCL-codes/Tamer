package com.leemba.monitor.server.servlet;

import com.leemba.monitor.server.dao.Dao;
import com.leemba.monitor.server.objects.LifecycleListener;
import com.leemba.monitor.server.objects.SensorReport;
import com.leemba.monitor.server.objects.Status;
import com.leemba.monitor.server.objects.state.Platform;
import com.leemba.monitor.server.objects.state.PlatformGroup;
import com.leemba.monitor.server.objects.state.Service;
import com.leemba.monitor.server.script.CustomScript;
import com.leemba.monitor.util.PrettyDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author mrjohnson
 */
public class PurpleThread extends Thread {

    private static final transient Logger log = Logger.getLogger(PurpleThread.class);

    boolean running = true;

    private static final int RUN_INTERVAL = 300;

    private static final int MISSED_REPORTS = 3;

    private static final int FLOOD_PERCENT = 30;

    public static class PurpleThreadListener implements LifecycleListener {

        private static final transient Logger log = Logger.getLogger(PurpleThreadListener.class);

        private PurpleThread inst = null;

        @Override
        public LifecycleListener startup() {
            inst = new PurpleThread();
            inst.start();
            return this;
        }

        @Override
        public LifecycleListener shutdown() {
            if (inst != null) {
                try {
                    inst.running = false;
                    inst.join(2000);
                    inst.interrupt();
                    inst.join(2000);
                    inst = null;
                } catch (Throwable t) {
                    log.error("Failed to shutdown PurpleThread", t);
                }
            }
            return this;
        }
    }

    @Override
    public void run() {
        try {
            Thread.sleep(600000);
        } catch (Throwable t) {
            log.error("sleep failed", t);
            if (this.isInterrupted()) return;
        }
        boolean massOutage = false;
        while (running) {
            try {
                log.trace("run");
                List<SensorReport> reports = new ArrayList<SensorReport>();
                int services = 0;
                for (PlatformGroup group : Dao.getHead().getGroups()) {
                    for (Platform platform : group.getPlatforms()) {
                        for (Service service : platform.getServices()) {
                            services++;
                            Date last = new Date(service.getLastReport());
                            if (last == null) continue;
                            if (!service.isEnabled()) continue;
                            long now = System.currentTimeMillis();
                            long grace = service.getJobFrequency() * MISSED_REPORTS * 1000L;
                            if (now - last.getTime() > grace) {
                                reports.add(new SensorReport(platform, service).setMessage("Service failed to report").setBody("Service last reported " + PrettyDate.past(last.getTime(), now)).setStatus(Status.Purple));
                            }
                        }
                    }
                }
                if (services > 0 && (reports.size() / (double) services) * 100 > FLOOD_PERCENT) {
                    log.error("Detected mass outage.");
                    if (!massOutage) {
                        massOutage = true;
                        CustomScript.run("purple_mass_outage");
                    }
                } else {
                    massOutage = false;
                    for (SensorReport report : reports) report.queue();
                }
                Thread.sleep(RUN_INTERVAL * 1000);
            } catch (InterruptedException e) {
                running = false;
                return;
            } catch (Throwable t) {
                log.error("Unhandled exception", t);
            }
        }
    }
}
