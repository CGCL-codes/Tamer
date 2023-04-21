package eyrolles;

import eyrolles.modele.ModeleAccesIndisponibleException;
import eyrolles.modele.ModeleInterface;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionMessage;

public class EditEmployeAction extends GenericAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ModeleInterface mI = getModele();
        String target = new String("success");
        HttpSession session = request.getSession();
        if (session.getAttribute("USER") == null) {
            target = new String("login");
            ActionMessages errors = new ActionMessages();
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.login.required"));
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
            }
            return (mapping.findForward(target));
        }
        if (isCancelled(request)) {
            return (mapping.findForward(target));
        }
        try {
            Employe emp = convertEmployeForm2Employe(form);
            mI.updateUser(emp);
        } catch (ModeleAccesIndisponibleException e) {
            target = new String("login");
            ActionMessages errors = new ActionMessages();
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.database.error", e.getMessage()));
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
            }
        }
        return (mapping.findForward(target));
    }
}
