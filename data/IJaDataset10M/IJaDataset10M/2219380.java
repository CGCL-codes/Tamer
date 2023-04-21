package net.sourceforge.thegreymenstool.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class ProjectActivityExecution implements DomainObject {

    @Id
    private long id;

    @Temporal(TemporalType.DATE)
    private Date startTime;

    @Temporal(TemporalType.DATE)
    private Date endTime;

    @ManyToOne
    private ProjectActivity projectActivity;

    public ProjectActivityExecution() {
        startTime = new Date();
    }

    public Date getEndTime() {
        return endTime;
    }

    public long getId() {
        return id;
    }

    public ProjectActivity getProjectActivity() {
        return projectActivity;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setProjectActivity(ProjectActivity projectActivity) {
        this.projectActivity = projectActivity;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return (int) ((endTime.getTime() - startTime.getTime()) / 1000);
    }
}
