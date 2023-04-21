package selftest;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.Enumeration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class DemoServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        log("DemoServlet starts");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final PrintWriter pw = response.getWriter();
        try {
            pw.println("Hello from " + getClass());
            pw.println(" --- Request: ---");
            pw.println(request);
            {
                pw.println(" --- request.getAttributeNames() ---");
                final Enumeration attrNames = request.getAttributeNames();
                while (attrNames.hasMoreElements()) {
                    final String attrName = (String) attrNames.nextElement();
                    pw.println("Attr " + attrName + " = " + request.getAttribute(attrName));
                }
            }
            {
                pw.println(" --- request.getSession().getAttributeNames() ---");
                final HttpSession session = request.getSession();
                final Enumeration attrNames = session.getAttributeNames();
                while (attrNames.hasMoreElements()) {
                    final String attrName = (String) attrNames.nextElement();
                    pw.println("SAttr " + attrName + " = " + session.getAttribute(attrName));
                }
            }
            {
                pw.println(" --- request.getSession().getServletContext().getAttributeNames() ---");
                final ServletContext servletContext = request.getSession().getServletContext();
                final Enumeration attrNames = servletContext.getAttributeNames();
                while (attrNames.hasMoreElements()) {
                    final String attrName = (String) attrNames.nextElement();
                    pw.println("ctxAttr " + attrName + " = " + servletContext.getAttribute(attrName));
                }
            }
            pw.println(" --- Cookies ---");
            final Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    pw.println(cookie.getName() + " = " + cookie.getValue());
                }
            } else {
                pw.println("No cookies here");
            }
            pw.println(" --- ---");
            pw.println("request.getServletPath() = " + request.getServletPath());
            pw.println("request.getPathInfo() = " + request.getPathInfo());
            pw.println("request.getAuthType() = " + request.getAuthType());
            pw.println("request.getContextPath() = " + request.getContextPath());
            pw.println("request.getMethod() = " + request.getMethod());
            pw.println("request.getPathTranslated() = " + request.getPathTranslated());
            pw.println("request.getQueryString() = " + request.getQueryString());
            pw.println("request.getRemoteUser() = " + request.getRemoteUser());
            final Principal principal = request.getUserPrincipal();
            if (principal == null) {
                pw.println("user principal is null");
            } else {
                pw.println("principal.getUserPrincipal().class = " + principal.getClass().getName() + "'");
                pw.println("principal name is '" + principal.getName() + "'");
            }
        } finally {
            pw.flush();
        }
    }
}
