package org.apache.jsp.WEB_002dINF.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.owasp.esapi.*;
import org.owasp.esapi.errors.*;
import org.owasp.esapi.AccessReferenceMap;
import org.owasp.esapi.codecs.*;

public final class XSSSecure_jsp extends org.apache.jasper.runtime.HttpJspBase implements org.apache.jasper.runtime.JspSourceDependent {

    private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

    private static java.util.List _jspx_dependants;

    static {
        _jspx_dependants = new java.util.ArrayList(2);
        _jspx_dependants.add("/WEB-INF/jsp/header.jsp");
        _jspx_dependants.add("/WEB-INF/jsp/footer.jsp");
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
            response.setContentType("text/html; charset=UTF-8");
            pageContext = _jspxFactory.getPageContext(this, request, response, null, true, 8192, true);
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
            String function = (String) request.getAttribute("function");
            String title = "ESAPI SwingSet - " + function;
            String querystring = request.getQueryString();
            String pageHeader = "ESAPI Swingset - " + function;
            int i1 = querystring.indexOf("&secure");
            boolean secure = (i1 != -1);
            if (secure) {
                querystring = querystring.substring(0, i1);
                title += ": Secured by ESAPI";
                pageHeader += ": Secured by ESAPI";
            }
            int i2 = querystring.indexOf("&insecure");
            boolean insecure = (i2 != -1);
            if (insecure) {
                querystring = querystring.substring(0, i2);
                title += ": Insecure";
                pageHeader += ": Insecure";
            }
            out.write("\r\n");
            out.write("\r\n");
            out.write("<html>\r\n");
            out.write("<head>\r\n");
            out.write("<title>");
            out.print(title);
            out.write("</title>\r\n");
            out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"style/style.css\" />\r\n");
            out.write("\r\n");
            out.write("</head>\r\n");
            out.write("\r\n");
            if (!insecure && !secure) {
                out.write(" <body> ");
            }
            out.write('\r');
            out.write('\n');
            if (insecure) {
                out.write(" <body bgcolor=\"#EECCCC\"> ");
            }
            out.write('\r');
            out.write('\n');
            if (secure) {
                out.write(" <body bgcolor=\"#BBDDBB\"> ");
            }
            out.write("\r\n");
            out.write("<div id=\"container\">\r\n");
            out.write("\t<div id=\"holder\">\r\n");
            out.write("\t\t<div id=\"logo\"><img src=\"style/images/owasp-logo_130x55.png\" width=\"130\" height=\"55\" alt=\"owasp_logo\" title=\"owasp_logo\"></div>\r\n");
            out.write("<h2>");
            out.print(pageHeader);
            out.write("</h2>\r\n");
            out.write("\r\n");
            out.write("<div id=\"navigation\">\r\n");
            out.write("<a href=\"main\">Home</a> | \r\n");
            if (!secure && !insecure) {
                out.write('<');
                out.write('b');
                out.write('>');
            }
            out.write("<a href=\"main?");
            out.print(querystring);
            out.write("\">Tutorial</a>");
            if (!secure && !insecure) {
                out.write("</b>");
            }
            out.write(" | \r\n");
            if (insecure) {
                out.write('<');
                out.write('b');
                out.write('>');
            }
            out.write("<a href=\"main?");
            out.print(querystring);
            out.write("&insecure\">Insecure Demo</a>");
            if (insecure) {
                out.write("</b>");
            }
            out.write(" | \r\n");
            if (secure) {
                out.write('<');
                out.write('b');
                out.write('>');
            }
            out.write("<a href=\"main?");
            out.print(querystring);
            out.write("&secure\">Secure Demo</a>");
            if (secure) {
                out.write("</b>");
            }
            out.write("\r\n");
            out.write("</div>\r\n");
            out.write("<div id=\"header\"></div>\r\n");
            out.write("<p>\r\n");
            out.write("<hr>\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            String type = request.getParameter("type");
            if (type == null) type = "SafeString";
            String input = request.getParameter("input");
            if (input == null) input = "type input here";
            System.out.println(" >>>>>");
            byte[] inputBytes = input.getBytes("UTF-8");
            for (int i = 0; i < inputBytes.length; i++) System.out.print(" " + inputBytes[i]);
            System.out.println();
            String canonical = "";
            try {
                canonical = ESAPI.encoder().canonicalize(input);
                ESAPI.validator().getValidInput("Swingset Validation Secure Exercise", input, type, 200, false);
            } catch (ValidationException e) {
                input = "Validation attack detected";
                request.setAttribute("userMessage", e.getUserMessage());
                request.setAttribute("logMessage", e.getLogMessage());
            } catch (IntrusionException ie) {
                input = "double encoding attack detected";
                request.setAttribute("userMessage", ie.getUserMessage());
                request.setAttribute("logMessage", ie.getLogMessage());
            }
            out.write("\r\n");
            out.write("\r\n");
            out.write("<h2 align=\"center\">Excercise</h2>\r\n");
            out.write("<h4>Enter a Type/Regex and Invalid Data</h4>\r\n");
            out.write("\r\n");
            out.write("<form action=\"main?function=ValidateUserInput&secure\" method=\"POST\">\r\n");
            out.write("\tType/Regex: <input name=\"type\" value=\"");
            out.print(ESAPI.encoder().encodeForHTMLAttribute(type));
            out.write("\"><br>\r\n");
            out.write("\t<textarea style=\"width:400px; height:150px\" name=\"input\">");
            out.print(ESAPI.encoder().encodeForHTML(input));
            out.write("</textarea><br>\r\n");
            out.write("\t<input type=\"submit\" value=\"submit\">\r\n");
            out.write("</form>\r\n");
            out.write("\r\n");
            out.write("<p>Canonical output: ");
            out.print(ESAPI.encoder().encodeForHTML(canonical));
            out.write("</p>\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("<hr>\r\n");
            String message = (String) request.getAttribute("message");
            if (request.getAttribute("userMessage") != null || request.getAttribute("logMessage") != null) {
                out.write("\r\n");
                out.write("\t<p>User Message: <font color=\"red\">");
                out.print(org.owasp.esapi.ESAPI.encoder().encodeForHTML(request.getAttribute("userMessage").toString()));
                out.write("</font></p>\r\n");
                out.write("\t<p>Log Message: <font color=\"red\">");
                out.print(org.owasp.esapi.ESAPI.encoder().encodeForHTML(request.getAttribute("logMessage").toString()));
                out.write("</font></p><hr>\r\n");
            }
            out.write("\r\n");
            out.write("<p><center><a href=\"http://www.owasp.org/index.php/ESAPI\">OWASP Enterprise Security API Project</a> (c) 2008</center></p>\r\n");
            out.write("<!--  <span id=\"copyright\">Design by <a href=\"http://www.sitecreative.net\" target=\"_blank\" title=\"Opens link to SiteCreative.net in a New Window\">SiteCreative</a></span> -->\r\n");
            out.write("\t</div> <!-- end holder div -->\r\n");
            out.write("</div> <!-- end container div -->\r\n");
            out.write("</body>\r\n");
            out.write("</html>");
            out.write('\r');
            out.write('\n');
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
