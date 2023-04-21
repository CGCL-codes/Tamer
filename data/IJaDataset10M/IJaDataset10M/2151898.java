package biut;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.google.appengine.api.datastore.Key;

public class UpdateEspacePersoController extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(true);
        EspacePerso E = EspacePerso.readEspacePerso((String) session.getAttribute("idMembre"));
        if (req.getParameter("ops").equals("ajouterDoc")) {
            E.addDocumentISBN(Long.parseLong(req.getParameter("ISBN")));
            try {
                resp.sendRedirect("/trueIndex.jsp?validate=addfav&&page=document&ISBN=" + Long.parseLong(req.getParameter("ISBN")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (req.getParameter("ops").equals("supprimerDoc")) {
            E.deleteDocument(Long.parseLong(req.getParameter("ISBN")));
            try {
                resp.sendRedirect("/trueIndex.jsp?validate=supprfav&&page=document&ISBN=" + Long.parseLong(req.getParameter("ISBN")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (req.getParameter("ops").equals("ajouter")) {
            E.addDocumentISBN(Long.parseLong(req.getParameter("isbn")));
            try {
                resp.sendRedirect("/espaceperso.jsp?validate=addfav");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            E.deleteDocument(Long.parseLong(req.getParameter("isbn")));
            try {
                resp.sendRedirect("/espaceperso.jsp?validate=supprfav");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
