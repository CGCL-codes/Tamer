package org.opengeotracker.hunting.web;

import java.util.ArrayList;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.opengeotracker.hunting.db.People;
import org.opengeotracker.hunting.db.PeopleService;
import org.opengeotracker.hunting.db.PeopleSite;
import org.opengeotracker.hunting.db.PeopleSiteService;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class LoginController extends SimpleFormController {

    private static Logger logger = Logger.getAnonymousLogger();

    private PeopleService peopleService;

    private PeopleSiteService peopleSiteService;

    public void setPeopleService(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    public void setPeopleSiteService(PeopleSiteService peopleSiteService) {
        this.peopleSiteService = peopleSiteService;
    }

    protected final ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws ServletException {
        LoginForm loginForm = (LoginForm) command;
        logger.info("User " + loginForm.getLoginName() + " trying to log in");
        boolean isValid = false;
        People p = null;
        try {
            p = peopleService.findByLogin(loginForm.getLoginName());
            isValid = peopleService.verifyPassword(p, loginForm.getPassword());
            logger.info("md5string=" + p.getSalt() + loginForm.getPassword() + " md5hex=" + p.getMd5hex());
        } catch (Exception e) {
            logger.info("Got exception when looking up user " + loginForm.getLoginName() + " - not found? ");
            logger.info("e=" + e.getMessage());
        }
        logger.info("User " + loginForm.getLoginName() + " login is " + isValid);
        if (isValid) {
            HttpSession hs = request.getSession();
            if (hs == null) {
                logger.info("HttpSession is null!!");
            }
            hs.setAttribute("userIsValid", true);
            hs.setAttribute("user", loginForm.getLoginName());
            ArrayList<PeopleSite> psa = peopleSiteService.findByPeopleId(p.getId());
            if (psa != null && psa.size() == 1) {
                PeopleSite ps = psa.get(0);
                hs.setAttribute("siteId", ps.getSite().getId());
                hs.setAttribute("siteRole", ps.getRole());
            } else {
                if (psa == null) {
                    logger.warning("Error looking up logged in users site(s)");
                } else {
                    logger.warning("User " + loginForm.getLoginName() + " have connections to " + psa.size() + " sites, currently only one is allowed!");
                }
            }
            return new ModelAndView(new RedirectView(getSuccessView()));
        } else {
            request.getSession().setAttribute("user", null);
            request.getSession().setAttribute("userIsValid", false);
            request.getSession().setAttribute("siteId", null);
            request.getSession().setAttribute("siteRole", null);
            return new ModelAndView("login", "loginForm", new LoginForm());
        }
    }

    protected Object formBackingObject(HttpServletRequest request) throws ServletException {
        request.getSession().setAttribute("userIsValid", true);
        LoginForm loginForm = new LoginForm();
        loginForm.setLoginName(null);
        loginForm.setPassword(null);
        return loginForm;
    }
}
