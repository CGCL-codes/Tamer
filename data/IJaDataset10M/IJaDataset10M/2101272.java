package pl.xperios.rdk.client.widgets.Fields;

import com.extjs.gxt.ui.client.widget.form.TextField;
import pl.xperios.rdk.shared.validators.Validator;
import pl.xperios.rdk.shared.validators.ValidatorCondition;

/**
 *
 * @param <T>
 * @author Praca
 */
public class XTextField extends TextField<String> implements XField<String> {

    private ValidatorCondition[] validatorConditions;

    private String objectId;

    public XTextField() {
    }

    /**
     *
     * @param id
     * @param name
     * @param description
     * @param validatorConditions
     */
    public XTextField(String id, String name, String description, ValidatorCondition[] validatorConditions) {
        this.validatorConditions = validatorConditions;
        this.objectId = id;
        setFieldLabel(name);
        setId(id);
        if (description != null && !description.equals("")) {
            setToolTip(description);
        }
    }

    public XTextField(String id, String name, String description, ValidatorCondition[] validatorConditions, boolean isPassword) {
        this(id, name, description, validatorConditions);
        setPassword(isPassword);
    }

    /**
     *
     * @param id
     * @param name
     * @param description
     * @param validator
     */
    public XTextField(String id, String name, String description, Validator validator) {
        this(id, name, description, validator.getValidatorConditionsForField(id));
    }

    @Override
    protected boolean validateValue(String value) {
        forceInvalidText = null;
        String message = "";
        if (validatorConditions != null) {
            for (int i = 0; i < validatorConditions.length; i++) {
                ValidatorCondition validatorCondition = validatorConditions[i];
                if (!validatorCondition.isValid(getValue())) {
                    if (!message.equals("")) {
                        message += ";\n";
                    }
                    message += validatorCondition.getErrorLabel();
                }
            }
        }
        if (!message.equals("")) {
            forceInvalidText = message;
        }
        return super.validateValue(value);
    }

    /**
     *
     * @param validatorConditionType
     * @return
     */
    public boolean containsCondition(int validatorConditionType) {
        if (validatorConditions == null) {
            return false;
        }
        for (int i = 0; i < validatorConditions.length; i++) {
            ValidatorCondition validatorCondition = validatorConditions[i];
            if (validatorCondition.getType() == validatorConditionType) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return
     */
    public int getValidationConditionsCount() {
        if (validatorConditions == null) {
            return 0;
        }
        return validatorConditions.length;
    }

    public String getIdName() {
        return objectId;
    }

    public int getFieldWidth() {
        return 200;
    }

    public void setFieldWidth(int width) {
        setWidth(width);
    }

    public String getFieldValue() {
        return getValue();
    }

    public void setFieldValue(String get) {
        setValue(get);
    }

    public void setValidators(ValidatorCondition[] validatorConditions) {
        this.validatorConditions = validatorConditions;
    }

    public XField<String> setFieldLabelName(String label) {
        setFieldLabel(label);
        return this;
    }

    public XField<String> setIdName(String id) {
        this.objectId = id;
        setId(id);
        return this;
    }

    public XField<String> setDescription(String description) {
        if (description != null && !description.equals("")) {
            setToolTip(description);
        }
        return this;
    }

    public XField<String> setValidatorConditions(ValidatorCondition[] validatorConditions) {
        this.validatorConditions = validatorConditions;
        return this;
    }
}
