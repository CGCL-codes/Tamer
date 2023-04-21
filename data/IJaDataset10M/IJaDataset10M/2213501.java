package com.frameworkset.common.tag;

import java.io.IOException;
import javax.servlet.jsp.JspException;

/**
 * @author biaoping.yin
 * created on 2005-7-19
 * version 1.0
 */
public class FormTag extends BaseTag {

    /**
     * @return Returns the action.
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action The action to set.
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @return Returns the method.
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param method The method to set.
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    private String action;

    private String method;

    private String name;

    public int doStartTag() throws JspException {
        try {
            this.getJspWriter().print("<form action=\"" + action + "\" method=\"" + method + "\" name=\"" + name + "\">");
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspException {
        try {
            this.getJspWriter().print("</form>");
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return EVAL_PAGE;
    }
}
