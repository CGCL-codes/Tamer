package org.apache.jsp.xava;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.openxava.controller.meta.MetaAction;

public final class bottomButtons_jsp extends org.apache.jasper.runtime.HttpJspBase implements org.apache.jasper.runtime.JspSourceDependent {

    private static java.util.List _jspx_dependants;

    static {
        _jspx_dependants = new java.util.ArrayList(2);
        _jspx_dependants.add("/xava/imports.jsp");
        _jspx_dependants.add("/WEB-INF/openxava.tld");
    }

    private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fxava_005fbutton_005faction_005fnobody;

    public Object getDependants() {
        return _jspx_dependants;
    }

    public void _jspInit() {
        _005fjspx_005ftagPool_005fxava_005fbutton_005faction_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    }

    public void _jspDestroy() {
        _005fjspx_005ftagPool_005fxava_005fbutton_005faction_005fnobody.release();
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
            out.write('\n');
            out.write('\n');
            out.write('\n');
            out.write("\n");
            out.write("\n");
            out.write("\n");
            out.write("\n");
            org.openxava.controller.ModuleContext context = null;
            synchronized (session) {
                context = (org.openxava.controller.ModuleContext) _jspx_page_context.getAttribute("context", PageContext.SESSION_SCOPE);
                if (context == null) {
                    context = new org.openxava.controller.ModuleContext();
                    _jspx_page_context.setAttribute("context", context, PageContext.SESSION_SCOPE);
                }
            }
            out.write('\n');
            out.write('\n');
            org.openxava.controller.ModuleManager manager = (org.openxava.controller.ModuleManager) context.get(request, "manager", "org.openxava.controller.ModuleManager");
            manager.setSession(session);
            out.write("\n");
            out.write("\n");
            out.write("<button name=\"xava.DEFAULT_ACTION\" \n");
            out.write("\tonclick=\"executeXavaAction('', false, ");
            out.print(manager.getForm());
            out.write(',');
            out.write(' ');
            out.write('\'');
            out.print(manager.getDefaultActionQualifiedName());
            out.write("')\"\n");
            out.write("\tstyle=\"padding: 0; border: none; background-color:transparent; size: 0\"></button>\n");
            out.write("\n");
            java.util.Iterator it = manager.getMetaActions().iterator();
            while (it.hasNext()) {
                MetaAction action = (MetaAction) it.next();
                if (action.isHidden()) continue;
                if (!action.hasImage()) {
                    out.write('\n');
                    out.write('	');
                    org.openxava.web.taglib.ButtonTag _jspx_th_xava_005fbutton_005f0 = (org.openxava.web.taglib.ButtonTag) _005fjspx_005ftagPool_005fxava_005fbutton_005faction_005fnobody.get(org.openxava.web.taglib.ButtonTag.class);
                    _jspx_th_xava_005fbutton_005f0.setPageContext(_jspx_page_context);
                    _jspx_th_xava_005fbutton_005f0.setParent(null);
                    _jspx_th_xava_005fbutton_005f0.setAction(action.getQualifiedName());
                    int _jspx_eval_xava_005fbutton_005f0 = _jspx_th_xava_005fbutton_005f0.doStartTag();
                    if (_jspx_th_xava_005fbutton_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fxava_005fbutton_005faction_005fnobody.reuse(_jspx_th_xava_005fbutton_005f0);
                        return;
                    }
                    _005fjspx_005ftagPool_005fxava_005fbutton_005faction_005fnobody.reuse(_jspx_th_xava_005fbutton_005f0);
                    out.write('\n');
                    out.write('	');
                }
            }
            out.write('\n');
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
