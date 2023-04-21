package ch.sahits.model.internal.java.gui;

import java.util.Vector;
import ch.sahits.model.gui.IFormInputElement;
import ch.sahits.model.gui.IFormListBuilder;

/**
 * Builder for a form list
 * @author Andi Hotz, Sahits GmbH
 * @since 3.0.0
 */
public class FormListBuilder extends FormBaseListBuilder implements IFormListBuilder {

    /** List of preselected values */
    Vector<String> selected = new Vector<String>();

    /** Flag indicating if multiselect is allowed */
    boolean mutltiselect = true;

    /**
	 * Add a selected element
	 * @param elem
	 * @return this
	 */
    public IFormListBuilder addSelected(String elem) {
        selected.add(elem);
        return this;
    }

    /**
	 * Set the multi select flag
	 * @param multiselect
	 * @return this
	 */
    public IFormListBuilder multiselect(boolean multiselect) {
        this.mutltiselect = multiselect;
        return this;
    }

    /**
	 * Create a form list
	 * @see ch.sahits.model.internal.java.gui.FormInputElementBuilder#build()
	 */
    @Override
    public IFormInputElement build() {
        return new FormList(this);
    }
}
