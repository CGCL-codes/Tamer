package org.magicbox.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.magicbox.util.Constant;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Controller di benvenuto dopo il login, ridirige in base al ruolo
 * 
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since jdk 1.6.0
 * @version 3.0
 */
public class WelcomeController extends AbstractController {

    public ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse res) throws Exception {
        ModelAndView mav = new ModelAndView();
        if (req.isUserInRole(Constant.ROLE_ADMIN)) {
            mav.setViewName(Constant.REDIRECT_WELCOME_ADMIN);
        } else if (req.isUserInRole(Constant.ROLE_USER)) {
            mav.setViewName("benvenuto/welcome");
            mav.addObject(Constant.USER, req.getRemoteUser());
        }
        return mav;
    }
}
