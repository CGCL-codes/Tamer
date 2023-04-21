package com.acv.dao.forms.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * The Class AirDetailsForm.
 */
public class AirDetailsForm extends CruiseRequestForm {

    /** The flight. */
    private String flight;

    /** The dep flight. */
    private String depFlight;

    /** The return flight. */
    private String returnFlight;

    /** The dep flight from. */
    private String depFlightFrom;

    /** The dep flight to. */
    private String depFlightTo;

    /** The dep flight class. */
    private String depFlightClass;

    /** The inbound. */
    private String inbound;

    /** The inbound date. */
    private String inboundDate;

    /** The inbound from. */
    private String inboundFrom;

    /** The inbound to. */
    private String inboundTo;

    /** The inbound class. */
    private String inboundClass;

    /** The comments. */
    private String comments;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
	 * Gets the comments.
	 *
	 * @return the comments
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

    /**
	 * Gets the dep flight.
	 *
	 * @return the dep flight
	 */
    public String getDepFlight() {
        return depFlight;
    }

    /**
	 * Sets the dep flight.
	 *
	 * @param depFlight the new dep flight
	 */
    public void setDepFlight(String depFlight) {
        this.depFlight = depFlight;
    }

    /**
	 * Gets the dep flight class.
	 *
	 * @return the dep flight class
	 */
    public String getDepFlightClass() {
        return depFlightClass;
    }

    /**
	 * Sets the dep flight class.
	 *
	 * @param depFlightClass the new dep flight class
	 */
    public void setDepFlightClass(String depFlightClass) {
        this.depFlightClass = depFlightClass;
    }

    /**
	 * Gets the dep flight from.
	 *
	 * @return the dep flight from
	 */
    public String getDepFlightFrom() {
        return depFlightFrom;
    }

    /**
	 * Sets the dep flight from.
	 *
	 * @param depFlightFrom the new dep flight from
	 */
    public void setDepFlightFrom(String depFlightFrom) {
        this.depFlightFrom = depFlightFrom;
    }

    /**
	 * Gets the dep flight to.
	 *
	 * @return the dep flight to
	 */
    public String getDepFlightTo() {
        return depFlightTo;
    }

    /**
	 * Sets the dep flight to.
	 *
	 * @param depFlightTo the new dep flight to
	 */
    public void setDepFlightTo(String depFlightTo) {
        this.depFlightTo = depFlightTo;
    }

    /**
	 * Gets the flight.
	 *
	 * @return the flight
	 */
    public String getFlight() {
        return flight;
    }

    /**
	 * Sets the flight.
	 *
	 * @param flight the new flight
	 */
    public void setFlight(String flight) {
        this.flight = flight;
    }

    /**
	 * Gets the inbound.
	 *
	 * @return the inbound
	 */
    public String getInbound() {
        return inbound;
    }

    /**
	 * Sets the inbound.
	 *
	 * @param inbound the new inbound
	 */
    public void setInbound(String inbound) {
        this.inbound = inbound;
    }

    /**
	 * Gets the inbound class.
	 *
	 * @return the inbound class
	 */
    public String getInboundClass() {
        return inboundClass;
    }

    /**
	 * Sets the inbound class.
	 *
	 * @param inboundClass the new inbound class
	 */
    public void setInboundClass(String inboundClass) {
        this.inboundClass = inboundClass;
    }

    /**
	 * Gets the inbound date.
	 *
	 * @return the inbound date
	 */
    public String getInboundDate() {
        return inboundDate;
    }

    /**
	 * Sets the inbound date.
	 *
	 * @param inboundDate the new inbound date
	 */
    public void setInboundDate(String inboundDate) {
        this.inboundDate = inboundDate;
    }

    /**
	 * Gets the inbound from.
	 *
	 * @return the inbound from
	 */
    public String getInboundFrom() {
        return inboundFrom;
    }

    /**
	 * Sets the inbound from.
	 *
	 * @param inboundFrom the new inbound from
	 */
    public void setInboundFrom(String inboundFrom) {
        this.inboundFrom = inboundFrom;
    }

    /**
	 * Gets the inbound to.
	 *
	 * @return the inbound to
	 */
    public String getInboundTo() {
        return inboundTo;
    }

    /**
	 * Sets the inbound to.
	 *
	 * @param inboundTo the new inbound to
	 */
    public void setInboundTo(String inboundTo) {
        this.inboundTo = inboundTo;
    }

    /**
	 * Gets the return flight.
	 *
	 * @return the return flight
	 */
    public String getReturnFlight() {
        return returnFlight;
    }

    /**
	 * Sets the return flight.
	 *
	 * @param returnFlight the new return flight
	 */
    public void setReturnFlight(String returnFlight) {
        this.returnFlight = returnFlight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AirDetailsForm)) return false;
        final AirDetailsForm airDetailsForm = (AirDetailsForm) o;
        return new EqualsBuilder().appendSuper(super.equals(o)).append(flight, airDetailsForm.getFlight()).append(depFlight, airDetailsForm.getDepFlight()).append(returnFlight, airDetailsForm.getReturnFlight()).append(depFlightFrom, airDetailsForm.getDepFlightFrom()).append(depFlightTo, airDetailsForm.getDepFlightTo()).append(depFlightClass, airDetailsForm.getDepFlightClass()).append(inbound, airDetailsForm.getInbound()).append(inboundDate, airDetailsForm.getInboundDate()).append(inboundFrom, airDetailsForm.getInboundFrom()).append(inboundTo, airDetailsForm.getInboundTo()).append(inboundClass, airDetailsForm.getInboundClass()).append(comments, airDetailsForm.getComments()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(flight).append(depFlight).append(returnFlight).append(depFlightFrom).append(depFlightTo).append(depFlightClass).append(inbound).append(inboundDate).append(inboundFrom).append(inboundTo).append(inboundClass).append(comments).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).appendSuper(super.toString()).append("Flight", this.flight).append("Departure Flight", this.depFlight).append("Return Flight", this.returnFlight).append("Departure Flight From", this.depFlightFrom).append("Departure Flight To", this.depFlightTo).append("Departure Flight Class", this.depFlightClass).append("Inbound", this.inbound).append("Inbound Date", this.inboundDate).append("Inbound From", this.inboundFrom).append("Inbound To", this.inboundTo).append("Inbound Class", this.inboundClass).append("Comments", this.comments).toString();
    }
}
