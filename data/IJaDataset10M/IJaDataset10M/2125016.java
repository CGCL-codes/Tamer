package org.blueoxygen.brigade.entity;

import java.sql.Date;
import java.sql.Time;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.blueoxygen.cimande.DefaultPersistence;

@Entity()
@Table(name = "brigade_assessement_period")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class AssessementPeriod extends DefaultPersistence {

    private String startTime;

    private String endTime;

    private String startDate;

    private String endDate;

    private Assessement assessement;

    private ExamForm examForm;

    @Column(name = "start_time")
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Column(name = "end_time")
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Column(name = "start_date")
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Column(name = "end_date")
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @ManyToOne()
    @JoinColumn(name = "assessement_id")
    public Assessement getAssessement() {
        return assessement;
    }

    public void setAssessement(Assessement assessement) {
        this.assessement = assessement;
    }

    @ManyToOne()
    @JoinColumn(name = "examform_id")
    public ExamForm getExamForm() {
        return examForm;
    }

    public void setExamForm(ExamForm examForm) {
        this.examForm = examForm;
    }
}
