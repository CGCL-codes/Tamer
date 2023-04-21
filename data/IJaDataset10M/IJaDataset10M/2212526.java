package org.weblayouttag.mode;

import org.weblayouttag.manager.Manager;
import org.dom4j.Element;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

/**
 * @author Andy Marek
 * @version Jun 13, 2005
 */
public interface ModeManager extends Manager {

    /**
	 * Returns the default mode for a given form.
	 * 
	 * @param pc The page context as a <code>PageContext</code>.
	 * @param form The form as an <code>Object</code>.
	 * @return Return the default mode as a <code>String</code>.
	 */
    String getDefaultMode(PageContext pc, Object form);

    /**
	 * Returns the default mode for a given form.
	 * 
	 * @param request The servlet request as a <code>HttpServletRequest</code>.
	 * @param form The form as an <code>Object</code>.
	 * @return Return the default mode as a <code>String</code>.
	 */
    String getDefaultMode(HttpServletRequest request, Object form);

    /**
	 * Returns the form mode for a given form.
	 * 
	 * @param pc The page context as a <code>PageContext</code>.
	 * @param form The form as an <code>Object</code>.
	 * @return Return the form mode as a <code>String</code>.
	 */
    String getFormMode(PageContext pc, Object form);

    /**
	 * Returns the form mode for a given form.
	 * 
	 * @param request The servlet request as a <code>HttpServletRequest</code>.
	 * @param form The form as an <code>Object</code>.
	 * @return Return the form mode as a <code>String</code>.
	 */
    String getFormMode(HttpServletRequest request, Object form);

    /**
	 * Parses a string into a form mode.
	 * 
	 * @param string The comma-seperated list of modes as a <code>String</code>.
	 * @return Returnt the form mode as a <code>FormMode</code>.
	 */
    FormMode parse(String string);

    /**
	 * Sets the form mode for a given form.
	 * 
	 * @param pc The page context as a <code>PageContext</code>.
	 * @param form The form as an <code>Object</code>.
	 * @param formMode Set the form mode as a <code>String</code>.
	 */
    void setFormMode(PageContext pc, Object form, String formMode);

    /**
	 * Sets the form mode for a given form.
	 * 
	 * @param request The servlet request as a <code>HttpServletRequest</code>.
	 * @param form The form as an <code>Object</code>.
	 * @param formMode Set the form mode as a <code>String</code>.
	 */
    void setFormMode(HttpServletRequest request, Object form, String formMode);

    /**
	 * Converts a parsable form mode string into XML based on the form mode that should be used.  The root tag 
	 * should be <code>modes</code>.<p>
	 * 
	 * For example, if you passed <code>toXml(&quot;S,H,I&quot;,&quot;edit&quot;)</code>, then the following 
	 * XML fragment should be returned: <code>&lt;mode form=&quot;edit&quot; field=&quotS&quot; /&gt;</code>.
	 * 
	 * @param parsableFormMode The parsable form mode string as a <code>String</code>.
	 * @param formModeToUse The form mode that will determine which field mode to use as a <code>String</code>.
	 * @return Return the form mode as an <code>Element</code>.
	 */
    Element toXml(String parsableFormMode, String formModeToUse);
}
