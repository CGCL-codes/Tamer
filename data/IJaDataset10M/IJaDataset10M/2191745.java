package edu.unibi.agbi.biodwh.entity.intact;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Entity(name = "intact_intact")
@Table(name = "intact_intact")
public class IntactIntact implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2528008025969544349L;

    private String id;

    private IntactXrefPrimary intactXrefPrimary;

    private String intactEntry;

    private Integer availabilityRef;

    private String avilability;

    private String fullName;

    private String imaxId;

    private String intraMolecular;

    private String modelled;

    private String negative;

    private String shortLabel;

    private Set<IntactParti> intactPartis = new HashSet<IntactParti>(0);

    private Set<IntactIntactAlias> intactIntactAliases = new HashSet<IntactIntactAlias>(0);

    private Set<IntactIntactType> intactIntactTypes = new HashSet<IntactIntactType>(0);

    private Set<IntactIntactParameter> intactIntactParameters = new HashSet<IntactIntactParameter>(0);

    private Set<IntactParti> intactPartis_1 = new HashSet<IntactParti>(0);

    private Set<IntactIntactConfidence> intactIntactConfidences = new HashSet<IntactIntactConfidence>(0);

    private Set<IntactIntactAttribute> intactIntactAttributes = new HashSet<IntactIntactAttribute>(0);

    private Set<IntactParti> intactPartiesByInferredInteraction = new HashSet<IntactParti>(0);

    private Integer intactId;

    private Set<IntactPartiFeature> intactPartiFeaturesByInferredInteraction = new HashSet<IntactPartiFeature>(0);

    private Set<IntactExp> intactExpByInferredInteraction = new HashSet<IntactExp>(0);

    private Set<IntactExp> intactExp = new HashSet<IntactExp>(0);

    public IntactIntact() {
    }

    public IntactIntact(String id, String intactEntry) {
        this.id = id;
    }

    public IntactIntact(Integer intactId, String id, IntactXrefPrimary intactXrefPrimary, String intactEntry, Integer availabilityRef, String avilability, String fullName, String imaxId, String intraMolecular, String modelled, String negative, String shortLabel, Set<IntactParti> intactPartis, Set<IntactIntactAlias> intactIntactAliases, Set<IntactIntactType> intactIntactTypes, Set<IntactIntactParameter> intactIntactParameters, Set<IntactParti> intactPartis_1, Set<IntactIntactConfidence> intactIntactConfidences, Set<IntactIntactAttribute> intactIntactAttributes, Set<IntactParti> intactPartiesByInferredInteraction, Set<IntactPartiFeature> intactPartiFeaturesByInferredInteraction, Set<IntactExp> intactExpByInferredInteraction, Set<IntactExp> intactExp) {
        this.id = id;
        this.intactId = intactId;
        this.intactXrefPrimary = intactXrefPrimary;
        this.intactEntry = intactEntry;
        this.availabilityRef = availabilityRef;
        this.avilability = avilability;
        this.fullName = fullName;
        this.imaxId = imaxId;
        this.intraMolecular = intraMolecular;
        this.modelled = modelled;
        this.negative = negative;
        this.shortLabel = shortLabel;
        this.intactPartis = intactPartis;
        this.intactIntactAliases = intactIntactAliases;
        this.intactIntactTypes = intactIntactTypes;
        this.intactIntactParameters = intactIntactParameters;
        this.intactPartis_1 = intactPartis_1;
        this.intactIntactConfidences = intactIntactConfidences;
        this.intactIntactAttributes = intactIntactAttributes;
        this.intactPartiesByInferredInteraction = intactPartiesByInferredInteraction;
        this.intactPartiFeaturesByInferredInteraction = intactPartiFeaturesByInferredInteraction;
        this.intactExpByInferredInteraction = intactExpByInferredInteraction;
        this.intactExp = intactExp;
    }

    @Id
    @Column(name = "interaction_id", nullable = false)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "xml_internal_id", nullable = false)
    public Integer getIntactId() {
        return this.intactId;
    }

    public void setIntactId(Integer intactId) {
        this.intactId = intactId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "xref_id")
    public IntactXrefPrimary getIntactXrefPrimary() {
        return this.intactXrefPrimary;
    }

    public void setIntactXrefPrimary(IntactXrefPrimary intactXrefPrimary) {
        this.intactXrefPrimary = intactXrefPrimary;
    }

    @Column(name = "entry_id", nullable = false)
    @org.hibernate.annotations.Index(name = "idx_intact_intact_entry_id")
    public String getIntactEntry() {
        return this.intactEntry;
    }

    public void setIntactEntry(String intactEntry) {
        this.intactEntry = intactEntry;
    }

    @Column(name = "availability_ref")
    public Integer getAvailabilityRef() {
        return this.availabilityRef;
    }

    public void setAvailabilityRef(Integer availabilityRef) {
        this.availabilityRef = availabilityRef;
    }

    @Column(name = "avilability", length = 45)
    public String getAvilability() {
        return this.avilability;
    }

    public void setAvilability(String avilability) {
        this.avilability = avilability;
    }

    @Column(name = "full_name", length = 255)
    @org.hibernate.annotations.Index(name = "idx_intact_intact_full_name")
    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Column(name = "imax_id", length = 45)
    public String getImaxId() {
        return this.imaxId;
    }

    public void setImaxId(String imaxId) {
        this.imaxId = imaxId;
    }

    @Column(name = "intra_molecular", length = 10)
    public String getIntraMolecular() {
        return this.intraMolecular;
    }

    public void setIntraMolecular(String intraMolecular) {
        this.intraMolecular = intraMolecular;
    }

    @Column(name = "modelled", length = 10)
    public String getModelled() {
        return this.modelled;
    }

    public void setModelled(String modelled) {
        this.modelled = modelled;
    }

    @Column(name = "negative", length = 10)
    public String getNegative() {
        return this.negative;
    }

    public void setNegative(String negative) {
        this.negative = negative;
    }

    @Column(name = "short_label")
    @org.hibernate.annotations.Index(name = "idx_intact_intact_short_lable")
    public String getShortLabel() {
        return this.shortLabel;
    }

    public void setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "intact_intact_parti", joinColumns = { @JoinColumn(name = "interaction_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "participant_id", nullable = false, updatable = false) })
    public Set<IntactParti> getIntactPartis() {
        return this.intactPartis;
    }

    public void setIntactPartis(Set<IntactParti> intactPartis) {
        this.intactPartis = intactPartis;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "intact_intact_inferredintact_parti", joinColumns = { @JoinColumn(name = "interaction_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "participant_id", nullable = false, updatable = false) })
    public Set<IntactParti> getIntactPartiesByInferredInteraction() {
        return this.intactPartiesByInferredInteraction;
    }

    public void setIntactPartiesByInferredInteraction(Set<IntactParti> intactPartiesByInferredInteraction) {
        this.intactPartiesByInferredInteraction = intactPartiesByInferredInteraction;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "intact_intact_inferredintact_feature", joinColumns = { @JoinColumn(name = "interaction_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "feature_id", nullable = false, updatable = false) })
    public Set<IntactPartiFeature> getIntactPartiFeaturesByInferredInteraction() {
        return this.intactPartiFeaturesByInferredInteraction;
    }

    public void setIntactPartiFeaturesByInferredInteraction(Set<IntactPartiFeature> intactPartiFeaturesByInferredInteraction) {
        this.intactPartiFeaturesByInferredInteraction = intactPartiFeaturesByInferredInteraction;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "intact_intact_inferredintact_exp_ref", joinColumns = { @JoinColumn(name = "interaction_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "experiment_id", nullable = false, updatable = false) })
    public Set<IntactExp> getIntactExpByInferredInteraction() {
        return this.intactExpByInferredInteraction;
    }

    public void setIntactExpByInferredInteraction(Set<IntactExp> intactExpByInferredInteraction) {
        this.intactExpByInferredInteraction = intactExpByInferredInteraction;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "intact_intact_exp", joinColumns = { @JoinColumn(name = "interaction_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "experiment_id", nullable = false, updatable = false) })
    public Set<IntactExp> getIntactExp() {
        return this.intactExp;
    }

    public void setIntactExp(Set<IntactExp> intactExp) {
        this.intactExp = intactExp;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "intactIntact")
    public Set<IntactIntactAlias> getIntactIntactAliases() {
        return this.intactIntactAliases;
    }

    public void setIntactIntactAliases(Set<IntactIntactAlias> intactIntactAliases) {
        this.intactIntactAliases = intactIntactAliases;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "intactIntact")
    public Set<IntactIntactType> getIntactIntactTypes() {
        return this.intactIntactTypes;
    }

    public void setIntactIntactTypes(Set<IntactIntactType> intactIntactTypes) {
        this.intactIntactTypes = intactIntactTypes;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "intactIntact")
    public Set<IntactIntactParameter> getIntactIntactParameters() {
        return this.intactIntactParameters;
    }

    public void setIntactIntactParameters(Set<IntactIntactParameter> intactIntactParameters) {
        this.intactIntactParameters = intactIntactParameters;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "intactIntact")
    public Set<IntactParti> getIntactPartis_1() {
        return this.intactPartis_1;
    }

    public void setIntactPartis_1(Set<IntactParti> intactPartis_1) {
        this.intactPartis_1 = intactPartis_1;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "intactIntact")
    public Set<IntactIntactConfidence> getIntactIntactConfidences() {
        return this.intactIntactConfidences;
    }

    public void setIntactIntactConfidences(Set<IntactIntactConfidence> intactIntactConfidences) {
        this.intactIntactConfidences = intactIntactConfidences;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "intactIntact")
    public Set<IntactIntactAttribute> getIntactIntactAttributes() {
        return this.intactIntactAttributes;
    }

    public void setIntactIntactAttributes(Set<IntactIntactAttribute> intactIntactAttributes) {
        this.intactIntactAttributes = intactIntactAttributes;
    }
}
