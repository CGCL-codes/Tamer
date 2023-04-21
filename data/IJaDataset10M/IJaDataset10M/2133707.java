package pl.xperios.rdk.client.widgets.Fields;

import com.extjs.gxt.ui.client.widget.form.DateField;
import java.util.Date;
import pl.xperios.rdk.shared.validators.Validator;
import pl.xperios.rdk.shared.validators.ValidatorCondition;

/**
 *
 * @author Praca
 */
public class XDateField extends DateField implements XField<Date> {

    private ValidatorCondition[] validatorConditions;

    private String objectId;

    /**
     *
     * @param id
     * @param name
     * @param description
     * @param validatorConditions
     */
    public XDateField(String id, String name, String description, ValidatorCondition[] validatorConditions) {
        this.validatorConditions = validatorConditions;
        this.objectId = id;
        setFieldLabel(name);
        setId(id);
        if (description != null && !description.equals("")) {
            setToolTip(description);
        }
    }

    /**
     *
     * @param id
     * @param name
     * @param description
     * @param validator
     */
    public XDateField(String id, String name, String description, Validator validator) {
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
        return 100;
    }

    public void setFieldWidth(int width) {
        setWidth(width);
    }

    public Date getFieldValue() {
        return getValue();
    }

    public void setFieldValue(Date get) {
        System.out.println("Setting for: " + getIdName() + " value: " + get);
        setValue(get);
    }

    public void setValidators(ValidatorCondition[] validatorConditions) {
        this.validatorConditions = validatorConditions;
    }

    public XField<Date> setFieldLabelName(String label) {
        setFieldLabel(label);
        return this;
    }

    public XField<Date> setIdName(String id) {
        this.objectId = id;
        setId(id);
        return this;
    }

    public XField<Date> setDescription(String description) {
        if (description != null && !description.equals("")) {
            setToolTip(description);
        }
        return this;
    }

    public XField<Date> setValidatorConditions(ValidatorCondition[] validatorConditions) {
        this.validatorConditions = validatorConditions;
        return this;
    }
}
