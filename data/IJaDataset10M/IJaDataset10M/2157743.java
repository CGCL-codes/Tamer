package com.bones.core.web.tags;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;
import org.apache.log4j.Logger;
import com.bones.core.utils.ArrayUtils;
import com.bones.core.utils.ConvertUtils;
import com.bones.core.utils.StringUtils;
import com.bones.core.utils.WebUtils;

/**
 * 构建缺省的分页标签内容，拼装分页的html展现和一些相关script 注：目前是button的展现形式，其实可以改成button和<a href...之类两种可互相切换的展现方式
 */
public class PaginationTag extends BodyTagSupport {

    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(PaginationTag.class);

    private String firstTitle = "首 页";

    private String prevTitle = "上一页";

    private String nextTitle = "下一页";

    private String lastTitle = "末 页";

    private String gotoButLabel = "跳至";

    private String gotoButTitle = "跳转";

    private String firstClass;

    private String nextClass;

    private String prevClass;

    private String lastClass;

    private String gotoClass;

    private String gotoButClass;

    private String maxResultsClass;

    private int allCount;

    private List<?> results;

    private String property;

    private String gotoPage;

    private String gotoPageParams;

    private int maxPageSize;

    @Override
    public int doEndTag() throws JspException {
        IPagination pagination = (IPagination) this.pageContext.getRequest().getAttribute("pagination");
        if (null == pagination) {
            pagination = new DefaultPagination(getPageFirstResult(), getPageMaxResults());
            pagination.setAllCount(this.allCount);
            pagination.setResults(this.results);
        }
        getHtml(pagination);
        return Tag.EVAL_PAGE;
    }

    private int getPageFirstResult() {
        String gotoPageIndex = this.pageContext.getRequest().getParameter("gotoPageIndex");
        int maxResults = getPageMaxResults();
        return (ConvertUtils.toInt(gotoPageIndex, 0) * maxResults);
    }

    private int getPageMaxResults() {
        String maxResults = this.pageContext.getRequest().getParameter("maxResults");
        return ConvertUtils.toInt(maxResults, 18);
    }

