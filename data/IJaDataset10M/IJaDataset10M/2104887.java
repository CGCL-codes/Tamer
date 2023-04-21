package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;

public final class warnLogged_jsp extends org.apache.jasper.runtime.HttpJspBase implements org.apache.jasper.runtime.JspSourceDependent {

    private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

    private static java.util.List _jspx_dependants;

    private javax.el.ExpressionFactory _el_expressionfactory;

    private org.apache.AnnotationProcessor _jsp_annotationprocessor;

    public Object getDependants() {
        return _jspx_dependants;
    }

    public void _jspInit() {
        _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
    }

    public void _jspDestroy() {
    }

    public void _jspService(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
        PageContext pageContext = null;
        HttpSession session = null;
        ServletContext application = null;
        ServletConfig config = null;
        JspWriter out = null;
        Object page = this;
        JspWriter _jspx_out = null;
        PageContext _jspx_page_context = null;
        try {
            response.setContentType("text/html");
            pageContext = _jspxFactory.getPageContext(this, request, response, null, true, 8192, true);
            _jspx_page_context = pageContext;
            application = pageContext.getServletContext();
            config = pageContext.getServletConfig();
            session = pageContext.getSession();
            out = pageContext.getOut();
            _jspx_out = out;
            out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("<html>\r\n");
            out.write("<head>\r\n");
            out.write("<title>Order Managment System, Release 1.0, v. 1.0</title>\r\n");
            out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\r\n");
            out.write("\r\n");
            out.write("<style type=\"text/css\">\r\n");
            out.write("html, body, form, fieldset, h1, h2, h3, h4, h5, h6, p, pre, select, \r\n");
            out.write("        input, blockquote, ul, ol, dl, address, a {      \r\n");
            out.write("    font: normal 11px auto \"Trebuchet MS\", Verdana, Arial, Helvetica, sans-serif;\r\n");
            out.write("    text-align:center;\r\n");
            out.write("    color:#0000FF;    \r\n");
            out.write("    align:center;\r\n");
            out.write("    background:white;\r\n");
            out.write("    }\r\n");
            out.write("body {\r\n");
            out.write("    background:#E6E6FA;\r\n");
            out.write("    }\r\n");
            out.write("</style>\r\n");
            out.write("</head>\r\n");
            out.write("\r\n");
            out.write("<body>\r\n");
            out.write("\r\n");
            out.write("<div style=\"align:center;margin:0;padding:0;position:absolute;left:339px;\r\n");
            out.write("        top:205px;width:314px;height:130px;text-align:center;z-index:0;\">\r\n");
            out.write("        \r\n");
            out.write("<fieldset>\r\n");
            out.write("    <legend> Warning </legend>\r\n");
            out.write("    <br>This user is already logged into the system under other browser.\r\n");
            out.write("    <br>Please use another session or log out and try to log in again.\r\n");
            out.write("    <br>");
            out.print(session.getAttribute("error"));
            out.write("\r\n");
            out.write("    <br>Please try again later.\r\n");
            out.write("    <br>\r\n");
            out.write("</fieldset>\r\n");
            out.write("</div>\r\n");
            out.write("\r\n");
            out.write("</body>\r\n");
            out.write("\r\n");
            out.write("</html>");
        } catch (Throwable t) {
            if (!(t instanceof SkipPageException)) {
                out = _jspx_out;
                if (out != null && out.getBufferSize() != 0) try {
                    out.clearBuffer();
                } catch (java.io.IOException e) {
                }
                if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
            }
        } finally {
            _jspxFactory.releasePageContext(_jspx_page_context);
        }
    }
}
