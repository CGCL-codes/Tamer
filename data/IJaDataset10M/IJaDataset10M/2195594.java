package de.uni_leipzig.lots.jsp.tag.core;

import de.uni_leipzig.lots.jsp.tag.AbstractTag;
import org.apache.struts.taglib.TagUtils;
import javax.servlet.jsp.JspException;

/**
 * @author Alexander Kiel
 * @version $Id: IfXHTMLTag.java,v 1.8 2007/10/23 06:29:17 mai99bxd Exp $
 */
public class IfXHTMLTag extends AbstractTag {

    @Override
    public int doStartTag() throws JspException {
        if (TagUtils.getInstance().isXhtml(pageContext)) {
            return EVAL_BODY_INCLUDE;
        } else {
            return SKIP_BODY;
        }
    }
}
