package seismosurfer.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import seismosurfer.domain.User;
import seismosurfer.util.Notification;

/**
 * This servlet sends an email to a user
 * who has forgot his password.
 *
 */
public class ForgotController extends BaseServlet {

    private static final long serialVersionUID = -2788650888151970095L;

    public static final String EMAIL = "email";

    public void init() throws ServletException {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter(USERNAME);
        Notification err = User.sendEmail(username);
        if (err.hasErrors()) {
            setErrors(err, request);
            forward("/forgot.jsp", request, response);
        } else {
            request.setAttribute(EMAIL, username);
            forward("/sentEmail.jsp", request, response);
        }
    }

    public void destroy() {
    }
}
