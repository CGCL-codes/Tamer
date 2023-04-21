package actions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import actions.Action;
import actions.events.ActionEvent;
import actions.events.ActionListener;
import beans.Token;

public class SensitiveActionListener implements ActionListener {

    public void beforeAction(ActionEvent event) throws ServletException {
        Action action = (Action) event.getSource();
        if (action.isSensitive()) {
            HttpServletRequest req = event.getRequest();
            String requestToken = (String) req.getParameter("token"), sessionToken = (String) req.getSession().getAttribute("token");
            if (sessionToken == null || requestToken == null || !sessionToken.equals(requestToken)) {
                throw new ServletException("Sorry, but this is a sensitive page " + "that can't be resubmitted.");
            }
        }
    }

    public void afterAction(ActionEvent event) throws ServletException {
        Action action = (Action) event.getSource();
        HttpServletRequest req = event.getRequest();
        HttpSession session = req.getSession();
        if (action.hasSensitiveForms()) {
            Token token = new Token(req);
            session.setAttribute("token", token.toString());
            req.setAttribute("token", token.toString());
        }
        if (action.isSensitive()) {
            session.removeAttribute("token");
        }
    }
}