    /** 拼装分页html和js */
    private void getHtml(IPagination pagination) {
        StringBuffer html = new StringBuffer();
        StringBuffer js = new StringBuffer();
        html.append("<table width='100%' cellpadding='0' cellspacing='0'>");
        html.append("<tr><td valign='middle' nowrap>");
        makeFirst(html, js, pagination);
        html.append("</td><td valign='middle' nowrap>");
        makePrev(html, js, pagination);
        html.append("</td><td valign=\"middle\" nowrap>");
        makeNext(html, js, pagination);
        html.append("</td><td valign=\"middle\" nowrap>");
        makeLast(html, js, pagination);
        html.append("</td><td valign=\"middle\" nowrap>");
        String totalRecord = "共" + pagination.getAllCount() + "条";
        html.append("&nbsp;").append(totalRecord);
        html.append("/").append("每页&nbsp;");
        makeMaxResults(html, js, pagination);
        html.append("&nbsp;条</td>");
        html.append("<td width='100%' align='right' valign='middle' nowrap>");
        String totalPages = "共" + pagination.getAllPageCount() + "页";
        html.append(totalPages);
        html.append("&nbsp;&nbsp;" + gotoButLabel + "&nbsp;");
        makeGoto(html, js, pagination);
        html.append("&nbsp;页&nbsp;");
        makeGotoBut(html, js, pagination);
        try {
            js.append(makeGetGotoPageParamFunction());
            js.append(makeExecuteGotoPage(pagination.getAllCount(), pagination.getAllPageCount()));
            String printStr = "<script>\n" + js.toString() + "</script>\n";
            this.pageContext.getOut().print(printStr);
            this.pageContext.getOut().print(html.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** 首页按钮html */
    private void makeFirst(StringBuffer html, StringBuffer js, IPagination pagination) {
        html.append("<input type='button' name='firstBut' title='" + firstTitle + "' value='" + firstTitle + "' class='" + firstClass + "'");
        if (null != pagination && pagination.getCurPageIndex() > 0) {
            js.append(makeChangePageIndexJS("toFirstPage", 0));
            html.append(" onclick='toFirstPage(this)'");
        }
        if (null == pagination || pagination.getCurPageIndex() == 0) {
            html.append(" disabled");
        }
        html.append("/></td>");
    }

    /** 上一页按钮html */
    private void makePrev(StringBuffer html, StringBuffer js, IPagination pagination) {
        html.append("<input type='button' name='prevBut' title='" + prevTitle + "' value='" + prevTitle + "' class='" + prevClass + "'");
        if (null != pagination && pagination.getCurPageIndex() > 0) {
            js.append(makeChangePageIndexJS("toPrevPage", pagination.getCurPageIndex() - 1));
            html.append(" onclick='toPrevPage(this)'");
        }
        if (null == pagination || pagination.getCurPageIndex() == 0) {
            html.append(" disabled");
        }
        html.append("/></td>");
    }

    /** 下一页按钮html */
    private void makeNext(StringBuffer html, StringBuffer js, IPagination pagination) {
        html.append("<input type='button' name='nextBut' title='" + nextTitle + "' value='" + nextTitle + "' class='" + nextClass + "'");
        if (pagination.getCurPageIndex() + 1 < pagination.getAllPageCount()) {
            js.append(makeChangePageIndexJS("toNextPage", pagination.getCurPageIndex() + 1));
            html.append(" onclick='toNextPage(this)'");
        }
        if (pagination.getCurPageIndex() + 1 >= pagination.getAllPageCount()) {
            html.append(" disabled");
        }
        html.append("/></td>");
    }

    /** 末页按钮html */
    private void makeLast(StringBuffer html, StringBuffer js, IPagination pagination) {
        html.append("<input type='button' name='lastBut' title='" + lastTitle + "' value='" + lastTitle + "' class='" + lastClass + "'");
        if (pagination.getCurPageIndex() + 1 < pagination.getAllPageCount()) {
            js.append(makeChangePageIndexJS("toLastPage", pagination.getAllPageCount() - 1));
            html.append(" onclick='toLastPage(this)'");
        }
        if (pagination.getCurPageIndex() + 1 >= pagination.getAllPageCount()) {
            html.append(" disabled");
        }
        html.append("/></td>");
    }

    private void makeMaxResults(StringBuffer html, StringBuffer js, IPagination pagination) {
        html.append("<input type='text' name='maxResultsTxt' class='" + maxResultsClass + "'");
        String onkeypress = makeChangePageSizeJS("toChangePageSize", true);
        js.append(onkeypress);
        html.append(" onkeypress='toChangePageSize(this)'");
        html.append(" value='" + pagination.getMaxResults() + "'");
        html.append(" size='2'/>");
    }

    private String makeChangePageSizeJS(String functionName, boolean checkClickEnter) {
        StringBuffer js = new StringBuffer();
        js.append("function " + functionName + "(tagName){\n");
        if (checkClickEnter) {
            js.append("    if(event.keyCode != 13)\n");
            js.append("        return false;\n");
        }
        js.append("    executeGotoPage(tagName.parentNode.parentNode);}\n");
        return js.toString();
    }

    protected void makeGoto(StringBuffer html, StringBuffer js, IPagination pagination) {
        html.append("<input type='text' name='gotoPageTxt' class='" + gotoClass + "'");
        String onkeypress = makeChangePageSizeJS("toChangePage", true);
        js.append(onkeypress);
        html.append("onkeypress='toChangePage(this)' value='" + (pagination.getCurPageIndex() + 1) + "'");
        html.append("size='3'/>");
    }

    protected void makeGotoBut(StringBuffer html, StringBuffer js, IPagination pagination) {
        html.append("&nbsp;<input type='button' name='gotoBut' class='" + gotoButClass + "'");
        js.append(makeChangePageSizeJS("toGotoPage", false));
        html.append("onclick='toGotoPage(this)' title='" + gotoButTitle + "' value='" + gotoButTitle + "'/></td></tr></table>");
    }

    /** 首页按钮点击事件 */
    private String makeChangePageIndexJS(String functionName, int pageIndex) {
        StringBuffer js = new StringBuffer();
        js.append("function " + functionName + "(tagName){\n");
        js.append("    var tr = tagName.parentNode.parentNode;\n");
        js.append("    getGotoPageParam(tr, 'gotoPageTxt').value = " + (pageIndex + 1) + ";\n");
        js.append("    executeGotoPage(tr);}\n");
        return js.toString();
    }

    protected String makeGetGotoPageParamFunction() {
        StringBuffer js = new StringBuffer();
        js.append("function getGotoPageParam(tr, tagId)\n");
        js.append("{\n");
        js.append("    var childrens = tr.children;\n");
        js.append("    for(var i = 0; i < childrens.length; i++)\n");
        js.append("    {\n");
        js.append("        var tdChildrens = childrens[i].children;\n");
        js.append("        for(var j = 0; j < tdChildrens.length; j++)\n");
        js.append("        {\n");
        js.append("            if(tdChildrens[j].id == tagId || tdChildrens[j].name == tagId)\n");
        js.append("            {\n");
        js.append("                return tdChildrens[j];\n");
        js.append("            }\n");
        js.append("        }\n");
        js.append("    }\n");
        js.append("    return null;\n");
        js.append("}\n");
        return js.toString();
    }

    protected String makeExecuteGotoPage(int allCount, int allPageCount) {
        StringBuffer js = new StringBuffer();
        js.append("function executeGotoPage(tr)\n");
        js.append("{\n");
        js.append("    var url = \"").append(parseGotoPage()).append("\";\n");
        js.append("    var gotoPageIndex = getGotoPageParam(tr, \"gotoPageTxt\").value;\n");
        js.append("    var maxResults = getGotoPageParam(tr, \"maxResultsTxt\").value;\n");
        js.append("    if(isNaN(gotoPageIndex) || gotoPageIndex < 1)\n");
        js.append("    {\n");
        js.append("        alert('请输入大于 0 的整数');\n");
        js.append("        return false;\n");
        js.append("    }\n");
        js.append("    else if(gotoPageIndex * maxResults > ").append(allCount).append(")\n");
        js.append("    {\n");
        js.append("        if(").append(allCount).append(" - (gotoPageIndex - 1) * maxResults <= 0)\n");
        js.append("        {\n");
        js.append("            alert('超出总页数范围');\n");
        js.append("            return;\n");
        js.append("        }\n");
        js.append("    }\n");
        js.append("    if(url.indexOf(\"?\") == -1)\n");
        js.append("        url += \"?\";\n");
        js.append("    else\n");
        js.append("        url += \"&\";\n");
        js.append("    url += \"gotoPageIndex=\" + (gotoPageIndex - 1);\n");
        js.append("    if(isNaN(maxResults) || maxResults < 1)\n");
        js.append("    {\n");
        js.append("        alert('请输入大于 0 的整数');\n");
        js.append("        return false;\n");
        js.append("    }\n");
        js.append("    else\n");
        js.append("    {\n");
        js.append("        if(maxResults > ").append(allCount).append(")\n");
        js.append("        {\n");
        js.append("            maxResults = ").append(allCount).append(";\n");
        js.append("        }\n");
        js.append("        if(").append(this.maxPageSize).append(" > 0 && maxResults > ").append(this.maxPageSize).append(")\n");
        js.append("        {\n");
        js.append("            maxResults = ").append(this.maxPageSize).append(";\n");
        js.append("        }\n");
        js.append("    }\n");
        js.append("    url += \"&maxResults=\" + maxResults;\n");
        js.append("    var customUrl = null;\n");
        js.append("    try\n");
        js.append("    {\n");
        js.append("        customUrl = ").append("getCustomGotoPageUrl(url);\n");
        js.append("    }\n");
        js.append("    catch(e){}\n");
        js.append("    if(customUrl != null && customUrl.length > 0)\n");
        js.append("    {\n");
        js.append("        url = customUrl;\n");
        js.append("    }\n");
        js.append("    try\n");
        js.append("    {\n");
        js.append("        ").append("customGotoPageAction(url, (gotoPageIndex - 1), maxResults);\n");
        js.append("    }\n");
        js.append("    catch(e)\n");
        js.append("    {\n");
        js.append("      window.location = url;\n");
        js.append("    }\n");
        js.append("}\n");
        return js.toString();
    }

    protected String parseGotoPage() {
        HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
        StringBuffer url = new StringBuffer();
        if ((gotoPage == null) || (gotoPage.length() == 0)) {
            log.info("gotoPage not set. use request uri");
            url.append(request.getRequestURI());
        } else {
            url.append(gotoPage);
        }
        String[] pageParamNames = (String[]) null;
        if (StringUtils.isNotEmpty(getGotoPageParams())) {
            String[] pageParams = getGotoPageParams().toString().split("&");
            pageParamNames = new String[pageParams.length];
            for (int i = 0; i < pageParams.length; i++) {
                int index = pageParams[i].indexOf("=");
                if (index == -1) continue;
                String name = pageParams[i].substring(0, index);
                index++;
                String value = pageParams[i].substring(index);
                WebUtils.appendParam(url, name, value);
                pageParamNames[i] = name;
            }
        }
        if (StringUtils.isNotEmpty(request.getQueryString())) {
            String[] queryString = request.getQueryString().split("&");
            for (int i = 0; i < queryString.length; i++) {
                if ((queryString[i].indexOf("gotoPageIndex" + "=") != -1) || (queryString[i].indexOf("maxResults" + "=") != -1)) {
                    continue;
                }
                if (ArrayUtils.isInArray(pageParamNames, queryString[i].split("=")[0])) {
                    continue;
                }
                if (url.indexOf("?") != -1) url.append("&"); else url.append("?");
                url.append(queryString[i]);
            }
        }
        return url.toString();
    }

    @Override
    public int doStartTag() throws JspException {
        return 1;
    }

    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }

    public List<?> getResults() {
        return results;
    }

    public void setResults(List<?> results) {
        this.results = results;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getGotoPage() {
        return gotoPage;
    }

    public void setGotoPage(String gotoPage) {
        this.gotoPage = gotoPage;
    }

    public int getMaxPageSize() {
        return maxPageSize;
    }

    public void setMaxPageSize(int maxPageSize) {
        this.maxPageSize = maxPageSize;
    }

    public String getGotoPageParams() {
        return gotoPageParams;
    }

    public void setGotoPageParams(String gotoPageParams) {
        this.gotoPageParams = gotoPageParams;
    }

    public void setLastTitle(String lastTitle) {
        this.lastTitle = lastTitle;
    }

    public void setFirstClass(String firstClass) {
        this.firstClass = firstClass;
    }

    public void setNextClass(String nextClass) {
        this.nextClass = nextClass;
    }

    public void setPrevClass(String prevClass) {
        this.prevClass = prevClass;
    }

    public void setLastClass(String lastClass) {
        this.lastClass = lastClass;
    }

    public void setGotoClass(String gotoClass) {
        this.gotoClass = gotoClass;
    }

    public void setGotoButClass(String gotoButClass) {
        this.gotoButClass = gotoButClass;
    }

    public void setMaxResultsClass(String maxResultsClass) {
        this.maxResultsClass = maxResultsClass;
    }

    public void setAllClass(String allClass) {
        this.firstClass = allClass;
        this.nextClass = allClass;
        this.prevClass = allClass;
        this.lastClass = allClass;
        this.gotoClass = allClass;
        this.maxResultsClass = allClass;
        this.gotoButClass = allClass;
    }
}
