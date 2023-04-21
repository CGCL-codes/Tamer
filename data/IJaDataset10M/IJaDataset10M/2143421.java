package org.springframework.richclient.binding.form;

import org.springframework.binding.form.FormModel;

/**
 * A binding from a visual control to a form model's field. 
 * 
 * @author Oliver Hutchison
 */
public interface Binding {

    /**
     * Returns the form model that this binding is for.
     */
    FormModel getFormModel();

    /**
     * Returns the field that this binding is for.
     */
    String getField();

    /**
     * Returns the visual control that is bound to the form model's field. 
     */
    Object getControl();

    /**
     * Should be called when a binding is no longer in use. Generally this call will cause 
     * the binding to release any resources allocated and unregister any listeners 
     * registered when the binding was created.
     */
    void disposeBinding();
}
