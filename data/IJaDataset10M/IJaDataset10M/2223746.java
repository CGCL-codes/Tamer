package com.ecmdeveloper.plugin.properties.editors.details;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import com.ecmdeveloper.plugin.properties.editors.details.input.IntegerFormInput;

/**
 * @author Ricardo.Belfor
 *
 */
public class MultiValueIntegerDetailsPage extends BaseMultiValueDetailsPage {

    private IntegerFormInput integerInput;

    @Override
    protected void createInput(Composite client, FormToolkit toolkit) {
        integerInput = new IntegerFormInput(client, form);
    }

    @Override
    protected Object getInputValue() {
        return integerInput.getValue();
    }

    @Override
    protected void setInputValue(Object value) {
        integerInput.setValue((Integer) value);
    }
}
