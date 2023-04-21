package edu.asu.commons.mme.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "suspend_repetition")
public class SuspendRepetition implements Serializable {

    private static final long serialVersionUID = -6058080502908139560L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Round round;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Student student;

    @Column(name = "days_in_harbor", nullable = false)
    private Integer daysInHarbor;

    @Column(name = "threshold", nullable = false)
    private Float threshold;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setDaysInHarbor(Integer harborDays) {
        this.daysInHarbor = harborDays;
    }

    public Integer getDaysInHarbor() {
        return daysInHarbor;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }

    public void setThreshold(Float threshold) {
        this.threshold = threshold;
    }

    public Float getThreshold() {
        return threshold;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public Round getRound() {
        return round;
    }
}
