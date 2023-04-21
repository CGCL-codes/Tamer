package action;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import bean.Travelogue;
import bean.User;
import dao.TravelogueDAO;
import dao.UserDAO;

public class ShowTravelogueAction extends Action {

    /** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        UserDAO userDAO = new UserDAO();
        TravelogueDAO travelogueDAO = new TravelogueDAO();
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession(false);
        int id = Integer.parseInt(request.getParameter("tid"));
        Travelogue tl = travelogueDAO.findById(id);
        if (tl != null) {
            request.setAttribute("travelogue", tl);
            return mapping.findForward("showTL");
        } else {
            errors.add("notExist", new ActionMessage("Travelogue.notExist"));
        }
        saveErrors(request, errors);
        return mapping.getInputForward();
    }
}
