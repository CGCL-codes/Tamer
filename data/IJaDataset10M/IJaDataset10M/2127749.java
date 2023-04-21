package org.matsim.core.api.experimental.facilities;

import java.util.Map;
import org.matsim.core.facilities.ActivityOption;

public interface ActivityFacility extends Facility {

    public Map<String, ActivityOption> getActivityOptions();
}
