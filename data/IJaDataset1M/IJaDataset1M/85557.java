package cn.ac.ntarl.umt.form.app;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 04-21-2008
 * 
 * XDoclet definition:
 * @struts.form name="promptGroupForm"
 */
public class PromptGroupForm extends ActionForm {

    /** groupname property */
    private String query;

    private int start;

    private int limit;

    /** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }

    /** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    }

    /** 
	 * Returns the groupname.
	 * @return String
	 */
    public String getQuery() {
        return query;
    }

    /** 
	 * Set the groupname.
	 * @param query The query to set
	 */
    public void setQuery(String query) {
        this.query = query;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getStart() {
        return start;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }
}
