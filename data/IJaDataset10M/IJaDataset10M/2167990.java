package com.sokolov.portal.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import java.io.IOException;

/**
 * Container tag for Ajax portal. It's based on &lt;DIV&gt; HTML tag.
 *
 * @author Sergei Sokolov
 * @version 1.0
 */
public class Container extends AbstractPortalTag {

    /** Holds CSS class for NoDecoration state */
    public static final String NODECORATION = "nodecoration";

    /** Holds CSS class for Window state */
    public static final String WINDOW = "window";

    /** Holds CSS class for Standard state */
    public static final String STANDARD = "standard";

    /** Holds CSS class for Accordion state */
    public static final String ACCORDION = "accordion";

    /** Holds CSS class for TabPanel state */
    public static final String TABS = "tabs";

    /** Holds dropTarget property (Drag & Drop target) */
    private String dropTarget = null;

    /** Hols minWidth property */
    private String minWidth = null;

    /**
     * Correct state of the container.
     */
    protected void correctState() {
        if ((getState() == null) || ("".equals(getState()))) {
            setState(STANDARD);
        } else if (!(NODECORATION.equals(getState().trim()) || WINDOW.equals(getState().trim()) || STANDARD.equals(getState().trim()) || ACCORDION.equals(getState().trim()) || TABS.equals(getState().trim()))) {
            setState(STANDARD);
        }
    }

    /**
     * Return div with defined width.
     *
     * @param width a minimal width of container
     * @return html code of div tag
     */
    protected String getDivWithFixedWidth(String width) {
        return "<div style='width: " + width + "; font-size: 0px; height: 0px;'></div>";
    }

    /**
     * Process component rendering.
     *
     * @return SKIP_BODY
     * @throws JspException if error occurs
     */
    public int doAfterBody() throws JspException {
        try {
            BodyContent bodycontent = getBodyContent();
            String body = bodycontent.getString();
            JspWriter out = bodycontent.getEnclosingWriter();
            correctState();
            String classValue = " class='" + getState() + ((convertToBoolean(getDropTarget())) ? " dropTarget" : "") + ((getStyleClass() != null) ? " " + getStyleClass() : "") + "'";
            if (body != null) {
                out.print("<div id='" + getId() + "'" + (((getStyle() != null) || (getIcon() != null)) ? " style='" + ((getStyle() != null) ? getStyle() + " " : "") + "'" : "") + classValue + ((getTitle() != null) ? " title='" + getTitle() + "'" : "") + ">");
                if (getMinWidth() != null) {
                    out.print(getDivWithFixedWidth(getMinWidth()));
                }
                out.print(body);
                out.print("</div>");
            }
        } catch (IOException ioe) {
            throw new JspException("Error:" + ioe.getMessage());
        }
        return SKIP_BODY;
    }

    /**
     * Getter of dropTarget property.
     *
     * @return a value of dropTarget property
     */
    public String getDropTarget() {
        return dropTarget;
    }

    /**
     * Setter of dropTarget property.
     *
     * @param dropTarget a new value of titleEditable property
     */
    public void setDropTarget(String dropTarget) {
        this.dropTarget = dropTarget;
    }

    /**
     * Getter of minWidth property.
     *
     * @return a value of minWidth property
     */
    public String getMinWidth() {
        return minWidth;
    }

    /**
     * Setter of minWidth property.
     *
     * @param minWidth a new value of minWidth property
     */
    public void setMinWidth(String minWidth) {
        this.minWidth = minWidth;
    }
}
