package cn.com.pxto.web.tag;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import cn.com.pxto.dao.DicDAO;
import cn.com.pxto.model.Dic;

public class DicNameTag extends BodyTagSupport {

    private String type;

    private String name;

    private DicDAO dicDAO;

    private static Log log = LogFactory.getLog(DicNameTag.class);

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DicNameTag() {
    }

    public int doStartTag() throws JspException {
        ApplicationContext ctx = (XmlWebApplicationContext) pageContext.getServletContext().getAttribute("org.springframework.web.struts.ContextLoaderPlugIn.CONTEXT.");
        dicDAO = (DicDAO) ctx.getBean("dicFeeDAO");
        Dic dic = dicDAO.getDicByTypeAndName(this.type, name);
        try {
            if (dic == null) {
                pageContext.getOut().print("-1");
            } else {
                pageContext.getOut().print(dic.getId());
            }
        } catch (IOException e) {
            throw new JspException("Print Error:" + e.getMessage());
        }
        return super.doStartTag();
    }
}
