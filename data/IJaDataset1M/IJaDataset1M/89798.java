package com.dbxml.db.enterprise.jsp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * PropertyTag
 */
public class PropertyTag extends TagSupport {

    private String name;

    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int doStartTag() throws JspException {
        Tag p = getParent();
        if (p instanceof PropertyContainer) {
            ((PropertyContainer) p).setProperty(name, value);
            return SKIP_BODY;
        } else throw new JspException("Parent tag cannot accept properties");
    }
}
