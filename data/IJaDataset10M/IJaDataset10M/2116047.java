package edu.univalle.lingweb.persistence;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

/**
 * AbstractCoMetacognitiveVariable entity provides the base persistence
 * definition of the CoMetacognitiveVariable entity.
 * 
 * @author LingWeb
 */
@MappedSuperclass
public abstract class AbstractCoMetacognitiveVariable implements java.io.Serializable {

    private Long metacognitiveVariableId;

    private String metacognitiveVariableName;

    private String metacognitiveVariableNameEn;

    private String metacognitiveVariableNameFr;

    private Set<CoExercises2> coExercises2s = new HashSet<CoExercises2>(0);

    private Set<CoStrategyMomentumDes> coStrategyMomentumDeses = new HashSet<CoStrategyMomentumDes>(0);

    private Set<CoStrategyPhaseDes> coStrategyPhaseDeses = new HashSet<CoStrategyPhaseDes>(0);

    /** default constructor */
    public AbstractCoMetacognitiveVariable() {
    }

    /** minimal constructor */
    public AbstractCoMetacognitiveVariable(Long metacognitiveVariableId, String metacognitiveVariableName, String metacognitiveVariableNameEn, String metacognitiveVariableNameFr) {
        this.metacognitiveVariableId = metacognitiveVariableId;
        this.metacognitiveVariableName = metacognitiveVariableName;
        this.metacognitiveVariableNameEn = metacognitiveVariableNameEn;
        this.metacognitiveVariableNameFr = metacognitiveVariableNameFr;
    }

    /** full constructor */
    public AbstractCoMetacognitiveVariable(Long metacognitiveVariableId, String metacognitiveVariableName, String metacognitiveVariableNameEn, String metacognitiveVariableNameFr, Set<CoExercises2> coExercises2s, Set<CoStrategyMomentumDes> coStrategyMomentumDeses, Set<CoStrategyPhaseDes> coStrategyPhaseDeses) {
        this.metacognitiveVariableId = metacognitiveVariableId;
        this.metacognitiveVariableName = metacognitiveVariableName;
        this.metacognitiveVariableNameEn = metacognitiveVariableNameEn;
        this.metacognitiveVariableNameFr = metacognitiveVariableNameFr;
        this.coExercises2s = coExercises2s;
        this.coStrategyMomentumDeses = coStrategyMomentumDeses;
        this.coStrategyPhaseDeses = coStrategyPhaseDeses;
    }

    @Id
    @Column(name = "metacognitive_variable_id", unique = true, nullable = false, insertable = true, updatable = true, precision = 15, scale = 0)
    public Long getMetacognitiveVariableId() {
        return this.metacognitiveVariableId;
    }

    public void setMetacognitiveVariableId(Long metacognitiveVariableId) {
        this.metacognitiveVariableId = metacognitiveVariableId;
    }

    @Column(name = "metacognitive_variable_name", unique = false, nullable = false, insertable = true, updatable = true, length = 60)
    public String getMetacognitiveVariableName() {
        return this.metacognitiveVariableName;
    }

    public void setMetacognitiveVariableName(String metacognitiveVariableName) {
        this.metacognitiveVariableName = metacognitiveVariableName;
    }

    @Column(name = "metacognitive_variable_name_en", unique = false, nullable = false, insertable = true, updatable = true, length = 60)
    public String getMetacognitiveVariableNameEn() {
        return this.metacognitiveVariableNameEn;
    }

    public void setMetacognitiveVariableNameEn(String metacognitiveVariableNameEn) {
        this.metacognitiveVariableNameEn = metacognitiveVariableNameEn;
    }

    @Column(name = "metacognitive_variable_name_fr", unique = false, nullable = false, insertable = true, updatable = true, length = 60)
    public String getMetacognitiveVariableNameFr() {
        return this.metacognitiveVariableNameFr;
    }

    public void setMetacognitiveVariableNameFr(String metacognitiveVariableNameFr) {
        this.metacognitiveVariableNameFr = metacognitiveVariableNameFr;
    }

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coMetacognitiveVariable")
    public Set<CoExercises2> getCoExercises2s() {
        return this.coExercises2s;
    }

    public void setCoExercises2s(Set<CoExercises2> coExercises2s) {
        this.coExercises2s = coExercises2s;
    }

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coMetacognitiveVariable")
    public Set<CoStrategyMomentumDes> getCoStrategyMomentumDeses() {
        return this.coStrategyMomentumDeses;
    }

    public void setCoStrategyMomentumDeses(Set<CoStrategyMomentumDes> coStrategyMomentumDeses) {
        this.coStrategyMomentumDeses = coStrategyMomentumDeses;
    }

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coMetacognitiveVariable")
    public Set<CoStrategyPhaseDes> getCoStrategyPhaseDeses() {
        return this.coStrategyPhaseDeses;
    }

    public void setCoStrategyPhaseDeses(Set<CoStrategyPhaseDes> coStrategyPhaseDeses) {
        this.coStrategyPhaseDeses = coStrategyPhaseDeses;
    }
}
