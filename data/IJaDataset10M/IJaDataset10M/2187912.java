package org.apache.jasper.el;

import javax.el.ELException;

public class JspELException extends ELException {

    public JspELException(String mark, ELException e) {
        super(mark + " " + e.getMessage(), e.getCause());
    }
}
