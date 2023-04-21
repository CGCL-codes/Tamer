package org.kablink.teaming.taglib;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.kablink.teaming.domain.Binder;
import org.kablink.teaming.domain.DefinableEntity;
import org.kablink.teaming.domain.FileAttachment;
import org.kablink.teaming.ssfs.util.SsfsUtil;

public class SsfsInternalTitleFileUrlTag extends TagSupport {

    private Binder binder;

    private DefinableEntity entity;

    private FileAttachment fa;

    public int doStartTag() throws JspException {
        if (binder == null) throw new JspException("Binder must be specified");
        if (entity == null) throw new JspException("Entity must be specified");
        if (fa == null) throw new JspException("File attachment must be specified");
        String url = SsfsUtil.getInternalTitleFileUrl((HttpServletRequest) pageContext.getRequest(), binder, entity, fa);
        try {
            pageContext.getOut().print(url);
        } catch (IOException e) {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    public void setBinder(Binder binder) {
        this.binder = binder;
    }

    public void setEntity(DefinableEntity entity) {
        this.entity = entity;
    }

    public void setFileAttachment(FileAttachment fa) {
        this.fa = fa;
    }
}
