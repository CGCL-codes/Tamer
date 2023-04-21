package com.xmlap.jrp.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;

public class ArgTag extends JRPTag {

    private String var;

    private boolean rest;

    public void setVar(String var) {
        this.var = var;
    }

    public void setRest(boolean rest) {
        this.rest = rest;
    }

    protected void doRpcExec() throws JspException, IOException {
        PageContext pc = (PageContext) getJspContext();
        ServletRequest req = pc.getRequest();
        List params = (List) req.getAttribute(JRP_PARAMS);
        if (!rest) {
            if (params.isEmpty()) {
                returnErrorResult("An argument is missing for: " + var);
                return;
            } else {
                pc.setAttribute(var, params.remove(0));
            }
        } else {
            ArrayList result = new ArrayList();
            while (params.size() > 0) {
                result.add(params.remove(0));
            }
            pc.setAttribute(var, result);
        }
    }

    protected void doIndex() throws JspException, IOException {
        Map jrp_info = (Map) getResult();
        List params = (List) jrp_info.get("_params_");
        if (params == null) {
            params = new ArrayList();
            jrp_info.put("_params_", params);
        }
        params.add(var);
    }

    protected void doUiForm() throws JspException, IOException {
        Map method_info = (Map) getResult();
        List args = (List) method_info.get("args");
        JspFragment body = getJspBody();
        String desc = null;
        if (body != null) {
            body.invoke(null);
            desc = (String) method_info.remove("_desc_");
        }
        if (desc == null) {
            desc = "(No description available)";
        }
        String[] arg_entry = new String[2];
        arg_entry[0] = var;
        arg_entry[1] = desc;
        args.add(arg_entry);
    }
}
