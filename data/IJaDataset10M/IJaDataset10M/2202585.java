package br.ufmg.ubicomp.droidguide.profile;

import junit.framework.TestCase;

public class HttpUserProfileDaoTest extends TestCase {

    public void testWrite() {
        UserProfile profile = new UserProfile();
        profile.setName("dummy");
        profile.setBirthyear(1970);
        profile.setGender('M');
        profile.setMaritalstatus("dummy 2");
        HttpUserProfileDao dao = new HttpUserProfileDao();
        dao.Write(profile);
    }
}
