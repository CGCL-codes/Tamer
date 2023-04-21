package org.apache.jsp.WEB_002dINF.view;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase implements org.apache.jasper.runtime.JspSourceDependent {

    private static java.util.List _jspx_dependants;

    static {
        _jspx_dependants = new java.util.ArrayList(1);
        _jspx_dependants.add("/WEB-INF/view/common/common.jsp");
    }

    private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fs_005fform;

    private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fhtml_005ftext_0026_005fproperty_005fnobody;

    private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fhtml_005fpassword_0026_005fproperty_005fnobody;

    public Object getDependants() {
        return _jspx_dependants;
    }

    public void _jspInit() {
        _005fjspx_005ftagPool_005fs_005fform = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
        _005fjspx_005ftagPool_005fhtml_005ftext_0026_005fproperty_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
        _005fjspx_005ftagPool_005fhtml_005fpassword_0026_005fproperty_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    }

    public void _jspDestroy() {
        _005fjspx_005ftagPool_005fs_005fform.release();
        _005fjspx_005ftagPool_005fhtml_005ftext_0026_005fproperty_005fnobody.release();
        _005fjspx_005ftagPool_005fhtml_005fpassword_0026_005fproperty_005fnobody.release();
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
            response.setContentType("text/html;charset=UTF-8");
            pageContext = _jspxFactory.getPageContext(this, request, response, null, true, 8192, true);
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
            out.write("  \r\n");
            out.write("  \r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("<html>\r\n");
            out.write("<head>\r\n");
            out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\r\n");
            out.write("<title>Dolteng Auto Generated</title>\r\n");
            out.write("</head>\r\n");
            out.write("<body>\r\n");
            out.write("<h1>トップページ</h1>\r\n");
            if (_jspx_meth_s_005fform_005f0(_jspx_page_context)) return;
            out.write('\r');
            out.write('\n');
            if (_jspx_meth_s_005fform_005f1(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("</body>\r\n");
            out.write("</html>");
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

    private boolean _jspx_meth_s_005fform_005f0(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.seasar.struts.taglib.S2FormTag _jspx_th_s_005fform_005f0 = (org.seasar.struts.taglib.S2FormTag) _005fjspx_005ftagPool_005fs_005fform.get(org.seasar.struts.taglib.S2FormTag.class);
        _jspx_th_s_005fform_005f0.setPageContext(_jspx_page_context);
        _jspx_th_s_005fform_005f0.setParent(null);
        int _jspx_eval_s_005fform_005f0 = _jspx_th_s_005fform_005f0.doStartTag();
        if (_jspx_eval_s_005fform_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
                out.write("\r\n");
                out.write("名前");
                if (_jspx_meth_html_005ftext_005f0(_jspx_th_s_005fform_005f0, _jspx_page_context)) return true;
                out.write("パスワード");
                if (_jspx_meth_html_005fpassword_005f0(_jspx_th_s_005fform_005f0, _jspx_page_context)) return true;
                out.write("<input type=\"submit\" name=\"login\" value=\"management\">\r\n");
                int evalDoAfterBody = _jspx_th_s_005fform_005f0.doAfterBody();
                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
            } while (true);
        }
        if (_jspx_th_s_005fform_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fs_005fform.reuse(_jspx_th_s_005fform_005f0);
            return true;
        }
        _005fjspx_005ftagPool_005fs_005fform.reuse(_jspx_th_s_005fform_005f0);
        return false;
    }

    private boolean _jspx_meth_html_005ftext_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fform_005f0, PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.struts.taglib.html.TextTag _jspx_th_html_005ftext_005f0 = (org.apache.struts.taglib.html.TextTag) _005fjspx_005ftagPool_005fhtml_005ftext_0026_005fproperty_005fnobody.get(org.apache.struts.taglib.html.TextTag.class);
        _jspx_th_html_005ftext_005f0.setPageContext(_jspx_page_context);
        _jspx_th_html_005ftext_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fform_005f0);
        _jspx_th_html_005ftext_005f0.setProperty("name");
        int _jspx_eval_html_005ftext_005f0 = _jspx_th_html_005ftext_005f0.doStartTag();
        if (_jspx_th_html_005ftext_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fhtml_005ftext_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005ftext_005f0);
            return true;
        }
        _005fjspx_005ftagPool_005fhtml_005ftext_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005ftext_005f0);
        return false;
    }

    private boolean _jspx_meth_html_005fpassword_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fform_005f0, PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.struts.taglib.html.PasswordTag _jspx_th_html_005fpassword_005f0 = (org.apache.struts.taglib.html.PasswordTag) _005fjspx_005ftagPool_005fhtml_005fpassword_0026_005fproperty_005fnobody.get(org.apache.struts.taglib.html.PasswordTag.class);
        _jspx_th_html_005fpassword_005f0.setPageContext(_jspx_page_context);
        _jspx_th_html_005fpassword_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fform_005f0);
        _jspx_th_html_005fpassword_005f0.setProperty("pass");
        int _jspx_eval_html_005fpassword_005f0 = _jspx_th_html_005fpassword_005f0.doStartTag();
        if (_jspx_th_html_005fpassword_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fhtml_005fpassword_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005fpassword_005f0);
            return true;
        }
        _005fjspx_005ftagPool_005fhtml_005fpassword_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005fpassword_005f0);
        return false;
    }

    private boolean _jspx_meth_s_005fform_005f1(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.seasar.struts.taglib.S2FormTag _jspx_th_s_005fform_005f1 = (org.seasar.struts.taglib.S2FormTag) _005fjspx_005ftagPool_005fs_005fform.get(org.seasar.struts.taglib.S2FormTag.class);
        _jspx_th_s_005fform_005f1.setPageContext(_jspx_page_context);
        _jspx_th_s_005fform_005f1.setParent(null);
        int _jspx_eval_s_005fform_005f1 = _jspx_th_s_005fform_005f1.doStartTag();
        if (_jspx_eval_s_005fform_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
                out.write('\r');
                out.write('\n');
                if (_jspx_meth_html_005ftext_005f1(_jspx_th_s_005fform_005f1, _jspx_page_context)) return true;
                out.write("<input type=\"submit\" name=\"search\" value=\"検索\">\r\n");
                int evalDoAfterBody = _jspx_th_s_005fform_005f1.doAfterBody();
                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
            } while (true);
        }
        if (_jspx_th_s_005fform_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fs_005fform.reuse(_jspx_th_s_005fform_005f1);
            return true;
        }
        _005fjspx_005ftagPool_005fs_005fform.reuse(_jspx_th_s_005fform_005f1);
        return false;
    }

    private boolean _jspx_meth_html_005ftext_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fform_005f1, PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.struts.taglib.html.TextTag _jspx_th_html_005ftext_005f1 = (org.apache.struts.taglib.html.TextTag) _005fjspx_005ftagPool_005fhtml_005ftext_0026_005fproperty_005fnobody.get(org.apache.struts.taglib.html.TextTag.class);
        _jspx_th_html_005ftext_005f1.setPageContext(_jspx_page_context);
        _jspx_th_html_005ftext_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fform_005f1);
        _jspx_th_html_005ftext_005f1.setProperty("data");
        int _jspx_eval_html_005ftext_005f1 = _jspx_th_html_005ftext_005f1.doStartTag();
        if (_jspx_th_html_005ftext_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fhtml_005ftext_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005ftext_005f1);
            return true;
        }
        _005fjspx_005ftagPool_005fhtml_005ftext_0026_005fproperty_005fnobody.reuse(_jspx_th_html_005ftext_005f1);
        return false;
    }
}
