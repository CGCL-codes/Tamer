package biut;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.KeyFactory;

public class UpdateRecommandationController extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        Recommandation recom = Recommandation.readRecommandation((KeyFactory.stringToKey(req.getParameter("idRecommandation"))));
        String content = req.getParameter("content");
        String filiere = req.getParameter("filiere");
        String promotion = req.getParameter("promotion");
        recom.updateRecommandation(filiere, promotion, content);
        try {
            resp.sendRedirect("/Recommandation.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
