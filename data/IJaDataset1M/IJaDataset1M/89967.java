package org.nomadpim.module.timetracking.recurrent_activity;

import java.util.List;
import org.nomadpim.core.CoreFacade;
import org.nomadpim.module.timetracking.activity.ActivityFacade;
import org.nomadpim.module.timetracking.activity.IActivity;
import org.nomadpim.module.timetracking.activity.IActivityProvider;

public class RecurrentActivityActivityProvider implements IActivityProvider {

    public List<IActivity> getActivities() {
        return ActivityFacade.convertEntitiesToActivities(CoreFacade.getContainer(RecurrentActivity.TYPE_NAME).get());
    }

    public String getLabel() {
        return "Recurrent Activities";
    }
}
