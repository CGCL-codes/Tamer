package com.magic.jmock.demo.service.impl;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.magic.jmock.demo.dao.UserDAO;
import com.magic.jmock.demo.model.User;
import com.magic.jmock.demo.service.UserService;
import com.magic.jmock.demo.service.impl.UserServiceImpl;
import junit.framework.*;
import org.jmock.Mockery;
import junit.framework.TestCase;
import org.jmock.Expectations;

public class UserServiceTest {

    Mockery context = new Mockery();

    UserService userService = null;

    UserDAO userDAO = null;

    @Before
    public void setUp() throws Exception {
        userService = new UserServiceImpl();
        userDAO = context.mock(UserDAO.class);
        userService.setUserDAO(userDAO);
    }

    @Test
    public void testGetUser() {
        final User fakeUser = new User();
        context.checking(new Expectations() {

            {
                one(userDAO).getUser();
                will(returnValue(fakeUser));
            }
        });
        User returnUser = userService.getUser();
        assertSame(fakeUser, returnUser);
    }

    @Test
    public void testSaveUser() {
        final User fakeUser = new User("John");
        context.checking(new Expectations() {

            {
                one(userDAO).getUser();
                will(returnValue(fakeUser));
            }
        });
        final User user = userService.getUser();
        assertEquals("John", user.getName());
        context.checking(new Expectations() {

            {
                one(userDAO).saveUser(fakeUser);
            }
        });
        user.setName("Mike");
        userService.saveUser(user);
        context.checking(new Expectations() {

            {
                one(userDAO).getUser();
                will(returnValue(user));
            }
        });
        User modifiedUser = userService.getUser();
        assertEquals("Mike", modifiedUser.getName());
    }

    @Test
    public void testSayHello() {
        final String message = "HelloSuperLeo";
        String result = userService.sayHello(message);
        assertSame(result, message);
    }
}
