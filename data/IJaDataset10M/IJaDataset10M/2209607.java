package org.matsim.basic.signalsystems;

import org.matsim.interfaces.basic.v01.Id;

/**
 * @author dgrether
 */
public class BasicSignalSystemDefinitionImpl implements BasicSignalSystemDefinition {

    private Id id;

    private double defaultCirculationTime;

    private double syncronizationOffset;

    private double defaultInterimTime;

    public BasicSignalSystemDefinitionImpl(Id id) {
        this.id = id;
    }

    /**
	 * @see org.matsim.basic.signalsystems.BasicSignalSystemDefinition#getId()
	 */
    public Id getId() {
        return id;
    }

    /**
	 * @see org.matsim.basic.signalsystems.BasicSignalSystemDefinition#getDefaultCirculationTime()
	 */
    public double getDefaultCirculationTime() {
        return defaultCirculationTime;
    }

    /**
	 * @see org.matsim.basic.signalsystems.BasicSignalSystemDefinition#setDefaultCirculationTime(double)
	 */
    public void setDefaultCirculationTime(double defaultCirculationTime) {
        this.defaultCirculationTime = defaultCirculationTime;
    }

    /**
	 * @see org.matsim.basic.signalsystems.BasicSignalSystemDefinition#getDefaultSyncronizationOffset()
	 */
    public double getDefaultSyncronizationOffset() {
        return syncronizationOffset;
    }

    /**
	 * @see org.matsim.basic.signalsystems.BasicSignalSystemDefinition#setDefaultSyncronizationOffset(double)
	 */
    public void setDefaultSyncronizationOffset(double syncronizationOffset) {
        this.syncronizationOffset = syncronizationOffset;
    }

    /**
	 * @see org.matsim.basic.signalsystems.BasicSignalSystemDefinition#getDefaultInterimTime()
	 */
    public double getDefaultInterimTime() {
        return defaultInterimTime;
    }

    /**
	 * @see org.matsim.basic.signalsystems.BasicSignalSystemDefinition#setDefaultInterimTime(double)
	 */
    public void setDefaultInterimTime(double defaultInterimTime) {
        this.defaultInterimTime = defaultInterimTime;
    }
}
