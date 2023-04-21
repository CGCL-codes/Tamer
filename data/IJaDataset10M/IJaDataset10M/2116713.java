package jistory.persistence;

import java.util.HashMap;
import java.util.Set;

/**
 *	Stores associations between version numbers of software and <i>Version
 *	Control System</i> revision number.
 *	@author Christopher Bull
 */
public class DBO_Versions {

    private HashMap<String, Long> versions;

    /**
	 *	The default constructor.
	 */
    public DBO_Versions() {
        versions = new HashMap<String, Long>();
    }

    /**
	 *	@param versions The versions that will make up this versions object
	 */
    public DBO_Versions(HashMap<String, Long> versions) {
        this.versions = versions;
    }

    /**
	 *	
	 *	@return the map of versions
	 */
    public HashMap<String, Long> getMap() {
        return versions;
    }

    /**
	 *	Adds a Version and associated revision number
	 *	@param version The Version number being added
	 *	@param revision The associated revision number
	 *	@return <code>true</code> on successful addition, and <code>false</code>
	 *	if the Version already exists
	 */
    public boolean addVersion(String version, int revision) {
        if (versions.containsKey(version)) {
            return false;
        }
        versions.put(version, new Long(revision));
        return true;
    }

    /**
	 *	Edits a current version with a new revision number
	 *	@param version The version to edit
	 *	@param revision The new revision number
	 *	@return <code>true</code> on a successful edit, and <code>false</code>
	 *	if the version does not exist.
	 */
    public boolean editVersion(String version, int revision) {
        if (versions.containsKey(version)) {
            versions.put(version, new Long(revision));
            return true;
        }
        return false;
    }

    /**
	 *	Removes the given Version number
	 *	@param version The Version number to have its association with a revision
	 *	number removed
	 */
    public void deleteVersion(String version) {
        versions.remove(version);
    }

    /**
	 *	Gets all of the Version numbers.
	 *	@return a <code>Set</code> of all the Version numbers
	 */
    public Set<String> getAllVersions() {
        return versions.keySet();
    }

    /**
	 *	Checks if the given version exists
	 *	@param version The version to check
	 *	@return <code>true</code> if it exists, else it returns <code>false</code>
	 */
    public boolean containsVersion(String version) {
        return versions.containsKey(version);
    }

    /**
	 *	Gets the number of Versions that have been stored.
	 *	@return the number of Versions that are stored
	 */
    public int getVersionsSize() {
        return versions.size();
    }

    /**
	 *	Gets the revision number associated with the given version number
	 *	@param version The version to get the associated revision number for
	 *	@return the <code>int</code> value of the revision number, or -1 if the
	 *	Version does not exist.
	 */
    public long getRevisionNumber(String version) {
        if (versions.containsKey(version)) {
            return versions.get(version).intValue();
        }
        return -1;
    }
}
