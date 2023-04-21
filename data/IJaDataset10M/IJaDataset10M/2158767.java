package edu.univalle.lingweb.persistence;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * AbstractStoredOpenResponse3 entity provides the base persistence definition
 * of the StoredOpenResponse3 entity.
 * 
 * @author LingWeb
 */
@MappedSuperclass
public abstract class AbstractStoredOpenResponse3 implements java.io.Serializable {

    private Long storedOpenResponseId;

    private MaUser maUser;

    private OpenResponse3 openResponse3;

    private String textResponse;

    private String score;

    private Long version;

    private String commentSection;

    private String commentAll;

    /** default constructor */
    public AbstractStoredOpenResponse3() {
    }

    /** minimal constructor */
    public AbstractStoredOpenResponse3(Long storedOpenResponseId, OpenResponse3 openResponse3) {
        this.storedOpenResponseId = storedOpenResponseId;
        this.openResponse3 = openResponse3;
    }

    /** full constructor */
    public AbstractStoredOpenResponse3(Long storedOpenResponseId, MaUser maUser, OpenResponse3 openResponse3, String textResponse, String score, Long version, String commentSection, String commentAll) {
        this.storedOpenResponseId = storedOpenResponseId;
        this.maUser = maUser;
        this.openResponse3 = openResponse3;
        this.textResponse = textResponse;
        this.score = score;
        this.version = version;
        this.commentSection = commentSection;
        this.commentAll = commentAll;
    }

    @Id
    @Column(name = "stored_open_response_id", unique = true, nullable = false, insertable = true, updatable = true, precision = 15, scale = 0)
    public Long getStoredOpenResponseId() {
        return this.storedOpenResponseId;
    }

    public void setStoredOpenResponseId(Long storedOpenResponseId) {
        this.storedOpenResponseId = storedOpenResponseId;
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
    @JoinColumn(name = "open_reponse_id", unique = false, nullable = false, insertable = true, updatable = true)
    public OpenResponse3 getOpenResponse3() {
        return this.openResponse3;
    }

    public void setOpenResponse3(OpenResponse3 openResponse3) {
        this.openResponse3 = openResponse3;
    }

    @Column(name = "text_response", unique = false, nullable = true, insertable = true, updatable = true)
    public String getTextResponse() {
        return this.textResponse;
    }

    public void setTextResponse(String textResponse) {
        this.textResponse = textResponse;
    }

    @Column(name = "score", unique = false, nullable = true, insertable = true, updatable = true, length = 60)
    public String getScore() {
        return this.score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Column(name = "version_", unique = false, nullable = true, insertable = true, updatable = true, precision = 4, scale = 0)
    public Long getVersion() {
        return this.version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Column(name = "comment_section", unique = false, nullable = true, insertable = true, updatable = true)
    public String getCommentSection() {
        return this.commentSection;
    }

    public void setCommentSection(String commentSection) {
        this.commentSection = commentSection;
    }

    @Column(name = "comment_all", unique = false, nullable = true, insertable = true, updatable = true)
    public String getCommentAll() {
        return this.commentAll;
    }

    public void setCommentAll(String commentAll) {
        this.commentAll = commentAll;
    }
}
