package org.plazmaforge.studio.reportdesigner.parts.properties.descriptors;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.plazmaforge.studio.reportdesigner.parts.properties.providers.ComboLabelProvider;

public class BooleanPropertyDescriptor extends ComboBoxPropertyDescriptor {

    private static final String labelsArray[] = { "false", "true" };

    public BooleanPropertyDescriptor(Object id, String displayName) {
        super(id, displayName, labelsArray);
        setLabelProvider(new ComboLabelProvider(labelsArray));
    }
}
