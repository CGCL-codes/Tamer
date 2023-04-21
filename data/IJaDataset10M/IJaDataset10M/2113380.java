package iConfWeb.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;

public interface ISecurityBean {

    /**
	 * Return true if the user is logged or not
	 * 
	 * @param request The request to process
	 * @return true if the user is logged
	 */
    public boolean isLoggued(HttpServletRequest request);

    /**
	 * Check if the user has Chairman privileges
	 * 
	 * @return true if the user has chairman privileges
	 */
    public boolean isChairman(HttpServletRequest request);

    /**
	 * Check if the user has referee privileges
	 * @return true if the user has referee privileges
	 */
    public boolean isReferee(HttpServletRequest request);

    /**
	 * Check if the user has author privileges
	 * @return true if the user has author privileges
	 */
    public boolean isAuthor(HttpServletRequest request);

    /**
	 * Check if the user has admin privleges 
	 * 
	 * @param request
	 * @return if the user is an admin
	 */
    public boolean isAdmin(HttpServletRequest request);

    /**
	 * Check if the user has Pc member privileges
	 * @return true if the user has Pc member privileges
	 */
    public boolean isPcMember(HttpServletRequest request);

    /**
	 * Check if the conference has been choosen
	 * @param request
	 * @return true if a conference has been choosen
	 */
    public boolean isConfChoosen(HttpServletRequest request);

    /**
	 *  The view to redirected to if the user is not logged
	 *  @return The view to redirect to 
	 **/
    public String getNotLogguedView();

    /**
	 * The view to redirected to if the conference has not been choosen
	 * @return The view to redirect to
	 */
    public String getConfNotChoosenView();

    /**
	 * Get the view to redirect to if the user has not enough privileges to 
	 * access a resource
	 * 
	 * @return The view to redirect to
	 */
    public String getBadRoleView();

    /**
	 * Set the message to display when an error occurs
	 * 
	 * @param request The request to use
	 */
    public void setNotLogguedMessage(HttpServletRequest request);

    /**
	 * Set the message to display if the conference has not been choosen
	 * 
	 * @param request The request to use
	 */
    public void setConfNotChoosenMessage(HttpServletRequest request);

    /**
	 * Set the message to display if the user has not enough privileges
	 * 
	 * @param request The request to use
	 */
    public void setNotEnoughPrivilegesMessage(HttpServletRequest request);
}
