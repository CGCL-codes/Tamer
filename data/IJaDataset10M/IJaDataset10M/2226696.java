package net.sf.uitags.tag.optionTransfer;

import javax.servlet.jsp.JspException;
import net.sf.uitags.tag.AbstractUiTag;
import net.sf.uitags.tagutil.validation.RuntimeValidator;
import net.sf.uitags.util.Template;

/**
 * Injects the functionality to return selected items in the target
 * box to the source box of the option transfer suite.
 *
 * @author hgani
 * @version $Id$
 */
public class ReturnTag extends AbstractUiTag {

    /**
   * Serial Version UID.
   */
    private static final long serialVersionUID = 100L;

    /**
   * The 'injectTo' tag attribute.
   */
    private String injectTo;

    /**
   * The 'injectToName' tag attribute.
   */
    private String injectToName;

    /**
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
    public void setInjectTo(String val) {
        this.injectTo = val;
    }

    /**
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
    public void setInjectToName(String val) {
        this.injectToName = val;
    }

    /**
   * Prints the necessary Javascript code.
   *
   * @see javax.servlet.jsp.tagext.Tag#doStartTag()
   * @return <code>SKIP_BODY</code>
   * @throws JspException to communicate error
   */
    public int doStartTag() throws JspException {
        RuntimeValidator.assertAttributeExclusive("injectTo", this.injectTo, "injectToName", this.injectToName);
        RuntimeValidator.assertEitherSpecified("injectTo", this.injectTo, "injectToName", this.injectToName);
        Template tpl = Template.forName(Template.OPTION_TRANSFER_RETURN);
        tpl.map("triggerId", this.injectTo);
        tpl.map("triggerName", this.injectToName);
        OptionTransferTag parent = (OptionTransferTag) findParent(OptionTransferTag.class);
        parent.addChildJsCode(tpl.evalToString());
        return SKIP_BODY;
    }
}
