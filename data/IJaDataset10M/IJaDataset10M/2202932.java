package org.ncsa.foodlog.rcp.profile.model;

import org.apache.log4j.Logger;
import org.ncsa.foodlog.data.profiles.Profile;
import org.ncsa.foodlog.data.profiles.ProfileFolder;

public class ProfileNode {

    /**
	 * 
	 */
    private Profile profile;

    private ProfileFolder parent;

    private Logger log;

    public ProfileNode() {
    }

    public ProfileNode(ProfileFolder aParent, Profile aProfile) {
        log = Logger.getLogger(getClass());
        profile = aProfile;
        parent = aParent;
        log.debug("Adding " + profile.formattedString());
    }

    public ProfileFolder getParent() {
        return parent;
    }

    public void setParent(ProfileFolder parent) {
        this.parent = parent;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getName() {
        return profile.getTag().getName();
    }
}
