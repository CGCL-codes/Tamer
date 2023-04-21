package org.mobicents.slee.container.congestion;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.slee.facilities.AlarmFacility;
import javax.slee.facilities.AlarmLevel;
import org.apache.log4j.Logger;
import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.management.jmx.CongestionControlConfiguration;

/**
 * Impl of the congestion control module of the Mobicents slee container.
 * @author martins
 *
 */
public class CongestionControlImpl extends AbstractSleeContainerModule implements CongestionControl {

    private static final Logger logger = Logger.getLogger(CongestionControlImpl.class);

    private final CongestionControlConfiguration configuration;

    private boolean notEnoughFreeMemory;

    private ScheduledFuture<?> scheduledFuture;

    private AlarmFacility alarmFacility;

    private int alarmIdCounter = 0;

    private String alarmID;

    private static final String ALARM_TYPE = "CONGESTION CONTROL";

    /**
	 * 
	 * @param configuration
	 */
    public CongestionControlImpl(CongestionControlConfiguration configuration) {
        this.configuration = configuration;
        if (configuration.getPeriodBetweenChecks() == 0) {
            logger.info("Mobicents SLEE Congestion Control is OFF. Configuration: " + configuration);
        } else {
            logger.info("Mobicents SLEE Congestion Control is ON. Configuration: " + configuration);
        }
        this.configuration.setCongestionControl(this);
    }

    @Override
    public void sleeInitialization() {
        alarmFacility = sleeContainer.getAlarmManagement().newAlarmFacility(new CongestionControlNotification(sleeContainer.getCluster().getLocalAddress()));
    }

    @Override
    public void sleeStarting() {
        configurationUpdate();
    }

    @Override
    public void sleeStopping() {
        reset();
    }

    private void reset() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            scheduledFuture = null;
        }
        clearAlarm();
        notEnoughFreeMemory = false;
    }

    public boolean refuseStartActivity() {
        if (configuration.isRefuseStartActivity()) {
            return notEnoughFreeMemory;
        } else {
            return false;
        }
    }

    public boolean refuseFireEvent() {
        if (configuration.isRefuseFireEvent()) {
            return notEnoughFreeMemory;
        } else {
            return false;
        }
    }

    /**
	 * 
	 */
    public void configurationUpdate() {
        reset();
        int periodBetweenChecks = configuration.getPeriodBetweenChecks();
        if (periodBetweenChecks > 0) {
            scheduledFuture = getScheduler().scheduleAtFixedRate(new TimerTask(), 0, periodBetweenChecks, TimeUnit.SECONDS);
        }
    }

    private class TimerTask implements Runnable {

        public void run() {
            if (notEnoughFreeMemory) {
                if (getFreeMemoryPercentage() > configuration.getMinFreeMemoryToTurnOff()) {
                    clearAlarm();
                    notEnoughFreeMemory = false;
                }
            } else {
                if (getFreeMemoryPercentage() < configuration.getMinFreeMemoryToTurnOn()) {
                    raiseAlarm();
                    notEnoughFreeMemory = true;
                }
            }
        }
    }

    private int getFreeMemoryPercentage() {
        return (int) ((getFreeMemory() * 100) / getMaxMemory());
    }

    protected ScheduledExecutorService getScheduler() {
        return sleeContainer.getNonClusteredScheduler();
    }

    protected long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    protected long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    protected void raiseAlarm() {
        if (alarmID == null) {
            alarmIdCounter++;
            alarmID = alarmFacility.raiseAlarm(ALARM_TYPE, Integer.toString(alarmIdCounter), AlarmLevel.CRITICAL, "Congestion Control activated since free memory is less than " + configuration.getMinFreeMemoryToTurnOn());
        }
    }

    protected void clearAlarm() {
        if (alarmID != null) {
            alarmFacility.clearAlarm(alarmID);
            alarmID = null;
        }
    }
}
