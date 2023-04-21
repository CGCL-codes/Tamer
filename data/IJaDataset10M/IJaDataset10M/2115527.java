package org.apache.jsp.console;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class get_005fbusinessDetailExt_jsp extends org.apache.jasper.runtime.HttpJspBase implements org.apache.jasper.runtime.JspSourceDependent {

    private static java.util.List _jspx_dependants;

    public Object getDependants() {
        return _jspx_dependants;
    }

    public void _jspService(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
        JspFactory _jspxFactory = null;
        PageContext pageContext = null;
        HttpSession session = null;
        ServletContext application = null;
        ServletConfig config = null;
        JspWriter out = null;
        Object page = this;
        JspWriter _jspx_out = null;
        PageContext _jspx_page_context = null;
        try {
            _jspxFactory = JspFactory.getDefaultFactory();
            response.setContentType("text/html");
            pageContext = _jspxFactory.getPageContext(this, request, response, null, true, 8192, true);
            _jspx_page_context = pageContext;
            application = pageContext.getServletContext();
            config = pageContext.getServletConfig();
            session = pageContext.getSession();
            out = pageContext.getOut();
            _jspx_out = out;
            org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "header.html", out, false);
            out.write('\n');
            out.write('\n');
            String requestName = "get_businessDetailExt";
            String requestType = "inquiry";
            String requestKey = requestName + ":request";
            String responseKey = requestName + ":response";
            String requestTimeKey = requestName + ":time";
            out.write("\n");
            out.write("\n");
            out.write("<h3>");
            out.print(requestName);
            out.write("</h3>\n");
            out.write("<div class=\"link\">\n");
            out.write("The <a href=\"uddiv2api.html#_Toc25137719\" target=\"doc\">get_businessDetailExt</a> API call \n");
            out.write("returns extended <a href=\"uddiv2data.html#_Toc25130756\" target=\"doc\">businessEntity</a> information \n");
            out.write("for one or more specified <a href=\"uddiv2data.html#_Toc25130756\" target=\"doc\">businessEntity</a> \n");
            out.write("registrations. This message returns exactly the same information as \n");
            out.write("the <a href=\"uddiv2api.html#_Toc25137718\" target=\"doc\">get_businessDetail</a> message, \n");
            out.write("but may contain additional attributes. If an error occurs while processing this \n");
            out.write("API call, a <a href=\"uddiv2api.html#_Toc25137750\" target=\"doc\">dispositionReport</a> element \n");
            out.write("will be returned to the caller within a <a href=\"uddiv2api.html#_Toc25137756\" target=\"doc\">SOAP \n");
            out.write("Fault</a> containing information about the <a href=\"uddiv2api.html#_Toc25137748\" target=\"doc\">error</a> that \n");
            out.write("was encountered.\n");
            out.write("</div>\n");
            out.write("\n");
            out.write("<form method=\"post\" action=\"controller.jsp\">\n");
            out.write("<textarea class=msgs id=soap_request name=soap_request rows=15 cols=75 wrap=off>");
            String requestMessage = (String) session.getAttribute(requestKey);
            if (requestMessage != null) {
                out.print(requestMessage);
            } else {
                out.write("\n");
                out.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
                out.write("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n");
                out.write("  <soapenv:Body>\n");
                out.write("    <get_businessDetailExt generic=\"2.0\" xmlns=\"urn:uddi-org:api_v2\">\n");
                out.write("      <businessKey>***</businessKey>\n");
                out.write("    </get_businessDetailExt>\n");
                out.write("  </soapenv:Body>\n");
                out.write("</soapenv:Envelope>\n");
            }
            out.write("\n");
            out.write("</textarea>\n");
            out.write("\n");
            String requestTime = (String) session.getAttribute(requestTimeKey);
            if (requestTime == null) {
                requestTime = "0";
            }
            out.write("\n");
            out.write("<table cellpadding=\"4\" width=\"100%\">\n");
            out.write("<tr>\n");
            out.write("<td>\n");
            out.write("<input type=\"hidden\" name=\"request_name\" value=");
            out.print(requestName);
            out.write(">\n");
            out.write("<input type=\"hidden\" name=\"request_type\" value=");
            out.print(requestType);
            out.write(">\n");
            out.write("<input type=\"submit\" name=\"validate_button\" value=\"Validate\">\n");
            out.write("<input type=\"submit\" name=\"submit_button\" value=\"Submit\">\n");
            out.write("<input type=\"submit\" name=\"reset_button\" value=\"Reset\">\n");
            out.write("</td>\n");
            out.write("<td align=\"right\">\n");
            out.write("Time: <strong>");
            out.print(requestTime);
            out.write("</strong> milliseconds\n");
            out.write("</td>\n");
            out.write("</tr>\n");
            out.write("</table>\n");
            out.write("\n");
            out.write("<textarea class=msgs id=soap_response name=soap_response rows=25 cols=75 wrap=off>");
            String responseMessage = (String) session.getAttribute(responseKey);
            if (responseMessage != null) {
                out.print(responseMessage);
            }
            out.write("\n");
            out.write("</textarea>\n");
            out.write("</form>\n");
            out.write("\n");
            org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "footer.html", out, false);
        } catch (Throwable t) {
            if (!(t instanceof SkipPageException)) {
                out = _jspx_out;
                if (out != null && out.getBufferSize() != 0) out.clearBuffer();
                if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
            }
        } finally {
            if (_jspxFactory != null) _jspxFactory.releasePageContext(_jspx_page_context);
        }
    }
}
