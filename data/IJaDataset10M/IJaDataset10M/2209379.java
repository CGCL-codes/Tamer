package org.matsim.signalsystems.config;

import java.util.SortedMap;
import org.matsim.api.core.v01.Id;

/**
 * 
 * @author dgrether
 *
 */
public interface PlanBasedSignalSystemControlInfo extends SignalSystemControlInfo {

    public SortedMap<Id, SignalSystemPlan> getPlans();

    public void addPlan(SignalSystemPlan plan);
}
