package com.express.domain;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import javax.persistence.*;
import java.util.Calendar;

/**
 * DailyProjectStatusRecord is immutable and records how many points have been competed to date and how many points
 * the Project has in total. This allows some more detailed reporting on the progress of a project toward completion.
 *
 * @author adam boas
 */
@Entity
@Table(name = "project_history")
public class DailyProjectStatusRecord implements Persistable {

    @Transient
    private PersistableEqualityStrategy equalityStrategy = new PersistableEqualityStrategy(this);

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "gen_proj_status_rec")
    @TableGenerator(name = "gen_proj_status_rec", table = "sequence_list", pkColumnName = "name", valueColumnName = "next_value", allocationSize = 1, initialValue = 100, pkColumnValue = "project_status")
    @Column(name = "project_status_id")
    private Long id;

    @Column(name = "record_date")
    @Temporal(TemporalType.DATE)
    private Calendar date;

    @Column(name = "points_total")
    private Integer totalPoints;

    @Column(name = "points_completed")
    private Integer completedPoints;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    /** This is not to be used, it exists to fulfill the serializable contract */
    protected DailyProjectStatusRecord() {
    }

    public DailyProjectStatusRecord(Calendar date, Integer totalPoints, Integer completedPoints, Project project) {
        this.date = date;
        this.totalPoints = totalPoints;
        this.completedPoints = completedPoints;
        this.project = project;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getDate() {
        return date;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public Integer getCompletedPoints() {
        return completedPoints;
    }

    public Project getProject() {
        return project;
    }

    @Override
    public boolean equals(Object obj) {
        return equalityStrategy.entityEquals(obj);
    }

    @Override
    public int hashCode() {
        return equalityStrategy.entityHashCode(super.hashCode());
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this).toString();
    }
}
