package org.zkoss.jsp.zul;

import org.zkoss.zul.Image;
import org.zkoss.jsp.zul.impl.BranchTag;

/**
 * The JSP tag to represent "Image" component in ZK's component definition.
 * @author Ian Tsai
 *
 */
public class ImageTag extends BranchTag {

    /**
	 * 
	 * @return the UI component name: "image".
	 */
    protected String getJspTagName() {
        return "image";
    }
}
