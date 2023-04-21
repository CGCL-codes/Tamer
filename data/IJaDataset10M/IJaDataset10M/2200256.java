package org.blueoxygen.cimande.security.login.interceptors;

import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.util.TextParseUtil;

/**
 * @author Dian Aditya
 *
 */
public class LoginInterceptor implements Interceptor {

    public static String LOGIN_KEY = "loggedIn";

    public static String LOGIN_USER = "user";

    public static String LOGIN_CIMANDE_USER = "GA_USER";

    public static String LOGIN_CIMANDE_SITE = "GS_USER";

    private Set<String> extensions;

    private String loginAction = "/backend/user/index";

    private String loginPage = "/backend/user/index";

    private String redirectUri;

    public void setLoginAction(String loginAction) {
        this.loginAction = loginAction;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public void setIgnoreExtensions(String ignoreExtension) {
        this.extensions = TextParseUtil.commaDelimitedStringToSet(ignoreExtension);
    }

    public void destroy() {
    }

    public void init() {
    }

    public String intercept(ActionInvocation invocation) throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        String servletPath = request.getServletPath();
        String extension = servletPath.substring(servletPath.lastIndexOf('.') + 1).toLowerCase();
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (servletPath.equals(loginAction) || servletPath.equals(loginPage) || extensions.contains(extension)) {
            System.out.println("loginAction");
            return invocation.invoke();
        } else if (request.getSession(true).getAttribute(LOGIN_CIMANDE_USER) == null) {
            System.out.println("redirecting");
            request.setAttribute("redirectUri", contextPath + loginPage + "?redirectUri=" + servletPath);
            redirectUri = contextPath + loginPage + "?redirectUri=" + servletPath;
            System.out.println(redirectUri);
            return "redirectUri";
        }
        return invocation.invoke();
    }

    public String getRedirectUri() {
        return redirectUri;
    }
}
