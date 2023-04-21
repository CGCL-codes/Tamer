package com.ipolyglot.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @axisfuse.beanMapping xmlns="http://model.appfuse.org"
 * 
 * @author mishag
 * @struts.form extends="BaseForm"
 * @hibernate.class table="lesson"
 */
public class Lesson extends BaseObject implements Comparable, Serializable {

    private static final long serialVersionUID = 4529088145523631771L;

    private Long id;

    private String name;

    private String description;

    private User user;

    private String username;

    private Set<WordTranslation> wordTranslations = new HashSet<WordTranslation>();

    private Language wordLanguage;

    private String wordLanguageId;

    private Language translationLanguage;

    private String translationLanguageId;

    private Level level;

    private Short levelId;

    private LessonSharingOptions lessonSharingOptions;

    private Set<LessonUserGrade> lessonUserGrades = new HashSet<LessonUserGrade>();

    private Double grade;

    private Integer numOfReviews;

    private Set<LessonUserAccess> lessonUserAccesses = new HashSet<LessonUserAccess>();

    private Score score;

    /**
     * @return Returns the id.
     * @struts.form-field form-name="LessonForm"
     * @hibernate.id column="id" generator-class="native" unsaved-value="null"
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @struts.validator type="required"
     * @struts.form-field form-name="LessonForm"
     * @hibernate.property column="name" length="255" not-null="true"
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @struts.form-field form-name="LessonForm"
     * @hibernate.property column="description" length="400"
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return username
     * @struts.form-field form-name="LessonForm"
     * @hibernate.property column="username" not-null="true"
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @hibernate.many-to-one column="username" insert="false" update="false"
     *                        not-null="true"
     */
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.username = user.getUsername();
        } else {
            this.username = null;
        }
    }

    /**
     * @struts.form-field form-name="LessonForm"
     * @hibernate.set inverse="true" cascade="all-delete-orphan" lazy="true"
     *                order-by="id"
     * @hibernate.collection-key column="lesson_id"
     * @hibernate.collection-one-to-many class="com.ipolyglot.model.WordTranslation"
     */
    public Set<WordTranslation> getWordTranslations() {
        return wordTranslations;
    }

    public void setWordTranslations(Set<WordTranslation> wordTranslations) {
        this.wordTranslations = wordTranslations;
    }

    public void addWordTranslation(WordTranslation wordTranslation) {
        getWordTranslations().add(wordTranslation);
        wordTranslation.setLesson(this);
    }

    /**
     * @return wordLanguageId
     * @struts.form-field form-name="LessonForm"
     * @struts.validator type="required"
     * @hibernate.property column="word_language_id" length="2"
     */
    public String getWordLanguageId() {
        return wordLanguageId;
    }

    public void setWordLanguageId(String wordLanguageId) {
        this.wordLanguageId = wordLanguageId;
    }

    /**
     * @hibernate.many-to-one column="word_language_id" insert="false"
     *                        update="false" not-null="true" lazy="false"
     */
    public Language getWordLanguage() {
        return wordLanguage;
    }

    public void setWordLanguage(Language wordLanguage) {
        this.wordLanguage = wordLanguage;
        if (wordLanguage != null) {
            this.wordLanguageId = wordLanguage.getId();
        }
    }

    /**
     * @return translationLanguageId
     * @struts.form-field form-name="LessonForm"
     * @struts.validator type="required"
     * @hibernate.property column="translation_language_id" length="2"
     */
    public String getTranslationLanguageId() {
        return translationLanguageId;
    }

    public void setTranslationLanguageId(String translationLanguageId) {
        this.translationLanguageId = translationLanguageId;
    }

    /**
     * @hibernate.many-to-one column="translation_language_id" insert="false"
     *                        update="false" not-null="true" lazy="false"
     */
    public Language getTranslationLanguage() {
        return translationLanguage;
    }

    public void setTranslationLanguage(Language translationLanguage) {
        this.translationLanguage = translationLanguage;
        if (translationLanguage != null) {
            this.translationLanguageId = translationLanguage.getId();
        }
    }

    /**
     * @return levelId
     * @struts.form-field form-name="LessonForm"
     * @struts.validator type="required"
     * @hibernate.property column="level_id"
     */
    public Short getLevelId() {
        return levelId;
    }

    public void setLevelId(Short levelId) {
        this.levelId = levelId;
    }

    /**
     * @hibernate.many-to-one column="level_id" insert="false" update="false"
     *                        not-null="true" lazy="false"
     */
    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    /**
     * @struts.form-field form-name="LessonForm"
     * @hibernate.set inverse="true" cascade="all-delete-orphan" lazy="true"
     *                order-by="date_modified"
     * @hibernate.collection-key column="lesson_id"
     * @hibernate.collection-one-to-many class="com.ipolyglot.model.LessonUserGrade"
     */
    public Set<LessonUserGrade> getLessonUserGrades() {
        return lessonUserGrades;
    }

    public void setLessonUserGrades(Set<LessonUserGrade> lessonUserGrades) {
        this.lessonUserGrades = lessonUserGrades;
    }

    /**
     * @return Lesson Grade
     * @struts.form-field form-name="LessonForm"
     * @hibernate.property column="grade"
     */
    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    /**
     * @return reversed Lesson Grade Formula: abs(getGrade() - 5) Will be used
     *         for descending order in lists
     */
    public Double getReversedGrade() {
        double reversedGrade = 5;
        if (grade != null) {
            reversedGrade = Math.abs(getGrade().doubleValue() - 5);
        }
        return new Double(reversedGrade);
    }

    /**
     * @return Number of lesson reviews
     * @struts.form-field form-name="LessonForm"
     * @hibernate.property column="num_of_reviews"
     */
    public Integer getNumOfReviews() {
        return numOfReviews;
    }

    public void setNumOfReviews(Integer numOfReviews) {
        this.numOfReviews = numOfReviews;
    }

    /**
     * @struts.form-field form-name="LessonForm"
     * @hibernate.component
     */
    public LessonSharingOptions getLessonSharingOptions() {
        return lessonSharingOptions;
    }

    public void setLessonSharingOptions(LessonSharingOptions lessonSharingOptions) {
        this.lessonSharingOptions = lessonSharingOptions;
    }

    /**
     * @hibernate.set inverse="true" cascade="all-delete-orphan" lazy="true"
     * @hibernate.collection-key column="lesson_id"
     * @hibernate.collection-one-to-many class="com.ipolyglot.model.LessonUserAccess"
     */
    public Set<LessonUserAccess> getLessonUserAccesses() {
        return lessonUserAccesses;
    }

    public void setLessonUserAccesses(Set<LessonUserAccess> lessonUserAccesses) {
        this.lessonUserAccesses = lessonUserAccesses;
    }

    /**
     * not persistent attribute used for showing the aggregate score (for example of a user)
     * @return
     */
    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals(Object object) {
        if (!(object instanceof Lesson)) {
            return false;
        }
        Lesson rhs = (Lesson) object;
        return new EqualsBuilder().append(this.getName(), rhs.getName()).append(this.getWordLanguageId(), rhs.getWordLanguageId()).append(this.getTranslationLanguageId(), rhs.getTranslationLanguageId()).isEquals();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder(1454052963, 521617827).append(this.id).append(this.name).append(this.description).toHashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this).append("id", this.id).append("name", this.name).append("description", this.description).append("username", this.username).append("wordLanguageId", this.wordLanguageId).append("translationLanguageId", this.translationLanguageId).append("levelId", this.levelId).append("grade", this.grade).append("score", this.score).toString();
    }

    /**
     * @see java.lang.Comparable#compareTo(Object)
     */
    public int compareTo(Object object) {
        Lesson myClass = (Lesson) object;
        return new CompareToBuilder().append(this.description, myClass.description).append(this.name, myClass.name).append(this.id, myClass.id).toComparison();
    }
}
