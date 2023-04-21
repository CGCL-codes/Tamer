package net.sf.dynxform.form.data.consumer;

import net.sf.dynxform.exception.business.BusinessException;
import net.sf.dynxform.form.DataSource;
import net.sf.dynxform.form.data.ModuleType;
import net.sf.dynxform.form.data.module.SourceModule;
import net.sf.dynxform.form.data.module.helpers.ModuleValueHelper;
import net.sf.dynxform.form.schema.Field;
import net.sf.dynxform.form.validation.ValidateException;
import net.sf.dynxform.form.validation.Validator;
import net.sf.dynxform.form.validation.ValidatorFactory;

/**
 * net.sf.dynxform.form.data.consumer Mar 15, 2004 11:54:30 AM andreyp
 * Copyright (c) dynxform.sf.net. All Rights Reserved
 * 
 * @author <a href="mailto:andreyp@sf.net">andreyp</a>
 */
public final class StateConsumer extends ParameterConsumer {

    private Exception createValidateException;

    private Validator valueValidator = null;

    private final Field field;

    public StateConsumer(final Field field) {
        super(ModuleType.STATE, field.getValue(), field.getGuid(), false);
        this.field = field;
        field.getValue().setContent("");
        if (field.getValidate() != null) try {
            valueValidator = ValidatorFactory.getInstance().createValidator(field);
        } catch (ValidateException e) {
            createValidateException = e;
        }
    }

    public final boolean update(final SourceModule module, final DataSource dataSource) throws BusinessException {
        boolean isValid = true;
        final String value = ModuleValueHelper.getParameter(module, parameterName, mandatory);
        if (createValidateException != null) setErrorMessage("Can not validate a value. Validator does not exists." + createValidateException.getMessage());
        if (getValueValidator() != null) {
            try {
                setErrorMessage(null);
                getValueValidator().validate(value);
            } catch (ValidateException e) {
                setErrorMessage(e.getMessage());
                isValid = false;
            }
        }
        if (value != null) fieldValue.setContent(value); else fieldValue.setContent("");
        return isValid;
    }

    private void setErrorMessage(final String msg) {
        if (getField().getValidate() != null) getField().getValidate().setErrorMessage(msg);
    }

    public final Field getField() {
        return field;
    }

    public final Validator getValueValidator() {
        return valueValidator;
    }
}
