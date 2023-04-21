package mortiforo.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mortiforo.beans.Topic;
import mortiforo.beans.UserPermissions;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author fjleon
 *
 * Moves a topic to another forum. Great when people post off-topic messages.
 */
public class MoveTopic extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        String logged_user = null;
        boolean[] permissions;
        permissions = new boolean[UserPermissions.permission_count];
        if (session != null) {
            logged_user = (String) session.getAttribute("logged_user_name");
            permissions = (boolean[]) session.getAttribute("permission");
        }
        if (logged_user == null || logged_user.equals("") || permissions[UserPermissions.perm_moderation_topic_move] == false) {
            request.setAttribute("not_admin", "");
            RequestDispatcher rd = request.getRequestDispatcher("movetopic.ftl");
            rd.forward(request, response);
            return;
        }
        request.setAttribute("navigation_topic_move", "");
        String topic_id = request.getParameter("id");
        SqlMapClient sqlMap = (SqlMapClient) getServletContext().getAttribute("sqlmap");
        List current_forums;
        try {
            current_forums = sqlMap.queryForList("getForumNames", null);
            request.setAttribute("forums", current_forums);
            request.setAttribute("topic_id", topic_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        RequestDispatcher rd = request.getRequestDispatcher("movetopic.ftl");
        rd.forward(request, response);
        return;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        String logged_user = null;
        boolean[] permissions;
        permissions = new boolean[UserPermissions.permission_count];
        if (session != null) {
            logged_user = (String) session.getAttribute("logged_user_name");
            permissions = (boolean[]) session.getAttribute("permission");
        }
        if (logged_user == null || logged_user.equals("") || permissions[UserPermissions.perm_administration] == false) {
            request.setAttribute("not_admin", "");
            RequestDispatcher rd = request.getRequestDispatcher("movetopic.ftl");
            rd.forward(request, response);
            return;
        }
        request.setAttribute("navigation_topic_move", "");
        String forum_id = request.getParameter("forum_list");
        String topic_id = request.getParameter("topic_id");
        Topic topic = new Topic();
        topic.setId(new Integer(topic_id).intValue());
        topic.setForum_id(new Integer(forum_id).intValue());
        SqlMapClient sqlMap = (SqlMapClient) getServletContext().getAttribute("sqlmap");
        try {
            sqlMap.startTransaction();
            sqlMap.update("moveTopic", topic);
            sqlMap.commitTransaction();
            request.setAttribute("topic_move_ok", "");
            request.setAttribute("topic_id", topic_id);
            RequestDispatcher rd = request.getRequestDispatcher("movetopic.ftl");
            rd.forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                sqlMap.endTransaction();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }
}
