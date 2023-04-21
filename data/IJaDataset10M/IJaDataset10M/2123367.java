package net.homeip.tinwiki.web.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form bean for a Struts application.
 * Users may access 1 field on this form:
 * <ul>
 * <li>content - [your comment here]
 * </ul>
 * @version 	1.0
 * @author
 */
public class WebPage extends ActionForm {

    private String content = null;

    private String fileName = null;

    private String sectionName = null;

    /**
	 * @return Returns the sectionName.
	 */
    public String getSectionName() {
        return sectionName;
    }

    /**
	 * @param sectionName The sectionName to set.
	 */
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    /**
     * Get content
     * @return String
     */
    public String getContent() {
        return content;
    }

    /**
     * Set content
     * @param <code>String</code>
     */
    public void setContent(String c) {
        this.content = c;
    }

    /**
	 * @return Returns the name.
	 */
    public String getFileName() {
        return fileName;
    }

    /**
	 * @param name The name to set.
	 */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        content = null;
        fileName = null;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        return errors;
    }
}
