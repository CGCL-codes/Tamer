package com.sfpay.histran.service;

import java.security.NoSuchAlgorithmException;
import javax.annotation.Resource;
import org.junit.Test;
import com.sfpay.framework.web.test.SpringTestCase;
import com.sfpay.histran.domain.User;

public class UserServiceImplTest extends SpringTestCase {

    @Resource
    IUserService userService;

    @Test
    public void testUserService() throws NoSuchAlgorithmException {
        User user = new User();
        user.setUsername("sf");
        user.setPassword("888888");
        if (logger.isDebugEnabled()) {
            logger.debug(userService.login(user).toString());
        }
    }
}
