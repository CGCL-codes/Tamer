package test.org.magicbox.controller;

import junit.framework.TestCase;
import org.magicbox.controller.ChangePasswordFormController;
import org.magicbox.domain.Credenziali;
import org.magicbox.util.Constant;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import test.org.magicbox.SpringFactory;

public class ChangePasswordFormControllerTest extends TestCase {

    public void setUp() throws Exception {
        _ctx = SpringFactory.getXmlWebApplicationContext();
        _controller = (ChangePasswordFormController) _ctx.getBean("magicbox.changePasswordFormController");
        _reqMock = new MockHttpServletRequest();
        _resMock = new MockHttpServletResponse();
    }

    public void testUpdatePassword() throws Exception {
        _reqMock = new MockHttpServletRequest("GET", "/changeP.page");
        _reqMock.getSession().setAttribute(Constant.ID_CENTRO_SESSIONE, 46);
        Credenziali credenziali = new Credenziali();
        credenziali.setUsername("pippo");
        credenziali.setOldPassword("pippo");
        credenziali.setPassword("michele");
        credenziali.setConfermaPassword("michele");
        ModelAndView mav = _controller.onSubmit(_reqMock, _resMock, credenziali, new BindException(credenziali, Constant.CREDENZIALI));
        assertEquals("redirect:welcome.page", mav.getViewName());
    }

    public void tearDown() throws Exception {
        _reqMock = null;
        _resMock = null;
    }

    private ChangePasswordFormController _controller;

    private XmlWebApplicationContext _ctx;

    private MockHttpServletRequest _reqMock;

    private MockHttpServletResponse _resMock;
}
