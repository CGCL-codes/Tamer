package org.quantumleaphealth.model.screener;

import static javax.persistence.TemporalType.TIMESTAMP;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.quantumleaphealth.model.Person;

/**
 * Represents a person who screens a patient for accrual to a clinical trial.
 * @author Tom Bechtold
 * @version 2009-01-21
 */
@Entity
public class UserScreener implements Person, Serializable {

    /**
     * Unique identifier or <code>null</code> if new object.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserScreenerID")
    @SequenceGenerator(name = "UserScreenerID", sequenceName = "userscreener_id_seq", allocationSize = 1)
    private Long id;

    /**
     * Email address or <code>null</code> if none
     */
    private String principal;

    /**
     * Credentials or <code>null</code> if none
     */
    private String credentials;

    /**
     * The secret question if credentials are lost
     */
    private String secretQuestion;

    /**
     * The secret answer if credentials are lost
     */
    private String secretAnswer;

    /**
     * The number of unsuccessful authentication attempts
     * since the most recent successful authentication.
     */
    private int unsuccessfulAuthenticationCount;

    /**
     * When accepted the terms/conditions 
     * or <code>null</code> if not accepted yet
     */
    @Temporal(TIMESTAMP)
    private Date termsAcceptedTime;

    /**
     * Access control levels for administering screener groups
     */
    public enum AccessControl {

        NORMAL, ADMINISTRATOR, SUPERUSER
    }

    /**
     * Access control, guaranteed to be non-<code>null</code>
     */
    @Enumerated(EnumType.ORDINAL)
    private AccessControl accessControl = AccessControl.NORMAL;

    /**
     * Last name
     */
    private String surName;

    /**
     * First name
     */
    private String givenName;

    /**
     * Middle initial
     */
    private String middleInitial;

    /**
     * Prefix
     */
    private String prefix;

    /**
     * Generation suffix
     */
    private String generationSuffix;

    /**
     * Professional suffix
     */
    private String professionalSuffix;

    /**
     * Title
     */
    private String title;

    /**
     * Telephone number
     */
    private String phone;

    /**
     * The groups this screener belongs to.
     * Note that the join table overwrites the default by 
     * switching the order of the entities 
     * and uses the singular to refer to the inverse join column.
     */
    @ManyToMany
    @JoinTable(name = "ScreenerGroup_UserScreener", inverseJoinColumns = @JoinColumn(name = "ScreenerGroup_ID"))
    private Set<ScreenerGroup> screenerGroups;

    /**
     * @return the unique identifier or <code>null</code> if new object
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the unique identifier or <code>null</code> if new object
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the principal
     */
    public String getPrincipal() {
        return principal;
    }

    /**
     * @param principal the principal to set;
     *        empty string is stored as <code>null</code>
     */
    public void setPrincipal(String principal) {
        this.principal = trim(principal);
    }

    /**
     * @return the credentials
     */
    public String getCredentials() {
        return credentials;
    }

    /**
     * @param credentials the credentials to set;
     *        empty string is stored as <code>null</code>
     */
    public void setCredentials(String credentials) {
        this.credentials = trim(credentials);
    }

    /**
     * @return the secret question if credentials are lost
     */
    public String getSecretQuestion() {
        return secretQuestion;
    }

    /**
     * @param secretQuestion the secret question if credentials are lost;
     *        empty string is stored as <code>null</code>
     */
    public void setSecretQuestion(String secretQuestion) {
        this.secretQuestion = trim(secretQuestion);
    }

    /**
     * @return the secret answer if credentials are lost
     */
    public String getSecretAnswer() {
        return secretAnswer;
    }

    /**
     * @param secretAnswer the secret answer if credentials are lost;
     *        empty string is stored as <code>null</code>
     */
    public void setSecretAnswer(String secretAnswer) {
        this.secretAnswer = trim(secretAnswer);
    }

    /**
     * @return whether data has been entered in <code>principal</code>, 
     *         <code>credentials</code>, <code>secretQuestion</code> and <code>secretAnswer</code>
     */
    @Transient
    public boolean isCredentialed() {
        return (trim(principal) != null) && (trim(credentials) != null) && (trim(secretQuestion) != null) && (trim(secretAnswer) != null);
    }

    /**
     * @return the number of unsuccessful authentication attempts
     *          since the most recent successful authentication
     */
    public int getUnsuccessfulAuthenticationCount() {
        return unsuccessfulAuthenticationCount;
    }

    /**
     * @param unsuccessfulAuthenticationCount the number of unsuccessful authentication attempts
     *        since the most recent successful authentication
     */
    public void setUnsuccessfulAuthenticationCount(int unsuccessfulAuthenticationCount) {
        this.unsuccessfulAuthenticationCount = unsuccessfulAuthenticationCount;
    }

