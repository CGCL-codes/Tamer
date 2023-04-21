package medieveniti.servlet;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import medieveniti.manager.PlayerManager;
import medieveniti.object.Player;
import medieveniti.system.AppConfigUserHttpServletImpl;

/**
 * @author max
 * 
 */
public class PlayerListServlet extends AppConfigUserHttpServletImpl {

    public void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        getAppConfig().getSessionManager().check(request, response);
        org.hibernate.Session session = getAppConfig().getHibernateUtil().getSessionFactory().openSession();
        session.beginTransaction();
        List<Player> table = PlayerManager.getAllPlayers(session);
        request.setAttribute("table", table);
        session.getTransaction().commit();
        request.getRequestDispatcher("/WEB-INF/view/game/player-list.jsp").forward(request, response);
    }
}
