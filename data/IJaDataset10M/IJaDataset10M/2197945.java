package com.integrance.budgetapp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "GOAL_TYPE")
public class GoalType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "goal_type_id", nullable = false, unique = true)
    private int id;

    @Column(name = "label", nullable = false, length = 30)
    private String label;

    @Column(name = "description", length = 1000)
    private String description;

    public GoalType() {
    }

    public GoalType(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
