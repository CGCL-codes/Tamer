package irrigator.scheduling;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ScheduledEventWithDuration implements Serializable {

    private static Log logger = LogFactory.getLog(ScheduledEventWithDuration.class);

    private String cronExpression;

    private int duration;

    private String description;

    private boolean active;

    public ScheduledEventWithDuration(String cronExpression, int duration, String description, boolean active) {
        this.cronExpression = cronExpression;
        this.duration = duration;
        this.description = description;
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
