package dev.cinema.struts;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import dev.cinema.ejb.session.ClientManagerRemote;
import dev.cinema.model.Client;
import dev.cinema.throwable.EntityExistsEjbException;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.TokenProcessor;

/**
 *
 * @author alessia
 */
public class CreateClientAccountAction extends org.apache.struts.action.Action {

    private static final String SUCCESS = "success";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!TokenProcessor.getInstance().isTokenValid(request)) {
            return mapping.getInputForward();
        }
        TokenProcessor.getInstance().resetToken(request);
        CreateClientAccountForm formCreate = (CreateClientAccountForm) form;
        String username = formCreate.getUsername();
        String password = formCreate.getPassword();
        String name = formCreate.getName();
        String surname = formCreate.getSurname();
        String email = formCreate.getEmail();
        String phoneNumber = formCreate.getPhoneNumber();
        InitialContext ic = (InitialContext) request.getSession().getServletContext().getAttribute("ic");
        ClientManagerRemote cm = (ClientManagerRemote) ic.lookup("java:global/cinema-ejb/ClientManagerBean");
        Client client = new Client(username, name, surname, email, phoneNumber);
        try {
            client = cm.createClient(client, password);
        } catch (EntityExistsEjbException e) {
            ActionMessages error = new ActionMessages();
            error.add("username", new ActionMessage("error.username.exist", username));
            saveErrors(request, error);
            return mapping.getInputForward();
        }
        HttpSession session = request.getSession();
        session.setAttribute("clientId", client.getIdClient());
        session.setAttribute("username", username);
        String retPage = request.getParameter("retPage");
        if (retPage != null && !"".equals(retPage)) {
            return new ActionForward(retPage, true);
        }
        return mapping.findForward(SUCCESS);
    }
}
