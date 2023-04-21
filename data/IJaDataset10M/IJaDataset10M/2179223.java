package com.acv.dao.forms.model;

import java.util.Date;

/**
 * Form send by offline booking request form.
 */
public class OfflineBookingForm extends BaseForm {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The title. */
    private String title = null;

    /** The departure date. */
    private Date departureDate = new Date();

    /** The duration. */
    private int duration = 0;

    /** The additional comments. */
    private String additionalComments = null;

    /** The preferred contact. */
    private String preferredContact = "E";

    /** The infants. */
    private int infants = 0;

    /** The children. */
    private int children = 0;

    /** The adults. */
    private int adults = 0;

    /** The seniors. */
    private int seniors = 0;

    /** The interest1. */
    private boolean interest1 = false;

    /** The interest2. */
    private boolean interest2 = false;

    /** The interest3. */
    private boolean interest3 = false;

    /** The interest4. */
    private boolean interest4 = false;

    /** The interest5. */
    private boolean interest5 = false;

    /**
     * get Title.
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * set Title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * get PreferredContact.
     *
     * @return preferredContact
     */
    public String getPreferredContact() {
        return preferredContact;
    }

    /**
     * set PreferredContact.
     *
     * @param preferredContact the preferred contact
     */
    public void setPreferredContact(String preferredContact) {
        this.preferredContact = preferredContact;
    }

    /**
     * get number of Infants.
     *
     * @return infants
     */
    public int getInfants() {
        return infants;
    }

    /**
     * set number of Infants.
     *
     * @param infants the infants
     */
    public void setInfants(int infants) {
        this.infants = infants;
    }

    /**
     * get number of Children.
     *
     * @return children
     */
    public int getChildren() {
        return children;
    }

    /**
     * set number of children.
     *
     * @param children the children
     */
    public void setChildren(int children) {
        this.children = children;
    }

    /**
     * get number of adults.
     *
     * @return adults
     */
    public int getAdults() {
        return adults;
    }

    /**
     * set number of adults.
     *
     * @param adults the adults
     */
    public void setAdults(int adults) {
        this.adults = adults;
    }

    /**
     * get number of seniors.
     *
     * @return seniors
     */
    public int getSeniors() {
        return seniors;
    }

    /**
     * set number of seniors.
     *
     * @param seniors the seniors
     */
    public void setSeniors(int seniors) {
        this.seniors = seniors;
    }

    /**
     * get Interest1.
     *
     * @return interest1
     */
    public boolean getInterest1() {
        return interest1;
    }

    /**
     * set Interest1.
     *
     * @param interest1 the interest1
     */
    public void setInterest1(boolean interest1) {
        this.interest1 = interest1;
    }

    /**
     * get Interest2.
     *
     * @return interest2
     */
    public boolean getInterest2() {
        return interest2;
    }

    /**
     * set Interest2.
     *
     * @param interest2 the interest2
     */
    public void setInterest2(boolean interest2) {
        this.interest2 = interest2;
    }

    /**
     * get Interest3.
     *
     * @return interest3
     */
    public boolean getInterest3() {
        return interest3;
    }

    /**
     * set Interest3.
     *
     * @param interest3 the interest3
     */
    public void setInterest3(boolean interest3) {
        this.interest3 = interest3;
    }

    /**
     * get Interest4.
     *
     * @return interest4
     */
    public boolean getInterest4() {
        return interest4;
    }

    /**
     * set Interest4.
     *
     * @param interest4 the interest4
     */
    public void setInterest4(boolean interest4) {
        this.interest4 = interest4;
    }

    /**
     * get Interest5.
     *
     * @return Interest5
     */
    public boolean getInterest5() {
        return interest5;
    }

    /**
     * set Interest5.
     *
     * @param interest5 the interest5
     */
    public void setInterest5(boolean interest5) {
        this.interest5 = interest5;
    }

    /**
     * get AdditionalComments.
     *
     * @return additionalComments
     */
    public String getAdditionalComments() {
        return additionalComments;
    }

    /**
     * set AdditionalComments.
     *
     * @param additionalComments the additional comments
     */
    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }

    /**
     * get DepartureDate.
     *
     * @return departureDate
     */
    public Date getDepartureDate() {
        return departureDate;
    }

    /**
     * set DepartureDate.
     *
     * @param departureDate the departure date
     */
    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    /**
     * get Duration in day.
     *
     * @return duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * set Duration.
     *
     * @param duration the duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }
}
