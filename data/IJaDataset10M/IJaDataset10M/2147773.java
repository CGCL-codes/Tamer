package org.apache.jsp.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.swing.services.ISwingServices;
import com.swing.services.SwingServices;
import java.util.HashMap;

public final class options_jsp extends org.apache.jasper.runtime.HttpJspBase implements org.apache.jasper.runtime.JspSourceDependent {

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
            response.setContentType("text/html;charset=UTF-8");
            pageContext = _jspxFactory.getPageContext(this, request, response, null, true, 8192, true);
            _jspx_page_context = pageContext;
            application = pageContext.getServletContext();
            config = pageContext.getServletConfig();
            session = pageContext.getSession();
            out = pageContext.getOut();
            _jspx_out = out;
            _jspx_resourceInjector = (org.apache.jasper.runtime.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            Integer state = 1;
            if (request.getParameter("state") != null) {
                state = Integer.valueOf(request.getParameter("state"));
            }
            ISwingServices swingServices = SwingServices.createDelegate(request, "");
            ((com.swing.services.SwingServicesDelegate) swingServices).loadLabels("");
            if (swingServices.getJSessionId() != null) {
                boolean isDashboard = swingServices.getNamespace().indexOf("SwingDashboard_WAR_SwingDashboard") != -1;
                String portlet = "";
                if (isDashboard) {
                    portlet = "dashboard";
                } else {
                    portlet = "report";
                }
                String report_pdf = "";
                String report_print = "";
                if (portlet == "report") {
                    HashMap m = swingServices.getServletParameters();
                    m.clear();
                    m.put("query", "Report");
                    m.put("method", "getReport");
                    m.put("render", "html");
                    m.put("namespace", swingServices.getNamespace());
                    m.put("SWparam1", swingServices.getNamespace());
                    m.put("SWparam2", swingServices.getLanguage());
                    if (swingServices.getLanguageId() != -1) m.put("SWparam3", swingServices.getLanguageId());
                    String result = swingServices.executeSwingRequest();
                    int id01 = result.indexOf("\"") + 1;
                    int id02 = result.indexOf("\"", id01);
                    report_print = result.substring(id01, id02);
                    report_pdf = result.substring(id01, id02) + "&__format=pdf&__pageoverflow=0&__overwrite=false";
                }
                out.write("\r\n");
                out.write("\r\n");
                out.write("\t<div>\r\n");
                out.write("            <div id=\"srv_export\"/>\r\n");
                out.write("            <div class=\"option option-style\" onclick=\"do_edit('");
                out.print(swingServices.getNamespace());
                out.write("', 2,'");
                out.print(request.getParameter("plid"));
                out.write("');\"  onmouseover=\"image_actif_passif('update',true);\" onmouseout=\"image_actif_passif('update',false);\" >\r\n");
                out.write("\t\t\t<div class=\"option_image\"><img id=\"img_update\" src=\"/swing-theme-bi/images/custom/options/update_passif.gif\"/></div>\r\n");
                out.write("\t\t\t<div class=\"option_label\">");
                out.print(swingServices.getLabel("menuupdate", portlet));
                out.write("</div>\r\n");
                out.write("\t\t\t<div class=\"clear\"></div>\r\n");
                out.write("\t\t</div>\r\n");
                out.write("            \r\n");
                out.write("\r\n");
                out.write("\t\t");
                if (isDashboard) {
                    out.write("\r\n");
                    out.write("\t\t\t<div class=\"option option-style\" onclick=\"do_csv('");
                    out.print(swingServices.getNamespace());
                    out.write("');\" onmouseover=\"image_actif_passif('csv',true);\" onmouseout=\"image_actif_passif('csv',false);\">\r\n");
                    out.write("\t\t\t\t<div class=\"option_image\"><img id=\"img_csv\" src=\"/swing-theme-bi/images/custom/options/csv_passif.gif\" /></div>\r\n");
                    out.write("\t\t\t\t<div class=\"option_label\">");
                    out.print(swingServices.getLabel("menucsv", portlet));
                    out.write("</div>\r\n");
                    out.write("\t\t\t\t<div class=\"clear\"></div>\r\n");
                    out.write("\t\t\t</div>\r\n");
                    out.write("\t\t");
                }
                out.write("\r\n");
                out.write("\r\n");
                out.write("\t\t");
                if (isDashboard) {
                    out.write("\r\n");
                    out.write("\t\t\t<div class=\"option option-style\" onclick=\"do_print_dashboard('");
                    out.print(swingServices.getNamespace());
                    out.write("');\" onmouseover=\"image_actif_passif('print',true);\" onmouseout=\"image_actif_passif('print',false);\">\r\n");
                    out.write("\t\t\t\t<div class=\"option_image\"><img id=\"img_print\" src=\"/swing-theme-bi/images/custom/options/print_passif.gif\" /></div>\r\n");
                    out.write("\t\t\t\t<div class=\"option_label\">");
                    out.print(swingServices.getLabel("menuprint", portlet));
                    out.write("</div>\r\n");
                    out.write("\t\t\t\t<div class=\"clear\"></div>\r\n");
                    out.write("\t\t\t</div>\r\n");
                    out.write("\t\t");
                } else {
                    out.write("\r\n");
                    out.write("\t\t\t<div class=\"option option-style\" onclick=\"do_print_report('");
                    out.print(report_print);
                    out.write("');\" onmouseover=\"image_actif_passif('print',true);\" onmouseout=\"image_actif_passif('print',false);\">\r\n");
                    out.write("\t\t\t\t<div class=\"option_image\"><img id=\"img_print\" src=\"/swing-theme-bi/images/custom/options/print_passif.gif\" /></div>\r\n");
                    out.write("\t\t\t\t<div class=\"option_label\">");
                    out.print(swingServices.getLabel("menuprint", portlet));
                    out.write("</div>\r\n");
                    out.write("\t\t\t\t<div class=\"clear\"></div>\r\n");
                    out.write("\t\t\t</div>\r\n");
                    out.write("\t\t");
                }
                out.write("\t\t\r\n");
                out.write("\r\n");
                out.write("\t\t<div class=\"option option-style\" onclick=\"do_comment('");
                out.print(swingServices.getNamespace());
                out.write("', '");
                out.print(swingServices.getUserId());
                out.write("', '");
                out.print(swingServices.getLanguage());
                out.write("');\" onmouseover=\"image_actif_passif('comment',true);\" onmouseout=\"image_actif_passif('comment',false);\">\r\n");
                out.write("\t\t\t<div class=\"option_image\"><img id=\"img_comment\" src=\"/swing-theme-bi/images/custom/options/comment_passif.gif\" /></div>\r\n");
                out.write("\t\t\t<div class=\"option_label\">");
                out.print(swingServices.getLabel("menucomment", portlet));
                out.write("</div>\r\n");
                out.write("\t\t\t<div class=\"clear\"></div>\r\n");
                out.write("\t\t</div>\r\n");
                out.write("\r\n");
                out.write("\t\t");
                if (isDashboard) {
                    out.write("\r\n");
                    out.write("\t\t\t<div class=\"option option-style\" onclick=\"do_export_dashboard('");
                    out.print(swingServices.getNamespace());
                    out.write("','PDF');\" onmouseover=\"image_actif_passif('save',true);\" onmouseout=\"image_actif_passif('save',false);\">\r\n");
                    out.write("\t\t\t\t<div class=\"option_image\"><img id=\"img_save\" src=\"/swing-theme-bi/images/custom/options/save_passif.gif\" /></div>\r\n");
                    out.write("\t\t\t\t<div class=\"option_label\">");
                    out.print(swingServices.getLabel("menusaveaspdf", "dashboard"));
                    out.write("</div>\r\n");
                    out.write("\t\t\t\t<div class=\"clear\"></div>\r\n");
                    out.write("\t\t\t</div>\r\n");
                    out.write("                        <div class=\"option option-style\" onclick=\"do_export_dashboard('");
                    out.print(swingServices.getNamespace());
                    out.write("','JPG');\" onmouseover=\"image_actif_passif('save',true);\" onmouseout=\"image_actif_passif('save',false);\">\r\n");
                    out.write("\t\t\t\t<div class=\"option_image\"><img id=\"img_save\" src=\"/swing-theme-bi/images/custom/options/save_passif.gif\" /></div>\r\n");
                    out.write("\t\t\t\t<div class=\"option_label\">");
                    out.print(swingServices.getLabel("menusaveasimg", "dashboard"));
                    out.write("</div>\r\n");
                    out.write("\t\t\t\t<div class=\"clear\"></div>\r\n");
                    out.write("\t\t\t</div>\r\n");
                    out.write("\t\t");
                } else {
                    out.write("\r\n");
                    out.write("\t\t\t<div class=\"option option-style\" onclick=\"do_export_report('");
                    out.print(report_pdf);
                    out.write("');\" onmouseover=\"image_actif_passif('save',true);\" onmouseout=\"image_actif_passif('save',false);\">\r\n");
                    out.write("\t\t\t\t<div class=\"option_image\"><img id=\"img_save\" src=\"/swing-theme-bi/images/custom/options/save_passif.gif\" /></div>\r\n");
                    out.write("\t\t\t\t<div class=\"option_label\">");
                    out.print(swingServices.getLabel("menusaveaspdf", "report"));
                    out.write("</div>\r\n");
                    out.write("\t\t\t\t<div class=\"clear\"></div>\r\n");
                    out.write("\t\t\t</div>\r\n");
                    out.write("\t\t");
                }
                out.write("\r\n");
                out.write("\r\n");
                out.write("\t</div>\r\n");
                out.write("\r\n");
            } else {
                out.println("Session expired or invalidated!");
            }
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
