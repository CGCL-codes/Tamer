package com.zhongkai.web.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.log4j.Logger;
import com.zhongkai.dao.TagDao;
import com.zhongkai.tools.CheckWord;
import com.zhongkai.tools.SpringUtils;

public class ListTag extends BodyTagSupport {

    Logger log = Logger.getLogger(this.getClass());

    private String table;

    private String name = "rows";

    private String pageMax = null;

    private String orderBy = "";

    private String frontRow = null;

    private String field;

    private String groupBy = "";

    @Override
    public int doEndTag() {
        try {
            String sqlwhere = "";
            String max = null;
            StringBuffer hql = null;
            if (field != null && !field.matches("\\s*") && !CheckWord.hasInvalidWord(field)) hql = new StringBuffer("select ").append(field).append(" from "); else hql = new StringBuffer("from ");
            if (table == null || table.matches("\\s*") || CheckWord.hasInvalidWord(table)) throw new RuntimeException("对像名不存在!"); else hql.append(table);
            if (this.getBodyContent() != null && !this.getBodyContent().getString().matches("\\s*") && !CheckWord.hasInvalidWord(this.getBodyContent().getString())) sqlwhere = "  where " + getBodyContent().getString();
            hql.append(sqlwhere);
            if (!groupBy.matches("\\s*") && !CheckWord.hasInvalidWord(groupBy)) hql.append("  group by ").append(groupBy);
            if (!orderBy.matches("\\s*") && !CheckWord.hasInvalidWord(orderBy)) hql.append("  order by ").append(orderBy);
            TagDao tagDao = (TagDao) SpringUtils.getBean("tagDao", this.pageContext.getServletContext());
            List list = null;
            StringBuffer cacheTable = new StringBuffer();
            cacheTable.append("TDmCcsBxdjzt");
            cacheTable.append("|TDmCcsCzlb");
            cacheTable.append("|TDmCcsDjzt");
            cacheTable.append("|TDmCcsJjcclx");
            cacheTable.append("|TDmCcsQryj");
            cacheTable.append("|TDmCcsSwcclx");
            cacheTable.append("|TDmCcsYwlx");
            cacheTable.append("TDmCcsZxyy");
            Pattern pattern = Pattern.compile(cacheTable.toString());
            Matcher matcher = pattern.matcher(table);
            if (frontRow == null && pageMax == null) {
                if (matcher.matches()) list = tagDao.selectForCache(hql.toString()); else list = tagDao.select(hql.toString());
            } else {
                String pageIndex = pageContext.getRequest().getParameter("pageIndex");
                if (pageIndex == null || !pageIndex.matches("\\d+")) {
                    pageIndex = "1";
                }
                if (frontRow != null && frontRow.matches("\\d+")) {
                    max = frontRow;
                    pageIndex = "1";
                } else if (pageMax != null && pageMax.matches("\\d+")) {
                    int count = tagDao.count(table, sqlwhere).intValue();
                    max = pageMax;
                    pageContext.setAttribute("pageMax", pageMax);
                    pageContext.setAttribute("totalRecord", String.valueOf(count));
                }
                if (matcher.matches()) list = tagDao.selectForCache(hql.toString(), new Integer(pageIndex), new Integer(max)); else list = tagDao.select(hql.toString(), new Integer(pageIndex), new Integer(max));
            }
            if (list == null) list = new ArrayList();
            pageContext.setAttribute(name, list);
            return EVAL_PAGE;
        } catch (Exception ex) {
            try {
                ex.printStackTrace();
                String path = ((HttpServletRequest) pageContext.getRequest()).getContextPath();
                log.error(ex.getMessage());
                ((HttpServletResponse) pageContext.getResponse()).sendRedirect(path + "/errorpage.jsp?errorinfo=" + java.net.URLEncoder.encode(ex.toString(), "UTF-8"));
            } catch (java.io.IOException ioe) {
                log.error(ioe.getMessage());
            }
            return EVAL_PAGE;
        }
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPageMax() {
        return pageMax;
    }

    public void setPageMax(String pageMax) {
        this.pageMax = pageMax;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getFrontRow() {
        return frontRow;
    }

    public void setFrontRow(String frontRow) {
        this.frontRow = frontRow;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }
}
