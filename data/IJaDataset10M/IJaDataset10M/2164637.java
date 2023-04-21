package by.brsu.portal.servlets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import by.brsu.portal.message.Message;
import by.brsu.portal.message.MessageDao;
import by.brsu.portal.user.User;

/**
 * @author Trutsik Eduard
 *
 */
public class SentMessages implements Action {

    private MessageDao messageDao = new MessageDao();

    private List<Message> messages;

    private Map<String, Object> map = new HashMap<String, Object>();

    @Override
    public boolean perform(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            messages = messageDao.findAllMessageUserFrom(user.getId());
            map.put("messages", messages);
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Object> getParametersMap() {
        return map;
    }
}
