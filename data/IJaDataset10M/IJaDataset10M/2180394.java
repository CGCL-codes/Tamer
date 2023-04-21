package com.gever.struts.pager.taglib;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import com.gever.struts.pager.PageControl;
import com.gever.web.util.URLBuilder;

/**
 * ��ҳ��ǩ
 * @author Hu.Walker
 */
public class FirstPageTag extends BodyTagSupport {

    private String text;

    private PagerTag parent;

    private PageControl pageControl = null;

    public int doEndTag() throws JspException {
        parent = (PagerTag) this.getParent();
        pageControl = (PageControl) pageContext.findAttribute(parent.getName());
        try {
            JspWriter out = bodyContent.getEnclosingWriter();
            if (pageControl.getCurrentPage() <= 1) {
                out.write(text);
            } else {
                String url = URLBuilder.addParameter(parent.getPage(), "page=1");
                out.write("<a href=" + url + ">");
                out.write(text + "</a>");
            }
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return EVAL_BODY_INCLUDE;
    }

    public int doAfterBody() throws JspException {
        text = bodyContent.getString();
        return SKIP_BODY;
    }
}
