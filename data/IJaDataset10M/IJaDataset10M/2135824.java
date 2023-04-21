package ar.fi.uba.apolo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.mvc.*;
import org.springframework.web.servlet.ModelAndView;

public class FMenuCtl implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse arg1) throws Exception {
        return new ModelAndView("fmenu");
    }
}
