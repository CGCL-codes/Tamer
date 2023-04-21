package jforum.actions;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jforum.DAO.MessageDAO;
import jforum.domain.Message;
import jforum.util.DBConnector;

/**
 *
 * @author Вадим
 */
public class SearchAuthorAction implements Action {

    public String perform(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String str_id = request.getParameter("id");
        int id = -1;
        try {
            id = Integer.parseInt(str_id);
        } catch (NumberFormatException ex) {
            request.getSession().setAttribute("errorMessage", "Id сообщения должно быть натуральным числом.");
            return ActionNames.error_page;
        }
        DBConnector conn = DBConnector.getInstance();
        MessageDAO dao = new MessageDAO(conn);
        List<Message> messages = dao.LoadForUser(id);
        request.setAttribute("messages", messages);
        return ActionNames.search_res_page;
    }
}
