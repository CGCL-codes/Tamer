package demo.formapp.applogic;

import demo.formapp.model.Profile;

public class Profiles {

    private static Profile profile;

    public static Profile getProfile() {
        return profile;
    }

    public static void setProfile(Profile profile) {
        Profiles.profile = profile;
    }
}
