package au.com.cahaya.hubung.project.adt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import au.com.cahaya.hubung.project.model.ProjectTaskModel;

/**
 *
 *
 * @author Mathew Pole
 * @since May 2008
 * @version $Revision$
 */
public class ProjectTaskTimeWeek extends ProjectTaskTime {

    /** The private logger for this class */
    private Logger myLog = LoggerFactory.getLogger(ProjectTaskTimeWeek.class);

    /** */
    private long[] myMinutesPerDay = new long[7];

    /**
   * Constructor
   */
    public ProjectTaskTimeWeek(ProjectTaskModel projectTask) {
        super(projectTask);
    }

    /**
   *
   */
    @Override
    public void addTime(long minutes) {
        throw new UnsupportedOperationException("Need to specify a day when using " + ProjectTaskTimeWeek.class);
    }

    /**
   *
   */
    public void addTime(int day, long minutes) {
        super.addTime(minutes);
        myMinutesPerDay[day - 1] += minutes;
    }

    /**
   *
   */
    public long getMinutes(int day) {
        return myMinutesPerDay[day - 1];
    }

    /**
   * Move the times allocated to Saturday and Sunday to Friday.
   */
    public void moveWeekendToFriday() {
        myMinutesPerDay[5] += myMinutesPerDay[0] + myMinutesPerDay[6];
        myMinutesPerDay[0] = 0;
        myMinutesPerDay[6] = 0;
    }

    /**
   *
   */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getProjectTask().getProject().getName());
        sb.append(" (ptyKey = ").append(getProjectTask().getProject().getParty().getKey());
        sb.append(" - ").append(getProjectTask().getName());
        sb.append(" M: ").append(myMinutesPerDay[1]);
        sb.append(", T: ").append(myMinutesPerDay[2]);
        sb.append(", W: ").append(myMinutesPerDay[3]);
        sb.append(", T: ").append(myMinutesPerDay[4]);
        sb.append(", F: ").append(myMinutesPerDay[5]);
        sb.append(", S: ").append(myMinutesPerDay[6]);
        sb.append(", S: ").append(myMinutesPerDay[0]);
        return sb.toString();
    }
}
