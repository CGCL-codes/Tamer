package org.justinengels.facebook.bst.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.justinengels.facebook.bst.business.BusinessRules;
import org.justinengels.facebook.bst.entity.persistent.Player;
import org.justinengels.facebook.bst.entity.persistent.PlayerGroup;
import org.justinengels.facebook.bst.facebook.FacebookPortal;
import org.justinengels.facebook.bst.util.DaoUtility;
import org.justinengels.facebook.bst.util.UserTransientDataAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller used to remove one Players from a Group
 *
 * @author Justin Engels
 */
@Controller
@RequestMapping("/leaveGroup.do")
public class RemovePlayerGroupController extends StandardForm {

    private final Logger log = Logger.getLogger(RegisterPlayerGroupController.class);

    /**
	 * This method is expected to be invoked by the Spring framework only. The autowired objects are sourced from the application context configuration XML file by Spring.
	 * @param daoUtility Object used to access the database
	 * @param fbPortal Object used to access Facebook
	 * @param businessRules Object used to access business rules.
	 * @param userTransientDataAccessor Object used to access user transient data.
	 */
    @Autowired
    public RemovePlayerGroupController(DaoUtility daoUtility, FacebookPortal fbPortal, BusinessRules businessRules, UserTransientDataAccessor userTransientDataAccessor) {
        super(daoUtility, fbPortal, businessRules, userTransientDataAccessor);
    }

    /**
	 * Method to allow other areas of the application, for example test cases, to access the functionality of this controller.
	 * This method delegates to the <code>_removePlayerGroups</code> method.
	 * @param model An object map accessible by the view. Used to transport results to the view.
	 * @param playerGroups The PlayerGroups to register
	 */
    public String removePlayerGroups(ModelMap model, PlayerGroup playerGroup) {
        return _removePlayerGroup(model, playerGroup);
    }

    /**
	 * This method handles any GET requests made to the /leaveGroup.do URL
	 * Functionality is delegated to the <code>processRequest</code> method.
	 * @param model An object map accessible by the view. Used to transport results to the view.
	 * @param request The HTTP request made to invoke this controller
	 * @param response The response associated with the HTTP request
	 * @param session The current HTTP session (associated with the request being made)
	 * @param groupId 
	 * @param facebookIds
	 * @return The String URL or instruction to access the view
	 */
    @RequestMapping(method = RequestMethod.GET)
    public String handleGetRequest(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, Long groupId) {
        return processRequest(model, session, request, response, groupId);
    }

    /**
	 * This method handles any POST requests made to the /leaveGroup.do URL
	 * Functionality is delegated to the <code>processRequest</code> method.
	 * @param model An object map accessible by the view. Used to transport results to the view.
	 * @param request The HTTP request made to invoke this controller
	 * @param response The response associated with the HTTP request
	 * @param session The current HTTP session (associated with the request being made)
	 * @param groupId 
	 * @param facebookIds 
	 * @return The String URL or instruction to access the view
	 */
    @RequestMapping(method = RequestMethod.POST)
    public String processSubmit(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session, Long groupId) {
        return processRequest(model, session, request, response, groupId);
    }

    /**
	 * This method persists Player Groups to the data model.
	 * @param model An object map accessible by the view. Used to transport results to the view.
	 * @param player The Player to find Groups for.
	 * @return The String URL or instruction to access the view ("groups")
	 */
    private String _removePlayerGroup(ModelMap model, PlayerGroup playerGroup) {
        daoUtility.removePlayerGroup(playerGroup);
        return "confirmRemovePlayerGroup";
    }

    /**
	 * This method identifies the current logged in User and then delegates to the _removePlayerGroup method.
	 * @param model An object map accessible by the view. Used to transport results to the view.
	 * @param request The HTTP request made to invoke this controller
	 * @param response The response associated with the HTTP request
	 * @param session The current HTTP session (associated with the request being made)
	 * @param facebookIds 
	 * @param groupId 
	 * @return The String URL or instruction to access the view
	 */
    private String processRequest(ModelMap model, HttpSession session, HttpServletRequest request, HttpServletResponse response, Long groupId) {
        Player player = getLoggedInPlayer(request, response);
        PlayerGroup playerGroup = daoUtility.getPlayerGroup(groupId, player.getPlayerId());
        return _removePlayerGroup(model, playerGroup);
    }
}
