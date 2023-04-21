package demo;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class GetNameForm extends ActionForm {

    private static final long serialVersionUID = 3212296465977140183L;

    private String name = "";

    public GetNameForm() {
    }

    public void reset(ActionMapping actionMapping, HttpServletRequest request) {
        this.name = "";
    }

    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request) {
        ActionErrors errs = new ActionErrors();
        return errs;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = (name == null ? "" : name);
    }
}
