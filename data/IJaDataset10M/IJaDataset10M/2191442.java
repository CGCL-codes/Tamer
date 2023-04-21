package Taverna.Tree.Processor;

import Taverna.Tree.TavernaNode;
import java.util.List;
import java.util.ArrayList;

public class Activities extends TavernaNode {

    public Activities() {
        activities = new ArrayList<Activity>();
    }

    public void addActivity(Activity a) {
        activities.add(a);
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> l) {
        activities = l;
    }

    private List<Activity> activities;
}
