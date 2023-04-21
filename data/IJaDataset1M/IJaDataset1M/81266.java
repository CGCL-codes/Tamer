package org.apache.jsp.src.jsp.shouhin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class A002_jsp extends org.apache.jasper.runtime.HttpJspBase implements org.apache.jasper.runtime.JspSourceDependent {

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
            response.setContentType("text/html;charset=UTF-8");
            pageContext = _jspxFactory.getPageContext(this, request, response, null, true, 8192, true);
            _jspx_page_context = pageContext;
            application = pageContext.getServletContext();
            config = pageContext.getServletConfig();
            session = pageContext.getSession();
            out = pageContext.getOut();
            _jspx_out = out;
            out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n");
            out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"ja\" lang=\"ja\">\r\n");
            out.write("<head>\r\n");
            out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n");
            out.write("<meta http-equiv=\"content-type\" content=\"text/css\" />\r\n");
            out.write("<link rel=\"stylesheet\" href=\"../css/base.css\" type=\"text/css\" media=\"all\" />\r\n");
            out.write("<link rel=\"stylesheet\" href=\"../css/tip.css\" type=\"text/css\" media=\"all\" />\r\n");
            out.write("\r\n");
            out.write("<title>商品詳細</title>\r\n");
            out.write("<script type=\"text/javascript\">\r\n");
            out.write("<!--\r\n");
            out.write("function hyouzi() {\r\n");
            out.write("document.form1.number2.value = Number(document.form1.number2.value) + Number(document.form1.number.value);\r\n");
            out.write("\t\t\tdocument.form1.number.value = \"\";\r\n");
            out.write("}\r\n");
            out.write("-->\r\n");
            out.write("</script>\r\n");
            out.write("</head>\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("<body>\r\n");
            out.write("<form name=\"form1\">\r\n");
            out.write("<div id=\"wrapper\">\r\n");
            out.write("\r\n");
            out.write("<div id=\"header\">\r\n");
            out.write("\t<h1><a href=\"top.html\"><img src=\"../../img/kyoutu/head4.jpg\" width=\"765px\" height=\"110\" alt=\"うるまかりゆし商店\"/></a></h1>\r\n");
            out.write("\r\n");
            out.write("\t<ul>\r\n");
            out.write("\t\t<li id=\"top\"><a href=\"top.jsp\">トップページ</a></li>\r\n");
            out.write("\t\t<li id=\"foods\"><span>食品</span></li>\r\n");
            out.write("\t\t<li id=\"craft\"><a href=\"./craft.jsp\">民芸品</a></li>\r\n");
            out.write("\t\t<li id=\"book_cd\"><a href=\"./book_cd.jsp\">本・CD</a></li>\r\n");
            out.write("\t\t<li id=\"info\"><a href=\"./cart.jsp\">カートを見る</a></li>\r\n");
            out.write("\t\t<li id=\"faq\"><a href=\"#\">お問い合わせ</a></li>\r\n");
            out.write("\t</ul>\r\n");
            out.write("</div><!-- header_END -->\r\n");
            out.write("\r\n");
            out.write("<div id=\"pagebody\">\r\n");
            out.write("\r\n");
            out.write("<div id=\"sidebar\">\r\n");
            out.write("\r\n");
            out.write("\t<h2>ログイン</h2>\r\n");
            out.write("<div id=\"login\">\r\n");
            out.write("\r\n");
            out.write("\t<label class=\"sidelabel\" for=\"identify\"\">お客様番号</label>\r\n");
            out.write("\t<input type=\"text\" id=\"identify\"></input>\r\n");
            out.write("\t<label class=\"sidelabel\" for=\"pass\">パスワード</label>\r\n");
            out.write("\t<input type=\"text\" id=\"pass\"></input>\r\n");
            out.write("\t<input type=\"submit\" id=\"loginsub\" value=\"送信\" />\r\n");
            out.write("\r\n");
            out.write("\t<a href=\"registration.html\">新規登録</a>\r\n");
            out.write("</div><!-- login_END -->\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("</div><!-- sidebar_END -->\r\n");
            out.write("<div id=\"content\">\r\n");
            out.write("\t<h2>石垣島産伊勢えび</h2>\r\n");
            out.write("\t<h3><img src=\"../../img/shouhin/A001_L.jpg\" alt=\"石垣島産伊勢えび\" width=\"240\" height=\"180\"></img></h3>\r\n");
            out.write("\t<dl>\r\n");
            out.write("\t\t<dt>商品詳細</dt>\r\n");
            out.write("\t\t<dd>商品の詳細文が入ります。商品の詳細文が入ります。商品の詳細文が入ります。</dd>\r\n");
            out.write("\t\t<dt>販売価格</dt>\r\n");
            out.write("\t\t<dd>\\5,000</dd>\r\n");
            out.write("\t</dl>\r\n");
            out.write("\t<label for=\"number\">購入個数</label>\r\n");
            out.write("\t<input type=\"text\" size=\"1\" id=\"number\"></input> <input type=\"text\" size=\"1\" id=\"number2\"></input>\r\n");
            out.write("\t<p><a href=\"foods.jsp\"><img src=\"../../img/kyoutu/cart.gif\" alt=\"商品をカートに入れる\" onclick=\"hyouzi()\"></img></a></p>\r\n");
            out.write("\r\n");
            out.write("</div><!-- content_END -->\r\n");
            out.write("</div><!-- pagebody_END -->\r\n");
            out.write("\r\n");
            out.write("<div id=\"footer\">\r\n");
            out.write("\t<ul>\r\n");
            out.write("\t\t<li id=\"privacy\"><a href=\"#\" title=\"プライバシーポリシー\">privacy</a></li>\r\n");
            out.write("\t\t<li id=\"sitemap\"><a href=\"#\" title=\"サイトマップ\">sitemap</a></li>\r\n");
            out.write("\t\t<li id=\"info\"><a href=\"#\" title=\"お問い合わせ\">info</a></li>\r\n");
            out.write("\t</ul>\r\n");
            out.write("\r\n");
            out.write("\t<p>All rights reserved Nak Corporation &amp; Co.2010-2011</p>\r\n");
            out.write("\r\n");
            out.write("</div><!-- footer_END -->\r\n");
            out.write("\r\n");
            out.write("</div><!-- wrapper_END -->\r\n");
            out.write("\r\n");
            out.write("</form>\r\n");
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
