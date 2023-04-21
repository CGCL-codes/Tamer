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
 * AbstractCoLanguage entity provides the base persistence definition of the
 * CoLanguage entity.
 * 
 * @author LingWeb
 */
@MappedSuperclass
public abstract class AbstractCoLanguage implements java.io.Serializable {

    private Long languageId;

    private String languageName;

    private String languageNameEn;

    private String languageNameFr;

    private String locale;

    private Set<CoParagraphBaseKnowledge> coParagraphBaseKnowledges = new HashSet<CoParagraphBaseKnowledge>(0);

    private Set<MaSyntaticError> maSyntaticErrors = new HashSet<MaSyntaticError>(0);

    private Set<MaParagraphCheckList> maParagraphCheckLists = new HashSet<MaParagraphCheckList>(0);

    private Set<MaPostag> maPostags = new HashSet<MaPostag>(0);

    private Set<MaParagraphForm> maParagraphForms = new HashSet<MaParagraphForm>(0);

    private Set<MaSpellError> maSpellErrors = new HashSet<MaSpellError>(0);

    private Set<CoCourse> coCourses = new HashSet<CoCourse>(0);

    private Set<CoSingleTextBaseKnowledge> coSingleTextBaseKnowledges = new HashSet<CoSingleTextBaseKnowledge>(0);

    private Set<MaSingleTextForm> maSingleTextForms = new HashSet<MaSingleTextForm>(0);

    private Set<MaSyntatic> maSyntatics = new HashSet<MaSyntatic>(0);

    private Set<CoMaterial> coMaterials = new HashSet<CoMaterial>(0);

    private Set<MaSingleTextCheckList> maSingleTextCheckLists = new HashSet<MaSingleTextCheckList>(0);

    private Set<MaSpell> maSpells = new HashSet<MaSpell>(0);

    /** default constructor */
    public AbstractCoLanguage() {
    }

    /** minimal constructor */
    public AbstractCoLanguage(Long languageId, String languageName, String locale) {
        this.languageId = languageId;
        this.languageName = languageName;
        this.locale = locale;
    }

    /** full constructor */
    public AbstractCoLanguage(Long languageId, String languageName, String languageNameEn, String languageNameFr, String locale, Set<CoParagraphBaseKnowledge> coParagraphBaseKnowledges, Set<MaSyntaticError> maSyntaticErrors, Set<MaParagraphCheckList> maParagraphCheckLists, Set<MaPostag> maPostags, Set<MaParagraphForm> maParagraphForms, Set<MaSpellError> maSpellErrors, Set<CoCourse> coCourses, Set<CoSingleTextBaseKnowledge> coSingleTextBaseKnowledges, Set<MaSingleTextForm> maSingleTextForms, Set<MaSyntatic> maSyntatics, Set<CoMaterial> coMaterials, Set<MaSingleTextCheckList> maSingleTextCheckLists, Set<MaSpell> maSpells) {
        this.languageId = languageId;
        this.languageName = languageName;
        this.languageNameEn = languageNameEn;
        this.languageNameFr = languageNameFr;
        this.locale = locale;
        this.coParagraphBaseKnowledges = coParagraphBaseKnowledges;
        this.maSyntaticErrors = maSyntaticErrors;
        this.maParagraphCheckLists = maParagraphCheckLists;
        this.maPostags = maPostags;
        this.maParagraphForms = maParagraphForms;
        this.maSpellErrors = maSpellErrors;
        this.coCourses = coCourses;
        this.coSingleTextBaseKnowledges = coSingleTextBaseKnowledges;
        this.maSingleTextForms = maSingleTextForms;
        this.maSyntatics = maSyntatics;
        this.coMaterials = coMaterials;
        this.maSingleTextCheckLists = maSingleTextCheckLists;
        this.maSpells = maSpells;
    }

    @Id
    @Column(name = "language_id", unique = true, nullable = false, insertable = true, updatable = true, precision = 15, scale = 0)
    public Long getLanguageId() {
        return this.languageId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }

