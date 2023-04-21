package net.sf.uitags.tag.formGuide;

import javax.servlet.jsp.JspException;

/**
 * Notifies {@link net.sf.uitags.tag.formGuide.FormGuideTag} of widgets to observe.
 *
 * @author jonni
 * @version $Id$
 */
public class ObserveTag extends ObserveForNullTag {

    /**
   * Serial Version UID.
   */
    private static final long serialVersionUID = 100L;

    /**
   * The "forValue" tag attribute
   */
    private String forValue;

    /**
   * Default constructor.
   */
    public ObserveTag() {
        super();
    }

    /**
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
    public void setForValue(String val) {
        this.forValue = val;
    }

    /**
   * Communicates with the parent tag ({@link FormGuideTag}).
   *
   * @return <code>EVAL_PAGE</code>
   * @throws JspException to communicate error
   */
    protected final void doEndTagWithoutRuntimeValidation() {
        FormGuideTag formGuideTag = (FormGuideTag) findParent(FormGuideTag.class);
        if (this.elementId != null) {
            formGuideTag.addObservedElementId(this.elementId, this.forValue);
        } else if (this.elementName != null) {
            formGuideTag.addObservedElementName(this.elementName, this.forValue);
        }
    }
}
