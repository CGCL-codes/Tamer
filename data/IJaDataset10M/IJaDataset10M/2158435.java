package mortiforo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mortiforo.beans.NewUser;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @author espectro
 *
 * Activates the registered user in the database
 */
public class ConfirmAuthKey extends HttpServlet {

    private static final int ACTIVE = 1;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String authkey = request.getParameter("key");
        String id_string = request.getParameter("id");
        List confirmation_result = new ArrayList();
        if (id_string == null || authkey == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = -1;
        try {
            id = Integer.parseInt(id_string);
        } catch (NumberFormatException e1) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            SqlMapClient sqlMap = (SqlMapClient) getServletContext().getAttribute("sqlmap");
            Integer isActive = (Integer) sqlMap.queryForObject("getUserStatus", new Integer(id));
            if (isActive != null && isActive.intValue() == ACTIVE) {
                confirmation_result.add("user_already_registered");
            } else if (isActive == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } else {
                String database_authkey = (String) sqlMap.queryForObject("getUserKey", new Integer(id));
                if (database_authkey != null && database_authkey.compareTo(authkey) != 0) {
                    confirmation_result.add("user_authkey_mismatch");
                } else {
                    NewUser newuser = new NewUser();
                    newuser.setActive(ACTIVE);
                    newuser.setId(id);
                    try {
                        sqlMap.update("updateUserStatus", newuser);
                        confirmation_result.add("user_authkey_confirmed");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        request.setAttribute("confirmation_result", confirmation_result);
        RequestDispatcher rd = request.getRequestDispatcher("confirmauthkey.ftl");
        rd.forward(request, response);
    }
}
