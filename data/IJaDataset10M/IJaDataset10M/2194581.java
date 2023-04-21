package se.infact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import se.infact.domain.User;
import se.infact.manager.UserManager;

public class ControllerUtil {

    protected UserManager userManager;

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    protected ModelAndView getModelAndView(String jspContent) {
        return getModelAndView(jspContent, "defaultmenu");
    }

    protected ModelAndView getModelAndView(String jspContent, String jspMenu) {
        final ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("contentView", jspContent);
        modelAndView.addObject("menuView", jspMenu);
        User currentUser = userManager.getAuthenticatedUser();
        modelAndView.addObject("currentuser", currentUser);
        return modelAndView;
    }

    protected ModelAndView getOnlyContentModelAndView(String jspContent) {
        final ModelAndView modelAndView = new ModelAndView("slimcontent");
        modelAndView.addObject("contentView", jspContent);
        return modelAndView;
    }

    protected ModelAndView getModelAndViewMinimal(String jspContent) {
        final ModelAndView modelAndView = new ModelAndView("minimal");
        modelAndView.addObject("contentView", jspContent);
        return modelAndView;
    }
}
