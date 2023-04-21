package org.springframework.richclient.form;

import java.beans.PropertyEditor;
import org.springframework.binding.form.FormModel;

/**
 * Interface to be implemented by property editors that wish to be aware 
 * of their owning form model and of the name of the property they are editing.
 * 
 * @author Oliver Hutchison
 */
public interface FormAwarePropertyEditor extends PropertyEditor {

    /**
     * Callback that supplies the owning form model and property name that 
     * this property editor is responsible for editing.
     * <p>
     * This method must be called before the custom editor is requested via the 
     * <code>getCustomEditor</code> method. 
     * 
     * @param formModel 
     *      the owning form model
     * @param propertyName 
     *      the name of the property which this property editor 
     *      is responsible for editing
     */
    void setFormDetails(FormModel formModel, String propertyName);
}
