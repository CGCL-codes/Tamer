package de.iph.arbeitsgruppenassistent.server.task.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import de.iph.arbeitsgruppenassistent.server.resource.entity.AbstractTaskStepEntity;
import de.iph.arbeitsgruppenassistent.server.resource.entity.EmployeeEntity;
import de.iph.arbeitsgruppenassistent.server.resource.entity.MachineEntity;

/**
 * 
 * @author Andreas Bruns
 */
@Entity
@Table(name = "CONCRETE_TASK_STEP")
public class ConcreteTaskStepEntity extends AbstractTaskStepEntity {

    private static final long serialVersionUID = 1L;

    private int startUpTime;

    private int tearDownTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    private int empQualiAdjustment;

    @ManyToMany
    @JoinColumn(unique = false)
    private List<ConcreteTaskStepEntity> precursors;

    @ManyToOne
    private TaskEntity task;

    @ManyToMany
    @LazyCollection(value = LazyCollectionOption.FALSE)
    private List<EmployeeEntity> savedEmployees;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @LazyToOne(value = LazyToOneOption.FALSE)
    private MachineEntity savedMachine;

    @Temporal(TemporalType.TIMESTAMP)
    private Date completeDate;

    private String completeComment;

    public Date getEndDate() {
        return endDate;
    }

    public MachineEntity getSavedMachine() {
        return savedMachine;
    }

    public void setSavedMachine(MachineEntity savedMachine) {
        this.savedMachine = savedMachine;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<EmployeeEntity> getSavedEmployees() {
        return savedEmployees;
    }

    public void setSavedEmployees(List<EmployeeEntity> savedEmployees) {
        this.savedEmployees = savedEmployees;
    }

    public int getStartUpTime() {
        return startUpTime;
    }

    public void setStartUpTime(int startUpTime) {
        this.startUpTime = startUpTime;
    }

    public int getTearDownTime() {
        return tearDownTime;
    }

    public void setTearDownTime(int tearDownTime) {
        this.tearDownTime = tearDownTime;
    }

    public TaskEntity getTask() {
        return task;
    }

    public void setTask(TaskEntity task) {
        this.task = task;
    }

    public List<ConcreteTaskStepEntity> getPrecursors() {
        return precursors;
    }

    public void setPrecursors(List<ConcreteTaskStepEntity> precursors) {
        this.precursors = precursors;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getEmpQualiAdjustment() {
        return empQualiAdjustment;
    }

    public void setEmpQualiAdjustment(int empQualiAdjustment) {
        this.empQualiAdjustment = empQualiAdjustment;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteComment(String completeComment) {
        this.completeComment = completeComment;
    }

    public String getCompleteComment() {
        return completeComment;
    }

    public int compareTo(AbstractTaskStepEntity other) {
        String thisCompareString = this.toString();
        String otherCompareString = other.toString();
        return thisCompareString.compareTo(otherCompareString);
    }
}
