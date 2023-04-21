package com.desktophrm.test.servicetest;

import org.junit.Before;
import org.junit.Test;
import com.desktophrm.service.UserService;

/**
 * @author hrmzone.cn(desktophrm.com)
 *
 */
public class UserServiceTest {

    private UserService us;

    @Before
    public void init() {
        us = new UserService();
    }

    @Test
    public void password() {
        String password = us.getPassword("hrmzone");
        System.out.println(password);
    }
}
