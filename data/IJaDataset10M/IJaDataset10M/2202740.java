package org.apache.jsp.WEB_002dINF.jsp.relatorio.convenio;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class policia_005fsemfiod_jsp extends org.apache.jasper.runtime.HttpJspBase implements org.apache.jasper.runtime.JspSourceDependent {

    private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

    private static java.util.List _jspx_dependants;

    private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fhtml_005flink_005fstyleId_005fhref;

    private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fhtml_005fform_005faction;

    private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fhtml_005fhidden_005fstyleId_005fproperty_005fnobody;

    private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fhtml_005ftext_005fstyleId_005fsize_005fproperty_005fnobody;

    private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fhtml_005ftext_005fstyleId_005fsize_005fproperty_005fmaxlength_005fnobody;

    private javax.el.ExpressionFactory _el_expressionfactory;

    private org.apache.AnnotationProcessor _jsp_annotationprocessor;

    public Object getDependants() {
        return _jspx_dependants;
    }

    public void _jspInit() {
        _005fjspx_005ftagPool_005fhtml_005flink_005fstyleId_005fhref = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
        _005fjspx_005ftagPool_005fhtml_005fform_005faction = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
        _005fjspx_005ftagPool_005fhtml_005fhidden_005fstyleId_005fproperty_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
        _005fjspx_005ftagPool_005fhtml_005ftext_005fstyleId_005fsize_005fproperty_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
        _005fjspx_005ftagPool_005fhtml_005ftext_005fstyleId_005fsize_005fproperty_005fmaxlength_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
        _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
    }

    public void _jspDestroy() {
        _005fjspx_005ftagPool_005fhtml_005flink_005fstyleId_005fhref.release();
        _005fjspx_005ftagPool_005fhtml_005fform_005faction.release();
        _005fjspx_005ftagPool_005fhtml_005fhidden_005fstyleId_005fproperty_005fnobody.release();
        _005fjspx_005ftagPool_005fhtml_005ftext_005fstyleId_005fsize_005fproperty_005fnobody.release();
        _005fjspx_005ftagPool_005fhtml_005ftext_005fstyleId_005fsize_005fproperty_005fmaxlength_005fnobody.release();
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
            response.setContentType("text/html");
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
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("<script src=\"./js/geral.js\" type=\"text/javascript\"></script>\r\n");
            out.write("<script src=\"./js/jQuery.js\" type=\"text/javascript\"></script>\r\n");
            out.write("<script src=\"./js/calendar.js\" type=\"text/javascript\"></script>\r\n");
            out.write("<script src=\"./js/jquery.alphanumeric.pack.js\" type=\"text/javascript\"></script>\r\n");
            out.write("<script src=\"./js/jquery.tablesorter.js\" type=\"text/javascript\"></script>\r\n");
            out.write("<script src=\"./js/jquery.maskedinput.js\" type=\"text/javascript\"></script>\r\n");
            out.write("<script src=\"./js/linhaTabela.js\" type=\"text/javascript\"></script>\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("<script type=\"text/javascript\">\r\n");
            out.write("     \r\n");
            out.write("     \r\n");
            out.write("     $().ready(function(){\r\n");
            out.write("     \t$(\"#dataInicio\").mask(\"99/99/9999\");\r\n");
            out.write("\t\t$(\"#dataFinal\").mask(\"99/99/9999\");\r\n");
            out.write("  \t });  \r\n");
            out.write("\r\n");
            out.write("\t\r\n");
            out.write("\tfunction carregarMascaras() {\r\n");
            out.write("\r\n");
            out.write("\t\tmesMask = new Mask(\"##\", \"number\");\r\n");
            out.write("\t\tmesMask.attach(document.getElementById('mesInicial'));\r\n");
            out.write("\t\tmesMask.attach(document.getElementById('mesFinal'));\r\n");
            out.write("\t\t\r\n");
            out.write("\t\tanoMask = new Mask(\"####\", \"number\");\r\n");
            out.write("\t\tanoMask.attach(document.getElementById('anoInicial'));\r\n");
            out.write("\t\tanoMask.attach(document.getElementById('anoFinal'));\r\n");
            out.write("\t\t\r\n");
            out.write("\t\t\r\n");
            out.write("\t}\r\n");
            out.write("\t\r\n");
            out.write("\r\n");
            out.write("\tfunction imprimir(id){\r\n");
            out.write("\t\tdocument.getElementById('metodo').value = \"imprimir\";\r\n");
            out.write("\t\tdocument.getElementById('reciboId').value = id;\r\n");
            out.write("\t\tdocument.forms[0].submit(); \r\n");
            out.write("\t}\r\n");
            out.write("\t\r\n");
            out.write("\tfunction vizualizar(){\r\n");
            out.write("\t\tdocument.getElementById('metodo').value = \"vizualizar\";\r\n");
            out.write("\t\tdocument.forms[0].submit()\r\n");
            out.write("\t}\r\n");
            out.write("\t\r\n");
            out.write("\tfunction inicializaCombo(){\r\n");
            out.write("\t\tvar a = document.getElementById('tipo').value;\r\n");
            out.write("\t\tdocument.forms[0].radioServico[a-1].checked = 1; \r\n");
            out.write("\t}\r\n");
            out.write("</script>\r\n");
            out.write("\r\n");
            out.write("<div id=\"corpo\">\r\n");
            out.write("\r\n");
            out.write("<div class=\"breadcrumb\">\r\n");
            out.write("\t");
            if (_jspx_meth_html_005flink_005f0(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("\t\t&raquo;<a class=\"ativo\" href=\"#\">Relatório de Conveniados à Polícia Militar sem FIOD no período</a> </div>\r\n");
            out.write("\r\n");
            if (_jspx_meth_html_005fform_005f0(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("\r\n");
            out.write("<script>\r\n");
            out.write("\tcarregarMascaras();\r\n");
            out.write("</script>\r\n");
            out.write("</div>");
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

    private boolean _jspx_meth_html_005flink_005f0(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.struts.taglib.html.LinkTag _jspx_th_html_005flink_005f0 = (org.apache.struts.taglib.html.LinkTag) _005fjspx_005ftagPool_005fhtml_005flink_005fstyleId_005fhref.get(org.apache.struts.taglib.html.LinkTag.class);
        _jspx_th_html_005flink_005f0.setPageContext(_jspx_page_context);
        _jspx_th_html_005flink_005f0.setParent(null);
        _jspx_th_html_005flink_005f0.setStyleId("incluir");
        _jspx_th_html_005flink_005f0.setHref("login.do");
        int _jspx_eval_html_005flink_005f0 = _jspx_th_html_005flink_005f0.doStartTag();
        if (_jspx_eval_html_005flink_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_html_005flink_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                out = _jspx_page_context.pushBody();
                _jspx_th_html_005flink_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                _jspx_th_html_005flink_005f0.doInitBody();
            }
            do {
                out.write("Início");
                int evalDoAfterBody = _jspx_th_html_005flink_005f0.doAfterBody();
                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
            } while (true);
            if (_jspx_eval_html_005flink_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                out = _jspx_page_context.popBody();
            }
        }
        if (_jspx_th_html_005flink_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fhtml_005flink_005fstyleId_005fhref.reuse(_jspx_th_html_005flink_005f0);
            return true;
        }
        _005fjspx_005ftagPool_005fhtml_005flink_005fstyleId_005fhref.reuse(_jspx_th_html_005flink_005f0);
        return false;
    }

    private boolean _jspx_meth_html_005fform_005f0(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.struts.taglib.html.FormTag _jspx_th_html_005fform_005f0 = (org.apache.struts.taglib.html.FormTag) _005fjspx_005ftagPool_005fhtml_005fform_005faction.get(org.apache.struts.taglib.html.FormTag.class);
        _jspx_th_html_005fform_005f0.setPageContext(_jspx_page_context);
        _jspx_th_html_005fform_005f0.setParent(null);
        _jspx_th_html_005fform_005f0.setAction("relatorioConvenioPoliciaSemFIOD.do	");
        int _jspx_eval_html_005fform_005f0 = _jspx_th_html_005fform_005f0.doStartTag();
        if (_jspx_eval_html_005fform_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
                out.write("\r\n");
                out.write("\t\r\n");
                out.write("\t");
                if (_jspx_meth_html_005fhidden_005f0(_jspx_th_html_005fform_005f0, _jspx_page_context)) return true;
                out.write("\r\n");
                out.write("\t\r\n");
                out.write("\t<h2>Relatório de Conveniados à Polícia Militar sem FIOD no período</h2>\r\n");
                out.write("\r\n");
                out.write("\t<fieldset>\r\n");
                out.write("\t\t<legend>Filtrar por:</legend>\r\n");
                out.write("\t\t<p class=\"nota\">Os campos marcados com \" * \" são obrigatórios</p>\r\n");
                out.write("\t\t\r\n");
                out.write("\t\t<label class=\"servico\">*Data Inicial:</label>\r\n");
                out.write("\t\t");
                if (_jspx_meth_html_005ftext_005f0(_jspx_th_html_005fform_005f0, _jspx_page_context)) return true;
                out.write(" \r\n");
                out.write("\t\t\r\n");
                out.write("\t\t<hr/>\r\n");
                out.write("\t\t\r\n");
                out.write("\t\t<label class=\"servico\">*Data Final:</label>\r\n");
                out.write("\t\t");
                if (_jspx_meth_html_005ftext_005f1(_jspx_th_html_005fform_005f0, _jspx_page_context)) return true;
                out.write(" \r\n");
                out.write("\t\t\t\t\r\n");
                out.write("\t\t<hr/>\r\n");
                out.write("\t\t\r\n");
                out.write("\t\t<label class=\"servico\">IPSM(s):</label>\r\n");
                out.write("\t\t");
                if (_jspx_meth_html_005ftext_005f2(_jspx_th_html_005fform_005f0, _jspx_page_context)) return true;
                out.write("\r\n");
                out.write("\t\t<span>Para filtar por mais de um ipsm separe-os por vírgula</span>\r\n");
                out.write("\t\t<hr/>\r\n");
                out.write("\t\t\r\n");
                out.write("\t\t<p class=\"comandos\"><a class=\"botao\" href=\"javascript:vizualizar()\"  name=\"visualizar\">Visualizar</a></p>\r\n");
                out.write("\t \t\t\r\n");
                out.write("\t</fieldset>\r\n");
                int evalDoAfterBody = _jspx_th_html_005fform_005f0.doAfterBody();
                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
            } while (true);
        }
        if (_jspx_th_html_005fform_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fhtml_005fform_005faction.reuse(_jspx_th_html_005fform_005f0);
            return true;
        }
        _005fjspx_005ftagPool_005fhtml_005fform_005faction.reuse(_jspx_th_html_005fform_005f0);
        return false;
    }

    private boolean _jspx_meth_html_005fhidden_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.struts.taglib.html.HiddenTag _jspx_th_html_005fhidden_005f0 = (org.apache.struts.taglib.html.HiddenTag) _005fjspx_005ftagPool_005fhtml_005fhidden_005fstyleId_005fproperty_005fnobody.get(org.apache.struts.taglib.html.HiddenTag.class);
        _jspx_th_html_005fhidden_005f0.setPageContext(_jspx_page_context);
        _jspx_th_html_005fhidden_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
        _jspx_th_html_005fhidden_005f0.setProperty("metodo");
        _jspx_th_html_005fhidden_005f0.setStyleId("metodo");
        int _jspx_eval_html_005fhidden_005f0 = _jspx_th_html_005fhidden_005f0.doStartTag();
        if (_jspx_th_html_005fhidden_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fhtml_005fhidden_005fstyleId_005fproperty_005fnobody.reuse(_jspx_th_html_005fhidden_005f0);
            return true;
        }
        _005fjspx_005ftagPool_005fhtml_005fhidden_005fstyleId_005fproperty_005fnobody.reuse(_jspx_th_html_005fhidden_005f0);
        return false;
    }

    private boolean _jspx_meth_html_005ftext_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.struts.taglib.html.TextTag _jspx_th_html_005ftext_005f0 = (org.apache.struts.taglib.html.TextTag) _005fjspx_005ftagPool_005fhtml_005ftext_005fstyleId_005fsize_005fproperty_005fnobody.get(org.apache.struts.taglib.html.TextTag.class);
        _jspx_th_html_005ftext_005f0.setPageContext(_jspx_page_context);
        _jspx_th_html_005ftext_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
        _jspx_th_html_005ftext_005f0.setProperty("dataInicio");
        _jspx_th_html_005ftext_005f0.setStyleId("dataInicio");
        _jspx_th_html_005ftext_005f0.setSize("10");
        int _jspx_eval_html_005ftext_005f0 = _jspx_th_html_005ftext_005f0.doStartTag();
        if (_jspx_th_html_005ftext_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fhtml_005ftext_005fstyleId_005fsize_005fproperty_005fnobody.reuse(_jspx_th_html_005ftext_005f0);
            return true;
        }
        _005fjspx_005ftagPool_005fhtml_005ftext_005fstyleId_005fsize_005fproperty_005fnobody.reuse(_jspx_th_html_005ftext_005f0);
        return false;
    }

    private boolean _jspx_meth_html_005ftext_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.struts.taglib.html.TextTag _jspx_th_html_005ftext_005f1 = (org.apache.struts.taglib.html.TextTag) _005fjspx_005ftagPool_005fhtml_005ftext_005fstyleId_005fsize_005fproperty_005fnobody.get(org.apache.struts.taglib.html.TextTag.class);
        _jspx_th_html_005ftext_005f1.setPageContext(_jspx_page_context);
        _jspx_th_html_005ftext_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
        _jspx_th_html_005ftext_005f1.setProperty("dataFim");
        _jspx_th_html_005ftext_005f1.setStyleId("dataFinal");
        _jspx_th_html_005ftext_005f1.setSize("10");
        int _jspx_eval_html_005ftext_005f1 = _jspx_th_html_005ftext_005f1.doStartTag();
        if (_jspx_th_html_005ftext_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fhtml_005ftext_005fstyleId_005fsize_005fproperty_005fnobody.reuse(_jspx_th_html_005ftext_005f1);
            return true;
        }
        _005fjspx_005ftagPool_005fhtml_005ftext_005fstyleId_005fsize_005fproperty_005fnobody.reuse(_jspx_th_html_005ftext_005f1);
        return false;
    }

    private boolean _jspx_meth_html_005ftext_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_html_005fform_005f0, PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.struts.taglib.html.TextTag _jspx_th_html_005ftext_005f2 = (org.apache.struts.taglib.html.TextTag) _005fjspx_005ftagPool_005fhtml_005ftext_005fstyleId_005fsize_005fproperty_005fmaxlength_005fnobody.get(org.apache.struts.taglib.html.TextTag.class);
        _jspx_th_html_005ftext_005f2.setPageContext(_jspx_page_context);
        _jspx_th_html_005ftext_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_005fform_005f0);
        _jspx_th_html_005ftext_005f2.setProperty("codigoProcedimento");
        _jspx_th_html_005ftext_005f2.setStyleId("codigoProcedimento");
        _jspx_th_html_005ftext_005f2.setSize("15");
        _jspx_th_html_005ftext_005f2.setMaxlength("20");
        int _jspx_eval_html_005ftext_005f2 = _jspx_th_html_005ftext_005f2.doStartTag();
        if (_jspx_th_html_005ftext_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fhtml_005ftext_005fstyleId_005fsize_005fproperty_005fmaxlength_005fnobody.reuse(_jspx_th_html_005ftext_005f2);
            return true;
        }
        _005fjspx_005ftagPool_005fhtml_005ftext_005fstyleId_005fsize_005fproperty_005fmaxlength_005fnobody.reuse(_jspx_th_html_005ftext_005f2);
        return false;
    }
}
