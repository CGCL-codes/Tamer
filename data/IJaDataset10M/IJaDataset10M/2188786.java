package org.mayo.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Acts as a base for tags which support style, onclick etc etc...
 * @author Chris Corbyn <chris@w3style.co.uk>
 */
public abstract class InteractiveTag extends TagSupport {

    /** The onClick attribute */
    private String onClick;

    /** The onMouseover attribute */
    private String onMouseover;

    /** The onMouseout attribute */
    private String onMouseout;

    /** The onMousedown attribute */
    private String onMousedown;

    /** The onMouseup attribute */
    private String onMouseup;

    /** The onMousemove attribute */
    private String onMousemove;

    /** The onKeypress attribute */
    private String onKeypress;

    /** The onKeydown attribute */
    private String onKeydown;

    /** The onKeyup attribute */
    private String onKeyup;

    /** The onFocus attribute */
    private String onFocus;

    /** The onBlur attribute */
    private String onBlur;

    /** The style attribute */
    private String style;

    /** The tabIndex value */
    private Integer tabIndex;

    /**
   * Set the onClick attribute.
   * @param String onClick
   */
    public void setOnClick(String onClick) {
        this.onClick = onClick;
    }

    /**
   * Get the onClick attribute.
   * @return String
   */
    public String getOnClick() {
        return onClick;
    }

    /**
   * Set the onMouseover attribute.
   * @param String onMouseover
   */
    public void setOnMouseover(String onMouseover) {
        this.onMouseover = onMouseover;
    }

    /**
   * Get the onMouseover attribute.
   * @return String
   */
    public String getOnMouseover() {
        return onMouseover;
    }

    /**
   * Set the onMouseout attribute.
   * @param String onMouseout
   */
    public void setOnMouseout(String onMouseout) {
        this.onMouseout = onMouseout;
    }

    /**
   * Get the onMouseout attribute.
   * @return String
   */
    public String getOnMouseout() {
        return onMouseout;
    }

    /**
   * Set the onMousedown attribute.
   * @param String onMousedown
   */
    public void setOnMousedown(String onMousedown) {
        this.onMousedown = onMousedown;
    }

    /**
   * Get the onMousedown attribute.
   * @return String
   */
    public String getOnMousedown() {
        return onMousedown;
    }

    /**
   * Set the onMouseup attribute.
   * @param String onMouseup
   */
    public void setOnMouseup(String onMouseup) {
        this.onMouseup = onMouseup;
    }

    /**
   * Get the onMouseup attribute.
   * @return String
   */
    public String getOnMouseup() {
        return onMouseup;
    }

    /**
   * Set the onMousemove attribute.
   * @param String onMousemove
   */
    public void setOnMousemove(String onMousemove) {
        this.onMousemove = onMousemove;
    }

    /**
   * Get the onMousemove attribute.
   * @return String
   */
    public String getOnMousemove() {
        return onMousemove;
    }

    /**
   * Set the onKeypress attribute.
   * @param String onKeypress
   */
    public void setOnKeypress(String onKeypress) {
        this.onKeypress = onKeypress;
    }

    /**
   * Get the onKeypress attribute.
   * @return String
   */
    public String getOnKeypress() {
        return onKeypress;
    }

    /**
   * Set the onKeydown attribute.
   * @param String onKeydown
   */
    public void setOnKeydown(String onKeydown) {
        this.onKeydown = onKeydown;
    }

    /**
   * Get the onKeydown attribute.
   * @return String
   */
    public String getOnKeydown() {
        return onKeydown;
    }

    /**
   * Set the onKeyup attribute.
   * @param String onKeyup
   */
    public void setOnKeyup(String onKeyup) {
        this.onKeyup = onKeyup;
    }

    /**
   * Get the onKeyup attribute.
   * @return String
   */
    public String getOnKeyup() {
        return onKeyup;
    }

    /**
   * Set the onFocus attribute.
   * @param String onFocus
   */
    public void setOnFocus(String onFocus) {
        this.onFocus = onFocus;
    }

    /**
   * Get the onFocus attribute.
   * @return String
   */
    public String getOnFocus() {
        return onFocus;
    }

    /**
   * Set the onBlur attribute.
   * @param String onBlur
   */
    public void setOnBlur(String onBlur) {
        this.onBlur = onBlur;
    }

    /**
   * Get the onBlur attribute.
   * @return String
   */
    public String getOnBlur() {
        return onBlur;
    }

    /**
   * Set the style attribute.
   * @param String style
   */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
   * Get the style attribute.
   * @return String
   */
    public String getStyle() {
        return style;
    }

    /**
   * Set the tabIndex attribute.
   * @param Integer tabIndex
   */
    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }

    /**
   * Get the tabIndex attribute.
   * @return Integer
   */
    public Integer getTabIndex() {
        return tabIndex;
    }

    /**
   * Get a formatted string with the attributes provided in it.
   * @return String
   * @throws JspException
   */
    protected String getAttributeString() throws JspException {
        StringBuffer buf = new StringBuffer();
        String onClick = getOnClick();
        if (onClick != null) {
            buf.append(" onclick=\"" + onClick + "\"");
        }
        String onMouseover = getOnMouseover();
        if (onMouseover != null) {
            buf.append(" onmouseover=\"" + onMouseover + "\"");
        }
        String onMouseout = getOnMouseout();
        if (onMouseout != null) {
            buf.append(" onmouseout=\"" + onMouseout + "\"");
        }
        String onMousedown = getOnMousedown();
        if (onMousedown != null) {
            buf.append(" onmousedown=\"" + onMousedown + "\"");
        }
        String onMouseup = getOnMouseup();
        if (onMouseup != null) {
            buf.append(" onmouseup=\"" + onMouseup + "\"");
        }
        String onMousemove = getOnMousemove();
        if (onMousemove != null) {
            buf.append(" onmousemove=\"" + onMousemove + "\"");
        }
        String onKeypress = getOnKeypress();
        if (onKeypress != null) {
            buf.append(" onkeypress=\"" + onKeypress + "\"");
        }
        String onKeydown = getOnKeydown();
        if (onKeydown != null) {
            buf.append(" onkeydown=\"" + onKeydown + "\"");
        }
        String onKeyup = getOnKeyup();
        if (onKeyup != null) {
            buf.append(" onkeyup=\"" + onKeyup + "\"");
        }
        String onFocus = getOnFocus();
        if (onFocus != null) {
            buf.append(" onfocus=\"" + onFocus + "\"");
        }
        String onBlur = getOnBlur();
        if (onBlur != null) {
            buf.append(" onblur=\"" + onBlur + "\"");
        }
        String style = getStyle();
        if (style != null) {
            buf.append(" style=\"" + style + "\"");
        }
        Integer tabIndex = getTabIndex();
        if (tabIndex != null) {
            buf.append(" tabIndex=\"" + tabIndex + "\"");
        }
        return buf.toString();
    }
}
