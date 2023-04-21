package at.rc.tacos.platform.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import at.rc.tacos.platform.util.MyUtils;

/**
 * Represents one roster entry
 * 
 * @author b.thek
 */
public class RosterEntry extends Lockable {

    private int rosterId;

    private Location station;

    private StaffMember staffMember;

    private long plannedStartOfWork;

    private long plannedEndOfWork;

    private long realStartOfWork;

    private long realEndOfWork;

    private ServiceType serviceType;

    private Job job;

    private String createdByUser;

    private String rosterNotes;

    private boolean standby;

    /**
	 * Default class constructor
	 */
    public RosterEntry() {
        rosterId = -1;
    }

    /**
	 * Constructor for a minimal roster entry object
	 * 
	 * @param staffMember
	 *            the person for this service
	 * @param serviceType
	 *            the employee status
	 * @param job
	 *            of this person only for this roster entry
	 * @param station
	 *            the place to work
	 * @param plannedStartOfWork
	 *            the planned time to start the service
	 * @param plannedEndOfWork
	 *            the planned end of the service
	 */
    public RosterEntry(StaffMember staffMember, ServiceType serviceType, Job job, Location station, long plannedStartOfWork, long plannedEndOfWork) {
        setStaffMember(staffMember);
        setPlannedEndOfWork(plannedEndOfWork);
        setPlannedStartOfWork(plannedStartOfWork);
        setStation(station);
        setJob(job);
        setServicetype(serviceType);
    }

    /**
	 * @param staffMember
	 *            the staff member for this entry
	 * @param plannedStartOfWork
	 *            the planned star of work
	 * @param plannedEndOfWork
	 *            the planned end of work
	 * @param realStartOfWork
	 *            the real start of work
	 * @param realEndOfWork
	 *            the real end of work
	 * @param station
	 *            the roster station of
	 * @param job
	 *            of this entry
	 * @param serviceType
	 *            the service type of this entry
	 * @param rosterNotes
	 *            the notes for this roster
	 * @param standby
	 *            flag to show that the staff member is at home
	 */
    public RosterEntry(StaffMember staffMember, long plannedStartOfWork, long plannedEndOfWork, long realStartOfWork, long realEndOfWork, ServiceType serviceType, Job job, Location station, String rosterNotes, boolean standby) {
        setStaffMember(staffMember);
        setPlannedEndOfWork(plannedEndOfWork);
        setPlannedStartOfWork(plannedStartOfWork);
        setRealStartOfWork(realStartOfWork);
        setRealEndOfWork(realEndOfWork);
        setStation(station);
        setJob(job);
        setServicetype(serviceType);
        setRosterNotes(rosterNotes);
        setStandby(standby);
    }

