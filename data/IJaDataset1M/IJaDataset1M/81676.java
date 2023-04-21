package com.javaitis.service.impl;

import com.javaitis.Constants;
import com.javaitis.dao.RoleDao;
import com.javaitis.dao.UserDao;
import com.javaitis.model.Role;
import com.javaitis.model.User;
import com.javaitis.service.UserExistsException;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.DataIntegrityViolationException;

public class UserManagerImplTest extends BaseManagerMockTestCase {

    private UserManagerImpl userManager = new UserManagerImpl();

    private RoleManagerImpl roleManager = new RoleManagerImpl();

    private UserDao userDao = null;

    private RoleDao roleDao = null;

    @Before
    public void setUp() throws Exception {
        userDao = context.mock(UserDao.class);
        userManager.setUserDao(userDao);
        roleDao = context.mock(RoleDao.class);
        roleManager.setRoleDao(roleDao);
    }

    @Test
    public void testGetUser() throws Exception {
        final User testData = new User("1");
        testData.getRoles().add(new Role("user"));
        context.checking(new Expectations() {

            {
                one(userDao).get(with(equal(1L)));
                will(returnValue(testData));
            }
        });
        User user = userManager.getUser("1");
        assertTrue(user != null);
        assert user != null;
        assertTrue(user.getRoles().size() == 1);
    }

    @Test
    public void testSaveUser() throws Exception {
        final User testData = new User("1");
        testData.getRoles().add(new Role("user"));
        context.checking(new Expectations() {

            {
                one(userDao).get(with(equal(1L)));
                will(returnValue(testData));
            }
        });
        final User user = userManager.getUser("1");
        user.setPhoneNumber("303-555-1212");
        context.checking(new Expectations() {

            {
                one(userDao).saveUser(with(same(user)));
                will(returnValue(user));
            }
        });
        User returned = userManager.saveUser(user);
        assertTrue(returned.getPhoneNumber().equals("303-555-1212"));
        assertTrue(returned.getRoles().size() == 1);
    }

    @Test
    public void testAddAndRemoveUser() throws Exception {
        User user = new User();
        user = (User) populate(user);
        context.checking(new Expectations() {

            {
                one(roleDao).getRoleByName(with(equal("ROLE_USER")));
                will(returnValue(new Role("ROLE_USER")));
            }
        });
        Role role = roleManager.getRole(Constants.USER_ROLE);
        user.addRole(role);
        final User user1 = user;
        context.checking(new Expectations() {

            {
                one(userDao).saveUser(with(same(user1)));
                will(returnValue(user1));
            }
        });
        user = userManager.saveUser(user);
        assertTrue(user.getUsername().equals("john"));
        assertTrue(user.getRoles().size() == 1);
        context.checking(new Expectations() {

            {
                one(userDao).remove(with(equal(5L)));
            }
        });
        userManager.removeUser("5");
        context.checking(new Expectations() {

            {
                one(userDao).get(with(equal(5L)));
                will(returnValue(null));
            }
        });
        user = userManager.getUser("5");
        assertNull(user);
    }

    @Test
    public void testUserExistsException() {
        final User user = new User("admin");
        user.setEmail("matt@raibledesigns.com");
        final Exception ex = new DataIntegrityViolationException("");
        context.checking(new Expectations() {

            {
                one(userDao).saveUser(with(same(user)));
                will(throwException(ex));
            }
        });
        try {
            userManager.saveUser(user);
            fail("Expected UserExistsException not thrown");
        } catch (UserExistsException e) {
            log.debug("expected exception: " + e.getMessage());
            assertNotNull(e);
        }
    }
}
