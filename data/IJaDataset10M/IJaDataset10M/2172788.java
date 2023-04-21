package fr.soleil.bensikin.profile.manager;

import fr.soleil.bensikin.profile.Profile;

/**
 * A useless Profile ManagerS
 *
 * @author SOLEIL
 */
class DummyProfileManager implements IProfileManager {

    public DummyProfileManager() {
    }

    public int loadProfiles() {
        return 0;
    }

    public int addProfile(String name, String path) {
        return -1;
    }

    public void deleteProfile(int profileId) {
    }

    public Profile[] getProfiles() {
        return null;
    }

    public int getNewId() {
        return 0;
    }

    public int getSelectedProfile() {
        return -1;
    }

    public void setSelectedProfile(int id) {
    }

    public String getSelectedProfilePath() {
        return null;
    }

    public String getSelectedProfileName() {
        return null;
    }
}
