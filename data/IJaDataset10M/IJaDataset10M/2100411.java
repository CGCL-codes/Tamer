package com.zhongkai.web.tag;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Logger;
import com.zhongkai.dao.TagDao;
import com.zhongkai.model.config.Button;
import com.zhongkai.tools.SpringUtils;

public class DisplayButtonTag extends BodyTagSupport {

    Logger log = Logger.getLogger(this.getClass());

    private String name = null;

    private String type = null;

    @Override
    public int doEndTag() throws JspException {
        try {
            TagDao tagDao = (TagDao) SpringUtils.getBean("tagDao", this.pageContext.getServletContext());
            List list = tagDao.select("from Button where buttonName=?", new Object[] { name });
            if (list != null && list.size() > 0) {
                Button button = (Button) list.get(0);
                String actionKey = button.getActionKey();
                List buttonActionList = (List) pageContext.getSession().getAttribute("buttonAction");
                if (buttonActionList.contains(actionKey)) {
                    StringBuffer pageButton = new StringBuffer();
                    if (type == null || type.matches("")) type = "button";
                    pageButton.append("<input type='").append(type).append("' name='").append(name).append("' class='system_button' ").append(this.getBodyContent() == null ? "" : this.getBodyContent().getString()).append(" />");
                    JspWriter writer = pageContext.getOut();
                    writer.println(pageButton.toString());
                }
            }
        } catch (Exception ex) {
            try {
                ex.printStackTrace();
                String path = ((HttpServletRequest) pageContext.getRequest()).getContextPath();
                log.error(ex.getMessage());
                ((HttpServletResponse) pageContext.getResponse()).sendRedirect(path + "/errorpage.jsp?errorinfo=" + java.net.URLEncoder.encode(ex.toString(), "UTF-8"));
            } catch (java.io.IOException ioe) {
                log.error(ioe.getMessage());
            }
        }
        return EVAL_PAGE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
