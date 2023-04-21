package net.woodstock.rockapi.struts;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StringResult implements StrutsResult {

    private String name;

    public StringResult() {
        super();
    }

    public StringResult(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ActionForward getForward(ActionMapping mapping) {
        return mapping.findForward(this.name);
    }
}
