package com.road.test.integration.controller;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeValue;
import static org.springframework.test.web.ModelAndViewAssert.assertViewName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.testng.annotations.Test;
import com.road.model.Passerby;
import com.road.site.RegisterController;

/**
 * @author Leopard
 * 
 */
@Test(groups = { "integration" })
public class TestRegisterController extends ControllerTestCase {

    @Autowired
    private RegisterController controller;

    public void testRegister() {
        String username = "路人甲";
        String email = "foo@bar.com";
        String password = "foobar";
        request.setParameter("username", username);
        request.setParameter("email", email);
        request.setParameter("password", password);
        request.setMethod(METHOD_POST);
        try {
            ModelAndView mv = controller.handleRequest(request, response);
            assertViewName(mv, "loginView");
            assertModelAttributeValue(mv, "message", "register.success");
            assertEquals(1, simpleJdbcTemplate.queryForInt("select count(*) from passerby where name=? and email=? and password=?", username, email, password));
        } catch (Exception e) {
            fail();
        }
    }

    public void testRegisterFailed() {
        Passerby user = new Passerby("路人甲", "foo@bar.com", "111111");
        dao.save(user);
        request.setParameter("username", user.getName());
        request.setParameter("email", user.getEmail());
        request.setParameter("password", user.getPassword());
        request.setMethod(METHOD_POST);
        try {
            ModelAndView mv = controller.handleRequest(request, response);
            assertViewName(mv, "registerForm");
            assertModelAttributeValue(mv, "error", "register.failure");
        } catch (Exception e) {
            fail();
        }
    }
}
