package biut;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.google.appengine.api.datastore.Key;

public class AddNotationController extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        int difficulte = 0;
        int pertinence = 0;
        int etatDocument = 0;
        if (req.getParameter("etatDocument") != null) {
            etatDocument = Integer.parseInt(req.getParameter("etatDocument"));
        } else {
            etatDocument = 0;
        }
        if (req.getParameter("pertinence") != null) {
            pertinence = Integer.parseInt(req.getParameter("pertinence"));
        } else {
            pertinence = 0;
        }
        if (req.getParameter("difficulte") != null) {
            difficulte = Integer.parseInt(req.getParameter("difficulte"));
        } else {
            difficulte = 0;
        }
        HttpSession session = req.getSession(true);
        String idMembre = (String) session.getAttribute("idMembre");
        Long idDoc = Long.parseLong(req.getParameter("ISBN"));
        new Notation(idMembre, idDoc, etatDocument, pertinence, difficulte);
        try {
            resp.sendRedirect("trueIndex.jsp?page=document&ISBN=" + idDoc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
