package educate.sis.bursary;

import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lebah.portal.action.RequestUtil;
import org.apache.velocity.VelocityContext;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.00
 */
public class ListSponsorAction implements lebah.portal.action.ActionTemplate {

    public boolean doAction(HttpServletRequest req, HttpServletResponse res, VelocityContext context) throws Exception {
        HttpSession session = req.getSession();
        SponsorModule.prepareList(context);
        return true;
    }
}
