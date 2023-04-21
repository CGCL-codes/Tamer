package org.az.calc.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 
  
  * @author Artem Zaborsky
 *
 */
@Entity
@Table(name = "PLANEXPENCE")
public class PlanExpence extends PlanEntry {

    /**
     * 
     */
    private static final long serialVersionUID = 230326292310376184L;

    @NotNull
    @Min(0)
    @Column(name = "DURATION")
    private final Integer duration = Integer.valueOf(0);

    @ManyToOne
    @JoinColumn(name = "EXERCISE_ID", insertable = true, updatable = false)
    @Valid
    private Expence exercise;

    @Transient
    @Override
    public String getDefaultEventTitle() {
        return exercise.getName();
    }

    public Integer getDuration() {
        return duration;
    }

    public void setExercise(Expence exercise) {
        this.exercise = exercise;
    }
}
