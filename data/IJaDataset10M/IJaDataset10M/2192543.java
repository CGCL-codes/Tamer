package com.ipolyglot.model;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @axisfuse.beanMapping xmlns="http://model.appfuse.org"
 * 
 * @author mishag
 * @struts.form extends="BaseForm"
 * @hibernate.class table="lesson_user_grade"
 */
public class LessonUserGrade extends BaseObject implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Lesson lesson;

    private Long lessonId;

    private User user;

    private String username;

    private Double grade;

    private String comment;

    private Date dateModified;

    public LessonUserGrade() {
        this.dateModified = new Date();
    }

    /**
     * @return Returns the id.
     * @struts.form-field form-name="LessonUserGradeForm"
     * @hibernate.id column="id" generator-class="native" unsaved-value="null"
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @hibernate.many-to-one column="lesson_id" insert="false" update="false"
     *                        not-null="true"
     */
    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
        if (lesson != null) {
            this.lessonId = lesson.getId();
        }
    }

    /**
     * @return lessonId
     * @struts.form-field form-name="LessonUserGradeForm"
     * @hibernate.property
     * @hibernate.column name="lesson_id" not-null="true"
     *                   index="lesson_id_username_ind"
     */
    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
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
        }
    }

    /**
     * @return username
     * @struts.form-field form-name="LessonUserGradeForm"
     * @hibernate.property
     * @hibernate.column name="username" not-null="true"
     *                   index="lesson_id_username_ind"
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return grade
     * @struts.form-field form-name="LessonUserGradeForm"
     * @struts.validator type="required"
     * @hibernate.property column="grade" not-null="true"
     */
    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    /**
     * @return reversed Lesson Grade Formula: abs(getGrade() - 5)
     */
    public Double getReversedGrade() {
        double reversedGrade = 5;
        if (grade != null) {
            reversedGrade = Math.abs(getGrade().doubleValue() - 5);
        }
        return new Double(reversedGrade);
    }

    /**
     * @struts.form-field form-name="LessonUserGradeForm"
     * @hibernate.property column="comment" length="500"
     */
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return Returns the dateModified.
     * @hibernate.property column="date_modified" not-null="true"
     */
    public Date getDateModified() {
        return dateModified;
    }

    /**
     * @param dateModified
     *            The dateModified to set.
     */
    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals(Object object) {
        if (!(object instanceof LessonUserGrade)) {
            return false;
        }
        LessonUserGrade rhs = (LessonUserGrade) object;
        return new EqualsBuilder().append(this.getLessonId(), rhs.getLessonId()).append(this.getUsername(), rhs.getUsername()).isEquals();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder(1883683937, -1349803067).append(this.id).append(this.lessonId).append(this.username).append(this.grade).toHashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this).append("id", this.id).append("lessonId", this.lessonId).append("username", this.username).append("gradeId", this.grade).append("comment", this.comment).append("dateModified", this.dateModified).toString();
    }
}
