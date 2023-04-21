package com.ideo.sweetdevria.taglib.collapse;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import com.ideo.sweetdevria.taglib.AbstractComponentTagSupport;
import com.ideo.sweetdevria.taglib.Hideable;
import com.ideo.sweetdevria.taglib.IRiaTag;

/**
 * @jsp.tag name = "collapseHeader"
 * display-name = "Collapse Header Tag"
 * description = "Description : The content of this tag will be displayed as the collapse header, overriding the header attribute. Nested in tag : &lt;b&gt;ria:collapse&lt;/b&gt;"
 */
public class CollapseHeaderTag extends AbstractComponentTagSupport implements IRiaTag, Hideable {

    private static final long serialVersionUID = 8750291207954621874L;

    public CollapseHeaderTag() {
        release();
    }

    public String getBuilderId() {
        return "collapseHeader.builder";
    }

    public void release() {
        super.release();
    }

    public int doStartTag() throws JspException {
        CollapseTag collapse = (CollapseTag) TagSupport.findAncestorWithClass(this, CollapseTag.class);
        if (collapse != null) {
            this.setId(collapse.getId() + "_headerTag");
            collapse.setHeader(null);
        }
        return super.doStartTag();
    }
}
