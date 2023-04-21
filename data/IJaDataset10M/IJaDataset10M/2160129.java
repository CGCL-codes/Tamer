package medieveniti.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import medieveniti.manager.PlayerManager;
import medieveniti.object.Player;
import medieveniti.system.AppConfigUserHttpServletImpl;

public class PlayerProfileServlet extends AppConfigUserHttpServletImpl {

    public void doService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getAppConfig().getSessionManager().check(request, response);
        org.hibernate.Session session = getAppConfig().getHibernateUtil().getSessionFactory().openSession();
        session.beginTransaction();
        Player player = PlayerManager.getPlayerById(session, Integer.parseInt(request.getPathInfo().substring(1)));
        request.setAttribute("player", player);
        request.getRequestDispatcher("/WEB-INF/view/game/player-profile.jsp").forward(request, response);
    }
}
