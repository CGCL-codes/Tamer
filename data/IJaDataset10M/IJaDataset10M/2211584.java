package actions;

import java.rmi.RemoteException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Displays next page with white list.
 * @author marcin
 * 
 */
public class NextWhiteList extends CommonConfigSpam {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, RemoteException {
        try {
            Integer start = (Integer) request.getSession().getAttribute("whiteListStart");
            if (start == null) {
                return resetSpamConfig(request, mapping);
            }
            Integer newStart = new Integer(10 + start.intValue());
            request.getSession().setAttribute("whiteListStart", newStart);
            return renewSpamConfig(request, mapping);
        } catch (Exception e) {
            e.printStackTrace();
            return mapping.findForward("notConnected");
        }
    }
}