    /**
	 * Returns the human readable string for this <code>RosterEntry</code>
	 * instance.
	 * 
	 * @return the build string
	 */
    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("id", rosterId);
        builder.append("staff", staffMember);
        builder.append("standby", standby);
        builder.append("OS", station);
        builder.append("Verwendung", job);
        builder.append("DV", serviceType);
        builder.append("notes", rosterNotes);
        builder.append("plannedStart", MyUtils.timestampToString(plannedStartOfWork, MyUtils.timeAndDateFormat));
        builder.append("plannedEnd", MyUtils.timestampToString(plannedEndOfWork, MyUtils.timeAndDateFormat));
        builder.append("signedIn", MyUtils.timestampToString(realStartOfWork, MyUtils.timeAndDateFormat));
        builder.append("signedOut", MyUtils.timestampToString(realEndOfWork, MyUtils.timeAndDateFormat));
        return builder.toString();
    }

    /**
	 * Returns the generated hashCode of this <code>RosterEntry</code> instance.
	 * <p>
	 * The hashCode is based uppon the {@link RosterEntry#getRosterId()}
	 * </p>
	 * 
	 * @return the generated hash code
	 */
    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(43, 53);
        builder.append(rosterId);
        return builder.toHashCode();
    }

    /**
	 * Returns wheter or not this <code>RosterEntry</code> instance is equal to
	 * the compared object.
	 * <p>
	 * The compared fields are {@link RosterEntry#getRosterId()}
	 * </p>
	 * 
	 * @return true if the instance is the same otherwise false.
	 */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        RosterEntry rosterEntry = (RosterEntry) obj;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(rosterId, rosterEntry.rosterId);
        return builder.isEquals();
    }

    /**
	 * Returns wheter or not this entry has notes
	 * 
	 * @return true if there are notes
	 */
    public boolean hasNotes() {
        if (rosterNotes == null) return false;
        if (rosterNotes.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public int getLockedId() {
        return rosterId;
    }

    @Override
    public Class<?> getLockedClass() {
        return RosterEntry.class;
    }

    /**
	 * Returns the identification string of this member
	 * 
	 * @return the rosterId
	 */
    public int getRosterId() {
        return rosterId;
    }

    /**
	 * Sets the identification string of this member
	 * 
	 * @param rosterId
	 *            the rosterId to set
	 * @throws IllegalArgumentException
	 *             if the id is negative
	 */
    public void setRosterId(int rosterId) {
        this.rosterId = rosterId;
    }

    /**
	 * Returns the member of this entry
	 * 
	 * @return the staff member
	 */
    public StaffMember getStaffMember() {
        return staffMember;
    }

    /**
	 * Sets the member of this entry
	 * 
	 * @param staffMember
	 *            the staff member to set
	 */
    public void setStaffMember(StaffMember staffMember) {
        this.staffMember = staffMember;
    }

    /**
	 * Returns the planned end of work for this roster entry.<br>
	 * 
	 * @return the plannedEndOfWork
	 */
    public long getPlannedEndOfWork() {
        return plannedEndOfWork;
    }

    /**
	 * Sets the planned end of work for this entry.<br>
	 * 
	 * @param plannedEndOfWork
	 *            the time and date of the planned end of work
	 */
    public void setPlannedEndOfWork(long plannedEndOfWork) {
        this.plannedEndOfWork = plannedEndOfWork;
    }

    /**
	 * Returns the planned start of work in milliseconds.
	 * 
	 * @return the planned start of work.
	 */
    public long getPlannedStartOfWork() {
        return plannedStartOfWork;
    }

    /**
	 * Sets the planned start of work for this roster entry.
	 * 
	 * @param plannedStartOfWork
	 *            the plannedStartOfWork to set
	 */
    public void setPlannedStartOfWork(long plannedStartOfWork) {
        this.plannedStartOfWork = plannedStartOfWork;
    }

    /**
	 * Returns the check in time of this roster entry. This time represents the
	 * real start time of the roster entry.
	 * 
	 * @return the realStartOfWork
	 */
    public long getRealStartOfWork() {
        return realStartOfWork;
    }

    /**
	 * Sets the check-in time for this roster entry
	 * 
	 * @param realStartOfWork
	 *            the realStartOfWork to set
	 * @throws IllegalArgumentException
	 *             if the given value is not a valid date
	 */
    public void setRealStartOfWork(long realStartOfWork) {
        this.realStartOfWork = realStartOfWork;
    }

    /**
	 * Returns the check-out time for this member. This time represents the real
	 * start time of the meber.
	 * 
	 * @return the realEndOfWork
	 */
    public long getRealEndOfWork() {
        return realEndOfWork;
    }

    /**
	 * Sets the check-out time for this roster entry.
	 * 
	 * @param realEndOfWork
	 *            the realEndOfWork to set
	 * @throws IllegalArgumentException
	 *             if the given value is not a valid date
	 */
    public void setRealEndOfWork(long realEndOfWork) {
        this.realEndOfWork = realEndOfWork;
    }

    /**
	 * Returns the station where the staff member is working for this roster
	 * entry.
	 * 
	 * @return the station of the duty
	 */
    public Location getStation() {
        return station;
    }

    /**
	 * Sets the station for this staff member
	 * 
	 * @param station
	 *            the station to set
	 */
    public void setStation(Location station) {
        this.station = station;
    }

    /**
	 * Returns the job of this staff member.<br>
	 * 
	 * @return the job of the roster entry.
	 */
    public Job getJob() {
        return job;
    }

    /**
	 * Sets the job for this staff member.
	 * 
	 * @param job
	 *            the job to set
	 */
    public void setJob(Job job) {
        this.job = job;
    }

    /**
	 * Returns the service type of this staff member.<br>
	 * 
	 * @return the service type
	 */
    public ServiceType getServicetype() {
        return serviceType;
    }

    /**
	 * Sets the service type for this staff member
	 * 
	 * @param serviceType
	 *            the service type to set
	 */
    public void setServicetype(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    /**
	 * Returns the notes for this roster entry.
	 * 
	 * @return the rosterNotes
	 */
    public String getRosterNotes() {
        return rosterNotes;
    }

    /**
	 * Sets the notes for this roster entry.
	 * 
	 * @param rosterNotes
	 *            the rosterNotes to set
	 */
    public void setRosterNotes(String rosterNotes) {
        this.rosterNotes = rosterNotes;
    }

    /**
	 * Returns whether or not the member is currently at home and in standby. <br>
	 * The staff member is at home and must be called.
	 * 
	 * @return the status of the staff member
	 */
    public boolean getStandby() {
        return standby;
    }

    /**
	 * Sets whether or not this staff member is in standby
	 * 
	 * @param standby
	 *            the standby to set
	 */
    public void setStandby(boolean standby) {
        this.standby = standby;
    }

    /**
	 * Returns whether or not the roster entry is split up onto more or one day.<br>
	 * The start day and the end day is not the same.
	 * 
	 * @return true if the start and end day is not the same
	 */
    public boolean isSplitEntry() {
        return !MyUtils.isEqualDate(plannedStartOfWork, plannedEndOfWork);
    }

    /**
	 * Returns the name of the user who created the roster entry.
	 * 
	 * @return the username of the creator.
	 */
    public String getCreatedByUsername() {
        return createdByUser;
    }

    /**
	 * Sets the name of the staff member who created the entry.
	 * 
	 * @param createdByUser
	 *            the username of the creator
	 */
    public void setCreatedByUsername(String createdByUser) {
        this.createdByUser = createdByUser;
    }
}
