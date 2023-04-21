package net.admin4j.ui.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import net.admin4j.config.Admin4JConfiguration;
import net.admin4j.monitor.Detector;
import net.admin4j.monitor.LowMemoryDetector;
import net.admin4j.util.Daemon;
import net.admin4j.util.ServletUtils;
import net.admin4j.util.notify.NotifierUtils;

/**
 * This servlet will start a memory monitor that will notify you of memory issues.
 * <p>Init parameters for this servlet are as follows:</p>
 * <li>sleep.interval.millis -- Optional.  Default 30000.  Amount of time in millis monitor will sleep between checks.</li>
 * <li>memory.threshold.pct -- Optional.  Default 90. The percentage of memory that must be allocated before low memory warnings are issued.</li>
 * <li>nbr.intervals.between.warnings -- Optional.  Default 30. Number of intervals low memory must be experienced before repeat warnings are issued.</li>
 * <li>notifier -- Required.  Handles admin notification.  See documentation for the Notifier you're using
 * for any additional configuration requirements.  For example, 'net.admin4j.util.notify.EmailNotifier'.</li>
 * @author D. Ashmore
 * @since 1.0
 */
public class MemoryMonitorStartupServlet extends Admin4JServlet {

    private static final long serialVersionUID = 694573116545416300L;

    public static final String PUBLIC_HANDLE = "memory";

    private long sleepIntervalMillis = LowMemoryDetector.DEFAULT_SLEEP_INTERVAL;

    private int memoryThresholdPct = LowMemoryDetector.DEFAULT_MEMORY_USAGE_THRESHOLD_PCT;

    private int nbrIntervalsBetweenWarnings = LowMemoryDetector.DEFAULT_NBR_INTERVALS_BETWEEN_WARNINGS;

    private int nbrLowWatermarkIntervals = LowMemoryDetector.DEFAULT_NBR_LOW_WATERMARK_INTERVALS;

    private long lowWatermarkMonitorIntervalInMillis = LowMemoryDetector.DEFAULT_LOW_WATERMARK_MONITOR_INTERVAL_IN_MILLIS;

