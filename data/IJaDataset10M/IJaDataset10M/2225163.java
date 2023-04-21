package org.gnomekr.potron.web.tag;

import javax.faces.component.UIComponent;
import org.apache.myfaces.taglib.html.HtmlOutputTextTag;

/**
 * TruncateOutputTextTag.java
 * @author Xavier Cho
 * @version $Revision 1.1 $ $Date: 2005/07/19 18:01:50 $
 * @jsp:tag
 *      name="truncateOutputText"
 *      body-content="JSP"
 */
public class TruncateOutputTextTag extends HtmlOutputTextTag {

    public static final String RENDERER_TYPE = "TruncateOutputText";

    private String value;

    private String escape;

    private String upper;

    private String lower;

    private String appendEnd;

    /**
     * @return Returns the appendEnd.
     * @jsp.attribute
     *      required = "false"
     *      rtexprvalue = "true"
     */
    public String getAppendEnd() {
        return appendEnd;
    }

    /**
     * @param appendEnd The appendEnd to set.
     */
    public void setAppendEnd(String appendEnd) {
        this.appendEnd = appendEnd;
    }

    /**
     * @return Returns the lower.
     * @jsp.attribute
     *      required = "false"
     *      rtexprvalue = "true"
     */
    public String getLower() {
        return lower;
    }

    /**
     * @param lower The lower to set.
     */
    public void setLower(String lower) {
        this.lower = lower;
    }

    /**
     * @return Returns the upper.
     * @jsp.attribute
     *      required = "true"
     *      rtexprvalue = "true"
     */
    public String getUpper() {
        return upper;
    }

    /**
     * @param upper The upper to set.
     */
    public void setUpper(String upper) {
        this.upper = upper;
    }

    /**
     * @return Returns the value.
     * @jsp.attribute
     *      required = "true"
     *      rtexprvalue = "true"
     */
    public String getValue() {
        return value;
    }

    /**
     * @see org.apache.myfaces.taglib.UIComponentTagBase#setValue(java.lang.String)
     */
    public void setValue(String value) {
        this.value = value;
        super.setValue(value);
    }

    /**
     * @return Returns the escape.
     * @jsp.attribute
     *      required = "false"
     *      rtexprvalue = "true"
     */
    public String getEscape() {
        return escape;
    }

    /**
     * @see org.apache.myfaces.taglib.html.HtmlOutputTextTagBase#setEscape(java.lang.String)
     */
    public void setEscape(String escape) {
        this.escape = escape;
        super.setEscape(escape);
    }

    /**
     * @see javax.faces.webapp.UIComponentTag#setProperties(javax.faces.component.UIComponent)
     */
    @SuppressWarnings("unchecked")
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        if (appendEnd != null) {
            if (isValueReference(appendEnd)) {
                component.setValueBinding("appendEnd", getFacesContext().getApplication().createValueBinding(appendEnd));
            } else {
                component.getAttributes().put("appendEnd", appendEnd);
            }
        }
        if (upper != null) {
            if (isValueReference(upper)) {
                component.setValueBinding("upper", getFacesContext().getApplication().createValueBinding(upper));
            } else {
                component.getAttributes().put("upper", Integer.parseInt(upper));
            }
        }
        if (lower != null) {
            if (isValueReference(lower)) {
                component.setValueBinding("lower", getFacesContext().getApplication().createValueBinding(lower));
            } else {
                component.getAttributes().put("lower", Integer.parseInt(lower));
            }
        }
    }

    /**
     * @see org.apache.myfaces.taglib.core.VerbatimTag#getRendererType()
     */
    @Override
    public String getRendererType() {
        return RENDERER_TYPE;
    }
}
