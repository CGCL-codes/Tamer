package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class result_jsp extends org.apache.jasper.runtime.HttpJspBase implements org.apache.jasper.runtime.JspSourceDependent {

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
            out.write("<html><body>\r\n");
            out.write("<h1>Result Page: ");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${title}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("</h1>\r\n");
            out.write("\r\n");
            out.write("<table border=\"1\">\r\n");
            out.write("\t<tr>\r\n");
            out.write("\t\t<td>Name:</td>\r\n");
            out.write("\t\t<td>");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${output.name}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("</td>\r\n");
            out.write("\t</tr>\r\n");
            out.write("\t<tr>\r\n");
            out.write("\t\t<td>AccountNumber:</td>\r\n");
            out.write("\t\t<td>");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${output.accountNumber}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("</td>\r\n");
            out.write("\t</tr>\r\n");
            out.write("\t<tr>\r\n");
            out.write("\t\t<td>AccountType:</td>\r\n");
            out.write("\t\t<td>");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${output.accountType}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("</td>\r\n");
            out.write("\t</tr>\r\n");
            out.write("\t<tr>\r\n");
            out.write("\t\t<td>Balance:</td>\r\n");
            out.write("\t\t<td>");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${output.balance}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("</td>\r\n");
            out.write("\t</tr>\r\n");
            out.write("\t<tr>\r\n");
            out.write("\t\t<td>OverDraft:</td>\r\n");
            out.write("\t\t<td>");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${output.overdraft}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("</td>\r\n");
            out.write("\t</tr>\r\n");
            out.write("</table>\r\n");
            out.write("<br><a href = \"..\\index.html\"> Return Home</a>\r\n");
            out.write("</body>\r\n");
            out.write("</html>\r\n");
            out.write("\r\n");
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
