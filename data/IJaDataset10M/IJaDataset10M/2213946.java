package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class principal_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(1);
    _jspx_dependants.add("/WEB-INF/c.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fout_005fvalue_005fnobody;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fc_005fout_005fvalue_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fc_005fout_005fvalue_005fnobody.release();
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

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
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
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
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("<!-- \r\n");
      out.write("<meta Http-Equiv=\"Cache-Control\" Content=\"no-cache\"> \r\n");
      out.write("<meta Http-Equiv=\"Cache-Control\" Content=\"no-store\"> \r\n");
      out.write("<meta Http-Equiv=\"Pragma\" Content=\"no-cache\">  \r\n");
      out.write("<meta Http-Equiv=\"Expires\" Content=\"0\"> \r\n");
      out.write("-->\r\n");
      out.write("<!--\r\n");

response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching
response.setHeader("Cache-Control","no-store"); //HTTP 1.1

      out.write(" \r\n");
      out.write("-->\r\n");
      out.write("\r\n");

response.expires = 0
response.expiresabsolute = Now() - 1
response.addHeader "pragma","no-cache"
response.addHeader "cache-control","private"
Response.CacheControl = "no-cache"

      out.write("\r\n");
      out.write("\r\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/layoutTecnica.css\" />\r\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/fonts.css\" />\r\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/menuCss.css\" />\r\n");
      out.write("<script language=\"JavaScript1.2\" type=\"text/javascript\"\r\n");
      out.write("\tsrc=\"css/menu.js\"></script>\r\n");
      out.write("</head>\r\n");
      out.write("\r\n");
      out.write("<title>Floapp - Repositorio</title>\r\n");
      out.write("\r\n");
      out.write("<body>\r\n");
      out.write("\r\n");
      out.write("<div id=\"menu\">\r\n");
      out.write("<div id=\"topoEsq\"></div>\r\n");
      out.write("<div id=\"topoDir\"></div>\r\n");
      out.write("<div id=\"topoCentral\"></div>\r\n");
      out.write("<div id=\"colunaEsq\"></div>\r\n");
      out.write("<div id=\"colunaDir\"></div>\r\n");
      out.write("<div style=\"height: 100px;\"></div>\r\n");
      out.write("<div id=\"colunaCentral\" style=\" text-align:justify;\">\r\n");
      out.write("\t<div class=\"suckertreemenu\" align=\"center\">\r\n");
      out.write("\t<ul id=\"treemenu1\">\r\n");
      out.write("\t\t<li><a href=\"./principal.jsp\">Home</a>\r\n");
      out.write("\t\t<li><a href=\"javascript:;\">Projetos</a>\r\n");
      out.write("\t\t<ul>\r\n");
      out.write("\t\t\t<li><a href=\"./ListaMeusProjetosAction.do\">Meus Projetos</a></li>\r\n");
      out.write("\t\t\t<li><a href=\"./cadastrarProjeto.jsp\">Cadastrar</a></li>\r\n");
      out.write("\t\t\t<li><a href=\"./ListaRemoveAction.do\">Remover</a></li>\r\n");
      out.write("\t\t\t<li><a href=\"./atualizarProjeto.jsp\">Atualizar</a></li>\r\n");
      out.write("\t\t\t<li><a href=\"./pesquisarProjeto.jsp\">Pesquisar</a></li>\r\n");
      out.write("\t\t\t<li><a href=\"./ListarProjetosAssociaAction.do\">Associar</a></li>\r\n");
      out.write("\t\t\t<li><a href=\"./ListarRequisicoesAction.do\">Requisições</a></li>\r\n");
      out.write("\t\t</ul>\r\n");
      out.write("\t\t</li>\r\n");
      out.write("\t\t<li><a href=\"./LogoutAction.do\">Logout</a></li>\r\n");
      out.write("\t\r\n");
      out.write("\t</ul>\r\n");
      out.write("\t</div>\r\n");
      out.write("\t<br><br><br><br><br><br><br><br>\r\n");
      out.write("\t\r\n");
      out.write("\t<h1>Minha Pagina</h1> <br>\r\n");
      out.write("\t<b>Nome: </b>");
      if (_jspx_meth_c_005fout_005f0(_jspx_page_context))
        return;
      out.write("</b>\r\n");
      out.write("\t<br><br>\r\n");
      out.write("\t<b>Usuario: </b>");
      if (_jspx_meth_c_005fout_005f1(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("\t<br><br>\r\n");
      out.write("\t<b>Senha: </b> ");
      if (_jspx_meth_c_005fout_005f2(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("\t</div>\r\n");
      out.write("\t<div id=\"footer\" align=\"center\"> \r\n");
      out.write("\t<img src=\"imagens/prinipal/barra embaixo.jpg\" ></div>\r\n");
      out.write("</div>\r\n");
      out.write("\r\n");
      out.write("</body>\r\n");
      out.write("</html>");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_c_005fout_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.el.core.OutTag _jspx_th_c_005fout_005f0 = (org.apache.taglibs.standard.tag.el.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.el.core.OutTag.class);
    _jspx_th_c_005fout_005f0.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f0.setParent(null);
    // /principal.jsp(70,14) name = value type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f0.setValue("${usuario.nome}");
    int _jspx_eval_c_005fout_005f0 = _jspx_th_c_005fout_005f0.doStartTag();
    if (_jspx_th_c_005fout_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f0);
    return false;
  }

  private boolean _jspx_meth_c_005fout_005f1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.el.core.OutTag _jspx_th_c_005fout_005f1 = (org.apache.taglibs.standard.tag.el.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.el.core.OutTag.class);
    _jspx_th_c_005fout_005f1.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f1.setParent(null);
    // /principal.jsp(72,17) name = value type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f1.setValue("${usuario.usuario}");
    int _jspx_eval_c_005fout_005f1 = _jspx_th_c_005fout_005f1.doStartTag();
    if (_jspx_th_c_005fout_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f1);
    return false;
  }

  private boolean _jspx_meth_c_005fout_005f2(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.el.core.OutTag _jspx_th_c_005fout_005f2 = (org.apache.taglibs.standard.tag.el.core.OutTag) _005fjspx_005ftagPool_005fc_005fout_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.el.core.OutTag.class);
    _jspx_th_c_005fout_005f2.setPageContext(_jspx_page_context);
    _jspx_th_c_005fout_005f2.setParent(null);
    // /principal.jsp(74,16) name = value type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fout_005f2.setValue("${usuario.senha}");
    int _jspx_eval_c_005fout_005f2 = _jspx_th_c_005fout_005f2.doStartTag();
    if (_jspx_th_c_005fout_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fout_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fout_005fvalue_005fnobody.reuse(_jspx_th_c_005fout_005f2);
    return false;
  }
}
