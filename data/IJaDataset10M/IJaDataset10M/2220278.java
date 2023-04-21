package com.andre.conferenceroom.model;

/**
 * <p>
 * This class is a wrapper for {@link Attendee}.
 * </p>
 *
 * @author    Andre Moura
 * @see       Attendee
 * @generated
 */
public class AttendeeWrapper implements Attendee {

    public AttendeeWrapper(Attendee attendee) {
        _attendee = attendee;
    }

    public long getPrimaryKey() {
        return _attendee.getPrimaryKey();
    }

    public void setPrimaryKey(long pk) {
        _attendee.setPrimaryKey(pk);
    }

    public long getAttendeeId() {
        return _attendee.getAttendeeId();
    }

    public void setAttendeeId(long AttendeeId) {
        _attendee.setAttendeeId(AttendeeId);
    }

    public long getReservationId() {
        return _attendee.getReservationId();
    }

    public void setReservationId(long ReservationId) {
        _attendee.setReservationId(ReservationId);
    }

    public java.lang.String getPersonName() {
        return _attendee.getPersonName();
    }

    public void setPersonName(java.lang.String PersonName) {
        _attendee.setPersonName(PersonName);
    }

    public java.lang.String getPersonEmail() {
        return _attendee.getPersonEmail();
    }

    public void setPersonEmail(java.lang.String PersonEmail) {
        _attendee.setPersonEmail(PersonEmail);
    }

    public com.andre.conferenceroom.model.Attendee toEscapedModel() {
        return _attendee.toEscapedModel();
    }

    public boolean isNew() {
        return _attendee.isNew();
    }

    public void setNew(boolean n) {
        _attendee.setNew(n);
    }

    public boolean isCachedModel() {
        return _attendee.isCachedModel();
    }

    public void setCachedModel(boolean cachedModel) {
        _attendee.setCachedModel(cachedModel);
    }

    public boolean isEscapedModel() {
        return _attendee.isEscapedModel();
    }

    public void setEscapedModel(boolean escapedModel) {
        _attendee.setEscapedModel(escapedModel);
    }

    public java.io.Serializable getPrimaryKeyObj() {
        return _attendee.getPrimaryKeyObj();
    }

    public com.liferay.portlet.expando.model.ExpandoBridge getExpandoBridge() {
        return _attendee.getExpandoBridge();
    }

    public void setExpandoBridgeAttributes(com.liferay.portal.service.ServiceContext serviceContext) {
        _attendee.setExpandoBridgeAttributes(serviceContext);
    }

    public java.lang.Object clone() {
        return _attendee.clone();
    }

    public int compareTo(com.andre.conferenceroom.model.Attendee attendee) {
        return _attendee.compareTo(attendee);
    }

    public int hashCode() {
        return _attendee.hashCode();
    }

    public java.lang.String toString() {
        return _attendee.toString();
    }

    public java.lang.String toXmlString() {
        return _attendee.toXmlString();
    }

    public Attendee getWrappedAttendee() {
        return _attendee;
    }

    private Attendee _attendee;
}
