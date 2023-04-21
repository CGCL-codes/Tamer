package cn.ac.ntarl.portlet.sso.action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * MyEclipse Struts Creation date: 01-17-2008
 * 
 * XDoclet definition:
 * 
 * @struts.action path="/ssoLogin" name="ssoLoginForm"
 *                input="/form/ssoLogin.jsp" scope="request" validate="true"
 * @struts.action-forward name="failure" path="/sso/ssologin.jsp"
 * @struts.action-forward name="success" path="/sso/setcookie.jsp"
 */
public class SsoLoginAction extends HttpServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Method execute
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String local = request.getParameter("local");
        LoginPipe pipe = null;
        if ("true".equals(local)) {
            pipe = new LocalLogin();
        } else {
            pipe = new AppLogin(request.getParameter("appname"));
        }
        pipe.login(request, response);
    }
}
