package org.akrogen.tkui.grammars.xul.ui.menus;

import org.akrogen.tkui.grammars.xul.ui.IXULElement;

public interface IMenuitem extends IXULElement {

    public Boolean getChecked();

    public void setChecked(Boolean checked);

    public Boolean getDisabled();

    /**
	 * Set the disabled property.
	 * 
	 * @param disabled
	 */
    public void setDisabled(Boolean disabled);

    public String getLabel();

    public void setLabel(String label);

    public Boolean getSelected();

    /**
	 * The selected attribute is readonly. 
	 * 
	 * @param selected
	 */
    public void setSelected(Boolean selected);
}
