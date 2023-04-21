package cn.ac.ntarl.umt.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 06-29-2007
 * 
 * XDoclet definition:
 * @struts.form name="deleteUserForm"
 */
public class DeleteUserForm extends ActionForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /** userID property */
    private String userID;

    /** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (Validator.isEmpty(userID)) {
            errors.add("userID", new ActionError("errors.impossible"));
        }
        return errors;
    }

    /** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.userID = null;
    }

    /** 
	 * Returns the userID.
	 * @return String
	 */
    public String getUserID() {
        return userID;
    }

    /** 
	 * Set the userID.
	 * @param userID The userID to set
	 */
    public int getID() {
        return Integer.parseInt(userID);
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
