package uipp.web.services.support;

import javax.servlet.http.HttpServletRequest;
import uipp.web.services.rest.exceptions.UnauthorizedException;
import com.flexive.shared.security.UserTicket;

/**
 * 
 * @author jindrich.basek
 *
 */
public class SecurityTools {

    public static UserTicket doAuth(HttpServletRequest httpServletRequest) {
        UserTicket ticket = null;
        try {
            ticket = (UserTicket) httpServletRequest.getAttribute("ticket");
            if (ticket == null) {
                throw new UnauthorizedException("Unknown user or bad password");
            }
        } catch (Exception e) {
            throw new UnauthorizedException("Unknown user or bad password");
        }
        return ticket;
    }
}
