package org.akrogen.tkui.grammars.xul.ui.groups;

import org.akrogen.tkui.grammars.xul.ui.IXULControlElement;

/**
 * Radio XUL interface. An element that can be turned on and off. Radio buttons
 * are almost always grouped together in groups. Only one radio button within
 * the same radiogroup may be selected at a time. The user can switch which
 * radio button is turned on by selecting it with the mouse or keyboard. Other
 * radio buttons in the same group are turned off. A label, specified with the
 * label attribute may be added beside the radio button.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 * @see http://developer.mozilla.org/en/docs/XUL:radio
 */
public interface IRadio extends IXULControlElement {

    public Boolean getSelected();

    public void setSelected(Boolean selected);

    public String getLabel();

    public void setLabel(String label);
}
