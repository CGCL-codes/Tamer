package org.akrogen.tkui.xul.dom;

/**
 * <p>
 * XUL control interface.
 * </p>
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 */
public interface XULControlElement extends XULElement {

    String DISABLED_ATTR = "disabled";

    String MINWIDTH_ATTR = "minwidth";

    String WIDTH_ATTR = "width";

    String MAXWIDTH_ATTR = "maxwidth";

    String MINHEIGHT_ATTR = "minheight";

    String HEIGHT_ATTR = "height";

    String MAXHEIGHT_ATTR = "maxheight";

    String TOOLTIPTEXT_ATTR = "tooltiptext";

    String COLLAPSED_ATTR = "collapsed";

    String CONTEXT_ATTR = "context";

    String BGCOLOR_ATTR = "bgColor";

    /**
	 * Get the disabled attribute.Indicates whether the element is disabled or
	 * not. If this element is set to true the element is disabled. Disabled
	 * elements are usually drawn with grayed-out text. If the element is
	 * disabled, it does not respond to user actions, it cannot be focused, and
	 * the command event will not fire.
	 * 
	 * @return
	 */
    boolean isDisabled();

    /**
	 * Set the disabled attribute.Indicates whether the element is disabled or
	 * not. If this element is set to true the element is disabled. Disabled
	 * elements are usually drawn with grayed-out text. If the element is
	 * disabled, it does not respond to user actions, it cannot be focused, and
	 * the command event will not fire.
	 * 
	 * @param disabled
	 */
    void setDisabled(boolean disabled);

    /**
	 * Gets the value of the minwidth attribute. The minimum width of the
	 * element. This corresponds to the min-width CSS property.
	 * 
	 * @return
	 */
    String getMinWidth();

    /**
	 * Sets the value of the minwidth attribute. The minimum width of the
	 * element. This corresponds to the min-width CSS property.
	 * 
	 * @param minWidth
	 */
    void setMinWidth(String minWidth);

    /**
	 * Get the width attribute. The preferred width of the element. The value
	 * should not include a unit as all values are in pixels. The actual
	 * displayed width may be different if the element or its contents have a
	 * minimum or maximum width, or the size is adjusted by the flexibility or
	 * alignment of its parent. The CSS width property may also be used.
	 * 
	 * @return
	 */
    String getWidth();

    /**
	 * Set the width attribute. The preferred width of the element. The value
	 * should not include a unit as all values are in pixels. The actual
	 * displayed width may be different if the element or its contents have a
	 * minimum or maximum width, or the size is adjusted by the flexibility or
	 * alignment of its parent. The CSS width property may also be used.
	 * 
	 * @param width
	 */
    void setWidth(String width);

    /**
	 * Gets the value of the maxwidth attribute. The maximum width of the
	 * element. This corresponds to the max-width CSS property.
	 * 
	 * @return
	 */
    String getMaxWidth();

    /**
	 * Sets the value of the maxwidth attribute. The maximum width of the
	 * element. This corresponds to the max-width CSS property.
	 * 
	 * @param minWidth
	 */
    void setMaxWidth(String maxWidth);

    /**
	 * Gets the value of the minheight attribute. The minimum height of the
	 * element. This corresponds to the min-height CSS property.
	 * 
	 * @return
	 */
    String getMinHeight();

    /**
	 * Sets the value of the minheight attribute. The minimum height of the
	 * element. This corresponds to the min-height CSS property.
	 * 
	 * @param minWidth
	 */
    void setMinHeight(String minHeight);

    /**
	 * Get the height attribute. The preferred height of the element in pixels.
	 * The actual displayed height may be different if the element or its
	 * contents have a minimum or maximum height. The CSS height property may
	 * also be used.
	 * 
	 * @return
	 */
    String getHeight();

    /**
	 * Set the height attribute. The preferred height of the element in pixels.
	 * The actual displayed height may be different if the element or its
	 * contents have a minimum or maximum height. The CSS height property may
	 * also be used.
	 * 
	 * @param height
	 */
    void setHeight(String height);

    /**
	 * Gets the value of the maxheight attribute. The maximum height of the
	 * element. This corresponds to the max-height CSS property.
	 * 
	 * @return
	 */
    String getMaxHeight();

    /**
	 * Sets the value of the maxheight attribute. The maximum height of the
	 * element. This corresponds to the max-height CSS property.
	 * 
	 * @param minHeight
	 */
    void setMaxHeight(String maxHeight);

    /**
	 * Get the tooltiptext attribute. Used to set the text which appears in the
	 * tooltip when the user moves the mouse over the element. This can be used
	 * instead of setting the tooltip to a popup for the common case where it
	 * contains only text. The tooltip is displayed in a default tooltip which
	 * displays only a label, however the default tooltip may be changed by
	 * setting the default attribute on a tooltip element.
	 * 
	 * @return
	 */
    String getTooltiptext();

    /**
	 * Set the tooltiptext attribute. Used to set the text which appears in the
	 * tooltip when the user moves the mouse over the element. This can be used
	 * instead of setting the tooltip to a popup for the common case where it
	 * contains only text. The tooltip is displayed in a default tooltip which
	 * displays only a label, however the default tooltip may be changed by
	 * setting the default attribute on a tooltip element.
	 * 
	 * @param tooltiptext
	 */
    void setTooltiptext(String tooltiptext);
}
