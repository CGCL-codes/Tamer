package org.apache.jsp.system.book;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.sql.*;

public final class transforform_jsp extends org.apache.jasper.runtime.HttpJspBase implements org.apache.jasper.runtime.JspSourceDependent {

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

    private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fmytag_005fView_0026_005ftable_005fname_005fid_005fnobody;

    private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fmytag_005fList_0026_005ftable_005fname_005fnobody;

    private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fmytag_005fView_0026_005ftable_005fname_005fid_005fentityStringId_005fnobody;

    private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005ffmt_005fformatDate_0026_005fvalue_005ftype_005fpattern_005fnobody;

    private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid;

    private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;

    private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fmytag_005fsequence_0026_005fkey_005fnobody;

    private javax.el.ExpressionFactory _el_expressionfactory;

    private org.apache.AnnotationProcessor _jsp_annotationprocessor;

    public Object getDependants() {
        return _jspx_dependants;
    }

    public void _jspInit() {
        _005fjspx_005ftagPool_005fmytag_005fView_0026_005ftable_005fname_005fid_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
        _005fjspx_005ftagPool_005fmytag_005fList_0026_005ftable_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
        _005fjspx_005ftagPool_005fmytag_005fView_0026_005ftable_005fname_005fid_005fentityStringId_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
        _005fjspx_005ftagPool_005ffmt_005fformatDate_0026_005fvalue_005ftype_005fpattern_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
        _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
        _005fjspx_005ftagPool_005fmytag_005fsequence_0026_005fkey_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
        _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
    }

