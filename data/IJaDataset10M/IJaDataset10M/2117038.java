package com.acarter.scenemonitor.propertydescriptor.definitions.renderstate;

import com.acarter.propertytable.Property;
import com.acarter.propertytable.PropertySection;
import com.acarter.propertytable.PropertySectionState;

/**
 * @author Carter
 * 
 */
public class StencilStatePropertyPage extends RenderStatePropertyPage {

    /**
     * 
     */
    public StencilStatePropertyPage() {
        PropertySection section = new PropertySection("Stencil State");
        section.addProperty(new Property("No Properties", null));
        section.setState(PropertySectionState.EXPANDED);
        model.addPropertySection(0, section);
    }
}
