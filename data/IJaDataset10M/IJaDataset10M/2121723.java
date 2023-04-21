package org.apache.jsp.system.config;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class rolelist_jsp extends org.apache.jasper.runtime.HttpJspBase implements org.apache.jasper.runtime.JspSourceDependent {

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

    private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fmytag_005fList_0026_005ftable_005fname_005fnobody;

    private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005flogic_005fnotEmpty_0026_005fname;

    private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid;

    private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005flogic_005fempty_0026_005fname;

    private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fmytag_005fpage_005fnobody;

    private javax.el.ExpressionFactory _el_expressionfactory;

    private org.apache.AnnotationProcessor _jsp_annotationprocessor;

    public Object getDependants() {
        return _jspx_dependants;
    }

    public void _jspInit() {
        _005fjspx_005ftagPool_005fmytag_005fList_0026_005ftable_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
        _005fjspx_005ftagPool_005flogic_005fnotEmpty_0026_005fname = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
        _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
        _005fjspx_005ftagPool_005flogic_005fempty_0026_005fname = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
        _005fjspx_005ftagPool_005fmytag_005fpage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
        _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
    }

    public void _jspDestroy() {
        _005fjspx_005ftagPool_005fmytag_005fList_0026_005ftable_005fname_005fnobody.release();
        _005fjspx_005ftagPool_005flogic_005fnotEmpty_0026_005fname.release();
        _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid.release();
        _005fjspx_005ftagPool_005flogic_005fempty_0026_005fname.release();
        _005fjspx_005ftagPool_005fmytag_005fpage_005fnobody.release();
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
            out.write("\r\n");
            out.write("\r\n");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            out.write("\r\n");
            out.write("<html>\r\n");
            out.write("<head>\r\n");
            out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n");
            out.write("<title>中山市中凯信息科技有限公司</title>\r\n");
            out.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"/system/css/style.css\" />\r\n");
            out.write("<script type=\"text/javascript\" language=\"javascript\" src=\"/system/js/public.js\"></script>\r\n");
            out.write("<script type=\"text/javascript\" language=\"javascript\">\r\n");
            out.write("\tfunction actionSet(rid){\r\n");
            out.write("\t\tlocation.href=\"actionlist.jsp?rid=\"+rid;\r\n");
            out.write("\t}\r\n");
            out.write("\tfunction mod(id){\r\n");
            out.write("\t\tlocation.href=\"rolemodify.jsp?id=\"+id;\r\n");
            out.write("\t}\r\n");
            out.write("\tfunction del(id){\r\n");
            out.write("\t\tif(confirm(\"确定要删除该角色吗?\")){\r\n");
            out.write("\t\t\tlocation.href=\"/system/config/role.do?method=delete&id=\"+id;\r\n");
            out.write("\t\t}\r\n");
            out.write("\t}\r\n");
            out.write("</script>\r\n");
            out.write("</head>\r\n");
            out.write("<body>\r\n");
            out.write("<div class=\"main\">\r\n");
            out.write("\t<div class=\"position\">当前位置: <a href=\"/sysadm/desktop.jsp\">桌 面</a> → 角色管理</div>\r\n");
            out.write("\t<div class=\"mainbody\">\r\n");
            out.write("\t\t<div class=\"operate_info\">\r\n");
            out.write("\t\t\t操作说明：\r\n");
            out.write("\t\t\t<font color=\"#FF0000\"><label id=\"explaininfo\">&nbsp;</label></font>\r\n");
            out.write("\t\t</div>\r\n");
            out.write("\t\t<div class=\"table\">\r\n");
            out.write("\t\t\t<table width=\"100%\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\" class=\"table_list\">\r\n");
            out.write("\t\t\t\t<tr>\r\n");
            out.write("\t\t\t\t\t<th width=\"30\"><input id=\"selector\" type=\"checkbox\" onClick=\"selectCheckbox('roleId')\"></th>\r\n");
            out.write("\t\t\t\t\t<th width=\"50\">角色ID</th>\r\n");
            out.write("\t\t\t\t\t<th width=\"200\">角色名称</th>\r\n");
            out.write("\t\t\t\t\t<th>角色描述</th>\r\n");
            out.write("\t\t\t\t\t<th width=\"120\">操作</th>\r\n");
            out.write("\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t");
            if (_jspx_meth_mytag_005fList_005f0(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("\t\t\t\t");
            org.apache.struts.taglib.logic.NotEmptyTag _jspx_th_logic_005fnotEmpty_005f0 = (org.apache.struts.taglib.logic.NotEmptyTag) _005fjspx_005ftagPool_005flogic_005fnotEmpty_0026_005fname.get(org.apache.struts.taglib.logic.NotEmptyTag.class);
            _jspx_th_logic_005fnotEmpty_005f0.setPageContext(_jspx_page_context);
            _jspx_th_logic_005fnotEmpty_005f0.setParent(null);
            _jspx_th_logic_005fnotEmpty_005f0.setName("roleList");
            int _jspx_eval_logic_005fnotEmpty_005f0 = _jspx_th_logic_005fnotEmpty_005f0.doStartTag();
            if (_jspx_eval_logic_005fnotEmpty_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                    out.write("\r\n");
                    out.write("\t\t\t\t\t");
                    org.apache.struts.taglib.logic.IterateTag _jspx_th_logic_005fiterate_005f0 = (org.apache.struts.taglib.logic.IterateTag) _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid.get(org.apache.struts.taglib.logic.IterateTag.class);
                    _jspx_th_logic_005fiterate_005f0.setPageContext(_jspx_page_context);
                    _jspx_th_logic_005fiterate_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_005fnotEmpty_005f0);
                    _jspx_th_logic_005fiterate_005f0.setId("role");
                    _jspx_th_logic_005fiterate_005f0.setName("roleList");
                    int _jspx_eval_logic_005fiterate_005f0 = _jspx_th_logic_005fiterate_005f0.doStartTag();
                    if (_jspx_eval_logic_005fiterate_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        java.lang.Object role = null;
                        if (_jspx_eval_logic_005fiterate_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                            out = _jspx_page_context.pushBody();
                            _jspx_th_logic_005fiterate_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                            _jspx_th_logic_005fiterate_005f0.doInitBody();
                        }
                        role = (java.lang.Object) _jspx_page_context.findAttribute("role");
                        do {
                            out.write("\r\n");
                            out.write("\t\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
                            out.write("\t\t\t\t\t\t\t<td width=\"30\"><input name=\"roleId\" type=\"checkbox\" value=\"1\"></td>\r\n");
                            out.write("\t\t\t\t\t\t\t<td>");
                            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${role.roleId }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                            out.write("</td>\r\n");
                            out.write("\t\t\t\t\t\t\t<td>");
                            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${role.roleName }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                            out.write("</td>\r\n");
                            out.write("\t\t\t\t\t\t\t<td>");
                            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${role.roleNote }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                            out.write("</td>\r\n");
                            out.write("\t\t\t\t\t\t\t<td>\r\n");
                            out.write("\t\t\t\t\t\t\t\t");
                            out.write("\r\n");
                            out.write("\t\t\t\t\t\t\t\t<input type=\"button\" value=\"权限设置\" class=\"system_button\" onclick=\"actionSet(");
                            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${role.roleId }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                            out.write(")\" />\r\n");
                            out.write("\t\t\t\t\t\t\t\t<input type=\"button\" value=\"修改\" class=\"system_button\" onclick=\"mod(");
                            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${role.roleId }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                            out.write(")\"  />\r\n");
                            out.write("\t\t\t\t\t\t\t\t<input type=\"button\" value=\"删除\" class=\"system_button\" onclick=\"del(");
                            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${role.roleId }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                            out.write(")\"  />\r\n");
                            out.write("\t\t\t\t\t\t\t</td>\r\n");
                            out.write("\t\t\t\t\t\t</tr>\r\n");
                            out.write("\t\t\t\t\t");
                            int evalDoAfterBody = _jspx_th_logic_005fiterate_005f0.doAfterBody();
                            role = (java.lang.Object) _jspx_page_context.findAttribute("role");
                            if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
                        } while (true);
                        if (_jspx_eval_logic_005fiterate_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                            out = _jspx_page_context.popBody();
                        }
                    }
                    if (_jspx_th_logic_005fiterate_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid.reuse(_jspx_th_logic_005fiterate_005f0);
                        return;
                    }
                    _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid.reuse(_jspx_th_logic_005fiterate_005f0);
                    out.write("\r\n");
                    out.write("\t\t\t\t");
                    int evalDoAfterBody = _jspx_th_logic_005fnotEmpty_005f0.doAfterBody();
                    if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
                } while (true);
            }
            if (_jspx_th_logic_005fnotEmpty_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005flogic_005fnotEmpty_0026_005fname.reuse(_jspx_th_logic_005fnotEmpty_005f0);
                return;
            }
            _005fjspx_005ftagPool_005flogic_005fnotEmpty_0026_005fname.reuse(_jspx_th_logic_005fnotEmpty_005f0);
            out.write("\r\n");
            out.write("\t\t\t\t");
            if (_jspx_meth_logic_005fempty_005f0(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("\t\t\t\t<tr>\r\n");
            out.write("\t\t\t\t\t<td colspan=\"5\" class=\"page\" style=\"padding-top:10px;\">");
            if (_jspx_meth_mytag_005fpage_005f0(_jspx_page_context)) return;
            out.write("</td>\r\n");
            out.write("\t\t\t\t</tr>\r\n");
            out.write("\t\t\t</table>\r\n");
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

    private boolean _jspx_meth_mytag_005fList_005f0(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        com.zhongkai.web.tag.ListTag _jspx_th_mytag_005fList_005f0 = (com.zhongkai.web.tag.ListTag) _005fjspx_005ftagPool_005fmytag_005fList_0026_005ftable_005fname_005fnobody.get(com.zhongkai.web.tag.ListTag.class);
        _jspx_th_mytag_005fList_005f0.setPageContext(_jspx_page_context);
        _jspx_th_mytag_005fList_005f0.setParent(null);
        _jspx_th_mytag_005fList_005f0.setName("roleList");
        _jspx_th_mytag_005fList_005f0.setTable("Role");
        int _jspx_eval_mytag_005fList_005f0 = _jspx_th_mytag_005fList_005f0.doStartTag();
        if (_jspx_th_mytag_005fList_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fmytag_005fList_0026_005ftable_005fname_005fnobody.reuse(_jspx_th_mytag_005fList_005f0);
            return true;
        }
        _005fjspx_005ftagPool_005fmytag_005fList_0026_005ftable_005fname_005fnobody.reuse(_jspx_th_mytag_005fList_005f0);
        return false;
    }

    private boolean _jspx_meth_logic_005fempty_005f0(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.struts.taglib.logic.EmptyTag _jspx_th_logic_005fempty_005f0 = (org.apache.struts.taglib.logic.EmptyTag) _005fjspx_005ftagPool_005flogic_005fempty_0026_005fname.get(org.apache.struts.taglib.logic.EmptyTag.class);
        _jspx_th_logic_005fempty_005f0.setPageContext(_jspx_page_context);
        _jspx_th_logic_005fempty_005f0.setParent(null);
        _jspx_th_logic_005fempty_005f0.setName("roleList");
        int _jspx_eval_logic_005fempty_005f0 = _jspx_th_logic_005fempty_005f0.doStartTag();
        if (_jspx_eval_logic_005fempty_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
                out.write("\r\n");
                out.write("\t\t\t\t\t<tr>\r\n");
                out.write("\t\t\t\t\t\t<td colspan=\"5\">暂无记录</td>\r\n");
                out.write("\t\t\t\t\t</tr>\r\n");
                out.write("\t\t\t\t");
                int evalDoAfterBody = _jspx_th_logic_005fempty_005f0.doAfterBody();
                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
            } while (true);
        }
        if (_jspx_th_logic_005fempty_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005flogic_005fempty_0026_005fname.reuse(_jspx_th_logic_005fempty_005f0);
            return true;
        }
        _005fjspx_005ftagPool_005flogic_005fempty_0026_005fname.reuse(_jspx_th_logic_005fempty_005f0);
        return false;
    }

    private boolean _jspx_meth_mytag_005fpage_005f0(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        com.zhongkai.web.tag.PageTag _jspx_th_mytag_005fpage_005f0 = (com.zhongkai.web.tag.PageTag) _005fjspx_005ftagPool_005fmytag_005fpage_005fnobody.get(com.zhongkai.web.tag.PageTag.class);
        _jspx_th_mytag_005fpage_005f0.setPageContext(_jspx_page_context);
        _jspx_th_mytag_005fpage_005f0.setParent(null);
        int _jspx_eval_mytag_005fpage_005f0 = _jspx_th_mytag_005fpage_005f0.doStartTag();
        if (_jspx_th_mytag_005fpage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fmytag_005fpage_005fnobody.reuse(_jspx_th_mytag_005fpage_005f0);
            return true;
        }
        _005fjspx_005ftagPool_005fmytag_005fpage_005fnobody.reuse(_jspx_th_mytag_005fpage_005f0);
        return false;
    }
}
