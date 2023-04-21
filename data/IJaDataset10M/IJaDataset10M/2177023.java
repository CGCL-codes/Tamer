package ch.serva.actions;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import ch.serva.ServaConstants;
import ch.serva.actions.results.Result;
import ch.serva.actions.results.Success;

/**
 * An action to logout of this application.
 * 
 * @author Lukas Blunschi
 */
public class LogoutAction implements Action {

    public static final String NAME = "logout";

    public Result execute(HttpServletRequest req, EntityManager em) {
        req.getSession().removeAttribute(ServaConstants.A_USER_ID);
        return new Success();
    }
}
