package salto.fwk.mvc.taglib.combobox;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author Manuel
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ComboboxOptionTag extends BodyTagSupport {

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
    private static final long serialVersionUID = 3256718472741206322L;

    private String value = "";

    public ComboboxOptionTag() {
        super();
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() throws JspException {
        try {
            BodyContent theBodyContent = getBodyContent();
            String content = "";
            if (bodyContent != null) {
                content = theBodyContent.getString();
                theBodyContent.clearBody();
            }
            ComboboxTag comboboxTag = (ComboboxTag) findAncestorWithClass(this, ComboboxTag.class);
            if (comboboxTag == null) {
                throw new JspException("An comboboxoption tag must be nested within a combobox tag.");
            }
            ComboboxOption comboboxOption = new ComboboxOption(value, content);
            comboboxTag.addOptionItem(comboboxOption);
            return SKIP_BODY;
        } finally {
            this.release();
        }
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#release()
     */
    public void release() {
        this.cleanup();
        super.release();
    }

    /**
     * 
     */
    private void cleanup() {
        this.value = "";
    }

    /**
	 * @param value The value to set.
	 */
    public void setValue(String value) {
        this.value = value;
    }
}
