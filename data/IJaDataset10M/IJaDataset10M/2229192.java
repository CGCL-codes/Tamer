package timeregistrering_v01.Model;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class WorkDay {

    private ArrayList<ProjectRegistration> projectRegistrations;

    private GregorianCalendar startTime, endTime;

    public WorkDay(GregorianCalendar startTime, GregorianCalendar endTime) {
        projectRegistrations = new ArrayList<ProjectRegistration>();
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public GregorianCalendar getEndTime() {
        return endTime;
    }

    public void setEndTime(GregorianCalendar val) {
        this.endTime = val;
    }

    public ArrayList<ProjectRegistration> getProjectRegistrations() {
        return projectRegistrations;
    }

    public GregorianCalendar getStartTime() {
        return startTime;
    }

    public void setStartTime(GregorianCalendar val) {
        this.startTime = val;
    }

    public void addProjectRegistration(ProjectRegistration reg) {
        projectRegistrations.add(reg);
    }

    public void updateEndTime() {
        this.setEndTime(new GregorianCalendar());
    }
}
