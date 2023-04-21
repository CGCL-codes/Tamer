package edu.osu.cse.be.struts.form.referee;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author eugene
 */
public class ViewGameDetailsForm extends ActionForm {

    private String id;

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        return errors;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.id = null;
    }

    /**
	 * 
	 * @uml.property name="id"
	 */
    public String getId() {
        return id;
    }

    /**
	 * 
	 * @uml.property name="id"
	 */
    public void setId(String id) {
        this.id = id;
    }
}
