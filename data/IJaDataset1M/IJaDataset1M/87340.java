package ch.sahits.model.internal.java.gui;

import ch.sahits.model.gui.IFormCombo;
import ch.sahits.model.gui.IFormComboBuilder;

/**
 * Builder for the form combo
 * @author Andi Hotz, Sahits GmbH
 * @since 3.0.0
 */
public class FormComboBuilder extends FormBaseListBuilder implements IFormComboBuilder {

    /**
	 * Create a new combo
	 * @see ch.sahits.model.internal.java.gui.FormInputElementBuilder#build()
	 */
    @Override
    public IFormCombo build() {
        return new FormCombo(this);
    }
}
