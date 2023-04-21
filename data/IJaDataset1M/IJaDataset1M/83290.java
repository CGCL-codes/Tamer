package test.web.controller;

import jmemento.web.controller.user.UserHomeController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author rusty
 */
public final class UserHomeControllerV2Test extends AbstractTransactionalSpringContextTests {

    private final Log log = LogFactory.getLog(getClass());

    private UserHomeController userHomeController;

    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    public void testRequest() throws Exception {
        request.setMethod("GET");
        request.addParameter("id", "1");
        final ModelAndView mav = userHomeController.handleRequest(request, response);
        assertNotNull(mav);
        log.debug(mav);
    }

    @Override
    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Override
    protected String[] getConfigLocations() {
        return (new String[] { "classpath:applicationContext.xml" });
    }

    public void setUserHomeController(final UserHomeController _userHomeController) {
        userHomeController = _userHomeController;
    }
}
