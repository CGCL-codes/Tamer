package org.apache.jsp.common;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class footer_jsp extends org.apache.jasper.runtime.HttpJspBase implements org.apache.jasper.runtime.JspSourceDependent {

    private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

    private static java.util.List _jspx_dependants;

    static {
        _jspx_dependants = new java.util.ArrayList(2);
        _jspx_dependants.add("/common/taglibs.jsp");
        _jspx_dependants.add("/WEB-INF/appfuse.tld");
    }

    private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody;

    private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fset_0026_005fvar;

    private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody;

    private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;

    private javax.el.ExpressionFactory _el_expressionfactory;

    private org.apache.AnnotationProcessor _jsp_annotationprocessor;

    public Object getDependants() {
        return _jspx_dependants;
    }

    public void _jspInit() {
        _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
        _005fjspx_005ftagPool_005fc_005fset_0026_005fvar = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
        _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
        _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
    }

    public void _jspDestroy() {
        _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.release();
        _005fjspx_005ftagPool_005fc_005fset_0026_005fvar.release();
        _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.release();
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
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
            response.setContentType("text/html;charset=utf-8");
            pageContext = _jspxFactory.getPageContext(this, request, response, "/error.jsp", true, 8192, true);
            _jspx_page_context = pageContext;
            application = pageContext.getServletContext();
            config = pageContext.getServletConfig();
            session = pageContext.getSession();
            out = pageContext.getOut();
            _jspx_out = out;
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            if (_jspx_meth_c_005fset_005f0(_jspx_page_context)) return;
            out.write('\r');
            out.write('\n');
            if (_jspx_meth_c_005fset_005f1(_jspx_page_context)) return;
            out.write("\n");
            out.write("\n");
            out.write("    <div id=\"divider\"><div></div></div>\n");
            out.write("    <span class=\"left\">");
            if (_jspx_meth_fmt_005fmessage_005f1(_jspx_page_context)) return;
            out.write(" |\n");
            out.write("        <span id=\"validators\">\n");
            out.write("            <a href=\"http://validator.w3.org/check?uri=referer\">XHTML Valid</a> |\n");
            out.write("            <a href=\"http://jigsaw.w3.org/css-validator/validator-uri.html\">CSS Valid</a>\n");
            out.write("        </span>\n");
            out.write("        ");
            if (_jspx_meth_c_005fif_005f0(_jspx_page_context)) return;
            out.write("\n");
            out.write("    </span>\n");
            out.write("    <span class=\"right\">\n");
            out.write("        &copy; ");
            if (_jspx_meth_fmt_005fmessage_005f3(_jspx_page_context)) return;
            out.write(" <a href=\"");
            if (_jspx_meth_fmt_005fmessage_005f4(_jspx_page_context)) return;
            out.write('"');
            out.write('>');
            if (_jspx_meth_fmt_005fmessage_005f5(_jspx_page_context)) return;
            out.write("</a>\n");
            out.write("    </span>\n");
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

    private boolean _jspx_meth_c_005fset_005f0(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.taglibs.standard.tag.rt.core.SetTag _jspx_th_c_005fset_005f0 = (org.apache.taglibs.standard.tag.rt.core.SetTag) _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.SetTag.class);
        _jspx_th_c_005fset_005f0.setPageContext(_jspx_page_context);
        _jspx_th_c_005fset_005f0.setParent(null);
        _jspx_th_c_005fset_005f0.setVar("ctx");
        _jspx_th_c_005fset_005f0.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.contextPath}", java.lang.Object.class, (PageContext) _jspx_page_context, null, false));
        int _jspx_eval_c_005fset_005f0 = _jspx_th_c_005fset_005f0.doStartTag();
        if (_jspx_th_c_005fset_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.reuse(_jspx_th_c_005fset_005f0);
            return true;
        }
        _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.reuse(_jspx_th_c_005fset_005f0);
        return false;
    }

    private boolean _jspx_meth_c_005fset_005f1(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.taglibs.standard.tag.rt.core.SetTag _jspx_th_c_005fset_005f1 = (org.apache.taglibs.standard.tag.rt.core.SetTag) _005fjspx_005ftagPool_005fc_005fset_0026_005fvar.get(org.apache.taglibs.standard.tag.rt.core.SetTag.class);
        _jspx_th_c_005fset_005f1.setPageContext(_jspx_page_context);
        _jspx_th_c_005fset_005f1.setParent(null);
        _jspx_th_c_005fset_005f1.setVar("datePattern");
        int _jspx_eval_c_005fset_005f1 = _jspx_th_c_005fset_005f1.doStartTag();
        if (_jspx_eval_c_005fset_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_c_005fset_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                out = _jspx_page_context.pushBody();
                _jspx_th_c_005fset_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                _jspx_th_c_005fset_005f1.doInitBody();
            }
            do {
                if (_jspx_meth_fmt_005fmessage_005f0(_jspx_th_c_005fset_005f1, _jspx_page_context)) return true;
                int evalDoAfterBody = _jspx_th_c_005fset_005f1.doAfterBody();
                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
            } while (true);
            if (_jspx_eval_c_005fset_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                out = _jspx_page_context.popBody();
            }
        }
        if (_jspx_th_c_005fset_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fset_0026_005fvar.reuse(_jspx_th_c_005fset_005f1);
            return true;
        }
        _005fjspx_005ftagPool_005fc_005fset_0026_005fvar.reuse(_jspx_th_c_005fset_005f1);
        return false;
    }

    private boolean _jspx_meth_fmt_005fmessage_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fset_005f1, PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f0 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
        _jspx_th_fmt_005fmessage_005f0.setPageContext(_jspx_page_context);
        _jspx_th_fmt_005fmessage_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fset_005f1);
        _jspx_th_fmt_005fmessage_005f0.setKey("date.format");
        int _jspx_eval_fmt_005fmessage_005f0 = _jspx_th_fmt_005fmessage_005f0.doStartTag();
        if (_jspx_th_fmt_005fmessage_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f0);
            return true;
        }
        _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f0);
        return false;
    }

    private boolean _jspx_meth_fmt_005fmessage_005f1(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f1 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
        _jspx_th_fmt_005fmessage_005f1.setPageContext(_jspx_page_context);
        _jspx_th_fmt_005fmessage_005f1.setParent(null);
        _jspx_th_fmt_005fmessage_005f1.setKey("webapp.version");
        int _jspx_eval_fmt_005fmessage_005f1 = _jspx_th_fmt_005fmessage_005f1.doStartTag();
        if (_jspx_th_fmt_005fmessage_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f1);
            return true;
        }
        _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f1);
        return false;
    }

    private boolean _jspx_meth_c_005fif_005f0(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
        _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
        _jspx_th_c_005fif_005f0.setParent(null);
        _jspx_th_c_005fif_005f0.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.remoteUser != null}", java.lang.Boolean.class, (PageContext) _jspx_page_context, null, false)).booleanValue());
        int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
        if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
                out.write("\n");
                out.write("        | ");
                if (_jspx_meth_fmt_005fmessage_005f2(_jspx_th_c_005fif_005f0, _jspx_page_context)) return true;
                out.write(' ');
                out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageContext.request.remoteUser}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                out.write("\n");
                out.write("        ");
                int evalDoAfterBody = _jspx_th_c_005fif_005f0.doAfterBody();
                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
            } while (true);
        }
        if (_jspx_th_c_005fif_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
            return true;
        }
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
        return false;
    }

    private boolean _jspx_meth_fmt_005fmessage_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f0, PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f2 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
        _jspx_th_fmt_005fmessage_005f2.setPageContext(_jspx_page_context);
        _jspx_th_fmt_005fmessage_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
        _jspx_th_fmt_005fmessage_005f2.setKey("user.status");
        int _jspx_eval_fmt_005fmessage_005f2 = _jspx_th_fmt_005fmessage_005f2.doStartTag();
        if (_jspx_th_fmt_005fmessage_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f2);
            return true;
        }
        _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f2);
        return false;
    }

    private boolean _jspx_meth_fmt_005fmessage_005f3(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f3 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
        _jspx_th_fmt_005fmessage_005f3.setPageContext(_jspx_page_context);
        _jspx_th_fmt_005fmessage_005f3.setParent(null);
        _jspx_th_fmt_005fmessage_005f3.setKey("copyright.year");
        int _jspx_eval_fmt_005fmessage_005f3 = _jspx_th_fmt_005fmessage_005f3.doStartTag();
        if (_jspx_th_fmt_005fmessage_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f3);
            return true;
        }
        _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f3);
        return false;
    }

    private boolean _jspx_meth_fmt_005fmessage_005f4(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f4 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
        _jspx_th_fmt_005fmessage_005f4.setPageContext(_jspx_page_context);
        _jspx_th_fmt_005fmessage_005f4.setParent(null);
        _jspx_th_fmt_005fmessage_005f4.setKey("company.url");
        int _jspx_eval_fmt_005fmessage_005f4 = _jspx_th_fmt_005fmessage_005f4.doStartTag();
        if (_jspx_th_fmt_005fmessage_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f4);
            return true;
        }
        _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f4);
        return false;
    }

    private boolean _jspx_meth_fmt_005fmessage_005f5(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.taglibs.standard.tag.rt.fmt.MessageTag _jspx_th_fmt_005fmessage_005f5 = (org.apache.taglibs.standard.tag.rt.fmt.MessageTag) _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.MessageTag.class);
        _jspx_th_fmt_005fmessage_005f5.setPageContext(_jspx_page_context);
        _jspx_th_fmt_005fmessage_005f5.setParent(null);
        _jspx_th_fmt_005fmessage_005f5.setKey("company.name");
        int _jspx_eval_fmt_005fmessage_005f5 = _jspx_th_fmt_005fmessage_005f5.doStartTag();
        if (_jspx_th_fmt_005fmessage_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f5);
            return true;
        }
        _005fjspx_005ftagPool_005ffmt_005fmessage_0026_005fkey_005fnobody.reuse(_jspx_th_fmt_005fmessage_005f5);
        return false;
    }
}
