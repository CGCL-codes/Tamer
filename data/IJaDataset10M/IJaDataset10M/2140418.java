package purej.web.tag;

import java.util.Hashtable;
import java.util.Stack;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import purej.logging.Logger;
import purej.logging.LoggerFactory;
import purej.web.tag.bean.PageParameter;

@SuppressWarnings("unchecked")
public class TemplateGetTag extends TagSupport {

    /**
     * <code>serialVersionUID</code>�� ����
     */
    private static final long serialVersionUID = 3089972926580508557L;

    private static final Logger log = LoggerFactory.getLogger(TemplateGetTag.class, Logger.FRAMEWORK);

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            Stack stack = (Stack) pageContext.getAttribute("template-stack", PageContext.APPLICATION_SCOPE);
            if (stack == null) throw new JspException("GetTag.doStartTag(): " + "NO STACK");
            Hashtable params = (Hashtable) stack.peek();
            if (params == null) throw new JspException("GetTag.doStartTag(): " + "NO HASHTABLE");
            PageParameter param = (PageParameter) params.get(name);
            if (param != null) {
                String content = param.getContent();
                if (param.isDirect()) {
                    try {
                        pageContext.getOut().print(content);
                    } catch (java.io.IOException ex) {
                        throw new JspException("Page print exception : " + ex.getMessage());
                    }
                } else {
                    try {
                        if (!pageContext.getOut().isAutoFlush()) {
                            pageContext.getOut().flush();
                        }
                        pageContext.include(content);
                    } catch (Exception ex) {
                        throw new JspException("Page include exception : " + ex.getMessage());
                    }
                }
            }
        } catch (Exception ex) {
            log.error("LSF JSP Template Get Tag Error : ", ex);
        }
        return SKIP_BODY;
    }

    @Override
    public void release() {
        name = null;
    }
}
