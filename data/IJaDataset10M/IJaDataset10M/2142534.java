package org.plazmaforge.framework.client.swing.forms;

import org.plazmaforge.framework.core.exception.ApplicationException;

public interface EditFormController extends FormController {

    void prepareData() throws ApplicationException;

    void populateData() throws ApplicationException;

    void validateData() throws ApplicationException;
}
