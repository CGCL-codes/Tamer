package wsdir.core;

import wsdir.util.DateTime;

/**
 * It represents the response of a Directory Service regarding the status of a successful modify() operation.
 */
public class ModifyReport implements java.io.Serializable {

    /**
	 * The symbolic identifier of the directory that the registration was made in.
	 */
    String directoryId = null;

    /**
	 * TODO: utility of directoryDescription Object?
	 * TODO: Why not use the class DirectoryDescription?
	 * The value of the data updated with the directory.
	 */
    Object directoryDescription = null;

    /**
	 * A token describing the resulting operation.
	 * @see wsdir.core.DirectoryToken
	 */
    DirectoryToken directoryToken = null;

    /**
	 * The period of time the registration is valid for.
	 * @see wsdir.util.DateTime
	 */
    DateTime leaseTime = null;

    /**
	 * The old Object stored in the Store
	 */
    Object oldObjectStored = null;

    /**
	 * Empty Constructor.
	 */
    public ModifyReport() {
    }

    /**
	 * Constructor with initial parameters.
	 * @param _directoryId the symbolic identifier of the directory the registration was made in.
	 * @param _directoryDescription
	 * @param _directoryToken a token describing the resulting operation.
	 * @param _leaseTime the period of time the registration is valid for.
	 */
    public ModifyReport(String _directoryId, Object _directoryDescription, DirectoryToken _directoryToken, DateTime _leaseTime) {
        directoryId = _directoryId;
        directoryDescription = _directoryDescription;
        directoryToken = _directoryToken;
        leaseTime = _leaseTime;
    }

    /**
	 * Set the value of directoryId.
	 * @param s  value to assign to directoryId.
	 * @uml.property  name="directoryId"
	 */
    public void setDirectoryId(String s) {
        directoryId = s;
    }

    /**
	 * Get the value of directoryId.
	 * @return  the value of directoryId.
	 * @uml.property  name="directoryId"
	 */
    public String getDirectoryId() {
        return directoryId;
    }

    /**
	 * Set the value of directoryDescription.
	 * @param d  value to assign to directoryDescription.
	 * @uml.property  name="directoryDescription"
	 */
    public void setDirectoryDescription(Object d) {
        directoryDescription = d;
    }

    /**
	 * Get the value of directoryDescription. 
	 * @return  value of directoryDescription.
	 * @uml.property  name="directoryDescription"
	 */
    public Object getDirectoryDescription() {
        return directoryDescription;
    }

    /**
	 * Set the value of directoryToken.
	 * @param d  value to assign to directoryToken.
	 * @uml.property  name="directoryToken"
	 */
    public void setDirectoryToken(DirectoryToken d) {
        directoryToken = d;
    }

    /**
	 * Get the value of directoryToken.
	 * @return  value of directoryToken.
	 * @uml.property  name="directoryToken"
	 */
    public DirectoryToken getDirectoryToken() {
        return directoryToken;
    }

    /**
	 * Set the value of leaseTime.
	 * @param d  value to assign to leaseTime.
	 * @uml.property  name="leaseTime"
	 */
    public void setLeaseTime(DateTime d) {
        leaseTime = d;
    }

    /**
	 * Get the value of leaseTime.
	 * @return  value of leaseTime.
	 * @uml.property  name="leaseTime"
	 */
    public DateTime getLeaseTime() {
        return leaseTime;
    }

    public Object getOldObjectStored() {
        return oldObjectStored;
    }

    public void setOldObjectStored(Object oldObjectStored) {
        this.oldObjectStored = oldObjectStored;
    }
}