    public void _jspDestroy() {
        _005fjspx_005ftagPool_005fmytag_005fView_0026_005ftable_005fname_005fid_005fnobody.release();
        _005fjspx_005ftagPool_005fmytag_005fList_0026_005ftable_005fname_005fnobody.release();
        _005fjspx_005ftagPool_005fmytag_005fView_0026_005ftable_005fname_005fid_005fentityStringId_005fnobody.release();
        _005fjspx_005ftagPool_005ffmt_005fformatDate_0026_005fvalue_005ftype_005fpattern_005fnobody.release();
        _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid.release();
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
        _005fjspx_005ftagPool_005fmytag_005fsequence_0026_005fkey_005fnobody.release();
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
            pageContext = _jspxFactory.getPageContext(this, request, response, "", true, 8192, true);
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
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
            pageContext.setAttribute("basePath", basePath);
            out.write("\r\n");
            out.write("<html>\r\n");
            out.write("<head>\r\n");
            out.write("<base href=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageScope.basePath }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\">\r\n");
            out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n");
            out.write("<meta http-equiv=\"pragma\" content=\"no-cache\">\r\n");
            out.write("<meta http-equiv=\"cache-control\" content=\"no-cache\">\r\n");
            out.write("<meta http-equiv=\"expires\" content=\"0\">\r\n");
            out.write("<title></title>\r\n");
            out.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"system/css/style.css\" />\r\n");
            out.write("<script type=\"text/javascript\" src=\"system/js/public.js\"></script>\r\n");
            out.write("<link href=\"system/plugins/validateMyForm/css/plugin.css\" rel=\"stylesheet\" type=\"text/css\">\r\n");
            out.write(" <script type=\"text/javascript\" src=\"system/js/jquery-1.4.2.js\"></script>\r\n");
            out.write(" <script type=\"text/javascript\" src=\"system/js/json2.js\"></script>\r\n");
            out.write(" <script type=\"text/javascript\" src=\"system/js/verify1.js\"></script>\r\n");
            out.write(" <script type=\"text/javascript\" src=\"system/plugins/validateMyForm/js/jquery.validateMyForm.1.5.js\"></script>\r\n");
            out.write("<script type=\"text/javascript\" src=\"system/js/DatePicker/WdatePicker.js\"></script>\r\n");
            out.write(" <script type=\"text/javascript\">  \r\n");
            out.write("\t$(document).ready(function(){ \r\n");
            out.write("\t   \r\n");
            out.write("\t   var now= new Date();  \r\n");
            out.write("\t   var year=now.getFullYear();  \r\n");
            out.write("\t   var month=now.getMonth()+1;  \r\n");
            out.write("\t   var day=now.getDate();  \r\n");
            out.write("\t   var nowdate=year+\"-\"+month+\"-\"+day;  \r\n");
            out.write("\t   $(\"#djRq\").val(nowdate);\r\n");
            out.write("\t   //onblur=\"verAndGenJosn('system/ajax/transfor.do?method=listByNsrbm',this)\"\r\n");
            out.write("\t   $(\"#nsrbm\").bind(\"blur\",function(){\r\n");
            out.write("\t\t   verAndGenJosn('system/ajax/transfor.do?method=listByNsrbm',this);\r\n");
            out.write("\t   });\r\n");
            out.write("\t //  var test=$(\"#djztDm\").val();\r\n");
            out.write("\t   if($(\"#czlbDm\").val()==\"02\"){\r\n");
            out.write("\t\t   $(\"#sfzhm\").addClass(\"required\");\r\n");
            out.write("\t\t   $(\"#czProperty\").text(\"车主名称：\");\r\n");
            out.write("\t    \t$(\"#grOrdw\").show();\r\n");
            out.write("\t    \t$(\"#grOrdwval\").show();\r\n");
            out.write("\t    \t$(\"#nsrbm\").unbind(\"blur\");\r\n");
            out.write("\t    \t$(\"#form1\").validateMyForm();\r\n");
            out.write("\t    \treturn;\r\n");
            out.write("\t    }\t\r\n");
            out.write("\t  $(\"#sfzhm\").removeClass(\"required\");\r\n");
            out.write("\t  $(\"#form1\").validateMyForm();\r\n");
            out.write("\t  return;\r\n");
            out.write("  \t}); \r\n");
            out.write("\r\n");
            out.write("</script>  \r\n");
            out.write("<body>\r\n");
            out.write("<div class=\"main\">\r\n");
            out.write("\t<div class=\"position\">当前位置: <a href=\"sysadm/desktop.jsp\">桌 面</a> → 车船过户</div>\r\n");
            out.write("\t<div class=\"mainbody\">\r\n");
            out.write("\t\t<div class=\"operate_info\">操作说明：带 * 号必填</div>\r\n");
            out.write("\t\t<div style=\"font-size:20px;font-weight:bold;color: blue\">旧车主信息：</div>\r\n");
            out.write("\t\t<div>\r\n");
            out.write("\t\t\t");
            if (_jspx_meth_mytag_005fView_005f0(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("\t\t\t");
            if (_jspx_meth_mytag_005fList_005f0(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("\t\t\t");
            if (_jspx_meth_mytag_005fList_005f1(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("\t\t\t<table width=\"100%\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\" class=\"table_form\" >\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">车主类别：</td>\t\r\n");
            out.write("\t\t\t\t\t\t");
            if (_jspx_meth_mytag_005fView_005f1(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"czlbDm\" readonly value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDmCcsCzlb.czlbMc}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/><!-- 需要显示名称,车主类别代码对应的名称 -->\r\n");
            out.write("\t\t\t\t\t\t</td>\r\n");
            out.write("\t\t\t\t\t\t<td  align=\"right\">登记状态：</td>\r\n");
            out.write("\t\t\t\t\t\t");
            if (_jspx_meth_mytag_005fView_005f2(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("\t\t\t\t\t\t\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"djztMc\"  readonly value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDmCcsDjzt.djztMc }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/></td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">纳税人编码：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"nsrbm\" readonly value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.nsrbm}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/></td>\r\n");
            out.write("\t\t\t\t\t\t<td  align=\"right\">车船登记号：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"ccdjh\" id=\"ccdjh\" readonly value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.ccdjh}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/></td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">个人名称：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"czMc\" readonly value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.czMc}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/></td>\r\n");
            out.write("\t\t\t\t\t\t<td  align=\"right\">身份证号码：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"sfzhm\" readonly value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.sfzhm}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/></td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">地址：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"czDz\" readonly value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.czDz}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/></td>\r\n");
            out.write("\t\t\t\t\t\t<td  align=\"right\">电话：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"czDh\" readonly value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.czDh}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/></td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">车船牌照号码：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"ccpzh\" id=\"ccpzh\" readonly value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.ccpzh}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/></td>\r\n");
            out.write("\t\t\t\t\t\t<td  align=\"right\">车船类型：</td>\r\n");
            out.write("\t\t\t\t\t\t");
            if (_jspx_meth_mytag_005fView_005f3(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"jjcclxDm\" id=\"jjcclxDm\" readonly value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDmCcsJjcclx.jjcclxMc}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/></td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">发动机号：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"fdjh\" id=\"fdjh\" readonly value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.fdjh}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/></td>\r\n");
            out.write("\t\t\t\t\t\t<td  align=\"right\">车架号：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"cjh\" id=\"cjh\" readonly value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.cjh}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/></td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">厂牌型号：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"cpxh\" id=\"cpxh\" readonly value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.cpxh}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/></td>\r\n");
            out.write("\t\t\t\t\t\t<td  align=\"right\">购置时间：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"gzRq\" id=\"gzRq\" readonly value=\"");
            if (_jspx_meth_fmt_005fformatDate_005f0(_jspx_page_context)) return;
            out.write("\"/></td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">核定载重量(吨)：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"hdzzl\" id=\"hdzzl\" readonly value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.hdzzl}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/></td>\r\n");
            out.write("\t\t\t\t\t\t<td  align=\"right\">核定载客量(人)：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"hdzkl\" id=\"hdzkl\" readonly value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.hdzkl}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/></td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">整备质量(吨)：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"zbzl\" id=\"zbzl\" readonly value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.zbzl}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/></td>\r\n");
            out.write("\t\t\t\t\t\t<td  align=\"right\">排气量：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"pql\" id=\"pql\" readonly value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.pql}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/></td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">登记日期：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"djRq\"  readonly value=\"");
            if (_jspx_meth_fmt_005fformatDate_005f1(_jspx_page_context)) return;
            out.write("\"/></td>\r\n");
            out.write("\t\t\t\t\t\t<td  align=\"right\">行驶证发证日期：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"fzRq\" id=\"fzRq\" readonly value=\"");
            if (_jspx_meth_fmt_005fformatDate_005f2(_jspx_page_context)) return;
            out.write("\"/></td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">免税：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"msBj\" id=\"msBj\" readonly value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.msBj}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/></td>\r\n");
            out.write("\t\t\t\t\t\t<td  align=\"right\">申报起始年月：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"sbqsny\"  readonly value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.sbqsny}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/></td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\" >\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">备注：</td>\r\n");
            out.write("\t\t\t\t\t\t<td colspan=\"3\">\r\n");
            out.write("\t\t\t\t\t\t\t<textarea name=\"bz\" id=\"bz\" cols=\"50\" rows=\"3\" readonly >");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.bz}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("</textarea>\r\n");
            out.write("\t\t\t\t\t\t</td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t</table>\r\n");
            out.write("\t\t</div>\r\n");
            out.write("\t\t<div style=\"font-size:20px;font-weight:bold;color: blue\">新车主信息：</div>\r\n");
            out.write("\t\t<div class=\"table\">\t\t\t\r\n");
            out.write("\t\t\t<form id=\"form1\" action=\"system/book/transfor_save.do\" method=\"post\">\r\n");
            out.write("\t\t\t\t<input type=\"hidden\" name=\"tDjCcdjxxLsccdjh\" value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.ccdjh}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\">\r\n");
            out.write("\t\t\t\t<table width=\"100%\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\" class=\"table_form\" >\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">车主类别：</td>\r\n");
            out.write("\t\t\t\t\t\t<td>\r\n");
            out.write("\t\t\t\t\t\t\t<select id=\"czlbDm\" name=\"czlbDm\" onchange=\"czlbChange()\">\r\n");
            out.write("\t\t\t\t\t\t\t\t");
            org.apache.struts.taglib.logic.IterateTag _jspx_th_logic_005fiterate_005f0 = (org.apache.struts.taglib.logic.IterateTag) _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid.get(org.apache.struts.taglib.logic.IterateTag.class);
            _jspx_th_logic_005fiterate_005f0.setPageContext(_jspx_page_context);
            _jspx_th_logic_005fiterate_005f0.setParent(null);
            _jspx_th_logic_005fiterate_005f0.setId("tDmCcsCzlb");
            _jspx_th_logic_005fiterate_005f0.setName("tDmCcsCzlbList");
            int _jspx_eval_logic_005fiterate_005f0 = _jspx_th_logic_005fiterate_005f0.doStartTag();
            if (_jspx_eval_logic_005fiterate_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                java.lang.Object tDmCcsCzlb = null;
                if (_jspx_eval_logic_005fiterate_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                    out = _jspx_page_context.pushBody();
                    _jspx_th_logic_005fiterate_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                    _jspx_th_logic_005fiterate_005f0.doInitBody();
                }
                tDmCcsCzlb = (java.lang.Object) _jspx_page_context.findAttribute("tDmCcsCzlb");
                do {
                    out.write("\r\n");
                    out.write("\t\t\t\t\t\t\t\t");
                    if (_jspx_meth_c_005fif_005f0(_jspx_th_logic_005fiterate_005f0, _jspx_page_context)) return;
                    out.write("\r\n");
                    out.write("\t\t\t\t\t\t\t\t");
                    int evalDoAfterBody = _jspx_th_logic_005fiterate_005f0.doAfterBody();
                    tDmCcsCzlb = (java.lang.Object) _jspx_page_context.findAttribute("tDmCcsCzlb");
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
            out.write("\t\t\t\t\t\t\t</select>\r\n");
            out.write("\t\t\t\t\t\t\t\r\n");
            out.write("\t\t\t\t\t\t\t\r\n");
            out.write("\t\t\t\t\t\t\t\r\n");
            out.write("\t\t\t\t\t\t\t&nbsp;<span style=\"color:red\">*</span>\r\n");
            out.write("\t\t\t\t\t\t</td>\r\n");
            out.write("\t\t\t\t\t\t<td  align=\"right\">登记状态：</td>\r\n");
            out.write("\t\t\t\t\t\t<input type=\"hidden\" id=\"djztDm\" name=\"djztDm\" value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDmCcsDjzt.djztDm}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"djztMc\" id=\"djztMc\" readonly=\"readonly\" value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDmCcsDjzt.djztMc }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"></td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">纳税人编码：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"nsrbm\" id=\"nsrbm\" class=\"required\" />&nbsp;<span style=\"color:red\">*</span></td>\r\n");
            out.write("\t\t\t\t\t\t<td  align=\"right\">车船登记号：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"ccdjh\" id=\"ccdjh\" readonly value=\"");
            if (_jspx_meth_mytag_005fsequence_005f0(_jspx_page_context)) return;
            out.write("\"/></td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\"><span id=\"czProperty\">公司名称：</span></td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"czMc\" id=\"czMc\" class=\"required\"/>&nbsp;<span style=\"color:red\">*</span></td>\r\n");
            out.write("\t\t\t\t\t\t<td  align=\"right\"><span id=\"grOrdw\" style=\"display: none\">身份证号码：</span></td>\r\n");
            out.write("\t\t\t\t\t\t<td><span id=\"grOrdwval\" style=\"display: none\"><input name=\"sfzhm\" id=\"sfzhm\" onblur=\"checkID(this)\"/>&nbsp;<span style=\"color:red\">*</span></span></td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">地址：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"czDz\" id=\"czDz\" class=\"required\" />&nbsp;<span style=\"color:red\">*</span></td>\r\n");
            out.write("\t\t\t\t\t\t<td  align=\"right\">电话：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"czDh\" id=\"czDh\" class=\"required numeric\" onblur=\"checkPM(this)\"/>&nbsp;<span style=\"color:red\">*</span></td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">车船牌照号码：</td>\r\n");
            out.write("\t\t\t\t\t\t<td>粤T.<input name=\"ccpzh\" id=\"ccpzh\" class=\"required\" onblur=\"uptocase(this);verify('system/ajax/transfor.do?method=findccpzh',this)\"/>&nbsp;<span style=\"color:red\">*</span></td>\r\n");
            out.write("\t\t\t\t\t\t<td  align=\"right\">车船类型：</td>\t\t\t\t\t\r\n");
            out.write("\t\t\t\t\t\t<td>\r\n");
            out.write("\t\t\t\t\t\t\t<select id=\"jjcclxDm\" name=\"jjcclxDm\" class=\"required\">\t\t\t\t\t\t\t\r\n");
            out.write("\t\t\t\t\t\t\t\t<option value=\"\" >--请选择--</option>\r\n");
            out.write("\t\t\t\t\t\t\t\t");
            org.apache.struts.taglib.logic.IterateTag _jspx_th_logic_005fiterate_005f1 = (org.apache.struts.taglib.logic.IterateTag) _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid.get(org.apache.struts.taglib.logic.IterateTag.class);
            _jspx_th_logic_005fiterate_005f1.setPageContext(_jspx_page_context);
            _jspx_th_logic_005fiterate_005f1.setParent(null);
            _jspx_th_logic_005fiterate_005f1.setId("tTDmCcsJjcclx");
            _jspx_th_logic_005fiterate_005f1.setName("tTDmCcsJjcclxList");
            int _jspx_eval_logic_005fiterate_005f1 = _jspx_th_logic_005fiterate_005f1.doStartTag();
            if (_jspx_eval_logic_005fiterate_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                java.lang.Object tTDmCcsJjcclx = null;
                if (_jspx_eval_logic_005fiterate_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                    out = _jspx_page_context.pushBody();
                    _jspx_th_logic_005fiterate_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                    _jspx_th_logic_005fiterate_005f1.doInitBody();
                }
                tTDmCcsJjcclx = (java.lang.Object) _jspx_page_context.findAttribute("tTDmCcsJjcclx");
                do {
                    out.write("\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t");
                    if (_jspx_meth_c_005fif_005f3(_jspx_th_logic_005fiterate_005f1, _jspx_page_context)) return;
                    out.write("\r\n");
                    out.write("\t\t\t\t\t\t\t\t");
                    int evalDoAfterBody = _jspx_th_logic_005fiterate_005f1.doAfterBody();
                    tTDmCcsJjcclx = (java.lang.Object) _jspx_page_context.findAttribute("tTDmCcsJjcclx");
                    if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
                } while (true);
                if (_jspx_eval_logic_005fiterate_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                    out = _jspx_page_context.popBody();
                }
            }
            if (_jspx_th_logic_005fiterate_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid.reuse(_jspx_th_logic_005fiterate_005f1);
                return;
            }
            _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid.reuse(_jspx_th_logic_005fiterate_005f1);
            out.write("\r\n");
            out.write("\t\t\t\t\t\t\t</select>\r\n");
            out.write("\t\t\t\t\t\t\t&nbsp;<span style=\"color:red\">*</span>\r\n");
            out.write("\t\t\t\t\t\t</td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">发动机号：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"fdjh\" id=\"fdjh\" value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.fdjh}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\" class=\"required\"/>&nbsp;<span style=\"color:red\">*</span></td>\r\n");
            out.write("\t\t\t\t\t\t<td  align=\"right\">车架号：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"cjh\" id=\"cjh\" value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.cjh}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\" class=\"required\"/>&nbsp;<span style=\"color:red\">*</span></td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">厂牌型号：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"cpxh\" id=\"cpxh\" class=\"required\" value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.cpxh}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/>&nbsp;<span style=\"color:red\">*</span></td>\r\n");
            out.write("\t\t\t\t\t\t<td  align=\"right\">购置时间：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"gzRq\" id=\"gzRq\" onFocus=\"new WdatePicker(this,'%Y-%M-%D',true,'default')\" value=\"");
            if (_jspx_meth_fmt_005fformatDate_005f3(_jspx_page_context)) return;
            out.write("\" readOnly class=\"required date\"/>&nbsp;<span style=\"color:red\">*</span></td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">核定载重量(吨)：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"hdzzl\" id=\"hdzzl\" class=\"required\" value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.hdzzl}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/>&nbsp;<span style=\"color:red\">*</span></td>\r\n");
            out.write("\t\t\t\t\t\t<td  align=\"right\">核定载客量(人)：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"hdzkl\" id=\"hdzkl\" value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.hdzkl}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\" class=\"required\"/>&nbsp;<span style=\"color:red\">*</span></td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">整备质量(吨)：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"zbzl\" id=\"zbzl\" class=\"required\" value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.zbzl}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/>&nbsp;<span style=\"color:red\">*</span></td>\r\n");
            out.write("\t\t\t\t\t\t<td  align=\"right\">排气量：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"pql\" id=\"pql\" value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.pql}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\" class=\"required\"/>&nbsp;<span style=\"color:red\">*</span></td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">免税：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"msBj\" id=\"msBj\" readonly value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.msBj}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/></td>\r\n");
            out.write("\t\t\t\t\t\t<input type=\"hidden\" id=\"sbqsnyStr\" value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.sbqsny}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/>\r\n");
            out.write("\t\t\t\t\t\t<td  align=\"right\">申报起始年月：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"sbqsny\" id=\"sbqsny\" readonly value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.sbqsny}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\"/></td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">过户时间：</td>\r\n");
            out.write("\t\t\t\t\t\t<td colspan=\"3\"><input name=\"ghRq\" id=\"ghRq\" onFocus=\"new WdatePicker(this,'%Y-%M-%D',true,'default')\" onblur=\"verTransfDateValuble()\" readonly class=\"required date\"/>&nbsp;<span style=\"color:red\">*</span></td>\r\n");
            out.write("\t\t\t\t\t\t\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">登记日期：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"djRq\" id=\"djRq\"  readonly class=\"date\"/>&nbsp;<span style=\"color:red\">*</span></td>\r\n");
            out.write("\t\t\t\t\t\t<td  align=\"right\">行驶证发证日期：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"fzRq\" id=\"fzRq\" onFocus=\"new WdatePicker(this,'%Y-%M-%D',true,'default')\" readonly class=\"required date\"/>&nbsp;<span style=\"color:red\">*</span></td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\" >\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">备注：</td>\r\n");
            out.write("\t\t\t\t\t\t<td colspan=\"3\">\r\n");
            out.write("\t\t\t\t\t\t\t<textarea name=\"bz\" id=\"bz\" cols=\"50\" rows=\"3\" ></textarea>\r\n");
            out.write("\t\t\t\t\t\t</td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr>\r\n");
            out.write("\t\t\t\t\t\t<td colspan=\"4\" class=\"form_button\" style=\"padding-top:10px;\">\r\n");
            out.write("\t\t\t\t\t\t\t");
            if (_jspx_meth_c_005fif_005f6(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("\t\t\t\t\t\t\t<input type=\"button\" value=\"返回\" onclick=\"javascript:location.href='system/search/unisearch.jsp?module=book/transforform.jsp'\"/>\r\n");
            out.write("\t\t\t\t\t\t</td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t</table>\r\n");
            out.write("\t\t\t</form>\r\n");
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

    private boolean _jspx_meth_mytag_005fView_005f0(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        com.zhongkai.web.tag.ViewTag _jspx_th_mytag_005fView_005f0 = (com.zhongkai.web.tag.ViewTag) _005fjspx_005ftagPool_005fmytag_005fView_0026_005ftable_005fname_005fid_005fnobody.get(com.zhongkai.web.tag.ViewTag.class);
        _jspx_th_mytag_005fView_005f0.setPageContext(_jspx_page_context);
        _jspx_th_mytag_005fView_005f0.setParent(null);
        _jspx_th_mytag_005fView_005f0.setTable("com.zhongkai.model.book.TDjCcdjxx");
        _jspx_th_mytag_005fView_005f0.setId((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${param.ccdjh}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
        _jspx_th_mytag_005fView_005f0.setName("tDjCcdjxx");
        int _jspx_eval_mytag_005fView_005f0 = _jspx_th_mytag_005fView_005f0.doStartTag();
        if (_jspx_th_mytag_005fView_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fmytag_005fView_0026_005ftable_005fname_005fid_005fnobody.reuse(_jspx_th_mytag_005fView_005f0);
            return true;
        }
        _005fjspx_005ftagPool_005fmytag_005fView_0026_005ftable_005fname_005fid_005fnobody.reuse(_jspx_th_mytag_005fView_005f0);
        return false;
    }

    private boolean _jspx_meth_mytag_005fList_005f0(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        com.zhongkai.web.tag.ListTag _jspx_th_mytag_005fList_005f0 = (com.zhongkai.web.tag.ListTag) _005fjspx_005ftagPool_005fmytag_005fList_0026_005ftable_005fname_005fnobody.get(com.zhongkai.web.tag.ListTag.class);
        _jspx_th_mytag_005fList_005f0.setPageContext(_jspx_page_context);
        _jspx_th_mytag_005fList_005f0.setParent(null);
        _jspx_th_mytag_005fList_005f0.setTable("TDmCcsJjcclx");
        _jspx_th_mytag_005fList_005f0.setName("tTDmCcsJjcclxList");
        int _jspx_eval_mytag_005fList_005f0 = _jspx_th_mytag_005fList_005f0.doStartTag();
        if (_jspx_th_mytag_005fList_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fmytag_005fList_0026_005ftable_005fname_005fnobody.reuse(_jspx_th_mytag_005fList_005f0);
            return true;
        }
        _005fjspx_005ftagPool_005fmytag_005fList_0026_005ftable_005fname_005fnobody.reuse(_jspx_th_mytag_005fList_005f0);
        return false;
    }

    private boolean _jspx_meth_mytag_005fList_005f1(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        com.zhongkai.web.tag.ListTag _jspx_th_mytag_005fList_005f1 = (com.zhongkai.web.tag.ListTag) _005fjspx_005ftagPool_005fmytag_005fList_0026_005ftable_005fname_005fnobody.get(com.zhongkai.web.tag.ListTag.class);
        _jspx_th_mytag_005fList_005f1.setPageContext(_jspx_page_context);
        _jspx_th_mytag_005fList_005f1.setParent(null);
        _jspx_th_mytag_005fList_005f1.setTable("TDmCcsCzlb");
        _jspx_th_mytag_005fList_005f1.setName("tDmCcsCzlbList");
        int _jspx_eval_mytag_005fList_005f1 = _jspx_th_mytag_005fList_005f1.doStartTag();
        if (_jspx_th_mytag_005fList_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fmytag_005fList_0026_005ftable_005fname_005fnobody.reuse(_jspx_th_mytag_005fList_005f1);
            return true;
        }
        _005fjspx_005ftagPool_005fmytag_005fList_0026_005ftable_005fname_005fnobody.reuse(_jspx_th_mytag_005fList_005f1);
        return false;
    }

    private boolean _jspx_meth_mytag_005fView_005f1(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        com.zhongkai.web.tag.ViewTag _jspx_th_mytag_005fView_005f1 = (com.zhongkai.web.tag.ViewTag) _005fjspx_005ftagPool_005fmytag_005fView_0026_005ftable_005fname_005fid_005fentityStringId_005fnobody.get(com.zhongkai.web.tag.ViewTag.class);
        _jspx_th_mytag_005fView_005f1.setPageContext(_jspx_page_context);
        _jspx_th_mytag_005fView_005f1.setParent(null);
        _jspx_th_mytag_005fView_005f1.setTable("com.zhongkai.model.book.TDmCcsCzlb");
        _jspx_th_mytag_005fView_005f1.setId((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.czlbDm}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
        _jspx_th_mytag_005fView_005f1.setName("tDmCcsCzlb");
        _jspx_th_mytag_005fView_005f1.setEntityStringId("true");
        int _jspx_eval_mytag_005fView_005f1 = _jspx_th_mytag_005fView_005f1.doStartTag();
        if (_jspx_th_mytag_005fView_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fmytag_005fView_0026_005ftable_005fname_005fid_005fentityStringId_005fnobody.reuse(_jspx_th_mytag_005fView_005f1);
            return true;
        }
        _005fjspx_005ftagPool_005fmytag_005fView_0026_005ftable_005fname_005fid_005fentityStringId_005fnobody.reuse(_jspx_th_mytag_005fView_005f1);
        return false;
    }

    private boolean _jspx_meth_mytag_005fView_005f2(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        com.zhongkai.web.tag.ViewTag _jspx_th_mytag_005fView_005f2 = (com.zhongkai.web.tag.ViewTag) _005fjspx_005ftagPool_005fmytag_005fView_0026_005ftable_005fname_005fid_005fentityStringId_005fnobody.get(com.zhongkai.web.tag.ViewTag.class);
        _jspx_th_mytag_005fView_005f2.setPageContext(_jspx_page_context);
        _jspx_th_mytag_005fView_005f2.setParent(null);
        _jspx_th_mytag_005fView_005f2.setTable("com.zhongkai.model.book.TDmCcsDjzt");
        _jspx_th_mytag_005fView_005f2.setId((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.djztDm}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
        _jspx_th_mytag_005fView_005f2.setName("tDmCcsDjzt");
        _jspx_th_mytag_005fView_005f2.setEntityStringId("true");
        int _jspx_eval_mytag_005fView_005f2 = _jspx_th_mytag_005fView_005f2.doStartTag();
        if (_jspx_th_mytag_005fView_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fmytag_005fView_0026_005ftable_005fname_005fid_005fentityStringId_005fnobody.reuse(_jspx_th_mytag_005fView_005f2);
            return true;
        }
        _005fjspx_005ftagPool_005fmytag_005fView_0026_005ftable_005fname_005fid_005fentityStringId_005fnobody.reuse(_jspx_th_mytag_005fView_005f2);
        return false;
    }

    private boolean _jspx_meth_mytag_005fView_005f3(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        com.zhongkai.web.tag.ViewTag _jspx_th_mytag_005fView_005f3 = (com.zhongkai.web.tag.ViewTag) _005fjspx_005ftagPool_005fmytag_005fView_0026_005ftable_005fname_005fid_005fentityStringId_005fnobody.get(com.zhongkai.web.tag.ViewTag.class);
        _jspx_th_mytag_005fView_005f3.setPageContext(_jspx_page_context);
        _jspx_th_mytag_005fView_005f3.setParent(null);
        _jspx_th_mytag_005fView_005f3.setTable("com.zhongkai.model.book.TDmCcsJjcclx");
        _jspx_th_mytag_005fView_005f3.setId((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.jjcclxDm}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
        _jspx_th_mytag_005fView_005f3.setName("tDmCcsJjcclx");
        _jspx_th_mytag_005fView_005f3.setEntityStringId("true");
        int _jspx_eval_mytag_005fView_005f3 = _jspx_th_mytag_005fView_005f3.doStartTag();
        if (_jspx_th_mytag_005fView_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fmytag_005fView_0026_005ftable_005fname_005fid_005fentityStringId_005fnobody.reuse(_jspx_th_mytag_005fView_005f3);
            return true;
        }
        _005fjspx_005ftagPool_005fmytag_005fView_0026_005ftable_005fname_005fid_005fentityStringId_005fnobody.reuse(_jspx_th_mytag_005fView_005f3);
        return false;
    }

    private boolean _jspx_meth_fmt_005fformatDate_005f0(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.taglibs.standard.tag.rt.fmt.FormatDateTag _jspx_th_fmt_005fformatDate_005f0 = (org.apache.taglibs.standard.tag.rt.fmt.FormatDateTag) _005fjspx_005ftagPool_005ffmt_005fformatDate_0026_005fvalue_005ftype_005fpattern_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.FormatDateTag.class);
        _jspx_th_fmt_005fformatDate_005f0.setPageContext(_jspx_page_context);
        _jspx_th_fmt_005fformatDate_005f0.setParent(null);
        _jspx_th_fmt_005fformatDate_005f0.setType("date");
        _jspx_th_fmt_005fformatDate_005f0.setPattern("yyyy-MM-dd");
        _jspx_th_fmt_005fformatDate_005f0.setValue((java.util.Date) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.gzRq}", java.util.Date.class, (PageContext) _jspx_page_context, null, false));
        int _jspx_eval_fmt_005fformatDate_005f0 = _jspx_th_fmt_005fformatDate_005f0.doStartTag();
        if (_jspx_th_fmt_005fformatDate_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005ffmt_005fformatDate_0026_005fvalue_005ftype_005fpattern_005fnobody.reuse(_jspx_th_fmt_005fformatDate_005f0);
            return true;
        }
        _005fjspx_005ftagPool_005ffmt_005fformatDate_0026_005fvalue_005ftype_005fpattern_005fnobody.reuse(_jspx_th_fmt_005fformatDate_005f0);
        return false;
    }

    private boolean _jspx_meth_fmt_005fformatDate_005f1(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.taglibs.standard.tag.rt.fmt.FormatDateTag _jspx_th_fmt_005fformatDate_005f1 = (org.apache.taglibs.standard.tag.rt.fmt.FormatDateTag) _005fjspx_005ftagPool_005ffmt_005fformatDate_0026_005fvalue_005ftype_005fpattern_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.FormatDateTag.class);
        _jspx_th_fmt_005fformatDate_005f1.setPageContext(_jspx_page_context);
        _jspx_th_fmt_005fformatDate_005f1.setParent(null);
        _jspx_th_fmt_005fformatDate_005f1.setType("date");
        _jspx_th_fmt_005fformatDate_005f1.setPattern("yyyy-MM-dd");
        _jspx_th_fmt_005fformatDate_005f1.setValue((java.util.Date) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.djRq}", java.util.Date.class, (PageContext) _jspx_page_context, null, false));
        int _jspx_eval_fmt_005fformatDate_005f1 = _jspx_th_fmt_005fformatDate_005f1.doStartTag();
        if (_jspx_th_fmt_005fformatDate_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005ffmt_005fformatDate_0026_005fvalue_005ftype_005fpattern_005fnobody.reuse(_jspx_th_fmt_005fformatDate_005f1);
            return true;
        }
        _005fjspx_005ftagPool_005ffmt_005fformatDate_0026_005fvalue_005ftype_005fpattern_005fnobody.reuse(_jspx_th_fmt_005fformatDate_005f1);
        return false;
    }

    private boolean _jspx_meth_fmt_005fformatDate_005f2(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.taglibs.standard.tag.rt.fmt.FormatDateTag _jspx_th_fmt_005fformatDate_005f2 = (org.apache.taglibs.standard.tag.rt.fmt.FormatDateTag) _005fjspx_005ftagPool_005ffmt_005fformatDate_0026_005fvalue_005ftype_005fpattern_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.FormatDateTag.class);
        _jspx_th_fmt_005fformatDate_005f2.setPageContext(_jspx_page_context);
        _jspx_th_fmt_005fformatDate_005f2.setParent(null);
        _jspx_th_fmt_005fformatDate_005f2.setType("date");
        _jspx_th_fmt_005fformatDate_005f2.setPattern("yyyy-MM-dd");
        _jspx_th_fmt_005fformatDate_005f2.setValue((java.util.Date) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.fzRq}", java.util.Date.class, (PageContext) _jspx_page_context, null, false));
        int _jspx_eval_fmt_005fformatDate_005f2 = _jspx_th_fmt_005fformatDate_005f2.doStartTag();
        if (_jspx_th_fmt_005fformatDate_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005ffmt_005fformatDate_0026_005fvalue_005ftype_005fpattern_005fnobody.reuse(_jspx_th_fmt_005fformatDate_005f2);
            return true;
        }
        _005fjspx_005ftagPool_005ffmt_005fformatDate_0026_005fvalue_005ftype_005fpattern_005fnobody.reuse(_jspx_th_fmt_005fformatDate_005f2);
        return false;
    }

    private boolean _jspx_meth_c_005fif_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_005fiterate_005f0, PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
        _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
        _jspx_th_c_005fif_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_005fiterate_005f0);
        _jspx_th_c_005fif_005f0.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDmCcsCzlb!=null&&tDmCcsCzlb.czlbDm!=null }", java.lang.Boolean.class, (PageContext) _jspx_page_context, null, false)).booleanValue());
        int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
        if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
                out.write("\r\n");
                out.write("\t\t\t\t\t\t\t\t\t");
                if (_jspx_meth_c_005fif_005f1(_jspx_th_c_005fif_005f0, _jspx_page_context)) return true;
                out.write("\r\n");
                out.write("\t\t\t\t\t\t\t\t\t");
                if (_jspx_meth_c_005fif_005f2(_jspx_th_c_005fif_005f0, _jspx_page_context)) return true;
                out.write("\r\n");
                out.write("\t\t\t\t\t\t\t\t");
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

    private boolean _jspx_meth_c_005fif_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f0, PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f1 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
        _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
        _jspx_th_c_005fif_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
        _jspx_th_c_005fif_005f1.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDmCcsCzlb.czlbDm==tDjCcdjxx.czlbDm}", java.lang.Boolean.class, (PageContext) _jspx_page_context, null, false)).booleanValue());
        int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
        if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
                out.write("\r\n");
                out.write("\t\t\t\t\t\t\t\t\t\t<option value=\"");
                out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDmCcsCzlb.czlbDm}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                out.write("\" selected>");
                out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDmCcsCzlb.czlbMc }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                out.write("</option>\r\n");
                out.write("\t\t\t\t\t\t\t\t\t");
                int evalDoAfterBody = _jspx_th_c_005fif_005f1.doAfterBody();
                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
            } while (true);
        }
        if (_jspx_th_c_005fif_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
            return true;
        }
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
        return false;
    }

    private boolean _jspx_meth_c_005fif_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f0, PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f2 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
        _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
        _jspx_th_c_005fif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
        _jspx_th_c_005fif_005f2.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDmCcsCzlb.czlbDm!=tDjCcdjxx.czlbDm}", java.lang.Boolean.class, (PageContext) _jspx_page_context, null, false)).booleanValue());
        int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
        if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
                out.write("\r\n");
                out.write("\t\t\t\t\t\t\t\t\t\t<option value=\"");
                out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDmCcsCzlb.czlbDm}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                out.write('"');
                out.write('>');
                out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDmCcsCzlb.czlbMc }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                out.write("</option>\r\n");
                out.write("\t\t\t\t\t\t\t\t\t");
                int evalDoAfterBody = _jspx_th_c_005fif_005f2.doAfterBody();
                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
            } while (true);
        }
        if (_jspx_th_c_005fif_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);
            return true;
        }
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);
        return false;
    }

    private boolean _jspx_meth_mytag_005fsequence_005f0(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        com.zhongkai.web.tag.SequenceTag _jspx_th_mytag_005fsequence_005f0 = (com.zhongkai.web.tag.SequenceTag) _005fjspx_005ftagPool_005fmytag_005fsequence_0026_005fkey_005fnobody.get(com.zhongkai.web.tag.SequenceTag.class);
        _jspx_th_mytag_005fsequence_005f0.setPageContext(_jspx_page_context);
        _jspx_th_mytag_005fsequence_005f0.setParent(null);
        _jspx_th_mytag_005fsequence_005f0.setKey("ccdjxx_seq");
        int _jspx_eval_mytag_005fsequence_005f0 = _jspx_th_mytag_005fsequence_005f0.doStartTag();
        if (_jspx_th_mytag_005fsequence_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fmytag_005fsequence_0026_005fkey_005fnobody.reuse(_jspx_th_mytag_005fsequence_005f0);
            return true;
        }
        _005fjspx_005ftagPool_005fmytag_005fsequence_0026_005fkey_005fnobody.reuse(_jspx_th_mytag_005fsequence_005f0);
        return false;
    }

    private boolean _jspx_meth_c_005fif_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_005fiterate_005f1, PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f3 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
        _jspx_th_c_005fif_005f3.setPageContext(_jspx_page_context);
        _jspx_th_c_005fif_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_005fiterate_005f1);
        _jspx_th_c_005fif_005f3.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tTDmCcsJjcclx!=null&&tTDmCcsJjcclx.jjcclxDm!=null }", java.lang.Boolean.class, (PageContext) _jspx_page_context, null, false)).booleanValue());
        int _jspx_eval_c_005fif_005f3 = _jspx_th_c_005fif_005f3.doStartTag();
        if (_jspx_eval_c_005fif_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
                out.write("\r\n");
                out.write("\t\t\t\t\t\t\t\t\t\t");
                if (_jspx_meth_c_005fif_005f4(_jspx_th_c_005fif_005f3, _jspx_page_context)) return true;
                out.write("\r\n");
                out.write("\t\t\t\t\t\t\t\t\t\t");
                if (_jspx_meth_c_005fif_005f5(_jspx_th_c_005fif_005f3, _jspx_page_context)) return true;
                out.write("\r\n");
                out.write("\t\t\t\t\t\t\t\t\t");
                int evalDoAfterBody = _jspx_th_c_005fif_005f3.doAfterBody();
                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
            } while (true);
        }
        if (_jspx_th_c_005fif_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f3);
            return true;
        }
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f3);
        return false;
    }

    private boolean _jspx_meth_c_005fif_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f3, PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f4 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
        _jspx_th_c_005fif_005f4.setPageContext(_jspx_page_context);
        _jspx_th_c_005fif_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
        _jspx_th_c_005fif_005f4.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tTDmCcsJjcclx.jjcclxDm==tDjCcdjxx.jjcclxDm}", java.lang.Boolean.class, (PageContext) _jspx_page_context, null, false)).booleanValue());
        int _jspx_eval_c_005fif_005f4 = _jspx_th_c_005fif_005f4.doStartTag();
        if (_jspx_eval_c_005fif_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
                out.write("\r\n");
                out.write("\t\t\t\t\t\t\t\t\t\t\t<option value=\"");
                out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tTDmCcsJjcclx.jjcclxDm}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                out.write("\" selected>");
                out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tTDmCcsJjcclx.jjcclxMc}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                out.write("</option>\r\n");
                out.write("\t\t\t\t\t\t\t\t\t\t");
                int evalDoAfterBody = _jspx_th_c_005fif_005f4.doAfterBody();
                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
            } while (true);
        }
        if (_jspx_th_c_005fif_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f4);
            return true;
        }
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f4);
        return false;
    }

    private boolean _jspx_meth_c_005fif_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_c_005fif_005f3, PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f5 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
        _jspx_th_c_005fif_005f5.setPageContext(_jspx_page_context);
        _jspx_th_c_005fif_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
        _jspx_th_c_005fif_005f5.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tTDmCcsJjcclx.jjcclxDm!=tDjCcdjxx.jjcclxDm}", java.lang.Boolean.class, (PageContext) _jspx_page_context, null, false)).booleanValue());
        int _jspx_eval_c_005fif_005f5 = _jspx_th_c_005fif_005f5.doStartTag();
        if (_jspx_eval_c_005fif_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
                out.write("\r\n");
                out.write("\t\t\t\t\t\t\t\t\t\t\t<option value=\"");
                out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tTDmCcsJjcclx.jjcclxDm}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                out.write('"');
                out.write('>');
                out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tTDmCcsJjcclx.jjcclxMc}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                out.write("</option>\r\n");
                out.write("\t\t\t\t\t\t\t\t\t\t");
                int evalDoAfterBody = _jspx_th_c_005fif_005f5.doAfterBody();
                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
            } while (true);
        }
        if (_jspx_th_c_005fif_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f5);
            return true;
        }
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f5);
        return false;
    }

    private boolean _jspx_meth_fmt_005fformatDate_005f3(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.taglibs.standard.tag.rt.fmt.FormatDateTag _jspx_th_fmt_005fformatDate_005f3 = (org.apache.taglibs.standard.tag.rt.fmt.FormatDateTag) _005fjspx_005ftagPool_005ffmt_005fformatDate_0026_005fvalue_005ftype_005fpattern_005fnobody.get(org.apache.taglibs.standard.tag.rt.fmt.FormatDateTag.class);
        _jspx_th_fmt_005fformatDate_005f3.setPageContext(_jspx_page_context);
        _jspx_th_fmt_005fformatDate_005f3.setParent(null);
        _jspx_th_fmt_005fformatDate_005f3.setType("date");
        _jspx_th_fmt_005fformatDate_005f3.setPattern("yyyy-MM-dd");
        _jspx_th_fmt_005fformatDate_005f3.setValue((java.util.Date) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDjCcdjxx.gzRq}", java.util.Date.class, (PageContext) _jspx_page_context, null, false));
        int _jspx_eval_fmt_005fformatDate_005f3 = _jspx_th_fmt_005fformatDate_005f3.doStartTag();
        if (_jspx_th_fmt_005fformatDate_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005ffmt_005fformatDate_0026_005fvalue_005ftype_005fpattern_005fnobody.reuse(_jspx_th_fmt_005fformatDate_005f3);
            return true;
        }
        _005fjspx_005ftagPool_005ffmt_005fformatDate_0026_005fvalue_005ftype_005fpattern_005fnobody.reuse(_jspx_th_fmt_005fformatDate_005f3);
        return false;
    }

    private boolean _jspx_meth_c_005fif_005f6(PageContext _jspx_page_context) throws Throwable {
        PageContext pageContext = _jspx_page_context;
        JspWriter out = _jspx_page_context.getOut();
        org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f6 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
        _jspx_th_c_005fif_005f6.setPageContext(_jspx_page_context);
        _jspx_th_c_005fif_005f6.setParent(null);
        _jspx_th_c_005fif_005f6.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${tDmCcsDjzt.djztDm=='01'}", java.lang.Boolean.class, (PageContext) _jspx_page_context, null, false)).booleanValue());
        int _jspx_eval_c_005fif_005f6 = _jspx_th_c_005fif_005f6.doStartTag();
        if (_jspx_eval_c_005fif_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
                out.write("\r\n");
                out.write("\t\t\t\t\t\t\t\t<input  id=\"save\" type=\"submit\" value=\"保存\" />\r\n");
                out.write("\t\t\t\t\t\t\t\t<input id=\"saveAndSb\" type=\"button\" value=\"保存并申报\" />\r\n");
                out.write("\t\t\t\t\t\t\t");
                int evalDoAfterBody = _jspx_th_c_005fif_005f6.doAfterBody();
                if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
            } while (true);
        }
        if (_jspx_th_c_005fif_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f6);
            return true;
        }
        _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f6);
        return false;
    }
}
