package glowaxes.tags;

import java.util.HashMap;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import org.apache.log4j.Logger;

/**
 * The Class LabelsBehaviorTag.
 * 
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 * @version $Id: LabelsBehaviorTag.java 202 2009-07-16 20:41:35Z nejoom $
 */
public class LabelsBehaviorTag extends AbstractBodyTag {

    /** The logger. */
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(LabelsBehaviorTag.class.getName());

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1731548815061564185L;

    /** The labels attributes. */
    private HashMap<String, String> labelsAttributes = new HashMap<String, String>();

    /**
     * Instantiates a new labels tag.
     */
    public LabelsBehaviorTag() {
        super();
    }

    @Override
    public int doAfterBody() throws JspException {
        BodyContent body = getBodyContent();
        if (body != null) {
            String text = body.getString();
            body.clearBody();
            setText(text);
        }
        return EVAL_PAGE;
    }

    @Override
    public int doEndTag() throws JspTagException {
        DataTag dataTag = (DataTag) findAncestorWithClass(this, DataTag.class);
        if (dataTag == null) {
            throw new JspTagException("&lt;ga:label> tag must be within a &lt;ga:data> tag");
        } else {
            dataTag.setAttribute("labels", labelsAttributes);
        }
        labelsAttributes = new HashMap<String, String>();
        return EVAL_PAGE;
    }

    @Override
    public int doStartTag() throws JspTagException {
        return EVAL_BODY_BUFFERED;
    }

    /**
     * Sets the popup.
     * 
     * @param popup
     *            The popup of the labels [true|false|smartOverlayTM]
     */
    public void setPopup(String popup) {
        labelsAttributes.put("popup", popup);
    }

    /**
     * Sets the title, the labels text.
     * 
     * @param text
     *            the text
     */
    public void setText(String text) {
        if (text != null) text = text.trim();
        labelsAttributes.put("text", text);
    }

    public void setType(String type) {
        labelsAttributes.put("type", type);
    }
}
