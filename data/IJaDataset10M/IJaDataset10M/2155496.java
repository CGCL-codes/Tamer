package com.nuts.model.server;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;

@Entity
@Table(name = "intake")
public class Intake extends AbstractEntity {

    private static final long serialVersionUID = 2066392859178021071L;

    @Column(name = "comment", length = 255)
    private String comment;

    @OneToOne
    @JoinColumn(name = "nutrient_id", referencedColumnName = "nutrient_id")
    private Nutrient nutrient;

    @Min(0)
    @Column(name = "val_male")
    private Double value;

    @Min(0)
    @Column(name = "val_female")
    private Double valueFemale;

    @Min(0)
    @Column(name = "val_per_body_kg")
    private Double valuePerBodyKg;

    public String getComment() {
        return comment;
    }

    public Nutrient getNutrient() {
        return nutrient;
    }

    public Double getValue() {
        return value;
    }

    public Double getValueFemale() {
        return valueFemale;
    }

    public Double getValuePerBodyKg() {
        return valuePerBodyKg;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setNutrient(Nutrient nutrient) {
        this.nutrient = nutrient;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public void setValueFemale(Double valueFemale) {
        this.valueFemale = valueFemale;
    }

    public void setValuePerBodyKg(Double valuePerBodyKg) {
        this.valuePerBodyKg = valuePerBodyKg;
    }
}
