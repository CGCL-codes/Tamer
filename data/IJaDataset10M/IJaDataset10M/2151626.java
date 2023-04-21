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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Jan_Fuessmann
 * @version 1.0_2010
 */
@Entity(name = "intact_parti_feature")
@Table(name = "intact_parti_feature")
public class IntactPartiFeature implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4595273648926446826L;

    private Integer featureId;

    private Integer id;

    private IntactXrefPrimary intactXrefPrimaryByDetectMethodXrefId;

    private IntactXrefPrimary intactXrefPrimaryByTypeXrefId;

    private IntactXrefPrimary intactXrefPrimaryByXrefId;

    private IntactParti intactParti;

    private String fullName;

    private String shortLabel;

    private String typeFullName;

    private String typeShortLabel;

    private String detectMethodShortLabel;

    private String detectMethodFullName;

    private Set<IntactPartiFeatureDetectMethodAlias> intactPartiFeatureDetectMethodAliases = new HashSet<IntactPartiFeatureDetectMethodAlias>(0);

    private Set<IntactPartiFeatureTypeAlias> intactPartiFeatureTypeAliases = new HashSet<IntactPartiFeatureTypeAlias>(0);

    private Set<IntactPartiFeatureAttribute> intactPartiFeatureAttributes = new HashSet<IntactPartiFeatureAttribute>(0);

    private Set<IntactPartiFeatureExpRef> intactPartiFeatureExpRefs = new HashSet<IntactPartiFeatureExpRef>(0);

    private Set<IntactPartiFeatureAlias> intactPartiFeatureAliases = new HashSet<IntactPartiFeatureAlias>(0);

    private IntactXrefPrimary intactXrefPrimaryByEndStatusXrefId;

    private IntactXrefPrimary intactXrefPrimaryByStratStatusXrefId;

    private Long beginIntervalBegin;

    private Long beginIntervalEnd;

    private Long beginPosition;

    private Long endIntervalBegin;

    private Long endIntervalEnd;

    private Long endPosition;

    private String endStatusFullName;

    private String endStatusShortLabel;

    private String isLink;

    private String startStatusFullName;

    private String startStatusShortLabel;

    private Set<IntactPartiFeatureRangeEndStatusAlias> intactPartiFeatureRangeEndStatusAliases = new HashSet<IntactPartiFeatureRangeEndStatusAlias>(0);

    private Set<IntactPartiFeatureRangeStartStatusAlias> intactPartiFeatureRangeStartStatusAliases = new HashSet<IntactPartiFeatureRangeStartStatusAlias>(0);

    private Set<IntactIntact> intactIntactbyInferredInteraction = new HashSet<IntactIntact>(0);

    public IntactPartiFeature() {
    }

    public IntactPartiFeature(Integer featureId, IntactParti intactParti) {
        this.featureId = featureId;
    }

    public IntactPartiFeature(Integer featureId, Integer id, IntactXrefPrimary intactXrefPrimaryByDetectMethodXrefId, IntactXrefPrimary intactXrefPrimaryByTypeXrefId, IntactXrefPrimary intactXrefPrimaryByXrefId, IntactParti intactParti, String fullName, String shortLabel, String typeFullName, String typeShortLabel, String detectMethodShortLabel, String detectMethodFullName, Set<IntactPartiFeatureDetectMethodAlias> intactPartiFeatureDetectMethodAliases, Set<IntactPartiFeatureTypeAlias> intactPartiFeatureTypeAliases, Set<IntactPartiFeatureAttribute> intactPartiFeatureAttributes, Set<IntactPartiFeatureExpRef> intactPartiFeatureExpRefs, Set<IntactPartiFeatureAlias> intactPartiFeatureAliases, Set<IntactIntact> intactIntactbyInferredInteraction, IntactXrefPrimary intactXrefPrimaryByEndStatusXrefId, IntactXrefPrimary intactXrefPrimaryByStratStatusXrefId, Long beginIntervalBegin, Long beginIntervalEnd, Long beginPosition, Long endIntervalBegin, Long endIntervalEnd, Long endPosition, String endStatusFullName, String endStatusShortLabel, String isLink, String startStatusFullName, String startStatusShortLabel, Set<IntactPartiFeatureRangeEndStatusAlias> intactPartiFeatureRangeEndStatusAliases, Set<IntactPartiFeatureRangeStartStatusAlias> intactPartiFeatureRangeStartStatusAliases) {
        this.featureId = featureId;
        this.id = id;
        this.intactXrefPrimaryByDetectMethodXrefId = intactXrefPrimaryByDetectMethodXrefId;
        this.intactXrefPrimaryByTypeXrefId = intactXrefPrimaryByTypeXrefId;
        this.intactXrefPrimaryByXrefId = intactXrefPrimaryByXrefId;
        this.intactParti = intactParti;
        this.fullName = fullName;
        this.shortLabel = shortLabel;
        this.typeFullName = typeFullName;
        this.typeShortLabel = typeShortLabel;
        this.detectMethodShortLabel = detectMethodShortLabel;
        this.detectMethodFullName = detectMethodFullName;
        this.intactPartiFeatureDetectMethodAliases = intactPartiFeatureDetectMethodAliases;
        this.intactPartiFeatureTypeAliases = intactPartiFeatureTypeAliases;
        this.intactPartiFeatureAttributes = intactPartiFeatureAttributes;
        this.intactPartiFeatureExpRefs = intactPartiFeatureExpRefs;
        this.intactPartiFeatureAliases = intactPartiFeatureAliases;
        this.intactIntactbyInferredInteraction = intactIntactbyInferredInteraction;
        this.intactXrefPrimaryByEndStatusXrefId = intactXrefPrimaryByEndStatusXrefId;
        this.intactXrefPrimaryByStratStatusXrefId = intactXrefPrimaryByStratStatusXrefId;
        this.beginIntervalBegin = beginIntervalBegin;
        this.beginIntervalEnd = beginIntervalEnd;
        this.beginPosition = beginPosition;
        this.endIntervalBegin = endIntervalBegin;
        this.endIntervalEnd = endIntervalEnd;
        this.endPosition = endPosition;
        this.endStatusFullName = endStatusFullName;
        this.endStatusShortLabel = endStatusShortLabel;
        this.isLink = isLink;
        this.startStatusFullName = startStatusFullName;
        this.startStatusShortLabel = startStatusShortLabel;
        this.intactPartiFeatureRangeEndStatusAliases = intactPartiFeatureRangeEndStatusAliases;
        this.intactPartiFeatureRangeStartStatusAliases = intactPartiFeatureRangeStartStatusAliases;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "feature_id", nullable = false)
    public Integer getFeatureId() {
        return this.featureId;
    }

    public void setFeatureId(Integer featureId) {
        this.featureId = featureId;
    }

    @Column(name = "xml_internal_id", nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detect_method_xref_id")
    public IntactXrefPrimary getIntactXrefPrimaryByDetectMethodXrefId() {
        return this.intactXrefPrimaryByDetectMethodXrefId;
    }

    public void setIntactXrefPrimaryByDetectMethodXrefId(IntactXrefPrimary intactXrefPrimaryByDetectMethodXrefId) {
        this.intactXrefPrimaryByDetectMethodXrefId = intactXrefPrimaryByDetectMethodXrefId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_xref_id")
    public IntactXrefPrimary getIntactXrefPrimaryByTypeXrefId() {
        return this.intactXrefPrimaryByTypeXrefId;
    }

    public void setIntactXrefPrimaryByTypeXrefId(IntactXrefPrimary intactXrefPrimaryByTypeXrefId) {
        this.intactXrefPrimaryByTypeXrefId = intactXrefPrimaryByTypeXrefId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "xref_id")
    public IntactXrefPrimary getIntactXrefPrimaryByXrefId() {
        return this.intactXrefPrimaryByXrefId;
    }

    public void setIntactXrefPrimaryByXrefId(IntactXrefPrimary intactXrefPrimaryByXrefId) {
        this.intactXrefPrimaryByXrefId = intactXrefPrimaryByXrefId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    public IntactParti getIntactParti() {
        return this.intactParti;
    }

    public void setIntactParti(IntactParti intactParti) {
        this.intactParti = intactParti;
    }

    @Column(name = "full_name", length = 255)
    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Column(name = "short_label")
    public String getShortLabel() {
        return this.shortLabel;
    }

    public void setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
    }

    @Column(name = "type_full_name", length = 255)
    public String getTypeFullName() {
        return this.typeFullName;
    }

    public void setTypeFullName(String typeFullName) {
        this.typeFullName = typeFullName;
    }

    @Column(name = "type_short_label")
    public String getTypeShortLabel() {
        return this.typeShortLabel;
    }

    public void setTypeShortLabel(String typeShortLabel) {
        this.typeShortLabel = typeShortLabel;
    }

    @Column(name = "detect_method_short_label")
    public String getDetectMethodShortLabel() {
        return this.detectMethodShortLabel;
    }

    public void setDetectMethodShortLabel(String detectMethodShortLabel) {
        this.detectMethodShortLabel = detectMethodShortLabel;
    }

    @Column(name = "detect_method_full_name", length = 1000)
    public String getDetectMethodFullName() {
        return this.detectMethodFullName;
    }

    public void setDetectMethodFullName(String detectMethodFullName) {
        this.detectMethodFullName = detectMethodFullName;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "intactPartiFeature")
    public Set<IntactPartiFeatureDetectMethodAlias> getIntactPartiFeatureDetectMethodAliases() {
        return this.intactPartiFeatureDetectMethodAliases;
    }

    public void setIntactPartiFeatureDetectMethodAliases(Set<IntactPartiFeatureDetectMethodAlias> intactPartiFeatureDetectMethodAliases) {
        this.intactPartiFeatureDetectMethodAliases = intactPartiFeatureDetectMethodAliases;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "intactPartiFeature")
    public Set<IntactPartiFeatureTypeAlias> getIntactPartiFeatureTypeAliases() {
        return this.intactPartiFeatureTypeAliases;
    }

    public void setIntactPartiFeatureTypeAliases(Set<IntactPartiFeatureTypeAlias> intactPartiFeatureTypeAliases) {
        this.intactPartiFeatureTypeAliases = intactPartiFeatureTypeAliases;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "intactPartiFeature")
    public Set<IntactPartiFeatureAttribute> getIntactPartiFeatureAttributes() {
        return this.intactPartiFeatureAttributes;
    }

    public void setIntactPartiFeatureAttributes(Set<IntactPartiFeatureAttribute> intactPartiFeatureAttributes) {
        this.intactPartiFeatureAttributes = intactPartiFeatureAttributes;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "intactPartiFeature")
    public Set<IntactPartiFeatureExpRef> getIntactPartiFeatureExpRefs() {
        return this.intactPartiFeatureExpRefs;
    }

    public void setIntactPartiFeatureExpRefs(Set<IntactPartiFeatureExpRef> intactPartiFeatureExpRefs) {
        this.intactPartiFeatureExpRefs = intactPartiFeatureExpRefs;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "intactPartiFeature")
    public Set<IntactPartiFeatureAlias> getIntactPartiFeatureAliases() {
        return this.intactPartiFeatureAliases;
    }

    public void setIntactPartiFeatureAliases(Set<IntactPartiFeatureAlias> intactPartiFeatureAliases) {
        this.intactPartiFeatureAliases = intactPartiFeatureAliases;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "range_end_status_xref_id")
    public IntactXrefPrimary getIntactXrefPrimaryByEndStatusXrefId() {
        return this.intactXrefPrimaryByEndStatusXrefId;
    }

    public void setIntactXrefPrimaryByEndStatusXrefId(IntactXrefPrimary intactXrefPrimaryByEndStatusXrefId) {
        this.intactXrefPrimaryByEndStatusXrefId = intactXrefPrimaryByEndStatusXrefId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "range_strat_status_xref_id")
    public IntactXrefPrimary getIntactXrefPrimaryByStratStatusXrefId() {
        return this.intactXrefPrimaryByStratStatusXrefId;
    }

    public void setIntactXrefPrimaryByStratStatusXrefId(IntactXrefPrimary intactXrefPrimaryByStratStatusXrefId) {
        this.intactXrefPrimaryByStratStatusXrefId = intactXrefPrimaryByStratStatusXrefId;
    }

    @Column(name = "range_begin_interval_begin")
    public Long getBeginIntervalBegin() {
        return this.beginIntervalBegin;
    }

    public void setBeginIntervalBegin(Long beginIntervalBegin) {
        this.beginIntervalBegin = beginIntervalBegin;
    }

    @Column(name = "range_begin_interval_end")
    public Long getBeginIntervalEnd() {
        return this.beginIntervalEnd;
    }

    public void setBeginIntervalEnd(Long beginIntervalEnd) {
        this.beginIntervalEnd = beginIntervalEnd;
    }

    @Column(name = "range_begin_position")
    public Long getBeginPosition() {
        return this.beginPosition;
    }

    public void setBeginPosition(Long beginPosition) {
        this.beginPosition = beginPosition;
    }

    @Column(name = "range_end_interval_begin")
    public Long getEndIntervalBegin() {
        return this.endIntervalBegin;
    }

    public void setEndIntervalBegin(Long endIntervalBegin) {
        this.endIntervalBegin = endIntervalBegin;
    }

    @Column(name = "range_end_interval_end")
    public Long getEndIntervalEnd() {
        return this.endIntervalEnd;
    }

    public void setEndIntervalEnd(Long endIntervalEnd) {
        this.endIntervalEnd = endIntervalEnd;
    }

    @Column(name = "range_end_position")
    public Long getEndPosition() {
        return this.endPosition;
    }

    public void setEndPosition(Long endPosition) {
        this.endPosition = endPosition;
    }

    @Column(name = "range_end_status_full_name", length = 255)
    public String getEndStatusFullName() {
        return this.endStatusFullName;
    }

    public void setEndStatusFullName(String endStatusFullName) {
        this.endStatusFullName = endStatusFullName;
    }

    @Column(name = "range_end_status_short_label")
    public String getEndStatusShortLabel() {
        return this.endStatusShortLabel;
    }

    public void setEndStatusShortLabel(String endStatusShortLabel) {
        this.endStatusShortLabel = endStatusShortLabel;
    }

    @Column(name = "range_is_link", length = 10)
    public String getIsLink() {
        return this.isLink;
    }

    public void setIsLink(String isLink) {
        this.isLink = isLink;
    }

    @Column(name = "range_start_status_full_name", length = 255)
    public String getStartStatusFullName() {
        return this.startStatusFullName;
    }

    public void setStartStatusFullName(String startStatusFullName) {
        this.startStatusFullName = startStatusFullName;
    }

    @Column(name = "range_start_status_short_label")
    public String getStartStatusShortLabel() {
        return this.startStatusShortLabel;
    }

    public void setStartStatusShortLabel(String startStatusShortLabel) {
        this.startStatusShortLabel = startStatusShortLabel;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "intactPartiFeatureRange")
    public Set<IntactPartiFeatureRangeEndStatusAlias> getIntactPartiFeatureRangeEndStatusAliases() {
        return this.intactPartiFeatureRangeEndStatusAliases;
    }

    public void setIntactPartiFeatureRangeEndStatusAliases(Set<IntactPartiFeatureRangeEndStatusAlias> intactPartiFeatureRangeEndStatusAliases) {
        this.intactPartiFeatureRangeEndStatusAliases = intactPartiFeatureRangeEndStatusAliases;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "intactPartiFeatureRange")
    public Set<IntactPartiFeatureRangeStartStatusAlias> getIntactPartiFeatureRangeStartStatusAliases() {
        return this.intactPartiFeatureRangeStartStatusAliases;
    }

    public void setIntactPartiFeatureRangeStartStatusAliases(Set<IntactPartiFeatureRangeStartStatusAlias> intactPartiFeatureRangeStartStatusAliases) {
        this.intactPartiFeatureRangeStartStatusAliases = intactPartiFeatureRangeStartStatusAliases;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "intactPartiFeaturesByInferredInteraction")
    public Set<IntactIntact> getIntactIntactbyInferredInteraction() {
        return this.intactIntactbyInferredInteraction;
    }

    public void setIntactIntactbyInferredInteraction(Set<IntactIntact> intactIntactbyInferredInteraction) {
        this.intactIntactbyInferredInteraction = intactIntactbyInferredInteraction;
    }
}
