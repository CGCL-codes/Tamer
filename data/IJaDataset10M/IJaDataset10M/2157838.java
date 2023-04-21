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
import com.figo.DAO.*;

/** 相关类别列表自定义标签类 */
public class ListFeedbackTag extends SimpleTagSupport implements DynamicAttributes {

    BaseDAO dao = null;

    Iterator<Feedback> oit;

    int id;

    private void initDao() {
        if (dao == null) {
            dao = (BaseDAOImpl) WebApplicationContextUtils.getRequiredWebApplicationContext(((PageContext) getJspContext()).getServletContext()).getBean("dao");
        }
    }

    public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
        initDao();
        id = (Integer) value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /** 标签体处理 */
    @SuppressWarnings("unchecked")
    public void doTag() throws JspException, IOException {
        String contextPath = ((HttpServletRequest) ((PageContext) getJspContext()).getRequest()).getContextPath();
        initDao();
        String hql = "from Feedback as a where a.contents.id=" + id + " order by a.postDate";
        List<Feedback> oFeedbackList = dao.query(hql, 0, 7, null);
        StringBuffer sb = new StringBuffer();
        sb.append("\n");
        if (oFeedbackList != null) {
            oit = oFeedbackList.iterator();
            if (!oFeedbackList.isEmpty()) {
                sb.append("Happy逗网友评论：<br />");
            }
            while (oit.hasNext()) {
                Feedback oFeedback = oit.next();
                sb.append("<div class='usertab'>\n");
                if (oFeedback.getUseId() == null) {
                    sb.append(oFeedback.getIp() + ":");
                } else {
                    sb.append(oFeedback.getUseId() + ":");
                }
                sb.append(oFeedback.getFeedback() + "\n");
                sb.append("</div>\n");
            }
        }
        getJspContext().getOut().println(sb);
    }
}
