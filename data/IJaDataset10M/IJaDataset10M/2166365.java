package example.chapter7.web;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import example.chapter7.UserService;

/**
 * 处理用户登录表单
 */
public class LoginController implements Controller {

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            userService.login(username, password);
            request.getSession().setAttribute("username", username);
            return new ModelAndView("welcome");
        } catch (RuntimeException e) {
            Map model = new HashMap();
            model.put("message", e.getMessage());
            return new ModelAndView("login", model);
        }
    }
}
