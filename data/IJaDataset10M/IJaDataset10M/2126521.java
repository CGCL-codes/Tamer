package com.genia.toolbox.web.gwt.form.client.validator.impl;

import java.util.ArrayList;
import java.util.List;
import com.genia.toolbox.web.gwt.basics.client.i18n.GwtI18nMessage;
import com.genia.toolbox.web.gwt.form.client.validator.Validator;
import com.genia.toolbox.web.gwt.form.client.validator.ValidatorCallback;

/**
 * base validation class for synchronous validator.
 */
public abstract class AbstractSynchronousValidator implements Validator {

    /**
   * validate if the item's values is acceptable.
   * 
   * @param values
   *          the values to validate.
   * @param callback
   *          the callback that will be called once the validation is done
   */
    public void validate(final List<String> values, final ValidatorCallback callback) {
        GwtI18nMessage validationError = validate(values);
        List<GwtI18nMessage> listErrors = new ArrayList<GwtI18nMessage>();
        if (validationError != null) {
            listErrors.add(validationError);
        }
        callback.validationResult(listErrors);
    }

    /**
   * validate if the item's values is acceptable.
   * 
   * @param values
   *          the values to validate.
   * @return <code>null</code> if the value if acceptable, an error message
   *         otherwise
   */
    public abstract GwtI18nMessage validate(List<String> values);
}
