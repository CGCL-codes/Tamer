package mable.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mable.service.PeopleService;
import mable.web.command.LoginCommand;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class AttachController implements Controller {

    private PeopleService peopleManager;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> PeopleList = new HashMap<String, Object>();
        ModelAndView mav = new ModelAndView();
        mav.setViewName("attach");
        mav.addObject("peopleTest", "testtest");
        return mav;
    }
}
