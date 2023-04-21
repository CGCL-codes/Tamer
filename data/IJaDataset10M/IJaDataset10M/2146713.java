package nl.gridshore.samples.training.domain;

import org.springmodules.validation.bean.conf.loader.annotation.handler.Range;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;
import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: jettro
 * Date: Jan 18, 2008
 * Time: 11:37:09 PM
 * Entity object representing an instance of a session of a specific training
 */
@Entity
@Table(name = "to_trainingsession")
public class TrainingSession extends BaseDomain {

    @Range(min = 1, max = 53)
    @NotNull
    private Integer weekNr;

    private Integer year;

    private String remark;

    @ManyToOne
    @JoinColumn(name = "training_id")
    private Training training;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    public Integer getWeekNr() {
        return weekNr;
    }

    public void setWeekNr(Integer weekNr) {
        this.weekNr = weekNr;
    }

    public String getRemark() {
        return remark;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }
}
