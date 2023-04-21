package at.itsecuritycheckpoint.hibernate.domain;

import java.util.HashSet;
import java.util.Set;

public class CompanySize {

    private Long id;

    private String name;

    private Set<Activity> activities = new HashSet<Activity>();

    public CompanySize() {
    }

    public CompanySize(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Activity> getActivities() {
        return this.activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }

    public void addActivity(Activity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("Activity is null");
        }
        activity.setCompanySize(this);
        this.activities.add(activity);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.id);
        sb.append(": ");
        sb.append(this.name);
        return sb.toString();
    }
}
