package com.jxva.tag;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import com.jxva.util.HtmlUtil;
import com.jxva.util.ModelUtil;

/**
 * <j:textarea prop="property" rows="" cols=""/>
 * or
 * <j:textarea prop="property" rows="" cols="">text</j:textarea>
 * @author  The Jxva Framework Foundation
 * @since 2006-12-25
 * 
 * 
 * 
 */
public class TextareaTag extends BaseInputTag {

    private static final long serialVersionUID = 1L;

    protected String textareaContent;

    protected String rows;

    protected String cols;

    public void setRows(String rows) {
        this.rows = rows;
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    public int doStartTag() throws JspException {
        textareaContent = null;
        return EVAL_BODY_AGAIN;
    }

    public int doAfterBody() throws JspException {
        if (bodyContent != null) {
            String text = bodyContent.getString();
            if (text != null) {
                text = text.trim();
                if (text.length() > 0) {
                    textareaContent = text;
                }
            }
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException {
        try {
            FormTag fromtag = (FormTag) TagSupport.findAncestorWithClass(this, FormTag.class);
            bean = fromtag.getBean();
            if (pageContext.getAttribute(bean) != null) {
                Object obj = pageContext.getAttribute(bean);
                result = ModelUtil.getPropertyValue(obj, name);
            }
            StringBuilder sb = new StringBuilder("<textarea name=\"" + name + "\"");
            prepareAttribute(sb, "rows", rows);
            prepareAttribute(sb, "cols", cols);
            sb.append(prepareOtherAttribute());
            sb.append(prepareStates());
            sb.append(prepareEventHandlers());
            sb.append(">");
            if (!hasBeanValue()) {
                sb.append((textareaContent == null) ? "" : textareaContent);
            } else {
                sb.append(HtmlUtil.filter(result));
            }
            sb.append("</textarea>");
            pageContext.getOut().print(sb.toString());
            return EVAL_PAGE;
        } catch (IOException e) {
            throw new JspException(e);
        }
    }
}
