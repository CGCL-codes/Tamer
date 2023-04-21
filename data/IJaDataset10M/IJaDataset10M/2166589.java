package validation;

import message.MessageId;
import org.apache.commons.lang.StringUtils;

public class ModelValidation {

    private static ModelValidation instance;

    public static ModelValidation instance() {
        if (instance == null) {
            instance = new ModelValidation();
        }
        return instance;
    }

    private void assertTrue(boolean value, MessageId errorMessageId, MessageId fieldMessageId) {
        if (!value) {
            throw new ModelValidationError(errorMessageId, fieldMessageId);
        }
    }

    public void assertNotBlank(String field, MessageId fieldMessageId) {
        assertTrue(StringUtils.isNotBlank(field), MessageId.assertNotBlank, fieldMessageId);
    }

    public void assertNotNull(Object field, MessageId fieldMessageId) {
        assertTrue(field != null, MessageId.assertNotNull, fieldMessageId);
    }

    public void assertPositive(Double value, MessageId fieldMessageId) {
        assertTrue(value != null && value > 0.0, MessageId.assertPositive, fieldMessageId);
    }

    public void assertNotNegative(Double value, MessageId fieldMessageId) {
        assertTrue(value != null && value >= 0.0, MessageId.assertNotNegative, fieldMessageId);
    }

    public void assertNotNegative(Double value, MessageId errorMessageId, String[] arguments) {
        if (value == null || value < 0.0) {
            throw new ModelValidationError(errorMessageId, arguments);
        }
    }
}
