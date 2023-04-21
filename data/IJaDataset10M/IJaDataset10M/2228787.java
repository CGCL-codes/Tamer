package edu.univalle.lingweb.persistence;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * AbstractMaSpellError entity provides the base persistence definition of the
 * MaSpellError entity.
 * 
 * @author LingWeb
 */
@MappedSuperclass
public abstract class AbstractMaSpellError implements java.io.Serializable {

    private Long errorId;

    private CoCourse coCourse;

    private MaUser maUser;

    private MaSpell maSpell;

    private MaRole maRole;

    private CoLanguage coLanguage;

    private String wrongWord;

    /** default constructor */
    public AbstractMaSpellError() {
    }

    /** minimal constructor */
    public AbstractMaSpellError(Long errorId) {
        this.errorId = errorId;
    }

    /** full constructor */
    public AbstractMaSpellError(Long errorId, CoCourse coCourse, MaUser maUser, MaSpell maSpell, MaRole maRole, CoLanguage coLanguage, String wrongWord) {
        this.errorId = errorId;
        this.coCourse = coCourse;
        this.maUser = maUser;
        this.maSpell = maSpell;
        this.maRole = maRole;
        this.coLanguage = coLanguage;
        this.wrongWord = wrongWord;
    }

    @Id
    @Column(name = "error_id", unique = true, nullable = false, insertable = true, updatable = true, precision = 15, scale = 0)
    public Long getErrorId() {
        return this.errorId;
    }

    public void setErrorId(Long errorId) {
        this.errorId = errorId;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", unique = false, nullable = true, insertable = true, updatable = true)
    public CoCourse getCoCourse() {
        return this.coCourse;
    }

    public void setCoCourse(CoCourse coCourse) {
        this.coCourse = coCourse;
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
    @JoinColumn(name = "word_id", unique = false, nullable = true, insertable = true, updatable = true)
    public MaSpell getMaSpell() {
        return this.maSpell;
    }

    public void setMaSpell(MaSpell maSpell) {
        this.maSpell = maSpell;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id", unique = false, nullable = true, insertable = true, updatable = true)
    public MaRole getMaRole() {
        return this.maRole;
    }

    public void setMaRole(MaRole maRole) {
        this.maRole = maRole;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", unique = false, nullable = true, insertable = true, updatable = true)
    public CoLanguage getCoLanguage() {
        return this.coLanguage;
    }

    public void setCoLanguage(CoLanguage coLanguage) {
        this.coLanguage = coLanguage;
    }

    @Column(name = "wrong_word", unique = false, nullable = true, insertable = true, updatable = true, length = 60)
    public String getWrongWord() {
        return this.wrongWord;
    }

    public void setWrongWord(String wrongWord) {
        this.wrongWord = wrongWord;
    }
}
