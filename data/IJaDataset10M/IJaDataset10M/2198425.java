package org.orbeon.faces.renderkit.xml;

import javax.faces.component.UIComponent;

/**
 * Renderer for a UISelectMany Checkbox.
 */
public class SelectmanyCheckboxRenderer extends SelectmanySelectoneMenuRenderer {

    public SelectmanyCheckboxRenderer() {
    }

    protected String getElementName(UIComponent component) {
        return "selectmany_checkbox";
    }
}
