package mecca.sis.view.register;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.VelocityContext;

/**
 * @author Shamsul Bahrin bin Abd Mutalib
 *
 * @version 0.1
 */
public class DefaultAction implements mecca.portal.action.ActionTemplate {

    public boolean doAction(HttpServletRequest req, HttpServletResponse res, VelocityContext context) throws Exception {
        return true;
    }
}
