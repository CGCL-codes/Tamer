package edu.unibi.agbi.biodwh.entity.intact;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Jan_Fuessmann
 * @version 1.0_2010
 */
@Entity(name = "intact_exp_confidence")
@Table(name = "intact_exp_confidence")
public class IntactExpConfidence implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1639779956182466955L;

    private Integer confidenceId;

    private IntactExp intactExp;

    private IntactXrefPrimary intactXrefPrimary;

    private String unitFullName;

    private String unitShortLabel;

    private String value;

    private Set<IntactExp> intactExps = new HashSet<IntactExp>(0);

    private Set<IntactExpConfidenceAttribute> intactExpConfidenceAttributes = new HashSet<IntactExpConfidenceAttribute>(0);

    private Set<IntactExpConfidenceUnitAlias> intactExpConfidenceUnitAliases = new HashSet<IntactExpConfidenceUnitAlias>(0);

    public IntactExpConfidence() {
    }

    public IntactExpConfidence(IntactExp intactExp) {
        this.intactExp = intactExp;
    }

    public IntactExpConfidence(IntactExp intactExp, IntactXrefPrimary intactXrefPrimary, String unitFullName, String unitShortLabel, String value, Set<IntactExp> intactExps, Set<IntactExpConfidenceAttribute> intactExpConfidenceAttributes, Set<IntactExpConfidenceUnitAlias> intactExpConfidenceUnitAliases) {
        this.intactExp = intactExp;
        this.intactXrefPrimary = intactXrefPrimary;
        this.unitFullName = unitFullName;
        this.unitShortLabel = unitShortLabel;
        this.value = value;
        this.intactExps = intactExps;
        this.intactExpConfidenceAttributes = intactExpConfidenceAttributes;
        this.intactExpConfidenceUnitAliases = intactExpConfidenceUnitAliases;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "confidence_id", nullable = false)
    public Integer getConfidenceId() {
        return this.confidenceId;
    }

    public void setConfidenceId(Integer confidenceId) {
        this.confidenceId = confidenceId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experiment_id", referencedColumnName = "experiment_id", nullable = false)
    public IntactExp getIntactExp() {
        return this.intactExp;
    }

    public void setIntactExp(IntactExp intactExp) {
        this.intactExp = intactExp;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_xref_id")
    public IntactXrefPrimary getIntactXrefPrimary() {
        return this.intactXrefPrimary;
    }

    public void setIntactXrefPrimary(IntactXrefPrimary intactXrefPrimary) {
        this.intactXrefPrimary = intactXrefPrimary;
    }

    @Column(name = "unit_full_name", length = 1000)
    public String getUnitFullName() {
        return this.unitFullName;
    }

    public void setUnitFullName(String unitFullName) {
        this.unitFullName = unitFullName;
    }

    @Column(name = "unit_short_label")
    public String getUnitShortLabel() {
        return this.unitShortLabel;
    }

    public void setUnitShortLabel(String unitShortLabel) {
        this.unitShortLabel = unitShortLabel;
    }

    @Column(name = "value")
    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "intact_exp_confidence_exp_ref", joinColumns = { @JoinColumn(name = "confidence_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "experiment_id", nullable = false, updatable = false) })
    public Set<IntactExp> getIntactExps() {
        return this.intactExps;
    }

    public void setIntactExps(Set<IntactExp> intactExps) {
        this.intactExps = intactExps;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "intactExpConfidence")
    public Set<IntactExpConfidenceAttribute> getIntactExpConfidenceAttributes() {
        return this.intactExpConfidenceAttributes;
    }

    public void setIntactExpConfidenceAttributes(Set<IntactExpConfidenceAttribute> intactExpConfidenceAttributes) {
        this.intactExpConfidenceAttributes = intactExpConfidenceAttributes;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "intactExpConfidence")
    public Set<IntactExpConfidenceUnitAlias> getIntactExpConfidenceUnitAliases() {
        return this.intactExpConfidenceUnitAliases;
    }

    public void setIntactExpConfidenceUnitAliases(Set<IntactExpConfidenceUnitAlias> intactExpConfidenceUnitAliases) {
        this.intactExpConfidenceUnitAliases = intactExpConfidenceUnitAliases;
    }
}
