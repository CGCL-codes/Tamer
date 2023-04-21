package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import com.meisenberger.stealthnet.app.*;

public final class finished_jsp extends org.apache.jasper.runtime.HttpJspBase implements org.apache.jasper.runtime.JspSourceDependent {

    private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

    private static java.util.Vector _jspx_dependants;

    private org.apache.jasper.runtime.ResourceInjector _jspx_resourceInjector;

    public Object getDependants() {
        return _jspx_dependants;
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
            _jspx_resourceInjector = (org.apache.jasper.runtime.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");
            out.write('\n');
            Locale loc = Util.getLocale(request);
            out.write('\n');
            out.print(Util.getHTMLTemplate("head.html", request, loc));
            out.write('\n');
            String refresh = request.getParameter("refresh");
            String doDelete = request.getParameter("doDelete");
            String statusMsg = null;
            if (refresh != null) {
            } else if (doDelete != null) {
                String[] todelete = request.getParameterValues("todelete");
                if (todelete != null && todelete.length > 0) {
                    statusMsg = "";
                    for (String dl : todelete) {
                        statusMsg += Util.msg("PAGE_FINISHED_DNLS_MSG_DELETED", dl, StealthnetServer.deleteDownloadedFile(dl), loc);
                    }
                }
            }
            if (statusMsg != null) {
                out.write('\n');
                out.write('	');
                out.print(Util.msg("COMMAND_RESPONSE", statusMsg, loc));
                out.write('\n');
                out.write('	');
            }
            out.write('\n');
            out.print(Util.title("PAGE_FINISHED_DNLS_TITLE", "ok.png", request, loc));
            out.write('\n');
            List<SearchResult> results = StealthnetServer.getFinishedDownloads(Util.sessionParam("orderfinished", request, "none"));
            if (results.size() > 0) {
                out.write("\n\t</form><form method=\"POST\"  name=\"list\" onSubmit=\"return askQuestion('");
                out.print(Util.msg("PAGE_FINISHED_DNLS_QUESTION_DELETE", loc));
                out.write("');\">\n\t<input type=\"submit\" name=\"doDelete\" value=\"");
                out.print(Util.msg("PAGE_FINISHED_DNLS_SUBMIT_DELETE_FILES", loc));
                out.write("\"/>\n\t<br/>\n\t<a onClick=\"javascript:selectAll('todelete')\">");
                out.print(Util.msg("WORD_SELECT_ALL", loc));
                out.write("</a>, \n\t<a onClick=\"javascript:unSelectAll('todelete')\">");
                out.print(Util.msg("WORD_UNSELECT_ALL", loc));
                out.write("</a>\n\t<br/>\n\t<div class=\"searchBlock\">\n\t<table class=\"searchTable\" border=\"1\">\n\t<tr>\n\t<td class=\"searchHeader\" onClick=\"javascript:setSearchOrder('orderfinished','none')\">");
                out.print(Util.msg("PAGE_FINISHED_DNLS_TBL_NONE", loc));
                out.write(":</td>\n\t<td class=\"searchHeader\" onClick=\"javascript:setSearchOrder('orderfinished','size')\">");
                out.print(Util.msg("PAGE_FINISHED_DNLS_TBL_SIZE", loc));
                out.write(":</td>\n\t<td class=\"searchHeader\" onClick=\"javascript:setSearchOrder('orderfinished','name')\">");
                out.print(Util.msg("PAGE_FINISHED_DNLS_TBL_NAME", loc));
                out.write(":</td></tr>\n\t");
                int i = 0;
                for (SearchResult r : results) {
                    i++;
                    out.write("\n\t\t<tr>\n\t\t<td><input type=\"checkbox\" name=\"todelete\" value=\"");
                    out.print(r.name.replace("\"", "&quot;"));
                    out.write("\"  onclick=\"javascript:stopRefreshTask()\"/></td>\n\t\t<td>");
                    out.print(Util.formatBytes(r.size));
                    out.write("</td>\n\t\t<td><a href=\"/bin/?file=");
                    out.print(HelperStd.urlencode(r.name));
                    out.write('"');
                    out.write('>');
                    out.print(r.name);
                    out.write("</a></td>\n\t\t\n\t\t</tr>\n\t\t");
                }
                out.write("\n\t</table>\n\t<a onClick=\"javascript:selectAll('todelete')\">");
                out.print(Util.msg("WORD_SELECT_ALL", loc));
                out.write("</a>, \n\t<a onClick=\"javascript:unSelectAll('todelete')\">");
                out.print(Util.msg("WORD_UNSELECT_ALL", loc));
                out.write("</a>\n\t<br/>\n\t<input type=\"submit\" name=\"doDelete\" value=\"");
                out.print(Util.msg("PAGE_FINISHED_DNLS_SUBMIT_DELETE_FILES", loc));
                out.write("\"/>\n\t<br/>\n\t</div>\n\t");
            } else {
                out.write('\n');
                out.write('	');
                out.print(Util.msg("PAGE_FINISHED_DNLS_NO_FILES_FOUND", loc));
                out.write('\n');
                out.write('	');
            }
            out.write('\n');
            out.write('\n');
            out.print(Util.getHTMLTemplate("footer.html", request, loc));
        } catch (Throwable t) {
            if (!(t instanceof SkipPageException)) {
                out = _jspx_out;
                if (out != null && out.getBufferSize() != 0) out.clearBuffer();
                if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
            }
        } finally {
            _jspxFactory.releasePageContext(_jspx_page_context);
        }
    }
}
