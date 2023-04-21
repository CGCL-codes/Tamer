package org.ujac.web.tag;

import javax.servlet.jsp.JspException;
import org.ujac.util.table.LayoutHints;

/**
 * Name: InsertColumnTag<br>
 * Description: A custom tag altering columns at surrounding print-table tags.
 * 
 * @author lauerc
 */
public class AlterColumnTag extends BaseTag {

    /** The serial version UID. */
    private static final long serialVersionUID = 4048791286360454709L;

    /** The column name. */
    private String name;

    /** The column width. */
    private String width;

    /** The column title. */
    private String title;

    /** The horizontal alignment. */
    private String halign;

    /**
   * Getter method for the the property halign.
   * @return The current value of property halign.
   */
    public String getHalign() {
        return halign;
    }

    /**
   * Setter method for the the property halign.
   * @param halign The value to set for the property halign.
   */
    public void setHalign(String halign) {
        this.halign = halign.toLowerCase();
    }

    /**
   * Getter method for the the property name.
   * @return The current value of property name.
   */
    public String getName() {
        return name;
    }

    /**
   * Setter method for the the property name.
   * @param name The value to set for the property name.
   */
    public void setName(String name) {
        this.name = name;
    }

    /**
   * Getter method for the the property width.
   * @return The current value of property width.
   */
    public String getWidth() {
        return width;
    }

    /**
   * Setter method for the the property width.
   * @param width The value to set for the property width.
   */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
   * Getter method for the the property title.
   * @return The current value of property title.
   */
    public String getTitle() {
        return title;
    }

    /**
   * Setter method for the the property title.
   * @param title The value to set for the property title.
   */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
   * @see javax.servlet.jsp.tagext.Tag#doStartTag()
   */
    public int doStartTag() throws JspException {
        PrintTableTag printTableTag = null;
        try {
            printTableTag = (PrintTableTag) getParent(PrintTableTag.class);
        } catch (ClassCastException ex) {
            throw new JspException("Parent tag of row-cell tag has to be print-row tag.");
        }
        if (printTableTag.isOutputStarted()) {
            return EVAL_BODY_INCLUDE;
        }
        float width = -1.0F;
        if (this.width != null) {
            width = Float.parseFloat(this.width);
        }
        int hAlign = -1;
        if (this.halign != null) {
            if ("left".equals(this.halign)) {
                hAlign = LayoutHints.LEFT;
            } else if ("right".equals(this.halign)) {
                hAlign = LayoutHints.RIGHT;
            } else if ("center".equals(this.halign)) {
                hAlign = LayoutHints.CENTER;
            } else {
                throw new JspException("Unsupported alignment type '" + this.halign + "'.");
            }
        }
        printTableTag.alterColumn(name, width, hAlign, title);
        return EVAL_BODY_INCLUDE;
    }

    /**
   * @see javax.servlet.jsp.tagext.BodyTagSupport#release()
   */
    public void release() {
        super.release();
        this.name = null;
        this.width = null;
        this.halign = null;
        this.title = null;
    }
}
