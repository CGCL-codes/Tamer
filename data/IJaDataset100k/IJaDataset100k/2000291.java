package lif.webclient.view;

import lif.core.domain.DbProfile;
import java.util.List;

public class ProfileSelectBean {

    public String profileName;

    public List<DbProfile> profiles;

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileName() {
        return this.profileName;
    }

    public List<DbProfile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<DbProfile> profiles) {
        this.profiles = profiles;
    }
}
