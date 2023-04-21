package com.xmlap.jrp.tag;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import com.xmlap.jrp.transform.JSONTransformer;

public class OutTag extends JRPTag {

    private Object value;

    public void setValue(Object value) {
        this.value = value;
    }

    protected void doRpcExec() throws JspException, IOException {
        PageContext pc = (PageContext) getJspContext();
        JspWriter out = pc.getOut();
        out.write(JSONTransformer.serialize(value));
    }
}
