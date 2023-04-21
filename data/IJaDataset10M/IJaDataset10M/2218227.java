package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import service.Service;

public class XaController implements Controller {

    private Service service;

    public ModelAndView handleRequest(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
        service.doJob();
        return new ModelAndView("xa");
    }

    public void setService(Service service) {
        this.service = service;
    }
}
