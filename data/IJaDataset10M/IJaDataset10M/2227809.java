package com.figo.tld;

import java.io.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.figo.ORM.*;
import com.figo.Service.ContentsService;
import com.figo.DAO.*;

/** 相关类别列表自定义标签类 */
public class RelatedcolumnsListTag extends SimpleTagSupport implements DynamicAttributes {

    BaseDAO dao = null;

    Iterator<Contentscolumns> it1;

    Contentscolumns cate1;

    String jsp = "newmer.jsp";

    Contents content = null;

    ContentsService service;

    private void initDao() {
        if (dao == null) {
            dao = (BaseDAOImpl) WebApplicationContextUtils.getRequiredWebApplicationContext(((PageContext) getJspContext()).getServletContext()).getBean("dao");
        }
    }

    public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
        initDao();
        if (localName.compareToIgnoreCase("cid") == 0) {
            String hql = "from Contents where id = " + (String) (value);
            List<Contents> Contentslist = dao.query(hql, null);
            if (Contentslist.size() > 0) {
                content = Contentslist.get(0);
            }
        }
    }

    /** 标签体处理 */
    @SuppressWarnings("unchecked")
    public void doTag() throws JspException, IOException {
        String contextPath = ((HttpServletRequest) ((PageContext) getJspContext()).getRequest()).getContextPath();
        initDao();
        StringBuffer sb = new StringBuffer();
        sb.append("\n");
        if (content != null) {
            it1 = content.getContentsColumnses().iterator();
            while (it1.hasNext()) {
                cate1 = it1.next();
                sb.append("<div class='cate_list'>\n");
                sb.append("<ul>\n");
                sb.append("<li><a target='_self' href='" + contextPath + "/webs/" + jsp + "?cateid=" + cate1.getId() + "'>" + cate1.getColumnName().trim() + "</a></li>\n");
                sb.append("</ul>\n");
                sb.append("</div>\n");
            }
        }
        content = null;
        getJspContext().getOut().println(sb);
    }
}
