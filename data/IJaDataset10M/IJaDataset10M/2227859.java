package org.apache.jsp.system;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.sql.*;

public final class top_jsp extends org.apache.jasper.runtime.HttpJspBase implements org.apache.jasper.runtime.JspSourceDependent {

    private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

    private static java.util.List _jspx_dependants;

    static {
        _jspx_dependants = new java.util.ArrayList(5);
        _jspx_dependants.add("/system/include/taglib.jsp");
        _jspx_dependants.add("/WEB-INF/struts-bean.tld");
        _jspx_dependants.add("/WEB-INF/struts-logic.tld");
        _jspx_dependants.add("/WEB-INF/struts-html.tld");
        _jspx_dependants.add("/WEB-INF/tld/mytag.tld");
    }

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
            response.setContentType("text/html; charset=utf-8");
            pageContext = _jspxFactory.getPageContext(this, request, response, "", true, 8192, true);
            _jspx_page_context = pageContext;
            application = pageContext.getServletContext();
            config = pageContext.getServletConfig();
            session = pageContext.getSession();
            out = pageContext.getOut();
            _jspx_out = out;
            out.write('\r');
            out.write('\n');
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
            pageContext.setAttribute("basePath", basePath);
            out.write("\r\n");
            out.write("<html>\r\n");
            out.write("<head>\r\n");
            out.write("<base href=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageScope.basePath }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\">\r\n");
            out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n");
            out.write("<meta http-equiv=\"pragma\" content=\"no-cache\">\r\n");
            out.write("<meta http-equiv=\"cache-control\" content=\"no-cache\">\r\n");
            out.write("<meta http-equiv=\"expires\" content=\"0\">\r\n");
            out.write("<title>中山市中凯信息科技有限公司</title>\r\n");
            out.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"system/css/public.css\" />\r\n");
            out.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"system/css/top.css\" />\r\n");
            out.write("<script type=\"text/javascript\" language=\"javascript\">\r\n");
            out.write("\tfunction mouseOver(element){\r\n");
            out.write("\t\telement.className=\"menu_title_over\";\r\n");
            out.write("\t}\r\n");
            out.write("\tfunction mouseOut(element){\r\n");
            out.write("\t\telement.className=\"menu_title\";\r\n");
            out.write("\t}\r\n");
            out.write("</script>\r\n");
            out.write("</head>\r\n");
            out.write("<body>\r\n");
            out.write("<div class=\"body\">\r\n");
            out.write("\t<div class=\"top\">\r\n");
            out.write("\t\t<div class=\"row01\">\r\n");
            out.write("\t\t\t<div class=\"logo\">中山市中凯信息科技有限公司</div>\r\n");
            out.write("\t\t\t<div class=\"tips\">\r\n");
            out.write("\t\t\t\t<div class=\"user_info\">");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${user.userAccount}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write(",欢迎登陆!</div>\r\n");
            out.write("\t\t\t\t<div class=\"msg_tips\">\r\n");
            out.write("\t\t\t\t\t<iframe src=\"system/msgtips.jsp\" width=\"150\" frameborder=\"0\" allowtransparency=\"true\"></iframe>\r\n");
            out.write("\t\t\t\t</div>\r\n");
            out.write("\t\t\t</div>\r\n");
            out.write("\t\t</div>\r\n");
            out.write("\t\t<div class=\"row02\">\r\n");
            out.write("\t\t\t<div class=\"user\">\r\n");
            out.write("\t\t\t\t<span class=\"username\">");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${user.userAccount}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("</span>\r\n");
            out.write("\t\t\t\t(<a href=\"#\" target=\"main\">帐号设置</a>)\r\n");
            out.write("\t\t\t\t | <a href=\"system/desktop.jsp\" target=\"main\">系统公告</a>\r\n");
            out.write("\t\t\t\t | <a href=\"logout.do\" target=\"_parent\" onClick=\"return window.confirm('您确认要安全退出吗');\">安全退出</a>\r\n");
            out.write("\t\t\t</div>\r\n");
            out.write("\t\t</div>\r\n");
            out.write("\t\t<div class=\"row03\">\r\n");
            out.write("\t\t\t<div class=\"menu\">\r\n");
            out.write("\t\t\t\t<ul>\r\n");
            out.write("\t\t\t\t\t<li class=\"menu_title\" onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\" style=\"margin-left:0px;\"><a href=\"system/desktop.jsp\" target=\"main\">系统公告</a></li>\r\n");
            out.write("\t\t\t\t\t<li class=\"menu_title\" onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\"><a href=\"logout.do\" target=\"_parent\" onClick=\"return window.confirm('您确认要安全退出吗');\">安全退出</a></li>\r\n");
            out.write("\t\t\t\t</ul>\r\n");
            out.write("\t\t\t</div>\r\n");
            out.write("\t\t</div>\r\n");
            out.write("\t</div>\r\n");
            out.write("</div>\r\n");
            out.write("</body>\r\n");
            out.write("</html>\r\n");
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
