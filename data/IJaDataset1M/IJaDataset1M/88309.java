package org.matsim.core.basic.signalsystemsconfig;

import java.util.HashMap;
import java.util.Map;
import org.matsim.api.basic.v01.Id;

/**
 * @author dgrether
 */
public class BasicSignalSystemPlanImpl implements BasicSignalSystemPlan {

    private Id id;

    private double startTime;

    private double endTime;

    private Map<Id, BasicSignalGroupSettings> groupConfigs;

    private Integer syncronizationOffset = null;

    private Integer circulationTime = null;

    public BasicSignalSystemPlanImpl(Id id) {
        this.id = id;
    }

    /**
	 * @see org.matsim.core.basic.signalsystemsconfig.BasicSignalSystemPlan#setStartTime(double)
	 */
    public void setStartTime(double seconds) {
        this.startTime = seconds;
    }

    /**
	 * @see org.matsim.core.basic.signalsystemsconfig.BasicSignalSystemPlan#setEndTime(double)
	 */
    public void setEndTime(double seconds) {
        this.endTime = seconds;
    }

    /**
	 * @see org.matsim.core.basic.signalsystemsconfig.BasicSignalSystemPlan#getId()
	 */
    public Id getId() {
        return id;
    }

    /**
	 * @see org.matsim.core.basic.signalsystemsconfig.BasicSignalSystemPlan#addLightSignalGroupConfiguration(org.matsim.core.basic.signalsystemsconfig.BasicSignalGroupSettings)
	 */
    public void addLightSignalGroupConfiguration(BasicSignalGroupSettings groupConfig) {
        if (this.groupConfigs == null) {
            this.groupConfigs = new HashMap<Id, BasicSignalGroupSettings>();
        }
        this.groupConfigs.put(groupConfig.getReferencedSignalGroupId(), groupConfig);
    }

    /**
	 * @see org.matsim.core.basic.signalsystemsconfig.BasicSignalSystemPlan#getStartTime()
	 */
    public double getStartTime() {
        return startTime;
    }

    /**
	 * @see org.matsim.core.basic.signalsystemsconfig.BasicSignalSystemPlan#getEndTime()
	 */
    public double getEndTime() {
        return endTime;
    }

    /**
	 * @see org.matsim.core.basic.signalsystemsconfig.BasicSignalSystemPlan#getGroupConfigs()
	 */
    public Map<Id, BasicSignalGroupSettings> getGroupConfigs() {
        return groupConfigs;
    }

    /**
	 * @see org.matsim.core.basic.signalsystemsconfig.BasicSignalSystemPlan#setCycleTime(java.lang.Integer)
	 */
    public void setCycleTime(Integer circulationTimeSec) {
        this.circulationTime = circulationTimeSec;
    }

    /**
	 * @see org.matsim.core.basic.signalsystemsconfig.BasicSignalSystemPlan#setSynchronizationOffset(java.lang.Integer)
	 */
    public void setSynchronizationOffset(Integer seconds) {
        this.syncronizationOffset = seconds;
    }

    /**
	 * @see org.matsim.core.basic.signalsystemsconfig.BasicSignalSystemPlan#getSynchronizationOffset()
	 */
    public Integer getSynchronizationOffset() {
        return syncronizationOffset;
    }

    /**
	 * @see org.matsim.core.basic.signalsystemsconfig.BasicSignalSystemPlan#getCycleTime()
	 */
    public Integer getCycleTime() {
        return circulationTime;
    }
}
