package org.homeunix.thecave.moss.jsp.lightbox;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

public class LightboxGalleryHeaderTag implements Tag {

    private PageContext pc = null;

    private Tag parent = null;

    public void setPageContext(PageContext p) {
        pc = p;
    }

    public void setParent(Tag t) {
        parent = t;
    }

    public Tag getParent() {
        return parent;
    }

    public int doStartTag() throws JspException {
        try {
            pc.getOut().write("<script type='text/javascript' src='" + LightboxFilter.JAVASCRIPT_PATH + "/prototype.js'></script>\n");
            pc.getOut().write("<script type='text/javascript' src='" + LightboxFilter.JAVASCRIPT_PATH + "/scriptaculous.js?load=effects,builder'></script>\n");
            pc.getOut().write("<script type='text/javascript' src='" + LightboxFilter.JAVASCRIPT_PATH + "/lightbox.js'></script>\n");
            pc.getOut().write("<link rel='stylesheet' href='" + LightboxFilter.CSS_PATH + "/lightbox.css' type='text/css' media='screen' />\n");
        } catch (IOException ioe) {
            throw new JspTagException("An IOException occurred.");
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException {
        return SKIP_BODY;
    }

    public void release() {
        pc = null;
        parent = null;
    }
}
