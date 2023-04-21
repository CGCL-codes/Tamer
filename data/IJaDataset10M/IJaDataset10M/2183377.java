package simpleorm.core.validators;

import simpleorm.core.SFieldMeta;
import simpleorm.core.SRecordInstance;
import simpleorm.core.SValidationException;

public class SStringArrayValidator extends SDefaultValidator {

    private static final long serialVersionUID = 1L;

    private String[] TArray = null;

    public SStringArrayValidator(String[] array) {
        TArray = array;
    }

    @Override
    public void validate(SFieldMeta field, SRecordInstance instance, boolean deferExceptions) throws SValidationException {
        if (instance.isNull(field)) return;
        boolean valid = false;
        String val = instance.getString(field);
        for (String str : TArray) {
            if (str.equals(val)) {
                valid = true;
            }
        }
        if (!valid) throw new SValidationException("Field " + field.getFieldName() + " must have a value among the String array ", val);
    }

    public String[] getValidStrings() {
        return TArray;
    }
}
