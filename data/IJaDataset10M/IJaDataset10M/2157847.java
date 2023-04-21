package com.eastidea.qaforum.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Name;

@Entity
@Name("phase")
@Table(name = "phase")
public class Phase implements Serializable {

    private static final long serialVersionUID = -5606557015529032252L;

    public Phase() {
    }

    ;

    private Long phaseid;

    private String name;

    private Integer number;

    private Date planStartDate;

    private Date planEndDate;

    private Date actualStartDate;

    private Date actualEndDate;

    private Long planWorkDay;

    private Long actualWorkDay;

    private Long planResourceCount;

    private Long actualResourceCount;

    private String status;

    private Integer percent;

    @Id
    @GeneratedValue
    public Long getPhaseid() {
        return phaseid;
    }

    public void setPhaseid(Long id) {
        this.phaseid = id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Date getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(Date date) {
        this.planStartDate = date;
        if (this.planEndDate != null && this.planStartDate != null) {
            long days = (this.planEndDate.getTime() - this.planStartDate.getTime()) / (24 * 60 * 60 * 1000);
            setPlanWorkDay(Long.valueOf(days));
        }
    }

    public Date getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(Date date) {
        this.planEndDate = date;
        if (this.planEndDate != null && this.planStartDate != null) {
            long days = (this.planEndDate.getTime() - this.planStartDate.getTime()) / (24 * 60 * 60 * 1000);
            setPlanWorkDay(Long.valueOf(days));
        }
    }

    public Date getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(Date date) {
        this.actualStartDate = date;
        if (this.actualEndDate != null && this.actualStartDate != null) {
            long days = (this.actualEndDate.getTime() - this.actualStartDate.getTime()) / (24 * 60 * 60 * 1000);
            setActualWorkDay(Long.valueOf(days));
        }
    }

    public Date getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(Date date) {
        this.actualEndDate = date;
        if (this.actualEndDate != null && this.actualStartDate != null) {
            long days = (this.actualEndDate.getTime() - this.actualStartDate.getTime()) / (24 * 60 * 60 * 1000);
            setActualWorkDay(Long.valueOf(days));
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public Long getPlanWorkDay() {
        return planWorkDay;
    }

    public void setPlanWorkDay(Long workDay) {
        this.planWorkDay = workDay;
    }

    public Long getActualWorkDay() {
        return actualWorkDay;
    }

    public void setActualWorkDay(Long workDay) {
        this.actualWorkDay = workDay;
    }

    public Long getPlanResourceCount() {
        return planResourceCount;
    }

    public void setPlanResourceCount(Long planResourceCount) {
        this.planResourceCount = planResourceCount;
    }

    public Long getActualResourceCount() {
        return actualResourceCount;
    }

    public void setActualResourceCount(Long actualResourceCount) {
        this.actualResourceCount = actualResourceCount;
    }

    private Project project;

    @ManyToOne(optional = false)
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    private Set<Task> taskItems = new HashSet<Task>();

    @OneToMany(mappedBy = "phase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public Set<Task> getTaskItems() {
        return taskItems;
    }

    public void setTaskItems(Set<Task> taskItems) {
        this.taskItems = taskItems;
    }
}
