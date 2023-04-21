package de.lot.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * A program goal holds one or more Learning-Outcomes and describe the
 * general goal behind a inspection of the outcome
 *
 * @see LearningOutcome
 *
 * @author Stefan Kohler <kohler.stefan@gmail.com>
 */
@Entity
@Table(name = "PROGRAMGOAL")
public class ProgramGoal implements Serializable {

    private Long id;

    private Integer number;

    private String description;

    private LoLanguage language;

    private Set<LearningOutcome> learningOutcomeList = new HashSet<LearningOutcome>(0);

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Length(max = 400)
    @NotNull
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "programGoal")
    public Set<LearningOutcome> getLearningOutcomeList() {
        return learningOutcomeList;
    }

    public void setLearningOutcomeList(Set<LearningOutcome> learningOutcomeList) {
        this.learningOutcomeList = learningOutcomeList;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LANGUAGE_ID", nullable = false)
    @NotNull
    public LoLanguage getLanguage() {
        return language;
    }

    public void setLanguage(LoLanguage language) {
        this.language = language;
    }

    @Transient
    public String getNumberDescription() {
        return number + " " + description;
    }
}
