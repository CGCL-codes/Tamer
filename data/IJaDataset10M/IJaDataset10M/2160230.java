package edu.univalle.lingweb.persistence;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AbstractCoTest entity provides the base persistence definition of the CoTest
 * entity.
 * 
 * @author LingWeb
 */
@MappedSuperclass
public abstract class AbstractCoTest implements java.io.Serializable {

    private Long testId;

    private CoTextType coTextType;

    private CoHability coHability;

    private CoTextualUnity coTextualUnity;

    private CoSequence coSequence;

    private CoParagraphType coParagraphType;

    private String testName;

    private String description;

    private Long studentWorkTime;

    private Long realTime;

    private Long item;

    private String suggestions;

    private Long exerciseAmount;

    private String materialEditor;

    private Date beginDate;

    private Date endDate;

    private String flagClone;

    private String flagForum;

    private String forumDescription;

    private String availableResourceTool;

    private Long testParentId;

    private Set<CoQuestion> coQuestions = new HashSet<CoQuestion>(0);

    private Set<AgStatusTest> agStatusTests = new HashSet<AgStatusTest>(0);

    private Set<CoMaterial> coMaterials = new HashSet<CoMaterial>(0);

    private Set<CoQuestionWeighted> coQuestionWeighteds = new HashSet<CoQuestionWeighted>(0);

    private Set<CoTestUserHistory> coTestUserHistories = new HashSet<CoTestUserHistory>(0);

    /** default constructor */
    public AbstractCoTest() {
    }