    @SuppressWarnings("unused")
    private static Daemon memoryMonitorDaemon = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            String sleepIntervalMillisStr = ServletUtils.getConfigurationSetting(new String[] { PUBLIC_HANDLE + ".sleep.interval.millis", "sleep.interval.millis" }, config);
            if (sleepIntervalMillisStr != null) {
                try {
                    sleepIntervalMillis = Long.parseLong(sleepIntervalMillisStr);
                } catch (Exception e) {
                    sleepIntervalMillis = LowMemoryDetector.DEFAULT_SLEEP_INTERVAL;
                    this.logger.error("Invalid sleep.interval.millis parameter for MemoryMonitorStartupServlet.  Default used.  sleep.interval.millis={}", sleepIntervalMillisStr, e);
                }
            } else if (Admin4JConfiguration.getMemorySleepIntervalMillis() != null) {
                sleepIntervalMillis = Admin4JConfiguration.getMemorySleepIntervalMillis();
            }
            String memoryThresholdPctStr = ServletUtils.getConfigurationSetting(new String[] { PUBLIC_HANDLE + ".threshold.pct" }, config);
            if (memoryThresholdPctStr != null) {
                try {
                    memoryThresholdPct = Integer.parseInt(memoryThresholdPctStr);
                } catch (Exception e) {
                    memoryThresholdPct = LowMemoryDetector.DEFAULT_MEMORY_USAGE_THRESHOLD_PCT;
                    this.logger.error("Invalid memory.threshold.pct parameter for MemoryMonitorStartupServlet.  Default used.  memory.threshold.pct={}", memoryThresholdPctStr, e);
                }
            } else if (Admin4JConfiguration.getMemoryThresholdPct() != null) {
                memoryThresholdPct = Admin4JConfiguration.getMemoryThresholdPct();
            }
            String nbrIntervalsBetweenWarningsStr = ServletUtils.getConfigurationSetting(new String[] { PUBLIC_HANDLE + ".nbr.intervals.between.warnings", "nbr.intervals.between.warnings" }, config);
            if (nbrIntervalsBetweenWarningsStr != null) {
                try {
                    nbrIntervalsBetweenWarnings = Integer.parseInt(nbrIntervalsBetweenWarningsStr);
                } catch (Exception e) {
                    nbrIntervalsBetweenWarnings = LowMemoryDetector.DEFAULT_NBR_INTERVALS_BETWEEN_WARNINGS;
                    this.logger.error("Invalid nbr.intervals.between.warnings for MemoryMonitorStartupServlet.  Default used.  nbr.intervals.between.warnings={}", nbrIntervalsBetweenWarningsStr, e);
                }
            } else if (Admin4JConfiguration.getMemoryNbrIntervalsBetweenWarnings() != null) {
                nbrIntervalsBetweenWarnings = Admin4JConfiguration.getMemoryNbrIntervalsBetweenWarnings();
            }
            String lowWatermarkMonitorIntervalStr = ServletUtils.getConfigurationSetting(new String[] { PUBLIC_HANDLE + ".low.watermark.monitor.interval.millis" }, config);
            if (lowWatermarkMonitorIntervalStr != null) {
                try {
                    lowWatermarkMonitorIntervalInMillis = Long.parseLong(lowWatermarkMonitorIntervalStr);
                } catch (Exception e) {
                    lowWatermarkMonitorIntervalInMillis = LowMemoryDetector.DEFAULT_LOW_WATERMARK_MONITOR_INTERVAL_IN_MILLIS;
                    this.logger.error("Invalid low.watermark.monitor.interval.millis parameter for MemoryMonitorStartupServlet.  Default used.  low.watermark.monitor.interval.millis={}", lowWatermarkMonitorIntervalStr, e);
                }
            } else if (Admin4JConfiguration.getMemoryLowWatermarkMonitorIntervalInMillis() != null) {
                lowWatermarkMonitorIntervalInMillis = Admin4JConfiguration.getMemoryLowWatermarkMonitorIntervalInMillis();
            }
            String nbrLowWatermarkIntervalsStr = ServletUtils.getConfigurationSetting(new String[] { PUBLIC_HANDLE + ".nbr.low.watermark.intervals" }, config);
            if (nbrLowWatermarkIntervalsStr != null) {
                try {
                    nbrLowWatermarkIntervals = Integer.parseInt(nbrLowWatermarkIntervalsStr);
                } catch (Exception e) {
                    nbrLowWatermarkIntervals = LowMemoryDetector.DEFAULT_NBR_LOW_WATERMARK_INTERVALS;
                    this.logger.error("Invalid memory.threshold.pct parameter for MemoryMonitorStartupServlet.  Default used.  memory.threshold.pct={}", nbrLowWatermarkIntervalsStr, e);
                }
            } else if (Admin4JConfiguration.getMemoryNbrLowWatermarkIntervals() != null) {
                nbrLowWatermarkIntervals = Admin4JConfiguration.getMemoryNbrLowWatermarkIntervals();
            }
            String notifierClassName = config.getInitParameter("notifier");
            Detector lowMemoryDetector = new LowMemoryDetector(NotifierUtils.configure(config, notifierClassName), memoryThresholdPct, nbrIntervalsBetweenWarnings, nbrLowWatermarkIntervals, lowWatermarkMonitorIntervalInMillis);
            memoryMonitorDaemon = new Daemon(lowMemoryDetector, "Admin4j Low Memory Detector", sleepIntervalMillis);
        } catch (Throwable t) {
            logger.error("Error in boot sequence", t);
            throw new ServletException(t);
        }
    }

    @Override
    public boolean hasDisplay() {
        return false;
    }

    protected long getSleepIntervalMillis() {
        return sleepIntervalMillis;
    }

    protected int getMemoryThresholdPct() {
        return memoryThresholdPct;
    }

    protected int getNbrIntervalsBetweenWarnings() {
        return nbrIntervalsBetweenWarnings;
    }

    protected int getNbrLowWatermarkIntervals() {
        return nbrLowWatermarkIntervals;
    }

    protected long getLowWatermarkMonitorIntervalInMillis() {
        return lowWatermarkMonitorIntervalInMillis;
    }
}
