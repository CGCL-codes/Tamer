package test.web.controller;

import memento.service.iface.IUserService;
import memento.web.controller.user.UserHomeController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author rusty
 *
 */
@RunWith(JMock.class)
public final class UserHomeControllerTest {

    private final Log log = LogFactory.getLog(this.getClass().getName());

    private UserHomeController controller;

    private IUserService userService;

    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    Mockery context = new JUnit4Mockery();

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
        controller = new UserHomeController();
        userService = context.mock(IUserService.class);
        controller.setUserService(userService);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void testGetMethod() throws Exception {
        request.setMethod("POST");
        try {
            controller.handleRequest(request, response);
            Assert.assertTrue("should have thrown RequestMethodNotSupportedException", 1 > 0);
        } catch (final HttpRequestMethodNotSupportedException e) {
        }
    }

    @Test
    public void testUserParams() throws Exception {
        request.setMethod("GET");
        request.addParameter("id", "1");
        context.checking(new Expectations() {

            {
                one(userService).loadUserById("1");
            }
        });
        final ModelAndView mav = controller.handleRequest(request, response);
        Assert.assertNotNull(mav);
        log.debug(String.format("model: %s", mav.getModel()));
    }
}
