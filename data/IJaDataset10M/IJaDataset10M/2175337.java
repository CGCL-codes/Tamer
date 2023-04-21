package com.leemba.monitor.server.sensor.active.jmx;

import com.leemba.monitor.server.mbean.LeembaStatistics;
import com.leemba.monitor.server.mbean.MBeanQuery;
import com.leemba.monitor.server.objects.MetricUnit;
import com.leemba.monitor.server.objects.PerformanceData;
import com.leemba.monitor.server.objects.SensorReport;
import com.leemba.monitor.server.objects.Status;
import com.leemba.monitor.server.objects.state.LeembaService;
import com.leemba.monitor.server.objects.state.Platform;
import com.leemba.monitor.util.StopWatch;
import java.util.List;
import java.util.Set;
import javax.management.Attribute;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import org.apache.log4j.Logger;

/**
 *
 * @author mrjohnson
 */
public class LeembaSensor {

    private static final transient Logger log = Logger.getLogger(LeembaSensor.class);

    public static final LeembaSensor instance = new LeembaSensor();

    private LeembaSensor() {
    }

    public void check(Platform platform, LeembaService service) throws Throwable {
        SensorReport report = new SensorReport(platform, service);
        StopWatch watch = new StopWatch().start();
        MBeanQuery q = null;
        try {
            q = new MBeanQuery(service.getUrl());
            q.connect(service.getUsername(), service.getPassword());
            MBeanServerConnection mbsc = q.getMBeanConnection();
            Set<ObjectInstance> instances = mbsc.queryMBeans(new ObjectName(LeembaStatistics.OBJECT_NAME), null);
            double duration = 0;
            double running = 0;
            double notified = 0;
            for (ObjectInstance inst : instances) {
                List<Attribute> ats = mbsc.getAttributes(inst.getObjectName(), new String[] { "AverageDuration", "AverageNotifications", "AverageRunning" }).asList();
                for (Attribute at : ats) {
                    if (at.getName().equals("AverageDuration") && at.getValue() != null) duration += (Double) at.getValue(); else if (at.getName().equals("AverageNotifications") && at.getValue() != null) notified += (Double) at.getValue(); else if (at.getName().equals("AverageRunning") && at.getValue() != null) running += (Double) at.getValue();
                }
            }
            report.addPerformanceData(new PerformanceData("duration", Math.round(duration * 100) / 100.0, MetricUnit.Number));
            report.addPerformanceData(new PerformanceData("notifications", Math.round(notified * 100) / 100.0, MetricUnit.Number));
            report.addPerformanceData(new PerformanceData("running", Math.round(running * 100) / 100.0, MetricUnit.Number));
            report.setDuration((int) watch.stop()).setStatus(Status.Green).setMessage("OK").queue();
        } catch (Throwable t) {
            log.info("JMX failed", t);
            report.setStatus(Status.Red).setMessage(t).queue();
        } finally {
            if (q != null) q.close();
        }
    }
}
