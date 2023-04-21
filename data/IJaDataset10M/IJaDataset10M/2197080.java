package mhk.action;

import org.seasar.struts.annotation.Execute;

public class IndexAction {

    @Execute(validator = false)
    public String index() {
        return "index.jsp";
    }
}
