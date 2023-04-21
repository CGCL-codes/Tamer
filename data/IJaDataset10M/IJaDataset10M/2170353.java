package edu.univalle.lingweb.persistence;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

/**
 * AbstractCoSingleTextTeacher3 entity provides the base persistence definition
 * of the CoSingleTextTeacher3 entity.
 * 
 * @author LingWeb
 */
@MappedSuperclass
public abstract class AbstractCoSingleTextTeacher3 implements java.io.Serializable {

    private Long singleTextTeacherId;

    private MaSingleTextCheckList maSingleTextCheckList;

    private MaSingleTextForm maSingleTextForm;

    private CoQuestion coQuestion;

    private String explainText;

    private String editorText;

    private Long numRows;

    private Long sizeY;

    private Long sizeX;

    private String titleText;

    private String flagSpellchecker;

    private String flagSyntax;

    private String flagAgent;

    private Set<CoSingleText3> coSingleText3s = new HashSet<CoSingleText3>(0);

    /** default constructor */
    public AbstractCoSingleTextTeacher3() {
    }

    /** minimal constructor */
    public AbstractCoSingleTextTeacher3(Long singleTextTeacherId) {
        this.singleTextTeacherId = singleTextTeacherId;
    }

    /** full constructor */
    public AbstractCoSingleTextTeacher3(Long singleTextTeacherId, MaSingleTextCheckList maSingleTextCheckList, MaSingleTextForm maSingleTextForm, CoQuestion coQuestion, String explainText, String editorText, Long numRows, Long sizeY, Long sizeX, String titleText, String flagSpellchecker, String flagSyntax, String flagAgent, Set<CoSingleText3> coSingleText3s) {
        this.singleTextTeacherId = singleTextTeacherId;
        this.maSingleTextCheckList = maSingleTextCheckList;
        this.maSingleTextForm = maSingleTextForm;
        this.coQuestion = coQuestion;
        this.explainText = explainText;
        this.editorText = editorText;
        this.numRows = numRows;
        this.sizeY = sizeY;
        this.sizeX = sizeX;
        this.titleText = titleText;
        this.flagSpellchecker = flagSpellchecker;
        this.flagSyntax = flagSyntax;
        this.flagAgent = flagAgent;
        this.coSingleText3s = coSingleText3s;
    }

    @Id
    @Column(name = "single_text_teacher_id", unique = true, nullable = false, insertable = true, updatable = true, precision = 15, scale = 0)
    public Long getSingleTextTeacherId() {
        return this.singleTextTeacherId;
    }

    public void setSingleTextTeacherId(Long singleTextTeacherId) {
        this.singleTextTeacherId = singleTextTeacherId;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "check_list_form_id", unique = false, nullable = true, insertable = true, updatable = true)
    public MaSingleTextCheckList getMaSingleTextCheckList() {
        return this.maSingleTextCheckList;
    }

    public void setMaSingleTextCheckList(MaSingleTextCheckList maSingleTextCheckList) {
        this.maSingleTextCheckList = maSingleTextCheckList;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "single_text_form_id", unique = false, nullable = true, insertable = true, updatable = true)
    public MaSingleTextForm getMaSingleTextForm() {
        return this.maSingleTextForm;
    }

    public void setMaSingleTextForm(MaSingleTextForm maSingleTextForm) {
        this.maSingleTextForm = maSingleTextForm;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", unique = false, nullable = true, insertable = true, updatable = true)
    public CoQuestion getCoQuestion() {
        return this.coQuestion;
    }

    public void setCoQuestion(CoQuestion coQuestion) {
        this.coQuestion = coQuestion;
    }

    @Column(name = "explain_text", unique = false, nullable = true, insertable = true, updatable = true)
    public String getExplainText() {
        return this.explainText;
    }

    public void setExplainText(String explainText) {
        this.explainText = explainText;
    }

    @Column(name = "editor_text", unique = false, nullable = true, insertable = true, updatable = true)
    public String getEditorText() {
        return this.editorText;
    }

    public void setEditorText(String editorText) {
        this.editorText = editorText;
    }

    @Column(name = "num_rows", unique = false, nullable = true, insertable = true, updatable = true, precision = 4, scale = 0)
    public Long getNumRows() {
        return this.numRows;
    }

    public void setNumRows(Long numRows) {
        this.numRows = numRows;
    }

    @Column(name = "size_y", unique = false, nullable = true, insertable = true, updatable = true, precision = 4, scale = 0)
    public Long getSizeY() {
        return this.sizeY;
    }

    public void setSizeY(Long sizeY) {
        this.sizeY = sizeY;
    }

    @Column(name = "size_x", unique = false, nullable = true, insertable = true, updatable = true, precision = 4, scale = 0)
    public Long getSizeX() {
        return this.sizeX;
    }

    public void setSizeX(Long sizeX) {
        this.sizeX = sizeX;
    }

    @Column(name = "title_text", unique = false, nullable = true, insertable = true, updatable = true)
    public String getTitleText() {
        return this.titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    @Column(name = "flag_spellchecker", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
    public String getFlagSpellchecker() {
        return this.flagSpellchecker;
    }

    public void setFlagSpellchecker(String flagSpellchecker) {
        this.flagSpellchecker = flagSpellchecker;
    }

    @Column(name = "flag_syntax", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
    public String getFlagSyntax() {
        return this.flagSyntax;
    }

    public void setFlagSyntax(String flagSyntax) {
        this.flagSyntax = flagSyntax;
    }

    @Column(name = "flag_agent", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
    public String getFlagAgent() {
        return this.flagAgent;
    }

    public void setFlagAgent(String flagAgent) {
        this.flagAgent = flagAgent;
    }

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coSingleTextTeacher3")
    public Set<CoSingleText3> getCoSingleText3s() {
        return this.coSingleText3s;
    }

    public void setCoSingleText3s(Set<CoSingleText3> coSingleText3s) {
        this.coSingleText3s = coSingleText3s;
    }
}
