package org.zkoss.jsp.zul;

import org.zkoss.zul.Row;
import org.zkoss.jsp.zul.impl.BranchTag;

/**
 * The JSP tag to represent "Row" component in ZK's component definition.
 * @author Ian Tsai
 *
 */
public class RowTag extends BranchTag {

    /**
	 * 
	 * @return the UI component name: "row".
	 */
    protected String getJspTagName() {
        return "row";
    }
}
