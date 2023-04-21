package ch.oois.web.speaker;

import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ch.oois.infocore.ejb.UpdateClientDataBean;

/**
 * UpdateData: Mechanism to update the event data.
 */
@WebServlet(urlPatterns = "/UpdateData", asyncSupported = true)
public class UpdateDataServlet extends HttpServlet {

    private static final long serialVersionUID = -277914015930424042L;

    @EJB
    private UpdateClientDataBean m_updateClientData;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("ISO-8859-1");
        final String id = "dummy";
        AsyncContext ac = request.startAsync(request, response);
        m_updateClientData.getAsyncContexts().put(id, ac);
    }

    @Override
    public void destroy() {
        m_updateClientData.getAsyncContexts().clear();
    }
}