    /**
     * Returns whether the terms were accepted. This method return
     * <code>true</code> if the accepted date is non-<code>null</code>. This
     * method is declared transient so the persistence engine does not save it;
     * rather, the persistence engine will access the data via
     * <code>getTermsAcceptedTime</code> and <code>setTermsAcceptedTime</code>.
     * 
     * @return whether the terms were accepted
     * @see #termsAcceptedTime
     */
    @Transient
    public boolean isTermsAccepted() {
        return termsAcceptedTime != null;
    }

    /**
     * Sets the terms accepted date. This method sets the date to now if the
     * parameter is <code>true</code> and the date has not been stored yet.
     * 
     * @param termsAcceptedNow whether the terms are accepted
     * @see #termsAcceptedTime
     */
    public void setTermsAccepted(boolean termsAcceptedNow) {
        if (!termsAcceptedNow) termsAcceptedTime = null; else if (this.termsAcceptedTime == null) termsAcceptedTime = new Date();
    }

    /**
     * @return when accepted the terms/conditions 
     *         or <code>null</code> if not accepted yet
     */
    public Date getTermsAcceptedTime() {
        return termsAcceptedTime;
    }

    /**
     * @param termsAcceptedTime when accepted the terms/conditions 
     *        or <code>null</code> if not accepted yet
     */
    public void setTermsAcceptedTime(Date termsAcceptedTime) {
        this.termsAcceptedTime = termsAcceptedTime;
    }

    /**
     * @return access control, guaranteed to be non-<code>null</code>
     */
    public AccessControl getAccessControl() {
        return accessControl;
    }

    /**
     * @param the access control
     * @throws IllegalArgumentException if parameter is <code>null</code>
     */
    public void setAccessControl(AccessControl accessControl) throws IllegalArgumentException {
        if (accessControl == null) throw new IllegalArgumentException("access control not specified");
        this.accessControl = accessControl;
    }

    /**
     * @return the last name
     * @see org.quantumleaphealth.model.Person#getSurName()
     */
    public String getSurName() {
        return surName;
    }

    /**
     * @param surName the last name
     */
    public void setSurName(String surName) {
        this.surName = surName;
    }

    /**
     * @return the given name
     * @see org.quantumleaphealth.model.Person#getGivenName()
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * @param givenName the given name
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    /**
     * @return the middleInitial
     * @see org.quantumleaphealth.model.Person#getMiddleInitial()
     */
    public String getMiddleInitial() {
        return middleInitial;
    }

    /**
     * @param middleInitial the middleInitial
     */
    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    /**
     * @return the prefix
     * @see org.quantumleaphealth.model.Person#getPrefix()
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix the prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * @return the generation suffix
     * @see org.quantumleaphealth.model.Person#getGenerationSuffix()
     */
    public String getGenerationSuffix() {
        return generationSuffix;
    }

    /**
     * @param generationSuffix the generation suffix
     */
    public void setGenerationSuffix(String generationSuffix) {
        this.generationSuffix = generationSuffix;
    }

    /**
     * @return the professional suffix
     * @see org.quantumleaphealth.model.Person#getProfessionalSuffix()
     */
    public String getProfessionalSuffix() {
        return professionalSuffix;
    }

    /**
     * @param professionalSuffix the professional suffix
     */
    public void setProfessionalSuffix(String professionalSuffix) {
        this.professionalSuffix = professionalSuffix;
    }

    /**
     * @return the title
     * @see org.quantumleaphealth.model.Person#getTitle()
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the telephone number
     * @see org.quantumleaphealth.model.Person#getPhone()
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the telephone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the email address, a.k.a., the principal
     * @see org.quantumleaphealth.model.Person#getEmail()
     */
    @Transient
    public String getEmail() {
        return getPrincipal();
    }

    /**
     * @return the groups this screener belongs to
     */
    public Set<ScreenerGroup> getScreenerGroups() {
        return screenerGroups;
    }

    /**
     * @param screenerGroups the groups this screener belongs to
     */
    public void setScreenerGroups(Set<ScreenerGroup> screenerGroups) {
        this.screenerGroups = screenerGroups;
    }

    /**
     * @param string the string to trim
     * @return a trimmed string or <code>null</code> if the string is empty
     */
    private static String trim(String string) {
        if (string == null) return null;
        string = string.trim();
        return (string.length() == 0) ? null : string;
    }

    /**
     * Version UID for serialization
     */
    private static final long serialVersionUID = -8376005639088444811L;
}
