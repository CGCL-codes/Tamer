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
 * AbstractCoParagraph3 entity provides the base persistence definition of the
 * CoParagraph3 entity.
 * 
 * @author LingWeb
 */
@MappedSuperclass
public abstract class AbstractCoParagraph3 implements java.io.Serializable {

    private Long paragraphId;

    private MaUser maUser;

    private CoParagraphTeacher3 coParagraphTeacher3;

    private String explainText;

    private String editorText;

    private Long numRows;

    private Long sizeY;

    private Long sizeX;

    private Long score;

    private String titleText;

    private Set<CoParagraphCheckList3> coParagraphCheckList3s = new HashSet<CoParagraphCheckList3>(0);

    /** default constructor */
    public AbstractCoParagraph3() {
    }

    /** minimal constructor */
    public AbstractCoParagraph3(Long paragraphId) {
        this.paragraphId = paragraphId;
    }

    /** full constructor */
    public AbstractCoParagraph3(Long paragraphId, MaUser maUser, CoParagraphTeacher3 coParagraphTeacher3, String explainText, String editorText, Long numRows, Long sizeY, Long sizeX, Long score, String titleText, Set<CoParagraphCheckList3> coParagraphCheckList3s) {
        this.paragraphId = paragraphId;
        this.maUser = maUser;
        this.coParagraphTeacher3 = coParagraphTeacher3;
        this.explainText = explainText;
        this.editorText = editorText;
        this.numRows = numRows;
        this.sizeY = sizeY;
        this.sizeX = sizeX;
        this.score = score;
        this.titleText = titleText;
        this.coParagraphCheckList3s = coParagraphCheckList3s;
    }

    @Id
    @Column(name = "paragraph_id", unique = true, nullable = false, insertable = true, updatable = true, precision = 15, scale = 0)
    public Long getParagraphId() {
        return this.paragraphId;
    }

    public void setParagraphId(Long paragraphId) {
        this.paragraphId = paragraphId;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = false, nullable = true, insertable = true, updatable = true)
    public MaUser getMaUser() {
        return this.maUser;
    }

    public void setMaUser(MaUser maUser) {
        this.maUser = maUser;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "paragraph_teacher_id", unique = false, nullable = true, insertable = true, updatable = true)
    public CoParagraphTeacher3 getCoParagraphTeacher3() {
        return this.coParagraphTeacher3;
    }

    public void setCoParagraphTeacher3(CoParagraphTeacher3 coParagraphTeacher3) {
        this.coParagraphTeacher3 = coParagraphTeacher3;
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

    @Column(name = "score", unique = false, nullable = true, insertable = true, updatable = true, precision = 4, scale = 0)
    public Long getScore() {
        return this.score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    @Column(name = "title_text", unique = false, nullable = true, insertable = true, updatable = true)
    public String getTitleText() {
        return this.titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coParagraph3")
    public Set<CoParagraphCheckList3> getCoParagraphCheckList3s() {
        return this.coParagraphCheckList3s;
    }

    public void setCoParagraphCheckList3s(Set<CoParagraphCheckList3> coParagraphCheckList3s) {
        this.coParagraphCheckList3s = coParagraphCheckList3s;
    }
}
