package com;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.TagSupport;

public class DateTag extends TagSupport {

    private static final long serialVersionUID = 5L;

    private String var = null;

    private String address = null;

    private String date = null;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public int doStartTag() throws JspException {
        this.var = "ʱ�䣺" + date + " �ص㣺 " + address;
        try {
            pageContext.getOut().write(var);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_BODY_INCLUDE;
    }

    public void release() {
        super.release();
        var = null;
    }
}
