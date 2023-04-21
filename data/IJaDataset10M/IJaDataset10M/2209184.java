package com.acv.dao.forms.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import com.acv.dao.common.BaseObject;

/**
 * Candidate class - also used to generate the Hibernate mapping file.
 *
 * <p><a href="Candidate.java.html"><i>View Source</i></a>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * Updated by Dan Kibler (dan@getrolling.com)
 * Extended to implement Acegi UserDetails interface
 * by David Carter david@carter.net
 * @hibernate.class table="candidate"
 */
public class Candidate extends BaseObject {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The id. */
    private Long id;

    /** The first name. */
    private String firstName;

    /** The last name. */
    private String lastName;

    /** The phone number. */
    private String phoneNumber;

    /** The resume file name. */
    private String resumeFileName;

    /** The email. */
    private String email;

    /** The comments. */
    private String comments;

    /** The position. */
    private String position;

    /**
	 * Instantiates a new candidate.
	 */
    public Candidate() {
    }

    /**
     * Instantiates a new candidate.
     *
     * @param firstName the first name
     */
    public Candidate(String firstName) {
        this.firstName = firstName;
    }

    /**
	 * Gets the id.
	 *
	 * @return the id
	 *
	 * @hibernate.id column = "ID" unsaved-value = "null"
	 * @hibernate.generator class="sequence"
	 * @hibernate.param name="sequence" value="ACV_WEB.CANDIDATE_SEQ"
	 */
    public Long getId() {
        return id;
    }

    /**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * Gets the first name.
	 *
	 * @return the first name
	 *
	 * @hibernate.property length="50" column="first_name"
	 */
    public String getFirstName() {
        return firstName;
    }

    /**
	 * Sets the first name.
	 *
	 * @param firstName the new first name
	 */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
	 * Gets the last name.
	 *
	 * @return the last name
	 *
	 * @hibernate.property length="50" column="last_name"
	 */
    public String getLastName() {
        return lastName;
    }

    /**
	 * Sets the last name.
	 *
	 * @param lastName the new last name
	 */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
	 * Gets the phone number.
	 *
	 * @return the phone number
	 *
	 * @hibernate.property length="24" column="phone_number"
	 */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
	 * Sets the phone number.
	 *
	 * @param phoneNumber the new phone number
	 */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
	 * Gets the email.
	 *
	 * @return the email
	 *
	 * @hibernate.property length="50" column="email"
	 */
    public String getEmail() {
        return email;
    }

    /**
	 * Sets the email.
	 *
	 * @param emailAddress the new email
	 */
    public void setEmail(String emailAddress) {
        this.email = emailAddress;
    }

    /**
	 * Gets the resume file name.
	 *
	 * @return the resume file name
	 *
	 * @hibernate.property length="50" column="resume_file"
	 */
    public String getResumeFileName() {
        return resumeFileName;
    }

    /**
	 * Sets the resume file name.
	 *
	 * @param resumeFileName the new resume file name
	 */
    public void setResumeFileName(String resumeFileName) {
        this.resumeFileName = resumeFileName;
    }

    /**
	 * @hibernate.property length="1024" column="comments"
     */
    public String getComments() {
        return comments;
    }

    /**
	 * Sets the comments.
	 *
	 * @param comments the new comments
	 */
    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) return false;
        Candidate rhs = (Candidate) obj;
        return new EqualsBuilder().append(firstName, rhs.getFirstName()).append(lastName, rhs.getLastName()).append(phoneNumber, rhs.getPhoneNumber()).append(resumeFileName, rhs.getResumeFileName()).append(email, rhs.getEmail()).append(comments, rhs.getComments()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(firstName).append(lastName).append(phoneNumber).append(resumeFileName).append(email).append(comments).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id: ", id).append("firstName: ", firstName).append("lastName: ", lastName).append("phoneNumber: ", phoneNumber).append("resumeFileName: ", resumeFileName).append("email: ", email).append("comments: ", comments).toString();
    }

    /**
	 * Gets the position.
	 *
	 * @return the position
	 */
    public String getPosition() {
        return position;
    }

    /**
	 * Sets the position.
	 *
	 * @param position the position to set
	 */
    public void setPosition(String position) {
        this.position = position;
    }
}
