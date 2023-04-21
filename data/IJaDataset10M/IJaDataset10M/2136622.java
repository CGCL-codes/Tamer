package com.incendiaryblue.forms.listeners;

import com.incendiaryblue.forms.Form;
import com.incendiaryblue.forms.FormSubmissionException;
import com.incendiaryblue.forms.SubmissionListener;
import java.sql.*;

/**
 * Integrated into the com.incendiaryblue.forms package with some modifications.
 * Apr2002/vijayan
 * 
 * Provides an abstract class that defines method for a SubmissionListener
 * to save form data to a database when the onSubmit() event is fired. Subclass
 * should override to the store(form) method to provide custom storing.
 *
 * @author  William Lee
 * @version
 */
public abstract class DatabaseSubmissionListener implements SubmissionListener {

    /** Called when form is being submitted.
	 *
	 * @param form The form object being submitted, with all fields validated.
	 */
    public void onSubmit(Form form) throws FormSubmissionException {
        if (form.validateAllFields()) {
            System.err.println("onSubmit() in DatabaseSubmissionListener: All fields valid..");
            store(form);
        } else {
            throw new FormSubmissionException("Database submission aborted due to invalid form-field content.");
        }
    }

    /**
	 *  This method is called once all all form fields have been validated;
	 *  Implementing subclasses provide code for storing this data into
	 *  any arbitrary database system.
	 *  
	 *  @param  form    The submitted form object, with all fields validated.
	 */
    protected abstract void store(Form form) throws FormSubmissionException;
}