    /** minimal constructor */
    public AbstractCoTest(Long testId, CoHability coHability, CoSequence coSequence, String testName, Long studentWorkTime, Long item, Long exerciseAmount, Date beginDate, Date endDate) {
        this.testId = testId;
        this.coHability = coHability;
        this.coSequence = coSequence;
        this.testName = testName;
        this.studentWorkTime = studentWorkTime;
        this.item = item;
        this.exerciseAmount = exerciseAmount;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    /** full constructor */
    public AbstractCoTest(Long testId, CoTextType coTextType, CoHability coHability, CoTextualUnity coTextualUnity, CoSequence coSequence, CoParagraphType coParagraphType, String testName, String description, Long studentWorkTime, Long realTime, Long item, String suggestions, Long exerciseAmount, String materialEditor, Date beginDate, Date endDate, String flagClone, String flagForum, String forumDescription, String availableResourceTool, Long testParentId, Set<CoQuestion> coQuestions, Set<AgStatusTest> agStatusTests, Set<CoMaterial> coMaterials, Set<CoQuestionWeighted> coQuestionWeighteds, Set<CoTestUserHistory> coTestUserHistories) {
        this.testId = testId;
        this.coTextType = coTextType;
        this.coHability = coHability;
        this.coTextualUnity = coTextualUnity;
        this.coSequence = coSequence;
        this.coParagraphType = coParagraphType;
        this.testName = testName;
        this.description = description;
        this.studentWorkTime = studentWorkTime;
        this.realTime = realTime;
        this.item = item;
        this.suggestions = suggestions;
        this.exerciseAmount = exerciseAmount;
        this.materialEditor = materialEditor;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.flagClone = flagClone;
        this.flagForum = flagForum;
        this.forumDescription = forumDescription;
        this.availableResourceTool = availableResourceTool;
        this.testParentId = testParentId;
        this.coQuestions = coQuestions;
        this.agStatusTests = agStatusTests;
        this.coMaterials = coMaterials;
        this.coQuestionWeighteds = coQuestionWeighteds;
        this.coTestUserHistories = coTestUserHistories;
    }

    @Id
    @Column(name = "test_id", unique = true, nullable = false, insertable = true, updatable = true, precision = 15, scale = 0)
    public Long getTestId() {
        return this.testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "text_type_id", unique = false, nullable = true, insertable = true, updatable = true)
    public CoTextType getCoTextType() {
        return this.coTextType;
    }

    public void setCoTextType(CoTextType coTextType) {
        this.coTextType = coTextType;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "hability_id", unique = false, nullable = false, insertable = true, updatable = true)
    public CoHability getCoHability() {
        return this.coHability;
    }

    public void setCoHability(CoHability coHability) {
        this.coHability = coHability;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "textual_unity_id", unique = false, nullable = true, insertable = true, updatable = true)
    public CoTextualUnity getCoTextualUnity() {
        return this.coTextualUnity;
    }

    public void setCoTextualUnity(CoTextualUnity coTextualUnity) {
        this.coTextualUnity = coTextualUnity;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "sequence_id", unique = false, nullable = false, insertable = true, updatable = true)
    public CoSequence getCoSequence() {
        return this.coSequence;
    }

    public void setCoSequence(CoSequence coSequence) {
        this.coSequence = coSequence;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "paragraph_type_id", unique = false, nullable = true, insertable = true, updatable = true)
    public CoParagraphType getCoParagraphType() {
        return this.coParagraphType;
    }

    public void setCoParagraphType(CoParagraphType coParagraphType) {
        this.coParagraphType = coParagraphType;
    }

    @Column(name = "test_name", unique = false, nullable = false, insertable = true, updatable = true, length = 60)
    public String getTestName() {
        return this.testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    @Column(name = "description", unique = false, nullable = true, insertable = true, updatable = true)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "student_work_time", unique = false, nullable = false, insertable = true, updatable = true, precision = 4, scale = 0)
    public Long getStudentWorkTime() {
        return this.studentWorkTime;
    }

    public void setStudentWorkTime(Long studentWorkTime) {
        this.studentWorkTime = studentWorkTime;
    }

    @Column(name = "real_time", unique = false, nullable = true, insertable = true, updatable = true, precision = 4, scale = 0)
    public Long getRealTime() {
        return this.realTime;
    }

    public void setRealTime(Long realTime) {
        this.realTime = realTime;
    }

    @Column(name = "item", unique = false, nullable = false, insertable = true, updatable = true, precision = 4, scale = 0)
    public Long getItem() {
        return this.item;
    }

    public void setItem(Long item) {
        this.item = item;
    }

    @Column(name = "suggestions", unique = false, nullable = true, insertable = true, updatable = true)
    public String getSuggestions() {
        return this.suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }

    @Column(name = "exercise_amount", unique = false, nullable = false, insertable = true, updatable = true, precision = 2, scale = 0)
    public Long getExerciseAmount() {
        return this.exerciseAmount;
    }

    public void setExerciseAmount(Long exerciseAmount) {
        this.exerciseAmount = exerciseAmount;
    }

    @Column(name = "material_editor", unique = false, nullable = true, insertable = true, updatable = true)
    public String getMaterialEditor() {
        return this.materialEditor;
    }

    public void setMaterialEditor(String materialEditor) {
        this.materialEditor = materialEditor;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "begin_date", unique = false, nullable = false, insertable = true, updatable = true, length = 29)
    public Date getBeginDate() {
        return this.beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date", unique = false, nullable = false, insertable = true, updatable = true, length = 29)
    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column(name = "flag_clone", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
    public String getFlagClone() {
        return this.flagClone;
    }

    public void setFlagClone(String flagClone) {
        this.flagClone = flagClone;
    }

    @Column(name = "flag_forum", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
    public String getFlagForum() {
        return this.flagForum;
    }

    public void setFlagForum(String flagForum) {
        this.flagForum = flagForum;
    }

    @Column(name = "forum_description", unique = false, nullable = true, insertable = true, updatable = true, length = 2000)
    public String getForumDescription() {
        return this.forumDescription;
    }

    public void setForumDescription(String forumDescription) {
        this.forumDescription = forumDescription;
    }

    @Column(name = "available_resource_tool", unique = false, nullable = true, insertable = true, updatable = true, length = 100)
    public String getAvailableResourceTool() {
        return this.availableResourceTool;
    }

    public void setAvailableResourceTool(String availableResourceTool) {
        this.availableResourceTool = availableResourceTool;
    }

    @Column(name = "test_parent_id", unique = false, nullable = true, insertable = true, updatable = true, precision = 15, scale = 0)
    public Long getTestParentId() {
        return this.testParentId;
    }

    public void setTestParentId(Long testParentId) {
        this.testParentId = testParentId;
    }

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coTest")
    public Set<CoQuestion> getCoQuestions() {
        return this.coQuestions;
    }

    public void setCoQuestions(Set<CoQuestion> coQuestions) {
        this.coQuestions = coQuestions;
    }

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coTest")
    public Set<AgStatusTest> getAgStatusTests() {
        return this.agStatusTests;
    }

    public void setAgStatusTests(Set<AgStatusTest> agStatusTests) {
        this.agStatusTests = agStatusTests;
    }

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(name = "co_test_material", schema = "public", joinColumns = { @JoinColumn(name = "test_id", unique = false, nullable = false, insertable = true, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "material_id", unique = false, nullable = false, insertable = true, updatable = false) })
    public Set<CoMaterial> getCoMaterials() {
        return this.coMaterials;
    }

    public void setCoMaterials(Set<CoMaterial> coMaterials) {
        this.coMaterials = coMaterials;
    }

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coTest")
    public Set<CoQuestionWeighted> getCoQuestionWeighteds() {
        return this.coQuestionWeighteds;
    }

    public void setCoQuestionWeighteds(Set<CoQuestionWeighted> coQuestionWeighteds) {
        this.coQuestionWeighteds = coQuestionWeighteds;
    }

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coTest")
    public Set<CoTestUserHistory> getCoTestUserHistories() {
        return this.coTestUserHistories;
    }

    public void setCoTestUserHistories(Set<CoTestUserHistory> coTestUserHistories) {
        this.coTestUserHistories = coTestUserHistories;
    }
}
