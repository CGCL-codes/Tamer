package org.weblayouttag.tag.button;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.weblayouttag.tag.AbstractXhtmlTag;
import org.weblayouttag.tag.WebLayoutTagException;
import org.weblayouttag.tag.form.FormTag;

/**
 * @author Andy Marek
 * @version Jun 6, 2005
 * @jsp.tag
 * 	name="buttons"
 * 	body-content="JSP"
 * 	description="Used inside a &lt;form&gt; tag to include buttons."
 */
public class ButtonsTagImpl extends AbstractXhtmlTag implements ButtonsTag {

    public void checkValid() throws WebLayoutTagException {
        if (this.getFormTag() == null) {
            throw new WebLayoutTagException("The form tag could not be found.");
        }
    }

    public Element createElement() {
        return DocumentHelper.createElement("buttons");
    }

    protected FormTag getFormTag() {
        return (FormTag) PARENT_FINDER.findTag(this.getPageContext(), this, FormTag.class);
    }
}
