package org.akrogen.tkui.grammars.xul.ui;

import org.akrogen.tkui.grammars.xul.ui.menus.IPopup;

public interface IXULControlElement extends IXULElement {

    public Boolean getDisabled();

    public void setDisabled(Boolean disabled);

    public void setHidden(Boolean hidden);

    public Boolean getHidden();

    public void setHeight(String height);

    public String getHeight();

    /**
	 * Set tooltip text of XUL control. Used to set the text which appears in
	 * the tooltip when the user moves the mouse over the element. This can be
	 * used instead of setting the tooltip to a popup for the common case where
	 * it contains only text. The tooltip is displayed in a default tooltip
	 * which displays only a label
	 * 
	 * @param tooltiptext
	 */
    public void setTooltiptext(String tooltiptext);

    /**
	 * Get tooltip text of XUL control. Used to set the text which appears in
	 * the tooltip when the user moves the mouse over the element. This can be
	 * used instead of setting the tooltip to a popup for the common case where
	 * it contains only text. The tooltip is displayed in a default tooltip
	 * which displays only a label
	 * 
	 * @return
	 */
    public String getTooltiptext();

    public void setWidth(String width);

    public String getWidth();

    public void setPopupMenu(IPopup popupMenu);
}
