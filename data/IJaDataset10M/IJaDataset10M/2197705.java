package org.mcisb.ui.wizard;

import org.mcisb.ui.util.*;
import org.mcisb.util.*;

/**
 * 
 * @author Neil Swainston
 */
public class ChooserWizardComponent extends WizardComponent {

    /**
	 * 
	 * @param bean
	 * @param parameterPanel
	 */
    public ChooserWizardComponent(GenericBean bean, ParameterPanel parameterPanel) {
        super(bean, parameterPanel);
    }

    @Override
    public void update() throws Exception {
        bean.setProperty(PropertyNames.CHOOSER_OBJECTS, ((Chooser) parameterPanel).getSelection());
    }
}
