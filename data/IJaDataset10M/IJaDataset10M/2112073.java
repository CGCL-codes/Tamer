package com.liferay.taglib.util;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * <a href="ParamTag.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class ParamTag extends TagSupport {

    public int doStartTag() throws JspException {
        ParamAncestorTag paramAncestor = (ParamAncestorTag) findAncestorWithClass(this, ParamAncestorTag.class);
        if (paramAncestor == null) {
            throw new JspException();
        }
        paramAncestor.addParam(_name, _value);
        return EVAL_BODY_INCLUDE;
    }

    public void setName(String name) {
        _name = name;
    }

    public void setValue(String value) {
        _value = value;
    }

    private String _name;

    private String _value;
}
