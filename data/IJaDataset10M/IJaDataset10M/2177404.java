package nl.gridshore.newsfeed.web.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * @author Jettro Coenradie
 */
public class LoginSecurityTag extends SecurityTag {

    private String destination = "/";

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public int doStartTag() throws JspException {
        if (isAuthenticated()) {
            String url = userService().createLogoutURL("/gs/news");
            write("<a href=\"" + url + "\">logout</a>");
        } else {
            String url = userService().createLoginURL(destination);
            write("<a href=\"" + url + "\">login</a>");
        }
        return SKIP_BODY;
    }
}
