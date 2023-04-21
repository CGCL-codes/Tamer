package com.generalynx.common.web.tags;

import javax.servlet.jsp.JspException;

public class BooleanCollectionHandlerTag extends VoidCollectionHandlerTag {

    public int doStartTag() throws JspException {
        try {
            Object result = handleCollection();
            if (result instanceof Boolean) {
                Boolean b = (Boolean) result;
                return b.booleanValue() ? EVAL_BODY_INCLUDE : SKIP_BODY;
            } else {
                logger.warn("handleCollection() didn't return boolean: " + result.getClass().getName());
                return SKIP_BODY;
            }
        } catch (Exception e) {
            throw new JspException(e);
        }
    }
}
