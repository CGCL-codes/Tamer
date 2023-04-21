package am.controler.servlet;

import am.controler.exceptions.BaseException;
import am.controler.servlet.utils.WebMethod;
import am.model.dao.User;
import am.model.factories.StdUserFactory;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Administrator
 */
public class UsersServlet extends BasicServlet {

    public static final String requestParameterUser = "user";

    public static final String requestParameterPass = "pass";

    public static final String sessionAttributeUserObject = "userObject";

    protected StdUserFactory userFactory;

    /**
   * @param String user
   * @param String pass
   * @return String
   * @throws BaseException
   */
    @WebMethod(parameters = { requestParameterUser, requestParameterPass })
    public String login() throws BaseException {
        String user = request.getParameter(requestParameterUser);
        String pass = request.getParameter(requestParameterPass);
        User u = userFactory.getUser(user, pass);
        if (u == null) {
            throw new BaseException("User not found");
        }
        request.getSession().setAttribute(sessionAttributeUserObject, u);
        return u.toJSONObject().toJSONString();
    }

    @WebMethod(parameters = {  })
    public String getUserStatus() {
        JSONObject jsonRes = new JSONObject();
        User u = (User) request.getSession().getAttribute(sessionAttributeUserObject);
        if (u == null) {
            jsonRes.put("User status error", "User does not exist");
        } else {
            jsonRes = u.toJSONObject();
        }
        return jsonRes.toJSONString();
    }

    @WebMethod(parameters = {  })
    public String listAllUsers() {
        List<User> users = userFactory.listAllUsers();
        JSONArray res = new JSONArray();
        for (User u : users) {
            res.add(u.toJSONObject());
        }
        return res.toJSONString();
    }

    @WebMethod(parameters = { "user", "pass" })
    public String createUser() throws BaseException {
        String user = request.getParameter(requestParameterUser);
        String pass = request.getParameter(requestParameterPass);
        Integer uid = userFactory.createUser(user, pass);
        JSONObject res = new JSONObject();
        res.put("result", "ok");
        res.put("id", uid);
        return res.toJSONString();
    }

    @Override
    protected void initialize() {
        userFactory = new StdUserFactory();
        userFactory.setSqlSession();
    }

    @Override
    protected void destroy() {
        userFactory.closeSqlSession();
        userFactory = null;
    }
}
