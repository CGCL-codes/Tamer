package com.firescrum.testmodule.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import com.firescrum.infrastructure.model.PersistentEntity;

/**
 * TestScript represents the test script of a test case.
 * @author andersonfellipe
 */
@Entity
public class TestScript extends PersistentEntity {

    /**
     * Represents the serial version of the entity.
     */
    private static final long serialVersionUID = -8711603186581257625L;

    /**
     * Represents the unique identifier of the test script.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Represents the version of the test script. Must be
     * updated always anything in the test script is changed.
     */
    private Integer version;

    /**
     * Represents additional notes of the test script.
     */
    private String notes;

    /**
     * Represents the initial conditions to execute the test script.
     */
    @Column(nullable = false)
    private String initialConditions;

    /**
     * Represents the list of steps of the test script.
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "testscriptid")
    private List<Step> steps;

    /**
     * Creates a new instance of the test script, with the default values.
     */
    public TestScript() {
        super();
    }

    /**
     * Get the id value.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Assign the value of the field id.
     *
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the version value.
     *
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * Assign the value of the field version.
     *
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Get the notes value.
     *
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Assign the value of the field notes.
     *
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Get the initialConditions value.
     *
     * @return the initialConditions
     */
    public String getInitialConditions() {
        return initialConditions;
    }

    /**
     * Assign the value of the field initialConditions.
     *
     * @param initialConditions the initialConditions to set
     */
    public void setInitialConditions(String initialConditions) {
        this.initialConditions = initialConditions;
    }

    /**
     * Get the steps value.
     *
     * @return the steps
     */
    public List<Step> getSteps() {
        return steps;
    }

    /**
     * Assign the value of the field steps.
     *
     * @param steps the steps to set
     */
    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}
