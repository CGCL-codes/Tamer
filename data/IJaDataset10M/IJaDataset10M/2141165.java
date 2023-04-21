package org.zkoss.jsp.zul;

import org.zkoss.zul.Tabbox;
import org.zkoss.jsp.zul.impl.BranchTag;

/**
 * The JSP tag to represent "Tabbox" component in ZK's component definition.
 * @author Ian Tsai
 *
 */
public class TabboxTag extends BranchTag {

    /**
	 * 
	 * @return the UI component name: "tabbox".
	 */
    protected String getJspTagName() {
        return "tabbox";
    }
}
