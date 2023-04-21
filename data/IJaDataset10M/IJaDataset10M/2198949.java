package org.technbolts.web.controller;

import static org.easymock.EasyMock.*;
import org.springframework.web.servlet.ModelAndView;
import org.technbolts.domain.service.GreetingService;
import junit.framework.TestCase;

public class HomeControllerTest extends TestCase {

    private GreetingService greetingService;

    private HomeController controller;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        greetingService = createMock(GreetingService.class);
        controller = new HomeController();
    }

    public void testHandleRequestView() throws Exception {
        final String tips = "Hello here!";
        expect(greetingService.getTipOfTheDay()).andReturn(tips);
        replay(greetingService);
        controller.setGreetingService(greetingService);
        ModelAndView modelAndView = controller.handleRequest(null, null);
        assertEquals("home", modelAndView.getViewName());
        assertEquals("Hello here!", modelAndView.getModel().get("tipOfTheDay"));
        verify(greetingService);
    }
}
