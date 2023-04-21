package org.examcity.domain;

import static org.examcity.domain.fixtures.FixtureUtil.create;
import javax.annotation.Resource;
import org.examcity.domain.fixtures.UserBuilder;
import org.springframework.dao.DataIntegrityViolationException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups = { "domain", "domain.repository" })
@SuppressWarnings("unused")
public class UserRepositoryTest extends RepositoryTest {

    @Resource
    private UserRepository userRepository;

    private User u1;

    private User u2;

    @BeforeMethod
    private void fixture() {
        u1 = create(UserBuilder.class).username("USER1").fullName("USER ONE").email("user1@ec.com").superuser().comments("This is the user number one").deleted().build();
        u1 = create(UserBuilder.class).username("USER2").fullName("USER TWO").email("user2@ec.com").comments("This is the user number two").build();
        flushAndClear();
    }

    public void testFind() {
        User u = userRepository.find(u1.getId());
        Assert.assertNotNull(u);
        Assert.assertEquals(u.getUsername(), u1.getUsername());
        Assert.assertEquals(u.getId(), u1.getId());
        Assert.assertEquals(u.getFullName(), u1.getFullName());
        Assert.assertEquals(u.isSuperuser(), u1.isSuperuser());
        Assert.assertEquals(u.getStatus(), u1.getStatus());
    }

    public void testRemove() {
        User u = userRepository.find(u1.getId());
        Assert.assertNotNull(u);
        userRepository.remove(u);
        u = userRepository.find(u1.getId());
        Assert.assertNull(u);
    }

    public void testPersist() {
        User u = new User();
        u.setUsername("NEWUSER");
        u.setFullName("NEW USER");
        u.setEmail("NEWuser@ec.com");
        u.setSuperuser(false);
        u.setComments("NEW");
        u.deactivate();
        userRepository.persist(u);
        Assert.assertNotNull(u.getId());
        u = userRepository.find(u.getId());
        Assert.assertNotNull(u);
    }

    public void testMerge() {
        User v1 = new User();
        v1.setUsername("NEWUSER");
        v1.setFullName("NEW USER");
        v1.setEmail("NEWuser@ec.com");
        v1.setSuperuser(false);
        v1.setComments("NEW");
        User v2 = userRepository.merge(v1);
        Assert.assertFalse(entityManager.contains(v1));
        Assert.assertTrue(entityManager.contains(v2));
        Assert.assertNull(v1.getId());
        Assert.assertNotNull(v2.getId());
        User v = userRepository.find(v2.getId());
        Assert.assertNotNull(v);
    }

    @Test(expectedExceptions = { DataIntegrityViolationException.class })
    public void testPersistErr() {
        User v = new User();
        v.setUsername(null);
        userRepository.persist(v);
    }
}
