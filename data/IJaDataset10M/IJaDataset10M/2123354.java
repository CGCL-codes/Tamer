package ch.sahits.model.internal.java.gui;

import ch.sahits.model.gui.IFormCheckbox;
import ch.sahits.model.gui.IFormGroup;
import ch.sahits.model.gui.IFormRadioButton;
import ch.sahits.model.gui.IFormRadioButtonBuilder;

/**
 * Builder for a radio button
 * @author Andi Hotz, Sahits GmbH
 * @since 3.0.0
 */
public class FormRadioButtonBuilder extends FormCheckboxBuilder implements IFormRadioButtonBuilder {

    /**
	 * Create a new instance of a radio button
	 * @see ch.sahits.model.internal.java.gui.FormCheckboxBuilder#build()
	 */
    @Override
    public IFormRadioButton build() {
        validate();
        return new FormRadioButton(this);
    }

    /**
	 * Validate
	 */
    private void validate() {
    }
}
