package sbpme.designer.model;

import java.util.ArrayList;
import java.util.List;

public class ProcessDiagram extends AbstractModel {

    private List<Activity> activities;

    public ProcessDiagram() {
        activities = new ArrayList<Activity>();
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
        notifyObservers();
    }

    public void addActivity(int index, Activity activity) {
        activities.add(index, activity);
        notifyObservers();
    }

    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public List getActivities() {
        return activities;
    }
}
