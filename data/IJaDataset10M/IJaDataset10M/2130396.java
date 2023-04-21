package monitor;

import java.util.Date;

/** A device that is known but not managed by the system.
 * Extends the Device class and is always created before a
 * ManagedDevice.
 * 
 * @author  ryan
 * @version 0.1
 */
public class KnownDevice extends Device {

    private Date dateKnown;

    /** Create a KnownDevice w/o Device ID or creation date
	 * Use this constructor to create a new KnownDevice, 
	 * when none exists, and the creation date is now.
	 * 
	 * @param ipAddress
	 * @param community
	 * @param name
	 * @param description
	 */
    public KnownDevice(String ipAddress, String community, String name, String description) {
        super(ipAddress, community, name, description, false);
        dateKnown = new Date();
    }

    /** Create a KnownDevice w/o Device ID
	 * Use this constructor to create a new KnownDevice, 
	 * when none exists, and the creation date is known.
	 * 
	 * @param ipAddress the IP address of the ManagedDevice. 
	 * @param community the community string or password for the ManagedDevice.
	 * @param name the name of the ManagedDevice.
	 * @param description the description for the ManagedDevice.
	 * @param dateKnown the date discovered.
	 */
    public KnownDevice(String ipAddress, String community, String name, String description, Date dateKnown) {
        super(ipAddress, community, name, description, false);
        this.dateKnown = dateKnown;
    }

    /** Create a full KnownDevice object.
	 * Use this constructor to create a KnownDevice, 
	 * when all data is available, including the DeviceID.
	 * <p><em>This constructor should only be used by the database classes.</em>
	 * 
	 * @param deviceID the record ID in the database table.
	 * @param ipAddress the IP address of the ManagedDevice. 
	 * @param community the community string or password for the ManagedDevice.
	 * @param name the name of the ManagedDevice.
	 * @param description the description for the ManagedDevice.
	 * @param removed the removed or present state of the ManagedDevice in the system. 
	 * @param dateKnown the date discovered.
	 */
    public KnownDevice(long deviceID, String ipAddress, String community, String name, String description, boolean removed, Date dateKnown) {
        super(deviceID, ipAddress, community, name, description, removed);
        this.dateKnown = dateKnown;
    }

    /** Remove a KnownDevice from the system.
	 * 
	 */
    public void remove() {
        removed = true;
    }

    /** Unremove a KnownDevice from the system.
	 * 
	 */
    public void unremove() {
        removed = false;
    }

    public String toString() {
        String results = "KnownDevice:\n" + super.toString();
        results += "\tDate Known:  " + dateKnown + "\n";
        return results;
    }

    /** Gets the original date discovered of the KnownDevice.
	 * 
	 * @return the date discovered.
	 */
    public Date getDateKnown() {
        return dateKnown;
    }

    /** Sets the date discovered of the KnownDevice.
	 * 
	 * @param dateKnown The dateKnown to set.
	 */
    public void setDateKnown(Date dateKnown) {
        this.dateKnown = dateKnown;
    }
}
