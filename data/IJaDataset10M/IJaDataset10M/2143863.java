package com.bardsoftware.foronuvolo.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.bardsoftware.foronuvolo.data.ForumUser;

public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getRequestURL().toString().startsWith(ForoNuvoloConstants.FORUM_DOMAIN)) {
            return;
        }
        resp = removeVKCookie(req, resp);
        req.setAttribute("user_id", ForumUser.ANONYMOUS.getID());
        req.setAttribute("friendConnectID", ForoNuvoloConstants.FRIEND_CONNECT_SITE_ID);
        req.setAttribute("redirectUrl", ForoNuvoloConstants.FORUM_JSP_PATH);
        getServletContext().getRequestDispatcher(JspHelper.getJspName("logout.jsp")).forward(req, resp);
        req.getSession().setAttribute("user_id", null);
    }

    private HttpServletResponse removeVKCookie(HttpServletRequest req, HttpServletResponse resp) {
        String cookieName = "vk_app_" + ForoNuvoloConstants.VK_OPEN_API_APP_ID;
        if (req.getCookies() == null) {
            return resp;
        }
        Cookie authCookie = null;
        for (Cookie cookie : req.getCookies()) {
            if (cookieName.equals(cookie.getName())) {
                authCookie = cookie;
                break;
            }
        }
        if (authCookie == null) {
            return resp;
        }
        return eraseCookie(authCookie, resp);
    }

    private String getCookieInfo(Cookie authCookie) {
        StringBuilder sb = new StringBuilder();
        sb.append("Cookie:  ").append("\n  name   : " + authCookie.getName()).append("\n  value  : " + authCookie.getValue()).append("\n  domain : " + authCookie.getDomain()).append("\n  Path   : " + authCookie.getPath()).append("\n  comment: " + authCookie.getComment()).append("\n  maxAge : " + authCookie.getMaxAge()).append("\n  Version: " + authCookie.getVersion()).append("\n  Secure : " + authCookie.getSecure());
        return sb.toString();
    }

    private HttpServletResponse eraseCookie(Cookie cookieForDelete, HttpServletResponse resp) {
        resp.setContentType("text/html");
        Cookie cookie = new Cookie(cookieForDelete.getName(), "");
        cookie.setMaxAge(-1);
        resp.addCookie(cookie);
        return resp;
    }
}
