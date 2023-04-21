package com.halware.nakedide.eclipse.ext.annot.strprop;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import com.halware.nakedide.eclipse.ext.annot.common.AbstractNodeLabelProvider;
import com.halware.nakedide.eclipse.ext.annot.mdd.MetadataDescriptorSet;
import com.halware.nakedide.eclipse.ext.annot.prop.NakedObjectProperty;

public class NakedObjectStringPropertiesLabelProvider extends AbstractNodeLabelProvider<NakedObjectProperty> {

    private static final Logger LOGGER = Logger.getLogger(NakedObjectStringPropertiesLabelProvider.class);

    public Logger getLOGGER() {
        return LOGGER;
    }

    private static Color inactiveBackground;

    public NakedObjectStringPropertiesLabelProvider(MetadataDescriptorSet metadataDescriptorSet) {
        super(metadataDescriptorSet);
        if (inactiveBackground == null) {
            Display display = Display.getCurrent();
            inactiveBackground = display.getSystemColor(SWT.COLOR_GRAY);
        }
    }

    protected String doGetText(NakedObjectProperty nop) {
        return nop.getAccessorMethodName();
    }

    public Color doGetBackground(NakedObjectProperty nop) {
        if (!nop.isStringType()) {
            return inactiveBackground;
        }
        return null;
    }
}
