package com.ecmdeveloper.plugin.properties.input;

import org.eclipse.swt.widgets.Composite;
import com.ecmdeveloper.plugin.core.model.IPropertyDescription;
import com.ecmdeveloper.plugin.properties.renderer.BooleanInputRenderer;

/**
 * @author Ricardo.Belfor
 *
 */
public class BooleanPropertyInput extends PropertyInputBase {

    private BooleanInputRenderer inputRenderer;

    public BooleanPropertyInput(IPropertyDescription propertyDescription) {
        super(propertyDescription);
        String displayName = propertyDescription.getDisplayName();
        String descriptiveText = propertyDescription.getDescriptiveText();
        inputRenderer = new BooleanInputRenderer(displayName, descriptiveText);
    }

    @Override
    public void renderControls(Composite container, int numColumns) {
        inputRenderer.renderControls(container, numColumns);
    }

    @Override
    public void setValue(Object value) {
        if (value == null || value instanceof Boolean) {
            inputRenderer.setValue((Boolean) value);
        } else {
            throw new UnsupportedOperationException("Class '" + value.getClass().getName() + "' is not a Boolean");
        }
    }

    @Override
    public Object getValue() {
        return null;
    }
}
