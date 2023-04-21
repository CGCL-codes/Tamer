package edu.asu.commons.mme.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.CollectionOfElements;
import edu.asu.commons.mme.entity.DayByDayDecisions;

@Entity
@Table(name = "student_strategy")
public class StudentStrategy implements Serializable {

    private static final long serialVersionUID = -7159061961616165928L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Round round;

    @Column(name = "allocation_sequence_no", nullable = false)
    private Integer allocationSeqNo;

    @Column(nullable = false)
    private Integer days;

    @Column(nullable = false)
    private Double threshold;

    @ManyToOne
    @JoinColumn(nullable = false)
    public Location location;

    @Column(name = "repeated_decision", columnDefinition = "Boolean", nullable = false)
    public boolean repeatedDecision;

    @Column(name = "repeated_decision_no")
    public Integer repeatedDecisionNo;

    public Integer getRepeatedDecisionNo() {
        return repeatedDecisionNo;
    }

    public void setRepeatedDecisionNo(Integer repeatedDecisionNo) {
        this.repeatedDecisionNo = repeatedDecisionNo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setAllocationSeqNo(Integer allocationSeqNo) {
        this.allocationSeqNo = allocationSeqNo;
    }

    public Integer getAllocationSeqNo() {
        return allocationSeqNo;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getDays() {
        return days;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setRepeatedDecision(boolean repeatedDecision) {
        this.repeatedDecision = repeatedDecision;
    }

    public boolean isRepeatedDecision() {
        return repeatedDecision;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public Round getRound() {
        return round;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }
}
