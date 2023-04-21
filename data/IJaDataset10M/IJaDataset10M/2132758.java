package org.progeeks.meta.swing.editor;

import java.util.*;
import javax.swing.*;
import org.progeeks.meta.*;
import org.progeeks.meta.swing.*;

/**
 *  Basic integer editor implementation.
 *
 *  @version   $Revision: 1.3 $
 *  @author    Paul Speed
 */
public class SpinnerEnumeratedTypeEditor extends AbstractSpinnerEditor {

    private Object emptyValue = null;

    private PropertyType lastType;

    public SpinnerEnumeratedTypeEditor(MetaPropertyContext viewContext) {
        super(viewContext);
    }

    /**
     *  Overridden so that we can properly configure the combo
     *  box model for the given type.  This editor works with
     *  any enumerated property type and so doesn't know this information
     *  up front.
     */
    public void setPropertyMutator(PropertyMutator mutator) {
        if (mutator != null) {
            EnumeratedPropertyType type = (EnumeratedPropertyType) mutator.getPropertyInfo().getPropertyType();
            if (type != lastType) {
                List values = type.getEnumeratedValues();
                JSpinner spinner = (JSpinner) getUIComponent();
                spinner.setModel(new SpinnerListModel(values));
                JFormattedTextField textField = getFormattedTextField();
                if (textField != null) {
                    textField.setEditable(false);
                }
                lastType = type;
            }
        }
        super.setPropertyMutator(mutator);
    }

    protected Object getEmptyValue() {
        return (emptyValue);
    }
}
