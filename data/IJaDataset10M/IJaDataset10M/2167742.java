package org.objectwiz.core.facet.display.form;

import java.util.Collection;
import org.objectwiz.core.ui.component.UIComponent;
import com.google.common.collect.Multimap;

/**
 * Represents an edition form.
 *
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
public interface EditForm {

    /**
     * The title of this form. Must be localized.
     */
    public String getTitle();

    /**
     * The elements to display on the form.
     *
     * They hold the values.
     */
    public Collection<FormComponent> getComponentsToDisplay();

    /**
     * Shall validate the form.
     *
     * Data is submitted by updating the value on each component
     * ({@link UIComponent#setValue(Object)}.
     *
     * @return an empty map if the data submitted is valid, otherwise
     * a map of the validation problems. Values corresponding to null
     * key indicate a problem that is not specific to a component.
     */
    public Multimap<FormComponent, String> validate();
}
