package com.sitescape.team.taglib;

import javax.servlet.jsp.JspTagException;

/**
 * @author Hemanth Chokkanathan
 *
 */
public class TitleTag extends AltTag {

    public int doEndTag() throws JspTagException {
        super.setAttName("TITLE");
        return super.doEndTag();
    }
}