    @Column(name = "language_name", unique = false, nullable = false, insertable = true, updatable = true, length = 60)
    public String getLanguageName() {
        return this.languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    @Column(name = "language_name_en", unique = false, nullable = true, insertable = true, updatable = true, length = 60)
    public String getLanguageNameEn() {
        return this.languageNameEn;
    }

    public void setLanguageNameEn(String languageNameEn) {
        this.languageNameEn = languageNameEn;
    }

    @Column(name = "language_name_fr", unique = false, nullable = true, insertable = true, updatable = true, length = 60)
    public String getLanguageNameFr() {
        return this.languageNameFr;
    }

    public void setLanguageNameFr(String languageNameFr) {
        this.languageNameFr = languageNameFr;
    }

    @Column(name = "locale", unique = false, nullable = false, insertable = true, updatable = true, length = 5)
    public String getLocale() {
        return this.locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coLanguage")
    public Set<CoParagraphBaseKnowledge> getCoParagraphBaseKnowledges() {
        return this.coParagraphBaseKnowledges;
    }

    public void setCoParagraphBaseKnowledges(Set<CoParagraphBaseKnowledge> coParagraphBaseKnowledges) {
        this.coParagraphBaseKnowledges = coParagraphBaseKnowledges;
    }

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coLanguage")
    public Set<MaSyntaticError> getMaSyntaticErrors() {
        return this.maSyntaticErrors;
    }

    public void setMaSyntaticErrors(Set<MaSyntaticError> maSyntaticErrors) {
        this.maSyntaticErrors = maSyntaticErrors;
    }

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coLanguage")
    public Set<MaParagraphCheckList> getMaParagraphCheckLists() {
        return this.maParagraphCheckLists;
    }

    public void setMaParagraphCheckLists(Set<MaParagraphCheckList> maParagraphCheckLists) {
        this.maParagraphCheckLists = maParagraphCheckLists;
    }

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coLanguage")
    public Set<MaPostag> getMaPostags() {
        return this.maPostags;
    }

    public void setMaPostags(Set<MaPostag> maPostags) {
        this.maPostags = maPostags;
    }

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coLanguage")
    public Set<MaParagraphForm> getMaParagraphForms() {
        return this.maParagraphForms;
    }

    public void setMaParagraphForms(Set<MaParagraphForm> maParagraphForms) {
        this.maParagraphForms = maParagraphForms;
    }

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coLanguage")
    public Set<MaSpellError> getMaSpellErrors() {
        return this.maSpellErrors;
    }

    public void setMaSpellErrors(Set<MaSpellError> maSpellErrors) {
        this.maSpellErrors = maSpellErrors;
    }

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coLanguage")
    public Set<CoCourse> getCoCourses() {
        return this.coCourses;
    }

    public void setCoCourses(Set<CoCourse> coCourses) {
        this.coCourses = coCourses;
    }

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coLanguage")
    public Set<CoSingleTextBaseKnowledge> getCoSingleTextBaseKnowledges() {
        return this.coSingleTextBaseKnowledges;
    }

    public void setCoSingleTextBaseKnowledges(Set<CoSingleTextBaseKnowledge> coSingleTextBaseKnowledges) {
        this.coSingleTextBaseKnowledges = coSingleTextBaseKnowledges;
    }

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coLanguage")
    public Set<MaSingleTextForm> getMaSingleTextForms() {
        return this.maSingleTextForms;
    }

    public void setMaSingleTextForms(Set<MaSingleTextForm> maSingleTextForms) {
        this.maSingleTextForms = maSingleTextForms;
    }

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coLanguage")
    public Set<MaSyntatic> getMaSyntatics() {
        return this.maSyntatics;
    }

    public void setMaSyntatics(Set<MaSyntatic> maSyntatics) {
        this.maSyntatics = maSyntatics;
    }

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coLanguage")
    public Set<CoMaterial> getCoMaterials() {
        return this.coMaterials;
    }

    public void setCoMaterials(Set<CoMaterial> coMaterials) {
        this.coMaterials = coMaterials;
    }

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coLanguage")
    public Set<MaSingleTextCheckList> getMaSingleTextCheckLists() {
        return this.maSingleTextCheckLists;
    }

    public void setMaSingleTextCheckLists(Set<MaSingleTextCheckList> maSingleTextCheckLists) {
        this.maSingleTextCheckLists = maSingleTextCheckLists;
    }

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coLanguage")
    public Set<MaSpell> getMaSpells() {
        return this.maSpells;
    }

    public void setMaSpells(Set<MaSpell> maSpells) {
        this.maSpells = maSpells;
    }
}
