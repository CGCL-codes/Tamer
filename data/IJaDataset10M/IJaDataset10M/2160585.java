package org.libreplan.business.test.users.daos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.libreplan.business.BusinessGlobalNames.BUSINESS_SPRING_CONFIG_FILE;
import static org.libreplan.business.test.BusinessGlobalNames.BUSINESS_SPRING_CONFIG_TEST_FILE;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.libreplan.business.common.exceptions.InstanceNotFoundException;
import org.libreplan.business.users.daos.IProfileDAO;
import org.libreplan.business.users.entities.Profile;
import org.libreplan.business.users.entities.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { BUSINESS_SPRING_CONFIG_FILE, BUSINESS_SPRING_CONFIG_TEST_FILE })
@Transactional
public class ProfileDAOTest {

    @Autowired
    IProfileDAO profileDAO;

    @Test
    public void testInSpringContainer() {
        assertNotNull(profileDAO);
    }

    private Profile createValidProfile() {
        Set<UserRole> roles = new HashSet<UserRole>();
        return Profile.create(UUID.randomUUID().toString(), roles);
    }

    @Test
    public void testSaveProfile() {
        Profile profile = createValidProfile();
        profileDAO.save(profile);
        assertNotNull(profile.getId());
    }

    @Test
    public void testRemoveProfile() throws InstanceNotFoundException {
        Profile profile = createValidProfile();
        profileDAO.save(profile);
        profileDAO.remove(profile.getId());
        assertFalse(profileDAO.exists(profile.getId()));
    }

    @Test
    public void testListProfiles() {
        int previous = profileDAO.list(Profile.class).size();
        Profile profile = createValidProfile();
        profileDAO.save(profile);
        assertEquals(previous + 1, profileDAO.list(Profile.class).size());
    }
}
