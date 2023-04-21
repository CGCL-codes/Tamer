package com.acv.dao.forms.model;

import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * pre and post travel support merge in only one form, the uses form is postTravelSupportForm
 * @deprecated
 */
@Deprecated
public class PreTravelSupportForm extends BaseForm {

    /** The travel agency. */
    private String travelAgency;

    /** The dep date. */
    private Date depDate;

    /** The leaving from. */
    private String leavingFrom;

    /** The going to. */
    private String goingTo;

    /** The traveled before. */
    private String traveledBefore;

    /** The companion member. */
    private String companionMember;

    /** The airplane name. */
    private String airplaneName;

    /** The airplane num. */
    private String airplaneNum;

    /** The experience. */
    private String experience;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
		 * Gets the airplane name.
		 *
		 * @return the airplane name
		 */
    public String getAirplaneName() {
        return airplaneName;
    }

    /**
		 * Sets the airplane name.
		 *
		 * @param airplaneName the new airplane name
		 */
    public void setAirplaneName(String airplaneName) {
        this.airplaneName = airplaneName;
    }

    /**
		 * Gets the airplane num.
		 *
		 * @return the airplane num
		 */
    public String getAirplaneNum() {
        return airplaneNum;
    }

    /**
		 * Sets the airplane num.
		 *
		 * @param airplaneNum the new airplane num
		 */
    public void setAirplaneNum(String airplaneNum) {
        this.airplaneNum = airplaneNum;
    }

    /**
		 * Gets the companion member.
		 *
		 * @return the companion member
		 */
    public String getCompanionMember() {
        return companionMember;
    }

    /**
		 * Sets the companion member.
		 *
		 * @param companionMember the new companion member
		 */
    public void setCompanionMember(String companionMember) {
        this.companionMember = companionMember;
    }

    /**
		 * Gets the dep date.
		 *
		 * @return the dep date
		 */
    public Date getDepDate() {
        return depDate;
    }

    /**
		 * Sets the dep date.
		 *
		 * @param depDate the new dep date
		 */
    public void setDepDate(Date depDate) {
        this.depDate = depDate;
    }

    /**
		 * Gets the experience.
		 *
		 * @return the experience
		 */
    public String getExperience() {
        return experience;
    }

    /**
		 * Sets the experience.
		 *
		 * @param experience the new experience
		 */
    public void setExperience(String experience) {
        this.experience = experience;
    }

    /**
		 * Gets the going to.
		 *
		 * @return the going to
		 */
    public String getGoingTo() {
        return goingTo;
    }

    /**
		 * Sets the going to.
		 *
		 * @param goingTo the new going to
		 */
    public void setGoingTo(String goingTo) {
        this.goingTo = goingTo;
    }

    /**
		 * Gets the leaving from.
		 *
		 * @return the leaving from
		 */
    public String getLeavingFrom() {
        return leavingFrom;
    }

    /**
		 * Sets the leaving from.
		 *
		 * @param leavingFrom the new leaving from
		 */
    public void setLeavingFrom(String leavingFrom) {
        this.leavingFrom = leavingFrom;
    }

    /**
		 * Gets the travel agency.
		 *
		 * @return the travel agency
		 */
    public String getTravelAgency() {
        return travelAgency;
    }

    /**
		 * Sets the travel agency.
		 *
		 * @param travelAgency the new travel agency
		 */
    public void setTravelAgency(String travelAgency) {
        this.travelAgency = travelAgency;
    }

    /**
		 * Gets the traveled before.
		 *
		 * @return the traveled before
		 */
    public String getTraveledBefore() {
        return traveledBefore;
    }

    /**
		 * Sets the traveled before.
		 *
		 * @param traveledBefore the new traveled before
		 */
    public void setTraveledBefore(String traveledBefore) {
        this.traveledBefore = traveledBefore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PreTravelSupportForm)) return false;
        final PreTravelSupportForm preTravelSupportForm = (PreTravelSupportForm) o;
        return new EqualsBuilder().appendSuper(super.equals(o)).append(travelAgency, preTravelSupportForm.getTravelAgency()).append(depDate, preTravelSupportForm.getDepDate()).append(leavingFrom, preTravelSupportForm.getLeavingFrom()).append(goingTo, preTravelSupportForm.getGoingTo()).append(traveledBefore, preTravelSupportForm.getTraveledBefore()).append(companionMember, preTravelSupportForm.getCompanionMember()).append(airplaneName, preTravelSupportForm.getAirplaneName()).append(airplaneNum, preTravelSupportForm.getAirplaneNum()).append(experience, preTravelSupportForm.getExperience()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(travelAgency).append(depDate).append(leavingFrom).append(goingTo).append(traveledBefore).append(companionMember).append(airplaneName).append(airplaneNum).append(experience).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).appendSuper(super.toString()).append("Travel Agency", this.travelAgency).append("Dep Date", this.depDate).append("Leaving From", this.leavingFrom).append("Going To", this.goingTo).append("Traveled Before", this.traveledBefore).append("Companion Member", this.companionMember).append("Airplane Name", this.airplaneName).append("Airplane Num", this.airplaneNum).append("Experience", this.experience).toString();
    }
}
