package org.justinengels.facebook.bst.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.justinengels.facebook.bst.business.BusinessRules;
import org.justinengels.facebook.bst.entity.persistent.Player;
import org.justinengels.facebook.bst.facebook.FacebookPortal;
import org.justinengels.facebook.bst.util.DaoUtility;
import org.justinengels.facebook.bst.util.UserTransientDataAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller used to get help content.
 *
 * @author Justin Engels
 */
@Controller
@RequestMapping("/help.do")
public class ViewHelpController extends StandardForm {

    private final Logger log = Logger.getLogger(ViewHelpController.class);

    /**
	 * This method is expected to be invoked by the Spring framework only. The autowired objects are sourced from the application context configuration XML file by Spring.
	 * @param daoUtility Object used to access the database
	 * @param fbPortal Object used to access Facebook
	 * @param businessRules Object used to access business rules.
	 * @param userTransientDataAccessor Object used to access user transient data.
	 */
    @Autowired
    public ViewHelpController(DaoUtility daoUtility, FacebookPortal fbPortal, BusinessRules businessRules, UserTransientDataAccessor userTransientDataAccessor) {
        super(daoUtility, fbPortal, businessRules, userTransientDataAccessor);
    }

    /**
	 * This method handles any GET requests made to the /groups.do URL
	 * Functionality is delegated to the <code>processRequest</code> method.
	 * @param model An object map accessible by the view. Used to transport results to the view.
	 * @param request The HTTP request made to invoke this controller
	 * @param response The response associated with the HTTP request
	 * @param session The current HTTP session (associated with the request being made)
	 * @return The String URL or instruction to access the view
	 */
    @RequestMapping(method = RequestMethod.GET)
    public String handleGetRequest(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        return processRequest(model, session, request, response);
    }

    /**
	 * This method handles any POST requests made to the /groups.do URL
	 * Functionality is delegated to the <code>processRequest</code> method.
	 * @param model An object map accessible by the view. Used to transport results to the view.
	 * @param request The HTTP request made to invoke this controller
	 * @param response The response associated with the HTTP request
	 * @param session The current HTTP session (associated with the request being made)
	 * @return The String URL or instruction to access the view
	 */
    @RequestMapping(method = RequestMethod.POST)
    public String processSubmit(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        return processRequest(model, session, request, response);
    }

    /**
	 * This method identifies the current logged in User and then delegates to the _getGroups method.
	 * @param model An object map accessible by the view. Used to transport results to the view.
	 * @param request The HTTP request made to invoke this controller
	 * @param response The response associated with the HTTP request
	 * @param session The current HTTP session (associated with the request being made)
	 * @return The String URL or instruction to access the view
	 */
    private String processRequest(ModelMap model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        Player player = getLoggedInPlayer(request, response);
        setLastUrlRequested(session, player, request.getRequestURI());
        return "help";
    }
}
