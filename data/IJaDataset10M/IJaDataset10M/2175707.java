package com.ever365.security;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.gqu.servlet.RandomImgServlet;
import net.gqu.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Servlet implementation class RegisterServlet
 */
public class RegisterServlet extends HttpServlet {

    private Log logger = LogFactory.getLog(RegisterServlet.class);

    private static final long serialVersionUID = 1L;

    private CookieService cookieService;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
    }

    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        userService = (UserService) ctx.getBean("userService");
        cookieService = (CookieService) ctx.getBean("cookieService");
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("pwd");
        String passwordc = request.getParameter("pwdcfm");
        String from = request.getParameter("from");
        if (!from.startsWith("/")) {
            response.setStatus(400);
            return;
        }
        Object rndimg = request.getSession().getAttribute(RandomImgServlet.VALIDATE_CODE);
        logger.debug("register: " + username + "  " + email + " " + password + "  ");
        if (username == null || username.length() < 4 || !StringUtils.isUserId(username)) {
            request.getSession().setAttribute("registerError", "用户名长度要大于4个字符,用英文字母与数字");
            response.sendRedirect(from);
            return;
        }
        if (email == null || !StringUtils.isEmail(email)) {
            request.getSession().setAttribute("registerError", "邮件账号名称不正确");
            response.sendRedirect(from);
            return;
        }
        if (password == null || password.length() < 6) {
            request.getSession().setAttribute("registerError", "密码长度要大于6个字符");
            response.sendRedirect(from);
            return;
        }
        if (request.getParameter("randomimg") == null || !request.getParameter("randomimg").equals(rndimg)) {
            request.getSession().setAttribute("registerError", "验证码错误");
            response.sendRedirect(from);
            return;
        }
        try {
            if (userService.getUser(username) != null) {
                request.getSession().setAttribute("registerError", "同名用户已经存在，请更换其他账号");
                response.sendRedirect(from);
                return;
            }
            boolean random = userService.createUser(username, password, null, email, false);
            userService.incLogCount(username);
            request.getSession().setAttribute(SetUserFilter.AUTHENTICATION_USER, username);
            AuthenticationUtil.setCurrentAsGuest(false);
            AuthenticationUtil.setCurrentUser(username);
            cookieService.saveUserCookie(request, response, username);
            request.getSession().removeAttribute("loginError");
            if (request.getSession().getAttribute("redirectTo") != null) {
                response.sendRedirect((String) request.getSession().getAttribute("redirectTo"));
                return;
            } else {
                response.sendRedirect("/");
            }
            return;
        } catch (Exception e) {
            response.sendRedirect("/");
            return;
        }
    }

    private void removeAllAttr(HttpSession session) {
        Enumeration l = session.getAttributeNames();
        while (l.hasMoreElements()) {
            String object = (String) l.nextElement();
            session.removeAttribute(object);
        }
    }
}
