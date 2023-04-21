package org.matsim.core.basic.signalsystems;

import org.matsim.api.basic.v01.Id;

/**
 * 
 * @author dgrether
 *
 */
public interface BasicSignalSystemDefinition {

    public Id getId();

    public double getDefaultCycleTime();

    public void setDefaultCycleTime(double defaultCirculationTime);

    public double getDefaultSynchronizationOffset();

    public void setDefaultSynchronizationOffset(double synchronizationOffset);

    public double getDefaultInterGreenTime();

    public void setDefaultInterGreenTime(double defaultInterimTime);
}
